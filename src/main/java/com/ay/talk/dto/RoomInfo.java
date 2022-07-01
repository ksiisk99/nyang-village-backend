package com.ay.talk.dto;

import java.io.Serializable;
import java.util.ArrayList;

//Ŭ���̾�Ʈ���� ������ ����  �� ������ ���ذ�ü
public class RoomInfo implements Serializable{
	String roomName; //�����̸�
    int roomId; //�����ε��� �������� ä�ù� �ε����� �ش���
    String nickName; //ä�ù� �ȿ��� �� �����г���
    String professorName; //�����̸�
    ArrayList<String> roomInNames; //ä�ù� �ȿ� �ִ� ���� �г���
    
    public RoomInfo() {}
 
	public RoomInfo(String roomName, int roomId, String nickName, ArrayList<String> roomInNames) {
		super();
		this.roomName = roomName;
		this.roomId = roomId;
		this.nickName = nickName;
		this.roomInNames = roomInNames;
	}

	public RoomInfo(String roomName, int roomId, String nickName, String professorName, ArrayList<String> roomInNames) {
		super();
		this.roomName = roomName;
		this.roomId = roomId;
		this.nickName = nickName;
		this.professorName = professorName;
		this.roomInNames = roomInNames;
	}

	public String getProfessorName() {
		return professorName;
	}

	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}

	public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public ArrayList<String> getRoomInNames() {
        return roomInNames;
    }

    public void setRoomInNames(ArrayList<String> roomInNames) {
        this.roomInNames = roomInNames;
    }

}