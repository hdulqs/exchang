/**
 * Created by Bat Admin on 2016/03/21.
 * 系统主页
 */
define(function (require,exports) {
    var socket;
    var AssetExchangeRate;
    var PairsDecimal = {};
    var Loading = 1;
    var currentindex = 1;
    var countFocus =0;
    var stompClient;

    var trade_area={};

    // 页面所有的计时器
    var pageTimer = {
        'USDT': {},
        'ETH': {},
        'BT': {}
    };

    var core     =require('./module_core'),
        C = require('./page_exchange_common');
    tpl = require('./module_tpl');
    U = require("./module_utils");

    function fav(pairs_id, state) {
        $.get("/pairs/fav", {"pairs_id": pairs_id, "state": state}, function (data) {
            response(data);
        });
    }
    //點擊收藏
    function saveCoinCollection(tradeCoinCode, pricingCoinCode, $e) {
        $.post("/exchange/svCoinCollection", {"tradeCoinCode": tradeCoinCode, "pricingCoinCode": pricingCoinCode}, function (data) {
            if(data.msg == '未登录') {
                window.location.href = '/login'
            }else{
                U.response(data);
                $e.toggleClass('favorited')
                initAreaCoins()
            }
        });

    }
    function deleteCoinCollection(id, $tr, $e) {
        $.get("/exchange/dteCoinCollection", {"id": id}, function (data) {
            if(data.msg == '未登录') {
                window.location.href = '/login'
            }else{
                if($('.cur').attr('data-code') == 'collect') {
                    $tr.remove()
                }else{
                    $e.toggleClass('favorited')
                    initAreaCoins()
                }

                //response(data);
            }

        });
    }

    function Connect() {
        try {
            var socket = new SockJS($("#wss_url").val());
            // var socket = new SockJS("http://47.52.202.171/");
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                sOpen();
                sMessage();
                Send();
                //console.log('Connected:ticker ' + frame);
                stompClient.subscribe("/exchange/" + 'all@ticker', function(respnose){ //订阅/topic/getResponse 目标发送的消息。这个是在控制器的@SendTo中定义的。
                    sMessage(JSON.parse(respnose.body));
                });

                stompClient.debug = function(str) {
                    // str 参数即为 debug 信息
                    // append the debug log to a #debug div somewhere in the page using JQuery:
                    // console.log('info==== ' + str);
                };

            });

        } catch (e) {
            console.log("book connect error");
            //setTimeout(Connect, timeout);
            return;
        }
    }

    function sOpen() {
        console.log('connect success!');
        if (Loading == 1) {
            Loading = 0;
            $("#loading").remove();
            $(".wrapper").show();
        }
    }

    function sError(e) {
        console.log("error " + e);
    }

    function sMessage(msg) {

        if (msg == null) {
            console.log('msg is null');
            return false;
        }
        var i = JSON.parse(msg.data);
        // console.log(i,"KSAD")
        // console.log('sMessage')
        // console.log(i)
        if (i[0] == undefined) {
            return;
        }

        var last_price = i[7];
        var change_rate = i[6];
        var high_price = i[9];
        var low_price = i[10];
        var volume = i[8];
        var pairs = i[0];
        var old_last_price = i[13];


        updateTradeAreaData(i);
        updateAssetExchangeRate(pairs, last_price);
        if ($(".last_price_" + i[0]).length == 0) {
            return false;
        }
        //var old_last_price = U.unFormatcoin($(".last_price_" + i[0]).first().text());

        $(".last_price_" + i[0]).html(U.formatcoin(last_price, PairsDecimal[pairs]['price_decimal']));
        $(".prelastprice_" + i[0]).html(old_last_price)

        // if (U.bccomp(last_price, old_last_price) < 0) {
        //     $(".last_price_" + i[0]).addClass("red").removeClass("green");
        // } else if (U.bccomp(last_price, old_last_price) == 0) {
        //     $(".last_price_" + i[0]).removeClass("green").removeClass("red");
        // } else {
        //     $(".last_price_" + i[0]).addClass("green").removeClass("red");
        // }
        if (change_rate < 0) {
            $(".last_price_" + i[0]).addClass("red").removeClass("green");
        } else if (change_rate == 0) {
            $(".last_price_" + i[0]).addClass("#c7cce6").removeClass("green").removeClass("red");
        } else {
            $(".last_price_" + i[0]).addClass("green").removeClass("red");
        }
        var tr = $(".last_price_" + i[0]).parents(".t_cont tr");
        var rate = tr.find(".price[rate]").attr('rate');
        tr.find(".price[rate]").text(C.guzhi(U.bcmul(last_price, rate)));
        var hot_tr = $(".last_price_" + i[0]).parents(".hotMarkets .item");
        hot_tr.find(".price[rate]").text(C.guzhi(U.bcmul(last_price, rate)));
        var t = Number(change_rate).toFixed(2) + "%";
        $(".change_rate_" + i[0]).html(t);
        $(".change_top_" + i[0]).html(t);
        if (change_rate < 0) {
            $(".change_rate_" + i[0]).addClass("red").removeClass("green").removeClass("gray");
            $(".change_top_" + i[0]).addClass("desc").removeClass("asc");
            $(".last_price_" + i[0]).addClass("red").removeClass("green").removeClass("gray");

        } else {
            $(".change_rate_" + i[0]).addClass("green").removeClass("red").removeClass("gray");
            $(".change_top_" + i[0]).addClass("asc").removeClass("desc");
            $(".last_price_" + i[0]).addClass("green").removeClass("red").removeClass("gray");
        }
        $(".high_price_" + i[0]).html(U.formatcoin(high_price, PairsDecimal[pairs]['price_decimal']));
        $(".low_price_" + i[0]).html(U.formatcoin(low_price, PairsDecimal[pairs]['price_decimal']));
        $(".volume_" + i[0]).html(U.formatcoin(Number(volume).toFixed(2), 2));

    }

    function sClose(e) {
        setTimeout(Connect, 5000);
    }

    function Send() {
        stompClient.send("/init_ticker", {}, "");
    }

    function Close() {
        socket.close();
    }

    function search() {
        $(".coin_k").css("display","none");
        var txt = $.trim($(".searchInput").val().toUpperCase());
        if (txt != "") {
            $("#godats:visible tr:not('.theader')").hide().each(function () {
                var keyword = $(this).find("td:eq(1)").text().toUpperCase();
                if (keyword.indexOf(txt) >= 0) {
                    $(this).show();
                }
            });
        } else {
            $("#godats:visible tr:not('.theader')").show();
        }

    }
    function searchs() {
        var txts = $.trim($(".searchInputE").val().toUpperCase());
        if (txts != "") {
            $("#godat:visible tr:not('.theader')").hide().each(function () {
                var keyword = $(this).find("td:eq(1)").text().toUpperCase();
                if (keyword.indexOf(txts) >= 0) {
                    $(this).show();
                }
            });
        } else {
            $("#godat:visible tr:not('.theader')").show();
        }
    };

    function updateAssetExchangeRate(pairs, last_price) {

        var pairs_data = pairs.split("_");
        var base_asset = pairs_data[0];
        var quote_asset = pairs_data[1];
        if (AssetExchangeRate[quote_asset] == undefined) {
            AssetExchangeRate[quote_asset] = {};
        }
        AssetExchangeRate[quote_asset][base_asset] = last_price;
        $("span.price[rate][pairs]").each(function () {
            var span_pairs = $(this).attr("pairs");
            if (span_pairs.length > 0) {
                var span_pairs_data = span_pairs.split("_");
                var span_rate = C.againstCNY(span_pairs_data[1]);
                $(this).attr("rate", span_rate);
            }
        });
    }


    /**
     * 更新缓冲交易区数据 24H 值
     * @param data
     */
    function updateTradeAreaData(i) {

        var last_price = i[7];
        var change_rate = i[6];
        var high_price = i[9];
        var low_price = i[10];
        var volume = i[8];
        var pairs = i[0];

        var pairs_data = pairs.split("_");
        var base_asset = pairs_data[0];
        var quote_asset = pairs_data[1];
        trade_area[quote_asset].forEach(function (item) {
            // if(item.coinCode===base_asset){
            //     item.last=last_price;
            //     item.high=high_price;
            //     item.low=low_price;
            //     item.rate=change_rate;
            //     item.volume=volume;
            // }

        })


    }



    var ExchangeRate = "";



    function charts() {
        var cnt = $("#pincount").val();
        if (cnt > 0) {
            for (var i = 0; i < cnt; i++) {
                name = "container" + i;
                // chart(name);
            }
        }
    }

    function chart(name) {
        var chart = null;
        var symbol = $("#" + name).attr("data");
        $.getJSON('/index/klines?symbol=' + symbol, function (data) {
            chart = Highcharts.chart(name, {
                credits: {enabled: false},
                style: {backgroundColor: "#ebebeb", spacingLeft: 0, marginLeft: 0,},
                chart: {zoomType: 'x'},
                title: {text: ''},
                subtitle: {text: document.ontouchstart === undefined ? '' : ''},
                margin: [0, 0, 0, 0],
                xAxis: {labels: {enabled: false}, visible: false, tickWidth: 0, lineWidth: 0, lineColor: '#FFFFFF',},
                yAxis: {labels: {enabled: false}, visible: false, gridLineWidth: 0, lineColor: '#FFFFFF',},
                tooltip: {
                    dateTimeLabelFormats: {
                        millisecond: '%H:%M:%S.%L',
                        second: '%H:%M:%S',
                        minute: '%H:%M',
                        hour: '%H:%M',
                        day: '%Y-%m-%d',
                        week: '%m-%d',
                        month: '%Y-%m',
                        year: '%Y'
                    }
                },
                legend: {enabled: false},
                plotOptions: {
                    area: {
                        fillColor: {
                            linearGradient: {x1: 0, y1: 0, x2: 0, y2: 1},
                            stops: [[0, '#FEFBF5'], [1, Highcharts.Color('#FEFBF5').setOpacity(1).get('rgba')]]
                        },
                        marker: {enabled: false,},
                        lineWidth: 1,
                        lineColor: '#F8E9CE',
                        states: {hover: {lineWidth: 1}},
                        threshold: null
                    }
                },
                series: [{type: 'area', name: '', data: data}]
            });
        });
    }

    // 原来index页面方法
    function changeflash(i) {
        i = i - 1;
        for (var j = 0; j < countFocus; j++) {
            if (j == i) {
                $(".focusList li:eq(" + j + ")").fadeIn("normal");
                $(".focusList li:eq(" + j + ")").css("display", "block");
                $(".pagination a:eq(" + j + ")").addClass("curr");
            } else {
                $(".focusList li:eq(" + j + ")").css("display", "none");
                $(".pagination a:eq(" + j + ")").removeClass("curr");
            }
        }
    }
    function timer_tick() {
        currentindex = currentindex >= countFocus ? 1 : currentindex + 1;
        changeflash(currentindex);
    }
    function startAm(){timerID = setInterval(timer_tick(), 3000);}
    function stopAm(){clearInterval(timerID);}

    function vol(){
        var vol=[];
        $(".marketsTable .t_cont:gt(0)").each(function(){
            var table=$(this);
            table.find("table tr:not(.theader)").each(function(){
                var td=$(this).find("td:last");
                var text=td.text();
                var volume=td.find("span").text();
                var symbol=$.trim(text.replace(volume,""));
                if(vol[symbol]==undefined){
                    vol[symbol]=0;
                }
                vol[symbol]=U.bcadd(vol[symbol],U.unFormatcoin(volume));
            });
        });
        var html=[];
        var isShow=true;
        for(var item in vol){
            if(vol[item]==0){
                isShow=false;
            }
            html.push('<span class="sVol"><span class="vol white">'+U.formatcoin(vol[item])+'</span><span class="unit gray">'+item+'</span></span>');
        }
        if(isShow==true){
            $("#sVol").html(html.join('<span class="sVol_hr">/</span>'));
            $("#sVol_box").removeClass("hide");
        }
    }

    function initEffect() {
        //---标签切换
        $('.tabs').each(function(){
            var it = $(this);
            var menus = $('.t_menu li',it);
            var conts = $('.t_cont',it);
            menus.on('click',function(){
                $(".coin_k").css("display","none");
                var _this = $(this);
                var code=_this.attr("data-code");
                if(_this.hasClass('cur')) return;
                var _index = _this.index();
                _this.addClass('cur').siblings().removeClass('cur');
                // conts.hide(0).eq(_index).stop().fadeIn();
                // console.log(code);
                renderAreaData(code);
                search();
            });
        });

        $('.tablist').each(function(){
            var it = $(this);
            var menulist = $('.t_menus li',it);
            var conts = $('.t_cont',it);
            menulist.on('click',function(){
                var _this = $(this);
                var code=_this.attr("data-code");
                if(_this.hasClass('curlist')) return;
                var _index = _this.index();
                _this.addClass('curlist').siblings().removeClass('curlist');
                // conts.hide(0).eq(_index).stop().fadeIn();
                // console.log(code);
                renderAreaDatas(code);
                searchs();
            });
        });
//        $(".searchBox .searchInput").focusin(function(){
//            $(this).parents(".searchBox").addClass("gold_border");
//        }).focusout(function(){
//            $(this).parents(".searchBox").removeClass("gold_border");
//        });
    }

    function clearAllPageTimer(code) {
        //清除页面的计时器
        if (code === 'all') {
            return;
        } else {
            // console.log(pageTimer);
            for(var each in pageTimer[code]){
                clearInterval(pageTimer[code][each]);
            }
            // console.log(pageTimer);
        }
    }


    /**
     * 加载 交易区币种
     */
    function initAreaCoins() {
        var self_aj =$('.sther').text();
        core.list({url:BASE_PATH+"/exchange/price/coins"}).then(function (data) {
            var selfSelected = {
                coinCode: "collect",
                coinName: self_aj,
                del: 0,
                page: 1,
                rows: 10
            };

            var sectionOneData = data.filter(function(d){
                // return d.coinName === "ETH" || d.coinName === "USDT"
                return  d.coinName === "USDT"
            });
            // var array = sectionOneData[0];
            // sectionOneData[0] = sectionOneData[1];
            // sectionOneData[1] = array;
            sectionOneData.push(selfSelected);
            var sectionTwoData = data.filter(function(d){
                return d.coinName === "BTC" || d.coinName === "BT"
            });
            sectionTwoData.push(selfSelected);

            tpl.render({"tplId": "#area_tpl", "renderId": "#area_render", "data": sectionOneData});
            tpl.render({"tplId": "#area_tpl_2", "renderId": "#area_render_2", "data": sectionTwoData});
            return {
                sectionOneData:sectionOneData,
                sectionTwoData:sectionTwoData
            };
        }).then(function (data) {
            renderAreaData(data.sectionOneData[0].coinCode);
            renderAreaData(data.sectionTwoData[0].coinCode, "#coin_area_render_2", "#coin_area_tpl_2");
            initEffect();
        })
    }

    /**
     * 推荐币列表
     */
    function initRecommends() {

        core.list({url:BASE_PATH+"/exchange/price/recommends"}).then(function (data) {
            tpl.render({"tplId": "#recommend_tpl", "renderId": "#recommend_render", "data": data});
        })
    }

    /**
     * 渲染交易区
     */
    function renderAreaData(code, renderId, tplId) {
        var coinAreaRender = renderId ? renderId : "#coin_area_render";
        var coinAreaTpl = tplId ? tplId : "#coin_area_tpl";
        function operateDom() {

            if(code == 'collect') {
                $(coinAreaRender).find('.favoriteIco').each(function(){
                    $(this).addClass('favorited')
                })
            }else{
                $(coinAreaRender).find('.favoriteIco').each(function(){
                    if($(this).attr('data-isCollect') == 'true') {
                        $(this).addClass('favorited')
                    }
                })
            }
            $(coinAreaRender + ' .prelastprice').each(function() {
                // var old_last_price = $(this).text()
                var old_last_price =$(this).text()
                var last_price = $(this).next().text()
                if (old_last_price < 0) {
                    $(this).next().addClass("red").removeClass("green");
                } else if (old_last_price == 0) {
                    $(this).next().addClass("#c7cce6").removeClass("green").removeClass("red");
                } else {
                    $(this).next().addClass("green").removeClass("red");
                }
                // if (U.bccomp(last_price, old_last_price) < 0) {
                //     $(this).next().addClass("red").removeClass("green");
                // } else if (U.bccomp(last_price, old_last_price) == 0) {
                //     $(this).next().removeClass("green").removeClass("red");
                // } else {
                //     $(this).next().addClass("green").removeClass("red");
                // }
            })

            $(coinAreaRender + ' .date_auto').each(function() {
                if ($(this).attr('data-value')) {
                    $(this).addClass("date_auto_on")
                }
            })

            $(coinAreaRender + ' .dayrate').each(function(i, e){
                var value = $(this).attr('data-value')
                if(U.bccomp(value, 0) > 0) {
                    $(this).addClass('green').removeClass('red')
                }else if(U.bccomp(value, 0) < 0){
                    $(this).removeClass('green').addClass('red')
                }else{
                    $(this).removeClass('green').removeClass('red')
                }
            })
        }

        if(code == 'collect'){
            //自选
            clearAllPageTimer('USDT');
            $.ajax({url:"/exchange/listCoinCollection",async:false}).then(function (data) {
                if(data.msg ==  '未登录') {
                    trade_area['collect'] = [];
                    window.location.href = '/login';
                    tpl.render({"tplId": coinAreaTpl, "renderId": coinAreaRender, "data":  []});
                    operateDom();
                }else{
                    if(data.data == ""){
                        trade_area['collect'] = [];
                        // $("#coin_area_render").css("display","none");
                        tpl.render({"tplId": coinAreaTpl, "renderId": coinAreaRender, "data":  []});
                    }else {
                        var data = JSON.parse(data.data);
                        for (let items in data) {
                            var timestamp = (new Date()).getTime();
                            // 有倒计时
                            if( data[items].actionTime){
                                // console.log(data[items]);
                                // console.log(data[items].tradeCoinCode);
                                data[items].endTime = parseInt(data[items].actionTime) - timestamp;
                                data[items].textTime = countFunc(data[items].endTime);
                                reTime();
                                function reTime() {
                                    pageTimer[data[items].pricingCoinCode][data[items].tradeCoinCode] = setTimeout(function() {
                                        // console.log(data[items].tradeCoinCode +':'+ data[items].endTime)
                                        if (data[items].endTime >= 0) {
                                            data[items].endTime = data[items].endTime - 1000;
                                            data[items].textTime = countFunc(data[items].endTime);
                                            tpl.render({"tplId": coinAreaTpl, "renderId": coinAreaRender, "data":  data});
                                            operateDom();
                                            reTime();
                                        } else {
                                            clearTimeout(pageTimer[data[items].pricingCoinCode][data[items].tradeCoinCode])
                                        }
                                    }, 1000)
                                }
                            } else {
                                data[items].endTime = 0;
                                // $('.date_auto').hide()
                                // data[items].textTime = 00 + "天" + 00 +"时"+ 00 +"分"+00 +"秒";
                                data[items].textTime = '';
                            }
                        }
                        tpl.render({"tplId": coinAreaTpl, "renderId": coinAreaRender, "data":  data});
                    }

                    operateDom()
                }
            });
        }else{
            clearAllPageTimer(code);
            //非自选
            $.ajax({url:"/exchange/coins_area",async:false}).then(function (data) {
                var _data=JSON.parse(data);
                for(var item in _data){
                    trade_area[item] = JSON.parse(_data[item]);
                }
                var data=trade_area[code];

                //不能使用var防止变量流失
                for (let items in data) {
                    var timestamp = (new Date()).getTime();
                    // 有倒计时
                    if( data[items].actionTime){
                        // console.log(data[items]);
                        // console.log(data[items].tradeCoinCode);
                        data[items].endTime = parseInt(data[items].actionTime) - timestamp;
                        data[items].textTime = countFunc(data[items].endTime);
                        reTime();
                        function reTime() {
                            pageTimer[data[items].pricingCoinCode][data[items].tradeCoinCode] = setTimeout(function() {
                                // console.log(data[items].tradeCoinCode +':'+ data[items].endTime)
                                if (data[items].endTime >= 0) {
                                    data[items].endTime = data[items].endTime - 1000;
                                    data[items].textTime = countFunc(data[items].endTime);
                                    tpl.render({"tplId": coinAreaTpl, "renderId": coinAreaRender, "data":  data});
                                    operateDom();
                                    reTime();
                                } else {
                                    clearTimeout(pageTimer[data[items].pricingCoinCode][data[items].tradeCoinCode])
                                }
                            }, 1000)
                        }
                    } else {
                        data[items].endTime = 0;
                        // $('.date_auto').hide()
                        // data[items].textTime = 00 + "天" + 00 +"时"+ 00 +"分"+00 +"秒";
                        data[items].textTime = '';
                    }

                    if (data[items].tradeCoinCode === 'BT' || data[items].pricingCoinCode === 'USDT') {
                        $(".coin_k").css("display","block");
                    }

                }

                tpl.render({"tplId": coinAreaTpl, "renderId": coinAreaRender, "data":  data});
                operateDom()

            });
        }
    }

    function countFunc(leftTime) {
        if(leftTime >= 0) {
            var days = parseInt(leftTime / 1000 / 60 / 60 / 24, 10); //计算剩余的天数
            var hours = parseInt(leftTime / 1000 / 60 / 60 % 24, 10); //计算剩余的小时
            var minutes = parseInt(leftTime / 1000 / 60 % 60, 10); //计算剩余的分钟
            var seconds = parseInt(leftTime / 1000 % 60, 10); //计算剩余的秒数
            days = checkTime(days);
            hours = checkTime(hours);
            minutes = checkTime(minutes);
            seconds = checkTime(seconds);
            var str = days +"d" +" \xa0" +hours+"\xa0" +":"+" \xa0"  + minutes +" \xa0" +":"+" \xa0"+seconds ;
            return str;
        } else {
            var str = 00+"d" + " \xa0" + 00 +" \xa0" +":"+" \xa0"+ 00 +" \xa0" +":"+" \xa0" +00 ;
            return str;
        }
    }
    function checkTime(i) { //将0-9的数字前面加上0，例1变为01
        if(i < 10) {
            i = "0" + i;
        }
        return i;
    }


    function renderAreaDatas(code, renderId, tplId) {
        var coinAreaRender = renderId ? renderId : "#coin_area_render_2";
        var coinAreaTpl = tplId ? tplId : "#coin_area_tpl_2";
        function operateDoms() {
            if(code == 'collect') {
                $(coinAreaRender).find('.favoriteIco').each(function(){
                    $(this).addClass('favorited')
                })
            }else{
                $(coinAreaRender).find('.favoriteIco').each(function(){
                    if($(this).attr('data-isCollect') == 'true') {
                        $(this).addClass('favorited')
                    }
                })
            }
            $(coinAreaRender + ' .prelastprice').each(function() {
                var old_last_price = $(this).text()
                var last_price = $(this).next().text()
                if (U.bccomp(last_price, old_last_price) < 0) {
                    $(this).next().addClass("red").removeClass("green");
                } else if (U.bccomp(last_price, old_last_price) == 0) {
                    $(this).next().removeClass("green").removeClass("red");
                } else {
                    $(this).next().addClass("green").removeClass("red");
                }
            })

            $(coinAreaRender + ' .date_auto').each(function() {
                if ($(this).attr('data-value')) {
                    $(this).addClass("date_auto_on")
                }
            })

            $(coinAreaRender + ' .dayrate').each(function(i, e){
                var value = $(this).attr('data-value');
                if(U.bccomp(value, 0) > 0) {
                    $(this).addClass('green').removeClass('red')
                }else if(U.bccomp(value, 0) < 0){
                    $(this).removeClass('green').addClass('red')
                }else{
                    $(this).removeClass('green').removeClass('red')
                }
            })
        }

        if(code == 'collect'){
            $.ajax({url:"/exchange/listCoinBtCollection ",async:false}).then(function (data) {
                if(data.msg ==  '未登录') {
                    trade_area['collect'] = [];
                    window.location.href = '/login';
                }else{
                    if(data.data ==""){
                        trade_area['collect'] = [];
                        tpl.render({"tplId": coinAreaTpl, "renderId": coinAreaRender, "data":  []});
                        clearAllPageTimer('BT')
                    }else {
                        clearAllPageTimer('BT')
                        var data = JSON.parse(data.data);
                        for (let items in data) {
                            var timestamp = (new Date()).getTime();
                            // 有倒计时
                            if( data[items].actionTime){
                                data[items].endTime = parseInt(data[items].actionTime) - timestamp;
                                data[items].textTime = countFunc(data[items].endTime);
                                reTime();
                                function reTime() {
                                    pageTimer[data[items].pricingCoinCode][data[items].tradeCoinCode] = setTimeout(function() {
                                        if (data[items].endTime >= 0) {
                                            data[items].endTime = data[items].endTime - 1000;
                                            data[items].textTime = countFunc(data[items].endTime);
                                            tpl.render({"tplId": coinAreaTpl, "renderId": coinAreaRender, "data":  data});
                                            operateDoms();
                                            reTime();
                                        } else {
                                            clearTimeout(pageTimer[data[items].pricingCoinCode][data[items].tradeCoinCode])
                                        }
                                    }, 1000)
                                }
                            } else {
                                data[items].endTime = 0;
                                // data[items].textTime = 00 + "天" + 00 +"时"+ 00 +"分"+00 +"秒";
                                data[items].textTime = '';
                            }
                        }
                        tpl.render({"tplId": coinAreaTpl, "renderId": coinAreaRender, "data":  data});
                    }

                    operateDoms()
                }
            });

        }else{
            clearAllPageTimer(code);
            $.ajax({url:"/exchange/coins_area",async:false}).then(function (data) {
                var _data=JSON.parse(data);
                for(var item in _data){
                    trade_area[item] = JSON.parse(_data[item]);
                }

                var data = trade_area[code];

                //不能使用var防止变量流失
                for (let items in data) {
                    var timestamp = (new Date()).getTime();
                    // 有倒计时
                    if( data[items].actionTime){
                        data[items].endTime = parseInt(data[items].actionTime) - timestamp;
                        data[items].textTime = countFunc(data[items].endTime);
                        reTime();
                        function reTime() {
                            pageTimer[data[items].pricingCoinCode][data[items].tradeCoinCode] = setTimeout(function() {
                                if (data[items].endTime >= 0) {
                                    data[items].endTime = data[items].endTime - 1000;
                                    data[items].textTime = countFunc(data[items].endTime);
                                    tpl.render({"tplId": coinAreaTpl, "renderId": coinAreaRender, "data":  data});
                                    operateDoms();
                                    reTime();
                                } else {
                                    clearTimeout(pageTimer[data[items].pricingCoinCode][data[items].tradeCoinCode])
                                }
                            }, 1000)
                        }
                    } else {
                        data[items].endTime = 0;
                        // $('.date_auto').hide()
                        // data[items].textTime = 00 + "天" + 00 +"时"+ 00 +"分"+00 +"秒";
                        data[items].textTime = '';
                    }
                }

                tpl.render({"tplId": coinAreaTpl, "renderId": coinAreaRender, "data":  data});
                operateDoms()

            });
        }

    }
    //加载banner
    function banners(){

        core.list({url:BASE_PATH+"/banner/list"}).then(function (data) {


            tpl.render({"tplId": "#banner_tpl", "renderId": "#banner_render", "data": data.data});

            currentindex = 1;
            countFocus = $('.focusList li').length;

            startAm();



            for (var num = 1; num <= countFocus; num++) {
                if (num == 1) {
                    $(".pagination").append("<a class='curr'><span>" + num + "</span></a>");
                } else {
                    $(".pagination").append("<a><span>" + num + "</span></a>");
                }
            }

            $(".pagination a").on('click', function () {
                var numOnclick = $(this).index() + 1;
                changeflash(numOnclick)
            })

            $(".focusList li").mouseover(function () {
                stopAm();
            }).mouseout(function () {
                startAm();
            });

        })
    }
    //加载公告
    function notices(){

        core.list({url:BASE_PATH+"/article/data/announcements/3"}).then(function (data) {
            tpl.render({"tplId": "#notice_tpl", "renderId": "#notice_render", "data": data.data});
        })
    }


    function init(){
        // $.ajax({url:"/exchange/coins_area",async:false}).then(function (data) {
        //     var _data=JSON.parse(data);
        //     for(var item in _data){
        //         trade_area[item] = JSON.parse(_data[item]);
        //     }
        // });
        $.ajax({url:"/exchange/listCoinCollection",async:false}).then(function (data) {
            if(data.msg ==  '未登录') {
                trade_area['collect'] = []
            }else{
                trade_area['collect'] = JSON.parse(data.data)
            }
        });


        $(document).ready(function () {
            $(".closeBt").click(function () {
                $(".noticeBar").hide();
            });
            $(".searchBt").click(function () {
                search();
                searchs();
            });
            $(document).on("click", ".favoriteIco", function () {
                var tradeCoinCode = $(this).attr("data-tradeCoinCode")
                var pricingCoinCode = $(this).attr("data-pricingCoinCode")
                var id = $(this).attr("data-id")
                var collectionId = $(this).attr("data-collectionid")
                if (!$(this).hasClass("favorited")) {
                    var state = 1; //点击收藏
                    saveCoinCollection(tradeCoinCode, pricingCoinCode, $(this))
                } else {
                    var state = 0; //取消收藏
                    var dataid = ''
                    if($('.cur').attr('data-code') == 'collect'){
                        dataid = id
                    }else{
                        dataid = collectionId
                    }
                    deleteCoinCollection(dataid, $(this).parent().parent(), $(this))
                }
                var p = $(this).attr("data");
                //fav(p, state)
                /*if ($(this).attr("fav") > 0) {
                    if (state == 0) {
                        $(this).parent().parent().remove();
                        $(".favorited").each(function () {
                            if ($(this).attr("data") == p) {
                                $(this).removeClass("favorited");
                            }
                        });
                    }
                } else {
                    if (state == 0) {
                        $(".favorited").each(function () {
                            if ($(this).attr("fav") == 1 && $(this).attr("data") == p) {
                                $(this).parent().parent().remove();
                            }
                            if ($(this).attr("data") == p) {
                                $(this).removeClass("favorited");
                            }
                        });
                    } else {
                        var dom = $(this).parent().parent().clone();
                        dom.find(".favoriteIco").attr("fav", 1);
                        $("#fav_th").after(dom);
                    }
                }*/
            });
            Connect();
            charts();
            var asset_exchange_rate = $("#asset_exchange_rate").val();
            AssetExchangeRate = $.parseJSON(asset_exchange_rate);
            var pairs_decimal = $("#pairs_decimal").val();
            if (pairs_decimal.length > 0) {
                PairsDecimal = $.parseJSON(pairs_decimal);
            }
            $(".t_cont span.price[rate]").each(function () {
                var tr = $(this).parents(".t_cont tr");
                var last_price = U.unFormatcoin(tr.find(".last_price").text());
                var rate = tr.find(".price[rate]").attr('rate');
                tr.find(".price[rate]").text(C.guzhi(U.bcmul(last_price, rate)));
            });
            $(".hotMarkets span.price[rate]").each(function () {
                var tr = $(this).parents(".item");
                var last_price = U.unFormatcoin(tr.find(".last_price").text());
                var rate = tr.find(".price[rate]").attr('rate');
                tr.find(".price[rate]").text(C.guzhi(U.bcmul(last_price, rate)));
            });
        })

        $(function(){
            $(document).on("click",".wrapMain .marketsTable tr:not(.theader)",function(event){
                if($(event.target).is(".favoriteIco")){
                    return false;
                }
                window.location=$(this).find("td:eq(1) a").attr("href");
            });
            vol();
        });
        //focus
        $("input[name='searchs']").bind('input propertychange', function() {
            searchs();
        });
        $("input[name='searchs']").bind('input oninput', function() {
            searchs();
        });

        $("input[name='search']").bind('input propertychange', function() {
            search();
        });
        $("input[name='search']").bind('input oninput', function() {
            search();
        });

        setInterval(vol(),3000);



        //加载banner
        banners();
        notices();
        initAreaCoins();
        initRecommends();
    }
    exports.init    = init;
});