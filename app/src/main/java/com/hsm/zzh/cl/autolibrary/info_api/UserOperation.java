package com.hsm.zzh.cl.autolibrary.info_api;

import android.util.Log;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserOperation {


    /** 注册一个账号
     * @param username   用户名
     * @param password  密码
     * @param email   email
     * @param saveListener
     */
    public static void sign_up(String username, String password, String email, SaveListener<MyUser> saveListener){
        MyUser myUser = new MyUser();
        myUser.setUsername(username);
        myUser.setPassword(password);
        myUser.setEmail(email);
        myUser.signUp(saveListener);

    }


    /** 登陆
     * @param username  用户名
     * @param password  密码
     * @param saveListener
     */
    public static void sign_in(String username, String password, SaveListener<MyUser> saveListener){


        MyUser myUser = new MyUser();
        myUser.setUsername(username);
        myUser.setPassword(password);
        myUser.login(saveListener);

    }


    /**
     * 退出
     */
    public static void login_out(){

        BmobUser.logOut();

    }



    /** 将老密码修改为新密码
     * @param old_password
     * @param new_password
     * @param updateListener
     */
    public static void change_password(String old_password, String new_password, UpdateListener updateListener){

        final String TAG = "change_word";
        BmobUser.updateCurrentUserPassword(old_password,new_password,updateListener);

    }





    /** 对一些简单的信息的修改
     * @param nickname
     * @param sex
     * @param updateListener
     */
    public static void change_user_info_normal(String nickname, boolean sex, UpdateListener updateListener){

        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        myUser.setNick(nickname);
        myUser.setSex(sex);
        myUser.update(updateListener);

    }

    /**
     * @param nickname  修改昵称
     * @param bmobFile  修改头像文件
     * @param sex  性改性别
     * @param callback
     */
    public static void change_user_info1(final String nickname, final BmobFile bmobFile, final boolean sex, final Callback callback){

        //头像有修改
        if (bmobFile != null) {
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
                        myUser.setHead_image(bmobFile);
                        myUser.setNick(nickname);
                        myUser.setSex(sex);
                        myUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    callback.onUpdateSucess();
                                } else {
                                    Log.d("更改头像步骤2", "done: 失败");
                                    callback.onFail(e);
                                }
                            }
                        });
                    } else {
                        callback.onFail(e);
                        Log.d("更改头像步骤1", "done: 失败");
                    }
                }
            });
        } else {
            //头像无修改
            change_user_info_normal(nickname, sex, new UpdateListener() {
                @Override
                public void done(BmobException e) {

                    if (e != null) {
                        Log.d("无头像修改", e.getMessage());
                        callback.onFail(e);
                    } else {
                        callback.onUpdateSucess();
                    }

                }
            });
        }

    }





    /** 获取当前用户的借书历史
     * @param findListener
     */
    public static void get_user_borrow_history(FindListener<Book> findListener){

        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        if(myUser == null){
            Log.d("MyUser_null", "get_user_borrow_history: ");
        }else {
            Log.d("MyUser", "get_user_borrow_history: "+myUser.repr());
        }

        BmobQuery<Book> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.addWhereRelatedTo("borrow_history", new BmobPointer(myUser));
        query.findObjects(findListener);

    }


    /**  充值的接口
     * @param num  充值数额
     * @param callback  回调接口
     */
    public static void recharge(int num, final Callback callback){

        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        myUser.setAccount_balance(myUser.getAccount_balance()+num);
        myUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    callback.onUpdateSucess();
                }else {
                    callback.onFail(e);
                }
            }
        });

    }


}
