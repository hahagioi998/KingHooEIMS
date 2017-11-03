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
* @version 创建时间：2017年10月19日 下午3:36:35
* 类说明
*/
public class ServerHandler implements ChannelInboundHandler {

	private UserDao userDao;
	private CategoryDao categoryDao;
	private CategoryMemberDao categoryMemberDao;
	
	
	// 这里其实可以直接使用ip存储，为了防止一台电脑登录多个账号
	/** key：SocketAddress，value：用户Id */
	private Map<SocketAddress, String> map = new HashMap<SocketAddress, String>();
	/** key：用户Id，value：channel */
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
		System.out.println("客户端" + ctx.channel().remoteAddress() + "与服务器连接成功！");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("客户端" + ctx.channel().remoteAddress() + "掉线了！");
		clientMap.remove(map.get(ctx.channel().remoteAddress()));
		map.remove(ctx.channel().remoteAddress());
		System.err.println("map大小:" + map.size());
		System.err.println("clientMap大小:" + clientMap.size());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("服务器接收到的消息：" + ((ByteBuf) msg).toString(Charset.defaultCharset()));
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
		System.out.println("掉线儿了，你还怎么说话？");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("异常了哦！");
		cause.printStackTrace();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Server---userEventTriggered");
	}
	
	//根据客户端发来的请求代码，完成相应操作
	private void dealBusiness(Channel channel, Object msg) {
		// TODO Auto-generated method stub
		// 接收到的消息
		String msgStr = ((ByteBuf) msg).toString(Charset.defaultCharset());
		Message message = JsonUtil.transToBean(msgStr);
		// 登陆
		if (message != null && Constants.LOGIN_MSG.equals(message.getType())) {
			Message backMsg = login(message, channel);
			backMsg.setPalindType(Constants.LOGIN_MSG);
			Server.sendMsg(channel, backMsg);//向客户端回传请求结果
		}
		//添加好友
		if(message != null && Constants.REQUEST_ADD_MSG.equals(message.getType())) {
			Message backMsg = new Message();
			String content[] = message.getContent().split(Constants.LEFT_SLASH);
			User user = userDao.getListByUIdAndName(content[1]);
			if(user != null) {
				Category category = categoryDao.getById(content[0]);
				CategoryMember categoryMember = categoryMemberDao.getByCidAndMid(category.getId(),user.getLoginName());
				if(categoryMember != null) {//已经是你的好友
					backMsg.setContent("对方已经是您的好友，请不要重复添加！");
					backMsg.setStatus(Constants.FAILURE);
					backMsg.setType(Constants.PALIND_MSG);
					backMsg.setPalindType(Constants.REQUEST_ADD_MSG);
					Server.sendMsg(channel, backMsg);
				} else {
					backMsg.setContent("请求已发送，静待对方回答！");
					backMsg.setType(Constants.PALIND_MSG);
					backMsg.setPalindType(Constants.REQUEST_ADD_MSG);
					Server.sendMsg(channel, backMsg);
					//告诉接收者
					backMsg.setType(Constants.REQUEST_ADD_MSG);
					backMsg.setSenderId(message.getSenderId());
					backMsg.setSenderName(message.getSenderName());
					backMsg.setContent(content[0]);// 将群组id带过去
					Server.sendMsg(clientMap.get(user.getLoginName()), backMsg);
				}
			} else {
				backMsg.setType(Constants.PALIND_MSG);
				backMsg.setStatus(Constants.FAILURE);
				backMsg.setPalindType(Constants.REQUEST_ADD_MSG);
				backMsg.setContent("用户不存在");
				Server.sendMsg(channel, backMsg);
			}
		}
		//回应添加好友 
		if(message != null && Constants.ECHO_ADD_MSG.equals(message.getType())) {
			String content[] = message.getContent().split(Constants.LEFT_SLASH);
			if(Constants.YES.equals(content[1])) {
				//要保存的成员ID
				String memberId = message.getSenderId();//回应方
				String ownerId = message.getReceiverId();//请求方
				//把回应方也加到请求方的好友列表
				//请求方
				Category category = categoryDao.getById(content[0]);
				if(category != null) {
					CategoryMember categoryMember = categoryMemberDao.saveCategoryMember(ownerId, category.getId(), memberId);
					if(categoryMember != null) {
						User self = userDao.getById(ownerId);
						User contacts = userDao.getById(memberId);
						List<Category> categoryList = categoryDao.getListByUIdAndType(self.getLoginName(), Constants.USER);
						Message backMsg = new Message();
						backMsg.setType(Constants.PALIND_MSG);
						backMsg.setUser(self);
						backMsg.setCategoryList(categoryList);
						backMsg.setCategoryMemberList(getMemberList(categoryList));
						backMsg.setFriend(contacts);

						backMsg.setPalindType(Constants.REQUEST_ADD_MSG);
						backMsg.setSenderName(message.getSenderName());
						backMsg.setContent(category.getId());
						backMsg.setStatus(Constants.SUCCESS);
						Server.sendMsg(clientMap.get(ownerId), backMsg);
					}
				}
				// 把请求方也加到回应方的好友列表
				// 默认分组：联系人
				// 回应方
				Category category2 = categoryDao.getByCondition(memberId, Constants.USER, Constants.DEFAULT_CATE);
				if(category2 != null) {
					CategoryMember categoryMember2 = categoryMemberDao.saveCategoryMember(memberId, category2.getId(), ownerId);
					if(categoryMember2 != null) {
						User user = userDao.getById(memberId);
						User contacts = userDao.getById(ownerId);
						List<Category> categoryList = categoryDao.getListByUIdAndType(user.getLoginName(), Constants.USER);
						
						Message backMsg = new Message();
						backMsg.setType(Constants.PALIND_MSG);
						backMsg.setUser(user);
						backMsg.setCategoryList(categoryList);
						backMsg.setCategoryMemberList(getMemberList(categoryList));
						backMsg.setFriend(contacts);
						
						backMsg.setPalindType(Constants.ECHO_ADD_MSG);
						backMsg.setContent(category2.getId());
						backMsg.setStatus(Constants.SUCCESS);
						Server.sendMsg(channel, backMsg);
					}
				}
			}
			if(Constants.NO.equals(content[1])) {
				Message backMsg = new Message();
				backMsg.setType(Constants.PALIND_MSG);
				backMsg.setStatus(Constants.FAILURE);
				backMsg.setPalindType(Constants.REQUEST_ADD_MSG);
				backMsg.setContent(message.getSenderName() + "拒绝了您的好友请求！");
				Server.sendMsg(clientMap.get(message.getReceiverId()), backMsg);
			}
		}
		//删除分组
		if(message != null && Constants.DELETE_USER_CATE_MSG.equals(message.getType())) {
			
		}
		//添加分组
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
		//重命名分组
		if(message != null && Constants.EDIT_USER_CATE_MSG.equals(message.getType())) {
			String content[] = message.getContent().split(Constants.LEFT_SLASH);
			Category category = categoryDao.editCategory(content[0],content[1]);
			Message backMsg = new Message();
			backMsg.setType(Constants.PALIND_MSG);
			backMsg.setPalindType(Constants.EDIT_USER_CATE_MSG);
			backMsg.setCategory(category);
			Server.sendMsg(channel, backMsg);
		}
	}
	
	/**
	 * 登录事件处理
	 * */
	private Message login(Message message, Channel channel) {
		// TODO Auto-generated method stub
		String content = message.getContent();
		String msgStr[] = content.split(Constants.LEFT_SLASH);
		System.out.println("msgStr[0]");
		User user = userDao.getByUserName(msgStr[0]);
		if (null == user) {
			return new Message(Constants.PALIND_MSG, Constants.LOGIN_MSG, "该账号不存在！");
		}
		user = userDao.login(msgStr[0], msgStr[1]);
		if (null == user) {
			return new Message(Constants.PALIND_MSG, Constants.LOGIN_MSG, "账号或密码输入有误！");
		}
		if (clientMap.containsKey(user.getLoginName())) {
			return new Message(Constants.PALIND_MSG, Constants.LOGIN_MSG, "请不要重复登录！");
		}
		// 保存客户端
		clientMap.put(user.getLoginName(), channel);
		map.put(channel.remoteAddress(), user.getLoginName());
		//联系人，*暂时不管群组、开发到群组是在处理
		List<Category> categoryList = categoryDao.getListByUIdAndType(user.getLoginName(), Constants.USER);
		return new Message(Constants.PALIND_MSG, user, categoryList, getMemberList(categoryList));
	}

	/**
	 * getMemberList: 获取分组以及分组下面的人	<br/>
	 * @param cateList 分组集合
	 * @return List	<br/>
	 * @since JDK 1.8
	 */
	private List<Map<String, List<User>>> getMemberList(List<Category> categoryList) {
		List<Map<String, List<User>>> categoryMemberList = new ArrayList<Map<String, List<User>>>();
		if (null != categoryList && categoryList.size() > 0) {
			for (Category category : categoryList) {
				Map<String, List<User>> map = new HashMap<String, List<User>>();// 分组信息Map
				List<User> list = new ArrayList<User>();// 分组下好友集合
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
