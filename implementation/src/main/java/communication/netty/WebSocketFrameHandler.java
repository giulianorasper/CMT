package communication.netty;

import communication.CommunicationHandler;
import communication.wrapper.Connection;
import communication.wrapper.NettyConnectionWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;


public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private CommunicationHandler handler;

    public WebSocketFrameHandler(CommunicationHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws UnsupportedOperationException {
        Connection connection = new NettyConnectionWrapper(ctx);
        if(frame instanceof TextWebSocketFrame) {
            String request = ((TextWebSocketFrame) frame).text();
            handler.onMessage(connection, request);
        } else if(frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
            handler.onMessage(connection, frame.content());
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        Connection connection = new NettyConnectionWrapper(ctx);
        handler.onRegistered(connection);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        Connection connection = new NettyConnectionWrapper(ctx);
        handler.onUnregistered(connection);
    }
}