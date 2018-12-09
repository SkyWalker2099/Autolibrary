package com.hsm.zzh.cl.autolibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hsm.zzh.cl.autolibrary.R;

public class MachineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        Intent intent = getIntent();
        Log.i("machine", "onCreate: "+intent.getIntExtra("machine_id", -1));
    }
}
