package com.schoolchat.client;

import com.schoolchat.model.TranObject;

/**
 * 消息监听接口
 * 
 * @author way
 * 
 */
public interface MessageListener {
	public void Message(TranObject msg);
}
