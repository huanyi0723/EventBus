package com.harvic.other;

public class SecondEvent{

	private String mMsg;
	public SecondEvent(String msg) {
		mMsg = "MainEvent:"+msg;
	}
	public String getMsg(){
		return mMsg;
	}
}
