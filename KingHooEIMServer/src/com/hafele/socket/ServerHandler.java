package com.hafele.socket;

import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hafele.bean.Category;
import com.hafele.bean.CategoryMember;
import com.hafele.bean.Message;
import com.hafele.bean.User;
import com.hafele.dao.CategoryDao;
import com.hafele.dao.CategoryMemberDao;
import com.hafele.dao.UserDao;
import com.hafele.util.Constants;
import com.hafele.util.JsonUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��19�� ����3:36:35
* ��˵��
*/
public class ServerHandler implements ChannelInboundHandler {

	private UserDao userDao;
	private CategoryDao categoryDao;
	private CategoryMemberDao categoryMemberDao;
	
	
	// ������ʵ����ֱ��ʹ��ip�洢��Ϊ�˷�ֹһ̨���Ե�¼����˺�
	/** key��SocketAddress��value���û�Id */
	private Map<SocketAddress, String> map = new HashMap<SocketAddress, String>();
	/** key���û�Id��value��channel */
	private Map<String, Channel> clientMap = new HashMap<String, Channel>();
	
	public ServerHandler(Map<SocketAddress, String> map,
			Map<String, Channel> clientMap) {
		this.map = map;
		this.clientMap = clientMap;
		
		userDao = new UserDao();
		categoryDao = new CategoryDao();
		categoryMemberDao = new CategoryMemberDao();
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Server---handlerAdded");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Server---handlerRemoved");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("�ͻ���" + ctx.channel().remoteAddress() + "����������ӳɹ���");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("�ͻ���" + ctx.channel().remoteAddress() + "�����ˣ�");
		clientMap.remove(map.get(ctx.channel().remoteAddress()));
		map.remove(ctx.channel().remoteAddress());
		System.err.println("map��С:" + map.size());
		System.err.println("clientMap��С:" + clientMap.size());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("���������յ�����Ϣ��" + ((ByteBuf) msg).toString(Charset.defaultCharset()));
		dealBusiness(ctx.channel(), msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.channel().flush();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Server---channelRegistered");
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Server---channelUnregistered");
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("���߶��ˣ��㻹��ô˵����");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("�쳣��Ŷ��");
		cause.printStackTrace();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Server---userEventTriggered");
	}
	
	//���ݿͻ��˷�����������룬�����Ӧ����
	private void dealBusiness(Channel channel, Object msg) {
		// TODO Auto-generated method stub
		// ���յ�����Ϣ
		String msgStr = ((ByteBuf) msg).toString(Charset.defaultCharset());
		Message message = JsonUtil.transToBean(msgStr);
		// ��½
		if (message != null && Constants.LOGIN_MSG.equals(message.getType())) {
			Message backMsg = login(message, channel);
			backMsg.setPalindType(Constants.LOGIN_MSG);
			Server.sendMsg(channel, backMsg);//��ͻ��˻ش�������
		}
		//��ӷ���
		if(message != null && Constants.ADD_USER_CATE_MSG.equals(message.getType())) {
			Message backMsg = new Message();
			backMsg.setType(Constants.PALIND_MSG);
			backMsg.setPalindType(Constants.ADD_USER_CATE_MSG);
			Category category = categoryDao.saveCategory(message.getSenderId(), 
					Constants.USER, message.getContent());
			backMsg.setCategory(category);
			backMsg.setStatus(Constants.SUCCESS);
			Server.sendMsg(channel, backMsg);
		}
	}

	//��¼�¼�����
	private Message login(Message message, Channel channel) {
		// TODO Auto-generated method stub
		String content = message.getContent();
		String msgStr[] = content.split(Constants.LEFT_SLASH);
		System.out.println("msgStr[0]");
		User user = userDao.getByUserName(msgStr[0]);
		if (null == user) {
			return new Message(Constants.PALIND_MSG, Constants.LOGIN_MSG, "���˺Ų����ڣ�");
		}
		user = userDao.login(msgStr[0], msgStr[1]);
		if (null == user) {
			return new Message(Constants.PALIND_MSG, Constants.LOGIN_MSG, "�˺Ż�������������");
		}
		if (clientMap.containsKey(user.getLoginName())) {
			return new Message(Constants.PALIND_MSG, Constants.LOGIN_MSG, "�벻Ҫ�ظ���¼��");
		}
		// ����ͻ���
		clientMap.put(user.getLoginName(), channel);
		map.put(channel.remoteAddress(), user.getLoginName());
		//��ϵ�ˣ�*��ʱ����Ⱥ�顢������Ⱥ�����ڴ���
		List<Category> categoryList = categoryDao.getListByUIdAndType(user.getLoginName(), Constants.USER);
		return new Message(Constants.PALIND_MSG, user, categoryList, getMemberList(categoryList));
	}

	/**
	 * getMemberList: ��ȡ�����Լ������������	<br/>
	 * @param cateList ���鼯��
	 * @return List	<br/>
	 * @since JDK 1.8
	 */
	private List<Map<String, List<User>>> getMemberList(List<Category> categoryList) {
		List<Map<String, List<User>>> categoryMemberList = new ArrayList<Map<String, List<User>>>();
		if (null != categoryList && categoryList.size() > 0) {
			for (Category category : categoryList) {
				Map<String, List<User>> map = new HashMap<String, List<User>>();// ������ϢMap
				List<User> list = new ArrayList<User>();// �����º��Ѽ���
				List<CategoryMember> cmList = categoryMemberDao.getListByCategoryId(category.getId());
				if (null != cmList && cmList.size() > 0) {
					for (CategoryMember cm : cmList) {
						User friend = userDao.getById(cm.getMemberLoginName());
						list.add(friend);
					}
				}
				map.put(category.getId(), list);
				categoryMemberList.add(map);
			}
		}
		return categoryMemberList;
		
	}

}
