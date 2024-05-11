package org.androidtown.gympalai.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.androidtown.gympalai.entity.HealthInfo;

import java.util.List;

@Dao
public interface HealthInfoDao {
    @Query("select * from healthInfo")
    LiveData<List<HealthInfo>> getAll();

    @Insert
    void insert (HealthInfo healthInfo);

    @Update
    void update (HealthInfo healthInfo);

    @Delete
    void delete (HealthInfo healthInfo);

}
