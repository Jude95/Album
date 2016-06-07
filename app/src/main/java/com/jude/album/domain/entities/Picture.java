package com.jude.album.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zhuchenxi on 16/6/1.
 */

public class Picture implements Parcelable  ,Serializable {
    String id;
    String name;
    String intro;
    int width;
    int height;
    String src;
    String tag;
    @SerializedName("author_id")
    String authorId;
    @SerializedName("author_avatar")
    String authorAvatar;
    @SerializedName("author_name")
    String authorName;
    @SerializedName("author_picture_count")
    int authorPictureCount;
    @SerializedName("watch_count")
    int watchCount;
    @SerializedName("collection_count")
    int collectionCount;
    @SerializedName("album_id")
    String albumId;
    @SerializedName("create_time")
    long createTime;

    public Picture() {
    }

    public Picture(String id, String name, String intro, int width, int height, String src, String tag, String authorId, String authorAvatar, int watchCount, int collectionCount, String albumId, long createTime, String authorName) {
        this.id = id;
        this.name = name;
        this.intro = intro;
        this.width = width;
        this.height = height;
        this.src = src;
        this.tag = tag;
        this.authorId = authorId;
        this.authorAvatar = authorAvatar;
        this.watchCount = watchCount;
        this.collectionCount = collectionCount;
        this.albumId = albumId;
        this.createTime = createTime;
        this.authorName = authorName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public int getAuthorPictureCount() {
        return authorPictureCount;
    }

    public void setAuthorPictureCount(int authorPictureCount) {
        this.authorPictureCount = authorPictureCount;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.intro);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.src);
        dest.writeString(this.tag);
        dest.writeString(this.authorId);
        dest.writeString(this.authorAvatar);
        dest.writeString(this.authorName);
        dest.writeInt(this.authorPictureCount);
        dest.writeInt(this.watchCount);
        dest.writeInt(this.collectionCount);
        dest.writeString(this.albumId);
        dest.writeLong(this.createTime);
    }

    protected Picture(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.intro = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.src = in.readString();
        this.tag = in.readString();
        this.authorId = in.readString();
        this.authorAvatar = in.readString();
        this.authorName = in.readString();
        this.authorPictureCount = in.readInt();
        this.watchCount = in.readInt();
        this.collectionCount = in.readInt();
        this.albumId = in.readString();
        this.createTime = in.readLong();
    }

    public static final Parcelable.Creator<Picture> CREATOR = new Parcelable.Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel source) {
            return new Picture(source);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };
}
