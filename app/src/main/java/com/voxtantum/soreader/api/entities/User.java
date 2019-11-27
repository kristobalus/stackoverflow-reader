package com.voxtantum.soreader.api.entities;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("reputation")
    public Integer reputation;

    @SerializedName("user_id")
    public Integer userId;

    @SerializedName("user_type")
    public String userType;

    @SerializedName("accept_rate")
    public Integer acceptRate;

    @SerializedName("profile_image")
    public String profileImage;

    @SerializedName("display_name")
    public String displayName;

    @SerializedName("link")
    public String link;

}
