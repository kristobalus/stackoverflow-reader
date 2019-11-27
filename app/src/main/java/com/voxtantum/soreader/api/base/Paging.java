package com.voxtantum.soreader.api.base;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Paging<T> {

    @SerializedName("items")
    public List<T> items;

    @SerializedName("has_more")
    public Boolean hasMore;

}
