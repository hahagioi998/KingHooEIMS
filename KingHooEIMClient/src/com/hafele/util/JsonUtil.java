package com.hafele.util;

import com.google.gson.Gson;
import com.hafele.bean.Message;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月19日 下午2:39:51
* JSON
*/
public class JsonUtil {
	/** 将对象转化为Json字符 */
	public static String transToJson(Message message) {
		Gson gson = new Gson();
		String text = gson.toJson(message);
		return text;
	}

	/** 将Json字符转化为对象 */
	public static Message transToBean(String text) {
		Gson gson = new Gson();
		Message message = gson.fromJson(text, Message.class);
		return message;
	}
}
