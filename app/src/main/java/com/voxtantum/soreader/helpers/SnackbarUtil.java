package com.voxtantum.soreader.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.SnackbarContentLayout;
import com.voxtantum.soreader.R;


public class SnackbarUtil {

    public static void show(Activity activity, String message) {
        show(activity, activity.findViewById(android.R.id.content), message);
    }

    public static void show(Activity activity, Integer messageId) {
        show(activity, activity.getString(messageId));
    }

    public static void show(Context context, View view, int messageId) {
        show(context, view, context.getString(messageId));
    }

    public static void show(Context context, View view, String message) {


        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.close, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setAction(R.string.close, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                    }

                    @Override
                    public void onShown(Snackbar sb) {
                        super.onShown(sb);
                    }
                });

        SnackbarContentLayout contentLayout = (SnackbarContentLayout) ((ViewGroup) snackbar.getView()).getChildAt(0);
        @SuppressLint("RestrictedApi") TextView tv = contentLayout.getMessageView();
        tv.setMaxLines(5);

        snackbar.setDuration(10000);
        snackbar.show();
    }
}
