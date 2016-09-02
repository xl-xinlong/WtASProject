package com.woting.activity.person.favorite.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.R;
import com.woting.activity.home.main.HomeActivity;
import com.woting.activity.home.player.main.dao.SearchPlayerHistoryDao;
import com.woting.activity.home.player.main.fragment.PlayerFragment;
import com.woting.activity.home.player.main.model.PlayerHistory;
import com.woting.activity.home.program.album.activity.AlbumActivity;
import com.woting.activity.home.program.fmlist.model.RankInfo;
import com.woting.activity.home.search.adapter.SearchContentAdapter;
import com.woting.activity.home.search.model.SuperRankInfo;
import com.woting.activity.main.MainActivity;
import com.woting.activity.person.favorite.activity.FavoriteActivity;
import com.woting.common.config.GlobalConfig;
import com.woting.common.constant.StringConstant;
import com.woting.common.volley.VolleyCallback;
import com.woting.common.volley.VolleyRequest;
import com.woting.util.CommonUtils;
import com.woting.util.DialogUtils;
import com.woting.util.PhoneMessage;
import com.woting.util.ToastUtils;

/**
 * 我喜欢的 全部界面
 */
public class TotalFragment extends Fragment {
	private View rootView;
	private FragmentActivity context;
	private ExpandableListView ex_listview;
	private ArrayList<RankInfo> playlist; 	// 节目list
	private ArrayList<RankInfo> sequlist; 	// 专辑list
	private ArrayList<RankInfo> ttslist; 	// tts
	private ArrayList<RankInfo> radiolist; 	// radio
	private ArrayList<SuperRankInfo> list = new ArrayList<SuperRankInfo>();// 返回的节目list，拆分之前的list
	private List<RankInfo> SubList;
	private List<String> dellist;
	private SearchContentAdapter searchadapter;
	private SearchPlayerHistoryDao dbdao;
	private Dialog DelDialog;
	private Dialog dialog;
	private int delchildposition = -1;
	private int delgroupposition = -1;
	private Intent mintent;
	private String tag = "TOTAL_VOLLEY_REQUEST_CANCEL_TAG";
	private boolean isCancelRequest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		initDao();
		DelDialog();
		mintent = new Intent();
		mintent.setAction(FavoriteActivity.VIEW_UPDATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_favorite_total, container, false);
			ex_listview = (ExpandableListView) rootView.findViewById(R.id.ex_listview);
			ex_listview.setGroupIndicator(null);			// 去除indicator
			setListener();
			IntentFilter myfileter = new IntentFilter();
			myfileter.addAction(FavoriteActivity.VIEW_UPDATE);
			context.registerReceiver(mBroadcastReceiver, myfileter);
			if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
				dialog = DialogUtils.Dialogph(context, "正在获取全部喜欢信息", dialog);
				send();
			} else {
				ToastUtils.show_allways(context, "网络失败，请检查网络");
			}
		}
		return rootView;
	}

	/**
	 * 初始化数据库
	 */
	private void initDao() {
		dbdao = new SearchPlayerHistoryDao(context);
	}

	/**
	 * 控件点击事件监听
	 */
	private void setListener() {
		/**
		 * 屏蔽group点击事件
		 */
		ex_listview.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				FavoriteActivity.updateviewpageer(list.get(groupPosition).getKey());
				return true;
			}
		});

		/**
		 * 长按删除喜欢
		 */
		ex_listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View childView, int flatPos, long id) {
				if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
					long packedPos = ((ExpandableListView) parent).getExpandableListPosition(flatPos);
					delgroupposition = ExpandableListView.getPackedPositionGroup(packedPos);
					delchildposition = ExpandableListView.getPackedPositionChild(packedPos);
					if (delgroupposition != -1 && delchildposition != -1) {
						DelDialog.show();
					}
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 长按单条删除数据对话框
	 */
	private void DelDialog() {
		final View dialog1 = LayoutInflater.from(context).inflate(R.layout.dialog_exit_confirm, null);
		TextView tv_cancle = (TextView) dialog1.findViewById(R.id.tv_cancle);
		TextView tv_title = (TextView) dialog1.findViewById(R.id.tv_title);
		TextView tv_confirm = (TextView) dialog1.findViewById(R.id.tv_confirm);
		tv_title.setText("确定?");
		DelDialog = new Dialog(context, R.style.MyDialog);
		DelDialog.setContentView(dialog1);
		DelDialog.setCanceledOnTouchOutside(false);
		DelDialog.getWindow().setBackgroundDrawableResource(R.color.dialog);

		/**
		 * 取消
		 */
		tv_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DelDialog.dismiss();
			}
		});

		/**
		 * 删除
		 */
		tv_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
					dialog = DialogUtils.Dialogph(context, "正在删除", dialog);
					if (dellist == null) {
						dellist = new ArrayList<String>();
						String type = list.get(delgroupposition).getList().get(delchildposition).getMediaType();
						String contentid = list.get(delgroupposition).getList().get(delchildposition).getContentId();
						dellist.add(type + "::" + contentid);
					} else {
						String type = list.get(delgroupposition).getList().get(delchildposition).getMediaType();
						String contentid = list.get(delgroupposition).getList().get(delchildposition).getContentId();
						dellist.add(type + "::" + contentid);
					}
					sendrequest();
				} else {
					ToastUtils.show_allways(context, "网络失败，请检查网络");
				}
				DelDialog.dismiss();
			}
		});
	}

	/**
	 * 执行删除单条喜欢的方法
	 */
	protected void sendrequest() {
		JSONObject jsonObject = new JSONObject();
		try {
			// 公共请求属性
			jsonObject.put("SessionId", CommonUtils.getSessionId(context));
			jsonObject.put("MobileClass", PhoneMessage.model + "::" + PhoneMessage.productor);
			jsonObject.put("ScreenSize", PhoneMessage.ScreenWidth + "x" + PhoneMessage.ScreenHeight);
			jsonObject.put("IMEI", PhoneMessage.imei);
			PhoneMessage.getGps(context);
			jsonObject.put("GPS-longitude", PhoneMessage.longitude);
			jsonObject.put("GPS-latitude ", PhoneMessage.latitude);
			jsonObject.put("PCDType",GlobalConfig.PCDType);
			// 模块属性
			jsonObject.put("UserId", CommonUtils.getUserId(context));
			// 对s进行处理 去掉"[]"符号
			String s = dellist.toString();
			jsonObject.put("DelInfos", s.substring(1, s.length() - 1).replaceAll(" ", ""));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		VolleyRequest.RequestPost(GlobalConfig.delFavoriteListUrl, tag, jsonObject, new VolleyCallback() {
			private String ReturnType;
			private String Message;
//			private String SessionId;

			@Override
			protected void requestSuccess(JSONObject result) {
				dellist.clear();
				if(isCancelRequest){
					return ;
				}
				try {
					ReturnType = result.getString("ReturnType");
//					SessionId = result.getString("SessionId");
					Message = result.getString("Message");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (ReturnType != null && ReturnType.equals("1001")) {
					if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
						send();
						context.sendBroadcast(mintent);
					} else {
						ToastUtils.show_allways(context, "网络失败，请检查网络");
					}
				} else if (ReturnType != null && ReturnType.equals("1002")) {
					ToastUtils.show_allways(context, "无法获取用户Id");
				} else if (ReturnType != null && ReturnType.equals("T")) {
					ToastUtils.show_allways(context, "异常返回值");
				} else if (ReturnType != null && ReturnType.equals("200")) {
					ToastUtils.show_allways(context, "尚未登录");
				} else if (ReturnType != null && ReturnType.equals("1003")) {
					ToastUtils.show_allways(context, "异常返回值");
				} else {
					if (Message != null && !Message.trim().equals("")) {
						ToastUtils.show_allways(context, Message + "");
					}
				}
			}
			
			@Override
			protected void requestError(VolleyError error) {
				if (dialog != null) {
					dialog.dismiss();
				}
				dellist.clear();
			}
		});
	}

	/**
	 * 请求网络获取数据
	 */
	private void send() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("SessionId", CommonUtils.getSessionId(context));
			jsonObject.put("MobileClass", PhoneMessage.model + "::" + PhoneMessage.productor);
			jsonObject.put("ScreenSize", PhoneMessage.ScreenWidth + "x" + PhoneMessage.ScreenHeight);
			jsonObject.put("IMEI", PhoneMessage.imei);
			PhoneMessage.getGps(context);
			jsonObject.put("GPS-longitude", PhoneMessage.longitude);
			jsonObject.put("GPS-latitude ", PhoneMessage.latitude);
			jsonObject.put("PCDType", GlobalConfig.PCDType);
			jsonObject.put("PageSize", "12");
			jsonObject.put("UserId", CommonUtils.getUserId(context));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		VolleyRequest.RequestPost(GlobalConfig.getFavoriteListUrl, tag, jsonObject, new VolleyCallback() {
//			private String SessionId;
			private String ReturnType;
			private String Message;
			private String resultlist;
			private JSONObject arg1;
			private String StringSubList;

			@Override
			protected void requestSuccess(JSONObject result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if(isCancelRequest){
					return ;
				}
				try {
//					SessionId = result.getString("SessionId");
					ReturnType = result.getString("ReturnType");
					Message = result.getString("Message");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (ReturnType != null && ReturnType.equals("1001")) {
					try {
						resultlist = result.getString("ResultList");
						JSONTokener jsonParser = new JSONTokener(resultlist);
						arg1 = (JSONObject) jsonParser.nextValue();
						StringSubList = arg1.getString("FavoriteList");
						SubList = new Gson().fromJson(StringSubList, new TypeToken<List<RankInfo>>() {}.getType());
					} catch (Exception e) {
						e.printStackTrace();
					}
					list.clear();
					if (playlist != null) {
						playlist.clear();
					}
					if (sequlist != null) {
						sequlist.clear();
					}
					if (SubList != null && SubList.size() > 0) {
						for (int i = 0; i < SubList.size(); i++) {
							if (SubList.get(i).getMediaType() != null && !SubList.get(i).getMediaType().equals("")) {
								if (SubList.get(i).getMediaType().equals("AUDIO")) {
									if (playlist == null) {
										playlist = new ArrayList<RankInfo>();
										playlist.add(SubList.get(i));
									} else {
										if (playlist.size() < 3) {
											playlist.add(SubList.get(i));
										}
									}
								} else if (SubList.get(i).getMediaType().equals("SEQU")) {
									if (sequlist == null) {
										sequlist = new ArrayList<RankInfo>();
										sequlist.add(SubList.get(i));
									} else {
										if (sequlist.size() < 3) {
											sequlist.add(SubList.get(i));
										}
									}
								} else if (SubList.get(i).getMediaType().equals("TTS")) {
									if (ttslist == null) {
										ttslist = new ArrayList<RankInfo>();
										ttslist.add(SubList.get(i));
									} else {
										if (ttslist.size() < 3) {
											ttslist.add(SubList.get(i));
										}
									}
								} else if (SubList.get(i).getMediaType().equals("RADIO")) {
									if (radiolist == null) {
										radiolist = new ArrayList<RankInfo>();
										radiolist.add(SubList.get(i));
									} else {
										if (radiolist.size() < 3) {
											radiolist.add(SubList.get(i));
										}
									}
								}
							}
						}
						if (sequlist != null && sequlist.size() != 0) {
							SuperRankInfo mSuperRankInfo1 = new SuperRankInfo();
							mSuperRankInfo1.setKey(sequlist.get(0).getMediaType());
							mSuperRankInfo1.setList(sequlist);
							list.add(mSuperRankInfo1);
						}
						if (playlist != null && playlist.size() != 0) {
							SuperRankInfo mSuperRankInfo = new SuperRankInfo();
							mSuperRankInfo.setKey(playlist.get(0).getMediaType());
							mSuperRankInfo.setList(playlist);
							list.add(mSuperRankInfo);
						}
						if (ttslist != null && ttslist.size() != 0) {
							SuperRankInfo mSuperRankInfo1 = new SuperRankInfo();
							mSuperRankInfo1.setKey(ttslist.get(0).getMediaType());
							mSuperRankInfo1.setList(ttslist);
							list.add(mSuperRankInfo1);
						}
						if (radiolist != null && radiolist.size() != 0) {
							SuperRankInfo mSuperRankInfo1 = new SuperRankInfo();
							mSuperRankInfo1.setKey(radiolist.get(0).getMediaType());
							mSuperRankInfo1.setList(radiolist);
							list.add(mSuperRankInfo1);
						}
						if (list.size() != 0) {
							searchadapter = new SearchContentAdapter(context, list);
							ex_listview.setAdapter(searchadapter);
							for (int i = 0; i < list.size(); i++) {
								ex_listview.expandGroup(i);
							}
							setitemListener();
						} else {
							ToastUtils.show_short(context, "没有数据");
						}
					} else {
						ToastUtils.show_short(context, "数据获取异常");
					}
				} else if (ReturnType != null && ReturnType.equals("1002")) {
					ToastUtils.show_allways(context, "" + Message);
				} else if (ReturnType != null && ReturnType.equals("1011")) {
					ToastUtils.show_allways(context, "" + Message);
					ex_listview.setVisibility(View.GONE);
				} else {
					if (Message != null && !Message.trim().equals("")) {
						ToastUtils.show_allways(context, "加载失败" + Message);
					}
				}
			}
			
			@Override
			protected void requestError(VolleyError error) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
	}

	/**
	 * ExpandableListView Item 点击事件监听
	 */
	protected void setitemListener() {
		ex_listview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				String MediaType = null;
				try {
					MediaType = list.get(groupPosition).getList().get(childPosition).getMediaType();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (MediaType != null && MediaType.equals("RADIO") || MediaType.equals("AUDIO")) {
					String playername = list.get(groupPosition).getList().get(childPosition).getContentName();
					String playerimage = list.get(groupPosition).getList().get(childPosition).getContentImg();
					String playerurl = list.get(groupPosition).getList().get(childPosition).getContentPlay();
					String playerurI = list.get(groupPosition).getList().get(childPosition).getContentURI();
					String playermediatype = list.get(groupPosition).getList().get(childPosition).getMediaType();
					String plaplayeralltime = "0";
					String playerintime = "0";
					String playercontentdesc = list.get(groupPosition).getList().get(childPosition).getCurrentContent();
					String playernum = list.get(groupPosition).getList().get(childPosition).getWatchPlayerNum();
					String playerzantype = "0";
					String playerfrom = "";
					String playerfromid = "";
					String playerfromurl = "";
					String playeraddtime = Long.toString(System.currentTimeMillis());
					String bjuserid = CommonUtils.getUserId(context);
					String playcontentshareurl = list.get(groupPosition).getList().get(childPosition).getContentShareURL();
					String ContentFavorite = list.get(groupPosition).getList().get(childPosition).getContentFavorite();
					String ContentId = list.get(groupPosition).getList().get(childPosition).getContentId();
					String localurl = list.get(groupPosition).getList().get(childPosition).getLocalurl();

					// 如果该数据已经存在数据库则删除原有数据，然后添加最新数据
					PlayerHistory history = new PlayerHistory(playername, playerimage, playerurl, playerurI,
							playermediatype, plaplayeralltime, playerintime, playercontentdesc, playernum,
							playerzantype, playerfrom, playerfromid, playerfromurl, playeraddtime, bjuserid,
							playcontentshareurl, ContentFavorite, ContentId, localurl);
					dbdao.deleteHistory(playerurl);
					dbdao.addHistory(history);
					if (PlayerFragment.context != null) {
						MainActivity.change();
						HomeActivity.UpdateViewPager();
						PlayerFragment.SendTextRequest(list.get(groupPosition).getList().get(childPosition).getContentName(), context);
						getActivity().finish();
					} else {
						SharedPreferences sp = context.getSharedPreferences("wotingfm", Context.MODE_PRIVATE);
						Editor et = sp.edit();
						et.putString(StringConstant.PLAYHISTORYENTER, "true");
						et.putString(StringConstant.PLAYHISTORYENTERNEWS, list.get(groupPosition).getList().get(childPosition).getContentName());
						et.commit();
						MainActivity.change();
						HomeActivity.UpdateViewPager();
						getActivity().finish();
					}
				} else if (MediaType!=null&&MediaType.equals("SEQU")) {	
					Intent intent = new Intent(context, AlbumActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", "search");
					bundle.putSerializable("list", list.get(groupPosition).getList().get(childPosition));
					intent.putExtras(bundle);
					startActivityForResult(intent, 1);
				} else {
					ToastUtils.show_short(context, "暂不支持的Type类型");
				}
				return true;
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if(resultCode==1){
				getActivity().finish();
			}
			break;
		}
	}

	/**
	 * 广播接收器 用于刷新界面
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(FavoriteActivity.VIEW_UPDATE)) {
				if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
					send();
				} else {
					ToastUtils.show_allways(context, "网络失败，请检查网络");
				}
			}
		}
	};

	/**
	 * 获取当前页面选中的为选中的数目
	 */
	public int getdelitemsum() {
		int sum = 0;
		if (SubList == null) {
			return sum;
		} else {
			sum = SubList.size();
		}
		return sum;
	}

	/**
	 * 删除
	 */
	public void delitem() {
		if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
			dialog = DialogUtils.Dialogph(context, "正在删除", dialog);
			for (int i = 0; i < SubList.size(); i++) {
				if (dellist == null) {
					dellist = new ArrayList<String>();
					String type = SubList.get(i).getMediaType();
					String contentid = SubList.get(i).getContentId();
					dellist.add(type + "::" + contentid);
				} else {
					String type = SubList.get(i).getMediaType();
					String contentid = SubList.get(i).getContentId();
					dellist.add(type + "::" + contentid);
				}
			}
			sendrequest();
		} else {
			ToastUtils.show_allways(context, "网络失败，请检查网络");
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroy();
		if (null != rootView) {
			((ViewGroup) rootView.getParent()).removeView(rootView);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isCancelRequest = VolleyRequest.cancelRequest(tag);
		context.unregisterReceiver(mBroadcastReceiver);
		ex_listview = null;
		DelDialog = null;
		rootView = null;
		context = null;
		playlist = null;
		sequlist = null;
		ttslist = null;
		radiolist = null;
		list = null;
		SubList = null;
		dellist = null;
		searchadapter = null;
		dialog = null;
		mintent = null;
		tag = null;
		if(dbdao != null){
			dbdao.closedb();
			dbdao = null;
		}
	}
}
