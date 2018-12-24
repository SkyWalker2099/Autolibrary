package com.hsm.zzh.cl.autolibrary.info_api;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class BookOperation {
    static String TAG = "BookOperation";


    /** 借书
     * @param book_id 要借的书籍objectid，
     * @param updateListener
     */
    public static void Borrow_book(String book_id, final UpdateListener updateListener){
        final MyUser myUser = BmobUser.getCurrentUser(MyUser.class);


//        Book book = new Book();


        BmobQuery<Book> query= new BmobQuery<>();
        query.addWhereDoesNotExists("now_user");
        query.addWhereExists("now_machine");
        query.addWhereEqualTo("objectId", book_id);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(new FindListener<Book>() {

            @Override
            public void done(List<Book> list, BmobException e) {
                if(e != null || list.size() == 0){
                    Book book = new Book();
                    book.setObjectId("");
                    Borrow_Return.borrow(book, myUser,updateListener);
                }else {
                    Borrow_Return.borrow(list.get(0), myUser, updateListener);
                }

            }
        });

    }


    /**     新的借书接口，使用的Callback接口，建议用这个
     * @param book_id   书本objectid
     * @param callback  回调函数接口
     */
    public static void Borrow_book_new(String book_id, final Callback callback){

        final MyUser myUser = BmobUser.getCurrentUser(MyUser.class);

        BmobQuery<Book> query = new BmobQuery<>();
        query.addWhereDoesNotExists("now_user");
        query.addWhereExists("now_machine");
        query.addWhereEqualTo("objectId", book_id);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(new FindListener<Book>() {

            @Override
            public void done(List<Book> list, BmobException e) {
                if(e != null || list.size() == 0){
//                    Book book = new Book();
//                    book.setObjectId("");
//                    Borrow_Return.borrow(book, myUser,updateListener);
                    if(e == null){
                        e = new BmobException();
                    }
                    callback.onFail(e);
                }else {

                    Borrow_Return.borrow1(list.get(0), myUser, callback);
                }

            }
        });

    }


    /** 还书操作
     * @param book_id   书的 objectid
     * @param machine_id  要还进去的机器的 objectid
     * @param updateListener
     */
    public static void Return_book(String book_id, final String machine_id, final UpdateListener updateListener){

        BmobQuery<Book> query = new BmobQuery<>();
        query.addWhereExists("now_user");
        query.addWhereDoesNotExists("now_machine");
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.getObject(book_id, new QueryListener<Book>() {
            @Override
            public void done(Book book, BmobException e) {
                if(e != null || book == null){
                    book = new Book();
                    book.setObjectId("");
                }
                Borrow_Return.Return(machine_id, book, updateListener);
            }
        });

    }


    /**   还书的新接口，建议用这个
     * @param book_id 书本objectid
     * @param machine_id   机器objectid
     * @param callback  回调函数
     */
    public static void Return_book_new(String book_id, final String machine_id, final Callback callback){

        BmobQuery<Book> query = new BmobQuery<>();
        query.addWhereExists("now_user");
        query.addWhereDoesNotExists("now_machine");
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.getObject(book_id, new QueryListener<Book>() {
            @Override
            public void done(Book book, BmobException e) {
                if(e != null || book == null){
                     if(e == null){
                         e = new BmobException();
                     }
                     callback.onFail(e);
                }else {
                    Borrow_Return.Return1(machine_id,book,callback);
                }

            }
        });

    }





    /** 获取一个机器里的书
     * @param machine_id   机器 objectid
     * @param findListener
     */
    public static void Get_books_by_machine(String machine_id, FindListener<Book> findListener){

        BmobQuery<Book> query = new BmobQuery<>();
        Machine machine = new Machine();
        machine.setObjectId(machine_id);
        query.addWhereEqualTo("now_machine",new BmobPointer(machine));
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(findListener);

    }   //  一个机器里的书籍






    /** 按位置附近 0.5 km 内机器里的书籍
     * @param bmobGeoPoint 一个地理位置类
     * @param sqlQueryListener  这里使用的是 bql 查询
     */
    public static void Books_by_your_location(BmobGeoPoint bmobGeoPoint, SQLQueryListener<Book> sqlQueryListener){

        String bql = "select * from Book where now_machine in (select * from Machine where location near ["+ Double.toString(bmobGeoPoint.getLongitude())+","+ Double.toString(bmobGeoPoint.getLatitude())+"] max 1  km)";
        BmobQuery<Book> query = new BmobQuery<>();
        query.setSQL(bql);
        query.setPreparedParams(new Object[]{bmobGeoPoint.getLatitude(), bmobGeoPoint.getLongitude()});
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.doSQLQuery(sqlQueryListener);

    }


    /** 搜寻一个关键词，图书的一些基本信息如果和这个关键词一直则被搜寻出来；
     * @param string 搜寻的关键词
     * @param findListener
     */
    public static void Books_by_search(String string, FindListener<Book> findListener){

        BmobQuery<Book> query = new BmobQuery<>();
        query.addWhereEqualTo("title",string);

        BmobQuery<Book> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("author",string);

        BmobQuery<Book> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("type",string);

        BmobQuery<Book> query3 = new BmobQuery<>();
        query3.addWhereEqualTo("tag", string);

        List<BmobQuery<Book>> queries = Arrays.asList(query,query1,query2,query3);

        BmobQuery<Book> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        mainQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        mainQuery.findObjects(findListener);

    }




}
