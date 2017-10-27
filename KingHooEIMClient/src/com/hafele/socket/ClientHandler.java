package com.hafele.socket;

import java.awt.SystemTray;
import java.nio.charset.Charset;

import com.hafele.bean.Category;
import com.hafele.bean.Message;
import com.hafele.ui.common.CategoryNode;
import com.hafele.ui.common.CustomOptionPane;
import com.hafele.ui.frame.MainWindow;
import com.hafele.util.Constants;
import com.hafele.util.JsonUtil;
import com.hafele.util.PictureUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月19日 下午2:58:15
* 类说明
*/
public class ClientHandler implements ChannelInboundHandler {

	private Client client;
	
	public ClientHandler(Client client) {
		this.client = client;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Client---handlerAdded");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Client---handlerRemoved");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		client.setChannel(ctx.channel());
		System.out.println(ctx.channel().localAddress() + "成功连接上了"
				+ ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(ctx.channel().remoteAddress() + "服务器挂了！");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		String msgStr = ((ByteBuf)msg).toString(Charset.defaultCharset());
		System.err.println("客户端接收到的消息：" + msgStr.getBytes().length + msgStr);
		Message message = JsonUtil.transToBean(msgStr);
		// 不同类型消息不同处理
		// 回文
		if (null != message && Constants.PALIND_MSG.equals(message.getType())) {
			//登录
			if(Constants.LOGIN_MSG.equals(message.getPalindType())) {
				if(message.getUser() != null) {
					System.err.println("开始搭建主窗体");
					// 销毁窗体
					client.getLogin().dispose();
					// 移除托盘图标
					SystemTray.getSystemTray().remove(client.getIcon());
					client.setUser(message.getUser());
					client.setCategoryList(message.getCategoryList());
					client.setCategoryMemberList(message.getCategoryMemberList());
					// 初始化主界面
					MainWindow mainWindow = MainWindow.getInstance(client);
					client.setMainWindow(mainWindow);
				} else {
					CustomOptionPane.showMessageDialog(client.getLogin(), message.getContent(), "友情提示");
				}
			}
		}
		//添加群组
		if (Constants.ADD_USER_CATE_MSG.equals(message.getPalindType())) {
			Category category = message.getCategory();
			CategoryNode categoryNode = new CategoryNode(PictureUtil.getPicture("arrow_left.png"), category);
			client.getBuddyRoot().add(categoryNode);
			client.getBuddyModel().reload();
			client.cateNodeMap.put(category.getId(), categoryNode);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.channel().flush();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Client---channelRegistered");
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Client---channelUnregistered");
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(ctx.channel().isWritable());
		System.out.println("我已掉线，如何继续说话？");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("异常了哦！");
		cause.printStackTrace();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Client---userEventTriggered");
	}

}
