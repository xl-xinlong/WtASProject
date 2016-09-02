package com.woting.activity.interphone.linkman.model;

import java.io.Serializable;
import java.util.List;

public class TalkPerson implements Serializable{
	private String Type;
	private String PageSize;
	private String AllSize;
	private String Name;
	private List<TalkPersonInside> Friends;
	
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getPageSize() {
		return PageSize;
	}
	public void setPageSize(String pageSize) {
		PageSize = pageSize;
	}
	public String getAllSize() {
		return AllSize;
	}
	public void setAllSize(String allSize) {
		AllSize = allSize;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public List<TalkPersonInside> getFriends() {
		return Friends;
	}
	public void setFriends(List<TalkPersonInside> friends) {
		Friends = friends;
	}
}
