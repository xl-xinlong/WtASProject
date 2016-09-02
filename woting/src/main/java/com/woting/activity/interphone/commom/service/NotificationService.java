package com.woting.activity.interphone.commom.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.R;
import com.woting.activity.interphone.commom.message.MessageUtils;
import com.woting.activity.interphone.commom.message.MsgNormal;
import com.woting.activity.interphone.commom.message.content.MapContent;
import com.woting.activity.interphone.commom.model.ApplyUserInfo;
import com.woting.activity.interphone.commom.model.BeInvitedUserInfo;
import com.woting.activity.interphone.commom.model.GroupInfo;
import com.woting.activity.interphone.commom.model.InviteUserInfo;
import com.woting.activity.interphone.commom.model.UserInfo;
import com.woting.activity.interphone.linkman.dao.NotifyHistoryDao;
import com.woting.activity.interphone.linkman.model.DBNotifyHistorary;
import com.woting.common.config.GlobalConfig;
import com.woting.util.CommonUtils;
import com.woting.util.JsonEncloseUtils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Notification
 * @author 辛龙
 * 2016年4月27日
 */
public  class NotificationService   extends  Service{
	private MessageReceiver Receiver;
	private NotificationService context;
	private NotifyHistoryDao dbdao;
	@Override
	public void onCreate() {
		super.onCreate();
		context=this;
		initDao();
		if(Receiver==null) {
			Receiver=new MessageReceiver();
			IntentFilter filter=new IntentFilter();
			filter.addAction("pushnotify");
			registerReceiver(Receiver, filter);
		}
	}

	private void initDao() {// 初始化数据库命令执行对象
		dbdao = new NotifyHistoryDao(context);
	}

	public void add(String type,String imageurl,String content,String title,String dealtime) {
		String addtime = Long.toString(System.currentTimeMillis());
		String bjuserid =CommonUtils.getUserId(context);
		DBNotifyHistorary history =new  DBNotifyHistorary( bjuserid,  type,  imageurl, content, 
				title,  dealtime, addtime);		
		dbdao.addNotifyHistory(history);
	}

	/*
	 * 接收socket的数据进行处理
	 */
	class MessageReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action=intent.getAction();
			if(action.equals("pushnotify")){
				//				MsgNormal message = (MsgNormal) intent.getSerializableExtra("outmessage");
				byte[] bt = intent.getByteArrayExtra("outmessage");
				Log.e("Notification接收器中数据", Arrays.toString(bt)+"");
				try {
					Log.e("Notification接收器中数据",JsonEncloseUtils.btToString(bt)+"");
				} catch (Exception e2) {
					e2.printStackTrace();
				}

				try {
					MsgNormal message = (MsgNormal) MessageUtils.buildMsgByBytes(bt);

					if(message!=null){
						int cmdType = message.getCmdType();
						switch (cmdType) {
						case 1:
							int command = message.getCommand();
							if(command==1){
								//介绍：当某用户被另一用户邀请为好友后，服务器向被邀请用户发送的消息。
								String news ;
								String dealtime = null;
								String imageurl = null;
								try {
									MapContent data = (MapContent) message.getMsgContent();
									Map<String, Object> map = data.getContentMap();
									String msg = new Gson ().toJson(map);

									JSONTokener jsonParser = new JSONTokener(msg);
									JSONObject arg1 = (JSONObject) jsonParser.nextValue();
									String inviteuserinfo= arg1.getString("InviteUserInfo");

									InviteUserInfo userinfo   = new Gson().fromJson(inviteuserinfo, new TypeToken<InviteUserInfo>() {}.getType());
									dealtime = data.get("InviteTime")+"";
									String name = userinfo.getUserName();
									imageurl = userinfo.getPortraitMini();
									if(name==null||name.trim().equals("")){
										news="有人申请添加您为好友,请查看";
									}else{
										news= name+"申请添加您为好友,请查看";
									}
								} catch (Exception e) {
									news="有人申请添加您为好友,请查看";
								}
								setNewMessageNotification(context, news, "我听");
								//已经添加在通讯录
								Intent pushintent=new Intent("push_newperson");
								Bundle bundle=new Bundle();
								bundle.putString("outmessage",news);
								pushintent.putExtras(bundle);
								context. sendBroadcast(pushintent);
								//	add("Ub1", imageurl, news, "好友邀请信息", dealtime);
							}else if(command==3){
								//好友邀请被接受或拒绝(Server->App)
								String news;
								String imageurl = null;
								String dealtime = null;
								String dealtype = null;
								try {

									MapContent data = (MapContent) message.getMsgContent();
									Map<String, Object> map = data.getContentMap();
									String msg = new Gson ().toJson(map);

									JSONTokener jsonParser = new JSONTokener(msg);
									JSONObject arg1 = (JSONObject) jsonParser.nextValue();
									String beinviteduserinfo= arg1.getString("BeInvitedUserInfo");

									BeInvitedUserInfo userinfo    = new Gson().fromJson(beinviteduserinfo, new TypeToken<BeInvitedUserInfo>() {}.getType());
									dealtime= data.get("DealTime")+"";
									dealtype= data.get("DealType")+"";
									String name = userinfo.getUserName();
									imageurl = userinfo.getPortraitMini();
									if(name==null||name.trim().equals("")){
										if(dealtype!=null&&dealtype.equals("1")){
											news="有人成为您好友了";
										}else{
											news="有人拒绝您添加为好友";
										}
									}else{
										if(dealtype!=null&&dealtype.equals("1")){
											news=name+"成为您好友了";
										}else{
											news=name+"拒绝添加您为好友";
										}
									}
								} catch (Exception e) {
									if(dealtype!=null&&dealtype.equals("1")){
										news="有人成为您好友了";
									}else{
										news="有人拒绝您添加为好友";
									}
								}
								if(dealtype!=null&&dealtype.equals("1")){
									setNewMessageNotification(context, news, "我听");
									add("Ub3", imageurl, news, "好友邀请信息", dealtime);
									Intent pushintent=new Intent("push_refreshlinkman");
									context. sendBroadcast(pushintent);
								}else{
									setNewMessageNotification(context, news, "我听");
									add("Ub3", imageurl, news, "好友邀请信息", dealtime);
								}
							}else if(command==5){
								//A与B原为好友，A把B从自己的好友中删除后，向B发送A已删除自己为好友的信息。
								//										Data data = message.getData();
								setNewMessageNotification(context, "测试：《该提示不显示》删除好友通知", "我听");
								Intent pushintent=new Intent("push_refreshlinkman");
								context. sendBroadcast(pushintent);
							}
							break;
						case 2:
							int command2 = message.getCommand();
							if(command2==1){
								//当某用户被自己好友邀请进某个组时，服务器向某用户发送的消息。
								String news;
								String friendname = null;
								String friendurl = null;
								String dealtime = null;
								try {
									MapContent data = (MapContent) message.getMsgContent();
									String friendid  =  data.get("FriendId")+"";
									if(friendid!=null&&!friendid.trim().equals("")&&GlobalConfig.list_person!=null&&GlobalConfig.list_person.size()>0){
										for(int i=0;i<GlobalConfig.list_person.size();i++){
											if(GlobalConfig.list_person.get(i).getUserId().equals(friendid)){
												friendname=GlobalConfig.list_person.get(i).getUserName();
												friendurl=GlobalConfig.list_person.get(i).getPortraitMini();
											}
										}
									}

									Map<String, Object> map = data.getContentMap();
									String msg = new Gson ().toJson(map);

									JSONTokener jsonParser = new JSONTokener(msg);
									JSONObject arg1 = (JSONObject) jsonParser.nextValue();
									String grouiInfo= arg1.getString("GroupInfo");

									GroupInfo userinfo    = new Gson().fromJson(grouiInfo, new TypeToken<GroupInfo>() {}.getType());
									dealtime = data.get("InviteTime")+"";
									String name = userinfo.getGroupName();
									if(friendname==null||friendname.trim().equals("")){
										if(name==null||name.trim().equals("")){
											news="有人邀请您加入对讲组";
										}else{
											news="有人邀请您加入对讲组:"+name;
										}
									}else{
										if(name==null||name.trim().equals("")){
											news=friendname+"邀请您加入对讲组";
										}else{
											news=friendname+"邀请您加入对讲组:"+name;
										}
									}
									setNewMessageNotification(context, news, "我听");
									//已经添加在通讯录
									Intent pushintent=new Intent("push_newperson");
									Bundle bundle=new Bundle();
									bundle.putString("outmessage",news);
									pushintent.putExtras(bundle);
									context. sendBroadcast(pushintent);
									//add("Gb1", friendurl, news, "组邀请信息", dealtime);
								} catch (Exception e) {
									Log.e("消息接收服务中Gb1的异常", e.toString());
								}
							}else if(command2==2){
								//当某用户申请加入组后，向组管理员发送有用户申请的消息
								String news;
								String dealtime = null;
								String username = null;
								String imageurl = null;
								try {
									MapContent data = (MapContent) message.getMsgContent();

									Map<String, Object> map = data.getContentMap();
									String msg = new Gson ().toJson(map);

									JSONTokener jsonParser = new JSONTokener(msg);
									JSONObject arg1 = (JSONObject) jsonParser.nextValue();
									String grouiInfo= arg1.getString("GroupInfo");
									String applyuserinfos= arg1.getString("ApplyUserInfo");

									ApplyUserInfo applyuserinfo    = new Gson().fromJson(applyuserinfos, new TypeToken<ApplyUserInfo>() {}.getType());
									GroupInfo userinfo    = new Gson().fromJson(grouiInfo, new TypeToken<GroupInfo>() {}.getType());

									username= applyuserinfo.getUserName();
									imageurl = applyuserinfo.getPortraitMini();
									dealtime=  data.get("ApplyTime")+"";
									String name = userinfo.getGroupName();
									if(username==null||username.trim().equals("")){
										if(name==null||name.trim().equals("")){
											news="有人申请加入您的对讲组";
										}else{
											news="有人申请加入您的对讲组:"+name;
										}
									}else{
										if(name==null||name.trim().equals("")){
											news=username+"申请加入您的对讲组";
										}else{
											news=username+"申请加入您的对讲组:"+name;
										}
									}

									setNewMessageNotification(context, news, "我听");
									//组信息管理
									//add("Gb2", imageurl, news, "组邀请信息", dealtime);
								} catch (Exception e) {
									Log.e("消息接收服务中Gb2的异常", e.toString());
								}
							}else if(command2==3){
								//当某用户被邀请入组或申请入组的请求
								//被管理员或其他有权限的人员处理（接受或拒绝）后，向该用户发送处理结果的消息。
								String news;
								String dealtime = null;
								try {
									MapContent data = (MapContent) message.getMsgContent();
									Map<String, Object> map = data.getContentMap();
									String msg = new Gson ().toJson(map);

									JSONTokener jsonParser = new JSONTokener(msg);
									JSONObject arg1 = (JSONObject) jsonParser.nextValue();
									String grouiInfo= arg1.getString("GroupInfo");

									GroupInfo userinfo    = new Gson().fromJson(grouiInfo, new TypeToken<GroupInfo>() {}.getType());

									dealtime =  data.get("ApplyTime")+"";
									String dealtype  =  data.get("DealType")+"";
									String name = userinfo.getGroupName();
									if(dealtype!=null&&!dealtype.trim().equals("")){
										if(dealtype.equals("1")){//同意
											if(name==null||name.trim().equals("")){
												news="有一个新的入组申请已经通过";
											}else{
												news="对讲组:"+name+"同意了您的入组请求";
											}
											Intent pushintent=new Intent("push_refreshlinkman");
											context. sendBroadcast(pushintent);
										}else{//拒绝
											if(name==null||name.trim().equals("")){
												news="有一个您的入组申请什么没有通过";
											}else{
												news="对讲组:"+name+"没有同意您的入组请求";
											}
										}
										setNewMessageNotification(context, news, "我听");
										add("Gb3", "", news, "组邀请信息", dealtime);
									}
								} catch (Exception e) {
									Log.e("消息接收服务中Gb3的异常", e.toString());
								}
							}else if(command2==4){
								//当有某人加入组后，向组内成员发送这个消息。
								//注意：这个消息与1.3.1相同，今后要删除掉1.3.1
								String news;
								String groupid = null;
								String groupname = null;
								String groupurl = null;
								try {
									MapContent data = (MapContent) message.getMsgContent();

									Map<String, Object> map = data.getContentMap();
									String msg = new Gson ().toJson(map);

									JSONTokener jsonParser = new JSONTokener(msg);
									JSONObject arg1 = (JSONObject) jsonParser.nextValue();
									String userinfos= arg1.getString("UserInfo");

									UserInfo userinfo    = new Gson().fromJson(userinfos, new TypeToken<UserInfo>() {}.getType());
									groupid =  data.get("GroupId")+"";
									if(groupid!=null&&!groupid.trim().equals("")&&GlobalConfig.list_group!=null&&GlobalConfig.list_group.size()>0){
										for(int i=0;i<GlobalConfig.list_group.size();i++){
											if(GlobalConfig.list_group.get(i).getGroupId().equals(groupid)){
												groupname=GlobalConfig.list_group.get(i).getGroupName();
												groupurl=GlobalConfig.list_group.get(i).getGroupImg();
											}
										}
									}
									String name = userinfo.getUserName();
									if(name==null||name.trim().equals("")){
										if(groupname==null||groupname.trim().equals("")){
											news="有一个人加入到您所在的对讲组";
										}else{
											news="有一个人加入到您所在的对讲组:"+groupname;
										}
									}else{
										if(groupname==null||groupname.trim().equals("")){
											news=name+"加入到您所在的对讲组";
										}else{
											news=name+"加入到您所在的对讲组:"+groupname;
										}
									}
									setNewMessageNotification(context, news, "我听");
									add("Gb4", groupurl, news, "组信息", String.valueOf(System.currentTimeMillis()));
								} catch (Exception e) {
									Log.e("消息接收服务中Gb4的异常", e.toString());
								}
							}else if(command2==5){
								//当有某人退出组后（包括主动退出和被管理员踢出），向组内成员发送这个消息。
								String name = null;
								String news = null;
								String groupid = null;
								String groupname = null;
								String groupurl = null;
								try {
									MapContent data = (MapContent) message.getMsgContent();
									Map<String, Object> map = data.getContentMap();
									String msg = new Gson ().toJson(map);
									JSONTokener jsonParser = new JSONTokener(msg);
									JSONObject arg1 = (JSONObject) jsonParser.nextValue();

									UserInfo userinfo = null;
									try {
										String userinfos= arg1.getString("UserInfo");
										userinfo = new Gson().fromJson(userinfos, new TypeToken<UserInfo>() {}.getType());
									} catch (Exception e) {
										e.printStackTrace();
									}

									List<UserInfo> userlist = null;
									try {
										String userlists= arg1.getString("UserList");
										userlist = new Gson().fromJson(userlists, new TypeToken<List<UserInfo>>() {}.getType());
									} catch (Exception e) {
										e.printStackTrace();
									}
									groupid =  data.get("GroupId")+"";
									if(groupid!=null&&!groupid.trim().equals("")&&GlobalConfig.list_group!=null&&GlobalConfig.list_group.size()>0){
										for(int i=0;i<GlobalConfig.list_group.size();i++){
											if(GlobalConfig.list_group.get(i).getGroupId().equals(groupid)){
												groupname=GlobalConfig.list_group.get(i).getGroupName();
												groupurl=GlobalConfig.list_group.get(i).getGroupImg();
											}
										}
									}

									if(userinfo!=null&&!userinfo.getUserName().equals("")){
										name=userinfo.getUserName();
									}else if(userlist!=null&&userlist.size()>0){
										StringBuffer loginname = new StringBuffer();
										for(int i=0;i<userlist.size();i++){
											if(userlist.get(i).getUserName()!=null&&!userlist.get(i).getUserName().equals("")){
												loginname.append(userlist.get(i).getUserName());
											}
										}
										name=loginname.toString()+",";
										name=name.substring(0, name.length()-1);
									}else{
										name="";
									}

									if(name==null||name.trim().equals("")){
										if(groupname==null||groupname.trim().equals("")){
											news="有人退出您所在的对讲组";
										}else{
											news="有人退出您所在的对讲组:"+groupname;
										}
									}else{
										if(groupname==null||groupname.trim().equals("")){
											news=name+"退出您所在的对讲组";
										}else{
											news=name+"退出您所在的对讲组:"+groupname;
										}
									}

									setNewMessageNotification(context, news, "我听");
									add("Gb5", groupurl, news, "组信息", String.valueOf(System.currentTimeMillis()));
								} catch (Exception e) {
									Log.e("消息接收服务中Gb5的异常", e.toString());
								}
							}else if(command2==6){
								//当组被管理员解散后，发送此消息。
								String news = null;
								String groupname = null;
								String groupurl = null;
								MapContent data = (MapContent) message.getMsgContent();
								String groupid = data.get("GroupId")+"";

								if(groupid!=null&&!groupid.trim().equals("")&&GlobalConfig.list_group!=null&&GlobalConfig.list_group.size()>0){
									for(int i=0;i<GlobalConfig.list_group.size();i++){
										if(GlobalConfig.list_group.get(i).getGroupId().equals(groupid)){
											groupname=GlobalConfig.list_group.get(i).getGroupName();
											groupurl=GlobalConfig.list_group.get(i).getGroupImg();
										}
									}
								}

								if(groupname==null||groupname.trim().equals("")){
									news="有一个您所在的对讲组被解散";
								}else{
									news="您所在的对讲组:"+groupname+"被群主解散";
								}
								//加入数据库
								setNewMessageNotification(context, news, "我听");
								add("Gb6", groupurl, news, "组信息", String.valueOf(System.currentTimeMillis()));
								//刷新通讯录
								Intent pushintent=new Intent("push_refreshlinkman");
								context. sendBroadcast(pushintent);
							}else if(command2==7){
								//当管理员把某组的权限移交给另一个人时，向组内所有成员发送新管理员Id。
								String news = null;
								String groupid = null;
								String groupname = null;
								String groupurl = null;
								try {
									MapContent data = (MapContent) message.getMsgContent();
									Map<String, Object> map = data.getContentMap();
									String msg = new Gson ().toJson(map);
									JSONTokener jsonParser = new JSONTokener(msg);
									JSONObject arg1 = (JSONObject) jsonParser.nextValue();
									String grouiInfo= arg1.getString("GroupInfo");
									String newadminInfo= arg1.getString("NewAdminInfo");

									GroupInfo groupinfo    = new Gson().fromJson(grouiInfo, new TypeToken<GroupInfo>() {}.getType());
									UserInfo userinfo    = new Gson().fromJson(newadminInfo, new TypeToken<UserInfo>() {}.getType());
									groupid =  groupinfo.getGroupId();

									if(groupid!=null&&!groupid.trim().equals("")&&GlobalConfig.list_group!=null&&GlobalConfig.list_group.size()>0){
										for(int i=0;i<GlobalConfig.list_group.size();i++){
											if(GlobalConfig.list_group.get(i).getGroupId().equals(groupid)){
												groupname=GlobalConfig.list_group.get(i).getGroupName();
												groupurl=GlobalConfig.list_group.get(i).getGroupImg();
											}
										}
									}
									String name = userinfo.getUserName();
									String userid = userinfo.getUserId();

									if(name==null||name.trim().equals("")){
										if(groupname==null||groupname.trim().equals("")){
											news="有一个您所在的对讲组的管理权限移交了";
										}else{
											news=groupname+"的管理权限移交了";
										}
									}else{
										if(groupname==null||groupname.trim().equals("")){
											news="有一个您所在的对讲组的管理权限移交给"+name;
										}else{
											news=groupname+"的管理权限移交给"+name;
										}
									}
									//加入数据库
									setNewMessageNotification(context, news, "我听");
									add("Gb7", groupurl, news, "组信息", String.valueOf(System.currentTimeMillis()));

									//如果管理员权限移交给自己，则需要刷新通讯录
									if(userid!=null&&!userid.equals("")&&CommonUtils.getUserId(context)!=null&&
											CommonUtils.getUserId(context).equals(userid)){
										Intent pushintent=new Intent("push_refreshlinkman");
										context. sendBroadcast(pushintent);
									}
								} catch (Exception e) {
									Log.e("消息接收服务中Gb7的异常", e.toString());
								}
							}else if(command2==8){
								//当管理员审核某个邀请后，只有当拒绝时，才把审核的消息发给邀请者。
								String news = null;
								String groupid = null;
								String groupname = null;
								String groupurl = null;
								try {
									MapContent data = (MapContent) message.getMsgContent();
									Map<String, Object> map = data.getContentMap();
									String msg = new Gson ().toJson(map);
									JSONTokener jsonParser = new JSONTokener(msg);
									JSONObject arg1 = (JSONObject) jsonParser.nextValue();
									String grouiInfo= arg1.getString("GroupInfo");
									//									String inviteuserinfos= arg1.getString("InviteUserInfo");
									String beinviteuserinfos= arg1.getString("BeInvitedUserInfo");
									String dealtime = data.get("InviteTime")+"";

									//									UserInfo inviteuserinfo    = new Gson().fromJson(inviteuserinfos, new TypeToken<UserInfo>() {}.getType());
									UserInfo beinviteuserinfo    = new Gson().fromJson(beinviteuserinfos, new TypeToken<UserInfo>() {}.getType());

									GroupInfo userinfo    = new Gson().fromJson(grouiInfo, new TypeToken<GroupInfo>() {}.getType());
									groupid =  userinfo.getGroupId();

									if(groupid!=null&&!groupid.trim().equals("")&&GlobalConfig.list_group!=null&&GlobalConfig.list_group.size()>0){
										for(int i=0;i<GlobalConfig.list_group.size();i++){
											if(GlobalConfig.list_group.get(i).getGroupId().equals(groupid)){
												groupname=GlobalConfig.list_group.get(i).getGroupName();
												groupurl=GlobalConfig.list_group.get(i).getGroupImg();
											}
										}
									}
									String name = beinviteuserinfo.getLoginName();

									if(name==null||name.trim().equals("")){
										if(groupname==null||groupname.trim().equals("")){
											news="有人退出您所在的对讲组";
										}else{
											news="您邀请别人进入"+groupname+"的请求被群主拒绝";
										}
									}else{
										if(groupname==null||groupname.trim().equals("")){
											news="您邀请"+name+"进入对讲组的请求被群主拒绝";
										}else{
											news="您邀请"+name+"进入"+groupname+"的请求被群主拒绝";
										}
									}
									//加入数据库
									setNewMessageNotification(context, news, "我听");
									add("Gb8", groupurl, news, "组信息", dealtime);
								} catch (Exception e) {
									Log.e("消息接收服务中Gb8的异常", e.toString());
								}
							}else if(command2==9){
								//当组管理员修改组信息后，向组内所有成员发送更新消息。
								String news = null;
								String groupid = null;
								String groupname = null;
								String groupurl = null;
								try {
									MapContent data = (MapContent) message.getMsgContent();
									Map<String, Object> map = data.getContentMap();
									String msg = new Gson ().toJson(map);
									JSONTokener jsonParser = new JSONTokener(msg); 
									JSONObject arg1 = (JSONObject) jsonParser.nextValue();
									String grouiInfo= arg1.getString("GroupInfo");

									GroupInfo userinfo    = new Gson().fromJson(grouiInfo, new TypeToken<GroupInfo>() {}.getType());
									groupid =  userinfo.getGroupId();

									if(groupid!=null&&!groupid.trim().equals("")&&GlobalConfig.list_group!=null&&GlobalConfig.list_group.size()>0){
										for(int i=0;i<GlobalConfig.list_group.size();i++){
											if(GlobalConfig.list_group.get(i).getGroupId().equals(groupid)){
												groupname=GlobalConfig.list_group.get(i).getGroupName();
												groupurl=GlobalConfig.list_group.get(i).getGroupImg();
											}
										}
									}

									if(groupname==null||groupname.trim().equals("")){
										news="有一个您所在的对讲组修改了组信息";
									}else{
										news="您所在的:"+groupname+"修改了组信息";
									}
									//加入数据库
									setNewMessageNotification(context, news, "我听");
									add("Gb9", groupurl, news, "组信息", String.valueOf(System.currentTimeMillis()));
									//刷新通讯录
									Intent pushintent=new Intent("push_refreshlinkman");
									context. sendBroadcast(pushintent);
								} catch (Exception e) {
									Log.e("消息接收服务中Gb9的异常", e.toString());
								}
							}
							break;
						default:
							break;
						}
					}
					//如果此时消息中心的界面在打开状态，则发送广播刷新消息中心界面
					Intent pushnews=new Intent("push_refreshnews");
					context. sendBroadcast(pushnews);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

//	private void setNewMessageNotification(Context ctx, String msg,String title) {
//		String ns = Context.NOTIFICATION_SERVICE;
//		NotificationManager nm = (NotificationManager) ctx.getSystemService(ns);
//		Intent pushaintent=new Intent("pushnnn");
//		Notification notification = new Notification(R.mipmap.app_logo, msg,System.currentTimeMillis());
//		notification.sound = null;
//		String ringNotify = "content://settings/system/notification_sound";
//		Uri soundUri = TextUtils.isEmpty(ringNotify) ? null : Uri.parse(ringNotify);
//		notification.audioStreamType = AudioManager.STREAM_RING; // tried
//		notification.sound = soundUri;
//		long[] vibrate = new long[] { 1000, 1000 };
//		notification.vibrate = vibrate;
//		notification.defaults=Notification.DEFAULT_LIGHTS;//点亮屏幕
//		PendingIntent in = PendingIntent.getBroadcast(ctx, 2, pushaintent,PendingIntent.FLAG_UPDATE_CURRENT);
//		notification.flags = notification.flags | Notification.FLAG_AUTO_CANCEL;
//		notification.setLatestEventInfo(ctx,title , msg, in);
//		nm.notify(0, notification);
//	}

	private void setNewMessageNotification(Context mContext, String message, String title){
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent pushIntent = new Intent("pushnnn");
//        Intent pushIntent = new Intent(mContext, NotifyNewActivity.class);
//        PendingIntent in = PendingIntent.getActivity(mContext, 0, pushIntent, 0);
		PendingIntent in = PendingIntent.getBroadcast(mContext, 2, pushIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
		mBuilder.setContentTitle(title)// 设置通知栏标题
				.setContentText(message)// 设置通知栏显示内容
				.setContentIntent(in)// 设置通知栏点击意图
				.setWhen(System.currentTimeMillis())// 通知产生时间
				.setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
				.setAutoCancel(true)// 设置点击通知消息时通知栏的通知自动消失
				.setDefaults(Notification.DEFAULT_VIBRATE)// 通知声音、闪灯和振动方式为使用当前的用户默认设置
				.setSmallIcon(R.mipmap.app_logo);// 设置通知图标
		mNotificationManager.notify(0, mBuilder.build());
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(Receiver!=null){
			unregisterReceiver(Receiver);
			Receiver=null;
		}
	}

}