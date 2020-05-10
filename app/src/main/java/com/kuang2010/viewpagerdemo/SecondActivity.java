package com.kuang2010.viewpagerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import jp.wasabeef.glide.transformations.BlurTransformation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kuang2010.bannerview.BannerAdapter;
import com.kuang2010.bannerview.Bannerview;
import com.kuang2010.bannerview.PointIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private List<Integer> datas = new ArrayList<>();
    private Bannerview mBanner_view;
    private ImageView mBgView;
    private ImageView mPreloadbgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mBanner_view = findViewById(R.id.banner_view);
        PointIndicatorView pv_main = findViewById(R.id.pv_main);
        mBgView = findViewById(R.id.bgView);
        mPreloadbgView = findViewById(R.id.preloadbgView);
        mBanner_view.addOnPageChangeListener(this);


        datas.clear();
        for (int i=R.mipmap.a;i<R.mipmap.a+6;i++){
            datas.add(i);
        }
        datas.add(R.mipmap.j);


        mBanner_view.initDatasAndItem(datas,new BannerAdapter.OnSetItemViewListener() {
            @Override
            public View instantiateItem(int position, Object data) {

                ImageView imageView = new ImageView(SecondActivity.this);

                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                imageView.setImageResource((Integer) data);

                return imageView;
            }
        });
        mBanner_view.setIndicator(pv_main);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mBanner_view.startAutoPlay(3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBanner_view.stopAutoPlay();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int next = position + 1;
        position = position % mBanner_view.getPageAdapter().getRealCount();
        next = next % mBanner_view.getPageAdapter().getRealCount();
        int resourceId = datas.get(position);
        int resourceIdNext = datas.get(next);
        Glide.with(this).load(resourceId)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 25)))
                .into(mBgView);
        Glide.with(this).load(resourceIdNext)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 25)))
                .into(mPreloadbgView);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
