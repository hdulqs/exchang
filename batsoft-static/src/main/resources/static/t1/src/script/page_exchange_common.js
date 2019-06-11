/**
 *<p>page_exchange_eventLog.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {
   var U = require('./module_utils'),
       core = require('./module_core');
    var ExchangeRate = "";

    /**
     * 获取买一 卖一
     */
    function buy_sell_first(){
        $.get("/exchange/buy_sell_first/buy/" + $("#base_asset").val()+"_"+$("#quote_asset").val(), function ($data) {
                    var _data=JSON.parse($data.data);
                    if($data == "page404"){
                        window.location.href = "/404";
                    }else{
                        $("#price_buy").val(_data.price);
                    }
                if(_data.price_decimal){
                    var num = _data.price_decimal;
                    var reg = new RegExp("^(\\-)*(\\d+)\\.(\\d{"+num+"}).*$")
                    $("#price_buy").on('input propertychange',function(){
                        this.value = this.value.replace(reg,'$1$2.$3');
                    });
                    $("#price_sell").on('input propertychange',function(){
                        ///^(\-)*(\d+)\.(\d{5}).*$/
                        this.value = this.value.replace(reg,'$1$2.$3');
                    });
                }

        });
        $.get("/exchange/buy_sell_first/sell/" +$("#base_asset").val()+"_"+$("#quote_asset").val(), function ($data) {
            var _data=JSON.parse($data.data);
            if($data == "page404"){
                window.location.href = "/404";
            }else{
                $("#price_sell").val(_data.price);
            }
        });
    }

    /**
     * 获取可用资金
     */
    function bal() {
        $.get("/ex/member/account/bal/" + $("#base_asset").val(), function ($data) {
            $("#base_account").text($data);
        });
        $.get("/ex/member/account/bal/" + $("#quote_asset").val(), function ($data) {
            $("#quote_account").text($data);
        });
    }

    function bal() {
        $.get("/ex/member/account/bal/" + $("#base_asset").val(), function ($data) {
            $("#base_account").text($data);
        });
        $.get("/ex/member/account/bal/" + $("#quote_asset").val(), function ($data) {
            $("#quote_account").text($data);
        });
    }
    function guzhi(num) {
        if (U.is_null(ExchangeRate) == true) {
            ExchangeRate = $("#exchange_rate").val();
        }
        if ($("#languageBt").hasClass("cn")) {
            var number = num.toFixed(2);
            return U.bccomp(number, 0) <= 0 ? "≈￥0.01" : ("￥" + number);
        } else {
            var number = bcdiv(num, ExchangeRate).toFixed(2);
            return U.bccomp(number, 0) <= 0 ? "≈$0.01" : ("$" + number);
        }
    }
    function againstCNY(symbol) {
        /*if (symbol == 'bitCNY'||symbol=='QC') {
            return 1;
        }
        if (U.is_null(AssetExchangeRate['bitCNY'][symbol]) == false) {
            return AssetExchangeRate['bitCNY'][symbol];
        }
        if (symbol == 'USC') {
            ExchangeRate = U.is_null(ExchangeRate) == true ? ("#exchange_rate").val() : ExchangeRate;
            return ExchangeRate;
        }
        if (U.is_null(AssetExchangeRate['BTC'][symbol]) == false && U.is_null(AssetExchangeRate['bitCNY']['BTC']) == false) {
            return AssetExchangeRate['BTC'][symbol] * AssetExchangeRate['bitCNY']['BTC'];
        }
        if (U.is_null(AssetExchangeRate['ETH'][symbol]) == false && U.is_null(AssetExchangeRate['bitCNY']['ETH']) == false) {
            return AssetExchangeRate['ETH'][symbol] * AssetExchangeRate['bitCNY']['ETH'];
        }
        if (U.is_null(AssetExchangeRate['USC'][symbol]) == false && U.is_null(AssetExchangeRate['USC']['ETH']) == false && U.is_null(AssetExchangeRate['bitCNY']['ETH']) == false) {
            return AssetExchangeRate['USC'][symbol] / AssetExchangeRate['USC']['ETH'] * AssetExchangeRate['bitCNY']['ETH'];
        }
        return 0;*/
        return 1;
    }
    function init(){
        $(function () {
            var asset_exchange_rate = $("#asset_exchange_rate").val();
            if (asset_exchange_rate.length > 0) {
                try {
                    AssetExchangeRate = $.parseJSON(asset_exchange_rate);
                }catch(err)
                {
                    console.log("json erro asset_exchange_rate=" +asset_exchange_rate );
                }
            }

        });

        if(core.isLogin()){
            bal();
        }

    }
    function digitsData(){

        /*core.list({url:BASE_PATH+ "/exchange/findDigitsMap"}).then(function (data) {
            // $("#asset_exchange_rate").val(data.tradeCoins);
            // $("#pairs_decimal").val(data.digits);
            // tpl.render({"tplId": "#gidits_tpl", "renderId": "#gidits_render", "data": data});
        })*/
    }

    exports.init=init;
    exports.guzhi=guzhi;
    exports.againstCNY=againstCNY;
    exports.digitsData = digitsData;
    exports.bal=bal;
    exports.buy_sell_first=buy_sell_first;
});