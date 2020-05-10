package com.kuang2010.bannerview;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class BannerAdapter<E> extends PagerAdapter {

    private List<E> mDatas = new ArrayList<>();
    private OnSetItemViewListener mOnInstantiateItemListener;

    public void initDatasAndItemViewListener(List<E> datas, OnSetItemViewListener onSetItemViewListener){
        mOnInstantiateItemListener = onSetItemViewListener;
        setData(datas);
    }

    public void setData(List<E> datas){
        mDatas.clear();
        notifyDataSetChanged();
        if (datas!=null&&datas.size()>0){
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }

    }

    public int getRealCount(){
        return mDatas==null?0:mDatas.size();
    }

    @Override
    public int getCount() {
        //      用来确定多少个数据显示
        if (mOnInstantiateItemListener==null)return 0;
        return mDatas.size()>0?Integer.MAX_VALUE:0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        //	    1. 缓存复用
        //	    2. 判断标记object与view是否一致  一致显示view
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //      1.初始化显示View
        //      2.默认缓存3个View  当前 前一个 后一个
        if (mOnInstantiateItemListener==null) return null;
        position = position % mDatas.size();
        View view = mOnInstantiateItemListener.instantiateItem(position,mDatas.get(position));//// 初始化view
        container.addView(view);// 添加View到ViewPager中
        return view;//  把当前的view做为标记返回
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        //  不是缓存位置的view要销毁 不要使用默认的super.destroyItem(container, position, object);UnsupportedOperationException
        container.removeView((View) object);

    }

    public interface OnSetItemViewListener<E>{
        View instantiateItem(int position, E data);
    }
}
