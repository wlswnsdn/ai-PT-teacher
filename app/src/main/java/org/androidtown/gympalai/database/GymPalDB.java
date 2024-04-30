package org.androidtown.gympalai.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.androidtown.gympalai.converter.Converters;
import org.androidtown.gympalai.dao.AvatarDao;
import org.androidtown.gympalai.dao.UserDao;
import org.androidtown.gympalai.entity.Avatar;
import org.androidtown.gympalai.entity.User;
// 데이터베이스에 넣을 테이블 추가,
@Database(entities = {User.class, Avatar.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class GymPalDB extends RoomDatabase {

    private static GymPalDB instance = null;

    // 테이블의 Dao 추가
    public abstract UserDao userDao();

    public abstract AvatarDao avatarDao();

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
