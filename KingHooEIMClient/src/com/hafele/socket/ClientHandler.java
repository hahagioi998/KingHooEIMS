package com.hafele.socket;

import java.awt.SystemTray;
import java.nio.charset.Charset;

import com.hafele.bean.Category;
import com.hafele.bean.Message;
import com.hafele.ui.common.CategoryNode;
import com.hafele.ui.common.CustomOptionPanel;
import com.hafele.ui.contacts.ContactsNode;
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
					CustomOptionPanel.showMessageDialog(client.getLogin(), message.getContent(), "友情提示");
				}
			}
			//请求添加好友
			if(Constants.REQUEST_ADD_MSG.equals(message.getPalindType())) {
				if(Constants.SUCCESS.equals(message.getStatus())) {
					if(client.getAddContactsWindow() != null) {
						CustomOptionPanel.showMessageDialog(client.getAddContactsWindow(), message.getSenderName()+"同意了您的好友请求！", "提示");
						client.getAddContactsWindow().dispose();
						client.setAddContactsWindow(null);
					} else {
						CustomOptionPanel.showMessageDialog(client.getMainWindow(), message.getSenderName()+"同意了您的好友请求！", "提示");
					}
					//将数据更新到最新
					client.setUser(message.getUser());
					client.setCategoryList(message.getCategoryList());
					client.setCategoryMemberList(message.getCategoryMemberList());
					//刷新tree
					CategoryNode categoryNode = client.cateNodeMap.get(message.getContent());
					ContactsNode contactsNode = new ContactsNode(PictureUtil.getPicture(message.getUser().getHeadPicture()+"_40px.png"), message.getUser());
					categoryNode.add(contactsNode);
					client.getBuddyModel().reload(client.getBuddyRoot());
					client.buddyNodeMap.put(message.getUser().getName(), contactsNode);
				} else {
					CustomOptionPanel.showMessageDialog(client.getAddContactsWindow(), message.getContent(), "提示");
					if(!Constants.FAILURE.equals(message.getStatus())) {
						if(client.getAddContactsWindow() != null) {
							client.getAddContactsWindow().dispose();
							client.setAddContactsWindow(null);
						}
					}
				}
			}
			//回应添加好友
			if(Constants.ECHO_ADD_MSG.equals(message.getPalindType())) {
				if(Constants.SUCCESS.equals(message.getStatus())) {
					//刷新Tree
					CategoryNode categoryNode = client.cateNodeMap.get(message.getContent());
					ContactsNode contactsNode = new ContactsNode(PictureUtil.getPicture(message.getUser().getHeadPicture()+"_40px.png"), message.getUser());
					categoryNode.add(contactsNode);
					client.getBuddyModel().reload(client.getBuddyRoot());
					client.buddyNodeMap.put(message.getUser().getName(), contactsNode);
				}
				if(Constants.FAILURE.equals(message.getStatus())) {
					CustomOptionPanel.showMessageDialog(client.getMainWindow(), message.getContent(), "提示");
				}
			}
			//添加分组
			if (Constants.ADD_USER_CATE_MSG.equals(message.getPalindType())) {
				Category category = message.getCategory();
				CategoryNode categoryNode = new CategoryNode(PictureUtil.getPicture("arrow_left.png"), category);
				client.getBuddyRoot().add(categoryNode);
				client.getBuddyModel().reload();
				client.cateNodeMap.put(category.getId(), categoryNode);
			}
			//重命名分组
			if(Constants.EDIT_USER_CATE_MSG.equals(message.getPalindType())) {
				Category category = message.getCategory();
				CategoryNode categoryNode = client.cateNodeMap.get(category.getId());
				categoryNode.category = category;
				categoryNode.categoryName.setText(category.getGroupName());
			}
			//删除分组
			if(Constants.DELETE_USER_CATE_MSG.equals(message.getPalindType())) {
				CategoryNode categoryNode = client.cateNodeMap.get(message.getContent());
				client.getBuddyRoot().remove(categoryNode);
				client.getBuddyModel().reload();//需要从根节点刷新
				client.cateNodeMap.remove(message.getContent());//清空记录
			}
			//删除成员
			if(Constants.DELETE_USER_MEMBER_MSG.equals(message.getPalindType())) {
				//刷新tree
				ContactsNode contactsNode = client.buddyNodeMap.get(message.getContent());
				CategoryNode categoryNode = (CategoryNode) contactsNode.getParent();
				categoryNode.remove(contactsNode);
				client.getBuddyModel().reload(categoryNode);
				client.buddyNodeMap.remove(message.getContent());//清空记录
			}
		}
		//请求添加好友
		if(message != null && Constants.REQUEST_ADD_MSG.equals(message.getType())) {
			int result = CustomOptionPanel.showConfirmDialog(client.getMainWindow(), "添加好友请求", message.getSenderName() + "想添加您为好友", "agreeButton", "refuseButton");
			Message backMsg = new Message();
			backMsg.setType(Constants.ECHO_ADD_MSG);
			backMsg.setSenderId(client.getUser().getLoginName());
			backMsg.setSenderName(client.getUser().getName());
			backMsg.setReceiverId(message.getSenderId());
			backMsg.setReceiverName(message.getSenderName());
			if(result == Constants.YES_OPTION) {
				backMsg.setContent(message.getContent() + Constants.LEFT_SLASH + Constants.YES);
			} else if(result == Constants.NO_OPTION) {
				backMsg.setContent(message.getContent() + Constants.LEFT_SLASH + Constants.NO);
			}
			client.sendMsg(backMsg);
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
