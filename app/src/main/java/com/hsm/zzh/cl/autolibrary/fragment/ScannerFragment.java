package com.hsm.zzh.cl.autolibrary.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.info_api.Book;
import com.hsm.zzh.cl.autolibrary.info_api.BookOperation;
import com.hsm.zzh.cl.autolibrary.info_api.Borrow_Return;
import com.hsm.zzh.cl.autolibrary.info_api.Callback;
import com.hsm.zzh.cl.autolibrary.wheel.Pending;

import java.lang.reflect.Type;
import java.util.List;

import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ScannerFragment extends Fragment implements QRCodeView.Delegate {

    private ZBarView view_scanView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        view_scanView = (ZBarView) view.findViewById(R.id.scanner_view);
        view_scanView.setDelegate(this);

        view_scanView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        view_scanView.setType(BarcodeType.ALL, null); // 识别所有类型的码
        return view;
    }

    private boolean started = false;

    public static final int TYPE_BORROW = 1;
    public static final int TYPE_BACK = 2;
    private int type = -1;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void scanStart() {
        started = true;
        view_scanView.startCamera();
        view_scanView.startSpotAndShowRect();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (started) {
            scanStart();
        }
    }

    @Override
    public void onStop() {
        if (started)
            view_scanView.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        view_scanView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        // TODO: 18-12-6  
        Log.i("扫描结果", "onScanQRCodeSuccess: " + type);
        if (type == -1) {
            Toast.makeText(getContext(), "出现未知错误", Toast.LENGTH_SHORT).show();
            finishActivityWithResult(false);
            return;
        }

        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(200);

        if (type == TYPE_BORROW) {
            final Pending pending = new Pending(getContext());
            pending.showProgressDialog();

            BookOperation.Borrow_book_new(result, new Callback() {
                @Override
                public void onSucess(List list) {
                }

                @Override
                public void onUpdateSucess() {
                    pending.closeProgressDialog();

                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle("借书成功")
                            .setMessage("借书成功，开始计费，请检查书籍是否有损坏")
                            .create();
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finishActivityWithResult(true);
                        }
                    });
                    dialog.show();
                }

                @Override
                public void onFail(Exception e) {
                    pending.closeProgressDialog();

                    BmobException be = (BmobException) e;
                    Log.e("error错误", "borrow: " + " " +
                            be.getErrorCode() + " " + be.getMessage());
                    if(be.getErrorCode() == 0){
                        Toast.makeText(getContext(), "该图书不存在，或者正在被他人使用", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "出现未知错误", Toast.LENGTH_SHORT).show();
                    }
                    finishActivityWithResult(false);
                }
            });
        } else if (type == TYPE_BACK) {
            String[] parms = result.split(";");
            if (parms.length != 2) {
                Toast.makeText(getContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                finishActivityWithResult(false);

                return;
            }

            final Pending pending = new Pending(getContext());
            pending.showProgressDialog();
            BookOperation.Return_book_new(parms[0], parms[1], new Callback() {
                @Override
                public void onSucess(List list) {
                }

                @Override
                public void onUpdateSucess() {
                    pending.closeProgressDialog();
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle("还书成功")
                            .setMessage("请查看余额检查扣款")
                            .create();
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finishActivityWithResult(true);
                        }
                    });
                    dialog.show();
                }

                @Override
                public void onFail(Exception e) {
                    pending.closeProgressDialog();
                    Toast.makeText(getContext(), "出现未知错误", Toast.LENGTH_SHORT).show();
                    BmobException be = (BmobException) e;
                    Log.e("error错误", "done: " + " " + be.getErrorCode());
                    finishActivityWithResult(false);
                }
            });
        }
    }

    private void finishActivityWithResult(boolean success) {
        Intent intent = new Intent();
        intent.putExtra("success", true);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e("错误,打开摄像头失败", "onScanQRCodeOpenCameraError: " + "扫描二维码时打开摄像头失败");
        Toast.makeText(getContext(), "错误，打开摄像头失败", Toast.LENGTH_SHORT).show();
    }
}
