package com.hsm.zzh.cl.autolibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hsm.zzh.cl.autolibrary.R;


public class MainNavItem extends LinearLayout {
    public MainNavItem(Context context) {
        super(context);
        initView(context);
    }


    public MainNavItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initTypeArray(context, attrs);
    }

    public MainNavItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initTypeArray(context, attrs);

    }

    public MainNavItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
        initTypeArray(context, attrs);
    }

    private ImageView imageView;
    private TextView textView;

    private int selectedImageId;
    private int noSelectedImageId;

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_main_nav_item,
                this, true);
        imageView = (ImageView) findViewById(R.id.select_image);
        textView = (TextView) findViewById(R.id.select_text);

    }

    private void initTypeArray(Context context, AttributeSet attrs) {
        @SuppressLint("Recycle") TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainNavItem);
        selectedImageId = typedArray.getResourceId(R.styleable.MainNavItem_selected_image,
                R.drawable.ic_launcher_background);
        noSelectedImageId = typedArray.getResourceId(R.styleable.MainNavItem_no_selected_image,
                R.drawable.ic_launcher_background);
        textView.setText(typedArray.getString(R.styleable.MainNavItem_hint_text));

        boolean selected = typedArray.getBoolean(R.styleable.MainNavItem_selected, false);
        do_selected(selected);
    }

    public void do_selected(boolean t){
        if(t)
            imageView.setImageResource(selectedImageId);
        else
            imageView.setImageResource(noSelectedImageId);
    }

}
