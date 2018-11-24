package com.example.nitinbehal.browser.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nitinbehal.browser.Interface.BookMarkInterface;
import com.example.nitinbehal.browser.R;

import java.util.ArrayList;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder> implements PopupMenu.OnMenuItemClickListener {

    private Context context;
    private ArrayList<String> bookMarkList;
    private BookMarkInterface bookMarkInterface;
    private int popUpIndex;


    public BookMarkAdapter(Context context, ArrayList<String> bookMarkList, BookMarkInterface bookMarkInterface) {

        this.context = context;
        this.bookMarkList = bookMarkList;
        this.bookMarkInterface = bookMarkInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.bookmark_adapter_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.title.setText(bookMarkList.get(position));

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookMarkInterface.bookMarkClicked(bookMarkList.get(position));
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpIndex = position;
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.setOnMenuItemClickListener(BookMarkAdapter.this);
                popupMenu.inflate(R.menu.popup_remove);
                popupMenu.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return bookMarkList.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.remove:
                bookMarkInterface.removeBookMark(bookMarkList.get(popUpIndex));
                notifyDataSetChanged();
        }
        return false;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView imageView;

        public ViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.textview);
            imageView = itemView.findViewById(R.id.menu_item);
        }
    }
}
