require.config({
    baseUrl: PRE_URL + '/js/lib',
    config: {
        i18n: {
            locale: ok.lang
        }
    },
    paths: {
        "jquery": 'jquery',
        'template': 'template-web',
        'backbone': 'backbone',
        'cookie': 'jquery.cookie',
        'DepthMerge': '../Tools/DepthMerge',
        'Calculate': '../Tools/Calculate',
        'Prototype': '../Tools/Prototype',
        'tradeSliderBar': '../component/tradeSliderBar',
        'common': '../common',
        'websocket': "../ws/websocket",
        'trade': '../page/market/trade',
        'depth': '../page/market/depth',
        'deals': '../page/market/deals',
        'entrusts': '../page/market/entrusts',
        'ajax': "../Tools/ajax"
    },
    shim: {
        cookie: {
            deps: ["jquery"]
        },
        pagination: {
            deps: ["jquery"]
        }
    }
});

// 国际化
require(["i18n!../page/market/nls/lang"], function (lang) {
    window.Lang = lang;
});

function getLang(key) {
    return Lang[key] || '';
}

require(["jquery", 'template', 'backbone', 'common', 'websocket', 'DepthMerge', 'Calculate', 'Prototype', 'cookie', 'trade', 'depth', 'deals', 'entrusts', 'ajax'], function () {

    var ajax = require('ajax');

    var Backbone = require('backbone');
    var template = require('template');
    var Calculate = require('Calculate');
    var WebSocketUtil = require('websocket');
    var DepthMerge = require('DepthMerge');

    var trade = require('trade');
    var depth = require('depth');
    var deals = require('deals');
    var entrusts = require('entrusts');
    var $ = require('jquery');

    var marketWebSocket = new WebSocketUtil();

    var socketHolder = {
        isOnline: false,
        webSocket: marketWebSocket
    };

    trade.init();
    deals.init();

    depth.init(socketHolder, onDepthDataChange);
    entrusts.init(socketHolder);

    // 点击深度
    depth.onBuyDepthClick(function (data) {
        if (!isLogin) return;
        trade.updateSellFormByDepth(data);
    });
    depth.onSellDepthClick(function (data) {
        if (!isLogin) return;
        trade.updateBuyFormByDepth(data);
    });

    // 加载资金密码配置
    window.loadTradePwdConfig = function () {
        // 获取交易密码配置
        ajax.getPwdConfig({
            success: function (data) {

                var needPwd = data === true;

                $('#isopen').val(needPwd ? 1 : 0);

                if (needPwd) {
                    $('#buyPwdWrapper').show();
                    $('#sellPwdWrapper').show();
                } else {
                    $('#buyPwdWrapper').hide();
                    $('#sellPwdWrapper').hide();
                }
            },
            fail: function (msg) {
                console.log('get trade password failed:', msg);
            }
        });
    };

    // 获取资金密码配置
    if (isLogin) {
        window.loadTradePwdConfig();
    }

    var onlineSymbols = [];

    /**
     * KLine
     * @type {string}
     */
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

        //console.log('kline period:', newPeriodType);

        if (period !== '') {
            socketHolder.webSocket.stop(socketHolder.webSocket.Utils.getKline({symbol: ok.symbol, period: period}));
        }

        period = type;
        lastPeriodType = newPeriodType;

        sendKline(period);

        //点击分时埋点记录
        var eventId = "";
        switch (newPeriodType) {
            case '1min':
                eventId = "click_spot_k_1mins_charts";
                break;
            case '3min':
                eventId = "click_spot_k_3mins_charts";
                break;
            case '5min':
                eventId = "click_spot_k_5mins_charts";
                break;
            case '15min':
                eventId = "click_spot_k_15mins_charts";
                break;
            case '30min':
                eventId = "click_spot_k_30mins_charts";
                break;
            case '1hour':
                eventId = "click_spot_k_1hour_charts";
                break;
            case '2hour':
                eventId = "click_spot_k_2hour_charts";
                break;
            case '4hour':
                eventId = "click_spot_k_4hour_charts";
                break;
            case '6hour':
                eventId = "click_spot_k_6hour_charts";
                break;
            case '12hour':
                eventId = "click_spot_k_12hour_charts";
                break;
            case 'day':
                eventId = "click_spot_k_day_charts";
                break;
            case 'week':
                eventId = "click_spot_k_week_charts";
                break;
        }
        if (eventId !== "") {
            okLog.eventLog({
                platform: 2,
                eventId: eventId
            });
        }
    }

    window.PushFromSpot = PushFromSpot;

    function sendKline(period) {
        if (!socketHolder.isOnline) {

            setTimeout(function () {
                sendKline(period);
            }, 500);

            return;
        }

        socketHolder.webSocket.send(socketHolder.webSocket.Utils.getKline({
            symbol: ok.symbol,
            period: period
        }), function (data) {

            var kLineFrame = jQuery("#kline_iframe")[0];

            var convertedData = getKlineData(data);

            kLineFrame && kLineFrame.contentWindow && kLineFrame.contentWindow.onPushingResponse && kLineFrame.contentWindow.onPushingResponse(marketFrom, type, coinVol, convertedData);
        });

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
        var parts = ok.symbol.split("_");

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

    /**
     * Model模型
     */
    var Model = Backbone.Model.extend({
        defaults: {
            tickers: {},
            assets: {}
        },
        sync: function () {
            var _this = this;

            if (!$.isEmptyObject(ok.productsOnlineObj)) {
                // 获取行情信息
                ajax.getMarketTickers({
                    success: function (data) {
                        if (!data || !data.length) {
                            return;
                        }
                        for (var currencyId in ok.productsOnlineObj) {
                            if (ok.productsOnlineObj.hasOwnProperty(currencyId)) {
                                for (var i = 0; i < data.length; i++) {
                                    if (ok.productsOnlineObj[currencyId].toLowerCase() === data[i].symbol.toLowerCase()) {
                                        ok.allTickersObj[ok.productsOnlineObj[currencyId]] = data[i];
                                        // 新建数据对象 触发更新
                                        // var newAllTickersObj = {};
                                        // for (var symbolKey in ok.allTickersObj) {
                                        //     if (ok.allTickersObj.hasOwnProperty(symbolKey)) {
                                        //         newAllTickersObj[symbolKey] = ok.allTickersObj[symbolKey];
                                        //     }
                                        // }
                                        // _this.set('tickers', newAllTickersObj);
                                        // break;
                                    }
                                }
                            }
                        }
                        isHasFetchAllTickers = true;
                        _this.set('tickers', ok.allTickersObj);
                    },
                    fail: $.proxy(function (msg, response, xhr) {
                        _this.set('tickers', {});
                    }, this)
                });

                // 当前币对存在时加载成交记录
                if (ok.symbol) {
                    depth.ajaxLoad(ok.symbol);
                    deals.ajaxLoad(ok.symbol);
                }

                if (!isLogin) {
                    $('#canUseCny').html('0.00000000');
                    $('#canBuyETC').html('0.00000000');
                    $('#canSellETC').html('0.00000000');
                    $('#canGetCny').html('0.00000000');

                    $('#entrustList').html('<li class="no-data" style="text-align:center;">' + getLang('trade_entrust_ten_yhnoo') + '</li>');

                    // 拆解币对
                    var baseCurrency = ok.symbol.split("_")[1];
                    var tradeCurrency = ok.symbol.split("_")[0];

                    // 显示当前交易币种
                    $(".base-currency").html(baseCurrency.toUpperCase());
                    $(".trade-currency").html(tradeCurrency.toUpperCase());

                    $('#sellMarketMoney').attr('class', tradeCurrency + '-icon spot-trade-type');

                    return;
                }

                // 当前币对存在时加载十笔未成交记录
                if (ok.symbol) {
                    entrusts.ajaxLoad(ok.symbol);
                }

                // 资产信息
                ajax.getUserAccounts({
                    success: function (data) {
                        if (!data || !data.length) {
                            return;
                        }

                        ok.allAssetsObj = ajax.Tools.transferAssets(data);

                        _this.set('assets', ok.allAssetsObj);
                    },
                    fail: function () {
                        _this.set('assets', {});
                    }
                });
            }
        }
    });

    var isFirstSort = true;
    var isHasFetchAllTickers = false;

    /**
     * View 视图
     */
    var AppView = Backbone.View.extend({
        el: $('#main'),
        events: {
            // 'mouseover #chooseCoinType': 'showCoinTypeBubble',
            // 'mouseout #chooseCoinType': 'hideCoinTypeBubble',
            'mouseover #mergeDepth': 'showDepthBubble',
            'mouseout #mergeDepth': 'hideDepthBubble'
        },
        initialize: function (options) {
            this.options = options;

            // 监听模型变化
            this.model.bind('change:tickers', this.onTickerChange, this);
            this.model.bind('change:assets', this.onAssetChange, this);
            this.render();
            if (typeof allTickerPanel != 'undefined') {
                var _this = this;
                allTickerPanel.onCheckOneTickerCallBack(function (self, symbol) {
                    _this.clickChooseCoinType(symbol);
                });
            }
        },
        render: function () {
            var _this = this;

            // 更新，但不依赖
            ajax.getMarketProducts({
                success: function (data) {
                    if (!data || !data.length) {
                        return;
                    }

                    // 初始化markets和products

                    ok.marketsProducts = {};
                    ok.productsObj = {};

                    for (var i = 0; i < data.length; i++) {

                        ok.marketsProducts[data[i].symbol] = data[i];
                        ok.productsObj[data[i].productId] = data[i].symbol;

                        if (data[i].online === 1) {
                            ok.productsOnlineObj[data[i].productId] = data[i].symbol;
                            onlineSymbols.push(data[i].symbol);
                        }
                    }

                    if (!ok.symbol) {

                        var cookieSymbol = $.cookie('symbol');

                        // if (!cookieSymbol || (cookieSymbol && ok.lang.toLowerCase().indexOf('cn') > -1 && cookieSymbol.indexOf('usdt') > -1)) {
                        //     cookieSymbol = 'bcc_btc';
                        // }

                        if (onlineSymbols.indexOf(cookieSymbol) === -1) {
                            cookieSymbol = null;
                        }

                        ok.symbol = cookieSymbol || onlineSymbols[0];

                        $.cookie('symbol', ok.symbol, {path: '/'});

                        updateBaseAndTradeCurrency();

                        window.depthClickAutoTrade._getDepthLevels(ok.symbol);  // 获取深度列表

                        changeBannerDepositLink(ok.symbol);
                    }

                    if (!$.isEmptyObject(ok.productsOnlineObj) && ok.symbol) {
                        _this.model.fetch();
                        setInterval(function () {
                            _this._getAllTickers();
                        }, 20 * 1000);
                        _this.startWebsocket();
                    }
                }
            });
        },
        //推送
        startWebsocket: function () {
            var _this = this;

            this.webSocket = marketWebSocket;

            this.webSocket.addSuccCallBackFun(function () {
                socketHolder.isOnline = true;
                shouldStopKLinePushingWhenSocketError = true;

                //清空depth深度缓存
                DepthMerge.sortPushDepthData(null, ok.symbol);

                // 登录
                var userId = $.cookie("coin_session_user_id");

                if (isLogin && userId) {
                    _this.webSocket.login(userId);
                }

                // 订阅 ticker 和 资产
                // 当前币对行情
                _this.webSocket.send(_this.webSocket.Utils.getTicker({symbol: ok.symbol}), function (data) {

                    var symbol = '';
                    // TODO 第一次返回对象，后端hack的原因
                    if ($.isPlainObject(data)) {
                        ok.allTickersObj[data.symbol] = data;
                        symbol = data.symbol;
                    } else if (data[0]) {
                        ok.allTickersObj[data[0].symbol] = data[0];
                        symbol = data[0].symbol;
                    }

                    // 刷新行情
                    var ticker = {};
                    ticker[symbol] = ok.allTickersObj[symbol];
                    _this.socketRefreshTicker(ticker);

                    // 更新顶部ticker行情
                    var arr = $.isPlainObject(data) ? [data] : (data[0] ? data : []);
                    bannerTicker.refreshDataSource(arr);
                });

                if (isLogin) {
                    // 当前币对的资产信息
                    _this.webSocket.send(_this.webSocket.Utils.getUserBalance({symbol: ok.symbol}), function (data) {

                        if (!$.isEmptyObject(data) && !$.isEmptyObject(ok.currenciesObj)) {
                            var currencyObj = ok.currenciesObj[data.currencyId];

                            if (currencyObj) {
                                ok.allAssetsObj[currencyObj.symbol.toLowerCase()] = $.extend(true, {}, {
                                    currency: currencyObj.symbol,
                                    sign: currencyObj.sign
                                }, data);
                            }
                        }

                        _this._updateTickerInfo(ok.symbol);
                        _this._updateAssetsInfo(ok.symbol);
                    });
                }


                // 订阅200档委托深度列表数据
                function websockietRefreshDepth200() {
                    _this.webSocket.stop(_this.webSocket.Utils.getDepth({symbol: ok.symbol}), function () {
                        DepthMerge.sortPushDepthData(null, ok.symbol);
                    });
                    _this.webSocket.send(_this.webSocket.Utils.getDepth({symbol: ok.symbol}), function (data) {
                        // 判断当前推送数据是不是当前symbol类型
                        if ((data.base + "_" + data.quote) !== ok.symbol) {
                            return;
                        }
                        data = data.data;

                        var newMergeType = $.cookie(ok.symbol + "deptMerge_stock");

                        //转换数据格式
                        var d = depthDomOperate._handleData(data);
                        //增量-处理depth排序x
                        d = DepthMerge.sortPushDepthData(d, ok.symbol);
                        //深度合并处理
                        if (newMergeType && newMergeType != 0.00000001) {
                            d = DepthMerge.mergeDepthSize20(d, newMergeType);
                        }
                        d = DepthMerge.amountOverDepth001(d);
                        //dom渲染
                        _this._getMarketDepthPush(d, ok.symbol);

                        //获取深度档位
                        // window.depthClickAutoTrade._getDepthLevels(ok.symbol);

                    });
                }

                websockietRefreshDepth200();
                // setInterval(function () {
                //     websockietRefreshDepth200();
                // }, 5 * 60 * 1000);

                // 订阅最新成交记录
                _this.webSocket.send(_this.webSocket.Utils.getDeal({symbol: ok.symbol}), function (data) {

                    deals.socketRefresh(data, ok.symbol);
                });

                // 登录时订阅用户订单
                if (isLogin) {

                    // 用户订单
                    _this.webSocket.send(_this.webSocket.Utils.getOrder({symbol: ok.symbol}), function (data) {
                        entrusts.socketRefresh(data, ok.symbol);
                    });
                }
            });

            // 连接失败后执行ajax请求
            this.webSocket.addErrCallBackFun(function () {

                socketHolder.isOnline = false;

                //清空depth深度缓存
                DepthMerge.sortPushDepthData(null, ok.symbol);

                // 执行一次数据请求
                _this.model.fetch();

                //更新深度
                // _this._getMarketDepth(ok.symbol);

                var kLineFrame = jQuery("#kline_iframe")[0];

                // 停掉一次
                if (shouldStopKLinePushingWhenSocketError) {
                    var canStop = kLineFrame && kLineFrame.contentWindow && kLineFrame.contentWindow.onPushingStop;
                    if (canStop) {
                        kLineFrame.contentWindow.onPushingStop();   //停止kline推送 开启轮训
                        shouldStopKLinePushingWhenSocketError = false;
                    }
                }
                // 更新顶部banner行情
                bannerTicker.getTickersByApi();
            });

            this.webSocket.connection();
        },

        // 行情更新时渲染
        onTickerChange: function () {
            //从发服务器端返回数据
            var tickers = this.model.get("tickers");

            if (!tickers || $.isEmptyObject(tickers)) {
                return;
            }

            // 设置symbol
            for (var symbol in tickers) {
                if (ok.symbol) {
                    break;
                }

                ok.symbol = symbol;
                $.cookie('symbol', ok.symbol, {path: '/'});

                updateBaseAndTradeCurrency();
            }

            // 渲染到页面中

            // 1，币种对选择列表展示
            this._updateSymbolPairList();

            // 2，ticker区域展示
            this._updateTickerInfo(ok.symbol);

            if (isLogin) {
                // 3. 更新资产信息-可能资产比ticker先回来
                this._updateAssetsInfo(ok.symbol);

                // 4. 更新十笔未成交
                // this._getEntrustList(ok.symbol);

                // 5. 更新成交记录列表
                // this._getDealList(ok.symbol);
            }

            // 5. 更新买卖深度
            //this._getMarketDepth(ok.symbol);

            // 6. 获取深度档位
            //window.depthClickAutoTrade._getDepthLevels(ok.symbol);
        },

        // 资产更新时渲染
        onAssetChange: function () {

            //console.log('assets changed.');

            //从发服务器端返回数据
            var assets = this.model.get("assets");

            if (!assets || $.isEmptyObject(assets)) {
                return;
            }

            // 先更新最新成交价，然后更新资产
            this._updateTickerInfo(ok.symbol);
            this._updateTradeCenterView(ok.symbol);

            // 更新资产信息
            this._updateAssetsInfo(ok.symbol);
        },

        // webSocket更新行情
        socketRefreshTicker: function (d) {

            // 1，币种对选择列表展示
            this._updateSymbolPairList(d);

            // 2，ticker区域展示
            this._updateTickerInfo(ok.symbol);

            // 3，资产信息更新
            this._updateAssetsInfo(ok.symbol);
        },

        // 更新行情信息
        _updateTickerInfo: function (symbol) {
            if (!symbol) {
                return;
            }

            var o = ok.allTickersObj[symbol];

            if (!$.isEmptyObject(o)) {
                //给title添加行情
                var tradeCurr = symbol.split('_')[0].toUpperCase();
                var baseCurr = symbol.split('_')[1].toUpperCase();
                var lastPrice = Calculate.ShowDownTruncation(o.last, ok.priceTruncate(ok.symbol)) + ' ' + baseCurr;
                document.title = lastPrice + '-' + symbol.toUpperCase().replace(/_/g, '/') + $('#titleContent').val();

                if (o['changePercentage'].indexOf('+') >= 0) {
                    this.$el.find('#changePercentage').removeClass('red').addClass('green');
                    this.$el.find('#last span').removeClass('red').addClass('green');
                    this.$el.find('#last em').removeClass('arrow-down').addClass('arrow-up');
                }
                if (o['changePercentage'].indexOf('-') >= 0) {
                    this.$el.find('#changePercentage').removeClass('green').addClass('red');
                    this.$el.find('#last span').removeClass('green').addClass('red');
                    this.$el.find('#last em').removeClass('arrow-up').addClass('arrow-down');
                }

                var dayVolume = +o.volume >= 10 ? Calculate.DownTruncation(o.volume, 0) : Calculate.downTruncation(o.volume, ok.amountTruncate(ok.symbol));

                this.$el.find('#coinType').text(ok.symbolToUpper(symbol));
                this.$el.find('#last span').text(lastPrice); // 计价币种
                this.$el.find('#changePercentage').text(o.changePercentage);
                this.$el.find('#volume').text(dayVolume + ' ' + symbol.split('_')[0].toUpperCase());// 交易币种

                this.$el.find('#24hrHigh').text(Calculate.ShowDownTruncation(o.dayHigh, ok.priceTruncate(ok.symbol)) + ' ' + symbol.split('_')[1].toUpperCase());
                this.$el.find('#24hrLow').text(Calculate.ShowDownTruncation(o.dayLow, ok.priceTruncate(ok.symbol)) + ' ' + symbol.split('_')[1].toUpperCase());

                // 五档委托 最新成交价
                $("#tradeLastPrice").html(Calculate.ShowDownTruncation(o.last, ok.priceTruncate(ok.symbol)));

                // 隐藏域
                $("#nowPrice").val(o.last);
                $("#nowSell1").val(o.sell);
                $("#nowBuy1").val(o.buy);

                // 限价单 价格输入框
                this._updateTradePriceInput(o, false);
            }
        },

        // 更新交易数据视图
        _updateTradeCenterView: function (symbol) {
            if (!symbol) {
                return;
            }

            // 拆解币对
            var baseCurrency = symbol.split("_")[1];
            var tradeCurrency = symbol.split("_")[0];

            // 显示当前交易币种
            $(".base-currency").html(baseCurrency.toUpperCase());
            $(".trade-currency").html(tradeCurrency.toUpperCase());

            $('#sellMarketMoney').attr('class', tradeCurrency + '-icon spot-trade-type');
        },

        // 更新价格输入框值
        _updateTradePriceInput: function (price, isForce) {

            var $buyPrice = $('#buyPrice');
            var $sellPrice = $('#sellPrice');

            if ($buyPrice.attr('data-init') === 'true') return;

            var buyPrice = Number($buyPrice.val());
            var sellPrice = Number($sellPrice.val());

            // 为空的时候才更新
            if (!buyPrice) {
                $buyPrice.val(price.sell);
            }
            if (!sellPrice) {
                $sellPrice.val(price.buy);
            }

            if (isForce) {
                $buyPrice.val(price.sell);
                $sellPrice.val(price.buy);
            }

            $buyPrice.attr('data-init', 'true');
        },
        // 更新币对列表
        _updateSymbolPairList: function (data) {
            var allTickersObj = ok.allTickersObj;
            if ($.isEmptyObject(data) && $.isEmptyObject(allTickersObj)) {
                return;
            }
            if (typeof allTickerPanel != 'undefined') {
                var tickers = typeof data !== 'undefined' ? data : allTickersObj;
                allTickerPanel.updateTickerPanel(allTickersObj);
            }
            if (ok.symbol.indexOf('_') > -1 && isHasFetchAllTickers && isFirstSort) {
                isFirstSort = false;
                var baseCurr = ok.symbol.split('_')[1];
                if (typeof allTickerPanel != 'undefined') {
                    allTickerPanel.setMarketChk(baseCurr);
                    allTickerPanel.setSort(false);
                }
            }
        },

        // 更新资产信息
        _updateAssetsInfo: function (symbol) {
            if (!symbol) {
                return;
            }

            var baseCurrency = symbol.split("_")[1];
            var tradeCurrency = symbol.split("_")[0];

            // 更新交易区域余额可买可卖

            if (!$.isEmptyObject(ok.allAssetsObj) && !$.isEmptyObject(ok.allAssetsObj[baseCurrency]) && !$.isEmptyObject(ok.allAssetsObj[tradeCurrency])) {

                // 取基准货币和交易货币数据
                var baseCurrencyObj = ok.allAssetsObj[baseCurrency];
                var tradeCurrencyObj = ok.allAssetsObj[tradeCurrency];

                var baseCurrencyAvailable = Calculate.InputDownTruncation(baseCurrencyObj.available, ok.priceTruncate(ok.symbol));   // 后端原始数据，不产生逗号
                var tradeCurrencyAvailable = Calculate.InputDownTruncation(tradeCurrencyObj.available, ok.amountTruncate(ok.symbol)); // 后端原始数据，不产生逗号

                var priceTruncate = ok.priceTruncate(symbol);
                var amountTruncate = ok.amountTruncate(symbol);

                // 可买基准币余额
                $("#canUseCny").html(baseCurrencyObj.sign + Numbers.formatCurrency(baseCurrencyObj.available, priceTruncate));

                // 可卖数字货币数量
                $("#canSellETC").html(tradeCurrencyObj.sign + Numbers.formatCurrency(tradeCurrencyObj.available, priceTruncate));

                var nowPrice = +$("#nowPrice").val();

                // 买入时： 根据可买法币余额 / 最新成交价 = 数字货币
                if (nowPrice > 0) {
                    var canBuy = toPlainNumber(Calculate.accDiv(baseCurrencyObj.available, nowPrice));
                    $("#canBuyETC").html(Numbers.formatCurrency(canBuy, amountTruncate))
                }

                // 卖出时： 根据可买数字货币数量 * 最新成交价 = 法币
                if (nowPrice > 0) {
                    var canSell = toPlainNumber(Calculate.accMul(tradeCurrencyObj.available, nowPrice));
                    $("#canGetCny").html(Numbers.formatCurrency(canSell, priceTruncate));
                }

                // banner 总资产
                $(".banner-base-currency").html(baseCurrency.toUpperCase());
                $(".banner-trade-currency").html(tradeCurrency.toUpperCase());

                $("#bannerBaseCurrencyAvailable").html(Calculate.ShowDownTruncation(baseCurrencyAvailable, priceTruncate));
                $("#bannerTradeCurrencyAvailable").html(Calculate.ShowDownTruncation(tradeCurrencyAvailable, amountTruncate));
                $("#bannerBaseCurrencyHold").html(Calculate.ShowDownTruncation(baseCurrencyObj.hold, priceTruncate));
                $("#bannerTradeCurrencyHold").html(Calculate.ShowDownTruncation(tradeCurrencyObj.hold, amountTruncate));
            }
        },

        // 更新十笔未成交记录
        _getEntrustList: function (symbol) {
            // 通过entrusts模块加载
            entrusts.ajaxLoad(symbol);
        },

        //更新所有ticker
        _getAllTickers: function () {
            var _this = this;
            if (!$.isEmptyObject(ok.productsOnlineObj)) {
                // 获取行情信息
                ajax.getMarketTickers({
                    success: function (data) {
                        if (!data || !data.length) {
                            return;
                        }
                        for (var currencyId in ok.productsOnlineObj) {
                            if (ok.productsOnlineObj.hasOwnProperty(currencyId)) {
                                for (var i = 0; i < data.length; i++) {
                                    if (ok.productsOnlineObj[currencyId].toLowerCase() === data[i].symbol.toLowerCase()) {
                                        ok.allTickersObj[ok.productsOnlineObj[currencyId]] = data[i];
                                    }
                                }
                            }
                        }

                        isHasFetchAllTickers = true;

                        // 1，币种对选择列表展示
                        _this._updateSymbolPairList();

                        // 2，ticker区域展示
                        _this._updateTickerInfo(ok.symbol);

                        var arr = [];
                        for (var key in ok.allTickersObj) {
                            if (ok.allTickersObj.hasOwnProperty(key)) {
                                arr.push(ok.allTickersObj[key])
                            }
                        }
                        //更新banner
                        bannerTicker.refreshDataSource(arr);
                    }
                });
            }
        },

        // 更新已成交记录
        _getDealList: function (symbol) {
            // 通过deals模块加载
            deals.ajaxLoad(symbol);
        },

        // 更新买卖深度
        _getMarketDepth: function (symbol) {
            // 通过depth模块加载
            depth.ajaxLoad(symbol);
        },
        _getMarketDepthPush: function (data, symbol) {
            // 通过depth模块加载
            depth.refresh(data, symbol);
        },

        // --------------------币对事件------------------

        // 币种气泡显影
        // showCoinTypeBubble: function (e) {
        //     var tickers = this.model.get("tickers");
        //     if (Object(tickers).length === 0) {
        //         return;
        //     }
        //     if (typeof allTickerPanel !== 'undefined') {
        //         allTickerPanel.show();
        //     }
        //     return false;
        // },
        // hideCoinTypeBubble: function (e) {
        //     if (typeof allTickerPanel !== 'undefined') {
        //         allTickerPanel.hide();
        //     }
        //     return false;
        // },
        // 深度气泡显影
        showDepthBubble: function () {
            this.$el.find('#depthContainer').show();
            this.$el.find('#depthArrow').removeClass('arrow-down').addClass('arrow-up');
        },
        hideDepthBubble: function () {
            this.$el.find('#depthContainer').hide();
            this.$el.find('#depthArrow').removeClass('arrow-up').addClass('arrow-down');
        },

        // 选择新币种处理: 重新加载各项数据
        clickChooseCoinType: function (symbol) {
            if (!symbol || symbol === ok.symbol) {
                return;
            }

            // 切换币种时清空买卖表单信息
            trade.resetAllForms();

            // 清除标记 更新价行情
            $('#buyPrice').removeAttr('data-init');

            var _this = this;

            // 更新200档买卖深度
            if (socketHolder.isOnline) {
                (function (oldSymbol, newSymbol) {

                    //停掉旧币种的ticker
                    _this.webSocket.stop(_this.webSocket.Utils.getTicker({symbol: oldSymbol}), function (data) {
                    });

                    _this.webSocket.send(_this.webSocket.Utils.getTicker({symbol: newSymbol}), function (data) {

                        var symbol = '';
                        // TODO 第一次返回对象，后端hack的原因
                        if ($.isPlainObject(data)) {
                            ok.allTickersObj[data.symbol] = data;
                            symbol = data.symbol;
                        } else if (data[0]) {
                            ok.allTickersObj[data[0].symbol] = data[0];
                            symbol = data[0].symbol;
                        }

                        // 刷新行情
                        var ticker = {};
                        ticker[symbol] = ok.allTickersObj[symbol];
                        _this.socketRefreshTicker(ticker);

                        // 更新顶部ticker行情
                        var arr = $.isPlainObject(data) ? [data] : (data[0] ? data : []);
                        bannerTicker.refreshDataSource(arr);
                    });

                    // 停掉旧币种的K线
                    socketHolder.webSocket.stop(socketHolder.webSocket.Utils.getKline({
                        symbol: oldSymbol,
                        period: period
                    }), function () {
                    });

                    // 停掉旧币种的200档委托
                    socketHolder.webSocket.stop(socketHolder.webSocket.Utils.getDepth({symbol: oldSymbol}), function (data) {
                    });

                    var newMergeType = $.cookie(oldSymbol + "deptMerge_stock");

                    // 订阅新币种的200档委托
                    socketHolder.webSocket.send(socketHolder.webSocket.Utils.getDepth({symbol: newSymbol}), function (data) {
                        // 判断当前推送数据是不是当前symbol类型
                        if ((data.base + "_" + data.quote) !== newSymbol) {
                            return;
                        }
                        data = data.data;

                        //console.log('now-symbol:', newSymbol);
                        //console.log(newSymbol+'-depth:', data);

                        var d = depthDomOperate._handleData(data);
                        d = DepthMerge.sortPushDepthData(d, newSymbol);

                        if (newMergeType && newMergeType != 0.00000001) {
                            d = DepthMerge.mergeDepthSize20(d, newMergeType);
                        }
                        d = DepthMerge.amountOverDepth001(d);
                        _this._getMarketDepthPush(d, newSymbol);
                    });

                    // 停掉旧币种的最新成交记录
                    _this.webSocket.stop(_this.webSocket.Utils.getDeal({symbol: oldSymbol}), function (data) {
                    });

                    // 订阅新币种的最新成交记录
                    _this.webSocket.send(_this.webSocket.Utils.getDeal({symbol: newSymbol}), function (data) {

                        deals.socketRefresh(data, newSymbol);
                    });


                    if (isLogin) {

                        // 停掉旧币种资产信息
                        _this.webSocket.stop(_this.webSocket.Utils.getUserBalance({symbol: oldSymbol}), function (data) {
                        });

                        // 订阅新币种的资产信息
                        _this.webSocket.send(_this.webSocket.Utils.getUserBalance({symbol: newSymbol}), function (data) {

                            if (!$.isEmptyObject(data) && !$.isEmptyObject(ok.currenciesObj)) {
                                var currencyObj = ok.currenciesObj[data.currencyId];

                                if (currencyObj) {
                                    ok.allAssetsObj[currencyObj.symbol.toLowerCase()] = $.extend(true, {}, {
                                        currency: currencyObj.symbol,
                                        sign: currencyObj.sign
                                    }, data);
                                }
                            }

                            _this._updateTickerInfo(ok.symbol);
                            _this._updateAssetsInfo(ok.symbol);
                        });


                        // 停止旧挂单
                        socketHolder.webSocket.stop(socketHolder.webSocket.Utils.getOrder({symbol: oldSymbol}), function (data) {
                        });
                        // 订阅新币种挂单
                        socketHolder.webSocket.send(socketHolder.webSocket.Utils.getOrder({symbol: newSymbol}), function (data) {

                            entrusts.socketRefresh(data, newSymbol);
                        });
                    }

                })(ok.symbol, symbol);
            } else {
                // 200档委托
                _this._getMarketDepth(symbol);
            }

            // 设置新的symbol
            ok.symbol = symbol;
            $.cookie('symbol', ok.symbol, {path: '/'});

            updateBaseAndTradeCurrency();

            changeBannerDepositLink(ok.symbol);

            // 更改K线
            period = '';
            lastPeriodType = '';

            $('#kline_iframe').remove();
            $('#marketImageNew').prepend('<iframe id="kline_iframe" onload="iframe_onload();" style="border:0;width:100%;height:100%;" src="' + $('#mainUrl').val() + '/spot/market/kline.do"></iframe>');

            if (isLogin) {
                // 资产信息更新
                this._updateAssetsInfo(ok.symbol);

                // 获取新币种十笔未成交
                this._getEntrustList(ok.symbol);
            }

            // 获取新币种最新交易记录
            this._getDealList(ok.symbol);

            // ticker区域展示
            this._updateTickerInfo(ok.symbol);

            // 交易中心输入区域展示
            this._updateTradeCenterView(ok.symbol);

            // 获取深度档位列表
            window.depthClickAutoTrade._getDepthLevels(ok.symbol);

            //切换币种埋点记录
            var eventId = "";
            switch (symbol) {
                case 'bcc_btc':
                    eventId = "click_coinselect_bcc/btc_charts";
                    break;
                case 'ltc_btc':
                    eventId = "click_coinselect_ltc/btc_charts";
                    break;
                case 'eth_btc':
                    eventId = "click_coinselect_eth/btc_charts";
                    break;
                case 'etc_btc':
                    eventId = "click_coinselect_etc/btc_charts";
                    break;
            }
            if (eventId !== "") {
                okLog.eventLog({
                    platform: 2,
                    eventId: eventId
                });
            }
        }
    });

    var model = new Model();

    new AppView({
        model: model
    });

    var common = require('common');

    common.init();
});