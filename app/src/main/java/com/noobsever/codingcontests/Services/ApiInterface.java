package com.noobsever.codingcontests.Services;

import com.noobsever.codingcontests.Models.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
/**url: https://kontests.net/api/v1/all
 * */
//    @GET("/data")
//    Call<ApiResponse> getAllContestsFromApi();
    @GET("/all")
    Call<ApiResponse> getAllContestsFromApi();

}
