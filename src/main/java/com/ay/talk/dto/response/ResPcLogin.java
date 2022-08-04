package com.ay.talk.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.ay.talk.dto.RoomInfo;
import com.ay.talk.jpaentity.Authority;

import io.swagger.annotations.ApiModelProperty;

public class ResPcLogin {
	@ApiModelProperty(example = "로그인 시 신호 => 1:로그인성공 / 2:모바일로그인요구 / 3:정지회원 / 4:잘못입력")
	private int signal;
	@ApiModelProperty(example = "signal이 3이면 정지기간 아니면 NULL")
	private String suspendedDate;
	@ApiModelProperty(example = "유저 권한(Customer or Manager)")
	private Authority authority;
	@ApiModelProperty(example = "채팅방 목록")
	private List<RoomInfo> roomInfos;
	@ApiModelProperty(example = "jwt")
	private String jwt;
	
	public ResPcLogin() {}
	
	public ResPcLogin(int signal, String suspendedDate, Authority authority, List<RoomInfo> roomInfos, String jwt) {
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

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
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
	public List<RoomInfo> getRoomInfos() {
		return roomInfos;
	}
	public void setRoomInfos(List<RoomInfo> roomInfos) {
		this.roomInfos = roomInfos;
	}
	
}
