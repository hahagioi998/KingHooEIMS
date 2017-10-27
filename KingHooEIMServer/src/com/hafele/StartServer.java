package com.hafele;

import com.hafele.socket.Server;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月19日 下午3:55:47
* 启动服务
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
