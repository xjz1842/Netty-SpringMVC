package com.netty.mvc;

import com.netty.mvc.server.handler.WebServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: zxj
 * @Date: 2019-09-04 16:38
 * @desc
 */
public class Server {

    public static void main(String[] args)throws Exception {
        //启动Spring的容器
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.scan("com.netty.mvc");
        annotationConfigApplicationContext.refresh();

        final DispatcherHandler dispatcherHandler = new DispatcherHandler(annotationConfigApplicationContext);

        //创建Even Loop Group
        //配置服务器的NIO线程组
        //两个Reactor 一个用于服务器接收客户端的连接  一个用于经行SocketChannel的网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //创建ServerBootStrap
            ServerBootstrap b = new ServerBootstrap();
            //指定所使用的NIO传输Channle
            b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .localAddress(8080)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            //如果ServerHandler被注为@Shareable的时候，则可以总是使用同样的实例
                            socketChannel.pipeline()
                                    .addLast(new HttpRequestDecoder())
                                    .addLast(new HttpResponseEncoder())
                                    .addLast(new WebServerHandler(dispatcherHandler));
                        }
                    });
            //异步的绑定服务器，调用sync===方法阻塞，直到绑定完成
            ChannelFuture f = b.bind().sync();
            System.out.println("netty服务端启动成功");
            //获取Channel的CloseFuture,并阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        }finally {
            //关闭EvenLoopGroup,释放所有资源
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }

}
