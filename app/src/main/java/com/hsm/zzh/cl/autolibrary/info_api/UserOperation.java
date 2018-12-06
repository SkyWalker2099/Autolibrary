package com.hsm.zzh.cl.autolibrary.info_api;

import android.util.Log;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserOperation {

    public static void sign_up(String username, String password, String email){
        MyUser myUser = new MyUser();
        myUser.setUsername(username);
        myUser.setPassword(password);
        myUser.setEmail(email);

        myUser.signUp(new SaveListener<MyUser>() {

            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e == null){
                    Log.d("sign up", "done: sucesssssssssssssssss"+myUser.repr());
                }else {
                    Log.d("sign up", "done: failed"+ e.getErrorCode()+",  "+e.getMessage());
                }
            }
        });

    }

    public static void sign_in(String username, String password){

        MyUser myUser = new MyUser();
        myUser.setUsername(username);
        myUser.setPassword(password);

        myUser.login(new SaveListener<MyUser>() {

            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e != null){
                    Log.d("error", "done: "+e.getErrorCode()+",   "+e.getMessage());
                }else {
                    Log.d("sucess", "done: 登陆成功");
                }
            }
        });

    }

    public static void login_out(){

        BmobUser.logOut();
        BmobUser currentUser = BmobUser.getCurrentUser();

    }

    public static void change_password(String old_password, String new_password){
        final String TAG = "change_word";
        BmobUser.updateCurrentUserPassword(old_password, new_password, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.d(TAG, "done: "+"sucess"+e.getMessage());
                }else {
                    Log.d(TAG, "done: "+"fail"+e.getMessage());
                }
            }
        });

    }




}
