/**
 * Created by yongwen.wang on 2017/7/25
 * 最近十笔未成交
 */

define(['backbone', 'template', 'Calculate', 'ajax'], function () {
    var Backbone = require("backbone");
    var Calculate = require("Calculate");
    var template = require('template');

    var thisView = null;      //当前模块view
    var MarketLang = null;    //多语言
    var currentSymbol = null; //当前symbol

    var $entrustList = $('#entrustList');

    var ajax = require('ajax');

    var socketObj;

    var tenNoDeal = {
        init: function (socketHolder) {

            socketObj = socketHolder;

            thisView = new this.view({
                model: new this.model()
            });
        },
        ajaxLoad: function (symbol) {
            currentSymbol = symbol;

            require(["i18n!../page/market/nls/lang"], function (lang) {
                MarketLang = lang;
                thisView.model.set("symbol", symbol);
                thisView.render();
            });
        },
        socketRefresh: function (data, symbol) {
            currentSymbol = symbol;

            // 判断当前推送数据是不是当前symbol类型
            if ((data.base + "_" + data.quote) !== currentSymbol) {
                return;
            }

            data = data.data;

            $entrustList.find('.no-data').remove();

            var prevListLength = $entrustList.find('li').length;

            var status = Number(data.status);

            if (status === 0) {    // 未成交
                var row = template('entrustRowTemplate', {
                    Calculate: Calculate,
                    MarketLang: MarketLang,
                    priceTruncate: ok.priceTruncate(currentSymbol),
                    amountTruncate: ok.amountTruncate(currentSymbol),
                    currentSymbol: currentSymbol,
                    data: data
                });
                var rows = $entrustList.find('li');

                // 删除最后一条
                if (rows.length >= 10) {
                    rows.last().remove();
                }

                // 添加到第一条
                $entrustList.prepend(row);
            } else if (status === 1 || status === 3) {  // 部分成交 或 取消中
                var orderId = data.id;
                var orderRow = $entrustList.find('#entrust' + orderId);

                var price = data.orderType == 0 ? Calculate.ShowDownTruncation(data.price, ok.priceTruncate(currentSymbol)) : MarketLang.market_price;

                // 更改未成交数量
                orderRow.find('.tree-price').html(price + '<br/>' + Calculate.ShowDownTruncation(data.size - data.filledSize, ok.amountTruncate(currentSymbol)));

                var text = status === 1 ? MarketLang.trade_entrust_ten_partfulfilled : MarketLang.trade_entrust_ten_cancelling;

                // 更改状态文字
                orderRow.find('.tree-status').html(text);
            } else if (status === 2 || status === -1) {    // 已成交 或 已取消

                // 直接从列表中删除
                $entrustList.find('#entrust' + data.id).remove();
            }

            var currListLength = $entrustList.find('li').length;

            if (prevListLength === 10 && currListLength === 9) {
                setTimeout(function () {
                    tenNoDeal.ajaxLoad(symbol);
                }, 500);
            }

            if (currListLength === 0) {
                $entrustList.html('<li class="no-data" style="text-align:center;">' + MarketLang.trade_entrust_ten_yhnoo + '</li>');
            }
        },
        view: Backbone.View.extend({
            el: $('#lastEntrusts'),
            events: {
                "click .operation": "cancelOrder"
            },
            initialize: function () {
                this.model.bind('change:data', this.updateTenNoDeal, this);
            },
            render: function () {
                this.model.fetch();
            },
            updateTenNoDeal: function () {
                var data = this.model.get("data").orders;
                tenNoDeal.entrustTemplate(data);
            },
            cancelOrder: function (e) {
                var orderId = jQuery(e.target).attr("data-id");
                ajax.cancelOrder({
                    opt: {
                        orderId: orderId,
                        symbol: currentSymbol
                    },
                    success: function (data) {
                        if (!socketObj.isOnline) {
                            tenNoDeal.ajaxLoad(currentSymbol);
                        }
                    },
                    fail: function (err) {
                        console.log("err", err);
                    }
                })
            }
        }),
        entrustTemplate: function (data) {

            thisView.$el.find('#entrustLink').attr('href', '/spot/trade/spotEntrust.do?symbol=' + currentSymbol);

            thisView.$el.find('#entrustList').html(template('entrustTemplate', {
                Calculate: Calculate,
                MarketLang: MarketLang,
                priceTruncate: ok.priceTruncate(currentSymbol),
                amountTruncate: ok.amountTruncate(currentSymbol),
                currentSymbol: currentSymbol,
                orders: data
            }));
        },
        model: Backbone.Model.extend({
            defaults: {
                data: {}
            },
            sync: function () {
                var thisModel = this;
                var symbol = this.get("symbol");

                ajax.getUnsettledOrders({
                    data: {
                        page: 1,
                        perPage: 10,
                        symbol: symbol
                    },
                    success: function (data) {

                        var orders = data.orders;

                        // 过滤掉已撤销的和已成交的
                        for (var i = 0; i < orders.length;) {
                            if (orders[i].status === -1 || orders[i].status === 2) {
                                orders.splice(i, 1);
                                continue;
                            }
                            i++;
                        }

                        thisModel.set("data", data);

                        // 强制刷新
                        thisView.updateTenNoDeal();
                    },
                    fail: function (err) {
                        console.log("err", err);
                    }
                });
            }
        })
    };

    return tenNoDeal;

});