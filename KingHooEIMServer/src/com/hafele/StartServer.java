package com.hafele;

import com.hafele.socket.Server;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��19�� ����3:55:47
* ��������
*/
public class StartServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Server.getInstance();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
