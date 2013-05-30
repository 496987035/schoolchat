package com.schoolchat.activity;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.schoolchat.model.TranObject;
import com.schoolchat.model.TranObjectType;
import com.schoolchat.model.User;
import com.schoolchat.util.Constant;

public class RegisterActivity extends Activity implements OnClickListener {
	private Button bt_confirm;
	private EditText r_name;
	private EditText r_pass;
	private TextView tv_uid;
	private Socket socket=null;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		init();
		bt_confirm.setOnClickListener(this);
	}

	public void init() {
		bt_confirm = (Button) findViewById(R.id.bt_cf);
		r_name = (EditText) findViewById(R.id.r_name);
		r_pass = (EditText) findViewById(R.id.r_pass);
		tv_uid = (TextView) findViewById(R.id.tv_uid);
	}

	public void onClick(View v) {
		System.out.println(2);
		register();
	}

	private void register() {
		String name = r_name.getText().toString();
		String pass = r_pass.getText().toString();
		try {
			
			socket = new Socket(Constant.SERVER_IP,Constant.SERVER_PORT);
			oos = new ObjectOutputStream(socket.getOutputStream());
			User u = new User();
			u.setName(name);
			u.setPassword(pass);
			
			TranObject<User> o = new TranObject<User>(
					TranObjectType.REGISTER);
	
			o.setObject(u);
			oos.writeObject(o);
			oos.flush();
	
			ois =new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			
			Object obj = ois.readObject();
			if(obj!=null){
				TranObject msg=(TranObject) obj;
				User user=(User) msg.getObject();
				System.out.println("ÄãµÄidÎª£º"+user.getId());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				oos.close();
				ois.close();
				socket.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}


	}

}
