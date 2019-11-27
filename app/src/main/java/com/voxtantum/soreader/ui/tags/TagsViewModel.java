package com.voxtantum.soreader.ui.tags;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.voxtantum.soreader.ReaderApp;
import com.voxtantum.soreader.api.entities.Tag;
import com.voxtantum.soreader.api.services.TagService;
import com.voxtantum.soreader.datasources.TagDataSource;
import com.voxtantum.soreader.ui.base.BaseViewModel;

import java.util.concurrent.Executors;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class TagsViewModel extends BaseViewModel {

    private LiveData<PagedList<Tag>> pagedListLiveData;
    private MediatorLiveData<Boolean> sourceLoading = new MediatorLiveData<>();
    private MediatorLiveData<Throwable> sourceError = new MediatorLiveData<>();
    private TagDataSource.Factory sourceFactory;

    @Inject
    TagService tagService;

    public TagsViewModel(@NonNull Application application) {
        super(application);

        ((ReaderApp) application).applicationComponent.inject(this);

        sourceFactory = new TagDataSource.Factory(tagService);

        sourceLoading.addSource(sourceFactory.getSourceLoading(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                sourceLoading.postValue(isLoading != null ? isLoading : false);
            }
        });

        sourceError.addSource(sourceFactory.getSourceError(), new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable err) {
                sourceError.postValue(err);
            }
        });

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(TagDataSource.PAGE_SIZE)
                .setInitialLoadSizeHint(TagDataSource.PAGE_SIZE)
                .build();

        pagedListLiveData = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();

    }


    public LiveData<PagedList<Tag>> getPagedListTags() {
        return pagedListLiveData;
    }

    public LiveData<Boolean> getIsSourceLoading() {
        return sourceLoading;
    }

    public LiveData<Throwable> getSourceError() {
        return sourceError;
    }

    public void invalidate() {
        sourceFactory.getSource().invalidate();
    }

}
