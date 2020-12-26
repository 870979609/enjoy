package com.lyq.framework.netty.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class SomeClient{

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup parentGroup = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			
			bootstrap.group(parentGroup)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>(){

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ChannelPipeline pipeline = ch.pipeline();
				//	pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
					pipeline.addLast(new SomeSocketClientHandler());
					
				}
				
			});
			
			ChannelFuture future = bootstrap.connect("localhost", 8888).sync();
			future.channel().closeFuture().sync();
		}finally {
			if(parentGroup != null) {
				parentGroup.shutdownGracefully();
			}
		}
	}
}
