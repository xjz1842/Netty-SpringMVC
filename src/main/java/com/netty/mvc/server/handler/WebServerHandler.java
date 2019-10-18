package com.netty.mvc.server.handler;

import com.netty.mvc.DispatcherHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedStream;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Author: zxj
 * @Date: 2019-09-05 11:05
 * @desc
 */
@Configuration
@ChannelHandler.Sharable
public class WebServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

    private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;


    DispatcherHandler dispatcherHandler;


    public WebServerHandler(DispatcherHandler dispatcherHandler){
        this.dispatcherHandler = dispatcherHandler;
    }
    /**
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        if (!fullHttpRequest.getDecoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }
        Object result  = dispatcherHandler.handle(fullHttpRequest);

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(result.toString().getBytes())); // 2

        HttpHeaders heads = response.headers();
        heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes()); // 3
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        //将接受到的消息写给发送者
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        ByteBuf content = Unpooled.copiedBuffer(
                "Failure: " + status.toString() + "\r\n",
                CharsetUtil.UTF_8);

        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
                HTTP_1_1,
                status,
                content
        );
        fullHttpResponse.headers().add(CONTENT_TYPE, "text/plain; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }


}
