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
import com.voxtantum.soreader.ReaderApp;
import com.voxtantum.soreader.api.base.ApiException;
import com.voxtantum.soreader.events.InvalidateDataSourceEvent;
import com.voxtantum.soreader.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class ApiErrorFragment extends BaseFragment {

    private static final String ARG_EXCEPTION = "arg_exception";

    public static ApiErrorFragment newInstance(ApiException err) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_EXCEPTION, err);

        ApiErrorFragment fragment = new ApiErrorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.code)
    AppCompatTextView codeView;

    @BindView(R.id.message)
    AppCompatTextView messageView;

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

        ApiException err = (ApiException) getArguments().getSerializable(ARG_EXCEPTION);

        codeView.setText(String.valueOf(err.httpCode));
        bodyView.setText(err.error.errorMessage);
        messageView.setText(err.message);
    }

    @OnClick(R.id.button_retry)
    public void onRetryClicked(){
        ReaderApp.getMessageBus().post(new InvalidateDataSourceEvent());
    }

}
