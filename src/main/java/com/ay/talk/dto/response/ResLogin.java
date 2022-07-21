package com.ay.talk.dto.response;

import java.util.ArrayList;
import com.ay.talk.dto.*;

import io.swagger.annotations.ApiModelProperty;


public class ResLogin{
	@ApiModelProperty(example = "로그인 시 신호 => 1:업데이트 / 2:강제로그아웃 / 3:첫로그인성공 / 4:수강정정or이중로그인 성공 "
			+ "/ 5:잘못입력 / 6:정지회원")
	private int signal;
	@ApiModelProperty(example = "signal 6이면 정지기간 아니면 NULL")
	private String suspendedDate;
	@ApiModelProperty(example = "채팅방 목록")
	private ArrayList<RoomInfo> roomInfos;
	@ApiModelProperty(example = "jwt")
	private String jwt;
	
	
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	public String getSuspendedDate(){
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
