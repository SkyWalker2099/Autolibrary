package com.hsm.zzh.cl.autolibrary.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.fragment.ScannerFragment;
import com.hsm.zzh.cl.autolibrary.view.BorrowBackDialog;

public class ScannerActivity extends AppCompatActivity {

    private ScannerFragment scanFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        scanFragment = (ScannerFragment) getSupportFragmentManager().findFragmentById(R.id.scanner);

        final BorrowBackDialog dialog = new BorrowBackDialog(this, R.style.BorrowBackTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setBtnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFragment.setType(ScannerFragment.TYPE_BACK);
                scanFragment.scanStart();
                dialog.dismiss();
            }
        });
        dialog.setBtnBorrowListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFragment.setType(ScannerFragment.TYPE_BORROW);
                scanFragment.scanStart();
                dialog.dismiss();
            }
        });
        dialog.setDoOnBackPressed(new BorrowBackDialog.DoOnBackPressed() {
            @Override
            public void onBackPressed() {
                finish();
            }
        });
        dialog.show();
    }

}
