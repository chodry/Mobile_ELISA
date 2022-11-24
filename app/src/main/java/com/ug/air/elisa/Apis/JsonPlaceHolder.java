package com.ug.air.elisa.Apis;

import com.ug.air.elisa.Models.Token;
import com.ug.air.elisa.Models.User;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JsonPlaceHolder {

    @POST("login/")
    Call<Token> login(@Body User user);

//    @Multipart
//    @POST("saveData/")
//    Call<String> sendFile(@Header("Authorization") String header, @Part MultipartBody.Part patient);



}
