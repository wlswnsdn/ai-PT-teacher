package org.androidtown.gympalai.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircularProgressView extends View {
    private Paint paint;
    private Paint textPaint;
    private int score = 0; // 점수 변수

    public CircularProgressView(Context context) {
        super(context);
        init();
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 20;
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        float angle = 360 * score / 1000; // 점수를 기반으로 각도 계산
        RectF rectF = new RectF(20, 20, width - 20, height - 20);
        paint.setColor(Color.rgb(212, 177, 255));
        canvas.drawArc(rectF, -90, angle, false, paint);

        // 중앙에 텍스트로 점수 표시
        canvas.drawText(score + "/1000", width / 2, height / 2 + textPaint.getTextSize() / 4, textPaint);
    }

    public void setScore(int score) { // 점수 설정 메서드
        this.score = score;
        invalidate();
    }
}
