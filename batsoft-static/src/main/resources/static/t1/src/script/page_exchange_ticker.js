/**
 *<p>page_exchange_eventLog.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {
    var ticker;
    var timeout = 5000;
    var trade_area={};
    // var list_most ="";

    var
        tpl = require('./module_tpl'),
        C = require('./page_exchange_common');
    U = require('./module_utils'),
        order = require('./page_exchange_order');
    core = require('./module_core');


    //點擊收藏
    function saveCoinCollection(tradeCoinCode, pricingCoinCode, $e) {
        $.post("/exchange/svCoinCollection", {"tradeCoinCode": tradeCoinCode, "pricingCoinCode": pricingCoinCode}, function (data) {
            if(data.msg == '未登录') {
                window.location.href = '/login'
            }else{
                U.response(data);
                $e.toggleClass("icoFavorites");
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
                    $e.toggleClass("icoFavorites");
                }

                //response(data);
            }

        });
    }

    function initEffect() {
        //---标签切换
        $('#products .tabs').each(function () {
            var it = $(this);
            var menus = $('.t_menu li', it);
            var conts = $('.t_cont', it);
            menus.on('click', function () {
                $(".Jcion").css("display","none");
                var _this = $(this);
                var code=_this.attr("data-code");
                if (_this.hasClass('cur')) return;
                var _index = _this.index();

                _this.addClass('cur').siblings().removeClass('cur');
                // conts.hide(0).eq(_index).stop().fadeIn();
                renderAreaData(code);
            });
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
        var money_list =i[14];

        var pairs_data = pairs.split("_");
        var base_asset = pairs_data[0];
        var quote_asset = pairs_data[1];

        trade_area[quote_asset].forEach(function (item) {
            if(item.coinCode===base_asset){
                item.last=last_price;
                item.high=high_price;
                item.low=low_price;
                item.rate=change_rate;
                item.volume=volume;
            }

        })


    }

    /**
     * 加载 交易区币种
     */
    function initAreaCoins() {
        var list_hat = $(".sther").text();
        core.list({url:BASE_PATH+"/exchange/price/coins"}).then(function (data) {
            // data.push({
            //     coinCode: "collect",
            //     coinName: "自选区",
            //     del: 0,
            //     page: 1,
            //     rows: 10
            // });
            var selfSelectde = {
                coinCode: "collect",
                coinName: list_hat,
                del: 0,
                page: 1,
                rows: 10
            };
            var sectionTwoData = data.filter(function(d){
                return d.coinName === "USDT" || d.coinName === "BT"
            });
            sectionTwoData.push(selfSelectde);
            tpl.render({"tplId": "#area_tpl", "renderId": "#area_render", "data": sectionTwoData});
            return sectionTwoData;
        }).then(function (data) {
            renderAreaData(data[0].coinCode);
            initEffect();
        })
    }

    /**
     * 渲染交易区
     */
    function renderAreaData(code) {
        function operateDom() {
            if(code == 'collect') {
                $('#coin_area_render').find('.icoStar').each(function(){
                    $(this).addClass('icoFavorites')
                })
            }else{
                $('#coin_area_render').find('.icoStar').each(function(){
                    if($(this).attr('data-isCollect') == 'true') {
                        $(this).addClass('icoFavorites')
                    }
                })
            }
            // $('.prelastprice').each(function() {
            //     var old_last_price = $(this).text()
            //     var last_price = $(this).next().text()
            //     if (U.bccomp(last_price, old_last_price) < 0) {
            //         $(this).next().addClass("red").removeClass("green");
            //     } else if (U.bccomp(last_price, old_last_price) == 0) {
            //         $(this).next().removeClass("green").removeClass("red");
            //     } else {
            //         $(this).next().addClass("green").removeClass("red");
            //     }
            // })
            // $('.dayrate').each(function(){
            //     var value = $(this).attr('data-value');
            //
            //     if(value > 0) {
            //         $(this).addClass('green').removeClass('red')
            //     }else if(value < 0){
            //         $(this).removeClass('green').addClass('red')
            //     }else{
            //         $(this).removeClass('green').removeClass('red').addClass("#c7cce6")
            //     }
            // })
            $( '.prelastprice').each(function() {
                // var old_last_price = $(this).text()
                var old_last_price =$(this).text();
                // console.log( $(this).(".last_price").text())
                if (old_last_price < 0) {
                    $(this).next().addClass("red").removeClass("green");
                   $('.dayrate').addClass("red").removeClass("green");
                } else if (old_last_price == 0) {
                    $(this).next().addClass("#c7cce6").removeClass("green").removeClass("red");
                    $('.dayrate').addClass("#c7cce6").removeClass("green").removeClass("red");
                } else {
                    $(this).next().addClass("green").removeClass("red");
                    $('.dayrate').addClass("green").removeClass("red");
                }
                // if (U.bccomp(last_price, old_last_price) < 0) {
                //     $(this).next().addClass("red").removeClass("green");
                // } else if (U.bccomp(last_price, old_last_price) == 0) {
                //     $(this).next().removeClass("green").removeClass("red");
                // } else {
                //     $(this).next().addClass("green").removeClass("red");
                // }
            })

            // $('.dayrate').each(function(i, e){
            //     var value = $(this).attr('data-value');
            //     console.log(value,"DADA")
            //     if(U.bccomp(value, 0) > 0) {
            //         $(this).addClass('green').removeClass('red')
            //     }else if(U.bccomp(value, 0) < 0){
            //         $(this).removeClass('green').addClass('red')
            //     }else{
            //         $(this).removeClass('green').removeClass('red')
            //     }
            // })
        }
        if(code == 'collect'){
            $.ajax({url:"/exchange/listExchangeCoinCollection",async:false}).then(function (data) {
                if(data.msg ==  '未登录') {
                    trade_area['collect'] = [];
                    window.location.href = '/login';
                }else{
                    if(data.data == ""){
                        tpl.render({"tplId": "#coin_area_tpl", "renderId": "#coin_area_render", "data": ""});
                        $(".click_kline").css("display","none")
                    }else {
                        trade_area['collect'] = JSON.parse(data.data);
                        var data=trade_area[code];
                        tpl.render({"tplId": "#coin_area_tpl", "renderId": "#coin_area_render", "data":  data});
                    }
                    operateDom()
                }
            });

        }else{
            $.ajax({url:"/exchange/coins_area",async:false}).then(function (data) {
                var _data=JSON.parse(data);

                for(var item in _data){
                    trade_area[item] = JSON.parse(_data[item]);
                }
                var data=trade_area[code];
                for(var items in data){
                    data[items].last =  data[items].last.toFixed(2);
                    data[items].duihuan = data[items].duihuan.toFixed(2);
                };
                if (data[items].tradeCoinCode === 'BT' || data[items].pricingCoinCode === 'USDT') {
                    $(".Jcion").css("display","block");
                }
                tpl.render({"tplId": "#coin_area_tpl", "renderId": "#coin_area_render", "data":  data});

                $.ajax({
                    type: "get",
                    url: " /exchangeAction/lastAction",
                    dataType: 'json',
                    async: true,
                    data: {"coinCode": $("#base_asset").val(), "priceCode": $("#quote_asset").val()},
                    success: function (data) {
                        if (data.success) {
                            $(".coin_kid").text($("#base_asset").val()+ "/" +$("#quote_asset").val());
                            if(data.data == null){
                                //活动页面弹出框隐藏
                                $(".displaylist").css("display","none");
                            }else {
                                $(".displaylist").css("display","block");
                                // //买入卖出按钮变色及禁用
                                // $(".buyBt").css("background","#c2c2c2");
                                // $("#buy_core").css("color","#fff");
                                // $("#buy_core").css("cursor","no-drop|not-allowed");
                                // $("#buy_core").attr("disabled",true);
                                // $("#buy_core").css("pointer-events","none");
                                // $("#sell_core").css("background","#c2c2c2");
                                // $("#sell_core").css("color","#fff");
                                // $("#sell_core").css("cursor","not-allowed");
                                // $("#sell_core").attr("disabled",true);
                                // $("#sell_core").css("pointer-events","none");
                                $(".displaylist_b_3").html(data.data.actionName);
                                $("#href_link").attr('href',data.data.link);
                                $(".displaylist").css('background','url('+data.data.coinPicture+') no-repeat');
                                $(".displaylist").css("background-size","100% 100%");
                                $("#href_link").text(data.data.linkName);
                                $(".maste").text(data.data.coinCode);
                                var str =data.data.introduce;
                                var reg = /[,, ]/g;
                                str=str.replace(reg,"$&<br>");;
                                $(".displaylist_b_5").html(str);
                                //活动倒计时
                                var SysSecond;
                                var InterValObj;
                                $(document).ready(function() {
                                    SysSecond = parseInt(data.data.startSeconds);
                                    InterValObj = window.setInterval(SetRemainTime, 1000); //间隔函数，1秒执行
                                });
                                //将时间减去1秒，计算天、时、分、秒
                                function SetRemainTime() {
                                    if (SysSecond > 0) {
                                        SysSecond = SysSecond - 1;
                                        var second = Math.floor(SysSecond % 60);             // 计算秒
                                        var minite = Math.floor((SysSecond / 60) % 60);      //计算分
                                        var hour = Math.floor((SysSecond / 3600) % 24);      //计算小时
                                        var day = Math.floor((SysSecond / 3600) / 24);        //计算天
                                        if(day <10){
                                            day ="0"+day;
                                    }
                                        if(hour <10){
                                            hour ="0"+hour;
                                        }
                                        if(minite <10){
                                            minite= "0"+minite;
                                        }
                                        if(second <10){
                                            second= "0"+second;
                                        }
                                        $("#time4_right").text(day);
                                        $("#time3_right").text(hour);
                                        $("#time1_right").text(minite);
                                        $("#time_right").text(second);
                                    } else {
                                        //剩余时间小于或等于0的时候，就停止间隔函数
                                        window.clearInterval(InterValObj);
                                        //这里可以添加倒计时时间为0后需要执行的事件
                                    }
                                }

                            }

                        }

                    },
                });

                // var clicktr = $(".click_kline");
                // for(var i=0;i<clicktr.length;i++){
                //     clicktr[i].onclick = function(){
                //         var finddata = $(this).find(".white1").text();
                //         var findlist =$(this).find(".hx").text();
                //         $.ajax({
                //             type: "get",
                //             url: " /exchangeAction/lastAction",
                //             dataType: 'json',
                //             async: true,
                //             data: {"coinCode": finddata, "priceCode": findlist},
                //             success: function (data) {
                //                 if (data.success) {
                //                     if(data.data == null){
                //                         //活动页面弹出框隐藏
                //                         $(".displaylist").css("display","none");
                //                     }else {
                //                         $(".displaylist").css("display","block");
                //                         //买入卖出按钮变色及禁用
                //                         // $(".buyBt").css("background","#c2c2c2");
                //                         // $("#buy_core").css("color","#fff");
                //                         // $("#buy_core").css("cursor","no-drop|not-allowed");
                //                         // $("#buy_core").attr("disabled",true);
                //                         // $("#buy_core").css("pointer-events","none");
                //                         // $("#sell_core").css("background","#c2c2c2");
                //                         // $("#sell_core").css("color","#fff");
                //                         // $("#sell_core").css("cursor","not-allowed");
                //                         // $("#sell_core").attr("disabled",true);
                //                         // $("#sell_core").css("pointer-events","none");
                //                         //活动倒计时
                //                         $(".displaylist_b_2").html(data.data.actionName);
                //                         $(".displaylist_b_3").html(data.data.publicDate.slice(0, 10));
                //                         $("#href_link.href").html(data.data.link);
                //                         function formatSeconds(value) {
                //                             var theTime = parseInt(value);// 秒
                //                             var theTime1 = 0;// 分
                //                             var theTime2 = 0;// 小时
                //                             var theTime3 = 0;//天
                //                             if (theTime > 60) {
                //                                 theTime1 = parseInt(theTime / 60);
                //                                 theTime = parseInt(theTime % 60);
                //                                 if (theTime1 > 60) {
                //                                     theTime2 = parseInt(theTime1 / 60);
                //                                     theTime1 = parseInt(theTime1 % 60);
                //                                 }
                //                                 if (theTime2 > 60) {
                //                                     theTime3 = parseInt(theTime2 / 24);
                //                                     theTime2 = parseInt(theTime2 % 24);
                //                                 }
                //                             }
                //                             if(parseInt(theTime) <10){
                //                                 theTime = "0" +  parseInt(theTime);
                //                             }
                //                             var result = "" + parseInt(theTime) + "秒";
                //                             if (theTime1 > 0) {
                //                                 result = "" + parseInt(theTime1) + "分" + result;
                //                             }
                //                             if(parseInt(theTime1) <10){
                //                                 theTime1 = "0" +  parseInt(theTime1);
                //                             }
                //                             if (theTime2 > 0) {
                //                                 result = "" + parseInt(theTime2) + "时" + result;
                //
                //                             }
                //                             if(parseInt(theTime2) <10){
                //                                 theTime2 = "0" +  parseInt(theTime2);
                //                             }
                //
                //                             if (theTime3 > 0) {
                //                                 result = "" + parseInt(theTime3) + "天" + result;
                //                             }
                //                             if(parseInt(theTime3) <10){
                //                                 theTime3 = "0" +  parseInt(theTime3);
                //                             }
                //                             $("#time_right").text(theTime);
                //                             $("#time1_right").text(theTime1);
                //                             $("#time3_right").text(theTime2);
                //                             $("#time4_right").text(theTime3);
                //                             // return  $(".displaylist_b_5").html(result);
                //                         }
                //                         var s = data.data.startSeconds;// 例如：後台得到的秒數
                //
                //                         time();
                //
                //                         function time() {
                //                             console.log(formatSeconds(s));
                //                             if (s == 0) {
                //                                 // $(".displaylist").css("display","none");
                //                                 // alert("時間到了！");
                //                             } else {
                //                                 s--;
                //                                 //定時器，每秒重新計算
                //                                 setTimeout(function () {
                //                                         time();
                //                                     },
                //                                     1000)
                //                             }
                //                         }
                //                     }
                //                     // console.log(data.data.startSeconds, "1111");
                //
                //                 }
                //
                //             },
                //         });
                //
                //     };
                // }
                operateDom()

            });
        }

        /*var data=trade_area[code];
        tpl.render({"tplId": "#coin_area_tpl", "renderId": "#coin_area_render", "data":  data});*/

    }

    function tickerConnect() {
        if (ticker) {
            console.log("restart");
            Close(ticker);
        }
        /* try {
             ticker = new WebSocket($("#ws_url").val() + 'all@ticker');
         } catch (e) {
             console.log("ticket connect error");
             setTimeout(function () {
                 tickerConnect();
             }, timeout);
             return;
         }*/


        try {
            var socket = new SockJS($("#wss_url").val());
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                tickerOpen();
                // tickerMessage();
                tickerSend();
                //console.log('Connected:ticker ' + frame);
                stompClient.subscribe("/exchange/" + 'all@ticker', function(respnose){ //订阅/topic/getResponse 目标发送的消息。这个是在控制器的@SendTo中定义的。
                    tickerMessage(JSON.parse(respnose.body));
                });

                stompClient.debug = function(str) {
                    // str 参数即为 debug 信息
                    // append the debug log to a #debug div somewhere in the page using JQuery:
                    // console.log('info==== ' + str);
                };

            });

        } catch (e) {
            console.log("book connect error");
            setTimeout(bookConnect, timeout);
            return;
        }

    }

    function tickerOpen() {
        console.log('ticker connect open!');
        if (Loading['ticker'] == undefined) {
            Loading['ticker'] = 1;
        }
    }

    function tickerError(e) {
        console.log("error " + e);
    }

    function tickerMessage(msg) {


        if (msg == null) {
            console.log('msg is null');
            return false;
        }
        var i = JSON.parse(msg.data);
        if (i[0] == undefined) {
            return;
        }
        init_ticker_data(i);
    }

    function tickerClose(e) {
        console.log("connect closed:" + e.code);
        setTimeout(function () {
            tickerConnect();
        }, timeout);
    }

    function tickerSend() {
        stompClient.send("/init_ticker", {}, "");
    }

    function Close(ws) {
        ws.close();
    }



    function load_trade_symbol() {
        $.ajax({
            type: "GET", url: "/trade/all_trade_symbol", async: true, dataType: "html", success: function (html) {
                $("#productsTable").html(html);
                var cur_pairs = $.trim($(".header .coin_select").text());
                var market = $("#productsTable .product_cont[id!='m_favorite']:contains('" + cur_pairs + "')").attr("id");
                $("#products .t_menu li[market='" + market + "']").addClass("cur");
                $("#productsTable").find("#" + market).show();
                $("#productsTable .product_cont .last_price").each(function () {
                    var tr = $(this).parents(".product_cont tr");
                    var last_price = U.unFormatcoin($(this).text());
                    var rate = tr.find(".price").attr("rate");
                    tr.find(".price").text(C.guzhi(U.bcmul(last_price, rate)));
                    $("#m_favorite tr").each(function () {
                        var pairs = $(this).attr("pairs");
                        $(".product_cont tr[pairs='" + pairs + "']").find(".tc span").addClass("icoFavorites").removeClass("icoStar");
                    });
                });
            }
        });
    }



    function product_search() {
        var txt = $.trim($("#product_input").val().toUpperCase());
        $(".product_cont").find("tr").hide().each(function () {
            var keyword = $(this).find(".sou_coin").text().toUpperCase();
            //  var keyword = $(this).text().toUpperCase();
            if (keyword.indexOf(txt) >= 0) {
                $(this).show();
            }
        });
        $("#searchBt_BTC").on("click", function () {
            var txt = $.trim($("#product_input").val().toUpperCase());
            $(".product_cont").find("tr").hide().each(function () {
                var keyword = $(this).find(".sou_coin").text().toUpperCase();
                //  var keyword = $(this).text().toUpperCase();
                if (keyword.indexOf(txt) >= 0) {
                    $(this).show();
                }
            });
        })
    }
    function init_ticker_data(i) {
        var pairs = i[0];
        var last_price = i[7];
        var change_rate = i[6];
        var high_price = i[9];
        var low_price = i[10];
        var volume = i[8];
        var old_tr_last_price = i[13];
        var guzhi = i[14];
        var lastMoney = i[15];
        var lastNum = i[16];
        // $(".sd").text("/"+guzhi);

        //cyj更新账户余额
        if(lastMoney && lastNum){
            $("#quote_account").text(lastMoney);
            $("#base_account").text(lastNum);
        }
        updateTradeAreaData(i);
        updateAssetExchangeRate(pairs, last_price);
        var tr = $(".product_cont tr[pairs='p_" + pairs + "']");//更新列表
        if (tr.length > 0) {
            var rate = tr.find(".price").attr("rate");
            tr.find(".price").text(C.guzhi(U.bcmul(last_price, rate)));
            var old_tr_last_price = U.unFormatcoin(old_tr_last_price);
            if (U.bccomp(last_price, old_tr_last_price) < 0) {
                tr.find(".last_price").text(U.formatcoin(last_price, PairsDecimal[pairs]['price_decimal'])).addClass("red").removeClass("green");
            } else if (U.bccomp(last_price, old_tr_last_price) == 0) {
                tr.find(".last_price").text(U.formatcoin(last_price, PairsDecimal[pairs]['price_decimal'])).removeClass("red").removeClass("green");
            } else {
                tr.find(".last_price").text(U.formatcoin(last_price, PairsDecimal[pairs]['price_decimal'])).addClass("green").removeClass("red");
            }
             tr.find(".sd").text("/"+"￥"+guzhi);
            if (change_rate < 0) {
                tr.find(".change_rate").html(change_rate + '%').addClass("red").removeClass("green");
                tr.find(".last_price ").html(last_price).addClass("red").removeClass("green");

            } else if(change_rate == 0){
                tr.find(".change_rate").html(change_rate + '%').addClass("white").removeClass("green").removeClass("red");
                tr.find(".last_price ").html(last_price).addClass("white").removeClass("green").removeClass("red");
            }else {
                tr.find(".change_rate").html(change_rate + '%').addClass("green").removeClass("red");
                tr.find(".last_price ").html(last_price).addClass("green").removeClass("red");
            }
            tr.find(".volume").text(U.formatcoin(Number(volume), 2));
        }
        var hc = $("#hc_" + pairs); //更新头部
        if (hc.length > 0) {
            hc.find(".price").text(C.guzhi(U.bcmul(last_price, rate)));
            //var old_hc_last_price = U.unFormatcoin(hc.find(".last_price").first().text());
            var old_hc_last_price = i[13];
            if (U.bccomp(last_price, old_hc_last_price) < 0) {
                hc.find(".last_price").text(U.formatcoin(last_price, PairsDecimal[pairs]['price_decimal'])).addClass("red").removeClass("green");
            } else if (U.bccomp(last_price, old_hc_last_price) == 0) {
                hc.find(".last_price").text(U.formatcoin(last_price, PairsDecimal[pairs]['price_decimal'])).removeClass("red").removeClass("green");
            } else {
                hc.find(".last_price").text(U.formatcoin(last_price, PairsDecimal[pairs]['price_decimal'])).addClass("green").removeClass("red");
            }
            if (change_rate < 0) {
                hc.find(".change_rate").html(change_rate + '%').addClass("red").removeClass("green");
            } else {
                hc.find(".change_rate").html(change_rate + '%').addClass("green").removeClass("red");
            }
            hc.find(".volume").text(U.formatcoin(Number(volume), 2));
            hc.find(".high_price").text(U.formatcoin(high_price, PairsDecimal[pairs]['price_decimal']));
            hc.find(".low_price").text(U.formatcoin(low_price, PairsDecimal[pairs]['price_decimal']));

            if (change_rate < 0) {
                $(".book_symbol").find(".change_rate").html(change_rate + '%').addClass("red").removeClass("green");
            } else {
                $(".book_symbol").find(".change_rate").html(change_rate + '%').addClass("green").removeClass("red");
            }
            $('.book_symbol .guzhi').text(guzhi)
            $('.book_symbol .last_price').text(last_price)
        }
        order.asset_guzhi();
    }

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

    function init(){

        // 初始化 交易区数据
        $.ajax({url:"/exchange/coins_area",async:false}).then(function (data) {
            var _data=JSON.parse(data)
            for(var item in _data){
                trade_area[item] = JSON.parse(_data[item]);
            }
        });

        $.ajax({url:"/exchange/listExchangeCoinCollection",async:false}).then(function (data) {
            if(data.msg ==  '未登录') {
                trade_area['collect'] = [];
                // window.location.href = '/login';
            }else{
                trade_area['collect'] = JSON.parse(data.data)
            }
        });


        $(function () {

            var pairs_decimal = $("#pairs_decimal").val();
            if (pairs_decimal.length > 0) {
                PairsDecimal = $.parseJSON(pairs_decimal);
            }
            // load_trade_symbol();

            $("#product_input").on('compositionstart', function () {
                $(this).prop('comStart', true);
            }).on('compositionend', function () {
                $(this).prop('comStart', false);
            }).on("input propertychange", function () {
                if ($(this).prop('comStart') == true) {
                    return false;
                }
                product_search();
            }).keyup(function (event) {
                if (!event) {
                    event = window.event;
                }
                if (event.keyCode == 13 || event.keyCode == 32 || event.keyCode == 16) {
                    product_search();
                }
            });
            $(document).on("click", ".product_cont .tc span", function () {
                var tradeCoinCode = $(this).attr("data-tradeCoinCode")
                var pricingCoinCode = $(this).attr("data-pricingCoinCode")
                var id = $(this).attr("data-id")
                var collectionId = $(this).attr("data-collectionid")
                if (!$(this).hasClass("icoFavorites")) {
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
                    //  deleteCoinCollection(dataid, $(".product_cont .tc span"), $(this))
                }



                /*var obj = this;
                var pairs_id = $(this).attr("pid");
                var state = 0;
                if ($(obj).hasClass("icoStar")) {
                    state = 1;
                }


                $.ajax({
                    type: "GET",
                    url: "/pairs/fav",
                    async: true,
                    data: {pairs_id: pairs_id, state: state},
                    dataType: "json",
                    success: function (result) {
                        if (result['code'] != 0) {
                            if (result['url'] != undefined) {
                                window.location = result['url'];
                            }
                        } else {
                            var pairs = $(obj).parents(".product_cont tr").attr("pairs");
                            if (state == 0) {
                                $("#m_favorite tr[pairs='" + pairs + "']").remove();
                                $(".product_cont tr[pairs='" + pairs + "']").find(".tc span").removeClass("icoFavorites").addClass("icoStar");
                            } else {
                                var tr = $(obj).parents(".product_cont tr").clone();
                                $("#m_favorite tbody").prepend(tr);
                                $(".product_cont tr[pairs='" + pairs + "']").find(".tc span").removeClass("icoStar").addClass("icoFavorites");
                            }
                        }
                    }
                });*/



            });

            tickerConnect();

            //初始化交易区
            initAreaCoins();

        });
    }

    exports.init=init;

});