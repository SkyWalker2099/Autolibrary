package com.hsm.zzh.cl.autolibrary.info_api;

import com.google.gson.annotations.SerializedName;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Book extends BmobObject{

    @SerializedName("id")
    String id = "";                             // 书的id  一般用不上 因为bmob自带 objectid

    @SerializedName("title")
    String title = "";                          // 名称

    @SerializedName("author")
    String author = "";                           //作者

    @SerializedName("publishing_house")
    String publish_house;                           // 出版社

    @SerializedName("publish_date")
    String publish_date;                    //出版日期

    @SerializedName("ISBN")
    String ISBN;                            //ISBN 号码

    @SerializedName("pic_url")
    String pic_url;                   // 封面的url

    @SerializedName("type")
    String type;                  // 类型

    @SerializedName("tag")
    String tag;                      //标签

    @SerializedName("shortdesc")
    String shortdesc;                   // 简介

    private MyUser now_user;                 //当前用户

    private BmobRelation history_user;     // 历史用户

    private Machine now_machine;        //   当前机器

    private BmobRelation history_machine;   //历史保存在的机器

    public Book(String id, String title, String author, String pic_url, String type, String shortdesc) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.pic_url = pic_url;
        this.type = type;
        this.shortdesc = shortdesc;
    }

    public MyUser getNow_user() {
        return now_user;
    }

    public void setNow_user(MyUser now_user) {
        this.now_user = now_user;
    }

    public BmobRelation getHistory_user() {
        return history_user;
    }

    public void setHistory_user(BmobRelation history_user) {
        this.history_user = history_user;
    }

    public Machine getNow_machine() {
        return now_machine;
    }

    public void setNow_machine(Machine now_machine) {
        this.now_machine = now_machine;
    }

    public BmobRelation getHistory_machine() {
        return history_machine;
    }

    public void setHistory_machine(BmobRelation history_machine) {
        this.history_machine = history_machine;
    }

    private Boolean isBorrowed = false;

    public Boolean getBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(Boolean borrowed) {
        isBorrowed = borrowed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublish_house() {
        return publish_house;
    }

    public void setPublish_house(String publish_house) {
        this.publish_house = publish_house;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String repr(){
        return id+","+title+","+author;
    }
}
