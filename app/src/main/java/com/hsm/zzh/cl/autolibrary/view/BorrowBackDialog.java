package com.hsm.zzh.cl.autolibrary.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.hsm.zzh.cl.autolibrary.R;

public class BorrowBackDialog extends Dialog {
    public BorrowBackDialog(@NonNull Context context) {
        super(context);
    }

    public BorrowBackDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BorrowBackDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private Button viewBorrow;
    private Button viewBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_borrow_back);
        Log.i("测试dialog", "onCreate: "+"lala");

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        viewBorrow = (Button) findViewById(R.id.borrow);
        viewBack = (Button) findViewById(R.id.back);

        viewBorrow.setOnClickListener(btnBorrowListener);
        viewBack.setOnClickListener(btnBackListener);
    }

    private View.OnClickListener btnBorrowListener;
    private View.OnClickListener btnBackListener;

    public void setBtnBorrowListener(View.OnClickListener listener){
        this.btnBorrowListener = listener;
    }

    public void setBtnBackListener(View.OnClickListener listener){
        this.btnBackListener = listener;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        doOnBackPressed.onBackPressed();
    }

    private DoOnBackPressed doOnBackPressed;
    public void setDoOnBackPressed(DoOnBackPressed doOnBackPressed){
        this.doOnBackPressed = doOnBackPressed;
    }
    public interface DoOnBackPressed{
        void onBackPressed();
    }
}
