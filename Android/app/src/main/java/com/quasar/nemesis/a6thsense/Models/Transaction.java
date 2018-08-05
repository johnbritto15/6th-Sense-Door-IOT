package com.quasar.nemesis.a6thsense.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("topLeftX")
    @Expose
    private Integer topLeftX;
    @SerializedName("topLeftY")
    @Expose
    private Integer topLeftY;
    @SerializedName("gallery_name")
    @Expose
    private String galleryName;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("quality")
    @Expose
    private Double quality;
    @SerializedName("confidence")
    @Expose
    private Double confidence;
    @SerializedName("subject_id")
    @Expose
    private String subjectId;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("face_id")
    @Expose
    private String faceId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTopLeftX() {
        return topLeftX;
    }

    public void setTopLeftX(Integer topLeftX) {
        this.topLeftX = topLeftX;
    }

    public Integer getTopLeftY() {
        return topLeftY;
    }

    public void setTopLeftY(Integer topLeftY) {
        this.topLeftY = topLeftY;
    }

    public String getGalleryName() {
        return galleryName;
    }

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getQuality() {
        return quality;
    }

    public void setQuality(Double quality) {
        this.quality = quality;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }
}
