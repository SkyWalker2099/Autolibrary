package com.hsm.zzh.cl.autolibrary.info_api;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    public static void change_password(String old_password, String new_password,UpdateListener updateListener){

        final String TAG = "change_word";
        BmobUser.updateCurrentUserPassword(old_password,new_password,updateListener);

    }



    /** 对一些简单的信息的修改
     * @param username
     * @param head_image
     * @param sex
     * @param updateListener
     */
    public static void change_user_info(String username, BmobFile head_image, boolean sex, UpdateListener updateListener){

        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        myUser.setUsername(username);
        myUser.setSex(sex);
        myUser.setHead_image(head_image);
        myUser.update(updateListener);

    }

    /** 获取当前用户的借书历史
     * @param findListener
     */
    public static void get_user_borrow_history(FindListener<Book> findListener){

        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<Book> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.addWhereRelatedTo("history_user", new BmobPointer(myUser));
        query.findObjects(findListener);

    }



}
