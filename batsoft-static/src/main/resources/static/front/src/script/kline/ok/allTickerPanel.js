;(function ($) {
    //用户选币面板输入框计时器，用来优化
    var timeInter = 0;
    // var initPanel = 0;
    var priceDefine = {
        price: 8,//币币最新价默认8位
        vol_max: 10, //默认成交量界限
        volume: 8, //默认成交量
        volumeBig: 0  //当成交量大于vol_max时，小数点保留的位数，0位保留到整数位
    }

    var allTickerPanel = {
        fnList: {
            checkEventCallList: []
        },
        //币对气泡tab切换
        _onPanelTabChange: function (self) {
            $(self).addClass('active').siblings('div').removeClass('active');
            var key = $(self).attr('data-key');
            var $table = $('#coinBubble table[data-key="' + key + '"]');
            if ($table.length) {
                $table.siblings('table').addClass('dis-none');
                $table.removeClass('dis-none');
            }
        },
        //币对panel输入框改变
        _onPanelInputChange: function (self) {
            if (timeInter) {
                clearTimeout(timeInter);
            }
            var _self = this;
            var _inp = $(self).val().trim();
            //300毫秒 为了提高性能
            timeInter = setTimeout(function () {
                var $lis = $('#coinBubble tr[data-desc]');
                if ($lis.length) {
                    for (var i = 0; i < $lis.length; i++) {
                        var $li = $lis.eq(i);
                        var desc = $li.attr('data-desc');
                        if (desc.toUpperCase().indexOf(_inp.toUpperCase()) > -1) {
                            $li.show();
                        }
                        else {
                            $li.hide();
                        }
                    }
                }
            }, 300);
        },
        //币队排序
        _onPanelSort: function (self) {
            var $i = $(self).find('i');
            var clsArrow = $i.attr('class');
            if (clsArrow == 'banner-sort' || clsArrow == 'banner-sort-up') {
                clsArrow = 'banner-sort-down';
            }
            else if (clsArrow == 'banner-sort-down') {
                clsArrow = 'banner-sort-up';
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
        },
        //注册事件
        _regEvent: function () {
            var _this = this;
            var $coinBubble = $('#coinBubble');
            $coinBubble.off('click', '.coin-title')
                .on('click', '.coin-title', function () {
                    _this._onPanelTabChange(this);
                }).off('keyup paste', '#coinBubbleSearchInp')
                .on('keyup paste', '#coinBubbleSearchInp', function () {
                    _this._onPanelInputChange(this);
                })
                .off('click', '.banner-sort-th')
                .on('click', '.banner-sort-th', function () {
                    _this._onPanelSort(this);
                })
                .off('click', 'tr[data-symbol]')
                .on('click', 'tr[data-symbol]', function () {
                    var symbol = $(this).attr('data-symbol');
                    // 添加选中高亮
                    $(this).addClass('active');
                    $(this).siblings().removeClass('active');
                    $('#coinBubble').hide();
                    for (var i = 0; i < _this.fnList.checkEventCallList.length; i++) {
                        _this.fnList.checkEventCallList[i](this, symbol);
                    }
                });
            $('#chooseCoinType').mouseover(function (e) {
                _this.show();
                return false;
            });
            $('#chooseCoinType').mouseout(function (e) {
                _this.hide();
                return false;
            });
        },
        //
        onCheckOneTickerCallBack: function (callback) {
            if (typeof callback != 'undefined') {
                this.fnList.checkEventCallList.push(callback);
            }
        },
        setSort: function (isAsc) {
            $('#coinBubble .banner-sort-th').each(function (i, o) {
                var $table = $(o).parents('.banner-coin-table');
                if (isAsc) {
                    $(o).find('i').attr('class', 'banner-sort-up');
                }
                else {
                    $(o).find('i').attr('class', 'banner-sort-down');
                }
                var $trs = $table.find('tbody').find('tr');
                $trs.sort(function (a, b) {
                    var v1 = $(a).find('#bannerChangePercentage').find('span').html();
                    var v2 = $(b).find('#bannerChangePercentage').find('span').html();
                    if (isAsc) {
                        return Number(v1) - Number(v2);
                    }
                    else {
                        return Number(v2) - Number(v1);
                    }
                });
                $table.find('tbody').html($trs);
            })
        },
        //对外提供API，设置选中的交易区
        setMarketChk: function (baseCurr) {
            var $coinBubble = $('#coinBubble');
            $coinBubble.find('.banner-coin-titles div[data-key="spot-' + baseCurr.toLowerCase() + '"]').addClass('active').siblings().removeClass('active');
            $coinBubble.find('table[data-key="spot-' + baseCurr + '"]').removeClass('dis-none').siblings('table').addClass('dis-none');
        },
        //对外提供API，更新Panel Ticker数据
        updateTickerPanel: function (sortedObj) {
            if (!$.isEmptyObject(sortedObj)) {
                var html = '';
                var upAndDown = '';
                var color = '';
                var $coinBubble = $('#coinBubble');
                for (var symbol in sortedObj) {
                    // 涨跌幅箭头 & 涨跌幅颜色
                    if (sortedObj[symbol].changePercentage.indexOf('+') >= 0) {
                        upAndDown = 'up';
                        color = 'green';
                    } else if (sortedObj[symbol].changePercentage.indexOf('-') >= 0) {
                        upAndDown = 'down';
                        color = 'red';
                    }
                    var sign = '';
                    var baseCurr = sortedObj[symbol].symbol.split('_')[1];
                    var tradeCurr = sortedObj[symbol].symbol.split('_')[0].toUpperCase();

                    var sign = ok.currencySymbol[symbol.split("_")[1]];
                    if (!sign) {
                        sign = ok.getSignByCurrency(symbol.split("_")[1]);
                    }

                    var color = sortedObj[symbol].changePercentage.indexOf('-') > -1 ? 'red' : 'green-new';
                    var arrow = sortedObj[symbol].changePercentage.indexOf('-') > -1 ? 'banner-arrow-down' : 'banner-arrow-up';
                    var vol = Math.floor(sortedObj[symbol].volume) >= priceDefine.vol_max ? Calculate.ShowDownTruncation(sortedObj[symbol].volume, priceDefine.volumeBig) : Calculate.ShowDownTruncation(sortedObj[symbol].volume, priceDefine.volume);

                    var $row = $coinBubble.find('table[data-key="spot-' + baseCurr.toLowerCase() + '"] tr[data-symbol="' + symbol + '"]');
                    if ($row && $row.length) {
                        $row.find("#bannerCoin").html((tradeCurr + '/' + baseCurr).toUpperCase() + '：');
                        $row.find("#bannerLast").attr('class', 'coinCell ' + color)
                            .find(".mar-right2").html(sign)
                            .next().html(sortedObj[symbol].last)
                            .next().attr('class', arrow);
                        $row.find("#bannerHigh").find("em").html(sign).next().html(sortedObj[symbol].dayHigh);
                        $row.find("#bannerLow").find("em").html(sign).next().html(sortedObj[symbol].dayLow);
                        $row.find("#bannerChangePercentage").attr('class', color).find("span").html(sortedObj[symbol].changePercentage.replace('%', '')).next().html('%');
                        $row.find("#bannerDeal").find("span").html(vol).next().html(tradeCurr);
                    } else {
                        var $spot = $coinBubble.find('table[data-key="spot-' + baseCurr.toLowerCase() + '"] tbody');
                        var desc = (tradeCurr + '/' + baseCurr).toUpperCase();
                        $spot.append('<tr data-desc="' + desc + '"  data-symbol=' + symbol + ' class="coinRow">'
                            + '<td id="bannerCoin" class="coinCell">' + desc + '：' + '</td>'
                            + '<td id="bannerLast" class="coinCell ' + color + '"><em class="mar-right2">' + sign + '</em><span class="bannerLast-inner">' + sortedObj[symbol].last + '</span><em class="' + arrow + '"></em></td>'
                            + '<td id="bannerHigh"><em class="mar-right2">' + sign + '</em><span>' + sortedObj[symbol].dayHigh + '</span></td>'
                            + '<td id="bannerLow"><em class="mar-right2">' + sign + '</em><span>' + sortedObj[symbol].dayLow + '</span></td>'
                            + '<td id="bannerChangePercentage" class="' + color + '"><span>' + sortedObj[symbol].changePercentage.replace('%', '') + '</span><em>%</em></td>'
                            + '<td id="bannerDeal"><span>' + vol + '</span><em class="mar-left5 gray1 vol-currency">' + tradeCurr + '</em></td>'
                            + '</tr>');
                    }
                }
            }
        },
        show: function () {
            $('#coinBubble').show();
            $('#chooseTypeArrow').addClass('arrow-active');
        },
        hide: function () {
            $('#coinBubble').hide();
            $('#chooseTypeArrow').removeClass('arrow-active');
        },
        init: function () {
            this._regEvent();
        }
    }

    allTickerPanel.init();

    // 即用于requirejs，又用于普通引用
    if (typeof define === 'function' && define.amd) {
        // AMD
        define([], function () {
            return allTickerPanel;
        });
    } else {
        // Browser globals
        window.allTickerPanel = allTickerPanel;
    }
})(jQuery);