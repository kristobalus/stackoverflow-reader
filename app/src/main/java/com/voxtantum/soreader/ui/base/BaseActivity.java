package com.voxtantum.soreader.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    public void setCurrentFragment(Fragment fragment, String fragmentTag) {

        this.currentFragment = fragment;
        this.currentFragmentTag = fragmentTag;

        if (isNetworkAvailable) {
            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
            tr.replace(getFragmentContainerId(), currentFragment, fragmentTag);
            tr.commit();
        }
    }

    protected void showNoNetworkFragment() {

        if (getSupportFragmentManager().findFragmentByTag("NoNetworkFragment") == null) {

            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
            tr.replace(getFragmentContainerId(), NoNetworkFragment.newInstance(), "NoNetworkFragment");
            tr.commit();
        }
    }

    protected void showAirplaneModeFragment() {

        if (getSupportFragmentManager().findFragmentByTag("AirplaneModeFragment") == null) {

            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
            tr.replace(getFragmentContainerId(), AirplaneModeFragment.newInstance(), "AirplaneModeFragment");
            tr.commit();
        }
    }

    protected void showCurrentFragment() {
        if (currentFragment != null) {
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

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())) {
                isAirplaneMode = Settings.System.getInt(context.getContentResolver(),
                        Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
            } else {
                isNetworkAvailable = !(NetworkUtil.getConnectivityStatus(context) == NetworkUtil.TYPE_NOT_CONNECTED);
            }

            if (isAirplaneMode) {
                showAirplaneModeFragment();
            } else if (!isNetworkAvailable) {
                showNoNetworkFragment();
            } else {
                showCurrentFragment();
            }
        }
    };

    protected void onViewModelError(Throwable err) {
        if (err instanceof ApiException) {
            showApiErrorFragment((ApiException) err);
        }
    }

    protected abstract int getFragmentContainerId();

}
