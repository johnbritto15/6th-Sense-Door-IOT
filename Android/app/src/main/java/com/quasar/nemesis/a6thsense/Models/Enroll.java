package com.quasar.nemesis.a6thsense.Models;

import com.google.gson.annotations.SerializedName;



public class Enroll {
    @SerializedName("image")
    public byte[] image;

    @SerializedName("subject_id")
    public String subject_id;

    @SerializedName("gallery_name")
    public String gallery_name;
}
