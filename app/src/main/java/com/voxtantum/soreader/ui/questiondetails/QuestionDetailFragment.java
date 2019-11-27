package com.voxtantum.soreader.ui.questiondetails;

import android.os.Bundle;

import com.voxtantum.soreader.ui.base.BaseFragment;

public class QuestionDetailFragment extends BaseFragment {

    public static QuestionDetailFragment newInstance() {

        Bundle args = new Bundle();

        QuestionDetailFragment fragment = new QuestionDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }



}
