package com.li.videoapplication.activity;

import java.util.ArrayList;

import com.li.videoapplication.R;
import com.li.videoapplication.utils.SharePreferenceUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class IntroduceActivity extends Activity{

	public ViewPager intro_vPager;
	public ImageView pg00,pg11,pg22,pg33;
	public int curr_index=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introduce);
		intro_vPager=(ViewPager)findViewById(R.id.introduce_viewpager);
		pg00=(ImageView)findViewById(R.id.introduce_page_now);
		pg11=(ImageView)findViewById(R.id.introduce_page1);
		pg22=(ImageView)findViewById(R.id.introduce_page2);
		pg33=(ImageView)findViewById(R.id.introduce_page3);
		
		intro_vPager.setOnPageChangeListener(new Mypagechane());
		
		LayoutInflater layoutInflater=LayoutInflater.from(this);
		View view1=layoutInflater.inflate(R.layout.introduce_01, null);
		View view2=layoutInflater.inflate(R.layout.introduce_02, null);
		View view3=layoutInflater.inflate(R.layout.introduce_03, null);
//		View view4=layoutInflater.inflate(R.layout.introduce_04, null);
		
		//ÿ��ҳ���view���
		final ArrayList<View> views=new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
//		views.add(view4);
		
		PagerAdapter pagerAdapter=new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0==arg1;
			}////Object��view�Ƿ�Ϊͬһ��view
			
			@Override
			public int getCount() {
				
				return views.size();
			}

			
			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}////��ǰview����Ҫ��ʱ����մ���

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}

			@Override
			public void finishUpdate(View arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Parcelable saveState() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void startUpdate(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
			
		};
		intro_vPager.setAdapter(pagerAdapter);
	}
	public class Mypagechane implements OnPageChangeListener
	{


		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}


		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
//			switch(arg0)
//			{
//			case 0:
//				pg00.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
//				pg11.setImageDrawable(getResources().getDrawable(R.drawable.page));
//				break;
//
//			case 1:
//				pg11.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
//				pg00.setImageDrawable(getResources().getDrawable(R.drawable.page));
//				pg22.setImageDrawable(getResources().getDrawable(R.drawable.page));
//				break;
//			case 2:
//				pg22.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
//				pg33.setImageDrawable(getResources().getDrawable(R.drawable.page));
//				pg11.setImageDrawable(getResources().getDrawable(R.drawable.page));
//				break;
//			case 3:
//				pg33.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
//				pg22.setImageDrawable(getResources().getDrawable(R.drawable.page));
//			}
			curr_index=arg0;
		}
		
	}
	public void startButton(View v)
	{
		IntroduceActivity.this.finish();
        SharePreferenceUtil.setPreference(this,"isFirst","1");
	}
}
