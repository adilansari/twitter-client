package com.codepath.apps.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Table(name = "user")
public class User extends Model{
    @Column(name = "name")
    public String name;

    @Column(name = "profile_img_url")
    public String profileImgUrl;

    @Column(name = "user_id", unique = true, index = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long userId;

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
        user.userId = jsonObject.getLong("id");
        user.profileImgUrl = jsonObject.getString("profile_image_url");
        user.location = jsonObject.getString("location");
        user.followersCount = jsonObject.getInt("followers_count");
        user.friendsCount = jsonObject.getInt("friends_count");
        user.statusesCount = jsonObject.getInt("statuses_count");

        user.save();

        return user;
    }

    public static User findOrCreateFromJson(JSONObject jsonObject) throws JSONException {
        long id = jsonObject.getLong("id");
        User existingUser = new Select().from(User.class).where("user_id = ?", id).executeSingle();
        if (existingUser != null) {
            return existingUser;
        } else {
            return fromJson(jsonObject);
        }
    }

    public List<Tweet> tweetsForUser(){
        return getMany(Tweet.class, "User");
    }

}
