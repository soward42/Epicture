package com.example.hugopeuz.epicture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

public class UserProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Intent i;
    User user;
    TextView t;
    Retrofit retrofit = null;
    GridView userGallery = null;
    public static final String BASE_URL = "https://api.imgur.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        i = this.getIntent();


        user = (User)i.getSerializableExtra("USER");
        t = headerView.findViewById(R.id.UserNameMenu);
        t.setText(user.ClientName);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<Avatar> avatarCall = apiInterface.getAvatarImage("Bearer " + user.AccessToken);

        avatarCall.enqueue(new Callback<Avatar>() {
            @Override
            public void onResponse(Call<Avatar> avatarCall, Response<Avatar> avatarResponse) {
                if (!avatarResponse.isSuccessful()) {
                    Log.e("API", "Unable to load avatar image: " + avatarResponse);
                    return;
                }
                Glide.with(getApplicationContext())
                        .load(avatarResponse.body().getData().getAvatar())
                        .apply(RequestOptions.circleCropTransform())
                        .into((ImageView)findViewById(R.id.MenuAvatarImage));
            }

            @Override
            public void onFailure(Call<Avatar> call, Throwable t) {
                Log.e("API", "Failed to get response");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Call<Images> UserGalleryCall = apiInterface.getGalleryImages("Bearer " + user.AccessToken);
        UserGalleryCall.enqueue(new Callback<Images>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<Images> call, Response<Images> response) {
                if (!response.isSuccessful()) {
                    Log.e("API", "Unable to load personal gallery: " + response);
                    return;
                }
                List<String> imagesLink = new ArrayList<>();
                for (Datum datum : response.body().data){
                    imagesLink.add(datum.link);
                }
                userGallery = findViewById(R.id.UserGallery);
                userGallery.setAdapter(new ImageAdapter(UserProfile.this, imagesLink));
            }

            @Override
            public void onFailure(Call<Images> call, Throwable t) {
                Log.e("API", "Unable to send request");
            }
        });
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
        getMenuInflater().inflate(R.menu.user_profile, menu);
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
                Intent gallery = new Intent(UserProfile.this, UserProfile.class);
                gallery.putExtra("USER", user);
                startActivity(gallery);
                finish();
                break;
            case R.id.NavFav:
                Intent fav = new Intent(UserProfile.this, FavoritesActivity.class);
                fav.putExtra("USER", user);
                startActivity(fav);
                finish();
                break;
            case R.id.NavTrending:
                Intent trend = new Intent(UserProfile.this, TrendingActivity.class);
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
