package com.hafele.socket;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月19日 下午3:37:54
* 类说明
*/
public class ServerListener implements ChannelFutureListener {

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		// TODO Auto-generated method stub
		if (future.isSuccess()) {
			System.out.println("服务器操作完成！");
		} else {
			System.out.println("服务器操作失败！");
		}
	}

}
