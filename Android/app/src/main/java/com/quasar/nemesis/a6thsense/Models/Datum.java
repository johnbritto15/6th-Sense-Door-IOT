package com.quasar.nemesis.a6thsense.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vipla on 31-03-2018.
 */

public class Datum {
    @SerializedName("face_id")
    @Expose
    private String face_id;

    @SerializedName("images")
    @Expose
    private List<Images> images = null;

    public String getFaceId() {
        return face_id;
    }

    public void setFaceId(String face_id) {
        this.face_id = face_id;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

}
