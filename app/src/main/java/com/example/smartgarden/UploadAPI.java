package com.example.smartgarden;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UploadAPI {
    @Multipart
    @PUT("upload")
    Call<RequestBody> uploadImage(@Part MultipartBody.Part part, @Part("somedata") RequestBody requestBody );

}
