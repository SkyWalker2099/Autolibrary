package com.hsm.zzh.cl.autolibrary.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.adapter.BookListViewAdapter;
import com.hsm.zzh.cl.autolibrary.info_api.Book;
import com.hsm.zzh.cl.autolibrary.info_api.BookOperation;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class CommunityFragment extends Fragment {

    private RecyclerView view_recyclerView;
    private Banner banner;
    private View viewHeader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        view_recyclerView = (RecyclerView) view.findViewById(R.id.book_list);

        sp = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);

        init_view();
        getBookInfo();
        getBannerPic();
        return view;
    }

    private void init_view() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        view_recyclerView.setLayoutManager(layoutManager);

        //在recyclerview中装如header
        viewHeader = LayoutInflater.from(getContext()).inflate(R.layout.header_view_community, view_recyclerView, false);
        banner = (Banner) viewHeader.findViewById(R.id.banner);
        bookListViewAdapter = new BookListViewAdapter(
                bookList, getContext(), viewHeader, null);

        view_recyclerView.setAdapter(bookListViewAdapter);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(getContext()).load(Integer.parseInt((String) path)).into(imageView);
            }
        });
    }


    private List<Book> bookList = new ArrayList<>();
    private List<String> bannerPicList = new ArrayList<>();
    private BookListViewAdapter bookListViewAdapter;

    private SharedPreferences sp;

    private void getBookInfo() {
        // TODO: 18-12-5
        double longitude = Double.parseDouble(sp.getString("location_longitude", "-1"));
        double latitude = Double.parseDouble(sp.getString("location_latitude", "-1"));
        BookOperation.Books_by_your_location(new BmobGeoPoint(longitude, latitude), new SQLQueryListener<Book>() {
            @Override
            public void done(BmobQueryResult<Book> bmobQueryResult, BmobException e) {
                if (e == null) {
                    List<Book> bookList = bmobQueryResult.getResults();
                    Log.i("book", "done: " + bookList.toString());
                    CommunityFragment.this.bookList.addAll(bookList);
                    bookListViewAdapter.notifyDataSetChanged();
                }else{
                    Log.e("book", "done: " + e.getMessage());
                    Toast.makeText(getContext(), "出现错误", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    private void getBannerPic() {
        // TODO: 18-12-5
        bannerPicList.add(R.drawable.locate + "");
        bannerPicList.add(R.drawable.locate + "");
        bannerPicList.add(R.drawable.locate + "");

        banner.setImages(bannerPicList);
        banner.setDelayTime(2000);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
    }
}
