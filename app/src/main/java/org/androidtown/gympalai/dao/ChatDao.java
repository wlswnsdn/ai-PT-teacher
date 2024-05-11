package org.androidtown.gympalai.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.androidtown.gympalai.entity.Chat;

import java.util.List;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chat WHERE userId = :userId")
    LiveData<List<Chat>> getAll(String userId);

    @Query("SELECT exerciseList FROM chat WHERE date = DATE('now') AND userId = :userId AND exerciseList IS NOT NULL; -- exerciseList가 null이 아닌 경우 선택\n")
    String getExerciseList(String userId);
    @Query("SELECT dietList FROM chat WHERE date = DATE('now') AND userId = :userId AND dietList IS NOT NULL; -- exerciseList가 null이 아닌 경우 선택\n")
    String getDietList(String userId);
    @Insert
    void insert(Chat chat);

    @Update
    void update(Chat chat);

    @Delete
    void delete(Chat chat);
}
