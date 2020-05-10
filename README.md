# ViewPagerDemo
使用viewpager实现轮播图效果

![Image text](1.gif)


![Image text](2.gif)

How to
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.kuang2010:ViewPagerDemo:1.0.0'
	}
  
  
  
  
 Step 3. layout xml file
 
 
    <com.kuang2010.bannerview.Bannerview
        android:id="@+id/banner_view"
        android:layout_width="match_parent"
        android:layout_height="200dp"/>

    <com.kuang2010.bannerview.PointIndicatorView
        android:id="@+id/pv_main"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        app:pointRadius = "5dp"
        app:pointNormalColor = "#999999"
        app:pointSelectColor = "#f00000"
        app:pointSpace = "5dp"
        android:layout_marginTop="15dp"/>
        
        
  Step 4. Activity file
  
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
	
	int screenWidth = getScreenWidth(this);
        mBanner_view.setMarginAndAspectRatio((int)(60 / 1080f * screenWidth), (int)(24 / 1080f * screenWidth),960/492f);
  
