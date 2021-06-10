package com.noobsever.codingcontests.Services;

import com.noobsever.codingcontests.Models.ApiResponse;
import com.noobsever.codingcontests.Models.ContestObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
/**url: https://kontests.net/api/v1/all
 * */
//    @GET("/data")
//    Call<ApiResponse> getAllContestsFromApi();
    @GET("api/v1/all")
    Call<List<ContestObject>> getAllContestsFromApi();

}
