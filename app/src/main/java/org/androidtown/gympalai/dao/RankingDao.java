package org.androidtown.gympalai.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.androidtown.gympalai.entity.Ranking;
import org.androidtown.gympalai.entity.Score;

import java.util.List;

@Dao
public interface RankingDao {
    @Query("select * from ranking")
    LiveData<List<Ranking>> getAll();

    @Query(("SELECT * FROM ranking WHERE DATE(date) = DATE('now') AND userId = :userId"))
    Ranking getRanking(String userId);

    @Insert
    void insert (Ranking ranking);

    @Query("UPDATE ranking SET Score=:score , date=DATE('now') WHERE rankingId=:rankingId")
    void updateRanking (long rankingId, int score);

    @Delete
    void delete (Ranking ranking);
}
