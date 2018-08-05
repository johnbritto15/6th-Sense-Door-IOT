package com.quasar.nemesis.a6thsense.Interfaces;

import com.quasar.nemesis.a6thsense.Models.Datum;
import com.quasar.nemesis.a6thsense.RecognitionModels.RootData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by vipla on 31-03-2018.
 */

public interface RecognitionInterface {

    @Multipart
    @Headers({

            "app_id:de04b272",
            "app_key:3d55d8284df120c1913cf19af6e4c1c2"
    })
    @POST("/enroll")
    Call<Datum> getImages(@Part  MultipartBody.Part image,@Part("subject_id") RequestBody subject, @Part("gallery_name") RequestBody gallery);


    @Multipart
    @Headers({

            "app_id:de04b272",
            "app_key:3d55d8284df120c1913cf19af6e4c1c2"
    })
    @POST("/recognize")
    Call<RootData> getImages(@Part  MultipartBody.Part image, @Part("gallery_name") RequestBody gallery);



}
