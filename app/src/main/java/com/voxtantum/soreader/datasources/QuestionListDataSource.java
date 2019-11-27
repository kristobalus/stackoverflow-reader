package com.voxtantum.soreader.datasources;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.voxtantum.soreader.api.base.ApiException;
import com.voxtantum.soreader.api.base.Paging;
import com.voxtantum.soreader.api.entities.Question;
import com.voxtantum.soreader.api.services.TagService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class QuestionListDataSource extends PageKeyedDataSource<Integer, Question> {

    public static final int PAGE_SIZE = 20;

    private String tag;
    private TagService service;
    private Integer firstPage = 1;
    private MutableLiveData<Throwable> error;
    private MutableLiveData<Boolean> loading;


    public QuestionListDataSource(String tag, TagService service, MutableLiveData<Boolean> loading, MutableLiveData<Throwable> error){
        super();
        this.tag = tag;
        this.service = service;
        this.error = error;
        this.loading = loading;
    }


    private Response<Paging<Question>> loadPage(Integer page, Integer pageSize) throws IOException {
        Call<Paging<Question>> call = service.searchAdvanced(tag, page, pageSize);
        return call.execute();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Question> callback) {

        try {

            loading.postValue(true);

            Response<Paging<Question>> response = loadPage(firstPage, params.requestedLoadSize);
            if (!response.isSuccessful()) {
                throw new ApiException(response.code(), response.message(), response.errorBody());
            }

            loading.postValue(false);

            Paging<Question> paging = response.body();
            if ( paging != null )
                callback.onResult(paging.items, null, firstPage + 1);

        } catch (Exception err) {
            error.postValue(err);
        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Question> callback) {


        try {

            loading.postValue(true);

            Response<Paging<Question>> response = loadPage(params.key, params.requestedLoadSize);
            if (!response.isSuccessful()) {
                throw new ApiException(response.code(), response.message(), response.errorBody());
            }

            loading.postValue(false);

            Paging<Question> paging = response.body();
            if ( paging != null )
                callback.onResult(paging.items, paging.hasMore ? params.key - 1 : null);

        } catch (Exception err) {
            error.postValue(err);
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Question> callback) {


        try {

            loading.postValue(true);

            Response<Paging<Question>> response = loadPage(params.key, params.requestedLoadSize);
            if (!response.isSuccessful()) {
                throw new ApiException(response.code(), response.message(), response.errorBody());
            }

            loading.postValue(false);

            Paging<Question> paging = response.body();
            if ( paging != null )
                callback.onResult(paging.items,  paging.hasMore ? params.key + 1 : null);

        } catch (Exception err) {
            error.postValue(err);
        }

    }


    public static class Factory extends DataSource.Factory<Integer, Question> {

        private MutableLiveData<Throwable> sourceError = new MutableLiveData<>();
        private MutableLiveData<Boolean> sourceLoading = new MutableLiveData<>();
        private TagService service;
        private String tag;
        private DataSource<Integer, Question> source;

        public Factory(String tag, TagService service){
            this.service = service;
            this.tag = tag;
        }

        @NonNull
        @Override
        public DataSource<Integer, Question> create() {
            source = new QuestionListDataSource(tag, service, sourceLoading, sourceError);
            sourceError.postValue(null);
            sourceLoading.postValue(null);
            return source;
        }

        public LiveData<Boolean> getSourceLoading(){
            return this.sourceLoading;
        }

        public LiveData<Throwable> getSourceError(){
            return this.sourceError;
        }

        public DataSource<Integer, Question> getSource(){
            return source;
        }

    }





}