package com.hsm.zzh.cl.autolibrary.info_api;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class MyUser extends BmobUser {

    private String nick; //昵称

    private boolean sex;

    private BmobFile head_image;  //头像文件

    private Integer account_balance = 0;

    private BmobRelation borrow_history;

    public BmobRelation getBorrow_history() {
        return borrow_history;
    }

    public void setBorrow_history(BmobRelation borrow_history) {
        this.borrow_history = borrow_history;
    }

    public Integer getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(Integer account_balance) {
        this.account_balance = account_balance;
    }


    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public BmobFile getHead_image() {
        return head_image;
    }

    public void setHead_image(BmobFile head_image) {
        this.head_image = head_image;
    }

    public String repr(){
        return super.getEmail()+" , "+super.getUsername()+","+getObjectId();
    }





}


