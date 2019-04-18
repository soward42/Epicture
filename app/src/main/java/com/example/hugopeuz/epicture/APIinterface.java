package com.example.hugopeuz.epicture;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/*
** APIInterface class integrate supported call to Imgur's API
** If any call should be added, it should be done here
*/

interface APIInterface {
    // Call to the API to get the profile picture
    @GET("3/account/me/avatar")
    Call<Avatar> getAvatarImage(
            @Header("Authorization") String accessToken
    );

    // Call to the API to get user's gallery
    @GET("3/account/me/images")
    Call<Images> getGalleryImages(
            @Header("Authorization") String accessToken
    );

    // Call to the API to get today viral gallery
    @GET("3/gallery/hot/viral/day")
    Call<Gallery> getTrendingGallery(
            @Header("Authorization") String clientId
    );

    // Call to the API to get users's favorites pics
    @GET("3/account/me/gallery_favorites/")
    Call<Gallery> getUsersFav(
            @Header("Authorization") String accessToken
    );

    // Call to the API to get the correspondant gallery to a keyword
    @GET("3/gallery/search/")
    Call<Gallery> getSearchedImages(
            @Header("Authorization") String accessToken,
            @Query("q") String search
    );
}
