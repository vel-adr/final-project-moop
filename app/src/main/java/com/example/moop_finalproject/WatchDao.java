package com.example.moop_finalproject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WatchDao {
    @Insert
    public void addData(WatchList watchList);

    @Query("select * from watchlist")
    public List<WatchList> getWatchListData();

    @Query("select exists (select 1 from watchlist where imdbID=:imdbID)")
    public int isInWatchList(String imdbID);

    @Query("delete from watchlist where imdbID=:imdbID")
    public int deleteByImdbID(String imdbID);
}
