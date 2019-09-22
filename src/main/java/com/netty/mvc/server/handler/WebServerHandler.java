package com.netty.mvc.server.handler;

import com.netty.mvc.DispatcherHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zxj
 * @Date: 2019-09-05 11:05
 * @desc
 */
@Configuration
@ChannelHandler.Sharable
public class WebServerHandler extends ChannelInboundHandlerAdapter{

    private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;


    DispatcherHandler dispatcherHandler;


    public WebServerHandler(DispatcherHandler dispatcherHandler){
        this.dispatcherHandler = dispatcherHandler;
    }
    /**
     * 每个信息入站都会调用
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Object result = "";
        if(msg instanceof HttpRequest){
            result  = dispatcherHandler.handle((HttpRequest)msg);
        }
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(result.toString().getBytes())); // 2

        HttpHeaders heads = response.headers();
        heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes()); // 3
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        //将接受到的消息写给发送者
        ctx.write(response);
    }


    /**
     * 通知处理器最后的channelread是当前批处理中的最后一条信息调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将未决消息冲刷到远程节点，并关闭该Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();  //打印异常栈追踪
        ctx.close(); //关闭该channel
    }
}
