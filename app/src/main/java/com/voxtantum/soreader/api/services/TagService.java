package com.voxtantum.soreader.api.services;

import com.voxtantum.soreader.api.base.Paging;
import com.voxtantum.soreader.api.entities.Question;
import com.voxtantum.soreader.api.entities.Tag;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TagService {

    @GET("tags?order=desc&sort=popular&site=stackoverflow")
    Call<Paging<Tag>> getTags(@Query("page") Integer page, @Query("pagesize") Integer pageSize);

    @GET("tags/{{tag}}/faq?site=stackoverflow&filter=withbody")
    Call<Paging<Question>> getFAQ(@Path("tag") String tag, @Query("page") Integer page, @Query("pagesize") Integer pageSize);

    @GET("search/advanced?site=stackoverflow&&filter=withbody&order=desc")
    Call<Paging<Question>> searchAdvanced(@Query("tagged") String tag, @Query("page") Integer page, @Query("pagesize") Integer pageSize);





}
