package org.androidtown.gympalai.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.androidtown.gympalai.entity.Ranking;

import java.util.List;

@Dao
public interface RankingDao {
    @Query("select * from ranking")
    LiveData<List<Ranking>> getAll();

    @Insert
    void insert (Ranking ranking);

    @Update
    void update (Ranking ranking);

    @Delete
    void delete (Ranking ranking);
}
