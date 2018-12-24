package com.hsm.zzh.cl.autolibrary.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hsm.zzh.cl.autolibrary.R;

import cn.bmob.v3.Bmob;

public class SplashActivity extends AppCompatActivity {

    private ImageView viewImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewImageView = (ImageView) findViewById(R.id.background);
        Glide.with(this).load(R.mipmap.splash).into(viewImageView);

        Bmob.initialize(this, "984af1c7e81a96a2afb05e32a83653c6");
        if(askPermission()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, 150);
        }


    }

    private boolean askPermission() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "android.permission.READ_PHONE_STATE",
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.CAMERA"
        };
        boolean permission_got = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
                permission_got = false;
                break;
            }
        }
        return permission_got;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length <= 0)
            return;
        Log.i("权限", "onRequestPermissionsResult: "+ grantResults.length);
        switch (requestCode) {
            case 1: {
                boolean bj = true;
                for(int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        bj = false;
                    }
                }
                if(bj){
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, "必须允许所有权限才能使用本应用", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
            }
        }
    }
}
