package com.jude.album.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuchenxi on 16/6/3.
 */

public class User implements Parcelable ,Serializable{
    String id;
    String name;
    String avatar;
    String background;
    int gender;
    String intro;
    @SerializedName("is_followed")
    boolean isFollowed;
    List<Picture> pictures;
    @SerializedName("collection_picture")
    List<Picture> collectionPictures;
    List<User> fans;
    List<User> star;
    String token;

    public User(String id, String name, String avatar, String background, int gender, String intro, List<Picture> pictures, List<Picture> collectionPictures, List<User> fans, List<User> star) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.background = background;
        this.gender = gender;
        this.intro = intro;
        this.pictures = pictures;
        this.collectionPictures = collectionPictures;
        this.fans = fans;
        this.star = star;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public List<Picture> getCollectionPictures() {
        return collectionPictures;
    }

    public void setCollectionPictures(List<Picture> collectionPictures) {
        this.collectionPictures = collectionPictures;
    }

    public List<User> getFans() {
        return fans;
    }

    public void setFans(List<User> fans) {
        this.fans = fans;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public List<User> getStar() {
        return star;
    }

    public void setStar(List<User> star) {
        this.star = star;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeString(this.background);
        dest.writeInt(this.gender);
        dest.writeString(this.intro);
        dest.writeByte(this.isFollowed ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.pictures);
        dest.writeTypedList(this.collectionPictures);
        dest.writeTypedList(this.fans);
        dest.writeTypedList(this.star);
        dest.writeString(this.token);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
        this.background = in.readString();
        this.gender = in.readInt();
        this.intro = in.readString();
        this.isFollowed = in.readByte() != 0;
        this.pictures = in.createTypedArrayList(Picture.CREATOR);
        this.collectionPictures = in.createTypedArrayList(Picture.CREATOR);
        this.fans = in.createTypedArrayList(User.CREATOR);
        this.star = in.createTypedArrayList(User.CREATOR);
        this.token = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
