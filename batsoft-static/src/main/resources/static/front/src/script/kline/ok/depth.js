define(['jquery', 'template', 'backbone', 'DepthMerge', 'Calculate', 'Prototype', 'ajax'], function () {

    var Calculate = require('Calculate');
    var DepthMerge = require('DepthMerge');
    var $ = require('jquery');

    var ajax = require('ajax');

    var thisView = null;//当前模块view
    var MarketLang = null;//多语言

    var depth_size = 200;//深度数

    var onBuyDepthClickFn;
    var onSellDepthClickFn;
    var socketObj;
    var depthDataChangeFn;

    var $v2DomMarketEntrust = jQuery("#marketEntrust");

    var $v2SellParent = $v2DomMarketEntrust.find('#depth_sell_context');
    var $v2BuyParent = $v2DomMarketEntrust.find('#depth_buy_context');
    var renderDepthTimeOut = 0;

    var marketRefreshDepth = {
        init: function (webSocketHolder, onDepthDataChangeFn) {
            thisView = new this.view({
                model: new this.model()
            });

            socketObj = webSocketHolder;
            depthDataChangeFn = onDepthDataChangeFn;
        },
        onBuyDepthClick: function (fn) {
            onBuyDepthClickFn = fn;
        },
        onSellDepthClick: function (fn) {
            onSellDepthClickFn = fn;
        },

        //ajax-load
        ajaxLoad: function (symbol) {
            require(["i18n!../page/market/nls/lang"], function (lang) {
                MarketLang = lang;
                thisView.model.set("symbol", symbol);
                thisView.render();
            });
        },

        //push-refresh
        refresh: function (data, symbol) {
            depthDataChangeFn(data);
            depthDomOperate._refreshDepthDomV2(data, true);
        },

        view: Backbone.View.extend({
            el: jQuery('#marketDepthList'),
            events: {
                // 切换深度
                'click #depthBubble li': 'chooseDepth'
            },
            initialize: function () {
                this.model.bind('change:data', this.refreshDepthDom, this);
            },
            render: function () {
                this.model.fetch();
            },
            refreshDepthDom: function () {
                //dom渲染-轮训
                var data = this.model.get("data");

                depthDomOperate._refreshDepthDomV2(data, true);//获取深度列表
                depthClickAutoTrade._getDepthLevels(ok.symbol);//获取深度档位

                depthDataChangeFn && depthDataChangeFn(data);

            },
            // 选择深度
            chooseDepth: function (e) {
                var depth = $(e.currentTarget).text();
                var _this = this;
                console.log(depth);
                this.$el.find('#depthContainer').hide();
                this.$el.find('#depth').text(depth);
                $.cookie(ok.symbol + 'deptMerge_stock', depth, {path: '/'});
                if (socketObj.isOnline == true) {

                    socketObj.webSocket.send(socketObj.webSocket.Utils.getDepth({symbol: ok.symbol}), function (data) {
                        // 判断当前推送数据是不是当前symbol类型
                        if ((data.base + "_" + data.quote) !== ok.symbol) {
                            return;
                        }
                        data = data.data;

                        var d = depthDomOperate._handleData(data);
                        d = DepthMerge.sortPushDepthData(d, ok.symbol);

                        if (depth && depth != 0.00000001) {
                            d = DepthMerge.mergeDepthSize20(d, depth);
                        }
                        d = DepthMerge.amountOverDepth001(d);
                        marketRefreshDepth.refresh(d, ok.symbol)

                    });
                } else {
                    this.model.fetch();
                }
            },
        }),
        model: Backbone.Model.extend({
            defaults: {
                data: {}
            },
            sync: function () {
                var thisModel = this;
                var symbol = this.get("symbol");
                ajax.getMarketDepth({
                    opt: {
                        symbol: symbol
                    },
                    success: function (data) {
                        var d = depthDomOperate._handleData(data);
                        // d = {bids:depthDomOperate._getBuyDepthList(d.bids),asks:depthDomOperate._getSellDepthList(d.asks)};
                        var mergeType = $.cookie(ok.symbol + 'deptMerge_stock');
                        if (mergeType && mergeType != 0.00000001) {
                            d = DepthMerge.mergeDepthSize20(d, mergeType);
                        }
                        d = DepthMerge.amountOverDepth001(d);
                        thisModel.set("data", d);
                    },
                    fail: function (err) {
                        console.log("err", err);
                        return;
                    }
                });
            }
        }),

    };


    var sortDepth = new function () {
        this.sort = function (depth) {
            depth.sort(function (a, b) {
                return a[1] - b[1];
            });
            return depth;
        };
        this.median = function (depth) {
            var i = Calculate.floor((depth.length / 3) * 2, 0);
            if (!depth[i]) return 1;
            return depth[i][1] < 1 ? 1 : depth[i][1];
        }
        this.medianUnit = function (buydepth, sellDepth, colorWidth) {
            var tmpBuy = new Array(buydepth);
            tmpBuy = tmpBuy[0];
            var tmpSell = new Array(sellDepth);
            tmpSell = tmpSell[0];
            tmpBuy = tmpBuy.concat(tmpSell);
            var result = this.median(this.sort(tmpBuy)) / colorWidth;
            tmpBuy = null;
            tmpSell = null;
            return result;
        }
        this.width = function (amount, medianUnit) {
            if (medianUnit == 0) {
                return 1;
            } else {
                var result = Calculate.round(Number(amount) / medianUnit, 0);
                if (result <= 0) {
                    return 1;
                } else if (result > 160) {
                    return 160;
                } else {
                    return result / 160 * 100;
                }
            }
        }
    };

    var depthClickAutoTrade = {
        /**
         * 深度点击事件
         */
        _buyAutoTrade: function (index) {
            var buyPriceNum = 0;
            var buyAmountNum = 0;
            var buyName = "buy";
            var buyPriceName = buyName + "PriceSpan" + index;

            buyPriceNum = Numbers.filter($v2BuyParent.find('#' + buyPriceName).html().replace(/,/g, ''));

            var buyAmountName = buyName + "AmountSpan" + index;

            buyAmountNum = Calculate.accAdd(buyAmountNum, $v2BuyParent.find('#' + buyAmountName).html().replace(/,/g, ''));

            buyAmountNum = Calculate.round(buyAmountNum, 8);

            var result = {
                price: buyPriceNum,
                amount: buyAmountNum
            };
            onBuyDepthClickFn(result);
        },

        _sellAutoTrade: function (index) {
            var sellPriceNum = 0;
            var sellAmountNum = 0;
            var sellName = "sell";
            var sellPriceName = sellName + "PriceSpan" + index;

            sellPriceNum = Numbers.filter($v2SellParent.find('#' + sellPriceName).html().replace(/,/g, ''));
            var sellAmountName = sellName + "AmountSpan" + index;

            sellAmountNum = Calculate.accAdd(sellAmountNum, $v2SellParent.find('#' + sellAmountName).html().replace(/,/g, ''));
            sellAmountNum = Calculate.round(sellAmountNum, 8);

            var result = {
                price: sellPriceNum,
                amount: sellAmountNum
            };
            onSellDepthClickFn(result);
        },

        // 获取深度档位列表
        _getDepthLevels: function (symbol) {
            var _this = this;
            var $main = jQuery("#main");
            ajax.getDepthList({
                opt: {
                    symbol: symbol
                },
                success: function (data) {
                    var depthList = data.mergeTypes.split(',');
                    var _html = [];

                    for (var i = 0; i < depthList.length; i++) {
                        _html.push('<li>' + depthList[i] + '</li>');
                    }

                    $main.find('#depthBubble').html(_html.join(''));

                    var depth = $.cookie(symbol + 'deptMerge_stock') || $main.find('#depthBubble li').eq(0).text();

                    $main.find('#depth').text(depth);
                },
                fail: function () {
                    var depthList = ['0.00000001', '0.00001', '0.001'];
                    var _html = [];

                    for (var i = 0; i < depthList.length; i++) {
                        _html.push('<li>' + depthList[i] + '</li>');
                    }

                    $main.find('#depthMergeLevel').html(_html.join(''));

                    var depth = $.cookie(symbol + 'deptMerge_stock') || $main.find('#depthBubble li').eq(0).text();

                    $main.find('#depth').text(depth);
                }
            })
        },

    }

    var depthDomOperate = {

        _sort: function (data) {
            for (var i = 0; i < data.length; ++i) {
                for (var j = i + 1; j < data.length; ++j) {
                    if (+data[i][0] < +data[j][0]) {
                        var tmp = data[i];
                        data[i] = data[j];
                        data[j] = tmp;
                    }
                }
            }
        },

        /**
         * 获取
         */
        _getAutoTrade: function (event) {
            event.target.innerHTML;
        },

        /**
         * 数据处理
         */
        _handleData: function (data) {
            var bids = data.bids;
            var asks = data.asks;
            var buyDepthList = [];
            var sellDepthList = [];
            if (!bids) {
                bids = [];
            } else {
                for (i = 0; i < bids.length; i++) {
                    var buyDepth = [];
                    buyDepth[0] = bids[i].price;
                    buyDepth[1] = bids[i].totalSize;
                    buyDepth[2] = 0;
                    buyDepthList.push(buyDepth);
                }
            }
            if (!asks) {
                asks = [];
            } else {
                for (i = 0; i < asks.length; i++) {
                    var sellDepth = [];
                    sellDepth[0] = asks[i].price;
                    sellDepth[1] = asks[i].totalSize;
                    sellDepth[2] = 1;
                    sellDepthList.push(sellDepth);
                }
            }

            return {
                bids: buyDepthList,
                asks: sellDepthList
            }
        },
        // _handleDataPush: function (data) {
        //     var bids = data.bids;
        //     var asks = data.asks;
        //     return {
        //         bids: bids,
        //         asks: asks
        //     }
        // },

        /**
         * 刷新深度
         * @param data
         * @param partialUpdate 局部更新
         */
        _refreshDepthDom: function (data, partialUpdate) {
            if (!data) {
                return;
            }
            jQuery("#depthHidding").hide();

            var medianUnit = sortDepth.medianUnit(data.bids, data.asks, 70);
            if (!!data.asks) {
                //深度交易信息获取
                var depths_resul = "";
                var sell_depths = data.asks;
                if (!this.getDetphsHtml.sellDepthLength || this.getDetphsHtml.sellDepthLength < 200 || sell_depths.length < 200) {
                    for (var i = 0; i < sell_depths.length; i++) {
                        if (i >= depth_size) {
                            break;
                        }
                        depths_resul += this.getDetphsHtml(i, sell_depths[i][0], sell_depths[i][1], medianUnit, 1);
                    }
                    this.getDetphsHtml.sellDepthLength = sell_depths.length;
                    jQuery("#depth_sell_context").html(depths_resul);
                } else {
                    for (var i = 0; i < sell_depths.length; i++) {
                        if (i >= depth_size) {
                            break;
                        }
                        this.getDepthHtml200(i, sell_depths[i][0], sell_depths[i][1], medianUnit, 1);
                    }
                }
            } else if (!partialUpdate) {
                jQuery("#depth_sell_context").empty();
            }
            if (!!data.bids) {
                var depths_resul_buy = "";
                var buy_depths = data.bids;
                if (!this.getDetphsHtml.buyDepthLength || this.getDetphsHtml.buyDepthLength < 200 || buy_depths.length < 200) {
                    for (var i = 0; i < buy_depths.length; i++) {
                        if (i >= depth_size) {
                            break;
                        }
                        depths_resul_buy += this.getDetphsHtml(i, buy_depths[i][0], buy_depths[i][1], medianUnit, 0);
                    }
                    this.getDetphsHtml.buyDepthLength = buy_depths.length;
                    jQuery("#depth_buy_context").html(depths_resul_buy);
                } else {
                    for (var i = 0; i < buy_depths.length; i++) {
                        if (i >= depth_size) {
                            break;
                        }
                        this.getDepthHtml200(i, buy_depths[i][0], buy_depths[i][1], medianUnit, 0);
                    }
                }
            } else if (!partialUpdate) {
                jQuery("#depth_buy_context").empty();
            }

            var baseCurrency = ok.symbol.split("_")[1];
            $('.depthPriceUnit').html(baseCurrency.toUpperCase());
        },

        _refreshDepthDomV2: function (data, partialUpdate) {
            if (!data) {
                return;
            }
            jQuery("#depthHidding").hide();

            if (typeof $v2DomMarketEntrust == 'undefined') {
                $v2DomMarketEntrust = jQuery("#marketEntrust");
                $v2SellParent = $v2DomMarketEntrust.find('#depth_sell_context');
                $v2BuyParent = $v2DomMarketEntrust.find('#depth_buy_context');
            }

            var _this = this;
            var _renderDepthDomV2 = function () {
                var medianUnit = sortDepth.medianUnit(data.bids, data.asks, 70);

                if (!!data.asks) {
                    //深度交易信息获取
                    $v2SellParent.find('#sellLoadingLi').remove();

                    var $parent = $v2SellParent;
                    var sellChildrenLength = $parent.find('li').length;
                    var sell_depths = data.asks;

                    // 不足200条时 补齐200条
                    if (sellChildrenLength < 200) {
                        _this._appendDepthHtml(sellChildrenLength, 1, "depth_sell_context");
                    }

                    var typeStr = 'sell', $li = null;

                    for (var i = 0, order = 1; i < 200; i++, order++) {
                        // 如果在数据范围内 就更新数据
                        $li = $parent.find('li[data-key="' + typeStr + 'li' + order + '"]');
                        if (i < sell_depths.length) {
                            $li.find('#' + typeStr + "PriceSpan" + order).attr('data-price', sell_depths[i][0]).html(Calculate.ShowDownTruncation(sell_depths[i][0], ok.priceTruncate(ok.symbol)));
                            $li.find('#' + typeStr + "AmountSpan" + order).attr('data-amount', sell_depths[i][1]).html(Calculate.ShowDownTruncation(sell_depths[i][1], ok.depthAmountTruncate));
                            $li.find('#' + typeStr + "SpanColor" + order).get(0).style.width = sortDepth.width(sell_depths[i][1], medianUnit) + "px";
                            $li.removeClass('dis-none');
                        } else {
                            // 超出数据范围 隐藏视图
                            $li.addClass('dis-none');
                        }
                    }
                } else if (!partialUpdate) {
                    $v2SellParent.empty();
                }

                if (!!data.bids) {
                    $v2BuyParent.find('#buyLoadingLi').remove();

                    var $parent = $v2BuyParent;
                    var buyChildrenLength = $parent.find('li').length;
                    var buy_depths = data.bids;

                    // 不足200条时 补齐200条
                    if (buyChildrenLength < 200) {
                        _this._appendDepthHtml(buyChildrenLength, 0, "depth_buy_context");
                    }

                    var typeStr = 'buy', $li = null;

                    for (var i = 0, order = 1; i < 200; i++, order++) {
                        // 如果在数据范围内 就更新数据
                        $li = $parent.find('li[data-key="' + typeStr + 'li' + order + '"]');
                        if (i < buy_depths.length) {
                            $li.find('#' + typeStr + "PriceSpan" + order).attr('data-price', buy_depths[i][0]).html(Calculate.ShowDownTruncation(buy_depths[i][0], ok.priceTruncate(ok.symbol)));
                            $li.find('#' + typeStr + "AmountSpan" + order).attr('data-amount', buy_depths[i][1]).html(Calculate.ShowDownTruncation(buy_depths[i][1], ok.depthAmountTruncate));
                            $li.find('#' + typeStr + "SpanColor" + order).get(0).style.width = sortDepth.width(buy_depths[i][1], medianUnit) + "px";
                            $li.removeClass('dis-none');
                        } else {
                            // 超出数据范围 隐藏视图
                            $li.addClass('dis-none');
                        }
                    }

                } else if (!partialUpdate) {
                    $v2BuyParent.empty();
                }

                var baseCurrency = ok.symbol.split("_")[1];
                $v2DomMarketEntrust.find('.depthPriceUnit').html(baseCurrency.toUpperCase());
            }

            if (!renderDepthTimeOut) {

                renderDepthTimeOut = setTimeout(function () {

                    _renderDepthDomV2();

                    renderDepthTimeOut = 0;
                }, 600);
            }
        },

        _appendDepthHtml: function (startIndex, type, parentId) {

            var typeStr = type == 0 ? 'buy' : 'sell';
            var typeStrDisplay = type == 0 ? Lang.buyin : Lang.immediatelysell;

            var appendHtml = [];

            for (var i = startIndex; i < 200; i++) {

                var order = i + 1;

                appendHtml.push(
                    '<li class="li-ct-transcation dis-none" data-key="' + typeStr + 'li' + order + '" onclick="depthClickAutoTrade._' + typeStr + 'AutoTrade(' + order + ')">'
                    + '<div class="part-ct-transcation ' + typeStr + '" style="width:55px;" id="order' + type + order + '">' + typeStrDisplay + order + '</div>'
                    + '<div class="part-ct-transcation_right"  style="padding: 0px 5px 0px 0px;width:70px;">'
                    + '<span id="' + typeStr + 'PriceSpan' + order + '" data-price="" class="price"></span>'
                    + '</div>'
                    + '<div class="part-ct-transcation_right" style="padding-left: 0px">'
                    + '<span id="' + typeStr + 'AmountSpan' + order + '" data-amount="" class="number"></span>'
                    + '</div>'
                    + '<div class="part-ct-transcation"  style="padding-left:5px">'
                    + '<span style="width: 18px" class="' + typeStr + 'span" id="' + typeStr + 'SpanColor' + order + '"></span>'
                    + '</div>'
                    + '</li>'
                );
            }

            jQuery('#' + parentId).append(appendHtml.join(''));
        },

        /**
         * 获取深度dom操作
         * @param price
         * @param amount
         * @param type
         * @param date_str
         */
        getDetphsHtml: function (order, price, amount, medianUnit, type) {
            var result = '';
            var order = order + 1;
            var price = Calculate.ShowDownTruncation(price, ok.priceTruncate(ok.symbol));
            if (type == 0) {
                result = '<li class="li-ct-transcation"  onclick="depthClickAutoTrade._buyAutoTrade(' + order + ')">';
                result += '<div class="part-ct-transcation buy" style="width:55px;" id="order' + type + order + '">' + Lang.buyin + order + '</div>';
                result += '<div class="part-ct-transcation_right"  style="padding: 0px 5px 0px 0px;width:70px;">';
                result += '<span id="buyPriceSpan' + order + '" class="price">' + price + '</span>';
                result += '<input type="hidden" id="buyPrice' + order + '" value="' + price + '" />';
                result += '</div>';
                result += '<div class="part-ct-transcation_right" style="padding-left: 0px">'
                result += '<span id="buyAmountSpan' + order + '" class="number">' + Calculate.DownTruncation(amount, ok.depthAmountTruncate) + '</span>';
                result += '<input type="hidden" id="buyAmount' + order + '" value="' + amount + '" />';
                result += '</div>';
                result += '<div class="part-ct-transcation"  style="padding-left:5px">';
                result += '<span style="width: ' + sortDepth.width(amount, medianUnit) + 'px" class="buyspan" id="buySpanColor' + order + '"></span>';
                result += '</div>';
            } else {
                result = '<li class="li-ct-transcation"  onclick="depthClickAutoTrade._sellAutoTrade(' + order + ')">';
                result += '<div class="part-ct-transcation sell" style="width:55px;" id="order' + type + order + '" class="order">' + Lang.immediatelysell + order + '</div>';
                result += '<div class="part-ct-transcation_right"  style="padding: 0px 5px 0px 0px;width:70px;">';
                result += '<span id="sellPriceSpan' + order + '" class="price">' + price + '</span>';
                result += '<input type="hidden" id="sellPrice' + order + '" value="' + price + '" />';
                result += '</div>';
                result += '<div class="part-ct-transcation_right" style="padding-left: 0px">';
                result += '<span id="sellAmountSpan' + order + '"  class="number">' + Calculate.DownTruncation(amount, ok.depthAmountTruncate) + '</span>';
                result += '<input type="hidden" id="sellAmount' + order + '" value="' + amount + '" />';
                result += '</div>';
                result += '<div class="part-ct-transcation"  style="padding-left:5px">';
                result += '<span style="width: ' + sortDepth.width(amount, medianUnit) + 'px" class="sellspan" id="sellSpanColor' + order + '"></span>';
                result += '</div>';
            }
            result += '</li>';
            return result;
        },

        getDepthHtml200: function (order, price, amount, medianUnit, type) {
            var order = order + 1;
            var price = Calculate.ShowDownTruncation(price, ok.priceTruncate(ok.symbol));
            var amount = Calculate.DownTruncation(amount, ok.depthAmountTruncate);
            if (type == 0) {
                document.getElementById("buyPriceSpan" + order).innerHTML = price;
                document.getElementById("buyPrice" + order).value = price;
                document.getElementById("buyAmountSpan" + order).innerHTML = amount;
                document.getElementById("buyAmount" + order).value = amount;
                document.getElementById("buySpanColor" + order).style.width = sortDepth.width(amount, medianUnit) + "px";
            } else {
                document.getElementById("sellPriceSpan" + order).innerHTML = price;
                document.getElementById("sellPrice" + order).value = price;
                document.getElementById("sellAmountSpan" + order).innerHTML = amount;
                document.getElementById("sellAmount" + order).value = amount;
                document.getElementById("sellSpanColor" + order).style.width = sortDepth.width(amount, medianUnit) + "px";
            }
        },

    }


    // return {
    //     init: function() {
    //
    //     },
    //     depthVew: depthVew
    // }
    window.depthClickAutoTrade = depthClickAutoTrade;
    window.depthDomOperate = depthDomOperate;
    marketRefreshDepth.init();
    return marketRefreshDepth;
});