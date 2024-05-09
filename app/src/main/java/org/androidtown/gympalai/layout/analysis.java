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
        //차트 기본 속성 설정
        LineChart[] charts = {chartWeight, chartScore, chartTDEE};
        for (LineChart chart : charts) {
            chart.getDescription().setEnabled(false); //차트 설명 비활성화
            chart.setDrawGridBackground(false); //그리드 배경 그리기 비활성화

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x축 위치 아래로 설정
            xAxis.setDrawGridLines(false); //x축 그리드 라인 비활성화

            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setDrawGridLines(false);

            chart.getAxisRight().setEnabled(false);
        }
    }

    private void loadChartData() {
        //차트에 표시할 데이터 로드
        List<Entry> entries = new ArrayList<>();

        /*db에 연결해서 값 가져오기
        entries.add(new Entry(0f, 60f)); // Sample data
        entries.add(new Entry(1f, 62f));
        entries.add(new Entry(2f, 61f));
         */

        LineDataSet dataSet = new LineDataSet(entries, "Label"); //데이터셋 생성

        //차트 형태 설정
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(true);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);

        LineData lineData = new LineData(dataSet);

        //차트에 데이터 설정 및 차트 갱신
        chartWeight.setData(lineData);
        chartWeight.invalidate(); //갱신
        chartScore.setData(lineData);
        chartScore.invalidate(); //갱신
        chartTDEE.setData(lineData);
        chartTDEE.invalidate(); //갱신
    }
}