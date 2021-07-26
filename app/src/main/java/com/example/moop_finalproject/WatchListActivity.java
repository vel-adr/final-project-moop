package com.example.moop_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WatchListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tv_empty;
    ProgressBar progressBar;
    BottomNavigationView nav;
    LinearLayout emptyLayout;
    ArrayList<Movies> moviesArrayList;
    int failedIdReceived = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

//        Get all component required by ID
        recyclerView = findViewById(R.id.rv_watchlist);
        tv_empty = findViewById(R.id.tv_wl_empty);
        progressBar = findViewById(R.id.pb_watchlist);
        nav = findViewById(R.id.nav_wl_bottom);
        emptyLayout = findViewById(R.id.wl_empty_layout);

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);

//        Get Movie Data
        getMovieData();

        //        Nav
        nav.setSelectedItemId(R.id.nav_watchlist);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_watchlist:
                        return true;
                }
                return false;
            }
        });
    }

    public void getMovieData(){
        List<WatchList> watchListData = MainActivity.watchListDatabase.watchDao().getWatchListData();
        if(watchListData.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            MovieDataService movieDataService = new MovieDataService(WatchListActivity.this);
            moviesArrayList = new ArrayList<>();

            for (int i = 0; i < watchListData.size(); i++) {
                movieDataService.getMovieDataDetail(watchListData.get(i).getImbdID(), new MovieDataService.VolleyResponseListenerDetail() {
                    @Override
                    public void onError(String message) {
                        failedIdReceived += 1;
                    }

                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        Movies m = new Movies(response.getString("imdbID"), response.getString("Title"), response.getString("Year"), response.getString("Type"), response.getString("Poster"));
                        moviesArrayList.add(m);
                    }
                });
            }
            if(failedIdReceived > 0) {
                Toast.makeText(WatchListActivity.this,"Failed to retrieved " + String.valueOf(failedIdReceived) + " items", Toast.LENGTH_SHORT).show();
            }

//        Adapter
            layoutManager = new LinearLayoutManager(WatchListActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MovieAdapter(moviesArrayList, WatchListActivity.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
        }
    }

}