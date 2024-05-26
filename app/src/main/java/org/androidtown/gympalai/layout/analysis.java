package org.androidtown.gympalai.layout;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.backmethod.LoginFunction;
import org.androidtown.gympalai.dao.HealthInfoCloneDao;
import org.androidtown.gympalai.dao.HealthInfoDao;
import org.androidtown.gympalai.dao.ScoreDao;
import org.androidtown.gympalai.dao.UserDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.HealthInfo;
import org.androidtown.gympalai.entity.HealthInfoClone;
import org.androidtown.gympalai.entity.Score;
import org.androidtown.gympalai.entity.User;
import org.androidtown.gympalai.model.LoginId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.Locale;

public class analysis extends Fragment {

    // 세 개의 라인 차트를 정의
    LineChart chartWeight;
    LineChart chartScore;
    LineChart chartTDEE;

    GymPalDB db = GymPalDB.getInstance(getActivity());
    LoginFunction loginFunction = new LoginFunction();
//    LoginId loginId = new LoginId(); // 테스트를 위한 하드코딩

    // 프래그먼트가 생성될 때 호출되는 메서드
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // layout 파일을 인플레이트하여 View 객체 생성
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);

        // 각 차트에 대해 View를 할당
        chartWeight = view.findViewById(R.id.chart_weight);
        chartScore = view.findViewById(R.id.chart_score);
        chartTDEE = view.findViewById(R.id.chart_TDEE);

        // 차트 설정 초기화
        setupCharts();

        // 샘플 데이터 추가(테스트용)
        //insertSampleHealthInfo();
        //insertSampleScores();


        // 차트 데이터 로딩
        loadChartData();

        return view;
    }

    // 차트 초기 설정 메서드
    private void setupCharts() {
        // 세 개의 차트를 배열로 저장
        LineChart[] charts = {chartWeight, chartScore, chartTDEE};
        // 각 차트에 대해 설정 적용
        for (LineChart chart : charts) {
            // 설명 비활성화
            chart.getDescription().setEnabled(false);
            // 배경 그리드 비활성화
            chart.setDrawGridBackground(false);
            // 범례 비활성화
            chart.getLegend().setEnabled(false);
            // 오른쪽 축 비활성화
            chart.getAxisRight().setEnabled(false);

            // X축 설정
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축을 아래로 설정
            xAxis.setDrawGridLines(false); // 그리드 라인 비활성화
            xAxis.setValueFormatter(new DateValueFormatter()); // 값 포맷터 설정

            // 왼쪽 Y축 설정
            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setDrawGridLines(false); // 그리드 라인 비활성화

            chart.setDoubleTapToZoomEnabled(false); // 더블 클릭 줌 비활성화
            chart.setScaleEnabled(false); // 스케일(확대/축소) 비활성화

            chart.setVisibleXRangeMaximum(10); // 초기 화면에 보이는 데이터 포인트 수
            chart.setDragEnabled(true); // 차트를 드래그 가능하게 설정

            if (chart.getData() != null && chart.getData().getEntryCount() > 0) {
                float minX = chart.getData().getXMin() - 1; // 최소값을 첫 데이터 포인트보다 작게 설정
                xAxis.setAxisMinimum(minX);
            }
        }
    }

    // HealthInfoClone 테이블에 샘플 데이터 추가


//    private void insertSampleHealthInfo() {
//        // 샘플 데이터 생성
//        // 데이터베이스에 추가
//        new InsertAsyncTask1(db.userDao()).execute(new User(loginId.getLoginId(), "123", "123", "Ha"));
//        new InsertAsyncTask(db.healthInfoCloneDao()).execute(new HealthInfoClone(loginId.getLoginId(), 170, 70, 25, true, 1, 0, new Date(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000)));
//        new InsertAsyncTask(db.healthInfoCloneDao()).execute(new HealthInfoClone(loginId.getLoginId(), 170, 71, 25, true, 1, 0, new Date(System.currentTimeMillis() - 9 * 24 * 60 * 60 * 1000)));
//        new InsertAsyncTask(db.healthInfoCloneDao()).execute(new HealthInfoClone(loginId.getLoginId(), 170, 72, 25, true, 1, 0, new Date(System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000)));
//        new InsertAsyncTask(db.healthInfoCloneDao()).execute(new HealthInfoClone(loginId.getLoginId(), 170, 73, 25, true, 1, 0, new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)));
//        new InsertAsyncTask(db.healthInfoCloneDao()).execute(new HealthInfoClone(loginId.getLoginId(), 170, 74, 25, true, 1, 0, new Date(System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000)));
//        new InsertAsyncTask(db.healthInfoCloneDao()).execute(new HealthInfoClone(loginId.getLoginId(), 170, 75, 25, true, 1, 0, new Date(System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000)));
//        new InsertAsyncTask(db.healthInfoCloneDao()).execute(new HealthInfoClone(loginId.getLoginId(), 170, 76, 26, true, 1, 0, new Date(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000)));
//        new InsertAsyncTask(db.healthInfoCloneDao()).execute(new HealthInfoClone(loginId.getLoginId(), 170, 77, 26, true, 1, 0, new Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000)));
//        new InsertAsyncTask(db.healthInfoCloneDao()).execute(new HealthInfoClone(loginId.getLoginId(), 170, 78, 26, true, 1, 0, new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000)));
//        new InsertAsyncTask(db.healthInfoCloneDao()).execute(new HealthInfoClone(loginId.getLoginId(), 170, 79, 26, true, 1, 0, new Date(System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000)));
//
//
//    }

//     Score 테이블에 샘플 데이터 추가
//    private void insertSampleScores() {
//        // 샘플 데이터 생성
//        new InsertAsyncTask2(db.scoreDao()).execute(new Score( LocalDateTime.now().minusDays(6),loginId.getLoginId(), 300));
//        new InsertAsyncTask2(db.scoreDao()).execute(new Score( LocalDateTime.now().minusDays(5),loginId.getLoginId(), 400));
//        new InsertAsyncTask2(db.scoreDao()).execute(new Score( LocalDateTime.now().minusDays(4),loginId.getLoginId(), 100));
//        new InsertAsyncTask2(db.scoreDao()).execute(new Score( LocalDateTime.now().minusDays(3),loginId.getLoginId(), 500));
//        new InsertAsyncTask2(db.scoreDao()).execute(new Score( LocalDateTime.now().minusDays(2),loginId.getLoginId(), 200));
//        new InsertAsyncTask2(db.scoreDao()).execute(new Score( LocalDateTime.now().minusDays(1),loginId.getLoginId(), 300));
//    }

    // 데이터 세트 초기 설정 메서드
    private void setupDataSet(LineDataSet dataSet) {
        dataSet.setColor(Color.BLACK); // 데이터 색상 설정
        dataSet.setDrawValues(false); // 값 표시 비활성화
        dataSet.setDrawCircles(true); // 데이터 포인트 원형 표시 활성화
        dataSet.setLineWidth(2f); // 라인 두께 설정
        dataSet.setCircleRadius(3f); // 원형 반지름 설정

        if (dataSet.getEntryCount() == 1) {
            dataSet.setDrawCircles(true); // 원형 표시 활성화
            dataSet.setDrawFilled(false); // 채우기 비활성화
            dataSet.setDrawValues(true); // 값 표시 활성화
        } else {
            dataSet.setDrawCircles(true); // 원형 표시 활성화
            dataSet.setDrawFilled(false); // 채우기 비활성화
            dataSet.setDrawValues(false); // 값 표시 비활성화
        }

    }

    // 차트 데이터 설정 메서드
    private void setupChartData(LineChart chart, List<Entry> entries, String label) {
        LineDataSet dataSet = new LineDataSet(entries, label); // 데이터 세트 생성
        setupDataSet(dataSet); // 데이터 세트 초기 설정 적용

        if (entries.size() == 1) {
            dataSet.setDrawCircles(true);
            dataSet.setDrawFilled(false);
            dataSet.setDrawValues(true);
            dataSet.setLineWidth(0f); // 선 두께 0으로 설정하여 선을 보이지 않게 함
        }

        LineData lineData = new LineData(dataSet); // 라인 데이터 생성
        chart.setData(lineData); // 차트에 데이터 설정
        chart.invalidate(); // 차트 갱신

        if (entries.size() > 0) {
            float minX = entries.get(0).getX() - 1; // 첫 데이터 포인트보다 작게 설정
            chart.getXAxis().setAxisMinimum(minX);
        }
    }

    // X축 값을 날짜 형식으로 포맷하는 클래스
    class DateValueFormatter extends ValueFormatter {
        private final SimpleDateFormat mDateFormat;

        // 생성자에서 날짜 포맷 초기화
        public DateValueFormatter() {
            mDateFormat = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
        }

        // 값을 포맷하여 반환
        @Override
        public String getFormattedValue(float value) {
            return mDateFormat.format(new Date((long) value));
        }
    }

    // 차트 데이터 로딩 메서드
    private void loadChartData() {

        db.healthInfoCloneDao().getUserInfoList(loginFunction.getMyId()).observe(getViewLifecycleOwner(), new Observer<List<HealthInfoClone>>() {
            @Override
            public void onChanged(List<HealthInfoClone> healthInfoClones) {
                List<Entry> weightEntries = new ArrayList<>();
                for(HealthInfoClone healthInfoClone : healthInfoClones){
                    long timestamp = healthInfoClone.getDate().getTime();
                    if (!weightEntries.isEmpty() && weightEntries.get(weightEntries.size() - 1).getX() == timestamp) {
                        // 날짜가 같다면 마지막 항목의 값을 업데이트
                        weightEntries.get(weightEntries.size() - 1).setY(healthInfoClone.getWeight());
                    } else {
                        // 날짜가 다르다면 새로운 데이터를 리스트에 추가
                        weightEntries.add(new Entry(timestamp, healthInfoClone.getWeight()));
                    }
                }
                setupChartData(chartWeight, weightEntries, "Weight");
            }
        });

        db.scoreDao().getUserScoreList(loginFunction.getMyId()).observe(getViewLifecycleOwner(), new Observer<List<Score>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(List<Score> scores) {
                List<Entry> scoreEntries = new ArrayList<>();
                for(Score score : scores){
                    // LocalDate를 사용하여 년도, 월, 일까지만 타임스탬프로 변환
                    long timestamp = score.getDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    if (!scoreEntries.isEmpty() && scoreEntries.get(scoreEntries.size() - 1).getX() == timestamp) {
                        // 날짜가 같다면 마지막 항목의 값을 업데이트
                        scoreEntries.get(scoreEntries.size() - 1).setY(score.getScore());
                    } else {
                        scoreEntries.add(new Entry(timestamp, score.getScore())); // Entry 객체를 생성하여 리스트에 추가
                    }

                }
                setupChartData(chartScore, scoreEntries, "Score");
            }
        });

        //TDEE
        db.healthInfoCloneDao().getUserInfoList(loginFunction.getMyId()).observe(getViewLifecycleOwner(), new Observer<List<HealthInfoClone>>() {
            @Override
            public void onChanged(List<HealthInfoClone> healthInfoClones) {
                List<Entry> TDEEEntries = new ArrayList<>();
                for(HealthInfoClone healthInfoClone : healthInfoClones){
                    long timestamp = healthInfoClone.getDate().getTime();
                    float TDEE = (float) getTDEE(healthInfoClone);
                    if (!TDEEEntries.isEmpty() && TDEEEntries.get(TDEEEntries.size() - 1).getX() == timestamp) {
                        // 날짜가 같다면 마지막 항목의 값을 업데이트
                        TDEEEntries.get(TDEEEntries.size() - 1).setY(TDEE);
                    } else {
                        TDEEEntries.add(new Entry(timestamp, TDEE)); // TDEE 값을 추가
                    }
                }
                setupChartData(chartTDEE, TDEEEntries, "TDEE");// 차트 데이터 설정
            }
        });

    }


    // TDEE를 계산하는 메서드
    private double getTDEE(HealthInfoClone healthInfoClone){
        double TDEE;
        if(healthInfoClone.isGender() == true){
            TDEE = 88.362 + (13.397 * healthInfoClone.getWeight()) + (4.799 * healthInfoClone.getHeight()) - (5.677 * healthInfoClone.getAge());
        }else{
            TDEE = 447.593 + (9.247 * healthInfoClone.getWeight()) + (3.098 * healthInfoClone.getHeight()) - (4.330 * healthInfoClone.getAge());
        }

        switch (healthInfoClone.getActivity()){
            case 0:
                TDEE *= 1.2;
                break;
            case 1:
                TDEE *= 1.375;
                break;
            case 2:
                TDEE *= 1.55;
                break;
            case 3:
                TDEE *= 1.725;
                break;
            default:
                TDEE *= 1.9;
                break;
        }

        if (healthInfoClone.getPurpose() == 0) TDEE -= 500;
        else if(healthInfoClone.getPurpose()==2) TDEE += 500;

        return TDEE;

    }

    //테스트용 Insert함수들
    private static class InsertAsyncTask extends AsyncTask<HealthInfoClone, Void, Void> {
        // 비동기 처리에서 수행되는 내용이 userDao에 있는 Insert함수이
        private HealthInfoCloneDao healthInfoCloneDao; //User를 조작하기 위한 함수가 있는 UserDao객체 생성
        public InsertAsyncTask(HealthInfoCloneDao healthInfoCloneDao){
            this.healthInfoCloneDao = healthInfoCloneDao;
        }
        @Override
        protected Void doInBackground(HealthInfoClone... healthInfoClones) {
            healthInfoCloneDao.insert(healthInfoClones[0]);
            return null;
        }
    }

    private static class InsertAsyncTask1 extends AsyncTask<User, Void, Void>{ //User객체 넘겨받아서 Insert작업을 수행할 거다
        // 비동기 처리에서 수행되는 내용이 userDao에 있는 Insert함수이
        private UserDao userDao; //User를 조작하기 위한 함수가 있는 UserDao객체 생성
        public InsertAsyncTask1(UserDao userDao){
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) { //여기서 비동기 처리 수행
            userDao.insert(users[0]);
            return null;
        }
    }
    private static class InsertAsyncTask2 extends AsyncTask<Score, Void, Void>{
        private ScoreDao scoreDao;
        public InsertAsyncTask2(ScoreDao scoreDao){this.scoreDao = scoreDao;}

        @Override
        protected Void doInBackground(Score... scores) {
            scoreDao.insert(scores[0]);
            return null;
        }
    }


}
