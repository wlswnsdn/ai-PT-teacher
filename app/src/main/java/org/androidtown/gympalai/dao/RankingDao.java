package org.androidtown.gympalai.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.androidtown.gympalai.entity.Ranking;
import org.androidtown.gympalai.entity.Score;
import org.androidtown.gympalai.model.UserTotalScore;

import java.util.List;

@Dao
public interface RankingDao {
    @Query("select * from ranking")
    LiveData<List<Ranking>> getAll();

    @Query("SELECT userId, SUM(Score) as totalScore FROM ranking GROUP BY userId ORDER BY totalScore DESC LIMIT 10")
    List<UserTotalScore> getTop10();

    @Query("SELECT SUM(Score) AS totalScore FROM ranking WHERE userId = :userId GROUP BY userId")
    int getUserTotalScore(String userId);

    @Query(("SELECT * FROM ranking WHERE DATE(date) = DATE('now') AND userId = :userId"))
    Ranking getRanking(String userId);

    @Insert
    void insert (Ranking ranking);

    @Query("UPDATE ranking SET Score=:score , date=DATE('now') WHERE rankingId=:rankingId")
    void updateRanking (long rankingId, int score);

    @Delete
    void delete (Ranking ranking);

    @Query("DELETE FROM ranking WHERE date <= date('now', '-14 days')")
    void seasonUpdate();
}
