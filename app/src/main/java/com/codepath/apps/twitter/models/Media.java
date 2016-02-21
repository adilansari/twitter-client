package com.codepath.apps.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel(analyze = {Media.class})
@Table(name = "media")
public class Media extends Model {

    @Column(name="type")
    public TYPE type;

    @Column(name="img_url", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String imgUrl;

    @Column(name="vid_url")
    public String vidUrl;

    public Media(){}

    public static Media fromJson(JSONObject jsonObject) throws JSONException{
        if (jsonObject == null) return null;

        if (jsonObject.has("extended_entities")) return videoFromJson(jsonObject.getJSONObject("extended_entities"));

        if (jsonObject.has("entity")) return photoFromJson(jsonObject.getJSONObject("entity"));

        return null;
    }

    private static Media photoFromJson(JSONObject entityObject) throws JSONException {
        if (entityObject.getJSONArray("media").length() == 0) return null;

        JSONObject entity= (JSONObject) entityObject.getJSONArray("media").get(0);

        if (entity.getString("type") != "photo") return null;

        Media media= findOrCreate(entity.getString("media_url"));
        if (media != null) return media;

        media = new Media();
        media.type = TYPE.PHOTO;
        media.imgUrl = entity.getString("media_url");
        media.save();

        return media;
    }

    private static Media videoFromJson(JSONObject entityObject) throws JSONException {
        if (entityObject.getJSONArray("media").length() == 0) return null;

        JSONObject entity= (JSONObject) entityObject.getJSONArray("media").get(0);

        String type = entity.getString("type");
        if (!(type.equalsIgnoreCase("video") || type.equalsIgnoreCase("animated_gif"))){
            return null;
        }

        Media media= findOrCreate(entity.getString("media_url"));
        if (media != null) return media;

        media = new Media();
        media.type = TYPE.VIDEO;
        media.imgUrl = entity.getString("media_url");

        JSONArray vidVariants = entity.getJSONObject("video_info").getJSONArray("variants");
        for(int i = 0; i< vidVariants.length(); i++){
            if(vidVariants.getJSONObject(i).getString("content_type").equals("video/mp4"))
                media.vidUrl = vidVariants.getJSONObject(i).getString("url");
        }

        if (media.vidUrl == null) return null;
        media.save();

        return media;
    }

    private static Media findOrCreate(String mediaUrl) throws JSONException {
        return new Select().from(Media.class).where("img_url = ?", mediaUrl).executeSingle();
    }



    enum TYPE {
        VIDEO("video"), PHOTO("photo");

        private String value;

        TYPE(String value){
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}