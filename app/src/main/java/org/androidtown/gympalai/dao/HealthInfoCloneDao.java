package org.androidtown.gympalai.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.androidtown.gympalai.entity.HealthInfoClone;

import java.util.List;
@Dao
public interface HealthInfoCloneDao {

    @Insert
    void insert (HealthInfoClone healthInfoClone);
    @Update
    void update (HealthInfoClone healthInfoClone);
    @Delete
    void delete (HealthInfoClone healthInfoClone);
    @Query("SELECT * FROM healthInfoClone WHERE userID = :userId")
    LiveData<List<HealthInfoClone>> getUserInfoList(String userId); // 그래프 만들 떄 사용

    @Query("UPDATE healthInfoClone SET height= :height, weight = :weight, purpose= :purpose WHERE userId = :userId")
    void updateHealthInfoClone(String userId, float height, float weight, int purpose);

}
