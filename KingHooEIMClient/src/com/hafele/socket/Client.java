package com.hafele.socket;

import java.awt.TrayIcon;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.hafele.bean.Category;
import com.hafele.bean.Message;
import com.hafele.bean.User;
import com.hafele.ui.common.CategoryNode;
import com.hafele.ui.contacts.ContactsNode;
import com.hafele.ui.frame.AddContactsWindow;
import com.hafele.ui.frame.ChatRoom;
import com.hafele.ui.frame.ChatRoomPanel;
import com.hafele.ui.frame.LoginWindow;
import com.hafele.ui.frame.MainWindow;
import com.hafele.util.Constants;
import com.hafele.util.JsonUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月17日 下午4:10:02
* 客户端核心，将东西都提到这个类共用，便于管理 
*/
public class Client {
	
	// 与服务器交互的channel通道
	private Channel channel;
	
	/** 主窗体 */
	private MainWindow mainWindow;
	/** 登陆框 */
	private LoginWindow login;
	/** 注册框 */

	/** 添加好友框 */
	private AddContactsWindow addContactsWindow;
	/** 聊天室 */
	private ChatRoom room;
	/** 系统托盘 */
	private TrayIcon icon;
	/** 好友tree */
	private JTree buddyTree;
	/** 好友treeModel */
	private DefaultTreeModel buddyModel;
	/** 好友treeRoot */
	private DefaultMutableTreeNode buddyRoot;
	
	/** key：好友名称 value：tab块 */
	public Map<String, ChatRoomPanel> tabMap = new HashMap<String, ChatRoomPanel>();
	/** key：好友名称 value：node节点 */
	public Map<String, ContactsNode> buddyNodeMap = new HashMap<String, ContactsNode>();
	/** key：分组Id value：node节点 */
	public Map<String, CategoryNode> cateNodeMap = new HashMap<String, CategoryNode>();
	/** key：好友名称 value：消息浏览状态 （用于确定头像的闪动） */
	
	/** key：好友名称 value：消息队列 （便于放到聊天窗中） */
	
	// 自身信息
	private User user;
	private List<Category> categoryList;
	private List<Map<String, List<User>>> categoryMemberList;
	
	public Client() {
		final ClientHandler clientHandler = new ClientHandler(this);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(new NioEventLoopGroup());
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2));
					ch.pipeline().addLast(new LengthFieldPrepender(2, false));
					ch.pipeline().addLast(clientHandler);
				}
			});
			ChannelFuture future = bootstrap.connect(
					new InetSocketAddress(Constants.SERVER_IP, Constants.SERVER_PORT)).sync();
			// TODO 这里为什么加上了sync()方法之后，启动client类的时候会被阻塞住，导致后面的发消息都不行
			// future.channel().closeFuture().sync();
			future.channel().closeFuture();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
	
	public void sendMsg(Message message) {
		String msg = JsonUtil.transToJson(message);
		channel.writeAndFlush(ByteBufAllocator.DEFAULT
				.buffer().writeBytes(msg.getBytes()))
				.addListener(new ClientListener());
		System.out.println("客户端发送的消息：" + msg.getBytes().length + msg);
	}

	public TrayIcon getIcon() {
		return icon;
	}

	public void setIcon(TrayIcon icon) {
		this.icon = icon;
	}
	
	public MainWindow getMainWindow() {
		return mainWindow != null ? mainWindow : null;
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public LoginWindow getLogin() {
		return login != null ? login : null;
	}

	public void setLogin(LoginWindow login) {
		this.login = login;
	}
	
	public AddContactsWindow getAddContactsWindow() {
		return addContactsWindow != null ? addContactsWindow : null;
	}

	public void setAddContactsWindow(AddContactsWindow addContactsWindow) {
		this.addContactsWindow = addContactsWindow;
	}
	
	public ChatRoom getRoom() {
		return room;
	}

	public void setRoom(ChatRoom room) {
		this.room = room;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public JTree getBuddyTree() {
		return buddyTree;
	}

	public void setBuddyTree(JTree buddyTree) {
		this.buddyTree = buddyTree;
	}

	public DefaultTreeModel getBuddyModel() {
		return buddyModel;
	}

	public void setBuddyModel(DefaultTreeModel buddyModel) {
		this.buddyModel = buddyModel;
	}

	public DefaultMutableTreeNode getBuddyRoot() {
		return buddyRoot;
	}

	public void setBuddyRoot(DefaultMutableTreeNode buddyRoot) {
		this.buddyRoot = buddyRoot;
	}

	public Map<String, CategoryNode> getCateNodeMap() {
		return cateNodeMap;
	}

	public void setCateNodeMap(Map<String, CategoryNode> cateNodeMap) {
		this.cateNodeMap = cateNodeMap;
	}
	
	public Map<String, ContactsNode> getBuddyNodeMap() {
		return buddyNodeMap;
	}

	public void setBuddyNodeMap(Map<String, ContactsNode> buddyNodeMap) {
		this.buddyNodeMap = buddyNodeMap;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public List<Map<String, List<User>>> getCategoryMemberList() {
		return categoryMemberList;
	}

	public void setCategoryMemberList(List<Map<String, List<User>>> categoryMemberList) {
		this.categoryMemberList = categoryMemberList;
	}
	
}
