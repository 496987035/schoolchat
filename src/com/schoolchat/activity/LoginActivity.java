package com.schoolchat.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.schoolchat.client.Client;
import com.schoolchat.client.ClientOutputThread;
import com.schoolchat.model.TranObject;
import com.schoolchat.model.TranObjectType;
import com.schoolchat.model.User;
import com.schoolchat.util.Constant;
import com.schoolchat.util.SharePreferenceUtil;

public class LoginActivity extends MyActivity implements OnClickListener {
	private Button bt_login;
	private Button bt_register;
	private EditText et_uid;
	private EditText et_pass;
	private MyApplication application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		init();
		bt_login.setOnClickListener(this);
		bt_register.setOnClickListener(this);

		application = (MyApplication) this.getApplicationContext();
	}

	public void init() {
		bt_login = (Button) findViewById(R.id.bt_login);
		bt_register = (Button) findViewById(R.id.bt_register);
		et_uid = (EditText) findViewById(R.id.l_uid);
		et_pass = (EditText) findViewById(R.id.l_pass);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login:
			login();

			break;
		case R.id.bt_register:
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	public void login() {
		String id = et_uid.getText().toString();
		String pass = et_pass.getText().toString();
		try {
			Client client = application.getClient();
			ClientOutputThread out = client.getClientOutputThread();

			TranObject<User> o = new TranObject<User>(TranObjectType.LOGIN);
			User u = new User();
			u.setId(Integer.parseInt(id));
			u.setPassword(pass);
			o.setObject(u);
			out.setMsg(o);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {// 在onResume方法里面先判断网络是否可用，再启动服务,这样在打开网络连接之后返回当前Activity时，会重新启动服务联网，
		super.onResume();
		if (isNetworkAvailable()) {
			Intent service = new Intent(this, GetMsgService.class);
			startService(service);
		} else {
			toast(this);
		}
	}

	/**
	 * 判断手机网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager mgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	private void toast(Context context) {
		new AlertDialog.Builder(context)
				.setTitle("温馨提示")
				.setMessage("亲！您的网络连接未打开哦")
				.setPositiveButton("前往打开",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS);
								startActivity(intent);
							}
						}).setNegativeButton("取消", null).create().show();
	}

	@Override
	public void getMessage(TranObject msg) {
		if (msg != null) {
			if (msg.getType().equals(TranObjectType.LOGIN)) {
				ArrayList list = (ArrayList) msg.getObject();
				
				SharePreferenceUtil util = new SharePreferenceUtil(
						LoginActivity.this, Constant.SAVE_USER);
				util.setId(et_uid.getText().toString());
				util.setPasswd(et_pass.getText().toString());
				
				
				Intent i = new Intent(LoginActivity.this, FriendActivity.class);
				i.putExtra(Constant.MSGKEY, list);
				startActivity(i);
				finish();
				Toast.makeText(getApplicationContext(), "登录成功", 0).show();
			}
		}
	}
	
	@Override
	public void onBackPressed() {// 捕获返回按键
		exitDialog(LoginActivity.this, "QQ提示", "亲！您真的要退出吗？");
	}

	/**
	 * 退出时的提示框
	 * 
	 * @param context
	 *            上下文对象
	 * @param title
	 *            标题
	 * @param msg
	 *            内容
	 */
	private void exitDialog(Context context, String title, String msg) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (application.isClientStart()) {// 如果连接还在，说明服务还在运行
							// 关闭服务
							Intent service = new Intent(LoginActivity.this,
									GetMsgService.class);
							stopService(service);
						}
						close();// 调用父类自定义的循环关闭方法
					}
				}).setNegativeButton("取消", null).create().show();
	}

}
