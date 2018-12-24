package com.hsm.zzh.cl.autolibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.info_api.MyUser;
import com.hsm.zzh.cl.autolibrary.info_api.UserOperation;
import com.hsm.zzh.cl.autolibrary.wheel.Pending;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    private Toolbar viewToolbar;
    private Button viewBtnLogin;
    private EditText viewEditAccount;
    private EditText viewEditPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(viewToolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        viewBtnLogin = (Button) findViewById(R.id.login);
        viewEditAccount = (EditText) findViewById(R.id.account);
        viewEditPwd = (EditText) findViewById(R.id.pwd);

        viewBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        // TODO: 18-12-8  
        String account = viewEditAccount.getText().toString();
        String pwd = viewEditPwd.getText().toString();
        if(account.equals("") || pwd.equals(""))
            return ;

        final Pending pending = new Pending(this);
        pending.showProgressDialog();

        UserOperation.sign_in(account, pwd, new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e != null){
                    if(e.getErrorCode()==101){
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT)
                                .show();
                    }else{
                        Toast.makeText(LoginActivity.this, "发生位未知错误", Toast.LENGTH_SHORT)
                                .show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //返回成功信息给上一个页面
                    Intent intent = new Intent();
                    intent.putExtra("login_result", true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                pending.closeProgressDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                break;
            }
        }
        return true;
    }
}
