package com.woting.activity.home.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.woting.R;
import com.woting.activity.home.player.main.fragment.PlayerFragment;
import com.woting.activity.home.program.main.ProgramFragment;
import com.woting.activity.home.search.activity.SearchLikeAcitvity;
import com.woting.common.adapter.MyFragmentPagerAdapter;
import com.woting.util.ToastUtils;

import java.util.ArrayList;

/**
 * 内容主页
 * @author 辛龙 
 * 2016年2月2日
 */
public class HomeActivity extends FragmentActivity {
	private static TextView view1;
	private static TextView view2;
	private static HomeActivity context;
	private static ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_wt_home);
		context = this;
		InitTextView();
		InitViewPager();
	}

	private void InitTextView() {
		view1 = (TextView) findViewById(R.id.tv_guid1);
		view2 = (TextView) findViewById(R.id.tv_guid2);
		//		LinearLayout lin_news = (LinearLayout) findViewById(R.id.lin_news);

		LinearLayout lin_find = (LinearLayout) findViewById(R.id.lin_find);
		lin_find.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转到搜索界面  原来的代码 要加在这里
				Intent intent = new Intent(context, SearchLikeAcitvity.class);
				startActivity(intent);	
			}
		});
		view1.setOnClickListener(new txListener(0));
		view2.setOnClickListener(new txListener(1));
	}

	public class txListener implements OnClickListener {
		private int index = 0;
		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
			if (index == 0) {
				view1.setTextColor(context.getResources().getColor(R.color.dinglan_orange));
				view2.setTextColor(context.getResources().getColor(R.color.white));
				view1.setBackgroundDrawable(context.getResources().getDrawable(	R.drawable.color_wt_circle_home_white));
				view2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.color_wt_circle_orange));
			} else if (index == 1) {
				view1.setTextColor(context.getResources().getColor(R.color.white));
				view2.setTextColor(context.getResources().getColor(R.color.dinglan_orange));
				view1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.color_wt_circle_orange));
				view2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.color_wt_circle_home_white));
			}
		}
	}

	/*
	 * 初始化ViewPager
	 */
	public void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.viewpager);
		mPager.setOffscreenPageLimit(1);
		ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
		PlayerFragment playfragment = new PlayerFragment();
		ProgramFragment newsfragment = new ProgramFragment();
		fragmentList.add(playfragment);
		fragmentList.add(newsfragment);
		// mPager.setAdapter(new MyFragmentChildPagerAdapter(getChildFragmentManager(), fragmentList));
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());	// 页面变化时的监听器
		mPager.setCurrentItem(0);	// 设置当前显示标签页为第一页mPager
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageSelected(int arg0) {
			if (arg0 == 0) {
				view1.setTextColor(context.getResources().getColor(R.color.dinglan_orange));
				view2.setTextColor(context.getResources().getColor(R.color.white));
				view1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.color_wt_circle_home_white));
				view2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.color_wt_circle_orange));
			} else if (arg0 == 1) {
				view1.setTextColor(context.getResources().getColor(R.color.white));
				view2.setTextColor(context.getResources().getColor(R.color.dinglan_orange));
				view1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.color_wt_circle_orange));
				view2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.color_wt_circle_home_white));
			}
		}
	}

	public static void UpdateViewPager() {
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页mPager
		view1.setTextColor(context.getResources().getColor(R.color.dinglan_orange));
		view2.setTextColor(context.getResources().getColor(R.color.white));
		view1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.color_wt_circle_home_white));
		view2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.color_wt_circle_orange));
	}



	/*
	 * 手机实体返回按键的处理  与onbackpress同理
	 */
	long waitTime = 2000;
	long touchTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= waitTime) {
				ToastUtils.show_allways(HomeActivity.this, "再按一次退出");
				touchTime = currentTime;
			} else {
				MobclickAgent.onKillProcess(this);
				finish();
				android.os.Process.killProcess(android.os.Process.myPid()); 
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
