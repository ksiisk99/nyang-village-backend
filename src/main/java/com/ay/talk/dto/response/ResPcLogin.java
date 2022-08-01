package com.ay.talk.dto.response;

import java.util.ArrayList;

import com.ay.talk.dto.RoomInfo;

import io.swagger.annotations.ApiModelProperty;

public class ResPcLogin {
	@ApiModelProperty(example = "로그인 시 신호 => 1:로그인성공 / 2:모바일로그인요구 / 3:정지회원 / 4:잘못입력")
	private int signal;
	@ApiModelProperty(example = "signal이 3이면 정지기간 아니면 NULL")
	private String suspendedDate;
	@ApiModelProperty(example = "유저 권한(Customer , Manager")
	private ArrayList<String> authority;
	@ApiModelProperty(example = "채팅방 목록")
	private ArrayList<RoomInfo> roomInfos;
	@ApiModelProperty(example = "jwt")
	private String jwt;
	
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
