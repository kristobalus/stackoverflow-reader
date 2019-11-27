package com.voxtantum.soreader.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.voxtantum.soreader.R;
import com.voxtantum.soreader.ReaderApp;
import com.voxtantum.soreader.api.base.ApiException;
import com.voxtantum.soreader.helpers.NetworkUtil;
import com.voxtantum.soreader.ui.errors.AirplaneModeFragment;
import com.voxtantum.soreader.ui.errors.ApiErrorFragment;
import com.voxtantum.soreader.ui.errors.NoNetworkFragment;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final String ACTION_WIFI_CHANGED = "android.net.wifi.WIFI_STATE_CHANGED";


    protected Fragment currentFragment;
    protected String currentFragmentTag;
    protected Boolean isNetworkAvailable = false;
    protected Boolean isAirplaneMode = false;
    protected IntentFilter networkChangeFilter = new IntentFilter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ReaderApp.getMessageBus().register(this);

        networkChangeFilter.addAction(ACTION_NETWORK_CHANGE);
        networkChangeFilter.addAction(ACTION_WIFI_CHANGED);
        networkChangeFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReaderApp.getMessageBus().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkChangeReceiver, networkChangeFilter);
        checkNetworkStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    public void setCurrentFragment(Fragment fragment, String fragmentTag) {

        this.currentFragment = fragment;
        this.currentFragmentTag = fragmentTag;

        if (isNetworkAvailable && !isAirplaneMode) {
            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
            tr.replace(getFragmentContainerId(), currentFragment, fragmentTag);
            tr.commit();
        }
    }

    protected void addOverlayView(){
        if ( findViewById(R.id.overlay_container) == null ){
            FrameLayout rootLayout = findViewById(android.R.id.content);
            View.inflate(this, R.layout.overlay_layout, rootLayout);
        }
    }

    protected void removeOverlayView(){
        if ( findViewById(R.id.overlay_container) != null ) {
            FrameLayout rootLayout = findViewById(android.R.id.content);
            rootLayout.removeViewAt(rootLayout.getChildCount() - 1);
        }
    }

    protected void showNoNetworkFragment() {

        addOverlayView();

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.overlay_container, NoNetworkFragment.newInstance());
        tr.commit();
    }

    protected void showAirplaneModeFragment() {

        addOverlayView();

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.overlay_container, AirplaneModeFragment.newInstance());
        tr.commit();
    }

    protected void showCurrentFragment() {

        if (currentFragment != null) {

            removeOverlayView();

            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
            tr.replace(getFragmentContainerId(), currentFragment, currentFragmentTag);
            tr.commit();
        }
    }

    protected void showApiErrorFragment(ApiException apiException) {
        if (getSupportFragmentManager().findFragmentByTag("ApiErrorFragment") == null) {
            setCurrentFragment(ApiErrorFragment.newInstance(apiException), "ApiErrorFragment");
        }
    }

    protected void checkNetworkStatus(){

        isAirplaneMode = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
        isNetworkAvailable = !(NetworkUtil.getConnectivityStatus(this) == NetworkUtil.TYPE_NOT_CONNECTED);

        if (isAirplaneMode) {
            showAirplaneModeFragment();
        } else if (!isNetworkAvailable) {
            showNoNetworkFragment();
        } else {
            showCurrentFragment();
        }
    }

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkNetworkStatus();
        }
    };

    protected void onViewModelError(Throwable err) {
        if (err instanceof ApiException) {
            showApiErrorFragment((ApiException) err);
        }
    }

    protected abstract int getFragmentContainerId();

}
