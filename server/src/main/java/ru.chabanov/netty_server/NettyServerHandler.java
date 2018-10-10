package ru.chabanov.netty_server;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
  private static final ChannelGroup channels = new DefaultChannelGroup("containers", GlobalEventExecutor.INSTANCE);


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for(Channel channel : channels){
            channel.write("[SERVER] - "+incoming.remoteAddress()+" has joined\n");
            channel.flush();
        }
      //  channels.add(incoming); //????
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for(Channel channel : channels){
            channel.write("[SERVER] - "+incoming.remoteAddress()+" has left\n");
            channel.flush();
        }
        //  channels.remove(incoming); //????
        channels.remove(ctx.channel());
    }

//    @Override
//    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String message) throws Exception {
//        Channel inchannel = channelHandlerContext.channel();
//
//        for(Channel channel :channels){
//                       if(channel !=inchannel){
//                           channel.write("[ "+inchannel.remoteAddress()+" ] "+message+"\n");
//                       }
//        }
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel inchannel = channelHandlerContext.channel();



//        for(Channel channel :channels){
//            if(channel !=inchannel){
//                channel.write("[ "+inchannel.remoteAddress()+" ] "+s+"\n");
//                channel.flush();
//            }
//        }
    }
}
