package com.woting.activity.interphone.commom.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.woting.activity.interphone.commom.message.Message;
import com.woting.activity.interphone.commom.message.MsgMedia;
import com.woting.activity.interphone.commom.message.MsgNormal;
import com.woting.activity.interphone.commom.message.content.MapContent;
import com.woting.common.config.GlobalConfig;
import com.woting.service.SocketService;
import com.woting.service.SubclassService;
import com.woting.util.CommonUtils;
import com.woting.util.PhoneMessage;
import com.woting.util.SequenceUUID;
/**
 * 对讲机数据组装
 * @author 辛龙
 *2016年8月3日
 */
public class InterPhoneControl   {
	public static String bdcallid;

	/**
	 * 退出小组
	 * @param context
	 * @param groupid
	 */
	public static  void Quit(Context context,String groupid) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(1);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(1);
		msg.setCommand(2);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("GroupId", groupid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);
	}

	/**
	 * 进入小组
	 * @param context
	 * @param groupid
	 */
	public static  void Enter(Context context,String groupid) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(1);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(1);
		msg.setCommand(1);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("GroupId", groupid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);
	}

	/**
	 * 取消说话
	 * @param context
	 * @param groupid
	 */
	public static  void Loosen(Context context,String groupid) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(1);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(2);
		msg.setCommand(2);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("GroupId", groupid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);
	}

	/**
	 * 请求说话
	 * @param context
	 * @param groupid
	 */
	public  static void Press(Context context,String groupid ) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(1);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(2);
		msg.setCommand(1);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("GroupId", groupid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);
	}

	/**
	 * 个人请求通话-----呼叫
	 * @param context
	 */
	public  static void PersonTalkPress(Context context,String id ) {
		String srcbdcallid = SequenceUUID.getUUIDSubSegment(0)+id;
		bdcallid=srcbdcallid.substring(0, 12);
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(2);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(1);
		msg.setCommand(1);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("CallederId", id);
		DataMap.put("CallId", bdcallid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);
	}
	///////////////////////////以下这个有问题
	/**
	 * 个人请求通话----------呼叫传递(Server->被叫App)-------应答消息
	 * @param context
	 */
	public  static void PersonTalkHJCDYD(Context context,String callid,String remsgidid,String callerId  ) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(1);
		msg.setReMsgId(remsgidid);
		msg.setReturnType(0x01);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(0);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(2);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(1);
		msg.setCommand(0x90);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("CallerId", callerId);
		DataMap.put("CallId", callid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);

		//		Map<String, Object> map = new HashMap<String, Object>();
		//		map.put("MsgId", SequenceUUID.getPureUUID());
		//		map.put("ReMsgId", remsgidid);
		//		map.put("MsgType", "-1");
		//		map.put("BizType", "CALL_CTL");
		//		map.put("IMEI", PhoneMessage.imei);
		//		map.put("UserId", Utils.getSocketUserId(context));
		//		map.put("CmdType", "CALL");
		//		map.put("SendTime",  System.currentTimeMillis());
		//		map.put("Command", "-b1");
		//		map.put("ReturnType", "1001");
		//		map.put("PCDType", "1");
		//		Map<String, Object> DataMap = new HashMap<String, Object>();
		//		DataMap.put("CallerId", callerId);
		//		DataMap.put("CallId", callid);
		//		map.put("Data", DataMap);
		//		pullToSocket(jsonEnclose(map).toString());
	}

	/**
	 * 个人开始通话------（获取说话权限）查看组内是否有人说话
	 * @param context
	 */
	public  static void PersonTalkPressStart(Context context,String id ) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(2);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(2);
		msg.setCommand(1);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("CallId", bdcallid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);
	}

	/**
	 * 个人结束通话------释放说话权限
	 * @param context
	 */
	public  static void PersonTalkPressStop(Context context,String id ) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(2);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(2);
		msg.setCommand(2);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("CallId", bdcallid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);
	}

	/**
	 * 个人请求通话----------接收应答
	 * @param context
	 */
	public  static void PersonTalkAllow(Context context,String callid ,String callerId) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(2);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(1);
		msg.setCommand(2);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("CallerId", callerId);
		DataMap.put("ACKType", "1");
		DataMap.put("CallId", callid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);
	}

	/**
	 * 个人请求通话----------拒绝应答
	 * @param context
	 */
	public  static void PersonTalkOver(Context context,String callid ,String callerId ) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(2);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(1);
		msg.setCommand(2);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("CallerId", callerId);
		DataMap.put("ACKType", "2");
		DataMap.put("CallId", callid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);
	}

	/**
	 * 个人请求通话----------应答超时
	 * @param context
	 */
	public  static void PersonTalkTimeOver(Context context,String callid ,String callerId ) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(2);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(1);
		msg.setCommand(2);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("CallerId", callerId);
		DataMap.put("ACKType", "31");
		DataMap.put("CallId", callid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);
	}

	/**
	 * 个人请求通话----------挂断电话
	 * @param context
	 */
	public  static void PersonTalkHangUp(Context context,String callid   ) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(2);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(1);
		msg.setCommand(3);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		Map<String, Object> DataMap = new HashMap<String, Object>();
		DataMap.put("CallId", callid);
		MapContent  map = new MapContent(DataMap);
		msg.setMsgContent(map);
		pullToSocket(msg);
	}

	/**
	 *socket 重连后发送的数据register
	 */
	public  static void sendEntryMessage(Context context) {
		MsgNormal msg = new MsgNormal();
		// 消息分类：MsgType 原始数据：1主动发出的消息；-1应答消息；	最新数据：0000主动发送；1000应答消息
		//16进制编码：0000=0；1000=8
		msg.setMsgType(0);
		//是否需要确认：NeedAffirm 原始数据：1需要确认，其他不需要确认 最新数据：0000不需要确认；1000需要确认
		// 16进制编码：0000=0；1000=8
		msg.setAffirm(1);
		//业务分类：BizType  最新数据：0000-应答消息；0001-组通话；0010-电话通话；0100-消息通知；1111-注册消息
		//16进制编码：0000=0；0001=1；0010=2；0100=4；1111=0x0f；
		msg.setBizType(0x0f);
		//  命令类型：CmdType   0001{1=group;2=ptt};   0010{1=call;2=ptt};       0100{1=user;2=group};
		msg.setCmdType(0);
		//消息源地址类型：FromType 最新数据：0000-设备；0001-服务器
		//16进制编码：0000=0；0001=1；
		msg.setFromType(0);
		//消息源地址类型：ToType  最新数据：0000-设备；0001-服务器
		// 16进制编码：0000=0；0001=1；
		msg.setToType(1);
		//消息源地址类型：PCDType  客户端型号  目前1=手机；2=设备；3=PC，默认1
		msg.setPCDType(GlobalConfig.PCDType);
		msg.setSendTime(System.currentTimeMillis());
		msg.setMsgId(SequenceUUID.getPureUUID());
		if(CommonUtils.getSocketUserId(context)!=null) msg.setUserId(CommonUtils.getSocketUserId(context));
		msg.setIMEI(PhoneMessage.imei);
		pullToSocket(msg);
	}
	
	/**
	 * 音频文件发送==把文件编码后传输出去
	 */
	public static void sendFile(byte[]  audio,int num,String type,String talkId,String groupId) {
		if(type!=null&&type.trim().equals("person")){
				MsgMedia msg = new MsgMedia();
				msg.setMsgType(0);
				msg.setAffirm(1);
				msg.setFromType(0);
				msg.setToType(1);
				msg.setBizType(2);
				msg.setMediaType(1);
				msg.setSendTime(System.currentTimeMillis());
				msg.setTalkId(talkId);
				msg.setSeqNo(num);
				msg.setObjId(bdcallid);
				msg.setMediaData(audio);
				pullToSocket(msg);
		}else{
				MsgMedia msg = new MsgMedia();
				msg.setMsgType(0);
				msg.setAffirm(1);
				msg.setFromType(0);
				msg.setToType(1);
				msg.setBizType(1);
				msg.setMediaType(1);
				msg.setSendTime(System.currentTimeMillis());
				msg.setTalkId(talkId);
				msg.setSeqNo(num);
				msg.setObjId(groupId);
				msg.setMediaData(audio);
				pullToSocket(msg);
		}
	}

	
	/**
	 *音频数据结束包=== 把最后一条数据发送出去
	 */
	public static void sendLastFile(int num,String type,String talkId,String groupId) {
		if(type!=null&&type.trim().equals("person")){
				MsgMedia msg = new MsgMedia();
				msg.setMsgType(0);
				msg.setAffirm(1);
				msg.setFromType(0);
				msg.setToType(1);
				msg.setBizType(2);
				msg.setMediaType(1);
				msg.setSendTime(System.currentTimeMillis());
				msg.setTalkId(talkId);
				msg.setSeqNo(num);
				msg.setObjId(bdcallid);
				pullToSocket(msg);
		}else{
				MsgMedia msg = new MsgMedia();
				msg.setMsgType(0);
				msg.setAffirm(1);
				msg.setFromType(0);
				msg.setToType(1);
				msg.setBizType(1);
				msg.setMediaType(1);
				msg.setSendTime(System.currentTimeMillis());
				msg.setTalkId(talkId);
				msg.setSeqNo(num);
				msg.setObjId(groupId);
				pullToSocket(msg);
		}
	}
	
	
	/**
	 * 数据传输到socket
	 * @param message
	 */
	public static   void pullToSocket(Message message) {
			SocketService.addSendMsg(message);
	}
}