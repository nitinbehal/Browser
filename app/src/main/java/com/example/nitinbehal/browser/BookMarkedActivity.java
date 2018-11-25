package com.example.nitinbehal.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.nitinbehal.browser.Adapter.BookMarkAdapter;
import com.example.nitinbehal.browser.HelperClass.EmptyScreen;
import com.example.nitinbehal.browser.Interface.BookMarkInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class BookMarkedActivity extends AppCompatActivity implements BookMarkInterface {


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private EmptyScreen holder;
    private ArrayList<String> bookMarksList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        getSupportActionBar().setTitle(getString(R.string.bookmarks));

        initializingSharedPref();
        initializingHolder();
        recyclerView = findViewById(R.id.recycler);

        try {
            settingAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void initializingHolder() {
        holder = new EmptyScreen(this, findViewById(android.R.id.content));
        holder.setVisibility(View.GONE);
    }

    private void settingAdapter() throws JSONException {

        String previousBookmarks = pref.getString(MainActivity.key, null);
        if (previousBookmarks != null) {


            JSONObject jsonObject = new JSONObject(previousBookmarks);
            Iterator<String> bookMarks = jsonObject.keys();

            while (bookMarks.hasNext()) {
                String query = bookMarks.next();
                if (!bookMarksList.contains(query))
                    bookMarksList.add(query);
            }

            BookMarkAdapter bookMarkAdapter = new BookMarkAdapter(this, bookMarksList, this);
            recyclerView.setAdapter(bookMarkAdapter);

            if (bookMarksList.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                holder.setVisibility(View.VISIBLE);
            }

        }else{
            recyclerView.setVisibility(View.GONE);
            holder.setVisibility(View.VISIBLE);
        }

    }

    private void initializingSharedPref() {

        if (pref == null) {

            pref = getApplicationContext().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
            editor = pref.edit();
        }
    }

    @Override
    public void bookMarkClicked(String query) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("bookmark_query", query);
        setResult(Activity.RESULT_OK, resultIntent);
        onBackPressed();
    }

    @Override
    public void removeBookMark(String query) {

        bookMarksList.remove(query);

        try {
            String previousBookmarks = pref.getString(MainActivity.key, null);
            JSONObject jsonObject = new JSONObject(previousBookmarks);
            jsonObject.remove(query);

            editor.putString(MainActivity.key, jsonObject.toString()).commit();

            if (bookMarksList.size() == 0) {
                recyclerView.setVisibility(View.VISIBLE);
                holder.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
