package com.ay.talk.dto.response;

import java.util.ArrayList;
import com.ay.talk.dto.*;

import io.swagger.annotations.ApiModelProperty;


public class ResLogin{
	@ApiModelProperty(example = "�α��� �� ��ȣ => 1:������Ʈ / 2:�����α׾ƿ� / 3:ù�α��μ��� / 4:��������or���߷α��� ���� "
			+ "/ 5:�߸��Է� / 6:����ȸ��")
	private int signal;
	@ApiModelProperty(example = "signal 6�̸� �����Ⱓ �ƴϸ� NULL")
	private String suspendedDate;
	@ApiModelProperty(example = "ä�ù� ���")
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
