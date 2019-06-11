package com.batsoft.socketjs;


/**
 * Created by yangyibo on 16/12/29.
 * 服务器向浏览器发送的此类消息。
 */
public class Response {


    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Response(String data){
        this.data = data;
    }

}
