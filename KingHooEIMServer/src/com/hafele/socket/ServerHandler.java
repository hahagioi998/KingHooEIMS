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
	}

	//登录事件处理
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
