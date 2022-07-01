package com.ay.talk.dto.request;


public class ReqLogin {
	private int version; //�� ������Ʈ ����
    private String fcm; //���̾�̽� ��ū
    private String studentId; //�й�
    private String password; //��й�ȣ

    public ReqLogin() {}
    
	public ReqLogin(int version, String fcm, String studentId, String password) {
		super();
		this.version = version;
		this.fcm = fcm;
		this.studentId = studentId;
		this.password = password;
	}

	public int getVersion() {
		return version;
	}

	public String getFcm() {
		return fcm;
	}

	public String getStudentId() {
		return studentId;
	}


	public String getPassword() {
		return password;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setFcm(String fcm) {
		this.fcm = fcm;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
