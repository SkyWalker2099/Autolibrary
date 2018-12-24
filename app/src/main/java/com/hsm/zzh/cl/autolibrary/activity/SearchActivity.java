package com.hsm.zzh.cl.autolibrary.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.adapter.BookListViewAdapter;
import com.hsm.zzh.cl.autolibrary.info_api.Book;
import com.hsm.zzh.cl.autolibrary.info_api.BookOperation;
import com.hsm.zzh.cl.autolibrary.wheel.Pending;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText viewSearchEdit;
    private ImageView viewSearchImage;
    private RecyclerView viewSearchList;
    private Toolbar viewToolBar;

    private BookListViewAdapter mAdapter;
    private List<Book> bookList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        viewSearchEdit = (EditText) findViewById(R.id.search);
        viewSearchList = (RecyclerView) findViewById(R.id.search_list);
        viewToolBar = (Toolbar) findViewById(R.id.toolbar);
        viewSearchImage = (ImageView) findViewById(R.id.search_image);

        setSupportActionBar(viewToolBar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("图书搜索");
        }

        viewSearchImage.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        viewSearchList.setLayoutManager(layoutManager);

        mAdapter = new BookListViewAdapter(bookList, this, null, null);
        viewSearchList.setAdapter(mAdapter);

        viewSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                get_search_result(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        get_search_result("");
    }

    private void get_search_result(String s) {
        final Pending pending = new Pending(this);
        pending.showProgressDialog();
        pending.closeProgressDialog();
        BookOperation.Books_by_search(s, new FindListener<Book>() {
            @Override
            public void done(List<Book> list, BmobException e) {
                if (e != null) {
                    Toast.makeText(SearchActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                    pending.closeProgressDialog();
                    return;
                }
                bookList.clear();
                bookList.addAll(list);
                mAdapter.notifyDataSetChanged();
                pending.closeProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.search_image:{
                String s = viewSearchEdit.getText().toString();
                get_search_result(s);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                break;
            }
        }
        return true;
    }
}
