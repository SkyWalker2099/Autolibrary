package com.hsm.zzh.cl.autolibrary.info_api;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;

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

    public static void Return(String machine_id,Book book , UpdateListener updateListener){
        Machine machine = new Machine();
        machine.setObjectId(machine_id);
        BmobRelation relation = new BmobRelation();
        relation.add(machine);
        book.setHistory_machine(relation);
        book.setNow_machine(machine);
        book.remove("now_user");
        book.update(book.getObjectId(),updateListener);

    }



}
