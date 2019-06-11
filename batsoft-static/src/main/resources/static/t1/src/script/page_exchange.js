/**
 *<p>page_exchange.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {
    var  mVad      = require('./module_validator'), //表单校验
        swal      = require('./module_swal'), //弹窗 alert
        effect = require('./module_effect'), //特效
        tpl = require('./module_tpl'),
        table = require('./module_table'),
        U = require('./module_utils'),
        socket = require('./module_socket'),
        core      =require('./module_core');
    /**
     * 初始数据
     * @type {{coin: string}}
     * @private
     */
    var _default={coin:'BTC'}
    /**
     * exchange 首页
     */
    function index(){
        socket.init({},function (client) {
            socket.connect({"connectCallback":function (frame) {
                    console.log('Connected: ' + frame);
                    client.subscribe(GLOBAL.webSocket.broker+'/websocket/trade/buy', function (data) {
                        var ret=JSON.parse(data.body);
                        $(".H_"+ret.symbol+ret.priceSymobl+"_Market").find(".price").html(ret.price);
                        $(".H_"+ret.symbol+ret.priceSymobl+"_Market").find(".height").html(ret.height);
                        $(".H_"+ret.symbol+ret.priceSymobl+"_Market").find(".low").html(ret.low);
                        $(".H_"+ret.symbol+ret.priceSymobl+"_Market").find(".vol").html(ret.vol);
                        $(".H_"+ret.symbol+ret.priceSymobl+"_Market").find(".change").html(ret.change);
                    });

                    client.subscribe('/user/'+GLOBAL.webSocket.broker+'/finance', function (data) {
                        var ret=JSON.parse(data.body);
                        var convertedData = getKlineData(ret);
                        $("#finance").html(convertedData)
                    });

                }
            });

        });

        $("#test-socket").on("click",function () {
            socket.send({url:"/user/finance"})
        })
        /**
         * 加载定价币种信息
         */
        core.list({url:BASE_PATH+ "/exchange/price/coins"}).then(function (data) {
            tpl.render({"tplId": "#coinsTpl", "renderId": "#coinsRender", "data": data});
            $("#coinsRender >li").each(function (e) {
                $(this).on("click",function () {
                    $(this).addClass("active").siblings().removeClass("active")
                    loadTradeCoins($(this).attr("data-symbol"));
                })
            })
            return data;
        }).then(function (data) {
            loadTradeCoins(data[0].symbol);
        })



    }

    /**
     * 加载交易币数据
     */
    function loadTradeCoins(symbol){
        /**
         * 加载定价币种信息
         */
        core.list({url:BASE_PATH+ "/exchange/coins/"+symbol}).then(function (data) {
            $(".price-coin").html(data.priceCoin);
            tpl.render({"tplId": "#tradeCoinsTpl", "renderId": "#tradeCoinsRender", "data": $.parseJSON(data.tradeCoins)});
        })
    }

    /**
     * 我的账户
     */
    function userFinance(){
        core.list({url:BASE_PATH+ "/exchange/user/financeData"}).then(function (data) {
            tpl.render({"tplId": "#financeTpl", "renderId": "#financeRender", "data": data.data});
        })
    }
    /**
     * 充值
     */
    function recharge(){
        /**
         * 加载币种信息
         */
        core.list({url:BASE_PATH+ "/exchange/coins"}).then(function (data) {

            tpl.render({"tplId": "#coinsTpl", "renderId": "#coinsRender", "data": data});
        })
    }
    /**
     * 提币
     */
    function deposit(){

    }
    /**
     * exchange list
     */
    function init() {


    }

    exports.userFinance=userFinance;
    exports.recharge=recharge;
    exports.deposit=deposit;
    exports.index=index;
});