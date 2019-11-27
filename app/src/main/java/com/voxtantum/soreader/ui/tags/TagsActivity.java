package com.voxtantum.soreader.ui.tags;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.voxtantum.soreader.R;
import com.voxtantum.soreader.ui.base.BaseActivity;

import butterknife.ButterKnife;

public class TagsActivity extends BaseActivity {

    TagsViewModel tagsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        ButterKnife.bind(this);

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

}
