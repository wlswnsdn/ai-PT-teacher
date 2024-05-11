package org.androidtown.gympalai.layout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.androidtown.gympalai.R;

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


    private void loadChartData() {
        // Weight Chart Data
        List<Entry> weightEntries = new ArrayList<>();
        weightEntries.add(new Entry(dateToTimestamp("01/01/2021"), 60f));
        weightEntries.add(new Entry(dateToTimestamp("05/01/2021"), 55f));
        weightEntries.add(new Entry(dateToTimestamp("11/03/2021"), 80f));
        LineDataSet weightDataSet = new LineDataSet(weightEntries, "Weight");
        setupDataSet(weightDataSet);
        LineData weightLineData = new LineData(weightDataSet);
        chartWeight.setData(weightLineData);
        chartWeight.invalidate(); // 갱신

        // Score Chart Data
        List<Entry> scoreEntries = new ArrayList<>();
        scoreEntries.add(new Entry(dateToTimestamp("01/01/2021"), 70f));
        scoreEntries.add(new Entry(dateToTimestamp("11/12/2021"), 68f));
        LineDataSet scoreDataSet = new LineDataSet(scoreEntries, "Score");
        setupDataSet(scoreDataSet);
        LineData scoreLineData = new LineData(scoreDataSet);
        chartScore.setData(scoreLineData);
        chartScore.invalidate(); // 갱신

        // TDEE Chart Data
        List<Entry> tdeeEntries = new ArrayList<>();
        tdeeEntries.add(new Entry(dateToTimestamp("01/01/2021"), 2500f));
        tdeeEntries.add(new Entry(dateToTimestamp("02/01/2021"), 2600f));
        tdeeEntries.add(new Entry(dateToTimestamp("03/01/2021"), 2550f));
        tdeeEntries.add(new Entry(dateToTimestamp("04/01/2021"), 2450f));
        tdeeEntries.add(new Entry(dateToTimestamp("05/01/2021"), 2475f));
        LineDataSet tdeeDataSet = new LineDataSet(tdeeEntries, "TDEE");
        setupDataSet(tdeeDataSet);
        LineData tdeeLineData = new LineData(tdeeDataSet);
        chartTDEE.setData(tdeeLineData);
        chartTDEE.invalidate(); // 갱신
    }

    private void setupDataSet(LineDataSet dataSet) {
        dataSet.setColor(Color.BLACK);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(true);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
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

}