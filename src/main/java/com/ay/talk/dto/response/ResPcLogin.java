package com.ay.talk.dto.response;

import java.util.ArrayList;

import com.ay.talk.dto.RoomInfo;

public class ResPcLogin {
	int signal;
	String suspendedDate;
	ArrayList<String> authority;
	ArrayList<RoomInfo> roomInfos;
	String jwt;
	
	public ResPcLogin() {}
	
	public ResPcLogin(int signal, String suspendedDate, ArrayList<String> authority, ArrayList<RoomInfo> roomInfos, String jwt) {
		super();
		this.signal = signal;
		this.suspendedDate = suspendedDate;
		this.authority=authority;
		this.roomInfos = roomInfos;
		this.jwt=jwt;
	}
	
	
	
	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public ArrayList<String> getAuthority() {
		return authority;
	}

	public void setAuthority(ArrayList<String> authority) {
		this.authority = authority;
	}

	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	public String getSuspendedDate() {
		return suspendedDate;
	}
	public void setSuspendedDate(String suspendedDate) {
		this.suspendedDate = suspendedDate;
	}
	public ArrayList<RoomInfo> getRoomInfos() {
		return roomInfos;
	}
	public void setRoomInfos(ArrayList<RoomInfo> roomInfos) {
		this.roomInfos = roomInfos;
	}
	
}
