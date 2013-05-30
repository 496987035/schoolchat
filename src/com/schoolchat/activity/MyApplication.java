package com.schoolchat.activity;

import android.app.Application;

import com.schoolchat.client.Client;
import com.schoolchat.util.Constant;

public class MyApplication extends Application {
	private Client client;// 客户端
	private boolean isClientStart;// 客户端连接是否启动

	@Override
	public void onCreate() {
		client = new Client(Constant.SERVER_IP,  Constant.SERVER_PORT);// 从配置文件中读ip和地址
		super.onCreate();
	}

	public Client getClient() {
		return client;
	}

	public boolean isClientStart() {
		return isClientStart;
	}

	public void setClientStart(boolean isClientStart) {
		this.isClientStart = isClientStart;
	}

}
