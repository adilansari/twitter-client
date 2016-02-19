package com.codepath.apps.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

@Table(name = "user")
public class User extends Model{
    @Column(name = "name")
    public String name;

    @Column(name = "profile_img_url")
    public String profileImgUrl;

    @Column(name = "id", unique = true, index = true)
    public long id;

    @Column(name = "location")
    public String location;

    @Column(name = "followers_count")
    public int followersCount;

    @Column(name = "screen_name", index = true)
    public String screenName;

    @Column(name = "friends_count")
    public int friendsCount;

    @Column(name = "statuses_count")
    public int statusesCount;

    public User(){
        super();
    }

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null)
            return null;

        User user = new User();

        user.screenName = jsonObject.getString("screen_name");
        user.name = jsonObject.getString("name");
        user.id = jsonObject.getLong("id");
        user.profileImgUrl = jsonObject.getString("profile_img_url");
        user.location = jsonObject.getString("location");
        user.followersCount = jsonObject.getInt("followers_count");
        user.friendsCount = jsonObject.getInt("friends_count");
        user.statusesCount = jsonObject.getInt("statuses_count");

        return user;
    }

}
