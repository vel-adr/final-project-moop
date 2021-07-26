package com.example.moop_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetail extends AppCompatActivity {

    ImageView img_poster;
    ImageButton img_eye;
    TextView titleTxt, releasedTxt, durationTxt, imdbRatingTxt, genreTxt, plotTxt;
    String imdbID;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

//        Get required component by ID
        intent = getIntent();
        imdbID = intent.getStringExtra("imdbID");
        img_poster = findViewById(R.id.img_detail_poster);
        titleTxt = findViewById(R.id.tv_detail_title);
        releasedTxt = findViewById(R.id.tv_detail_released);
        durationTxt = findViewById(R.id.tv_detail_duration);
        imdbRatingTxt = findViewById(R.id.tv_detail_imdbRating);
        genreTxt = findViewById(R.id.tv_detail_genre);
        plotTxt = findViewById(R.id.tv_detail_plot);
        img_eye = findViewById(R.id.img_detail_eye);

//        Request movie data to API
        MovieDataService movieDataService = new MovieDataService(MovieDetail.this);
        movieDataService.getMovieDataDetail(imdbID, new MovieDataService.VolleyResponseListenerDetail() {
            @Override
            public void onError(String message) {
                Toast.makeText(MovieDetail.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                Glide.with(MovieDetail.this).load(response.getString("Poster")).into(img_poster);
                titleTxt.setText(response.getString("Title"));
                releasedTxt.setText(response.getString("Released"));
                durationTxt.setText(response.getString("Runtime"));
                imdbRatingTxt.setText("IMDB Rating: " + response.getString("imdbRating"));
                genreTxt.setText("Genre: " + response.getString("Genre"));
                plotTxt.setText(response.getString("Plot"));
            }
        });
        if(MainActivity.watchListDatabase.watchDao().isInWatchList(imdbID) == 1){
            img_eye.setImageResource(R.drawable.ic_favorite);
        } else {
            img_eye.setImageResource(R.drawable.ic_favorite_border);
        }

        img_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WatchList watchList = new WatchList();
                watchList.setImbdID(imdbID);

                if(MainActivity.watchListDatabase.watchDao().isInWatchList(imdbID) != 1){
                    img_eye.setImageResource(R.drawable.ic_favorite);
                    MainActivity.watchListDatabase.watchDao().addData(watchList);
                    Toast.makeText(MovieDetail.this, "Movie has been added to watchlist successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    img_eye.setImageResource(R.drawable.ic_favorite_border);
                    MainActivity.watchListDatabase.watchDao().deleteByImdbID(imdbID);
                    Toast.makeText(MovieDetail.this, "Movie has been removed to watchlist successfully!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}