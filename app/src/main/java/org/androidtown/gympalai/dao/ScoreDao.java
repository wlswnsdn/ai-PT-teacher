package org.androidtown.gympalai.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.androidtown.gympalai.entity.Score;

import java.util.List;

@Dao
public interface ScoreDao {
    @Query("select * from score")
    LiveData<List<Score>> getAll();

    @Query(("SELECT * FROM score WHERE DATE(date) = DATE('now') AND userId = :userId"))
    Score getScore(String userId);

    @Insert
    void insert (Score score);

    @Query("UPDATE score SET Score=:score , date=DATE('now') WHERE scoreId=:scoreId")
    void updateScore (long scoreId, int score);

    @Delete
    void delete (Score score);

    @Query("SELECT * FROM score Where userId = :userId")
    LiveData<List<Score>> getUserScoreList(String userId); //Graph생성시 필요
}
