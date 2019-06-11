;(function ($) {
    var isLogin = $('#banner_is_login').val();
    var isNoLoad = location.pathname === '/' || location.href.indexOf("/index.do") > 0 || location.href.indexOf("spot/trade/index.do") > 0
        || location.href.indexOf("/spot.html") > 0 || location.href.indexOf("/spot/market/index.do") > 0
        || location.href.indexOf("future/future.do") > 0 || location.href.indexOf("future/market.do") > 0 || location.href.indexOf("futures.html") > 0
        || location.href.indexOf("c2c/trade/trade.do") > 0
        || location.href.indexOf("c2c/trade/openTrade.do") > 0;

    var failNum = 0; //推送入口进入时，调用币种ajax失败次数，失败过多时放弃请求
    var beforeLock = false;
    var isBannerFirstSort = true;

    function initAddEve() {
        $("#coinListBox .coin-title").click(function (event) {
            $(this).addClass('active').siblings('div').removeClass('active');
            var key = $(this).attr('data-key');
            var $table = $('#coinListBox table[data-key="' + key + '"]');
            if ($table.length) {
                $table.siblings('table').addClass('dis-none');
                $table.removeClass('dis-none');
            }
            event.stopPropagation();
        });

        var timeInter = 0;
        var _this = this;
        $("#bannerSearchInp").on('keyup paste', function (event) {
            console.log($(this).val());
            if (timeInter) {
                clearTimeout(timeInter);
            }
            var _self = this;
            //300毫秒 为了提高性能
            timeInter = setTimeout(function () {
                var _inp = $(_self).val().trim().toUpperCase();
                $('#coinListBox tr[data-desc]').each(function (index, obj) {
                    var desc = $(obj).attr('data-desc');
                    if (desc && desc.toUpperCase().indexOf(_inp) > -1) {
                        $(obj).show();
                    }
                    else {
                        $(obj).hide();
                    }
                });
            }, 300);
            event.stopPropagation();
        });

        $("#coinListBox .banner-sort-th").click(function (event) {
            var isAsc = $(this).find('i').attr('class').indexOf('banner-sort-up') > -1 ? false : true;
            sortBannerPanel(this, isAsc);
            event.stopPropagation();
        });
    }

    initAddEve();

    function sortBannerPanel(self, isAsc) {
        var $i = $(self).find('i');
        var clsArrow = '';
        if (isAsc) {
            clsArrow = 'banner-sort-up';
        }
        else {
            clsArrow = 'banner-sort-down';
        }
        $i.attr('class', clsArrow);
        var $table = $(self).parents('.banner-coin-table');
        var $trs = $table.find('tbody').find('tr');
        $trs.sort(function (a, b) {
            var v1 = $(a).find('#bannerChangePercentage').find('span').html();
            var v2 = $(b).find('#bannerChangePercentage').find('span').html();
            if (clsArrow == 'banner-sort-up') {
                return Number(v1) - Number(v2);
            }
            else {
                return Number(v2) - Number(v1);
            }
        });
        $table.find('tbody').html($trs);
    }


    var bannerTicker = {
        priceDefine: {
            price: 8,//币币最新价默认8位
            vol_max: 10, //默认成交量界限，默认1000
            volume: 8, //默认成交量 4位
            volumeBig: 0  //当成交量大于vol_max时，小数点保留的位数，0位保留到整数位
        },
        // 是否第一次加载
        isFirstLoad: true,
        // 是否不载入
        isNoLoad: isNoLoad,
        selectTickerArr: ['bch', 'ltc'],
        signObj: {},
        refreshSelectedTickers: function (list) {
            var _this = this;
            var priceTruncate = _this.priceDefine.price;
            $.each(list, function (i, item) {
                var currency = item.symbol.split('_')[0], baseCurr = item.symbol.split('_')[1];
                if (_this.selectTickerArr.length && baseCurr === 'btc') {
                    var cls = item.changePercentage.indexOf('-') > -1 ? 'red' : 'green',
                        clsArrow = item.changePercentage.indexOf('-') > -1 ? 'banner-arrow-down' : 'banner-arrow-up';
                    var $selectedItem = $('#bannerTickerSpot'), $sign = null, $price = null, $arrow = null;
                    if (_this.selectTickerArr[0] === currency.toLowerCase()) {
                        $sign = $selectedItem.find('i').eq(0);
                        $price = $selectedItem.find('span').eq(1);
                        $arrow = $selectedItem.find('i').eq(1);
                    } else if (_this.selectTickerArr[1] === currency.toLowerCase()) {
                        $sign = $selectedItem.find('i').eq(2);
                        $price = $selectedItem.find('span').eq(3);
                        $arrow = $selectedItem.find('i').eq(3);
                    }
                    if ($sign && $sign.length) {
                        $sign.attr("class", cls);
                        $price.html(Calculate.ShowDownTruncation(item.last, priceTruncate)).attr("class", cls);
                        $arrow.attr("class", clsArrow);
                    }
                }
            })
        },
        refreshPanelTickers: function (list) {
            var _this = this;
            var sign = '฿';
            var priceTruncate = _this.priceDefine.price;
            var $listBox = $('#coinListBox');
            $.each(list, function (i, item) {
                var baseCurr = item.symbol.split('_')[1];
                var tradeCurr = item.symbol.split('_')[0].toUpperCase();
                var color = item.changePercentage.indexOf('-') > -1 ? 'red' : 'green-new';
                var arrow = item.changePercentage.indexOf('-') > -1 ? 'banner-arrow-down' : 'banner-arrow-up';
                var vol = Math.floor(item.volume) >= _this.priceDefine.vol_max ? Calculate.ShowDownTruncation(item.volume, _this.priceDefine.volumeBig) : Calculate.ShowDownTruncation(item.volume, _this.priceDefine.volume);
                sign = _this.signObj[baseCurr.toLowerCase()].sign;
                var $row = $listBox.find('table[data-key="spot-' + baseCurr.toLowerCase() + '"] tr[data-symbol="' + item.symbol.toLowerCase() + '"]');
                if ($row && $row.length) {
                    $row.find("#bannerCoin").html((tradeCurr + '/' + baseCurr).toUpperCase() + '：');
                    $row.find("#bannerLast").attr('class', 'coinCell ' + color)
                        .find(".mar-right2").html(sign)
                        .next().html(Calculate.ShowDownTruncation(item.last, priceTruncate))
                        .next().attr('class', arrow);
                    $row.find("#bannerHigh").find("em").html(sign).next().html(Calculate.ShowDownTruncation(item.dayHigh, priceTruncate));
                    $row.find("#bannerLow").find("em").html(sign).next().html(Calculate.ShowDownTruncation(item.dayLow, priceTruncate));
                    $row.find("#bannerChangePercentage").attr('class', color).find("span").html(item.changePercentage.replace('%', '')).next().html('%');
                    $row.find("#bannerDeal").find("span").html(vol).next().html(tradeCurr);
                } else {
                    var $spot = $listBox.find('table[data-key="spot-' + baseCurr.toLowerCase() + '"] tbody');
                    var desc = (tradeCurr + '/' + baseCurr).toUpperCase();
                    $spot.append('<tr data-desc="' + desc + '"  data-symbol=' + item.symbol.toLowerCase() + ' class="coinRow">'
                        + '<td id="bannerCoin" class="coinCell">' + desc + '：' + '</td>'
                        + '<td id="bannerLast" class="coinCell ' + color + '"><em class="mar-right2">' + sign + '</em><span class="bannerLast-inner">' + Calculate.ShowDownTruncation(item.last, priceTruncate) + '</span><em class="' + arrow + '"></em></td>'
                        + '<td id="bannerHigh"><em class="mar-right2">' + sign + '</em><span>' + Calculate.ShowDownTruncation(item.dayHigh, priceTruncate) + '</span></td>'
                        + '<td id="bannerLow"><em class="mar-right2">' + sign + '</em><span>' + Calculate.ShowDownTruncation(item.dayLow, priceTruncate) + '</span></td>'
                        + '<td id="bannerChangePercentage" class="' + color + '"><span>' + item.changePercentage.replace('%', '') + '</span><em>%</em></td>'
                        + '<td id="bannerDeal"><span>' + vol + '</span><em class="mar-left5 gray1 vol-currency">' + tradeCurr + '</em></td>'
                        + '</tr>');
                }
            });
            if (isBannerFirstSort) {
                isBannerFirstSort = false;
                $('#coinListBox .banner-sort-th').each(function (i, obj) {
                    sortBannerPanel(obj);
                });
            }
        },
        // 初始化
        init: function () {
            var _this = this;
            //获取所有币种符号
            OkAjax.getMarketCurrencies({
                success: function (data) {
                    if (!data || !data.length) {
                        return;
                    }
                    for (var k = 0; k < data.length; k++) {
                        _this.signObj[data[k].symbol.toLowerCase()] = data[k];
                    }
                    _this.getMarketProducts();
                }
            });
        },
        getMarketProducts: function () {
            var _this = this;
            OkAjax.getMarketProducts({
                success: function (data) {
                    if (!data || !data.length) {
                        return;
                    }
                    data.sort(function (a, b) {
                        return a.sort > b.sort;
                    });
                    var productsOnlineArr = [];
                    for (var i = 0; i < data.length; i++) {
                        if (+data[i].online === 1) {
                            productsOnlineArr.push(data[i].symbol);
                        }
                    }

                    // 设置在线币对列表
                    if (productsOnlineArr.length) {
                        $("#productsOnlineListInBanner").val(productsOnlineArr.join(','));
                    }
                    _this.getTickersByApi();
                }
            });
        },
        getTickersByApi: function () {
            var _this = this;
            var productsOnlineListInBanner = $("#productsOnlineListInBanner").val();
            var productsOnlineArr = productsOnlineListInBanner ? productsOnlineListInBanner.split(',') : "";
            if (!productsOnlineListInBanner || productsOnlineArr.length === 0) {
                return;
            }
            OkAjax.getMarketTickers({
                success: function (list) {
                    var onlineList = [];
                    for (var i = 0; i < productsOnlineArr.length; i++) {
                        for (var j = 0; j < list.length; j++) {
                            if (productsOnlineArr[i] == list[j].symbol) {
                                onlineList.push(list[j]);
                            }
                        }
                    }
                    $("#isAjaxLoadSuccessInBanner").val(1);
                    _this.refreshDataSource(onlineList);

                    // 启动一次
                    if (_this.isFirstLoad && !_this.isNoLoad) {
                        _this.isFirstLoad = false;
                        _this.startWebsocket();
                    }
                }
            });
        },
        // 提供刷新数据接口，供api和ws使用
        refreshDataSource: function (onlineList) {
            var _this = this;
            if ($("#isAjaxLoadSuccessInBanner").val() !== "1") {
                return;
            }
            if ((!_this.signObj || $.isEmptyObject(_this.signObj)) && failNum <= 5) {
                var _this = this;
                if (!beforeLock) {
                    beforeLock = true;
                    //获取所有币种符号
                    OkAjax.getMarketCurrencies({
                        success: function (data) {
                            beforeLock = false;
                            if (!data || !data.length) {
                                return;
                            }
                            for (var k = 0; k < data.length; k++) {
                                _this.signObj[data[k].symbol.toLowerCase()] = data[k];
                            }
                        },
                        fail: function () {
                            beforeLock = false;
                            failNum += 1;
                        }
                    });
                }
            }
            else {
                if (onlineList.length) {
                    bannerTicker.refreshSelectedTickers(onlineList);
                    bannerTicker.refreshPanelTickers(onlineList);
                }
            }
        },
        startWebsocket: function () {
            var _this = this;
            var productsOnlineListInBanner = $("#productsOnlineListInBanner").val();
            var productsOnlineArr = productsOnlineListInBanner ? productsOnlineListInBanner.split(',') : "";
            if (!productsOnlineListInBanner || productsOnlineArr.length === 0) {
                return;
            }
            webSocket3.addSuccCallBackFun(function () {
                console.log('banner addSuccCallBackFun');
                for (var i = 0; i < productsOnlineArr.length; i++) {
                    //当前币对行情
                    webSocket3.send(webSocket3.Utils.getTicker({symbol: productsOnlineArr[i]}), function (data) {
                        var arr = $.isPlainObject(data) ? [data] : (data[0] ? data : []);
                        bannerTicker.refreshDataSource(arr);
                        if (typeof leftMenu !== 'undefined') {
                            leftMenu.setTickers(arr);
                        }
                    });
                }
            });
            webSocket3.addErrCallBackFun(function () {
                console.log('banner addErrCallBackFun');
                bannerTicker.getTickersByApi();
            });
            webSocket3.connection();
        }
    };

    // 即用于requirejs，又用于普通引用
    if (typeof define === 'function' && define.amd) {
        // AMD
        define([], function () {
            return bannerTicker;
        });
    } else {
        // Browser globals
        window.bannerTicker = bannerTicker;
    }
})(jQuery);