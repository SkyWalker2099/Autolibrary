package com.hsm.zzh.cl.autolibrary.fragment;

import android.app.Service;
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

import com.hsm.zzh.cl.autolibrary.activity.BorrowActivity;
import com.hsm.zzh.cl.autolibrary.R;

import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class ScannerFragment extends Fragment implements QRCodeView.Delegate{

    private ZBarView view_scanView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        view_scanView =  (ZBarView) view.findViewById(R.id.scanner_view);
        view_scanView.setDelegate(this);

        view_scanView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        view_scanView.setType(BarcodeType.ALL, null); // 识别所有类型的码
        return view;
    }

    private boolean started = false;
    
    public static final int TYPE_BORROW = 1;
    public static final int TYPE_BACK = 2;
    private int type = -1;
    
    public void setType(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }
    
    public void scanStart(){
        started = true;
        view_scanView.startCamera();
        view_scanView.startSpotAndShowRect();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        if(started){
            scanStart();
        }
    }

    @Override
    public void onStop() {
        if(started)
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
        Log.i("扫描结果", "onScanQRCodeSuccess: "+type);

        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(200);

        Intent intent = new Intent(getActivity(), BorrowActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e("错误,打开摄像头失败", "onScanQRCodeOpenCameraError: "+"扫描二维码时打开摄像头失败");
        Toast.makeText(getContext(), "错误，打开摄像头失败", Toast.LENGTH_SHORT).show();
    }
}
