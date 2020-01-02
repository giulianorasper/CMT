package communication.netty;

import communication.CommunicationHandler;
import communication.CommunicationManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;


public final class NettyCommunicationManager implements CommunicationManager {

    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    //TODO
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    Channel channel;
    int port;
    private CommunicationHandler handler;
    private boolean secure = true;
    private SslContext sslContext;

    public NettyCommunicationManager(CommunicationHandler handler, int port) {
        this(handler, port, null);
        secure = false;
    }

    public NettyCommunicationManager(CommunicationHandler handler, int port, SslContext sslContext) {
        this.handler = handler;
        this.port = port;
        this.sslContext = sslContext;
    }

    @Override
    public void start() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new WebSocketServerInitializer(sslContext, handler));

            channel = b.bind(port).sync().channel();

            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {
        handler.stop();
        channel.close();
    }

    public boolean isSecure() {
        return secure;
    }
}