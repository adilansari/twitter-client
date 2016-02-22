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

	@Column(name="tweet_id", unique = true, index = true, onUniqueConflict = Column.ConflictAction.REPLACE)
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

	public Tweet() {}

    public String getRelativeTimestamp() {
        return DateConversionUtils.getRelativeTimeStamp(createdAt);
    }

    public boolean hasMedia(){
        return (this.media != null);
    }

    public boolean isPhotoTweet(){
        return (hasMedia() && (this.media.type == MediaType.PHOTO));
    }

    public boolean isVideoTweet(){
        return (hasMedia() && (this.media.type == MediaType.VIDEO));
    }

    public String getReadableDate(){
        return DateConversionUtils.getReadableDateTime(createdAt);
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

        tweet.save();
        return tweet;
    }

    public static void insertFromJson(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null)
            return;

        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson((JSONObject) jsonArray.get(i)));
        }
    }


	public static Tweet byId(long id) {
		return new Select().from(Tweet.class).where("tweet_id = ?", id).executeSingle();
	}

	public static List<Tweet> recentItems() {
		return new Select().from(Tweet.class).orderBy("tweet_id DESC").limit("25").execute();
	}

    public static List<Tweet> olderItems(Tweet t){
        if (t == null)
            return recentItems();
        return new Select().from(Tweet.class).orderBy("tweet_id DESC").where("tweet_id < ?", t.tweetId).limit("25").execute();
    }

}
