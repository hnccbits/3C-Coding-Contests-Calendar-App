package com.hnccbits.codingcontestscalendar.RoomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hnccbits.codingcontestscalendar.Models.ContestObject;
import com.hnccbits.codingcontestscalendar.Utils.Constants;

@Database(entities = {ContestObject.class}, version = 2)
public abstract class RoomDB extends RoomDatabase {

    //TODO Room Functions to be written in this folder
    private static RoomDB instance;

    public abstract RoomDAO roomDAO();

    public static synchronized RoomDB getDatabaseInstance(Context context) {
        if (instance == null) {
            instance =  Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, Constants.ROOM_DB_NAME)
                            .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
