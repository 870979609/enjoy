package com.lyq.framework.netty.socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SomeSocketClientHandler extends ChannelInboundHandlerAdapter{

	
	private String msg =  "Netty is a NIO client server framework " +
			 "which enables quick and easy development of network applications " +
			 "such as protocol servers and clients. It greatly simplifies and " +
			 "streamlines network programming such as TCP and UDP socket server." +
			 "'Quick and easy' doesn't mean that a resulting application will " +
			 "suffer from a maintainability or a performance issue. Netty has " +
			 "this guide and play with Netty.In other words, Netty is an NIO " +
			 "framework that enables quick and easy development of network " +
			 "as protocol servers and clients. It greatly simplifies and network " +
			 "programming such as TCP and UDP socket server development.'Quick " +
			 "not mean that a resulting application will suffer from a maintain" +
			 "performance issue. Netty has been designed carefully with the expe " +
			 "from the implementation of a lot of protocols such as FTP, SMTP, " +
			 " binary and text-based legacy protocols. As a result, Netty has " +
			 "a way to achieve of development, performance, stability, without " +
			 "which enables quick and easy development of network applications " +
			 "such as protocol servers and clients. It greatly simplifies and " +
			 "streamlines network programming such as TCP and UDP socket server." +
			 "'Quick and easy' doesn't mean that a resulting application will " +
			 "suffer from a maintainability or a performance issue. Netty has " +
			 "this guide and play with Netty.In other words, Netty is an NIO " +
			 "framework that enables quick and easy development of network " +
			 "as protocol servers and clients. It greatly simplifies and network " +
			 "programming such as TCP and UDP socket server development.'Quick " +
			 "not mean that a resulting application will suffer from a maintain" +
			 "performance issue. Netty has been designed carefully with the expe " +
			 "from the implementation of a lot of protocols such as FTP, SMTP, " +
			 " binary and text-based legacy protocols. As a result, Netty has " +
			 "a way to achieve of development, performance, stability, without " +
			 "which enables quick and easy development of network applications " +
			 "such as protocol servers and clients. It greatly simplifies and " +
			 "streamlines network programming such as TCP and UDP socket server." +
			 "'Quick and easy' doesn't mean that a resulting application will " +
			 "suffer from a maintainability or a performance issue. Netty has " +
			 "this guide and play with Netty.In other words, Netty is an NIO " +
			 "framework that enables quick and easy development of network " +
			 "as protocol servers and clients. It greatly simplifies and network " +
			 "programming such as TCP and UDP socket server development.'Quick " +
			 "not mean that a resulting application will suffer from a maintain" +
			 "performance issue. Netty has been designed carefully with the expe " +
			 "from the implementation of a lot of protocols such as FTP, SMTP, " +
			 " binary and text-based legacy protocols. As a result, Netty has " +
			 "a way to achieve of development, performance, stability, without " +
			 "which enables quick and easy development of network applications " +
			 "such as protocol servers and clients. It greatly simplifies and " +
			 "framework that enables quick and easy development of network " +
			 "as protocol servers and clients. It greatly simplifies and network " +
			 "programming such as TCP and UDP socket server development.'Quick " +
			 "not mean that a resulting application will suffer from a maintain" +
			 "performance issue. Netty has been designed carefully with the expe " +
			 "from the implementation of a lot of protocols such as FTP, SMTP, " +
			 " binary and text-based legacy protocols. As a result, Netty has " +
			 "a way to achieve of development, performance, stability, without " +
			 "which enables quick and easy development of network applications " +
			 "such as protocol servers and clients. It greatly simplifies and " +
			 "framework that enables quick and easy development of network " +
			 "as protocol servers and clients. It greatly simplifies and network " +
			 "programming such as TCP and UDP socket server development.'Quick " +
			 "not mean that a resulting application will suffer from a maintain" +
			 "performance issue. Netty has been designed carefully with the expe " +
			 "from the implementation of a lot of protocols such as FTP, SMTP, " +
			 " binary and text-based legacy protocols. As a result, Netty has " +
			 "a way to achieve of development, performance, stability, without " +
			 "which enables quick and easy development of network applications " +
			 "such as protocol servers and clients. It greatly simplifies and " +
			 "streamlines network programming such as TCP and UDP socket server." +
			 "'Quick and easy' doesn't mean that a resulting application will " +
			 "suffer from a maintainability or a performance issue. Netty has " +
			 "this guide and play with Netty.In other words, Netty is an NIO " +
			 "framework that enables quick and easy development of network " +
			 "as protocol servers and clients. It greatly simplifies and network " +
			 "programming such as TCP and UDP socket server development.'Quick " +
			 "not mean that a resulting application will suffer from a maintain" +
			 "performance issue. Netty has been designed carefully with the expe " +
			 "from the implementation of a lot of protocols such as FTP, SMTP, " +
			 " binary and text-based legacy protocols. As a result, Netty has " +
			 "a way to achieve of development, performance, stability, without " +
			 "a compromise.=====================================================";;
	
/*	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(msg);
		ctx.writeAndFlush(msg);
	}*/
			 
			 @Override
			    public void channelActive(ChannelHandlerContext ctx) throws Exception {
			        byte[] bytes = "HelloWorld".getBytes();
			        ByteBuf buffer = null;
			        for(int i=0; i<100; i++) {
			            // 申请缓存空间
			            buffer = Unpooled.buffer(bytes.length);
			            // 将数据写入到缓存
			            buffer.writeBytes(bytes);
			            // 将缓存中的数据写入到Channel
			            ctx.writeAndFlush(buffer);
			        }

			        // for(int i=0; i<100; i++) {
			        //     ctx.channel().writeAndFlush(message);
			        // }
			    }

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
