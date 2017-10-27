package com.hafele.util;

import com.google.gson.Gson;
import com.hafele.bean.Message;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��19�� ����2:39:51
* JSON
*/
public class JsonUtil {
	/** ������ת��ΪJson�ַ� */
	public static String transToJson(Message message) {
		Gson gson = new Gson();
		String text = gson.toJson(message);
		return text;
	}

	/** ��Json�ַ�ת��Ϊ���� */
	public static Message transToBean(String text) {
		Gson gson = new Gson();
		Message message = gson.fromJson(text, Message.class);
		return message;
	}
}
