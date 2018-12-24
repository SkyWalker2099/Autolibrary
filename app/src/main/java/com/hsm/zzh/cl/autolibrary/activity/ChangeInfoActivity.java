package com.hsm.zzh.cl.autolibrary.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import com.bumptech.glide.request.RequestOptions;
import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.info_api.Callback;
import com.hsm.zzh.cl.autolibrary.info_api.MyUser;
import com.hsm.zzh.cl.autolibrary.info_api.UserOperation;
import com.hsm.zzh.cl.autolibrary.wheel.BasePopupWindow;
import com.hsm.zzh.cl.autolibrary.wheel.getPhotoFromAlbum;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangeInfoActivity extends AppCompatActivity {

    String photoPath = null;
    private File cameraSavePath;
    private Uri uri;
    private String[] sexArray = {"boy", "girl", "else"};
    boolean sex;

    private Context mContext;

    private Toolbar toolbar;

    private TextView nickName;
    private TextView Sex;
    private ImageView headImage;
    private RelativeLayout historyLayout;

    private MyUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        toolbar = (Toolbar) findViewById(R.id.change_info_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");

        }

        mContext = ChangeInfoActivity.this;
        currentUser = BmobUser.getCurrentUser(MyUser.class);
        nickName = (TextView) findViewById(R.id.nickname_edit);
        headImage = (ImageView) findViewById(R.id.head_image);
        Sex = (TextView) findViewById(R.id.sex);
        historyLayout = (RelativeLayout) findViewById(R.id.history_layout);


        //加载信息
        initInfo();

        nickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });

        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ChangeInfo", "headImage click");
                initPopupWindow(v.getRootView());
            }
        });

        Sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseSexDialog();
            }
        });

        historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeInfoActivity.this, BorrowHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initInfo() {
        nickName.setText(currentUser.getNick());

        sex = currentUser.isSex();
        if (sex) {
            Sex.setText("男");
        } else {
            Sex.setText("女");
        }

        if (currentUser.getHead_image() != null) {
            Glide.with(mContext).load(currentUser.getHead_image().getFileUrl()).into(headImage);
        }else{
            Glide.with(mContext).load(R.drawable.user).into(headImage);
        }
    }

    private void showEditDialog() {

        final EditText editText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入昵称")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nickName.setText(editText.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void initPopupWindow(View v) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_choose_image, null, false);
        Button btn_take_photo = (Button) view.findViewById(R.id.take_photo);
        Button btn_open_album = (Button) view.findViewById(R.id.choose_from_album);
        final BasePopupWindow popupWindow = new BasePopupWindow(mContext);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        //popupWindow.showAsDropDown(v, 0, 0);
        popupWindow.showAtLocation(v,Gravity.BOTTOM,0,0);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("ChangeInfo", "take photo");
//                goCamera();
                popupWindow.dismiss();
            }
        });
        btn_open_album.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("ChangeInfo", "open album");
                openAlbum();
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 打开相机
     */
    private void goCamera() {
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() +
                "/" + System.currentTimeMillis() + ".jpg");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //sdk always >=24
        uri = FileProvider.getUriForFile(ChangeInfoActivity.this,
                "com.hsm.zzh.cl.autolibrary_he.fileprovider", cameraSavePath);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ChangeInfoActivity.this.startActivityForResult(cameraIntent, 1);
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent albumIntent = new Intent();
        albumIntent.setAction(Intent.ACTION_PICK);
        albumIntent.setType("image/*");
        startActivityForResult(albumIntent, 2);
    }

    /**
     * 加载图片
     * @degree 图片旋转角度
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        float degree = 0f;

        if (requestCode == 1 && resultCode == RESULT_OK) {
            photoPath = String.valueOf(cameraSavePath);
            Log.d("拍照返回图片路径:", photoPath);
            //获取图片旋转角度
            //degree = getBitmapDegree(photoPath);
            //Log.d("degree", Float.toString(degree));
            Glide.with(ChangeInfoActivity.this).load(photoPath).into(headImage);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = getPhotoFromAlbum.getRealPathFromUri(this, data.getData());
            //degree = getBitmapDegree(photoPath);
            //Log.d("degree", Float.toString(degree));
            Glide.with(ChangeInfoActivity.this).load(photoPath).into(headImage);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     *
     * @param path 图片绝对路径
     * @return 旋转角度
     */
    private float getBitmapDegree(String path) {
        float degree = 0f;
        try {
            //获取图片EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            //获取角度
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 选择性别Dialog
     */
    private void showChooseSexDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(sexArray, 2,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Sex.setText(sexArray[which]);
                        if ("boy".equals(sexArray[which])) {
                            sex = true;
                        } else {
                            sex = false;
                        }
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void updateInfo(){
        BmobFile bmobFile = null;
        if (photoPath != null) {
            bmobFile = new BmobFile(new File(photoPath));
        }

        UserOperation.change_user_info1(nickName.getText().toString(), bmobFile, sex, new Callback() {
            @Override
            public void onSucess(List list) {

            }

            @Override
            public void onUpdateSucess() {
                Log.d("update", "success");
            }

            @Override
            public void onFail(Exception e) {
                Log.d("update", "fail");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        updateInfo();
        super.onBackPressed();
    }
}
