package communication.wrapper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class NettyConnectionWrapper implements Connection {

    private ChannelHandlerContext context;

    public NettyConnectionWrapper(ChannelHandlerContext context) {
        this.context = context;
    }

    @Override
    public void send(String message) {
        context.channel().writeAndFlush(new TextWebSocketFrame(message));
    }

    @Override
    public void close() {
        context.close();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NettyConnectionWrapper) {
            NettyConnectionWrapper o = (NettyConnectionWrapper) obj;
            return o.context.channel().equals(context.channel());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return context.channel().hashCode();
    }
}
