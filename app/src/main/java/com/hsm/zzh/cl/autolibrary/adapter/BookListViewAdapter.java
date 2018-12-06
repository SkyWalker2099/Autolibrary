package com.hsm.zzh.cl.autolibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.info_api.Book;
import com.hsm.zzh.cl.autolibrary.wheel.BaseAdapter;

import java.util.List;

public class BookListViewAdapter extends BaseAdapter<Book, BookListViewAdapter.BookViewHolder> {


    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView viewImage;
        TextView viewTitle;
        TextView viewDesc;
        TextView viewAuthor;
        TextView viewType;
        View viewItemView;

        public BookViewHolder(View view) {
            super(view);
            if (view == view_headerView || view == view_footerView)
                return;
            viewImage = (ImageView) view.findViewById(R.id.book_image);
            viewTitle = (TextView) view.findViewById(R.id.book_title);
            viewDesc = (TextView) view.findViewById(R.id.book_short_desc);
            viewAuthor = (TextView) view.findViewById(R.id.book_author);
            viewType = (TextView) view.findViewById(R.id.book_type);
            viewItemView = view;
        }
    }

    private List<Book> bookList;
    private Context context;
    private View view_headerView;
    private View view_footerView;

    public BookListViewAdapter(List<Book> bookList, Context context,
                               View view_headerView, View view_footerView) {
        super(bookList, context, view_headerView, view_footerView);
        this.bookList = bookList;
        this.context = context;
        this.view_headerView = view_headerView;
        this.view_footerView = view_footerView;
    }

    @Override
    protected int layoutId() {
        return R.layout.book_item;
    }

    @Override
    protected BookViewHolder myCreateViewHolder(View view) {
        return new BookViewHolder(view);
    }

    @Override
    protected void myBindViewHolder(BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.viewType.setText(book.getType());
        holder.viewAuthor.setText(book.getAuthor());
        holder.viewTitle.setText(book.getTitle());
        holder.viewDesc.setText(book.getShortdesc());

        Glide.with(context).load(book.getPic_url()).into(holder.viewImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 18-12-5
            }
        });
    }


}
