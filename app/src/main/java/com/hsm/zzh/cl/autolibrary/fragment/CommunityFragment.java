package com.hsm.zzh.cl.autolibrary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.adapter.BookListViewAdapter;
import com.hsm.zzh.cl.autolibrary.info_api.Book;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    private RecyclerView view_recyclerView;
    private Banner banner;
    private View viewHeader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        view_recyclerView = (RecyclerView) view.findViewById(R.id.book_list);
//        banner = (Banner) view.findViewById(R.id.banner);

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
                Glide.with(getContext()).load(Integer.parseInt((String)path)).into(imageView);
            }
        });
    }


    private List<Book> bookList = new ArrayList<>();
    private List<String> bannerPicList = new ArrayList<>();

    private BookListViewAdapter bookListViewAdapter;

    private void getBookInfo() {
        // TODO: 18-12-5
        bookList.add(new Book("1", "第一行代码", "郭霖", "", "code",
                        "lalalalalla"
                ));
        bookList.add(new Book("1", "第一行代码", "郭霖", "", "code",
                "lalalalalla"
        ));
        bookList.add(new Book("1", "第一行代码", "郭霖", "", "code",
                "lalalalalla"
        ));
        bookList.add(new Book("1", "第一行代码", "郭霖", "", "code",
                "lalalalalla"
        ));
        bookList.add(new Book("1", "第一行代码", "郭霖", "", "code",
                "lalalalalla"
        ));


        bookListViewAdapter.notifyDataSetChanged();
    }

    private void getBannerPic() {
        // TODO: 18-12-5
        bannerPicList.add(R.drawable.locate+"");
        bannerPicList.add(R.drawable.locate+"");
        bannerPicList.add(R.drawable.locate+"");

        banner.setImages(bannerPicList);
        banner.setDelayTime(2000);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
    }
}
