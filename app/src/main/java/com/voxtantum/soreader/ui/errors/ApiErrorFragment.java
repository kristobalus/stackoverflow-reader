package com.voxtantum.soreader.ui.errors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.voxtantum.soreader.R;
import com.voxtantum.soreader.ui.base.BaseFragment;

import butterknife.BindView;

public class ApiErrorFragment extends BaseFragment {

    private static final String ARG_HTTP_CODE = "arg_http_code";
    private static final String ARG_BODY = "arg_body";

    public static ApiErrorFragment newInstance(Integer httpCode, String body) {

        Bundle args = new Bundle();
        args.putString(ARG_BODY, body);
        args.putInt(ARG_HTTP_CODE, httpCode);

        ApiErrorFragment fragment = new ApiErrorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.code)
    AppCompatTextView codeView;

    @BindView(R.id.body)
    AppCompatTextView bodyView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_api_error, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Integer httpCode = getArguments().getInt(ARG_HTTP_CODE);
        String body = getArguments().getString(ARG_BODY);
        codeView.setText(httpCode != null ? String.valueOf(httpCode) : null);
        bodyView.setText(body);
    }

}
