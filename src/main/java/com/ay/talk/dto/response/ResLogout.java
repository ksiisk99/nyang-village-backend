package com.ay.talk.dto.response;

import io.swagger.annotations.ApiModelProperty;

public class ResLogout {
	@ApiModelProperty(example = "�α׾ƿ� �� ��ȣ => 0:���� / 1:����")
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
