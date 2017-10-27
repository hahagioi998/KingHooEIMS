package com.hafele;

import com.hafele.socket.Client;
import com.hafele.ui.frame.LoginWindow;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月17日 下午1:46:44
* 启动客户端
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
