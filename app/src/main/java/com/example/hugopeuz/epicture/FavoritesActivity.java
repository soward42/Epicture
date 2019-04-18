package com.example.hugopeuz.epicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoritesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Intent i;
    User user;
    TextView t;
    Retrofit retrofit = null;
    GridView trendingGallery = null;
    public static final String BASE_URL = "https://api.imgur.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);

        i = this.getIntent();
        user = (User)i.getSerializableExtra("USER");


        //Set Username in the menu's header
        t = headerView.findViewById(R.id.FavUsername);
        t.setText(user.ClientName);

        //Initiate Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Initiate retrofit with our Imgur's API call
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<Avatar> avatarCall = apiInterface.getAvatarImage("Bearer " + user.AccessToken);

        //Setting our call on queue
        avatarCall.enqueue(new Callback<Avatar>() {
            @Override
            public void onResponse(Call<Avatar> avatarCall, Response<Avatar> avatarResponse) {
                if (!avatarResponse.isSuccessful()) {
                    Log.e("API", "Unable to load avatar image: " + avatarResponse);
                    return;
                }
                //setting the Avatar image in the menu
                Glide.with(getApplicationContext())
                        .load(avatarResponse.body().getData().getAvatar())
                        .apply(RequestOptions.circleCropTransform())
                        .into((ImageView)findViewById(R.id.FavAvatar));
            }
            @Override
            public void onFailure(Call<Avatar> call, Throwable t) {
                Log.e("API", "Failed to get response");
            }
        });


        //Initiate call to get the trending gallery
        Call<Gallery> favoriteGalleryCall = apiInterface.getUsersFav("Bearer " + user.AccessToken);
        favoriteGalleryCall.enqueue(new Callback<Gallery>() {
            @Override
            public void onResponse(Call<Gallery> call, Response<Gallery> response) {
                if (!response.isSuccessful()) {
                    Log.e("API[favorites]", "Error in the url: " + response);
                    return;
                }
                List<String> imagesLinks = new ArrayList<>();
                List<GalleryDatum> datum = response.body().data;
                for (int i = 0; i < datum.size(); i++) {
                    List<GalleryImage> images = datum.get(i).images;
                    if (images == null)
                        continue;
                    imagesLinks.add(images.get(0).link);
                }
                trendingGallery = findViewById(R.id.FavoritesGallery);
                trendingGallery.setAdapter(new ImageAdapter(FavoritesActivity.this, imagesLinks));
            }

            @Override
            public void onFailure(Call<Gallery> call, Throwable t) {
                Log.e("API[Favorite]", "Unknown error occured");
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.NavGallery:
                Intent gallery = new Intent(FavoritesActivity.this, UserProfile.class);
                gallery.putExtra("USER", user);
                startActivity(gallery);
                finish();
                break;
            case R.id.NavFav:
                Intent fav = new Intent(FavoritesActivity.this, FavoritesActivity.class);
                fav.putExtra("USER", user);
                startActivity(fav);
                finish();
                break;
            case R.id.NavTrending:
                Intent trend = new Intent(FavoritesActivity.this, TrendingActivity.class);
                trend.putExtra("USER", user);
                startActivity(trend);
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
