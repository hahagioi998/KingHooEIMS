package com.hafele.socket;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��19�� ����3:37:54
* ��˵��
*/
public class ServerListener implements ChannelFutureListener {

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		// TODO Auto-generated method stub
		if (future.isSuccess()) {
			System.out.println("������������ɣ�");
		} else {
			System.out.println("����������ʧ�ܣ�");
		}
	}

}
