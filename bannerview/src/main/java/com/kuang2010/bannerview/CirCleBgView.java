package com.kuang2010.bannerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;



/**
 * 支持镂空的白底圆弧背景view
 */

public class CirCleBgView extends View {

    private Paint mPaint = null;
    private int mWidth;
    private int mHeight;

    private int mPiercedX, mPiercedY;
    private int mPiercedRadius;
    private Bitmap mBitmap;

    public CirCleBgView(Context context) {
        this(context, null);
    }

    public CirCleBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

        if (mWidth == 0) {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            mWidth = dm.widthPixels;
            mHeight = dm.heightPixels;

            mPiercedX = mWidth / 2;
            mPiercedRadius = mWidth;
            mPiercedY =  - mWidth / 2;

        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xFFFFFFFF);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        RectF rectf = new RectF(0,0,mWidth,mHeight);
        canvas.saveLayerAlpha(rectf,0xFF);//创建个新的图层用来绘制以下图片。canvas默认的图层不能绘制透明图片的
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawCircle(mPiercedX, mPiercedY, mPiercedRadius, mPaint);
        canvas.drawRect(rectf,mPaint);
        mPaint.setXfermode(null);
        canvas.restore();

    }



    public static int dipTopx(Context tContext, float dipValue) {
        return (int) (dipValue * tContext.getResources().getDisplayMetrics().density + 0.5f);
    }


    //test
    //    @Override
    protected void onDraw(Canvas canvas, int f) {
        super.onDraw(canvas);

        //清屏操作
        canvas.drawColor(Color.GRAY);
        //画布向右下移动50像素
        canvas.translate(50, 50);
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(5, 5f, 5, mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 40, mPaint);
        int sc = canvas.saveLayerAlpha(0, 0, 200, 200, 0xFF, Canvas.ALL_SAVE_FLAG);
//        canvas.saveLayer(0, 0, 200, 200,mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(100, 100, 60, mPaint);
//        canvas.restore();  // 还原画布
        canvas.restoreToCount(sc);  // 还原画布
    }

    //test
//    @Override
    protected void onDraw(Canvas canvas, int f, int f2) {
        super.onDraw(canvas);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tp2);

        canvas.drawBitmap(mBitmap, 100, 100, mPaint);

        RectF innerRect = new RectF(250, 250, 550, 550);
        RectF outterRect = new RectF(100, 100, 100 + mBitmap.getWidth(), 100 + mBitmap.getHeight());

        canvas.saveLayerAlpha(outterRect, 0xC8, Canvas.ALL_SAVE_FLAG);

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));

//        canvas.drawRect(innerRect, mPaint);//下层
        canvas.drawCircle(100 + mBitmap.getWidth()/2,100 + mBitmap.getHeight()/2,mBitmap.getWidth()/2,mPaint);

        PorterDuffXfermode mode = new PorterDuffXfermode(
                PorterDuff.Mode.DST_IN);//取上层绘制非交集部分。
        mPaint.setXfermode(mode); // 设置混合模式

        canvas.drawRect(outterRect, mPaint);//上层


        mPaint.setXfermode(null); // 还原混合模式
        canvas.restore(); // 还原画布

    }
}
