package com.hsm.zzh.cl.autolibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.info_api.MyUser;
import com.hsm.zzh.cl.autolibrary.info_api.UserOperation;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity{

    private Toolbar viewToolbar;
    private EditText viewInputAccount;
    private EditText viewInputEmail;
    private EditText viewInputPwd;
    private EditText viewInputPwd2;
    private Button viewBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(viewToolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        viewInputAccount = (EditText) findViewById(R.id.account);
        viewInputEmail = (EditText) findViewById(R.id.email);
        viewInputPwd = (EditText) findViewById(R.id.pwd);
        viewInputPwd2 = (EditText) findViewById(R.id.pwd2);
        viewBtnRegister = (Button) findViewById(R.id.register);

        viewBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register(){
        String account = viewInputAccount.getText().toString();
        String email = viewInputEmail.getText().toString();
        String pwd = viewInputPwd.getText().toString();
        String pwd2 = viewInputPwd2.getText().toString();

        if(account.isEmpty() || email.isEmpty()||pwd.isEmpty()||pwd2.isEmpty()){
            Toast.makeText(this, "请将注册信息填写完整", Toast.LENGTH_SHORT).show();
            return ;
        }

        if(!pwd.equals(pwd2)){
            Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
        }

        UserOperation.sign_up(account, pwd, email, new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e!=null){
                    String message = null;
                    switch(e.getErrorCode()) {
                        case 301: {
                            message = "请填写正确的邮箱";
                            break;
                        }

                        case 202:{
                            message = "该用户名已被使用";
                            break;
                        }
                        default: {
                            Log.e("错误", "用户注册失败： " + e.getMessage() + " " + e.getErrorCode());
                            message = "出现未知错误";
                        }
                    }
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    return ;
                }

                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                //返回成功信息给上一个页面
                Intent intent = new Intent();
                intent.putExtra("register_result", true);
                setResult(RESULT_OK, intent);

                RegisterActivity.this.finish();
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
