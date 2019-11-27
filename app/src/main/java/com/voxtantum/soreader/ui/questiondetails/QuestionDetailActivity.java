package com.voxtantum.soreader.ui.questiondetails;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.voxtantum.soreader.R;
import com.voxtantum.soreader.ui.base.BaseActivity;

import butterknife.ButterKnife;

public class QuestionDetailActivity extends BaseActivity {


    public static final String ARG_QUESTION_ID = "arg_question_id";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);
        ButterKnife.bind(this);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }


}
