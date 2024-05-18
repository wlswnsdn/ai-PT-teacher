package org.androidtown.gympalai.layout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import org.androidtown.gympalai.dao.HealthInfoDao;
import org.androidtown.gympalai.entity.HealthInfo;

import java.util.ArrayList;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.Locale;

public class analysis extends Fragment {

    LineChart chartWeight;
    LineChart chartScore;
    LineChart chartTDEE;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);

        chartWeight = view.findViewById(R.id.chart_weight);
        chartScore = view.findViewById(R.id.chart_score);
        chartTDEE = view.findViewById(R.id.chart_TDEE);

        setupCharts(); //차트 설정 초기화
        loadChartData(); //차트 데이터 로딩

        return view;

    }

    private void setupCharts() {
        LineChart[] charts = {chartWeight, chartScore, chartTDEE};
        for (LineChart chart : charts) {

            chart.getDescription().setEnabled(false);
            chart.setDrawGridBackground(false);
            chart.getLegend().setEnabled(false);
            chart.getAxisRight().setEnabled(false);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new DateValueFormatter());

            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setDrawGridLines(false);
        }
    }

    private void setupDataSet(LineDataSet dataSet) {
        dataSet.setColor(Color.BLACK);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(true);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
    }

    private void setupChartData(LineChart chart, List<Entry> entries, String label) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        setupDataSet(dataSet);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // 갱신
    }

    //X값을 날짜 형식으로 포맷
    class DateValueFormatter extends ValueFormatter {
        private final SimpleDateFormat mDateFormat;

        public DateValueFormatter() {
            mDateFormat = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
        }

        @Override
        public String getFormattedValue(float value) {
            return mDateFormat.format(new Date((long) value));
        }
    }


    private void loadChartData() {

        // 여기 아래 weight 받아와야합니다
        HealthInfoDao.getAll().observe(getViewLifecycleOwner(), new Observer<List<HealthInfo>>() {
            @Override
            public void onChanged(List<HealthInfo> healthInfos) {
                List<Entry> weightEntries = new ArrayList<>();

                for (HealthInfo healthInfo : healthInfos) {

                    long timestamp =0; //dateToTimestamp( ); 여기에 날짜 데이터 넣어야함.
                    weightEntries.add(new Entry(timestamp, healthInfo.getWeight()));

                }
                setupChartData(chartWeight, weightEntries, "Weight");


            }
        });
        // 여기 아래 Score 받아와야합니다.
        HealthInfoDao.getAll().observe(getViewLifecycleOwner(), new Observer<List<HealthInfo>>() {
            @Override
            public void onChanged(List<HealthInfo> healthInfos) {
                List<Entry> scoreEntries = new ArrayList<>();

                for (HealthInfo healthInfo : healthInfos) {

                    long timestamp =dateToTimestamp(); //dateToTimestamp( ); 여기에 날짜 데이터 넣어야함.
                    scoreEntries.add(new Entry(timestamp, healthInfo.getscore()));

                }
                setupChartData(chartScore, scoreEntries, "Score");


            }
        });
        // 여기 아래 TDEE 받아와야합니다.
        HealthInfoDao.getAll().observe(getViewLifecycleOwner(), new Observer<List<HealthInfo>>() {
            @Override
            public void onChanged(List<HealthInfo> healthInfos) {
                List<Entry> TDEEEntries = new ArrayList<>();

                for (HealthInfo healthInfo : healthInfos) {

                    long timestamp =dateToTimestamp(); //dateToTimestamp( ); 여기에 날짜 데이터 넣어야함.
                    TDEEEntries.add(new Entry(timestamp, healthInfo.getTDEE()));

                }
                setupChartData(chartTDEE, weightEntries, "TDEE");


            }
        });


    }


            //날짜 스탬프 변환 함수
            public long dateToTimestamp(String dateStr) {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); // 날짜 포맷 지정
                try {
                    Date date = formatter.parse(dateStr);
                    return date.getTime(); // 타임스탬프 반환
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
}