/**
 *<p>page_market.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {
    var  mVad      = require('./module_validator'), //表单校验
        effect = require('./module_effect'), //特效
        el = require('./page_exchange_eventLog'),
        socket = require('./module_socket'),
        U = require('./module_utils'),
        core      =require('./module_core');
    /**
     * 初始数据
     * @type {{coin: string}}
     * @private
     */
    var _default={coin:'BTC'}

    /**
     * 行情图表
     */
    function init(){
        effect.toggle();
        socket.init({},function (client) {
            socket.connect({"connectCallback":function (frame) {
                    socket.isOnline(true);
                    console.log('Connected: ' + frame);
                    client.subscribe(GLOBAL.webSocket.broker+'/websocket/kline/data', function (data) {
                        var ret=JSON.parse(data.body);
                        var convertedData = getKlineData(ret);
                        window.onPushingResponse &&window.onPushingResponse(marketFrom, type, coinVol, convertedData);
                    });


                }
            });

        });
        var symbol=U.getUrlParameter("symbol",true);
        $.cookie('symbol',symbol, {path: '/'});
        var period = '';

        var marketFrom = '';
        var type = '';
        var coinVol = 0;

        var lastPeriodType = '';

        // socket出错时是否需要停掉K线推送
        var shouldStopKLinePushingWhenSocketError = true;
        function PushFromSpot(marketString, marketFromParam, typeParam, coinVolParam, time, isLine) {


            marketFrom = marketFromParam;
            type = typeParam;
            coinVol = coinVolParam;

            // 区分"分时"和"1分钟"
            var newPeriodType = isLine ? (typeParam + 'Line') : typeParam;

            if (lastPeriodType === newPeriodType) return;

            period = type;
            lastPeriodType = newPeriodType;
            sendKline(period);
        }

        window.PushFromSpot = PushFromSpot;
        function sendKline(period) {
            if (!socket.isOnline) {

                setTimeout(function () {
                    sendKline(period);
                }, 500);

                return;
            }



        }

        // 深度数据更新到K线
        function onDepthDataChange(data) {
            var kLineFrame = jQuery("#kline_iframe")[0];

            // 设置K线的深度信息
            if (kLineFrame && kLineFrame.contentWindow && kLineFrame.contentWindow._set_current_depth) {
                var kdata = $.extend(true, {}, data);
                kLineFrame.contentWindow._set_current_depth(getKlineDepthData(kdata));
            }
        }

        // 对推送的K线数据进行转换
        function getKlineData(datas) {
            var dataNew = [];
            for (var i = 0; i < datas.length; i++) {
                var data = datas[i];
                var tmp = [];
                tmp.push(Number(data.createdDate));
                tmp.push(Number(data.open));
                tmp.push(Number(data.high));
                tmp.push(Number(data.low));
                tmp.push(Number(data.close));
                tmp.push(Number(data.volume));
                dataNew.push(tmp);
            }
            return dataNew;
        }

        // 对K线深度数据进行转换
        function getKlineDepthData(datas) {
            var asks = datas.asks;
            var bids = datas.bids;

            asks.reverse();

            var i;

            var newAsks = [];
            var newBids = [];

            for (i = 0; i < asks.length; i++) {
                newAsks.push([Number(asks[i][0]), Number(asks[i][1])]);
            }

            for (i = 0; i < bids.length; i++) {
                newBids.push([Number(bids[i][0]), Number(bids[i][1])]);
            }

            return {
                asks: newAsks,
                bids: newBids
            }
        }

        // 更新基准货币和交易货币
        function updateBaseAndTradeCurrency() {
            var parts = symbol.split("_");

            ok.tradeCurrency = parts[0];
            ok.baseCurrency = parts[1];
        }

        // 科学计数法转为普通数字
        function toPlainNumber(value) {

            var e;

            if (Math.abs(value) < 1.0) {
                e = parseInt(value.toString().split('e-')[1]);
                if (e) {
                    value *= Math.pow(10, e - 1);
                    value = '0.' + (new Array(e)).join('0') + value.toString().substring(2);
                }
            } else {
                e = parseInt(value.toString().split('e+')[1]);
                if (e > 20) {
                    e -= 20;
                    value /= Math.pow(10, e);
                    value += (new Array(e + 1)).join('0');
                }
            }

            return value;
        }


        intKlineData();


    }

    /**
     * 初始K线数据
     */
    function intKlineData(){
        var symbol=$.cookie('symbol');
        var languageType = '0';
        var url = BASE_PATH+"/market/" + symbol + "/kline";

        if (languageType === '0') {
            window._set_current_language("zh-cn");
        } else {
            window._set_current_language("en-us");
        }

        window._setCaptureMouseWheelDirectly(false);
        window._set_current_url(url);
        window._set_current_coin(symbol);
        window._set_money_type("cny");
        window.onPushingStarted(PushFromSpot);
    }

    exports.init=init;
});