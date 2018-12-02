package com.hsm.zzh.cl.autolibrary.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.hsm.zzh.cl.autolibrary.R;


public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragmentList;
    private Context context;

    public MainViewPagerAdapter(FragmentManager fm, Fragment[] fragmentList, Context context) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList[position];
    }

    @Override
    public int getCount() {
        return fragmentList.length;
    }

    private int[] imageIds = {R.mipmap.ic_launcher
            , R.mipmap.ic_launcher
            , R.mipmap.ic_launcher};

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable drawable = ContextCompat.getDrawable(context, imageIds[position]);
        Log.i("图片", "getPageTitle: "+drawable.toString());
//        drawable.setBounds(0,0,drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.setBounds(0,0,5,5);
        ImageSpan imagespan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        SpannableString spannableString = new SpannableString(" "+"lala");
        spannableString.setSpan(imagespan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
