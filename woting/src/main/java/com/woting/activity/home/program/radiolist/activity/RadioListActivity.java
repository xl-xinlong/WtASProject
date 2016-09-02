package com.woting.activity.home.program.radiolist.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.R;
import com.woting.activity.home.program.fenlei.model.fenleiname;
import com.woting.activity.home.program.radiolist.adapter.MyPagerAdaper;
import com.woting.activity.home.program.radiolist.fragment.ClassifyFragment;
import com.woting.activity.home.program.radiolist.fragment.RecommendFragment;
import com.woting.activity.home.program.radiolist.mode.CatalogData;
import com.woting.activity.home.program.radiolist.mode.SubCata;
import com.woting.common.config.GlobalConfig;
import com.woting.common.volley.VolleyCallback;
import com.woting.common.volley.VolleyRequest;
import com.woting.util.CommonUtils;
import com.woting.util.DialogUtils;
import com.woting.util.PhoneMessage;
import com.woting.util.ToastUtils;
import com.woting.widgetui.PagerSlidingTabStrip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 某一分类数据
 * @author 辛龙 
 * 2016年4月5日
 */
public class RadioListActivity extends FragmentActivity implements OnClickListener {
	private LinearLayout head_left_btn;		// 返回
	private TextView mtextview_head;
	public static String CatagoryName;
	public static String CatagoryType;
	public static String id;
	private Dialog dialog;					// 加载对话框
	private List<String> list;
	private List<Fragment> fragments;
	private PagerSlidingTabStrip pageSlidingTab;
	private ViewPager viewPager;
	private int count = 1;
	public static final String tag = "RADIOLIST_VOLLEY_REQUEST_CANCEL_TAG";
	public static boolean isCancelRequest;
	private RecommendFragment reco;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radiolist);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);		// 透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);	// 透明导航栏
		fragments = new ArrayList<Fragment>();
		setview();
		HandleRequestType();
		if(list == null){
			list = new ArrayList<String>();
			list.add("推荐");
			reco = new RecommendFragment();
			fragments.add(reco);
		}
		sendRequest();
		dialog = DialogUtils.Dialogph(this, "正在获取数据", dialog);
	}

	/**
	 * 接收上一个页面传递过来的数据
	 */
	private void HandleRequestType() {
		Intent listIntent = getIntent();
		if (listIntent != null) {
			fenleiname list = (fenleiname) listIntent.getSerializableExtra("Catalog");
			CatagoryName = list.getCatalogName();
			CatagoryType = list.getCatalogType();
			id = list.getCatalogId();
			mtextview_head.setText(CatagoryName);
		}
	}

	/**
	 * 请求网络获取分类信息
	 */
	private void sendRequest(){
		VolleyRequest.RequestPost(GlobalConfig.getCatalogUrl, tag, setParam(), new VolleyCallback() {
			private String ReturnType;
			private List<SubCata> subcataList;
			private String CatalogData;

			@Override
			protected void requestSuccess(JSONObject result) {
//				closeDialog();
				try {
					ReturnType = result.getString("ReturnType");
					CatalogData = result.getString("CatalogData");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (ReturnType != null && ReturnType.equals("1001")) {
					CatalogData catalogData = new Gson().fromJson(CatalogData, new TypeToken<CatalogData>() {}.getType());
					subcataList = catalogData.getSubCata();
					if(subcataList != null && subcataList.size() > 0){
						for(int i=0; i<subcataList.size(); i++){
							list.add(subcataList.get(i).getCatalogName());
							fragments.add(ClassifyFragment.instance(subcataList.get(i).getCatalogId(), subcataList.get(i).getCatalogType()));
							count++;
						}
					}
					viewPager.setAdapter(new MyPagerAdaper(getSupportFragmentManager(), list, fragments));
					pageSlidingTab.setViewPager(viewPager);
					if(count == 1){
						pageSlidingTab.setVisibility(View.GONE);
					}
				} else {
					ToastUtils.show_allways(RadioListActivity.this, "暂没有该分类数据");
				}
			}

			@Override
			protected void requestError(VolleyError error) {
				closeDialog();
			}
		});
	}

	private JSONObject setParam(){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("SessionId", CommonUtils.getSessionId(RadioListActivity.this));
			jsonObject.put("MobileClass", PhoneMessage.model + "::" + PhoneMessage.productor);
			jsonObject.put("ScreenSize", PhoneMessage.ScreenWidth + "x" + PhoneMessage.ScreenHeight);
			jsonObject.put("IMEI", PhoneMessage.imei);
			PhoneMessage.getGps(RadioListActivity.this);
			jsonObject.put("GPS-longitude", PhoneMessage.longitude);
			jsonObject.put("GPS-latitude ", PhoneMessage.latitude);
			jsonObject.put("PCDType", GlobalConfig.PCDType);
			// 模块属性
			jsonObject.put("UserId", CommonUtils.getUserId(RadioListActivity.this));
			jsonObject.put("CatalogType", CatagoryType);
			jsonObject.put("CatalogId", id);
			jsonObject.put("Page", "1");
			jsonObject.put("ResultType", "1");
			jsonObject.put("RelLevel", "0");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 关闭加载对话框
	 */
	public void closeDialog(){
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == 1) {
				finish();
			}
			break;
		}
	}

	/**
	 * 初始化界面
	 */
	private void setview() {
		head_left_btn = (LinearLayout) findViewById(R.id.head_left_btn);
		head_left_btn.setOnClickListener(this);
		mtextview_head = (TextView) findViewById(R.id.head_name_tv);
		pageSlidingTab = (PagerSlidingTabStrip) findViewById(R.id.tabs_title);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		pageSlidingTab.setIndicatorHeight(4);								// 滑动指示器的高度
		pageSlidingTab.setIndicatorColorResource(R.color.dinglan_orange);	// 滑动指示器的颜色
		pageSlidingTab.setDividerColorResource(R.color.WHITE);				// 菜单之间的分割线颜色
		pageSlidingTab.setSelectedTextColorResource(R.color.dinglan_orange);// 选中的字体颜色
		pageSlidingTab.setTextColorResource(R.color.wt_login_third);		// 默认字体颜色
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_left_btn:
			finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isCancelRequest = VolleyRequest.cancelRequest(tag);
		pageSlidingTab = null;
		viewPager = null;
		head_left_btn = null;
		mtextview_head = null;
		dialog = null;
		if(list != null){
			list.clear();
			list = null;
		}
		reco = null;
		if(fragments != null){
			fragments.clear();
			fragments = null;
		}
		setContentView(R.layout.activity_null);
	}
}
