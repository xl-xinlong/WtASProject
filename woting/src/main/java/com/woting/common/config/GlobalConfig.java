package com.woting.common.config;

import com.woting.activity.home.player.main.model.LanguageSearchInside;
import com.woting.activity.interphone.linkman.model.TalkGroupInside;
import com.woting.activity.interphone.linkman.model.TalkPersonInside;

import java.util.List;

public class GlobalConfig {

	/***三方信息*/
	//微信
	public static final String WEIXIN_KEY="wx99e28b6b8ed44a60";
	public static final String WEIXIN_SECRET="a579a0cfe0755b87eb17442e81254857";
	//QQ
	public static final String QQ_KEY="1105341370";
	public static final String QQ_SECRET="Hi2ccDP2eAfvjg1E";
	//新浪微博                                                                                                        
	public static final String WEIBO_KEY="2633057288";
	public static final String WEIBO_SECRET="95a5b41d6818cecffab50b2f1347fef8";
	//定位信息保存
	public static String longitude;
	public static String latitude;
	public static String CityName;	
	public static String AdCode;
	// 网络情况 1为成功WiFi已连接，2为cmnet，3为cmwap，4为ctwap， -1为网络未连接
	public static final int NETWORK_STATE_IDLE = -1;
	public static final int NETWORK_STATE_WIFI = 1;
	public static final int NETWORK_STATE_CMNET = 2;
	public static final int NETWORK_STATE_CMWAP = 3;
	public static final int NETWORK_STATE_CTWAP = 4;
	public static int CURRENT_NETWORK_STATE_TYPE = NETWORK_STATE_IDLE;
	//volley请求超时 时间
	public static final int  HTTP_CONNECTION_TIMEOUT = 60 * 1000;
	public static List<TalkGroupInside> list_group;
	public static List<TalkPersonInside> list_person;
	public static LanguageSearchInside playerobject;//播放器播放对象
	/** 数据库版本号 */
	public static final int dbversoncode = 32;
	/** 是否活跃状态，有活跃状态才能播放声音，否则即使收到音频包也不播放*/
	public static boolean isactive = false;
	/** 是否吐司*/
	public static boolean istusi = false;
	/**PersonClientDevice(个人客户端设备) 终端类型1=app,2=设备，3=pc*/
	public static int PCDType = 1;
	/** socket请求端口 */
	public static final int socketport = 15678;
	/** socket请求ip */
//	public static final String socketUrl = "182.92.175.134";//生产服务器地址
		public static final String socketUrl = "123.56.254.75";//测试服务器地址
//			public static final String socketUrl = "192.168.1.3";//
	/** http请求总url */
//	public static final String baseUrl = "http://182.92.175.134:808/";//生产服务器地址
	public static final String baseUrl = "http://123.56.254.75:808/";//测试服务器地址
//		public static final String baseUrl = "http://192.168.1.3:808/";//

	/** image请求路径前缀 */
	public static final String imageurl = baseUrl + "wt/";// 服务器

	/** apk下载默认路径*/
	public static  String apkUrl = "http://182.92.175.134/download/WoTing.apk";
	/**
	 * 公共部分
	 */
	// 启动页登录 应用入口
	public static final String splashUrl = baseUrl + "wt/common/entryApp.do?";
	// 获得版本信息
	//	public static final String VersionUrl = baseUrl+ "wt/common/getVersion.do?";
	// 获得版本信息
	public static final String VersionUrl = baseUrl+ "wt/common/judgeVersion.do?";
	// 注册
	public static final String registerUrl = baseUrl+ "wt/passport/user/register.do?";
	// 登录
	public static final String loginUrl = baseUrl+ "wt/passport/user/mlogin.do?";
	// 注销
	public static final String logoutUrl = baseUrl+ "wt/passport/user/mlogout.do?";

	// 上传头像
	// public static final String uploadtxUrl = baseUrl+
	// "wt/common/uploadImg.do?";
	// 修改密码
	public static final String modifyPasswordUrl = baseUrl+ "wt/passport/user/updatePwd.do?";
	// 找回密码
	// public static final String retrievePasswordUrl = baseUrl+
	// "wt//passport/user/retrievePwd.do?";
	// 账号绑定
	public static final String bindExtUserUrl = baseUrl+ "wt/passport/user/bindExtUserInfo.do?";
	// 帮助
	public static final String wthelpUrl = baseUrl + "wt/wtHelp.html";
	// 意见反馈
	public static final String FeedBackUrl = baseUrl+ "wt/opinion/app/commit.do";
	// 意见反馈历史记录
	public static final String FeedBackListUrl = baseUrl+ "wt/opinion/app/getList.do";
	/**
	 * 内容
	 */
	// 第一次请求
	public static final String mainPageUrl = baseUrl + "wt/mainPage.do?";
	// 语音搜索
	public static final String searchvoiceUrl = baseUrl+ "wt/searchByVoice.do?";
	// 电台首页展示展示
	// public static final String
	// BroadcastMainPage=baseUrl+"wt/broadcast/mainPage.do?";
	// 获取list？
	public static final String getListByCatalog = baseUrl+ "wt/content/getListByCatalog.do?";
	// 电台分类展示B
	public static final String getListByZoneUrl = baseUrl+ "wt/broadcast/getListByZone.do?";
	// 城市列表
	public static final String getZoneListUrl = baseUrl+ "wt/common/getZoneList.do?";
	// 热门搜索
	public static final String getHotSearch = baseUrl + "wt/getHotKeys.do";
	// 依照文字搜索
	// public static final String getSearchByText = baseUrl +
	// "wt/searchByText.do";
	// 按照声音搜索
	// public static final String getSearchByVoice = baseUrl+
	// "wt/searchByVoice.do";
	// 某子分类首页内容
	// public static final String getCatalogMainPageUrl = baseUrl+
	// "wt/mainPage.do?";
	// 分类首页
	public static final String BroadcastMainPage = baseUrl+ "wt/content/mainPage.do";
	// 图片banner
	public static final String getadvertUrl = baseUrl+ "wt/content/getLoopImgs.do";
	// 获取系列节目
	public static final String getSequUrl = baseUrl+ "wt/content/getSeqMaInfo.do";
	// 获取历史记录 此处不对 需要后台工作完成后修改
	// public static final String playHistoryUrl = baseUrl+
	// "wt/passport/uploadImg.do?";
	/**
	 * 对讲
	 */
	// 对讲-创建对讲小组 统一建组参数 2016.1.21更新 未改名修改了原有接口
	public static final String talkgroupcreatUrl = baseUrl+ "wt/passport/group/buildGroup.do";
	// 获取联系人 此接口之前存在 被注释掉了 现在在添加好友进组时使用
	public static final String getfriendlist = baseUrl+ "wt/passport/friend/getList.do";
	// 获取创建对讲小组的联系人
	public static final String creattalkgroupUrl = baseUrl+ "wt/passport/friend/getList.do";
	// 对讲-创建对讲小组
	// public static final String talkgroupcreatUrl = baseUrl+
	// "wt/passport/group/num/createGroup.do?";
	// 对讲-小组列表
	public static final String talkgrouplistUrl = baseUrl+ "wt/passport/group/getGroupList.do?";
	// 对讲-小组联系人
	public static final String grouptalkUrl = baseUrl+ "wt/passport/group/getGroupMembers.do?";
	// 退出组
	public static final String ExitGroupurl = baseUrl+ "wt/passport/group/exitGroup.do?";
	// 通过口令加入用户组
	public static final String JoinGroupByNumUrl = baseUrl+ "wt/passport/group/num/joininGroup.do";
	// 获取联系人
	// public static final String
	// gettalkpersonsurl=baseUrl+"wt/passport/friend/getList.do";
	public static final String gettalkpersonsurl = baseUrl+ "wt/passport/getGroupsAndFriends.do";
	// 邀请我列表
	public static final String getInvitedMeListUrl = baseUrl+ "wt/passport/friend/getInvitedMeList.do?";
	// 拒绝或接受邀请
	public static final String InvitedDealUrl = baseUrl+ "wt/passport/friend/inviteDeal.do?";
	// 查询陌生人
	public static final String searchStrangerUrl = baseUrl+ "wt/passport/friend/searchStranger.do?";
	// 查找用户组
	public static final String searchStrangerGroupUrl = baseUrl+ "wt/passport/group/searchGroup.do?";
	// 邀请陌生人为好友
	public static final String sendInviteUrl = baseUrl+ "wt/passport/friend/invite.do";
	public static final String talkoldlistUrl = baseUrl+ "wt/passport/getHistoryUG.do?";
	// 2016/2.26新加入的接口
	// 邀请某人进入组
	public static final String sendInviteintoGroupUrl = baseUrl+ "wt/passport/group/groupInvite.do?";
	// 加入群（公开群 密码群）
	public static final String JoinGroupUrl = baseUrl+ "wt/passport/group/joinInGroup.do?";
	// 加入群(验证群)
	public static final String JoinGroupVertifyUrl = baseUrl+ "wt/passport/group/groupApply.do";
	// 获取邀请我的组的信息
	public static final String getInvitedMeGroupListUrl = baseUrl+ "wt/passport/group/getInviteMeList.do";
	// 处理组邀请请求
	public static final String InvitedGroupDealUrl = baseUrl+ "wt/passport/group/inviteDeal.do?";
	// 删除好友
	public static final String delFriendUrl = baseUrl+ "wt/passport/friend/delFriend.do?";
	// 修改好友别名
	public static final String updateFriendnewsUrl = baseUrl+ "wt/passport/friend/updateFriendInfo.do?";
	// 修改群成员别名
	public static final String updategroupFriendnewsUrl = baseUrl+ "wt/passport/group/updateGroupUser.do?";
	// 管理员踢出用户 当踢出用户只剩用户本人时 此群将解散
	public static final String KickOutMembersUrl = baseUrl+ "wt/passport/group/kickoutGroup.do";
	// 管理员权限移交
	public static final String changGroupAdminnerUrl = baseUrl+ "wt/passport/group/changGroupAdminner.do";
	// 修改群密码 没接口
	public static final String UpdateGroupPassWordUrl = baseUrl+ "wt/passport/group/updatePwd.do";
	// 加群消息
	public static final String GetApplyListUrl = baseUrl+ "wt/passport/group/getExistApplyUserGroupList.do";
	// 同意或者拒绝组申请
	public static final String DealGroupApplyUrl = baseUrl+ "wt/passport/group/applyDeal.do";
	// 审核消息
	public static final String JoinGroupListUrl = baseUrl+ "wt/passport/group/getApplyUserList.do";
	// 更改群信息
	public static final String UpdateGroupInfoUrl = baseUrl+ "wt/passport/group/updateGroup.do";
	// 接受申请或者拒绝申请
	public static final String applyDealUrl = baseUrl+ "wt/passport/group/applyDeal.do";
	// 获取审核列表
	public static final String checkVertifyUrl = baseUrl+ "wt/passport/group/getNeedCheckInviteUserGroupList.do";
	// 审核邀请
	public static final String checkDealUrl = baseUrl+ "wt/passport/group/checkDeal.do";
	// 依照文字搜索
	public static final String getSearchByText = baseUrl + "wt/searchByText.do";
	// 获取分类
	public static final String getCatalogUrl = baseUrl + "wt/getCatalogInfo.do";
	//内容主页获取统一接口
	public static final String getContentUrl= baseUrl + "wt/content/getContents.do";
	//根据contentID获取内容列表
	public static final String getContentById= baseUrl + "wt/content/getContentInfo.do";
	//搜索检索热词
	public static final String searchHotKeysUrl= baseUrl + "wt/searchHotKeys.do";
	//通过手机号码注册
	public static final String registerByPhoneNumUrl= baseUrl + "wt/passport/user/registerByPhoneNum.do";
	// 再发验证码 
	public static final String reSendPhoneCheckCodeNumUrl= baseUrl + "wt/passport/user/reSendPhoneCheckCode.do";
	// 验证验证码，并得到手机号所绑定的用户
	public static final String checkPhoneCheckCodeUrl= baseUrl + "wt/passport/user/checkPhoneCheckCode.do";	
	//通过手机号码找回用户
	public static final String retrieveByPhoneNumUrl= baseUrl + "wt/passport/user/retrieveByPhoneNum.do";
	//根据手机号返回值修改密码 
	public static final String updatePwd_AfterCheckPhoneOKUrl= baseUrl + "wt/passport/user/updatePwd_AfterCheckPhoneOK.do";
	//第三方认证
	public static final String afterThirdAuthUrl= baseUrl + "wt/passport/user/afterThirdAuth.do";
	//喜欢content/clickFavorite.do
	public static final String clickFavoriteUrl= baseUrl + "wt/content/clickFavorite.do";
	//获取路况数据
	public static final String getLKTTS= baseUrl + "wt/lkTTS.do";
	//获取favorite列表
	public static final String getFavoriteListUrl= baseUrl + "wt/content/getFavoriteList.do";
	//删除喜欢列表
	public static final String delFavoriteListUrl= baseUrl + "wt/content/delFavorites.do";
}
