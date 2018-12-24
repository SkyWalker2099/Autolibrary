package com.hsm.zzh.cl.autolibrary.info_api;

import java.util.List;

public interface Callback <T>{

    /**
     * @param list  用在查询类的操作上，list为返回的值
     */
    public void onSucess(List<T> list);

    /**
     *  onUpdateSucess（） 主要是用在非查询类的操作
     */
    public void onUpdateSucess();


    /**  操作出错
     * @param e
     */
    public void onFail(Exception e);

}
