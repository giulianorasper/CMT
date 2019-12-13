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
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        Connection connection = new NettyConnectionWrapper(ctx);
        if(frame instanceof TextWebSocketFrame) {
            String request = ((TextWebSocketFrame) frame).text();
            handler.onMessage(connection, request);
            System.out.println("Text");
        } else if(frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
            handler.onMessage(connection, frame.content().nioBuffer());
            System.out.println("Binary");
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

}