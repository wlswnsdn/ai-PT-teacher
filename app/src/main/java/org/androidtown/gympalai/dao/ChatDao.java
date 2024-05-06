package org.androidtown.gympalai.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.androidtown.gympalai.entity.Chat;
import org.androidtown.gympalai.entity.User;

import java.util.List;

@Dao
public interface ChatDao {

    @Query("select * from chat")
    LiveData<List<Chat>> getAll();

    @Insert
    void insert(Chat chat);

    @Update
    void update(Chat chat);

    @Delete
    void delete(Chat chat);
}
