package com.hsm.zzh.cl.autolibrary.info_api;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class Borrow_Return {

    static String TAG = "Borrow_Return";

    public static void borrow(final Book book,final MyUser myUser,UpdateListener updateListener){
        book.setNow_user(myUser);
        book.remove("now_machine");
        BmobRelation relation = new BmobRelation();
        relation.add(myUser);
        book.setHistory_user(relation);
//        Log.d(TAG, "borrow: "+book.getObjectId()+" , "+myUser.repr());
        book.update(book.getObjectId(), updateListener);


    }


    public static void borrow1(final Book book, final MyUser myUser, final Callback callback){

        BmobRelation relation = new BmobRelation();
        relation.add(book);
        myUser.setBorrow_history(relation);
        myUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if( e != null){
                    callback.onFail(e);
                }else {
                    book.setNow_user(myUser);
                    book.remove("now_machine");
                    book.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if( e != null){
                                callback.onFail(e);
                            }else {
                                callback.onUpdateSucess();
                            }
                        }
                    });
                }
            }
        });

    }


    public static void Return(String machine_id, Book book , UpdateListener updateListener){
        Machine machine = new Machine();
        machine.setObjectId(machine_id);
        BmobRelation relation = new BmobRelation();
        relation.add(machine);
        book.setHistory_machine(relation);
        book.setNow_machine(machine);
        book.remove("now_user");
        book.update(book.getObjectId(),updateListener);

    }


    public static void Return1(String machine_id, final Book book, final Callback callback){
        Machine machine = new Machine();
        machine.setObjectId(machine_id);
        BmobRelation relation = new BmobRelation();
        relation.add(machine);

        final String update_date = book.getUpdatedAt();

        book.setHistory_machine(relation);
        book.setNow_machine(machine);
        book.remove("now_user");
        book.update(book.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    callback.onUpdateSucess();
                    MyUser myUser = BmobUser.getCurrentUser(MyUser.class);

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    try{
                        Date date = format.parse(update_date);
                        Date date1 = new Date(System.currentTimeMillis());
                        final long days = (date1.getTime()-date.getTime())/(1000*60*60*24);
                        myUser.setAccount_balance((int)(myUser.getAccount_balance() - days/7 - 1));
                        myUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        Log.d(TAG, "Return1: 借了几天后还回 "+days);
                    }catch (ParseException e1){
                        final long days = 0;
                        Log.d(TAG, "Return1: parse 失败");
                    }




                }else {
                    callback.onFail(e);
                }
            }
        });
    }



}
