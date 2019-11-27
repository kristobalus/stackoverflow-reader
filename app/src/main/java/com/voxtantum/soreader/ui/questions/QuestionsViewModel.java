package com.voxtantum.soreader.ui.questions;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.voxtantum.soreader.ReaderApp;
import com.voxtantum.soreader.api.entities.Question;
import com.voxtantum.soreader.api.services.TagService;
import com.voxtantum.soreader.datasources.QuestionListDataSource;
import com.voxtantum.soreader.ui.base.BaseViewModel;

import java.util.concurrent.Executors;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class QuestionsViewModel extends BaseViewModel {

    private LiveData<PagedList<Question>> pagedListLiveData;
    private MediatorLiveData<Boolean> sourceLoading = new MediatorLiveData<>();
    private MediatorLiveData<Throwable> sourceError = new MediatorLiveData<>();
    private QuestionListDataSource.Factory sourceFactory;

    @Inject
    TagService tagService;

    public QuestionsViewModel(@NonNull Application application) {
        super(application);

        ((ReaderApp) application).applicationComponent.inject(this);
    }

    public void loadForTag(String selectedTag){

        sourceFactory = new QuestionListDataSource.Factory(selectedTag, tagService);

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
                .setPageSize(QuestionListDataSource.PAGE_SIZE)
                .setInitialLoadSizeHint(QuestionListDataSource.PAGE_SIZE)
                .build();

        pagedListLiveData = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();
    }


    public LiveData<PagedList<Question>> getQuestionList() {
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
