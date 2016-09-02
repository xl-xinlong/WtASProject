package com.woting.activity.person.playhistory.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.woting.R;
import com.woting.activity.home.player.main.dao.SearchPlayerHistoryDao;
import com.woting.activity.person.playhistory.fragment.RadioFragment;
import com.woting.activity.person.playhistory.fragment.SoundFragment;
import com.woting.activity.person.playhistory.fragment.TTSFragment;
import com.woting.activity.person.playhistory.fragment.TotalFragment;
import com.woting.manager.MyActivityManager;
import com.woting.util.PhoneMessage;
import com.woting.util.ToastUtils;
import com.woting.widgetui.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 播放历史
 * @author woting11
 */
public class PlayHistoryActivity extends FragmentActivity implements OnClickListener {
	private LinearLayout lin_back; 			// 返回
	private PlayHistoryActivity context;
	private MyViewPager viewPager;
	private TextView allText, soundText, radioText, ttsText, clearEmpty, openEdit;
	//private TextView sequText;
	private ImageView image; 				// Cursor
	private ImageView imgQuanxuan;
	private Dialog delDialog;
	private Dialog confirmdialog;
	private List<Fragment> fragmentList;
	private int currIndex; 					// 当前页卡编号
	private int bmpW; 						// 横线图片宽度
	private int offset; 					// 图片移动的偏移量
	private int screenw;
	private int dialogflag = 0; 			// 编辑全选状态的变量 0为未选中，1为选中
	private SearchPlayerHistoryDao dbdao;	// 播放历史数据库
	private TotalFragment allFragment; 		// 全部
//	private SequFragment sequFragment; 		// 专辑
	private SoundFragment soundFragment; 	// 声音
	private RadioFragment radioFragment; 	// 电台
	private TTSFragment ttsFragment; 		// TTS
	public static boolean isEdit = true; 	// 是否为编辑状态
	public static final String UPDATA_ACTION_ALL = "com.woting.playhistory.all";
	public static final String UPDATA_ACTION_CHECK = "com.woting.playhistory.check";
	private boolean isDelete=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playhistory);
		IntentFilter intentFileter = new IntentFilter();	//注册广播
		intentFileter.addAction(PlayHistoryActivity.UPDATA_ACTION_ALL);
		intentFileter.addAction(PlayHistoryActivity.UPDATA_ACTION_CHECK);
		registerReceiver(myBroadcast, intentFileter);
		context = this;
		dbdao = new SearchPlayerHistoryDao(context); 		// 初始化数据库
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); 		// 透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); 	// 透明导航栏
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(context);						//将 Activity 添加到集合中
		InitImage();
		setview(); // 设置界面
	}
	
	/*
	 * 初始化视图
	 */
	private void setview() {
		lin_back = (LinearLayout) findViewById(R.id.head_left_btn);	// 左上返回键
		lin_back.setOnClickListener(this);
		clearEmpty = (TextView) findViewById(R.id.clear_empty); 	// 清空
		clearEmpty.setOnClickListener(this);
		openEdit = (TextView) findViewById(R.id.open_edit); 		// 编辑
		openEdit.setOnClickListener(this);
		fragmentList = new ArrayList<Fragment>();					//存放 Fragment
		viewPager = (MyViewPager) findViewById(R.id.viewpager);
		allText = (TextView) findViewById(R.id.text_all); 			// 全部
		allText.setOnClickListener(new TxListener(0));
		allFragment = new TotalFragment();
		fragmentList.add(allFragment);

		//sequText = (TextView) findViewById(R.id.text_sequ); 		// 专辑
		//sequText.setOnClickListener(new TxListener(1));
		//sequFragment = new SequFragment();
		//fragmentList.add(sequFragment);

		soundText = (TextView) findViewById(R.id.text_sound); 		// 声音
		soundText.setOnClickListener(new TxListener(1));
		soundFragment = new SoundFragment();
		fragmentList.add(soundFragment);

		radioText = (TextView) findViewById(R.id.text_radio); 		// 电台
		radioText.setOnClickListener(new TxListener(2));
		radioFragment = new RadioFragment();
		fragmentList.add(radioFragment);

		ttsText = (TextView) findViewById(R.id.text_tts); 			// TTS
		ttsText.setOnClickListener(new TxListener(3));
		ttsFragment = new TTSFragment();
		fragmentList.add(ttsFragment);

		viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener()); 	// 页面变化时的监听器
		viewPager.setCurrentItem(0); 										// 设置当前显示标签页为第一页
	}

	/*
	 * TextView 点击事件
	 */
	class TxListener implements OnClickListener {
		private int index = 0;

		public TxListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	}

	/*
	 * ViewPager 设置适配器
	 */
	class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
	}

	/*
	 * ViewPager 监听事件设置
	 */
	class MyOnPageChangeListener implements OnPageChangeListener {
		private int one = offset * 2 + bmpW;	// 两个相邻页面的偏移量

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = new TranslateAnimation(currIndex * one, arg0 * one, 0, 0);// 平移动画
			currIndex = arg0;
			animation.setFillAfter(true); 		// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200); 		// 动画持续时间0.2秒
			image.startAnimation(animation); 	// 是用ImageView来显示动画的
			int i = currIndex + 1;
			if (i == 1) { // 全部
				allText.setTextColor(context.getResources().getColor(R.color.dinglan_orange));

				//sequText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				soundText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				radioText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				ttsText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				clearEmpty.setVisibility(View.VISIBLE);
				openEdit.setVisibility(View.GONE);

			} /*else if (i == 2) { // 专辑
				//sequText.setTextColor(context.getResources().getColor(R.color.dinglan_orange));

				allText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				soundText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				radioText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				ttsText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				clearEmpty.setVisibility(View.GONE);
				openEdit.setVisibility(View.VISIBLE);
			}*/ else if (i == 2) { // 声音
				soundText.setTextColor(context.getResources().getColor(R.color.dinglan_orange));
				allText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				
				//sequText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				radioText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				ttsText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				clearEmpty.setVisibility(View.GONE);
				openEdit.setVisibility(View.VISIBLE);
			} else if (i == 3) { // 电台
				radioText.setTextColor(context.getResources().getColor(R.color.dinglan_orange));
				allText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				
				//sequText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				soundText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				ttsText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				clearEmpty.setVisibility(View.GONE);
				openEdit.setVisibility(View.VISIBLE);
			} else if (i == 4) { // TTS
				ttsText.setTextColor(context.getResources().getColor(R.color.dinglan_orange));
				allText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				
				//sequText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				soundText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				radioText.setTextColor(context.getResources().getColor(R.color.group_item_text2));
				clearEmpty.setVisibility(View.GONE);
				openEdit.setVisibility(View.VISIBLE);
			}
			setCancle();
		}
	}

	/*
	 * 编辑设置
	 */
	private void setEdit() {
		int i = 0;
		i = currIndex + 1;
		switch (i) {
		/*
		case 2: // 专辑
			if (!SequFragment.isData) { // 判断当前界面是否有数据
				Toast.makeText(getApplicationContext(), "没有历史播放数据", Toast.LENGTH_SHORT).show();
			} else {
				delDialog();
				openEdit.setText("取消");
				PlayHistoryActivity.isEdit = false;
				sequFragment.setCheck(true);
				delDialog.show();
				sequFragment.setLinearVisibility();
			}
			break;
			*/
		case 2: // 声音
			if (!SoundFragment.isData) {
				Toast.makeText(getApplicationContext(), "没有历史播放数据", Toast.LENGTH_SHORT).show();
			} else {
				delDialog();
				openEdit.setText("取消");
				PlayHistoryActivity.isEdit = false;
				soundFragment.setCheck(true);
				delDialog.show();
				soundFragment.setLinearVisibility();
			}
			break;
		case 3: // 电台
			if (!RadioFragment.isData) {
				Toast.makeText(getApplicationContext(), "没有历史播放数据", Toast.LENGTH_SHORT).show();
			} else {
				delDialog();
				openEdit.setText("取消");
				PlayHistoryActivity.isEdit = false;
				radioFragment.setCheck(true);
				delDialog.show();
				radioFragment.setLinearVisibility();
			}
			break;
		case 4: // TTS
			if (!TTSFragment.isData) {
				Toast.makeText(getApplicationContext(), "没有历史播放数据", Toast.LENGTH_SHORT).show();
			} else {
				delDialog();
				openEdit.setText("取消");
				PlayHistoryActivity.isEdit = false;
				ttsFragment.setCheck(true);
				delDialog.show();
				ttsFragment.setLinearVisibility();
			}
			break;
		}
	}
	
	/*
	 * 取消设置
	 */
	private void setCancle(){
		int i = 0;
		i = currIndex + 1;
		switch (i) {
		/*
		case 2: // 专辑
			sequFragment.setCheck(false);
			sequFragment.setCheckStatus(0);
			sequFragment.setLinearHint();
			break;
			*/
		case 2: // 声音
			soundFragment.setCheck(false);
			soundFragment.setCheckStatus(0);
			soundFragment.setLinearHint();
			break;
		case 3: // 电台
			radioFragment.setCheck(false);
			radioFragment.setCheckStatus(0);
			radioFragment.setLinearHint();
			break;
		case 4: // TTS
			ttsFragment.setCheck(false);
			ttsFragment.setCheckStatus(0);
			ttsFragment.setLinearHint();
			break;
		}
		if(delDialog != null){
			delDialog.dismiss();
		}
		PlayHistoryActivity.isEdit = true;
		openEdit.setText("编辑");
		dialogflag = 0;
	}

	/*
	 * 设置cursor的宽
	 */
	public void InitImage() {
		image = (ImageView) findViewById(R.id.cursor);
		LayoutParams lp = image.getLayoutParams();
		lp.width = (PhoneMessage.ScreenWidth / 4);
		image.setLayoutParams(lp);
		bmpW = BitmapFactory.decodeResource(getResources(), R.mipmap.left_personal_bg).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 4 - bmpW) / 2;
		// imgageview设置平移，使下划线平移到初始位置（平移一个offset）
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		image.setImageMatrix(matrix);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_left_btn:	//左上角返回键
			finish();
			break;
		case R.id.clear_empty:		//清空数据
			if (TotalFragment.isData) {
				confirmdialog();
				confirmdialog.show();
			} else {
				ToastUtils.show_allways(this, "没有历史播放记录");
			}
			break;
		case R.id.open_edit:		//编辑
			if (isEdit) {
				setEdit();
			} else {
				setCancle();
			}
			break;
		}
	}

	/*
	 * 编辑状态下的对话框 在界面底部显示
	 */
	private void delDialog() {
		final View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_fravorite, null);
		LinearLayout lin_favorite_quanxuan = (LinearLayout) dialog.findViewById(R.id.lin_favorite_quanxuan);
		LinearLayout lin_favorite_shanchu = (LinearLayout) dialog.findViewById(R.id.lin_favorite_shanchu);
		imgQuanxuan = (ImageView) dialog.findViewById(R.id.img_fravorite_quanxuan);
		delDialog = new Dialog(context, R.style.MyDialog_duijiang);
		delDialog.setContentView(dialog); // 从底部上升到一个位置
		Window window = delDialog.getWindow();
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenw = dm.widthPixels;
		LayoutParams params = dialog.getLayoutParams();
		params.width = (int) screenw;
		dialog.setLayoutParams(params);
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.sharestyle);
		// 设置dialog不获取焦点
		window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		delDialog.setCanceledOnTouchOutside(false);
		
		lin_favorite_quanxuan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialogflag == 0) {
					imgQuanxuan.setImageResource(R.mipmap.wt_group_checked);
					dialogflag = 1;
				} else if(dialogflag == 1){
					imgQuanxuan.setImageResource(R.mipmap.wt_group_nochecked);
					dialogflag = 0;
				}
				handledata(dialogflag);
			}
		});

		lin_favorite_shanchu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				delete();
				if(isDelete){
					allFragment.getData();
					delDialog.dismiss();
					setCancle();
				}else{
					Toast.makeText(context, "请选择你要删除的历史播放记录", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	/**
	 * 处理数据
	 */
	private void handledata(int status) {
		switch (currIndex) {
		/*
		case 1:// 专辑
			sequFragment.setCheckStatus(status);
			break;
			*/
		case 1:// 声音
			soundFragment.setCheckStatus(status);
			break;
		case 2:// 电台
			radioFragment.setCheckStatus(status);
			break;
		case 3 :// TTS
			ttsFragment.setCheckStatus(status);
			break;
		}
	}
	
	/*
	 * 删除数据
	 */ 
	private void delete(){
		int number = 0;
		String message = "";
		switch (currIndex) {
		/*
		case 1:// 专辑
			number = sequFragment.deleteData();
			message = "专辑";
			break;
			*/
		case 1:// 声音
			number = soundFragment.deleteData();
			message = "声音";
			break;
		case 2:// 电台
			number = radioFragment.deleteData();
			message = "电台";
			break;
		case 3:// TTS
			number = ttsFragment.deleteData();
			message = "TTS";
			break;
		}
		if(number > 0){
			isDelete = true;
			Toast.makeText(context, "删除了 " + number + " 条" + message + "播放历史记录", Toast.LENGTH_SHORT).show();
		}
	}
	
	//查看更多
	public void updateViewPager(String mediatype){ 
		int index = 0;
		if(mediatype != null && !mediatype.equals("")){
			if(mediatype.equals("AUDIO")){
				index = 1; 
			}else if(mediatype.equals("RADIO")){
				index = 2;  
			}else if(mediatype.equals("TTS")){
				index = 3; 
			}
			viewPager.setCurrentItem(index);
		}
	}

	//清空所有数据 对话框
	private void confirmdialog() {
		final View dialog1 = LayoutInflater.from(this).inflate(R.layout.dialog_exit_confirm, null);
		TextView tv_cancle = (TextView) dialog1.findViewById(R.id.tv_cancle);
		TextView tv_confirm = (TextView) dialog1.findViewById(R.id.tv_confirm);
		TextView tv_title = (TextView) dialog1.findViewById(R.id.tv_title);
		tv_title.setText("是否清空全部历史记录");
		confirmdialog = new Dialog(this, R.style.MyDialog);
		confirmdialog.setContentView(dialog1);
		confirmdialog.setCanceledOnTouchOutside(true);
		confirmdialog.getWindow().setBackgroundDrawableResource(R.color.dialog);

		tv_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmdialog.dismiss();
			}
		});

		tv_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dbdao.deleteHistoryAll();
				allFragment.getData();
				if(SoundFragment.isData && SoundFragment.isLoad){
					soundFragment.getData();
				}
				if(RadioFragment.isData && RadioFragment.isLoad){
					radioFragment.getData();
				}
				if(TTSFragment.isData && TTSFragment.isLoad){
					ttsFragment.getData();
				}
				confirmdialog.dismiss();
			}
		});
	}
	
	/**
	 * 广播接收器  接收 Fragment 发送的广播  用于更新全选状态
	 */
	private BroadcastReceiver myBroadcast = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(PlayHistoryActivity.UPDATA_ACTION_ALL)) {
				imgQuanxuan.setImageResource(R.mipmap.wt_group_checked);
				dialogflag = 1;
			}else if(action.equals(PlayHistoryActivity.UPDATA_ACTION_CHECK)){
				imgQuanxuan.setImageResource(R.mipmap.wt_group_nochecked);
				dialogflag = 0;
			}
		}
	};
	
	// 返回键功能
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
			if (!isEdit) {
				setCancle();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.popOneActivity(context);	// 将 Activity 从集合中移除
		unregisterReceiver(myBroadcast);// 反注册广播
		SoundFragment.isLoad = false;
		RadioFragment.isData = false;
		TTSFragment.isLoad = false;
		context = null;
		image = null;
		allText = null;
		soundText = null;
		radioText = null;
		ttsText = null;
		clearEmpty = null;
		openEdit = null;
		lin_back = null;
		viewPager = null;
		soundFragment = null;
		radioFragment = null;
		ttsFragment = null;
		setContentView(R.layout.activity_null);
	}
}
