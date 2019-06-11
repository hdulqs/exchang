define(["backbone","Calculate","tradeSliderBar","ajax"],function(){function filterInput(input){if($(input).attr("data-last-val")!==input.value){var start=input.selectionStart,end=input.selectionEnd;input.value=Numbers.filter(input.value,8),input.setSelectionRange(start,end),$(input).attr("data-last-val",input.value)}}var buySlider,sellSlider,buyTipsTimer,sellTipsTimer,ajax=require("ajax"),MIN_AMOUNT=.001,Calculate=require("Calculate"),TradeSliderBar=require("tradeSliderBar"),$buyTypeSelect=$("#buyTypeSelect"),$sellTypeSelect=$("#sellTypeSelect"),$buyLimitView=$("#buyLimitView"),$buyMarketView=$("#buyMarketView"),$sellLimitView=$("#sellLimitView"),$sellMarketView=$("#sellMarketView"),$buyPrice=$("#buyPrice"),$buyAmount=$("#buyAmount"),$buyLimitMoney=$("#buyLimitMoney"),$buyMarketMoney=$("#buyMarketMoney"),$sellPrice=$("#sellPrice"),$sellAmount=$("#sellAmount"),$sellLimitMoney=$("#sellLimitMoney"),$sellMarketMoney=$("#sellMarketMoney"),$buyTips=$("#buyTips"),$sellTips=$("#sellTips"),$nowPrice=$("#nowPrice"),$buyPwd=$("#buyPwd"),$sellPwd=$("#sellPwd"),$isopen=$("#isopen"),tradeData={buySubmitting:!1,sellSubmitting:!1},tradeMgr={initSliders:function(){buySlider=new TradeSliderBar({containerId:"buySlider",roundNumber:!0,type:"buy"}),sellSlider=new TradeSliderBar({containerId:"sellSlider",roundNumber:!0,type:"sell"}),buySlider.onPercentChange(function(percent){var floatValue=.01*percent;floatValue.toString().length>6&&(floatValue=floatValue.toFixed(4));var buyPrice=$buyPrice.val(),totalCny=Numbers.filter($("#canUseCny").html()),currCny=Calculate.accMul(totalCny,floatValue),currAmount=0;if(Number(buyPrice)>0&&(currAmount=Calculate.accDiv(currCny,buyPrice)),percent>0){var money=Calculate.floor(currCny,ok.priceTruncate(ok.symbol)),amount=Calculate.floor(currAmount,ok.amountTruncate(ok.symbol));$buyLimitMoney.val(money),$buyMarketMoney.val(money),$buyAmount.val(amount)}else $buyLimitMoney.val(""),$buyMarketMoney.val(""),$buyAmount.val("")}),sellSlider.onPercentChange(function(percent){var floatValue=.01*percent;floatValue.toString().length>6&&(floatValue=floatValue.toFixed(4));var sellPrice=$sellPrice.val(),totalAmount=Numbers.filter($("#canSellETC").html()),currAmount=Calculate.accMul(totalAmount,floatValue),currCny=0;if(Number(sellPrice)>0&&(currCny=Calculate.accMul(currAmount,sellPrice)),percent>0){var amount=Calculate.floor(currAmount,ok.amountTruncate(ok.symbol)),money=Calculate.floor(currCny,ok.priceTruncate(ok.symbol));$sellAmount.val(amount),$sellLimitMoney.val(money),$sellMarketMoney.val(amount)}else $sellAmount.val(""),$sellLimitMoney.val(""),$sellMarketMoney.val("")})},initEvents:function(){var self=this;$buyTypeSelect.change(this.onBuyTypeChange),$sellTypeSelect.change(this.onSellTypeChange),$buyPrice.keyup(this.onBuyPriceKeyUp),$buyAmount.keyup(this.onBuyAmountKeyUp),$buyLimitMoney.keyup(this.onBuyLimitMoneyKeyUp),$buyMarketMoney.keyup(this.onBuyMarketMoneyKeyUp),$sellPrice.keyup(this.onSellPriceKeyUp),$sellAmount.keyup(this.onSellAmountKeyUp),$sellLimitMoney.keyup(this.onSellLimitMoneyKeyUp),$sellMarketMoney.keyup(this.onSellMarketMoneyKeyUp),$("#buyBtn").click(function(){self.submitBuy()}),$("#sellBtn").click(function(){self.submitSell()}),$(".recharge-word").click(function(){okLog.eventLog({platform:2,eventId:"click_moneytransfer_buypart_charts"})})},onBuyTypeChange:function(){var buyType=$buyTypeSelect.val(),eventId="";"0"===buyType?($buyLimitView.show(),$buyMarketView.hide(),$buyAmount.focus(),eventId="click_pricelimit_buypart_charts"):($buyLimitView.hide(),$buyMarketView.show(),$buyMarketMoney.focus(),eventId="click_pricenow_buypart_charts"),tradeMgr.resetBuyForm(),okLog.eventLog({platform:2,eventId:eventId})},onSellTypeChange:function(){var sellType=$sellTypeSelect.val(),eventId="";"0"===sellType?($sellLimitView.show(),$sellMarketView.hide(),$sellAmount.focus(),eventId="click_pricelimit_sellpart_charts"):($sellLimitView.hide(),$sellMarketView.show(),$sellMarketMoney.focus(),eventId="click_pricenow_sellpart_charts"),tradeMgr.resetSellForm(),okLog.eventLog({platform:2,eventId:eventId})},onBuyPriceKeyUp:function(){filterInput(this);var buyPrice=this.value,buyAmount=$buyAmount.val(),buyLimitMoney=$buyLimitMoney.val();if(Number(buyAmount)>0){var costMoney=Calculate.MultiPriceDownTruncation(Calculate.accMul(buyPrice,buyAmount),ok.priceTruncate(ok.symbol));$buyLimitMoney.val(costMoney),tradeMgr.onBuyCostMoneyChange(costMoney)}else if(Number(buyLimitMoney)>0){var realAmount=0;Number(buyPrice)>0&&(realAmount=Calculate.MultiPriceDownTruncation(Calculate.accDiv(buyLimitMoney,buyPrice),ok.amountTruncate(ok.symbol))),$buyAmount.val(realAmount)}},onBuyAmountKeyUp:function(){filterInput(this);var buyAmount=this.value,buyPrice=$buyPrice.val();if(Number(buyPrice)>=0){var costMoney=Calculate.MultiPriceDownTruncation(Calculate.accMul(buyPrice,buyAmount),ok.priceTruncate(ok.symbol));$buyLimitMoney.val(costMoney),tradeMgr.onBuyCostMoneyChange(costMoney)}},onBuyLimitMoneyKeyUp:function(){filterInput(this);var buyLimitMoney=this.value,buyPrice=$buyPrice.val();if(Number(buyPrice)>0){var realAmount=Calculate.MultiPriceDownTruncation(Calculate.accDiv(buyLimitMoney,buyPrice),ok.amountTruncate(ok.symbol));$buyAmount.val(realAmount),tradeMgr.onBuyCostMoneyChange(buyLimitMoney)}},onBuyMarketMoneyKeyUp:function(){filterInput(this),tradeMgr.onBuyCostMoneyChange(this.value)},onBuyCostMoneyChange:function(costMoney){var totalBalance=Numbers.filter($("#canUseCny").html()),percent=0;Number(totalBalance)>0&&(percent=100*Calculate.accDiv(costMoney,totalBalance)),buySlider.setPercent(percent),Number(costMoney)>Number(totalBalance)&&tradeMgr.showFormTips("buy",Lang.balance_insufficient)},onSellPriceKeyUp:function(){filterInput(this);var sellPrice=this.value,sellAmount=$sellAmount.val(),sellLimitMoney=$sellLimitMoney.val();if(Number(sellAmount)>0){var costMoney=Calculate.MultiPriceDownTruncation(Calculate.accMul(sellPrice,sellAmount),ok.priceTruncate(ok.symbol));$sellLimitMoney.val(costMoney)}else if(Number(sellLimitMoney)>0){var realAmount=Calculate.MultiPriceDownTruncation(Calculate.accDiv(sellLimitMoney,sellPrice),ok.amountTruncate(ok.symbol));$sellAmount.val(realAmount),tradeMgr.onSellCostAmountChange(realAmount)}},onSellAmountKeyUp:function(){filterInput(this);var sellAmount=this.value,sellPrice=$sellPrice.val();if(Number(sellPrice)>=0){var costMoney=Calculate.MultiPriceDownTruncation(Calculate.accMul(sellPrice,sellAmount),ok.priceTruncate(ok.symbol));$sellLimitMoney.val(costMoney),tradeMgr.onSellCostAmountChange(sellAmount)}},onSellLimitMoneyKeyUp:function(){filterInput(this);var sellLimitMoney=this.value,sellPrice=$sellPrice.val();if(Number(sellPrice)>0){var realAmount=Calculate.MultiPriceDownTruncation(Calculate.accDiv(sellLimitMoney,sellPrice),ok.amountTruncate(ok.symbol));$sellAmount.val(realAmount),tradeMgr.onSellCostAmountChange(realAmount)}},onSellMarketMoneyKeyUp:function(){filterInput(this),tradeMgr.onSellCostAmountChange(this.value)},onSellCostAmountChange:function(costAmount){var totalCoinBalance=Numbers.filter($("#canSellETC").html()),percent=0;Number(totalCoinBalance)>0&&(percent=100*Calculate.accDiv(costAmount,totalCoinBalance)),sellSlider.setPercent(percent),Number(costAmount)>Number(totalCoinBalance)&&tradeMgr.showFormTips("sell",Lang.balance_insufficient)},submitBuy:function(){if(tradeMgr.showFormTips("buy",""),!tradeData.buySubmitting){var buyPrice=$buyPrice.val(),buyAmount=$buyAmount.val(),buyType=$buyTypeSelect.val(),buyLimitMoney=$buyLimitMoney.val(),buyMarketMoney=$buyMarketMoney.val(),canUseCny=Numbers.filter($("#canUseCny").html()),isLimit="0"===buyType,isMarket=!isLimit;if(isLimit){if(""===buyPrice.trim())return void tradeMgr.showFormTips("buy",Lang.price_empty);if(Number(buyPrice)<=0)return void tradeMgr.showFormTips("buy",Lang.price_wrong);if(""===buyAmount.trim())return void tradeMgr.showFormTips("buy",Lang.amount_empty);if(Number(buyAmount)<MIN_AMOUNT)return void tradeMgr.showFormTips("buy",Lang.least_unit_001.replace("@currency",ok.tradeCurrency));if(Number(buyLimitMoney)>Number(canUseCny))return void tradeMgr.showFormTips("buy",Lang.balance_insufficient)}if(isMarket){var nowPrice=$nowPrice.val(),realAmount=Calculate.accDiv(buyMarketMoney,nowPrice);if(Number(realAmount)<MIN_AMOUNT)return void tradeMgr.showFormTips("buy",Lang.least_unit_001.replace("@currency",ok.tradeCurrency));if(Number(buyMarketMoney)>Number(canUseCny))return void tradeMgr.showFormTips("buy",Lang.balance_insufficient)}if("1"===$isopen.val()&&""===$buyPwd.val())return void tradeMgr.showFormTips("buy",Lang.input_password);var param={side:1,price:buyPrice,orderType:buyType};isLimit?param.size=buyAmount:param.quoteSize=buyMarketMoney,"1"===$isopen.val()&&(param.tradePasswd=$buyPwd.val()),tradeData.buySubmitting=!0,ajax.dealOrder({opt:{symbol:ok.symbol},data:param,success:function(data){tradeData.buySubmitting=!1,tradeMgr.resetBuyForm(),tradeMgr.showFormTips("buy",Lang.entrust_success),window.loadTradePwdConfig(),okLog.eventLog({platform:2,eventId:"click_buy_buypart_charts"})},fail:function(message){tradeData.buySubmitting=!1,tradeMgr.showFormTips("buy",message)}})}},resetBuyForm:function(){$buyAmount.val(""),$buyLimitMoney.val(""),$buyMarketMoney.val(""),$("#buyPwd").val(""),tradeMgr.onBuyCostMoneyChange(0)},submitSell:function(){if(tradeMgr.showFormTips("sell",""),!tradeData.sellSubmitting){var sellPrice=$sellPrice.val(),sellAmount=$sellAmount.val(),sellType=$sellTypeSelect.val(),sellMarketMoney=$sellMarketMoney.val(),canSellETC=Numbers.filter($("#canSellETC").html()),isLimit="0"===sellType,isMarket=!isLimit;if(isLimit){if(""===sellPrice.trim())return void tradeMgr.showFormTips("sell",Lang.price_empty);if(Number(sellPrice)<=0)return void tradeMgr.showFormTips("sell",Lang.price_wrong);if(""===sellAmount.trim())return void tradeMgr.showFormTips("sell",Lang.amount_empty)}if(isMarket&&""===sellMarketMoney.trim())return void tradeMgr.showFormTips("sell",Lang.amount_empty);var limitTooSmall=isLimit&&Number(sellAmount)<MIN_AMOUNT,marketTooSmall=isMarket&&Number(sellMarketMoney)<MIN_AMOUNT,limitTooBig=isLimit&&Number(sellAmount)>Number(canSellETC),marketTooBig=isMarket&&Number(sellMarketMoney)>Number(canSellETC);if(limitTooSmall||marketTooSmall)return void tradeMgr.showFormTips("sell",Lang.least_unit_001.replace("@currency",ok.tradeCurrency));if(limitTooBig||marketTooBig)return void tradeMgr.showFormTips("sell",Lang.balance_insufficient);if("1"===$isopen.val()&&""===$sellPwd.val())return void tradeMgr.showFormTips("sell",Lang.input_password);var param={side:2,price:sellPrice,orderType:sellType};isLimit?param.size=sellAmount:param.size=sellMarketMoney,"1"===$isopen.val()&&(param.tradePasswd=$sellPwd.val()),tradeData.sellSubmitting=!0,ajax.dealOrder({opt:{symbol:ok.symbol},data:param,success:function(data){tradeData.sellSubmitting=!1,tradeMgr.resetSellForm(),tradeMgr.showFormTips("sell",Lang.entrust_success),window.loadTradePwdConfig(),okLog.eventLog({platform:2,eventId:"click_sell_sellpart_charts"})},fail:function(message){tradeData.sellSubmitting=!1,tradeMgr.showFormTips("sell",message)}})}},resetSellForm:function(){$sellAmount.val(""),$sellLimitMoney.val(""),$sellMarketMoney.val(""),$("#sellPwd").val(""),tradeMgr.onSellCostAmountChange(0)},showFormTips:function(type,msg){"buy"===type?($buyTips.html(msg),clearTimeout(buyTipsTimer),buyTipsTimer=setTimeout(function(){$buyTips.html("")},3e3)):($sellTips.html(msg),clearTimeout(sellTipsTimer),sellTipsTimer=setTimeout(function(){$sellTips.html("")},3e3))},updateBuyFormByDepth:function(data){$buyTypeSelect.val(0),$buyLimitView.show(),$buyMarketView.hide(),tradeMgr.resetBuyForm();var availableAmount=Calculate.accDiv(Numbers.filter($("#canUseCny").html()),data.price),realAmount=Math.min(data.amount,availableAmount),costMoney=Calculate.accMul(data.price,realAmount);$buyPrice.val(Numbers.formatDigits(data.price,8)),$buyAmount.val(Numbers.formatDigits(realAmount,8)),$buyLimitMoney.val(Numbers.formatDigits(costMoney,8)),tradeMgr.onBuyCostMoneyChange(costMoney)},updateSellFormByDepth:function(data){$sellTypeSelect.val(0),$sellLimitView.show(),$sellMarketView.hide(),tradeMgr.resetSellForm();var realAmount=Math.min(data.amount,Numbers.filter($("#canSellETC").html())),costMoney=Calculate.accMul(data.price,realAmount);$sellPrice.val(Numbers.formatDigits(data.price,8)),$sellAmount.val(Numbers.formatDigits(realAmount,8)),$sellLimitMoney.val(Numbers.formatDigits(costMoney,8)),tradeMgr.onSellCostAmountChange(realAmount)}};return{init:function(){tradeMgr.initSliders(),tradeMgr.initEvents()},updateBuyFormByDepth:function(data){tradeMgr.updateBuyFormByDepth(data)},updateSellFormByDepth:function(data){tradeMgr.updateSellFormByDepth(data)},resetAllForms:function(){tradeMgr.resetBuyForm(),tradeMgr.resetSellForm(),$buyTypeSelect.val(0),$sellTypeSelect.val(0),$buyLimitView.show(),$buyMarketView.hide(),$sellLimitView.show(),$sellMarketView.hide(),$buyPrice.val(""),$sellPrice.val("")}}});