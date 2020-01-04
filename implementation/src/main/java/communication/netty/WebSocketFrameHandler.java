package communication.netty;

import communication.CommunicationHandler;
import communication.wrapper.Connection;
import communication.wrapper.NettyConnectionWrapper;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;


public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private CommunicationHandler handler;

    public WebSocketFrameHandler(CommunicationHandler handler) {
        this.handler = handler;
    }

    private StringBuilder textBuffer = null;
    private CompositeByteBuf byteBuf = null;
    private FrameType frameType = FrameType.Undefined;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws UnsupportedOperationException {
        Connection connection = new NettyConnectionWrapper(ctx);
        if(frame instanceof TextWebSocketFrame) {
            if(frameType == FrameType.Undefined) frameType = FrameType.Text;
            else return;

            String message = ((TextWebSocketFrame) frame).text();
            textBuffer = new StringBuilder(message);
        } else if(frame instanceof BinaryWebSocketFrame) {
            if(frameType == FrameType.Undefined) frameType = FrameType.Binary;
            else return;
            frame.content().retain();
            byteBuf = ByteBufAllocator.DEFAULT.compositeBuffer().addComponent(true, frame.content()).retain();
        } else if(frame instanceof ContinuationWebSocketFrame) {
            ContinuationWebSocketFrame continuationWebSocketFrame = (ContinuationWebSocketFrame) frame;
            if(frameType == FrameType.Text) textBuffer.append(continuationWebSocketFrame.text());
            else if(frameType == FrameType.Binary) {
                frame.content().retain();
                byteBuf.addComponent(true, frame.content());
            }
            else throw new UnsupportedOperationException("unexpected continuation frame, no initial frame received");
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
        if(frame.isFinalFragment()) {
            try {
                if(frameType == FrameType.Text) handler.onMessage(connection, textBuffer.toString());
                else if(frameType == FrameType.Binary) handler.onMessage(connection, byteBuf);
            } finally {
                if(frameType == FrameType.Binary) byteBuf.release();
                frameType = FrameType.Undefined;
                textBuffer = null;
                byteBuf = null;
            }
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