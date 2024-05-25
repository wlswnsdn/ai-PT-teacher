package org.androidtown.gympalai.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.androidtown.gympalai.entity.Chat;
import org.androidtown.gympalai.entity.HealthInfo;

import java.util.List;

@Dao
public interface HealthInfoDao {
    @Query("select * from healthInfo")
    LiveData<List<HealthInfo>> getAll();

//    @Query("SELECT * FROM healthInfo WHERE userId = :userId")
//    LiveData<HealthInfo> getHealthInfo(String userId);

    @Query("SELECT * FROM healthInfo WHERE userId = :userId")
    LiveData<HealthInfo> getUserInfo(String userId);

    @Query("Update healthInfo SET height= :height, weight = :weight, purpose= :purpose WHERE userId = :userId ")
    void updateHealthInfo(String userId, float height, float weight, int purpose);

    @Query("SELECT * FROM healthInfo WHERE userId = :userId")
    HealthInfo getHealthInfoByUserId(String userId);
    @Insert
    void insert (HealthInfo healthInfo);

    @Update
    void update (HealthInfo healthInfo);

    @Delete
    void delete (HealthInfo healthInfo);

}
