package com.example.nitinbehal.browser.HelperClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nitinbehal.browser.R;

public class EmptyScreen {

    private RelativeLayout root;    // or container or great grand parent, the first view in the layout file

    private ImageView image;
    private TextView msg;
    private Button button;


    /**
     * Create and show a non_availability screen (An image, a retry button and a text message) with complete controls of the views
     *
     * @param context Activity context
     * @param view    the view where the non_availability screen will be shown
     */
    public EmptyScreen(Context context, View view) {

        root = new RelativeLayout(context);
        root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        root = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.empty_screen, (ViewGroup) view, false);
        ((ViewGroup) view).addView(root);

        image = (ImageView) root.findViewById(R.id.image);
        msg = (TextView) root.findViewById(R.id.msg);
        button = (Button) root.findViewById(R.id.button);

    }

    /**
     * Sets the message in the non availability screen
     *
     * @param msg the message to be shown
     */
    public void setMessage(String msg) {

        this.msg.setText(msg);
    }

    /**
     * Sets the properties of the bottom button in the non availability screen
     *
     * @param button          the text to be written on the button
     * @param onClickListener the action that the button will carry out
     */
    public void setButton(String button, View.OnClickListener onClickListener) {

        this.button.setText(button);
        this.button.setOnClickListener(onClickListener);
    }

    /**
     * Set the image to be shown in non availability screen (def: cloud)
     *
     * @param image the image to be shown
     */
    public void setImage(int image) {

        this.image.setImageResource(image);
    }

    /**
     * Sets the visibility of the non availability screen (all views)
     *
     * @param visibility same as android view (invisible, visible, gone)
     */
    public void setVisibility(int visibility) {

        this.root.setVisibility(visibility);
    }


}

