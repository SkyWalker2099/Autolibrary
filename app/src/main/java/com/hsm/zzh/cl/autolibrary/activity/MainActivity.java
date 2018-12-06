package com.hsm.zzh.cl.autolibrary.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.adapter.MainViewPagerAdapter;
import com.hsm.zzh.cl.autolibrary.fragment.CommunityFragment;
import com.hsm.zzh.cl.autolibrary.fragment.MapFragment;
import com.hsm.zzh.cl.autolibrary.fragment.UserFragment;
import com.hsm.zzh.cl.autolibrary.view.MainNavItem;
import com.hsm.zzh.cl.autolibrary.view.NoScrollViewPager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NoScrollViewPager view_viewPager;
    private int NavItemNum = 3;
    private MainNavItem[] view_NavItemList = new MainNavItem[NavItemNum];
    private MainNavItem view_ScannerItem;

    private MapFragment mapFragment = new MapFragment();
    private UserFragment userFragment = new UserFragment();
    private CommunityFragment communityFragment = new CommunityFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_viewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        view_NavItemList[0] = (MainNavItem) findViewById(R.id.item_1);
        view_NavItemList[1] = (MainNavItem) findViewById(R.id.item_2);
        view_NavItemList[2] = (MainNavItem) findViewById(R.id.item_3);
        view_ScannerItem = (MainNavItem) findViewById(R.id.item_scanner);
        view_ScannerItem.setOnClickListener(this);

        view_viewPager.setNoScroll(true);
        mainViewPagerAdapter = new MainViewPagerAdapter(this.getSupportFragmentManager()
                , new Fragment[]{mapFragment, communityFragment, userFragment}
                , this);
        view_viewPager.setAdapter(mainViewPagerAdapter);
        view_viewPager.setOffscreenPageLimit(3);

        view_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pagerSelect(position);
                Log.i("位置", "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        askPermission();

        for (int i = 0; i < view_NavItemList.length; i++) {
            final int finalI = i;
            view_NavItemList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pagerSelect(finalI);
                }
            });
        }

        pagerSelect(0);
    }


    private MainViewPagerAdapter mainViewPagerAdapter;

    private void pagerSelect(int i) {
        for (MainNavItem item : view_NavItemList) {
            item.do_selected(false);
        }
        view_NavItemList[i].do_selected(true);
        view_viewPager.setCurrentItem(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_scanner: {
                final Intent intent = new Intent(this, ScannerActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void askPermission() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "android.permission.READ_PHONE_STATE",
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.CAMERA"
        };
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length <= 0)
            return;
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "必须允许所有权限才能使用本应用", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
