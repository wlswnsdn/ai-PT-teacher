package org.androidtown.gympalai.backmethod;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.dao.RankingDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.Ranking;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RankingSettings extends AppCompatActivity {
    //DB 생성
    GymPalDB db = GymPalDB.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            List<String> top10 = new rankingAsyncTask(db.rankingDao()).execute("hi").get();
            
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public static class rankingAsyncTask extends AsyncTask<String, Void, List<String>> {

        private RankingDao rankingDao;

        public rankingAsyncTask(RankingDao rankingDao) {
            this.rankingDao = rankingDao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected List<String> doInBackground(String ... strings) {
            List<String> top10 = rankingDao.getTop10();
            return top10;

        }

    }
}
