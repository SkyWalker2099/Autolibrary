package com.hsm.zzh.cl.autolibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hsm.zzh.cl.autolibrary.R;
import com.hsm.zzh.cl.autolibrary.info_api.Book;
import com.hsm.zzh.cl.autolibrary.info_api.MyUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context mContext;

    private List<Book> historyList;

    private String Status = "returned";

    private BmobUser myUser = BmobUser.getCurrentUser(MyUser.class);

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle;
        TextView bookAuthor;
        TextView bookPublisher;
        TextView returnDate;
        TextView currentStatus;
        private ViewHolder(View view) {
            super(view);
            bookTitle = (TextView) view.findViewById(R.id.history_book_title);
            bookAuthor = (TextView) view.findViewById(R.id.history_book_author);
            bookPublisher = (TextView) view.findViewById(R.id.publisher);

            returnDate = (TextView) view.findViewById(R.id.return_date);
            currentStatus = (TextView) view.findViewById(R.id.status);
        }

    }

    public HistoryAdapter(List<Book> list) {
        historyList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.history_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Book book = historyList.get(position);
//                Intent intent = new Intent(mContext, BorrowInfoActivity.class);
//                intent.putExtra("selected_book", book);
//                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Book book = historyList.get(position);
        viewHolder.bookTitle.setText(book.getTitle());
        viewHolder.bookAuthor.setText(book.getAuthor());
        viewHolder.bookPublisher.setText(book.getPublish_house());
        try {
            if (book.getNow_user() == null||
                    !book.getNow_user().getObjectId().equals(myUser.getObjectId())) {
                viewHolder.currentStatus.setText("已还");
            }else{
                viewHolder.currentStatus.setText("未还");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d("NPE", "fail");
        }


        String borrowDate=book.getUpdatedAt();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd");
        Date bDate = null;
        try {
            bDate = dateFormat.parse(borrowDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date rDate = new Date(bDate.getTime() + (long) 7 * 24 * 60 * 60 * 1000);
        viewHolder.returnDate.setText(dateFormat.format(rDate));

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
}
