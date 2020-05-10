package com.kuang2010.viewpagerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kuang2010.bannerview.BannerAdapter;
import com.kuang2010.bannerview.Bannerview;
import com.kuang2010.bannerview.PointIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Integer> datas = new ArrayList<>();
    private Bannerview mBanner_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBanner_view = findViewById(R.id.banner_view);
        PointIndicatorView pv_main = findViewById(R.id.pv_main);

        datas.clear();
        for (int i=R.mipmap.a;i<R.mipmap.a+5;i++){
            datas.add(i);
        }

        mBanner_view.initDatasAndItem(datas,new BannerAdapter.OnSetItemViewListener() {
            @Override
            public View instantiateItem(int position, Object data) {

                ImageView imageView = new ImageView(MainActivity.this);

                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                imageView.setImageResource((Integer) data);

                return imageView;
            }
        });

        mBanner_view.setIndicator(pv_main);


        mBanner_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        datas.add(R.mipmap.f);
                        datas.add(R.mipmap.j);
                        mBanner_view.setDatas(datas);
                    }
                });
            }
        },5000);



        mBanner_view.setOnPageItemClickListener(new Bannerview.OnPageItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });


        int screenWidth = getScreenWidth(this);
        mBanner_view.setMarginAndAspectRatio((int)(60 / 1080f * screenWidth), (int)(24 / 1080f * screenWidth),960/492f);
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

    public int getScreenWidth(Context tContext) {
        DisplayMetrics dm = tContext.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
}
