package com.voxtantum.soreader.ui.errors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.voxtantum.soreader.R;
import com.voxtantum.soreader.ui.base.BaseFragment;

public class AirplaneModeFragment extends BaseFragment {


    public static AirplaneModeFragment newInstance() {

        Bundle args = new Bundle();

        AirplaneModeFragment fragment = new AirplaneModeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_airplane_mode, container, false);
    }

}
