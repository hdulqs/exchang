/**
 * Created by wenqiang.li on 2017/9/7.
 */
;(function ($) {
    //var isNoLoad = location.href.indexOf("/future/future.do") > 0 || location.href.indexOf("/futures.html") > 0 || location.href.indexOf("/spot/market/index.do") > 0;
    var isNoLoad = location.pathname === '/' ? true : false;

    var futureBannerTicker = {
        priceDefine: {
            futures_cny: 1, //合约价格人民币默认1位
            futures_usd: 2 //合约价格美元默认2位
        },
        symbolDefine: {
            0: 'BTC',
            1: 'LTC',
            2: 'ETH',
            4: 'ETC',
            5: 'BCH'
        },
        allFuturesArr: [],
        refreshSelectedTickersByFutures: function (item) {
            var cls = item.changePercent.toString().indexOf('-') > -1 ? 'red' : 'green',
                clsArrow = item.changePercent.toString().indexOf('-') > -1 ? 'banner-arrow-down' : 'banner-arrow-up',
                current_symbol = $('#current_symbol').val().toString();
            var priceTruncate = current_symbol === '0' ? this.priceDefine.futures_usd : this.priceDefine.futures_cny;
            var $selectedItem = $('#bannerTickerFutures'), $name = null, $sign = null, $price = null, $arrow = null;
            if (item.symbol === 0) {
                $name = $selectedItem.find('span').eq(0);
                $sign = $selectedItem.find('i').eq(0);
                $price = $selectedItem.find('span').eq(1);
                $arrow = $selectedItem.find('i').eq(1);
            } else if (item.symbol === 1) {
                $name = $selectedItem.find('span').eq(2);
                $sign = $selectedItem.find('i').eq(2);
                $price = $selectedItem.find('span').eq(3);
                $arrow = $selectedItem.find('i').eq(3);
            }
            if ($selectedItem && $selectedItem.length) {
                $sign.attr("class", cls);
                $name.html(languageJson['bannerTickerQuarter'] + this.symbolDefine[item.symbol] + item.contractId.toString().substr(4, 4) + ':');
                if (current_symbol === '0') {
                    $price.html(Calculate.ShowDownTruncation(item.last, priceTruncate)).attr("class", cls);
                }
                else {
                    $price.html(Calculate.ShowDownTruncation(Calculate.accMul_z(item.last, item.usdCnyRate), priceTruncate)).attr("class", cls);
                }
                $arrow.attr("class", clsArrow);
            }
        },
        refreshPanelTickersByFutures: function (list, isBatch) {
            if (list.length === 0) {
                return;
            }
            var current_symbol = $('#current_symbol').val().toString();
            var _this = this;
            var baseCurr = current_symbol === '0' ? 'USD' : 'CNY';
            var sign = current_symbol === '0' ? '$' : '￥';
            var priceTruncate = current_symbol === '0' ? this.priceDefine.futures_usd : this.priceDefine.futures_cny;

            //注：为了保证顺序，这里新增了等待BTC/LTC数据第一次都加载完毕后再render
            if (!isBatch) {

                list.sort(function (v1, v2) {
                    return v2.contractType - v1.contractType;
                });

                if (this.allFuturesArr.length === 0) {
                    this.allFuturesArr = list;
                    return;
                } else if (this.allFuturesArr.length > 0 && this.allFuturesArr.length < 6) {
                    //注：因为用的after追加，所以顺序要倒着concat
                    if (list[0].symbol === 1) {
                        list = list.concat(this.allFuturesArr);
                    } else if (list[0].symbol === 0) {
                        list = this.allFuturesArr.concat(list);
                    }
                    this.allFuturesArr = list;
                }
            }

            var allRowContent = '';

            $.each(list, function (i, item) {
                var desc = item.contractType < 4 ? item.contractType === 2 ? languageJson['bannerTickerNextWeek'] : languageJson['bannerTickerWeek'] : languageJson['bannerTickerQuarter'];
                desc += _this.symbolDefine[item.symbol] + item.contractId.toString().substr(4, 4);

                var unit = languageJson['bannerTickerUnit'];
                var tradeCurr = _this.symbolDefine[item.symbol];

                var color = item.changePercent.toString().indexOf('-') > -1 ? 'red' : 'green-new';
                var arrow = item.changePercent.toString().indexOf('-') > -1 ? 'banner-arrow-down' : 'banner-arrow-up';
                var vol = Calculate.formatNumber(item.volume);

                var changePercent = Calculate.ShowDownTruncation(item.changePercent, 2);
                if (changePercent.toString().indexOf('-') === -1) {
                    changePercent = changePercent.toString().indexOf('+') > -1 ? changePercent.toString() : '+' + changePercent.toString();
                }
                if (current_symbol === '0') {
                    var last = Calculate.ShowDownTruncation(item.last, priceTruncate),
                        high = Calculate.ShowDownTruncation(item.high, priceTruncate),
                        low = Calculate.ShowDownTruncation(item.low, priceTruncate);
                }
                else {
                    var last = Calculate.ShowDownTruncation(Calculate.accMul_z(item.last, item.usdCnyRate), priceTruncate),
                        high = Calculate.ShowDownTruncation(Calculate.accMul_z(item.high, item.usdCnyRate), priceTruncate),
                        low = Calculate.ShowDownTruncation(Calculate.accMul_z(item.low, item.usdCnyRate), priceTruncate);
                }

                if (isBatch) {

                    allRowContent += '<tr data-symbol=' + (tradeCurr + '_' + baseCurr + '_' + item.contractType).toLowerCase() + ' data-desc="' + desc + '" data-contract=' + item.contractId + ' class="coinRow bannerFutureCoinRow">'
                        + '<td id="bannerCoin" class="coinCell">' + desc + '：</td>'
                        + '<td id="bannerLast" style="width:148px;" class="coinCell ' + color + '"><em class="mar-right2">' + sign + '</em><span class="bannerLast-inner">' + last + '</span><em class="' + arrow + '"></em></td>'
                        + '<td id="bannerHigh"><em class="mar-right2">' + sign + '</em><span>' + high + '</span></td>'
                        + '<td id="bannerLow"><em class="mar-right2">' + sign + '</em><span>' + low + '</span></td>'
                        + '<td id="bannerChangePercentage" class="' + color + '"><span>' + changePercent + '</span><em>%</em></td>'
                        + '<td id="bannerDeal"><span>' + vol + '</span><em class="mar-left5 gray1 vol-currency">' + unit + '</em></td>'
                        + '</tr>';

                    if (i == list.length - 1) {
                        $('.bannerFutureCoinRow').remove();
                        $('#coinListBox table[data-key="futures"] tbody').append(allRowContent);
                    }

                    return;
                }

                var $row = $('#coinListBox table[data-key="futures"] tr[data-symbol=' + (tradeCurr + '_' + baseCurr + '_' + item.contractType).toLowerCase() + ']');

                if ($row && $row.length) {
                    $row.find("#bannerCoin").html(desc + '：');
                    $row.find("#bannerLast").attr('class', 'coinCell ' + color)
                        .find(".mar-right2").html(sign)
                        .next().html(last)
                        .next().attr('class', arrow);
                    $row.find("#bannerHigh").find("em").html(sign).next().html(high);
                    $row.find("#bannerLow").find("em").html(sign).next().html(low);
                    $row.find("#bannerChangePercentage").attr('class', color).find("span").html(changePercent).next().html('%');
                    $row.find("#bannerDeal").find("span").html(vol).next().html(unit);
                } else {
                    var $futures = $('#coinListBox table[data-key="futures"] tbody');
                    $futures.append('<tr data-symbol=' + (tradeCurr + '_' + baseCurr + '_' + item.contractType).toLowerCase() + ' data-desc="' + desc + '" data-contract=' + item.contractId + ' class="coinRow">'
                        + '<td id="bannerCoin" class="coinCell">' + desc + '：</td>'
                        + '<td id="bannerLast" class="coinCell ' + color + '"><em class="mar-right2">' + sign + '</em><span class="bannerLast-inner">' + last + '</span><em class="' + arrow + '"></em></td>'
                        + '<td id="bannerHigh"><em class="mar-right2">' + sign + '</em><span>' + high + '</span></td>'
                        + '<td id="bannerLow"><em class="mar-right2">' + sign + '</em><span>' + low + '</span></td>'
                        + '<td id="bannerChangePercentage" class="' + color + '"><span>' + changePercent + '</span><em>%</em></td>'
                        + '<td id="bannerDeal"><span>' + vol + '</span><em class="mar-left5 gray1 vol-currency">' + unit + '</em></td>'
                        + '</tr>');
                }
            });
        },
        refreshFutures: function (list) {
            if (list.length === 0) {
                return;
            }
            for (var i = 0; i < list.length; i++) {
                var ticker = list[i];
                if (ticker.contractType === 4) {
                    this.refreshSelectedTickersByFutures(ticker);
                    break;
                }
            }
            this.refreshPanelTickersByFutures(list);
        },
        refreshFuturesDataSource: function () {

            var _this = this;

            $.get("/future/api/inner/ticker.do?symbol=-1", function (data) {
                futureBannerTicker.refreshPanelTickersByFutures(data.ticker, true);

                if (window.refreshFutureContractList) {
                    window.refreshFutureContractList(data.ticker);
                }

                $.each(data.ticker, function (index, item) {
                    var isBtcOrLtc = item.symbol === 0 || item.symbol === 1;
                    var canUpdate = isBtcOrLtc && item.contractType === 4;
                    if (canUpdate) {
                        _this.refreshSelectedTickersByFutures(item);
                    }
                });

            }, "JSON");

            // var arr = [0, 1];
            // var _this = this;
            // for (var i = 0; i < arr.length; i++) {
            //     $.get("/future/api/inner/ticker.do?symbol=" + arr[i].toString(), function (data) {
            //         for (var i = 0; i < data.ticker.length; i++) {
            //             var ticker = data.ticker[i];
            //             if (ticker.contractType === 4) {
            //                 _this.refreshSelectedTickersByFutures(ticker);
            //                 break;
            //             }
            //         }
            //         _this.refreshPanelTickersByFutures(data.ticker);
            //     }, "JSON");
            // }
        }
    }

    function addClickEvent() {
        var runClick = function (self) {
            var data_symbol = $(self).attr('data-symbol');
            if (!!data_symbol && data_symbol.indexOf('_') > -1) {
                var arr = data_symbol.split('_');
                //现货
                if (arr.length === 2) {
                    window.location.href = '/spot/trade/index.do#symbol=' + arr[0] + '_' + arr[1];
                    if (location.pathname.indexOf('/spot/trade/index.do') > -1) {
                        location.reload();
                    }
                }
                else if (arr.length === 3) {
                    //合约
                    var symbol = arr[0].toUpperCase() === 'BTC' ? 'Btc' : 'Ltc';
                    var contractId = $(self).attr('data-contract');
                    $.cookie('treatyType_' + symbol, contractId);
                    var key = arr[0].toUpperCase() === 'BTC' ? 0 : 1;
                    window.location.href = '/future/future.do?symbol=' + key;
                }
            }
            else {
                $('#coinListBox').hide();
            }
        }
        $('#coinListBox table').on('click', 'tr', function () {
            runClick(this);
        }).on('dblclick', 'tr', function () {
            runClick(this);
        });
    }

    addClickEvent();

    futureBannerTicker.refreshFuturesDataSource();
    if (!isNoLoad) {
        setInterval(function () {
            futureBannerTicker.refreshFuturesDataSource();
        }, 20 * 1000);
    }

    // 即用于requirejs，又用于普通引用
    if (typeof define === 'function' && define.amd) {
        // AMD
        define([], function () {
            return futureBannerTicker;
        });
    } else {
        // Browser globals
        window.futureBannerTicker = futureBannerTicker;
    }

})(jQuery);