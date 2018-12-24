package com.hsm.zzh.cl.autolibrary.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;


import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.adapter.BookListViewAdapter;
import com.hsm.zzh.cl.autolibrary.adapter.SpaceItemDecoration;
import com.hsm.zzh.cl.autolibrary.info_api.Book;
import com.hsm.zzh.cl.autolibrary.info_api.BookOperation;
import com.hsm.zzh.cl.autolibrary.wheel.Pending;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BookListActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private RecyclerView recyclerView;
    private BookListViewAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklist);
        toolBar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("图书列表");
        }

        getBookByMachine(getIntent().getStringExtra("machine_id"));
        recyclerView = (RecyclerView) findViewById(R.id.book_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        bookAdapter = new BookListViewAdapter(bookList, this, null, null);
        recyclerView.addItemDecoration(new SpaceItemDecoration(5));
        recyclerView.setAdapter(bookAdapter);

    }

    private Pending pending;
    private void getBookByMachine(String machine_id) {
        pending = new Pending(this);
        pending.showProgressDialog();

        BookOperation.Get_books_by_machine(machine_id, new FindListener<Book>() {
            @Override
            public void done(List<Book> list, BmobException e) {
                if (e == null) {
                    bookList.clear();
                    for (int i=0;i<list.size();i++) {

                        bookList.add(list.get(i));
                        Log.d("book_title ",bookList.get(i).getTitle());
                    }
                    bookAdapter.notifyDataSetChanged();
                    Log.d("getBookByMach_success:", Integer.toString(bookList.size()));
                } else {
                    e.printStackTrace();
                    Log.d("getBookByMachine fail ", "0");
                }
                pending.closeProgressDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
