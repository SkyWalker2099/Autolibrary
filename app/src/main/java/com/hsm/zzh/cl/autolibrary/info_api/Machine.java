package com.hsm.zzh.cl.autolibrary.info_api;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class Machine extends BmobObject{

    private BmobGeoPoint location = new BmobGeoPoint(); // 地理位置

    private int id;  //机器id

    private String shortdesc;  // 简单的介绍

    public Machine(BmobGeoPoint location, int id, String shortdesc) {
        this.location = location;
        this.id = id;
        this.shortdesc = shortdesc;
    }

    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String repr(){
        return Double.toString(this.location.getLatitude()) +","+ Double.toString(this.location.getLongitude())+ ",id:"+Integer.toString(id)+" ,"+shortdesc;

    }
}
