package org.androidtown.gympalai.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.androidtown.gympalai.database.GymPalDB;

public class SeasonUpdateWorker extends Worker {
    public SeasonUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    GymPalDB db;


    @NonNull
    @Override
    public Result doWork() {

        //DB 생성
        db = GymPalDB.getInstance(getApplicationContext());
        db.rankingDao().seasonUpdate();
        return Result.success();
    }
}
