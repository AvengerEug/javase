package io.netty.funcdemo.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Random;

public class HeartbeatClient {

    public static void main(String[] args) {
        EventLoopGroup clientEventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(clientEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new HeartbeatClientHandler());
                        }
                    });

            System.out.println("开始启动netty客户端");
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).sync();
            Channel channel = channelFuture.channel().closeFuture().sync().channel();

            Random random = new Random();
            // channel连接没有断开时，循环向服务器发送心跳数据包
            while (channel.isActive()) {
                // 随机0 - 9 秒
                int delayTime = random.nextInt(10);
                Thread.sleep(delayTime * 1000);
                channel.writeAndFlush("heartbeat");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clientEventLoopGroup.shutdownGracefully();
        }
    }
}
