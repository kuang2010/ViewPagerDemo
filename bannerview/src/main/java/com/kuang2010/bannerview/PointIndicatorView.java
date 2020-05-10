package com.kuang2010.bannerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * author: kuangzeyu2019
 * desc: 轮播点点点
 */
public class PointIndicatorView extends View implements IIndicator{

	private float mPointRadius;
	private float mPointSpace;
	private int mColorNormal;
	private int mColorSelect;
	
	private int mTotalNum = 0;
	private int mSelectIndex = 0;
	
	private int mWidth;
	private int mHeight;
	private Paint mPaint;
	
	public PointIndicatorView(Context context, AttributeSet attrs,
                              int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	public PointIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public PointIndicatorView(Context context) {
		super(context);
		init(context, null);
	}

	private void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PointIndicator);
			mPointRadius = a.getDimensionPixelSize(R.styleable.PointIndicator_pointRadius, 5);
			mPointSpace = a.getDimensionPixelSize(R.styleable.PointIndicator_pointSpace, 9);
			mColorNormal = a.getColor(R.styleable.PointIndicator_pointNormalColor, 0);
			mColorSelect = a.getColor(R.styleable.PointIndicator_pointSelectColor, 0);
			a.recycle();
		}
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Style.FILL);
		
	}

	@Override
	public void setTotalNum(int total) {
		if (mTotalNum != total) {
			mTotalNum = total;
			mTotalNum = mTotalNum >= 0 ? mTotalNum : 0;
			requestLayout();
		}
	}

	@Override
	public void setSelectIndex(int index) {
		mSelectIndex = index;
		mSelectIndex = mSelectIndex >= 0 ? mSelectIndex : 0;
		postInvalidate();
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (widthMode == MeasureSpec.EXACTLY) {
			mWidth = getWidth();
		} else {
			mWidth = (int)(mTotalNum * mPointRadius * 2 + (mTotalNum - 1) * mPointSpace);
			mWidth = mWidth >= 0 ? mWidth : 0;
		}
		if (heightMode == MeasureSpec.EXACTLY) {
			mHeight = getHeight();
		} else {
			mHeight = (int)(mPointRadius * 2);
		}
		
		setMeasuredDimension(mWidth, mHeight);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float startX = 0;
		float startY = 0;//(mHeight - mPointRadius * 2) / 2;
		for (int i = 0; i < mTotalNum; i++) {
			float cx = startX + (mPointRadius * 2 + mPointSpace) * i + mPointRadius;
			float cy = startY + mPointRadius;
			mPaint.setColor(mSelectIndex == i ? mColorSelect : mColorNormal);
			canvas.drawCircle(cx, cy, mPointRadius, mPaint);
		}
	}
}
