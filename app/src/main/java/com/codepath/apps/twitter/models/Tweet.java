package com.codepath.apps.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.twitter.utils.DateConversionUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Parcel(analyze = {Tweet.class})
@Table(name = "tweets")
public class Tweet extends Model {

    @Column(name = "tweet_id", unique = true, index = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long tweetId;

    @Column(name = "text")
    public String text;

    @Column(name = "retweeted")
    public boolean retweeted;

    @Column(name = "favorited")
    public boolean favorited;

    @Column(name = "retweet_count")
    public int retweetCount;

    @Column(name = "favorite_count")
    public int favoriteCount;

    @Column(name = "created_at", index = true)
    public Date createdAt;

    @Column(name = "user")
    public User user;

    @Column(name = "media")
    public Media media;

    @Column(name= "is_mention", index = true)
    public boolean isMention;

    public Tweet() {
    }

    public String getRelativeTimestamp() {
        return DateConversionUtils.getRelativeTimeStamp(createdAt);
    }

    public boolean hasMedia() {
        return (this.media != null);
    }

    public boolean isPhotoTweet() {
        return (hasMedia() && (this.media.type == MediaType.PHOTO));
    }

    public boolean isVideoTweet() {
        return (hasMedia() && (this.media.type == MediaType.VIDEO));
    }

    public String getReadableDate() {
        return DateConversionUtils.getReadableDateTime(createdAt);
    }

    private static boolean isMention(JSONObject entity) throws JSONException {
        if(!entity.has("user_mentions")) return false;
        JSONArray mentions = entity.getJSONArray("user_mentions");

        for(int i = 0; i< mentions.length(); i++){
            JSONObject mention = (JSONObject) mentions.get(i);
            if (mention.get("screen_name").equals("adilansari")){
                return true;
            }
        }
        return false;
    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null)
            return null;

        Tweet tweet = new Tweet();

        tweet.tweetId = jsonObject.getLong("id");
        tweet.text = jsonObject.getString("text");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweetCount = jsonObject.getInt("retweet_count");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.createdAt = DateConversionUtils.getDateFromString(jsonObject.getString("created_at"));
        tweet.user = User.findOrCreateFromJson(jsonObject.getJSONObject("user"));
        tweet.media = Media.fromJson(jsonObject);
        tweet.isMention = isMention(jsonObject.getJSONObject("entities"));

        tweet.save();
        return tweet;
    }

    public static List<Tweet> fromJson(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null)
            return null;

        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson((JSONObject) jsonArray.get(i)));
        }

        return tweets;
    }

    public static Tweet byId(long id) {
        return new Select().from(Tweet.class).where("tweet_id = ?", id).executeSingle();
    }

    public static List<Tweet> recentTweets() {
        return new Select().from(Tweet.class).orderBy("tweet_id DESC").where("is_mention = ?", false).limit("25").execute();
    }

    public static List<Tweet> olderTweets(Tweet t) {
        if (t == null)
            return recentTweets();
        return new Select().from(Tweet.class).orderBy("tweet_id DESC").where("tweet_id < ?", t.tweetId).where("is_mention = ?", false).limit("25").execute();
    }

    public static List<Tweet> recentMentions(){
        return new Select().from(Tweet.class).orderBy("tweet_id DESC").where("is_mention = ?", true).limit("25").execute();
    }

    public static List<Tweet> olderMentions(Tweet t){
        if (t == null)
            return recentMentions();
        return new Select().from(Tweet.class).orderBy("tweet_id DESC").where("tweet_id < ?", t.tweetId).where("is_mention = ?", true).limit("25").execute();
    }

}
