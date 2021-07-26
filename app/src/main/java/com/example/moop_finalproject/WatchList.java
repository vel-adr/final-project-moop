package com.example.moop_finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "watchlist")
public class WatchList {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "imdbID")
    private String imbdID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImbdID() {
        return imbdID;
    }

    public void setImbdID(String imbdID) {
        this.imbdID = imbdID;
    }
}
