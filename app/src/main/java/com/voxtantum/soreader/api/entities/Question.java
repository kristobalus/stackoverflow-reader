package com.voxtantum.soreader.api.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {

    @SerializedName("tags")
    public List<String> tags;

    @SerializedName("owner")
    public User owner;

    @SerializedName("is_answered")
    public Boolean isAnswered;

    @SerializedName("view_count")
    public Integer viewCount;

    @SerializedName("favorite_count")
    public Integer favoriteCount;

    @SerializedName("answer_count")
    public Integer answerCount;

    @SerializedName("score")
    public Integer score;

    @SerializedName("creation_date")
    public Long creationDate;

    @SerializedName("last_edit_date")
    public Integer lastEditDate;

    @SerializedName("question_id")
    public Integer questionId;

    @SerializedName("link")
    public String linkUrl;

    @SerializedName("title")
    public String title;

    @SerializedName("body")
    public String body;

}
