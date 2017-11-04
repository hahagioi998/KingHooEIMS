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
* @version ����ʱ�䣺2017��10��19�� ����2:58:15
* ��˵��
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
		System.out.println(ctx.channel().localAddress() + "�ɹ���������"
				+ ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(ctx.channel().remoteAddress() + "���������ˣ�");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		String msgStr = ((ByteBuf)msg).toString(Charset.defaultCharset());
		System.err.println("�ͻ��˽��յ�����Ϣ��" + msgStr.getBytes().length + msgStr);
		Message message = JsonUtil.transToBean(msgStr);
		// ��ͬ������Ϣ��ͬ����
		// ����
		if (null != message && Constants.PALIND_MSG.equals(message.getType())) {
			//��¼
			if(Constants.LOGIN_MSG.equals(message.getPalindType())) {
				if(message.getUser() != null) {
					System.err.println("��ʼ�������");
					// ���ٴ���
					client.getLogin().dispose();
					// �Ƴ�����ͼ��
					SystemTray.getSystemTray().remove(client.getIcon());
					client.setUser(message.getUser());
					client.setCategoryList(message.getCategoryList());
					client.setCategoryMemberList(message.getCategoryMemberList());
					// ��ʼ��������
					MainWindow mainWindow = MainWindow.getInstance(client);
					client.setMainWindow(mainWindow);
				} else {
					CustomOptionPanel.showMessageDialog(client.getLogin(), message.getContent(), "������ʾ");
				}
			}
			//������Ӻ���
			if(Constants.REQUEST_ADD_MSG.equals(message.getPalindType())) {
				if(Constants.SUCCESS.equals(message.getStatus())) {
					if(client.getAddContactsWindow() != null) {
						CustomOptionPanel.showMessageDialog(client.getAddContactsWindow(), message.getSenderName()+"ͬ�������ĺ�������", "��ʾ");
						client.getAddContactsWindow().dispose();
						client.setAddContactsWindow(null);
					} else {
						CustomOptionPanel.showMessageDialog(client.getMainWindow(), message.getSenderName()+"ͬ�������ĺ�������", "��ʾ");
					}
					//�����ݸ��µ�����
					client.setUser(message.getUser());
					client.setCategoryList(message.getCategoryList());
					client.setCategoryMemberList(message.getCategoryMemberList());
					//ˢ��tree
					CategoryNode categoryNode = client.cateNodeMap.get(message.getContent());
					ContactsNode contactsNode = new ContactsNode(PictureUtil.getPicture(message.getUser().getHeadPicture()+"_40px.png"), message.getUser());
					categoryNode.add(contactsNode);
					client.getBuddyModel().reload(client.getBuddyRoot());
					client.buddyNodeMap.put(message.getUser().getName(), contactsNode);
				} else {
					CustomOptionPanel.showMessageDialog(client.getAddContactsWindow(), message.getContent(), "��ʾ");
					if(!Constants.FAILURE.equals(message.getStatus())) {
						if(client.getAddContactsWindow() != null) {
							client.getAddContactsWindow().dispose();
							client.setAddContactsWindow(null);
						}
					}
				}
			}
			//��Ӧ��Ӻ���
			if(Constants.ECHO_ADD_MSG.equals(message.getPalindType())) {
				if(Constants.SUCCESS.equals(message.getStatus())) {
					//ˢ��Tree
					CategoryNode categoryNode = client.cateNodeMap.get(message.getContent());
					ContactsNode contactsNode = new ContactsNode(PictureUtil.getPicture(message.getUser().getHeadPicture()+"_40px.png"), message.getUser());
					categoryNode.add(contactsNode);
					client.getBuddyModel().reload(client.getBuddyRoot());
					client.buddyNodeMap.put(message.getUser().getName(), contactsNode);
				}
				if(Constants.FAILURE.equals(message.getStatus())) {
					CustomOptionPanel.showMessageDialog(client.getMainWindow(), message.getContent(), "��ʾ");
				}
			}
			//��ӷ���
			if (Constants.ADD_USER_CATE_MSG.equals(message.getPalindType())) {
				Category category = message.getCategory();
				CategoryNode categoryNode = new CategoryNode(PictureUtil.getPicture("arrow_left.png"), category);
				client.getBuddyRoot().add(categoryNode);
				client.getBuddyModel().reload();
				client.cateNodeMap.put(category.getId(), categoryNode);
			}
			//����������
			if(Constants.EDIT_USER_CATE_MSG.equals(message.getPalindType())) {
				Category category = message.getCategory();
				CategoryNode categoryNode = client.cateNodeMap.get(category.getId());
				categoryNode.category = category;
				categoryNode.categoryName.setText(category.getGroupName());
			}
			//ɾ������
			if(Constants.DELETE_USER_CATE_MSG.equals(message.getPalindType())) {
				CategoryNode categoryNode = client.cateNodeMap.get(message.getContent());
				client.getBuddyRoot().remove(categoryNode);
				client.getBuddyModel().reload();//��Ҫ�Ӹ��ڵ�ˢ��
				client.cateNodeMap.remove(message.getContent());//��ռ�¼
			}
			//ɾ����Ա
			if(Constants.DELETE_USER_MEMBER_MSG.equals(message.getPalindType())) {
				//ˢ��tree
				ContactsNode contactsNode = client.buddyNodeMap.get(message.getContent());
				CategoryNode categoryNode = (CategoryNode) contactsNode.getParent();
				categoryNode.remove(contactsNode);
				client.getBuddyModel().reload(categoryNode);
				client.buddyNodeMap.remove(message.getContent());//��ռ�¼
			}
		}
		//������Ӻ���
		if(message != null && Constants.REQUEST_ADD_MSG.equals(message.getType())) {
			int result = CustomOptionPanel.showConfirmDialog(client.getMainWindow(), "��Ӻ�������", message.getSenderName() + "�������Ϊ����", "agreeButton", "refuseButton");
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
		System.out.println("���ѵ��ߣ���μ���˵����");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("�쳣��Ŷ��");
		cause.printStackTrace();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Client---userEventTriggered");
	}

}
