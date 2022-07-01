package com.ay.talk.dto.response;

import java.io.Serializable;
import java.util.ArrayList;

import com.ay.talk.dto.*;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class ResLogin{
	int signal;
	String suspendedDate;
	ArrayList<RoomInfo> roomInfos;
	String jwt;
	
	
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
