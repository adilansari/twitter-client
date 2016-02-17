package com.codepath.apps.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

@Table(name = "tweets")
public class Tweet extends Model {

	@Column(name="id", unique = true)
	private long id;

	@Column(name = "text")
	private String text;

	@Column(name = "in_reply_to_screen_name")
	private String inReplyToScreenName;

	@Column(name = "retweeted")
	private boolean retweeted;

	@Column(name = "favorited")
	private boolean favorited;

	@Column(name = "retweet_count")
	private int retweetCount;

	@Column(name = "favorite_count")
	private int favoriteCount;

    @Column(name = "created_at", index = true)
    private Date createdAt;

	public Tweet() {
		super();
	}


	public static Tweet byId(long id) {
		return new Select().from(Tweet.class).where("id = ?", id).executeSingle();
	}

	public static List<Tweet> recentItems() {
		return new Select().from(Tweet.class).orderBy("id DESC").limit("25").execute();
	}
}
