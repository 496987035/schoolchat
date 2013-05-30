package com.schoolchat.activity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.schoolchat.client.Client;
import com.schoolchat.client.ClientInputThread;
import com.schoolchat.client.ClientOutputThread;
import com.schoolchat.client.MessageListener;
import com.schoolchat.model.TranObject;
import com.schoolchat.model.TranObjectType;
import com.schoolchat.model.User;
import com.schoolchat.util.Constant;
import com.schoolchat.util.SharePreferenceUtil;

public class GetMsgService extends Service{
	private MyApplication application;
	private Client client;
	private boolean isStart = false;// 是否与服务器连接上
	private SharePreferenceUtil util;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {// 在onCreate方法里面注册广播接收者
		// TODO Auto-generated method stub
		super.onCreate();
		application = (MyApplication) this.getApplicationContext();
		client = application.getClient();
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		isStart=client.start();
		util = new SharePreferenceUtil(getApplicationContext(),
				Constant.SAVE_USER);
		application.setClientStart(isStart);
		System.out.println("client start:"+isStart);
		if(isStart){
			ClientInputThread in = client.getClientInputThread();
			in.setMessageListener(new MessageListener() {

				public void Message(TranObject msg) {
					// TODO Auto-generated method stub
					Intent broadCast = new Intent();
					broadCast.setAction(Constant.ACTION);
					broadCast.putExtra(Constant.MSGKEY, msg);
					sendBroadcast(broadCast)
;				}
				
			});

		}
	}
	
	@Override
	// 在服务被摧毁时，做一些事情
	public void onDestroy() {
		super.onDestroy();
		if (isStart) {
			ClientOutputThread out = client.getClientOutputThread();
			TranObject<User> o = new TranObject<User>(TranObjectType.LOGOUT);
			User u = new User();
			u.setId(Integer.parseInt(util.getId()));
			o.setObject(u);
			out.setMsg(o);
			// 发送完之后，关闭client
			out.setStart(false);
			client.getClientInputThread().setStart(false);
		}
	}
}
