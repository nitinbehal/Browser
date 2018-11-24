package com.example.nitinbehal.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private WebView webView;
    private EditText editText;
    private TextView goText;
    private ImageButton previous, next;
    private TextView bookmark;
    private ImageView menuItem;
    private RelativeLayout bookmarkLayout;
    private static final String googleQuery = "http://www.google.com/search?q=";
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;
    private static final int callbackValue = 101;
    private ProgressBar progressBar;

    public static final String PREF_NAME = "BROWSER_EXAMPLE";
    public static final String key = "bookmark_query";
    private int backPressCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initialization();

        webViewInitialization();

        goText.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        goText.setOnClickListener(this);
        bookmarkLayout.setOnClickListener(this);
        menuItem.setOnClickListener(this);


        initializingSharedPref();

        settingActionListener();
    }

    private void webViewInitialization() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
    }

    private void initialization() {

        webView = findViewById(R.id.webview);
        editText = findViewById(R.id.edit_query);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        goText = findViewById(R.id.go_textview);
        menuItem = findViewById(R.id.menu_item);
        bookmark = findViewById(R.id.bookmark_text);
        bookmarkLayout = findViewById(R.id.bookmark_layout);
        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.bookmark_layout:

                bookmarkQuery();
                break;

            case R.id.edit_query:

                break;

            case R.id.next:

                webView.goForward();
                break;

            case R.id.previous:

                webView.goBack();
                break;

            case R.id.menu_item:
                PopupMenu popupMenu = new PopupMenu(this, v);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();

                break;

            case R.id.go_textview:

                String query = editText.getText().toString();

                loadingWebview(query);
                initBookMark();


                break;


        }
    }

    private void bookmarkQuery() {
        String bookmarkQuery = editText.getText().toString();

        if (bookmarkQuery.trim().length() == 0) {
            Toast.makeText(this, "Empty query", Toast.LENGTH_LONG).show();
            return;
        }

        String previousBookmarks = pref.getString(key, null);

        JSONObject jsonObject = new JSONObject();
        if (previousBookmarks != null) {
            try {
                jsonObject = new JSONObject(previousBookmarks);

                Iterator<String> iterable = jsonObject.keys();
                while (iterable.hasNext()) {
                    if (iterable.next().equals(bookmarkQuery)) {

                        Toast.makeText(this, " Already Bookmarked", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                jsonObject.put(bookmarkQuery, "");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        bookmark.setText(getString(R.string.bookmarked));
        bookmarkLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        editor.putString(key, jsonObject.toString()).apply();
    }

    private void initBookMark() {
        bookmarkLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        bookmark.setText(getString(R.string.bookmark));
    }

    private void loadingWebview(String query) {

        if (query.trim().length() == 0) {
            Toast.makeText(this, "Empty query", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String result;

        if (query.contains("https://"))
            result = query;
        else if (query.contains("www."))
            result = "https://" + query;
        else
            result = googleQuery + query;


        webView.loadUrl(result);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case callbackValue:

                if (resultCode == Activity.RESULT_OK) {


                    String query = data.getStringExtra("bookmark_query");
                    loadingWebview(query);
                    editText.setText(query);

                }
        }
    }

    private void hideKeyBoard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {

        ++backPressCount;
        if (backPressCount > 1)
            super.onBackPressed();
        else
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_LONG).show();

    }

    private void settingActionListener() {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                try {

                    hideKeyBoard();

                    loadingWebview(editText.getText().toString());
                    initBookMark();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
    }


    private void initializingSharedPref() {

        if (pref == null) {

            pref = getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = pref.edit();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.bookmark:

                startActivityForResult(new Intent(this, BookMarkedActivity.class), callbackValue);
                break;
        }
        return false;
    }
}
