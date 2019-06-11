/**
 * Created by yongwen.wang on 2017/7/25
 * 最新成交
 */

define(['backbone', 'template', 'Calculate', 'Prototype', 'ajax'], function () {
    var Backbone = require("backbone");
    var template = require('template');
    var Calculate = require("Calculate");

    var ajax = require('ajax');
    var wsData = [];

    var thisView = null;        //当前模块view
    var currentSymbol = null;   //当前币种

    var tenNoDeal = {
        init: function () {
            thisView = new this.view({
                model: new this.model()
            });
        },
        ajaxLoad: function (symbol) {

            currentSymbol = symbol;

            thisView.model.set("symbol", symbol);

            thisView.render();
        },
        socketRefresh: function (data, symbol) {

            if (!data || data.length < 1) return;

            currentSymbol = symbol;

            // 处理增量
            if (wsData.length !== 0 && data.length < 60) {
                wsData = data.concat(wsData);
                //推送第一次全量，推送断了重连后第一次也是全量，通过对象的Id为key去重
                var temp = {}, arr = [];
                for (var i = 0; i < wsData.length; i++) {
                    if (!temp[wsData[i].id.toString()]) {
                        temp[wsData[i].id.toString()] = wsData[i];
                        arr.push(wsData[i]);
                    }
                }

                if (arr.length > 60) {
                    arr = arr.slice(0, 60);
                }
                wsData = arr;
            }
            else {
                wsData = data;
            }

            wsData.sort(function (d1, d2) {
                return d2.createdDate - d1.createdDate;
            });


            thisView.model.set("data", wsData);
        },
        view: Backbone.View.extend({
            el: $('#lastDeal'),
            events: {},
            initialize: function () {
                this.model.bind('change:data', this.updateTenNoDeal, this);
            },
            render: function () {
                this.model.fetch();
            },
            updateTenNoDeal: function () {
                var data = this.model.get("data");
                tenNoDeal.dealsTemplate(data);
            }
        }),
        dealsTemplate: function (data) {
            var getLocalTime = function (nS) {
                return new Date(parseInt(nS)).Format("hh:mm:ss");
            };

            var baseCoinSymbol = currentSymbol.split('_')[1].toUpperCase();
            var tradeCoinSymbol = currentSymbol.split('_')[0].toUpperCase();

            thisView.$el.find('#baseCoinSymbol').html(baseCoinSymbol);
            thisView.$el.find('#tradeCoinSymbol').html(tradeCoinSymbol);

            var $dealsBody = thisView.$el.find('#marketRecentBody');
            var $dealsBodyRows = $dealsBody.find('tr');

            var colorArr = {"1": "green", "2": "red"};

            if ($dealsBodyRows.length < 60 || data.length < $dealsBodyRows.length) {
                // 重新渲染
                $dealsBody.html(template('lastDealTemplate', {
                    getLocalTime: getLocalTime,
                    Calculate: Calculate,
                    priceTruncate: ok.priceTruncate(currentSymbol),
                    amountTruncate: ok.amountTruncate(currentSymbol),
                    deals: data
                }));
            } else {
                // 修改DOM
                $dealsBodyRows.each(function (index, row) {
                    var deal = data[index];
                    var sideColor = colorArr[deal.side];

                    var time = getLocalTime(deal.createdDate);
                    var price = Calculate.ShowDownTruncation(deal.price, ok.priceTruncate(currentSymbol));
                    var amount = Calculate.ShowDownTruncation(deal.amount, ok.amountTruncate(currentSymbol));

                    $(row).find('td:eq(0)').html(time);
                    $(row).find('td:eq(1)').html(price);
                    $(row).find('td:eq(2) span').attr('class', sideColor).html(amount);
                })

            }

        },
        model: Backbone.Model.extend({
            defaults: {
                data: {}
            },
            sync: function () {
                var thisModel = this;
                var symbol = this.get("symbol");

                ajax.getDealOrderDepth({
                    opt: {
                        symbol: symbol
                    },
                    success: function (data) {

                        if (!data || data.length < 1) return;

                        data.sort(function (d1, d2) {
                            return d2.createdDate - d1.createdDate;
                        });

                        thisModel.set("data", data);
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