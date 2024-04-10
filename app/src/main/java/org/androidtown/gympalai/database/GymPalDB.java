package org.androidtown.gympalai.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.androidtown.gympalai.entity.Avatar;
import org.androidtown.gympalai.entity.User;

@Database(entities = {User.class, Avatar.class}, version = 1)
public abstract class GymPalDB extends RoomDatabase {

    private static GymPalDB instance = null;

    public static GymPalDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), GymPalDB.class, "GymPalDB").build();
        }
        return instance;
    }

    public static void destroyInstance(){
        instance = null;
    }
}
