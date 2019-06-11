
define(['backbone', 'Calculate', 'tradeSliderBar', 'ajax'], function () {

    var ajax = require('ajax');

    var MIN_AMOUNT = 0.001;

    var Calculate = require('Calculate');
    var TradeSliderBar = require('tradeSliderBar');

    var buySlider, sellSlider;

    var $buyTypeSelect = $('#buyTypeSelect');
    var $sellTypeSelect = $('#sellTypeSelect');

    var $buyLimitView = $('#buyLimitView');
    var $buyMarketView = $('#buyMarketView');

    var $sellLimitView = $('#sellLimitView');
    var $sellMarketView = $('#sellMarketView');

    var $buyPrice = $('#buyPrice');
    var $buyAmount = $('#buyAmount');
    var $buyLimitMoney = $('#buyLimitMoney');
    var $buyMarketMoney = $('#buyMarketMoney');

    var $sellPrice = $('#sellPrice');
    var $sellAmount = $('#sellAmount');
    var $sellLimitMoney = $('#sellLimitMoney');
    var $sellMarketMoney = $('#sellMarketMoney');

    var $buyTips = $('#buyTips');
    var $sellTips = $('#sellTips');

    var $nowPrice = $('#nowPrice');

    var $buyPwd = $('#buyPwd');
    var $sellPwd = $('#sellPwd');

    var $isopen = $('#isopen');

    var buyTipsTimer, sellTipsTimer;

    var tradeData = {
        buySubmitting: false,
        sellSubmitting: false
    };

    // 输入过滤
    function filterInput(input) {

        if ($(input).attr('data-last-val') === input.value) return;

        var start = input.selectionStart,
            end = input.selectionEnd;

        // 过滤输入并限制8位小数
        input.value = Numbers.filter(input.value, 8);

        // 光标处理
        input.setSelectionRange(start, end);

        $(input).attr('data-last-val', input.value);
    }

    var tradeMgr = {

        initSliders: function () {
            buySlider = new TradeSliderBar({
                containerId: 'buySlider',
                roundNumber: true,
                type: 'buy'
            });
            sellSlider = new TradeSliderBar({
                containerId: 'sellSlider',
                roundNumber: true,
                type: 'sell'
            });

            // 买入比例变化
            buySlider.onPercentChange(function (percent) {

                var floatValue = percent * 0.01;

                // 0.1234
                if (floatValue.toString().length > 6) {
                    floatValue = floatValue.toFixed(4);
                }

                var buyPrice = $buyPrice.val();
                var totalCny = Numbers.filter($('#canUseCny').html());

                var currCny = Calculate.accMul(totalCny, floatValue);

                var currAmount = 0;

                if (Number(buyPrice) > 0) {
                    currAmount = Calculate.accDiv(currCny, buyPrice);
                }

                if (percent > 0) {

                    var money = Calculate.floor(currCny, ok.priceTruncate(ok.symbol));
                    var amount = Calculate.floor(currAmount, ok.amountTruncate(ok.symbol));

                    $buyLimitMoney.val(money);
                    $buyMarketMoney.val(money);
                    $buyAmount.val(amount);

                } else {
                    $buyLimitMoney.val('');
                    $buyMarketMoney.val('');
                    $buyAmount.val('');
                }
            });

            // 卖出比例变化
            sellSlider.onPercentChange(function (percent) {

                var floatValue = percent * 0.01;

                // 0.1234
                if (floatValue.toString().length > 6) {
                    floatValue = floatValue.toFixed(4);
                }

                var sellPrice = $sellPrice.val();
                var totalAmount = Numbers.filter($('#canSellETC').html());

                var currAmount = Calculate.accMul(totalAmount, floatValue);

                var currCny = 0;

                if (Number(sellPrice) > 0) {
                    currCny = Calculate.accMul(currAmount, sellPrice);
                }

                if (percent > 0) {

                    var amount = Calculate.floor(currAmount, ok.amountTruncate(ok.symbol));
                    var money = Calculate.floor(currCny, ok.priceTruncate(ok.symbol));

                    $sellAmount.val(amount);
                    $sellLimitMoney.val(money);
                    $sellMarketMoney.val(amount);

                } else {
                    $sellAmount.val('');
                    $sellLimitMoney.val('');
                    $sellMarketMoney.val('');
                }
            });
        },

        initEvents: function () {
            
            var self = this;

            $buyTypeSelect.change(this.onBuyTypeChange);

            $sellTypeSelect.change(this.onSellTypeChange);

            $buyPrice.keyup(this.onBuyPriceKeyUp);

            $buyAmount.keyup(this.onBuyAmountKeyUp);

            $buyLimitMoney.keyup(this.onBuyLimitMoneyKeyUp);

            $buyMarketMoney.keyup(this.onBuyMarketMoneyKeyUp);

            $sellPrice.keyup(this.onSellPriceKeyUp);

            $sellAmount.keyup(this.onSellAmountKeyUp);

            $sellLimitMoney.keyup(this.onSellLimitMoneyKeyUp);

            $sellMarketMoney.keyup(this.onSellMarketMoneyKeyUp);

            // 买卖提交

            $('#buyBtn').click(function () {
                self.submitBuy();
            });

            $('#sellBtn').click(function () {
                self.submitSell();
            });

            //充值埋点记录
            $('.recharge-word').click(function () {
                okLog.eventLog({
                    platform: 2,
                    eventId: "click_moneytransfer_buypart_charts"
                })
            });
        },

        onBuyTypeChange: function () {

            var buyType = $buyTypeSelect.val();
            var eventId = "";

            if (buyType === '0') {
                $buyLimitView.show();
                $buyMarketView.hide();

                $buyAmount.focus();
                eventId = "click_pricelimit_buypart_charts";
            } else {
                $buyLimitView.hide();
                $buyMarketView.show();

                $buyMarketMoney.focus();
                eventId = "click_pricenow_buypart_charts";
            }

            tradeMgr.resetBuyForm();

            //选择现价单，市价单埋点记录
            okLog.eventLog({
                platform: 2,
                eventId: eventId
            });
        },
        onSellTypeChange: function () {

            var sellType = $sellTypeSelect.val();
            var eventId = "";

            if (sellType === '0') {
                $sellLimitView.show();
                $sellMarketView.hide();

                $sellAmount.focus();
                eventId = "click_pricelimit_sellpart_charts";
            } else {
                $sellLimitView.hide();
                $sellMarketView.show();

                $sellMarketMoney.focus();
                eventId = "click_pricenow_sellpart_charts";
            }

            tradeMgr.resetSellForm();

            //选择现价单，市价单埋点记录
            okLog.eventLog({
                platform: 2,
                eventId: eventId
            });
        },

        // ------------------------买入联动------------------------
        onBuyPriceKeyUp: function () {
            filterInput(this);

            var buyPrice = this.value;
            var buyAmount = $buyAmount.val();
            var buyLimitMoney = $buyLimitMoney.val();

            if (Number(buyAmount) > 0) {                // 数量非空时，计算总金额

                var costMoney = Calculate.MultiPriceDownTruncation(Calculate.accMul(buyPrice, buyAmount), ok.priceTruncate(ok.symbol));

                $buyLimitMoney.val(costMoney);

                tradeMgr.onBuyCostMoneyChange(costMoney);

            } else if (Number(buyLimitMoney) > 0) {     // 金额非空，计算买入数量

                var realAmount = 0;

                if (Number(buyPrice) > 0) {
                    realAmount = Calculate.MultiPriceDownTruncation(Calculate.accDiv(buyLimitMoney, buyPrice), ok.amountTruncate(ok.symbol));
                }

                $buyAmount.val(realAmount);
            }
        },
        onBuyAmountKeyUp: function () {
            filterInput(this);

            var buyAmount = this.value;
            var buyPrice = $buyPrice.val();

            if (Number(buyPrice) >= 0) {

                var costMoney = Calculate.MultiPriceDownTruncation(Calculate.accMul(buyPrice, buyAmount), ok.priceTruncate(ok.symbol));

                $buyLimitMoney.val(costMoney);

                tradeMgr.onBuyCostMoneyChange(costMoney);
            }
        },
        onBuyLimitMoneyKeyUp: function () {
            filterInput(this);

            var buyLimitMoney = this.value;
            var buyPrice = $buyPrice.val();

            if (Number(buyPrice) > 0) {             // 价格非空，计算买入数量

                var realAmount = Calculate.MultiPriceDownTruncation(Calculate.accDiv(buyLimitMoney, buyPrice), ok.amountTruncate(ok.symbol));

                $buyAmount.val(realAmount);

                tradeMgr.onBuyCostMoneyChange(buyLimitMoney);
            }
        },
        onBuyMarketMoneyKeyUp: function () {
            filterInput(this);

            tradeMgr.onBuyCostMoneyChange(this.value);
        },
        onBuyCostMoneyChange: function (costMoney) {

            var totalBalance = Numbers.filter($('#canUseCny').html());

            var percent = 0;

            if (Number(totalBalance) > 0) {
                percent = Calculate.accDiv(costMoney, totalBalance) * 100;
            }

            buySlider.setPercent(percent);

            if (Number(costMoney) > Number(totalBalance)) {
                tradeMgr.showFormTips('buy', Lang.balance_insufficient);
            }
        },

        // ------------------------卖出联动------------------------
        onSellPriceKeyUp: function () {
            filterInput(this);

            var sellPrice = this.value;
            var sellAmount = $sellAmount.val();
            var sellLimitMoney = $sellLimitMoney.val();

            if (Number(sellAmount) > 0) {                // 数量非空时，计算总金额

                var costMoney = Calculate.MultiPriceDownTruncation(Calculate.accMul(sellPrice, sellAmount), ok.priceTruncate(ok.symbol));

                $sellLimitMoney.val(costMoney);

            } else if (Number(sellLimitMoney) > 0) {     // 金额非空，计算卖出数量

                var realAmount = Calculate.MultiPriceDownTruncation(Calculate.accDiv(sellLimitMoney, sellPrice), ok.amountTruncate(ok.symbol));

                $sellAmount.val(realAmount);

                tradeMgr.onSellCostAmountChange(realAmount);
            }
        },
        onSellAmountKeyUp: function () {
            filterInput(this);

            var sellAmount = this.value;
            var sellPrice = $sellPrice.val();

            if (Number(sellPrice) >= 0) {

                var costMoney = Calculate.MultiPriceDownTruncation(Calculate.accMul(sellPrice, sellAmount), ok.priceTruncate(ok.symbol));

                $sellLimitMoney.val(costMoney);

                tradeMgr.onSellCostAmountChange(sellAmount);
            }
        },
        onSellLimitMoneyKeyUp: function () {
            filterInput(this);

            var sellLimitMoney = this.value;
            var sellPrice = $sellPrice.val();

            if (Number(sellPrice) > 0) {             // 价格非空，计算买入数量

                var realAmount = Calculate.MultiPriceDownTruncation(Calculate.accDiv(sellLimitMoney, sellPrice), ok.amountTruncate(ok.symbol));

                $sellAmount.val(realAmount);

                tradeMgr.onSellCostAmountChange(realAmount);
            }
        },
        onSellMarketMoneyKeyUp: function () {
            filterInput(this);

            tradeMgr.onSellCostAmountChange(this.value);
        },
        onSellCostAmountChange: function (costAmount) {

            var totalCoinBalance = Numbers.filter($('#canSellETC').html());

            var percent = 0;

            if (Number(totalCoinBalance) > 0) {
                percent = Calculate.accDiv(costAmount, totalCoinBalance) * 100;
            }

            sellSlider.setPercent(percent);

            if (Number(costAmount) > Number(totalCoinBalance)) {

                tradeMgr.showFormTips('sell', Lang.balance_insufficient);
            }
        },

        // 买入下单操作
        submitBuy: function () {

            tradeMgr.showFormTips('buy', '');

            if (tradeData.buySubmitting) {
                return;
            }

            var buyPrice = $buyPrice.val();
            var buyAmount = $buyAmount.val();
            var buyType = $buyTypeSelect.val();
            var buyLimitMoney = $buyLimitMoney.val();
            var buyMarketMoney = $buyMarketMoney.val();

            var canUseCny = Numbers.filter($('#canUseCny').html());

            var isLimit = buyType === '0';
            var isMarket = !isLimit;

            // 限价买 直接验证
            if (isLimit) {

                if (buyPrice.trim() === '') {
                    tradeMgr.showFormTips('buy', Lang.price_empty);
                    return;
                }

                if (Number(buyPrice) <= 0) {
                    tradeMgr.showFormTips('buy', Lang.price_wrong);
                    return;
                }

                if (buyAmount.trim() === '') {
                    tradeMgr.showFormTips('buy', Lang.amount_empty);
                    return;
                }

                // 最小买入量验证
                if (Number(buyAmount) < MIN_AMOUNT) {
                    tradeMgr.showFormTips('buy', Lang.least_unit_001.replace('@currency', ok.tradeCurrency));
                    return;
                }

                // 最大值验证
                if (Number(buyLimitMoney) > Number(canUseCny)) {
                    tradeMgr.showFormTips('buy', Lang.balance_insufficient);
                    return;
                }
            }

            // 市价买
            if (isMarket) {

                var nowPrice = $nowPrice.val();
                var realAmount = Calculate.accDiv(buyMarketMoney, nowPrice);

                // 算出实际买入量之后验证
                if (Number(realAmount) < MIN_AMOUNT) {
                    tradeMgr.showFormTips('buy', Lang.least_unit_001.replace('@currency', ok.tradeCurrency));
                    return;
                }

                // 最大值验证
                if (Number(buyMarketMoney) > Number(canUseCny)) {
                    tradeMgr.showFormTips('buy', Lang.balance_insufficient);
                    return;
                }
            }

            // 资金密码验证
            if ($isopen.val() === '1' && $buyPwd.val() === '') {
                tradeMgr.showFormTips('buy', Lang.input_password);
                return;
            }

            var param = {
                side: 1,
                price: buyPrice,
                orderType: buyType
            };

            if (isLimit) {
                param.size = buyAmount;
            } else {
                param.quoteSize = buyMarketMoney;
            }

            // 资金密码参数
            if ($isopen.val() === '1') {
                param.tradePasswd = $buyPwd.val();
            }

            tradeData.buySubmitting = true;

            // 下单
            ajax.dealOrder({
                opt: {
                    symbol: ok.symbol
                },
                data: param,
                success: function (data) {
                    tradeData.buySubmitting = false;

                    tradeMgr.resetBuyForm();
                    tradeMgr.showFormTips('buy', Lang.entrust_success);

                    window.loadTradePwdConfig();

                    //买入埋点记录
                    okLog.eventLog({
                        platform: 2,
                        eventId: "click_buy_buypart_charts"
                    });
                },
                fail: function (message) {
                    tradeData.buySubmitting = false;
                    tradeMgr.showFormTips('buy', message);
                }
            });
        },
        resetBuyForm: function () {
            $buyAmount.val('');
            $buyLimitMoney.val('');
            $buyMarketMoney.val('');

            $('#buyPwd').val('');

            tradeMgr.onBuyCostMoneyChange(0);
        },

        // 卖出下单操作
        submitSell: function () {

            tradeMgr.showFormTips('sell', '');

            if (tradeData.sellSubmitting) {
                return;
            }

            var sellPrice = $sellPrice.val();
            var sellAmount = $sellAmount.val();
            var sellType = $sellTypeSelect.val();
            var sellMarketMoney = $sellMarketMoney.val();

            var canSellETC = Numbers.filter($('#canSellETC').html());

            var isLimit = sellType === '0';
            var isMarket = !isLimit;

            if (isLimit) {

                if (sellPrice.trim() === '') {
                    tradeMgr.showFormTips('sell', Lang.price_empty);
                    return;
                }

                if (Number(sellPrice) <= 0) {
                    tradeMgr.showFormTips('sell', Lang.price_wrong);
                    return;
                }

                if (sellAmount.trim() === '') {
                    tradeMgr.showFormTips('sell', Lang.amount_empty);
                    return;
                }
            }

            if (isMarket) {

                if (sellMarketMoney.trim() === '') {
                    tradeMgr.showFormTips('sell', Lang.amount_empty);
                    return;
                }
            }

            var limitTooSmall = isLimit && Number(sellAmount) < MIN_AMOUNT;
            var marketTooSmall = isMarket && Number(sellMarketMoney) < MIN_AMOUNT;

            var limitTooBig = isLimit && Number(sellAmount) > Number(canSellETC);
            var marketTooBig = isMarket && Number(sellMarketMoney) > Number(canSellETC);

            // 最小量验证
            if (limitTooSmall || marketTooSmall) {
                tradeMgr.showFormTips('sell', Lang.least_unit_001.replace('@currency', ok.tradeCurrency));
                return;
            }

            // 最大量验证
            if (limitTooBig || marketTooBig) {
                tradeMgr.showFormTips('sell', Lang.balance_insufficient);
                return;
            }

            // 资金密码验证
            if ($isopen.val() === '1' && $sellPwd.val() === '') {
                tradeMgr.showFormTips('sell', Lang.input_password);
                return;
            }

            var param = {
                side: 2,
                price: sellPrice,
                orderType: sellType
            };

            if (isLimit) {
                param.size = sellAmount;
            } else {
                param.size = sellMarketMoney;
            }

            // 资金密码参数
            if ($isopen.val() === '1') {
                param.tradePasswd = $sellPwd.val();
            }

            tradeData.sellSubmitting = true;

            // 下单
            ajax.dealOrder({
                opt: {
                    symbol: ok.symbol
                },
                data: param,
                success: function (data) {
                    tradeData.sellSubmitting = false;

                    tradeMgr.resetSellForm();
                    tradeMgr.showFormTips('sell', Lang.entrust_success);

                    window.loadTradePwdConfig();

                    //卖出埋点记录
                    okLog.eventLog({
                        platform: 2,
                        eventId: "click_sell_sellpart_charts"
                    });
                },
                fail: function (message) {
                    tradeData.sellSubmitting = false;
                    tradeMgr.showFormTips('sell', message);
                }
            });
        },
        resetSellForm: function () {
            $sellAmount.val('');
            $sellLimitMoney.val('');
            $sellMarketMoney.val('');

            $('#sellPwd').val('');

            tradeMgr.onSellCostAmountChange(0);
        },

        showFormTips: function (type, msg) {

            if (type === 'buy') {

                $buyTips.html(msg);

                clearTimeout(buyTipsTimer);

                buyTipsTimer = setTimeout(function () {
                    $buyTips.html('');
                }, 3000);

            } else {

                $sellTips.html(msg);

                clearTimeout(sellTipsTimer);

                sellTipsTimer = setTimeout(function () {
                    $sellTips.html('');
                }, 3000);

            }
        },

        updateBuyFormByDepth: function (data) {
            $buyTypeSelect.val(0);
            $buyLimitView.show();
            $buyMarketView.hide();

            tradeMgr.resetBuyForm();

            var availableAmount = Calculate.accDiv(Numbers.filter($('#canUseCny').html()), data.price);
            var realAmount = Math.min(data.amount, availableAmount);
            var costMoney = Calculate.accMul(data.price, realAmount);

            $buyPrice.val(Numbers.formatDigits(data.price, 8));
            $buyAmount.val(Numbers.formatDigits(realAmount, 8));
            $buyLimitMoney.val(Numbers.formatDigits(costMoney, 8));

            tradeMgr.onBuyCostMoneyChange(costMoney);
        },
        updateSellFormByDepth: function (data) {
            $sellTypeSelect.val(0);
            $sellLimitView.show();
            $sellMarketView.hide();

            tradeMgr.resetSellForm();

            var realAmount = Math.min(data.amount, Numbers.filter($('#canSellETC').html()));
            var costMoney = Calculate.accMul(data.price, realAmount);

            $sellPrice.val(Numbers.formatDigits(data.price, 8));
            $sellAmount.val(Numbers.formatDigits(realAmount, 8));
            $sellLimitMoney.val(Numbers.formatDigits(costMoney, 8));

            tradeMgr.onSellCostAmountChange(realAmount);
        }
    };

    return {
        init: function () {
            tradeMgr.initSliders();
            tradeMgr.initEvents();
        },
        updateBuyFormByDepth: function (data) {
            tradeMgr.updateBuyFormByDepth(data);
        },
        updateSellFormByDepth: function (data) {
            tradeMgr.updateSellFormByDepth(data);
        },
        resetAllForms: function () {
            tradeMgr.resetBuyForm();
            tradeMgr.resetSellForm();

            $buyTypeSelect.val(0);
            $sellTypeSelect.val(0);

            $buyLimitView.show();
            $buyMarketView.hide();

            $sellLimitView.show();
            $sellMarketView.hide();

            $buyPrice.val('');
            $sellPrice.val('');
        }
    }

});