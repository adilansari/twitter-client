package com.codepath.apps.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.twitter.utils.DateConversionUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "tweets")
public class Tweet extends Model {

	@Column(name="tweet_id", unique = true, index = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public long tweetId;

	@Column(name = "text")
	public String text;

	@Column(name = "in_reply_to_screen_name")
	public String inReplyToScreenName;

	@Column(name = "retweeted")
	public boolean retweeted;

	@Column(name = "favorited")
	public boolean favorited;

	@Column(name = "retweet_count")
	public int retweetCount;

	@Column(name = "favorite_count")
	public int favoriteCount;

    @Column(name = "created_at", index = true)
    private Date createdAt;

    @Column(name = "user")
    public User user;

	public Tweet() {
		super();
	}

    public String getRelativeTimestamp() {
        return DateConversionUtils.getRelativeTimeStamp(createdAt);
    }

    private static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null)
            return null;

        Tweet tweet = new Tweet();

        tweet.tweetId = jsonObject.getLong("id");
        tweet.text = jsonObject.getString("text");

        if (!jsonObject.isNull("in_reply_to_screen_name"))
            tweet.inReplyToScreenName = jsonObject.getString("in_reply_to_screen_name");

        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweetCount = jsonObject.getInt("retweet_count");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.createdAt = DateConversionUtils.getDateFromString(jsonObject.getString("created_at"));
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        tweet.save();
        return tweet;
    }

    public static List<Tweet> fromJson(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null)
            return null;

        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson((JSONObject) jsonArray.get(i)));
        }

        return tweets;
    }


	public static Tweet byId(long id) {
		return new Select().from(Tweet.class).where("tweet_id = ?", id).executeSingle();
	}

	public static List<Tweet> recentItems() {
		return new Select().from(Tweet.class).orderBy("tweet_id DESC").limit("25").execute();
	}
}
