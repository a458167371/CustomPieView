package com.chao.pieview.pie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by qichao on 2017/11/17.
 */

public class PieView extends View {

    private Paint mPaint = new Paint();
    // 颜色表
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    // 饼状图初始绘制角度w
    private float mStartAngle = 0;
    // 数据
    private ArrayList<PieData> mData;
    // 宽高
    private int mWidth, mHeight;

    // 文字色块部分
    private PointF mStartPoint = new PointF(20, 20);
    private PointF mCurrentPoint = new PointF(mStartPoint.x, mStartPoint.y);
    private float mColorRectSideLength = 20;
    private float mTextInterval = 10;
    private float mRowMaxLength;


    public PieView(Context context) {
        this(context,null);
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    public void setData(ArrayList<PieData> mData) {
        this.mData = mData;
        initData(mData);
        invalidate();
    }

    // 设置起始角度
    public void setStartAngle(int mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();   // 刷新
    }

    private void initData(ArrayList<PieData> mData) {
        if(null == mData || mData.size() == 0)
            return;

        float sumValue = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pieData = mData.get(i);
            sumValue += pieData.getValue();   //数值和

            int j= i% mColors.length;
            pieData.setColor(mColors[j]);  //设置颜色
        }

        float sumAngle = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pieData = mData.get(i);
            float percent = pieData.getValue() / sumValue;  //百分比
            float angle = percent * 360; //对应角度

            pieData.setPercentage(percent);
            pieData.setAngle(angle);
            sumAngle += angle;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null == mData)
            return;
        float currentStartAngle = mStartAngle;
        canvas.translate(mWidth / 2,mHeight / 2);
        float r = (float)(Math.min(mWidth,mHeight) / 2 * 0.8);

        RectF rect = new RectF(-r,-r,r,r);

        for (int i = 0; i < mData.size(); i++) {
            PieData pieData = mData.get(i);
            mPaint.setColor(pieData.getColor());
            canvas.drawArc(rect,currentStartAngle,pieData.getAngle(),true,mPaint);

            canvas.save();
            canvas.translate(-mWidth / 2, -mHeight / 2);
            RectF colorRect = new RectF(mCurrentPoint.x, mCurrentPoint.y, mCurrentPoint.x + mColorRectSideLength, mCurrentPoint.y + mColorRectSideLength);


            canvas.restore();
        }
    }
}
