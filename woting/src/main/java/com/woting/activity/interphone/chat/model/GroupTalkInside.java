package com.woting.activity.interphone.chat.model;

import java.io.Serializable;

public class GroupTalkInside implements Serializable{
	private String UserId;
	private String UserName;
	private int Type=1;
	private String RealName;		//实名
	private String UserNum;			//用户码
	private String PhoneNum;		//用户主手机号
	private String Email;			//
	private String Descn;			//
	private String PortraitBig;		//
	private String PortraitMini;	//
	private String UserAliasName;
	private int OnLine=1;			//该成员是否在线
	
	public int getOnLine() {
		return OnLine;
	}
	public void setOnLine(int onLine) {
		OnLine = onLine;
	}
	public String getUserAliasName() {
		return UserAliasName;
	}
	public void setUserAliasName(String userAliasName) {
		UserAliasName = userAliasName;
	}
	public String getPortraitBig() {
		return PortraitBig;
	}
	public void setPortraitBig(String portraitBig) {
		PortraitBig = portraitBig;
	}
	public String getPortraitMini() {
		return PortraitMini;
	}
	public void setPortraitMini(String portraitMini) {
		PortraitMini = portraitMini;
	}
	public String getRealName() {
		return RealName;
	}
	public void setRealName(String realName) {
		RealName = realName;
	}
	public String getUserNum() {
		return UserNum;
	}
	public void setUserNum(String userNum) {
		UserNum = userNum;
	}
	public String getPhoneNum() {
		return PhoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		PhoneNum = phoneNum;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getDescn() {
		return Descn;
	}
	public void setDescn(String descn) {
		Descn = descn;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
}
