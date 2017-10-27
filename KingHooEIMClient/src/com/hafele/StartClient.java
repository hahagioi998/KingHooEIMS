package com.hafele;

import com.hafele.socket.Client;
import com.hafele.ui.frame.LoginWindow;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��17�� ����1:46:44
* �����ͻ���
*/
public class StartClient {
	public static void main(String[] args) {
		try {
			Client client = new Client();
			LoginWindow loginWindow = LoginWindow.getInstance(client);
			client.setLogin(loginWindow);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
