package com.jude.album.domain.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhuchenxi on 16/6/16.
 */

public class PictureDescribeResult {
    int errorcode;
    String errormsg;
    List<PictureTag> tags;

    public class PictureTag {
        @SerializedName("tag_name")
        String tagName;
        @SerializedName("tag_confidence")
        String tagConfidence;

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public String getTagConfidence() {
            return tagConfidence;
        }

        public void setTagConfidence(String tagConfidence) {
            this.tagConfidence = tagConfidence;
        }
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public List<PictureTag> getTags() {
        return tags;
    }

    public void setTags(List<PictureTag> tags) {
        this.tags = tags;
    }
}
