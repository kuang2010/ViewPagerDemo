package com.kuang2010.bannerview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * author: kuangzeyu2019
 */
public class Bannerview extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnTouchListener {


    private BannerAdapter mBannerAdapter;
    private ViewPager mViewPager;
    private IIndicator mIIndicator;
    private static final int WHAT_SELECT_NEXT_PAGE = 1;
    private long delayMillis;//自动播放停留时间
    private boolean mAllowAutoPlay;//是否允许自动播放
    private boolean mIsAutoPlaying;//是否正在自动播放
    private int mScaledPagingTouchSlop;
    private OnPageItemClickListener mOnPageItemClickListener;

    public void setOnPageItemClickListener(OnPageItemClickListener onPageItemClickListener) {
        mOnPageItemClickListener = onPageItemClickListener;
    }

    public Bannerview(Context context) {
        super(context);
        initView(context);
    }

    public Bannerview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_bnnnerview, this);
        mViewPager = findViewById(R.id.viewpage);
        mBannerAdapter = new BannerAdapter();
        mViewPager.setAdapter(mBannerAdapter);
        mViewPager.addOnPageChangeListener(this);
        setScrollervelocity(400);
        mViewPager.setOnTouchListener(this);
        mScaledPagingTouchSlop = ViewConfiguration.get(getContext()).getScaledPagingTouchSlop();
        int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }


    /**
     * 设置数据并初始化item
     * @param datas 页面数据
     * @param onSetItemViewListener 回调获取itemview
     */
    public<E> void initDatasAndItem(List<E> datas, BannerAdapter.OnSetItemViewListener onSetItemViewListener){
        mBannerAdapter.initDatasAndItemViewListener(datas,onSetItemViewListener);
        mViewPager.setCurrentItem(mBannerAdapter.getRealCount()*100);
        if (mIIndicator!=null){
            mIIndicator.setTotalNum(mBannerAdapter.getRealCount());
            mIIndicator.setSelectIndex(0);
        }
    }

    /**
     * 设置点点点
     * @param pointIndicatorView
     */
    public void setIndicator(IIndicator pointIndicatorView){
        mIIndicator = pointIndicatorView;
        if (mBannerAdapter!=null){
            int realCount = mBannerAdapter.getRealCount();
            mIIndicator.setTotalNum(realCount);
            mIIndicator.setSelectIndex(0);
        }
    }

    /**
     * 刷新数据
     * @param datas
     */
    public<E> void setDatas(List<E> datas){
        int currentItem = mViewPager.getCurrentItem();
        int currentSelect = currentItem % mBannerAdapter.getRealCount();
        mBannerAdapter.setData(datas);
        if (mIIndicator!=null){
            int realCount = mBannerAdapter.getRealCount();
            mIIndicator.setTotalNum(realCount);
            //间接调用setSelectIndex改变选中的位置
            mViewPager.setCurrentItem(mBannerAdapter.getRealCount()*100+currentSelect,false);
        }
    }


    public BannerAdapter getPageAdapter(){
        return mBannerAdapter;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (mViewPager == null || mBannerAdapter == null) {
                return;
            }
            switch (msg.what){
                case WHAT_SELECT_NEXT_PAGE:
                    int currentItem = mViewPager.getCurrentItem();
                    int next = currentItem + 1;
                    if (next<mBannerAdapter.getCount()){
                        mViewPager.setCurrentItem(next,true);
                        mHandler.sendEmptyMessageDelayed(WHAT_SELECT_NEXT_PAGE, delayMillis);
                        mIsAutoPlaying = true;
                    }
                    break;
            }
        }
    };

    /**
     * 启动自动播放
     * @param delayMillis
     */
    public void startAutoPlay(long delayMillis){
        this.delayMillis = delayMillis;
        mAllowAutoPlay = true;
        startAutoPlay();
    }


    /**
     * 停止自动播放
     */
    public void stopAutoPlay(){
        if (mIsAutoPlaying){
            mHandler.removeMessages(WHAT_SELECT_NEXT_PAGE);
            mIsAutoPlaying =false;
        }
    }
    /**
     * 针对stopAutoPlay后又开始播放
     */
    public void startAutoPlay() {
        if (mAllowAutoPlay&&!mIsAutoPlaying&&delayMillis>0){
            mHandler.removeMessages(WHAT_SELECT_NEXT_PAGE);
            mHandler.sendEmptyMessageDelayed(WHAT_SELECT_NEXT_PAGE, delayMillis);
            mIsAutoPlaying = true;
        }
    }

    /**
     * 通过改变viewpage里的mScroller对象，修改
     * viewpage的滚动速度
     * mScroller.startScroll(sx, sy, dx, dy, duration);
     * 重写Scroller的startScroll方法
     * @param duration 切换过渡时间
     */
    public void setScrollervelocity(int duration){
        try {
            Class<? extends ViewPager> type = mViewPager.getClass();
            Field mScroller = type.getDeclaredField("mScroller");
            mScroller.setAccessible(true);//当该字段时private修饰时,isAccessible()得到的值是false，必须要改成true才可以访问
//            BannerScroller bannerScroller = new BannerScroller(getContext());
            BannerScroller bannerScroller = new BannerScroller(getContext(),new LinearInterpolator(){
                @Override
                public float getInterpolation(float input) {
                    return input;
                }
            });
            bannerScroller.setDuration(duration);
            mScroller.set(mViewPager, bannerScroller);//修改mScroller的值
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    /**
     * 设置banner中各种间距大小
     * @param leftMarginToScreen 最中间item到屏幕的间距
     * @param itemMargin item之间的间距
     * @param aspectRatio leftMargin调整前的item宽高比
     */
    public void setMarginAndAspectRatio(int leftMarginToScreen, int itemMargin, float aspectRatio) {
        mViewPager.setPageMargin(itemMargin);

        if (leftMarginToScreen > 0) {
            setClipChildren(false);
            mViewPager.setClipChildren(false);
        }

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int itemWidth = screenWidth - 2 * leftMarginToScreen;
        int itemHeight = (int) (itemWidth/aspectRatio);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)mViewPager.getLayoutParams();
        lp.leftMargin = leftMarginToScreen;
        lp.rightMargin = leftMarginToScreen;
        lp.height = itemHeight;
        mViewPager.setLayoutParams(lp);

        ViewGroup.LayoutParams lpp = getLayoutParams();
        lpp.height = itemHeight;
        setLayoutParams(lpp);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mIIndicator!=null){
            position %=  mBannerAdapter.getRealCount();
            mIIndicator.setSelectIndex(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    float mDownX = 0;
    float mDownY = 0;
    int moveFlag =0;
    long mDownMillis = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v==mViewPager){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    mDownX = event.getX();
                    mDownY = event.getY();
                    mDownMillis = System.currentTimeMillis();
                    moveFlag = 0;
                    break;
                case MotionEvent.ACTION_MOVE://单击快的话move有可能不执行
                    float x = event.getX();
                    float y = event.getY();
                    if (Math.abs(x-mDownX)<mScaledPagingTouchSlop && Math.abs(y-mDownY)<mScaledPagingTouchSlop){
                        moveFlag = 0;
                    }else {
                        moveFlag = 1;
                    }
                    break;
                case MotionEvent.ACTION_UP://处理最终边界结果
                    Log.d("tagtag","ACTION_UP");
                    startAutoPlay();
                    long dMills = Math.abs(System.currentTimeMillis() - mDownMillis);
                    if (moveFlag == 0 && dMills <=800){
                        //点击事件
                        int currentItem = mViewPager.getCurrentItem();
                        int realPosition = currentItem % mBannerAdapter.getRealCount();
                        if (mOnPageItemClickListener!=null){
                            mOnPageItemClickListener.onItemClick(realPosition);
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.d("tagtag","ACTION_CANCEL");
                    startAutoPlay();
                    break;
            }
        }

        return false;
    }


    public void addOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener){
        mViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    public interface OnPageItemClickListener{
        void onItemClick(int position);
    }
}
