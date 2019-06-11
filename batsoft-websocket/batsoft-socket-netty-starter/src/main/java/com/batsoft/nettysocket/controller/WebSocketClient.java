package com.batsoft.nettysocket.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.nettysocket.config.NettyConfig;
import com.batsoft.nettysocket.domain.RequestData;
import com.batsoft.nettysocket.service.SocketDataService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class WebSocketClient {

    @Autowired
    private SocketDataService socketDataService;

    public static final String CHANNEL_BOOK="channel_book";
    public static final String CHANNEL_TRADE="channel_trade";

    public static void onReceive(ChannelHandlerContext context, WebSocketFrame webSocketFrame){
        //返回应答消息
        //获取客户端向服务端发送的消息
          String request = ((TextWebSocketFrame) webSocketFrame ).text();
         System.out.println("服务端收到客户端的消息：" + request);
        // TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(context.channel().id() + ":" + request);
        //服务端向每个连接上来的客户端发送消息
       // NettyConfig.group.writeAndFlush(textWebSocketFrame);


        RequestData requestData=JSON.parseObject(request,RequestData.class);

        if(CHANNEL_BOOK.equals(requestData.getChannel())){
            initBook(context,requestData.getData().get("coinPair").toString());
        }

        if(CHANNEL_TRADE.equals(requestData.getChannel())){
            initTrade(context,requestData.getData().get("coinPair").toString());
        }

    }


    public static void initBook(ChannelHandlerContext context,String coinPair){
        JSONObject data=new JSONObject();
       // SocketDataService socketDataService=(SocketDataService)SpringContextUtil.getBean("socketDataService");
        data.put("channel",coinPair+"_book");
       // data.put("data",socketDataService.initBook(coinPair));

        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(data.toJSONString());
        NettyConfig.group.find(context.channel().id()).writeAndFlush(textWebSocketFrame);
    }

    public static void initTrade(ChannelHandlerContext context,String coinPair){
        JSONObject data=new JSONObject();
        data.put("channel",coinPair+"_trade");
        data.put("data",new String[]{"zbc","def"});
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(data.toJSONString());
        NettyConfig.group.find(context.channel().id()).writeAndFlush(textWebSocketFrame);
    }

    @GetMapping(value="/aa/test")
    public  void test(){
        String data=socketDataService.testBook();
        System.out.println("====="+data);
    }

}
