package com.voxtantum.soreader.ui.questions;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;
import com.squareup.otto.Subscribe;
import com.voxtantum.soreader.R;
import com.voxtantum.soreader.events.InvalidateDataSourceEvent;
import com.voxtantum.soreader.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsActivity extends BaseActivity {

    public static final String ARG_TAG = "arg_tag";


    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private QuestionsViewModel faqViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        ButterKnife.bind(this);

        toolbar.setTitle(getIntent().getStringExtra(ARG_TAG));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if ( item.getItemId() == R.id.action_refresh ){
                    faqViewModel.invalidate();
                }

                return false;
            }
        });

        faqViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        faqViewModel.loadForTag(getIntent().getStringExtra(ARG_TAG));
        faqViewModel.getSourceError().observe(this, this::onViewModelError);

        showFaqFragment();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    private void showFaqFragment(){
        if ( getSupportFragmentManager().findFragmentByTag("QuestionsFragment") == null ){
            setCurrentFragment(QuestionsFragment.newInstance(),"QuestionsFragment" );
        }
    }

    @Subscribe()
    public  void onInvalidateDataSourceEvent(InvalidateDataSourceEvent event){
        faqViewModel.invalidate();
    }


}
