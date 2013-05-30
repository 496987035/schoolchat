package com.schoolchat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.schoolchat.model.TranObject;
import com.schoolchat.model.User;
import com.schoolchat.util.Constant;

public class FriendActivity extends MyActivity {
	private ListView lv;
	private List<Map<String, Object>> list ;
	private ArrayList<User> userList;
	private MyApplication application;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
		application = (MyApplication) this.getApplicationContext();
		
		
		userList=(ArrayList<User>)getIntent().getSerializableExtra(Constant.MSGKEY);	
        lv=(ListView) findViewById(R.id.listview);
        SimpleAdapter adapter = new SimpleAdapter(this, getData(),
                R.layout.activity_item, new String[] { "img", "name", "info" },
                new int[] { R.id.img, R.id.name, R.id.info });
         lv.setAdapter(adapter);
         lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
			System.out.println(list.get(position).get("name"));
				
			}
		});
         
	}
	 private List<Map<String, Object>> getData() {
         list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map ;
        for(User user:userList){
        	map= new HashMap<String, Object>();
            map.put("img", R.drawable.ic_launcher);
            map.put("name",user.getName());
            map.put("info", "啊");
           
            list.add(map);
        }
  

        return list;
    }
		@Override
		// 菜单选项添加事件处理
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.friend_menu_add:
				Toast.makeText(getApplicationContext(), "亲！此功能暂未实现哦", 0).show();
				break;
			case R.id.friend_menu_exit:
				exitDialog(FriendActivity.this, "提示", "亲！您真的要退出吗？");
				break;
			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}

	 
		// 完全退出提示窗
		private void exitDialog(Context context, String title, String msg) {
			new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// 关闭服务
							if (application.isClientStart()) {
								Intent service = new Intent(
										FriendActivity.this,
										GetMsgService.class);
								stopService(service);
							}
							close();// 父类关闭方法
						}
					}).setNegativeButton("取消", null).create().show();
		}
		@Override
		public void getMessage(TranObject msg) {
			// TODO Auto-generated method stub
			
		}



}
