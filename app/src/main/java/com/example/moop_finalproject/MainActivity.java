package com.example.moop_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ImageButton searchBtn;
    LinearLayout emptyL;
    TextView tv_empty;
    EditText queryTxt;
    ProgressBar progressBar;
    public static WatchListDatabase watchListDatabase;
    BottomNavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Initialize database
        watchListDatabase = Room.databaseBuilder(getApplicationContext(), WatchListDatabase.class, "watchlistdb").allowMainThreadQueries().build();

//        Get all components required by ID
        recyclerView = findViewById(R.id.rv_movies);
        searchBtn = findViewById(R.id.btn_search);
        emptyL = findViewById(R.id.ll_empty);
        queryTxt = findViewById(R.id.et_queryInput);
        tv_empty = findViewById(R.id.tv_empty);
        progressBar = findViewById(R.id.pb_main);
        nav = findViewById(R.id.nav_bottom);

        progressBar.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);

//        Navigation
        nav.setSelectedItemId(R.id.nav_home);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_watchlist:
                        startActivity(new Intent(getApplicationContext(), WatchListActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


//        Add onClick listener to searchButton
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get query from user
                String query = queryTxt.getText().toString();

                if(query.isEmpty()){
                    Toast.makeText(MainActivity.this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    MovieDataService movieDataService = new MovieDataService(MainActivity.this);

                    // Set visibility of emptyLayout to gone
                    emptyL.setVisibility(View.VISIBLE);
                    tv_empty.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    movieDataService.getMovieDataShort(query, new MovieDataService.VolleyResponseListenerShort() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResponse(List<Movies> moviesList) {
                            // Set visibility of emptyLayout to gone
                            emptyL.setVisibility(View.GONE);

                            // Add movie cards to views
                            layoutManager = new LinearLayoutManager(MainActivity.this);
                            recyclerView.setLayoutManager(layoutManager);

                            mAdapter = new MovieAdapter(moviesList, MainActivity.this);
                            recyclerView.setAdapter(mAdapter);
                        }
                    });
                }
            }
        });
    }
}