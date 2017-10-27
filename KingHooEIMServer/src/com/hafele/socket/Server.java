package com.hafele.socket;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.hafele.bean.Message;
import com.hafele.util.Constants;
import com.hafele.util.JsonUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月19日 下午3:34:15
* 服务器
*/
public class Server {
	private Map<SocketAddress, String> map = new HashMap<SocketAddress, String>();
	private Map<String, Channel> clientMap = new HashMap<String, Channel>();

	public void startServer() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2));
					ch.pipeline().addLast(new LengthFieldPrepender(2, false));
					ch.pipeline().addLast(new ServerHandler(map, clientMap));
				}
			});
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture future = bootstrap.bind(Constants.SERVER_PORT).sync();
			System.err.println("服务器成功启动——IP地址为："+Constants.SERVER_IP+"端口为："+Constants.SERVER_PORT);
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	public static void sendMsg(Channel channel, Message message) {
		if (null != channel) {
			String msg = JsonUtil.transToJson(message);
			channel.writeAndFlush(ByteBufAllocator.DEFAULT.buffer()
					.writeBytes(msg.getBytes()))
					.addListener(new ServerListener());
			System.out.println("服务端发送的消息：" + msg.getBytes().length + msg);
		}
	}

	public static void getInstance() {
		Server server = new Server();
		server.startServer();
	}
}
