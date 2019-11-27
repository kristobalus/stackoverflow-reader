package com.voxtantum.soreader.ui.tags;

import android.os.Bundle;
import android.view.MenuItem;


import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.otto.Subscribe;
import com.voxtantum.soreader.R;
import com.voxtantum.soreader.events.InvalidateDataSourceEvent;
import com.voxtantum.soreader.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagsActivity extends BaseActivity {

    TagsViewModel tagsViewModel;

    @BindView(R.id.toolbar)
    Toolbar toolbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        ButterKnife.bind(this);

        toolbarView.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if ( item.getItemId() == R.id.action_refresh ){
                    tagsViewModel.invalidate();
                }

                return false;
            }
        });

        tagsViewModel = new ViewModelProvider(this).get(TagsViewModel.class);
        tagsViewModel.getSourceError().observe(this, this::onViewModelError);

        showTagsFragment();
    }

    private void showTagsFragment(){
        if ( getSupportFragmentManager().findFragmentByTag("TagsFragment") == null ){
            setCurrentFragment(TagsFragment.newInstance(),"TagsFragment" );
        }
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }


    @Subscribe()
    public  void onInvalidateDataSourceEvent(InvalidateDataSourceEvent event){
        tagsViewModel.invalidate();
    }

}
