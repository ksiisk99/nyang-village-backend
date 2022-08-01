package com.ay.talk.dto;

import java.io.Serializable;
import java.util.ArrayList;

import io.swagger.annotations.ApiModelProperty;

//클라이언트에게 전송을 위한  방 정보
public class RoomInfo implements Serializable{
	@ApiModelProperty(example = "과목명")
	private String roomName; //과목이름
	@ApiModelProperty(example = "방번호")
  private int roomId; //과목인덱스 서버에서 채팅방 인덱스에 해당함
	@ApiModelProperty(example = "랜덤닉네임")
  private String nickName; //채팅방 안에서 내 랜덤닉네임
	@ApiModelProperty(example = "교수님성함")
  private String professorName; //교수이름
	@ApiModelProperty(example = "방안에 있는 사용자들 닉네임")
  private ArrayList<String> roomInNames; //채팅방 안에 있는 유저 닉네임
    
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