package com.example.moop_finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WatchList.class}, version = 1)
public abstract class WatchListDatabase extends RoomDatabase {

    public abstract WatchDao watchDao();
}
