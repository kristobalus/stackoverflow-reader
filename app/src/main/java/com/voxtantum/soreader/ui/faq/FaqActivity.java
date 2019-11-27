package com.voxtantum.soreader.ui.faq;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;
import com.voxtantum.soreader.R;
import com.voxtantum.soreader.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaqActivity extends BaseActivity {

    public static final String ARG_TAG = "arg_tag";


    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FaqViewModel listViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ButterKnife.bind(this);

        toolbar.setTitle(getIntent().getStringExtra(ARG_TAG));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listViewModel = new ViewModelProvider(this).get(FaqViewModel.class);
        listViewModel.loadForTag(getIntent().getStringExtra(ARG_TAG));
        listViewModel.getSourceError().observe(this, this::onViewModelError);

        showFaqFragment();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    private void showFaqFragment(){
        if ( getSupportFragmentManager().findFragmentByTag("FaqFragment") == null ){
            setCurrentFragment(FaqFragment.newInstance(),"FaqFragment" );
        }
    }





}
