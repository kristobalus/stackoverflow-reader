package com.voxtantum.soreader.datasources;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.voxtantum.soreader.api.base.ApiException;
import com.voxtantum.soreader.api.base.Paging;
import com.voxtantum.soreader.api.entities.Tag;
import com.voxtantum.soreader.api.services.TagService;

import retrofit2.Call;
import retrofit2.Response;

public class TagDataSource extends PageKeyedDataSource<Integer, Tag> {

    public static final int PAGE_SIZE = 20;

    private TagService service;
    private Integer firstPage = 1;
    private MutableLiveData<Throwable> error;
    private MutableLiveData<Boolean> loading;


    public TagDataSource(TagService service, MutableLiveData<Boolean> loading, MutableLiveData<Throwable> error){
        super();
        this.service = service;
        this.error = error;
        this.loading = loading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Tag> callback) {

        Call<Paging<Tag>> call = service.getTags(firstPage, params.requestedLoadSize);

        try {

            loading.postValue(true);

            Response<Paging<Tag>> response = call.execute();
            if (!response.isSuccessful()) {
                throw new ApiException(response.code(), response.message());
            }

            loading.postValue(false);

            Paging<Tag> paging = response.body();
            if ( paging != null )
                callback.onResult(paging.items, null, firstPage + 1);

        } catch (Exception err) {
            error.postValue(err);
        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Tag> callback) {

        Call<Paging<Tag>> call = service.getTags(params.key, params.requestedLoadSize);

        try {

            loading.postValue(true);

            Response<Paging<Tag>> response = call.execute();
            if (!response.isSuccessful()) {
                throw new ApiException(response.code(), response.message());
            }

            loading.postValue(false);

            Paging<Tag> paging = response.body();
            if ( paging != null )
                callback.onResult(paging.items, paging.hasMore ? params.key - 1 : null);

        } catch (Exception err) {
            error.postValue(err);
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Tag> callback) {

        Call<Paging<Tag>> call = service.getTags(params.key, params.requestedLoadSize);

        try {

            loading.postValue(true);

            Response<Paging<Tag>> response = call.execute();
            if (!response.isSuccessful()) {
                throw new ApiException(response.code(), response.message());
            }

            loading.postValue(false);

            Paging<Tag> paging = response.body();
            if ( paging != null )
                callback.onResult(paging.items,  paging.hasMore ? params.key + 1 : null);

        } catch (Exception err) {
            error.postValue(err);
        }

    }


    public static class Factory extends DataSource.Factory<Integer, Tag> {

        private MutableLiveData<Throwable> sourceError = new MutableLiveData<>();
        private MutableLiveData<Boolean> sourceLoading = new MutableLiveData<>();
        private TagService service;

        public Factory(TagService service){
            this.service = service;
        }

        @NonNull
        @Override
        public DataSource<Integer, Tag> create() {
            sourceError.postValue(null);
            sourceLoading.postValue(null);
            return new TagDataSource(service, sourceLoading, sourceError);
        }

        public LiveData<Boolean> getSourceLoading(){
            return this.sourceLoading;
        }

        public LiveData<Throwable> getSourceError(){
            return this.sourceError;
        }

    }





}
