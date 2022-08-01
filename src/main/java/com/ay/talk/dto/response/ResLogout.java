package com.ay.talk.dto.response;

import io.swagger.annotations.ApiModelProperty;

public class ResLogout {
	@ApiModelProperty(example = "로그아웃 시 신호 => 0:실패 / 1:성공")
	private int signal;
	
	public ResLogout() {}
	public ResLogout(int signal) {
		this.signal=signal;
	}
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
}
