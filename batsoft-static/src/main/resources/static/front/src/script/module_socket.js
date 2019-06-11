/**
 * socket
 */
define(function (require, exports, module) {

    var stompClient;
    var socket;


    /**
     * 生成客户端
     */
    function client(options) {
        if (socket === undefined) {
            if ('WebSocket' in window) {
                socket = new SockJS(options.wsUrl);
            } else {
                socket = new SockJS(options.httpUrl);
            }
        }
        stompClient = Stomp.over(socket);
    }

    /**
     * 创建连接
     * @param options
     */
    function connect(options) {
        var _default = {
                headers: {
                    //login: 'mylogin',
                    // passcode: 'mypasscode',
                    // additional header
                    //'client-id': 'my-client-id'
                },
                /*回调函数*/
                connectCallback: connectCallback,
                errorCallback:errorCallback
            },
            _option = $.extend(_default, options);

        stompClient.connect(_option.headers, _option.connectCallback, _option.errorCallback);
    }

    /**
     * 连接回调
     * @param frame
     */
    function connectCallback(frame) {
        console.log('Connected: ' + frame);
        /* stompClient.subscribe(GLOBAL.webSocket.broker+'/websocket/trade/buy', function (data) {
             var ret=JSON.parse(data.body);
             $("#test-socket-content").html(ret.symbol+"==="+ret.priceSymobl+"=="+ret.price);
         });*/
    }

    /**
     *异常回调
     * @param error
     */
    function errorCallback(error) {
        console.log(error.headers.message);
    }

    /**
     * 销毁
     */
    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    /**
     * 发送消息
     * @param msg
     */
    function send(options) {
        var _default = {
                //发送地址
                url: "",
                /*发送数据*/
                data: null
            },
            _option = $.extend(_default, options);
        stompClient.send(_option.url, {}, JSON.stringify(_option.data));
    }

    /**
     * 初始化socket
     * @param options
     * @param fun
     */
    function init(options, fun) {
        var _default = {
                //连接地址
                wsUrl: GLOBAL.webSocket.point == null ? "/socket" : GLOBAL.webSocket.point,
                httpUrl:'/'
            },
            _option = $.extend(_default, options);
        //生成客户端
        client(_option);
        //回调
        fun(stompClient);
    }

    /**
     * 是否在线
     * @returns {boolean}
     */
    function isOnline(connected) {
        return connected;
    }
    exports.isOnline=isOnline;
    exports.init = init;
    exports.send = send;
    exports.connect = connect;
    exports.disconnect=disconnect;
});