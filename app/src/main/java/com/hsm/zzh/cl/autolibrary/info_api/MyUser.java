package com.hsm.zzh.cl.autolibrary.info_api;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MyUser extends BmobUser{

    private String nick; //昵称

    private boolean sex;

    private BmobFile head_image;  //头像文件

    private List<Borrow_history> borrow_histories; //借书历史

    public List<Borrow_history> getBorrow_histories() {
        return borrow_histories;
    }

    public void setBorrow_histories(List<Borrow_history> borrow_histories) {
        this.borrow_histories = borrow_histories;
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
        return super.getEmail()+" , "+super.getUsername();
    }

    public static class Borrow_history{
        public String title;
        public int book_id;
        public String borrow_date;
        public boolean return_yet = false;

        public Borrow_history(String title, int id, String date, Boolean yet){
            this.title = title;
            this.book_id = id;
            this.borrow_date = date;
            this.return_yet = yet;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getBook_id() {
            return book_id;
        }

        public void setBook_id(int book_id) {
            this.book_id = book_id;
        }

        public String getBorrow_date() {
            return borrow_date;
        }

        public void setBorrow_date(String borrow_date) {
            this.borrow_date = borrow_date;
        }

        public boolean isReturn_yet() {
            return return_yet;
        }

        public void setReturn_yet(boolean return_yet) {
            this.return_yet = return_yet;
        }
    }



}


