/**
 *<p>page_exchange_eventLog.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {
    var last_price = 0;
    var stompClient = null;
    var trade, book;
    var timeout = 5000;
    var reload_book = 0;
    var reload_trade = 0;
    var max_volume = 0;
    var max_volume_intervel;
    // var trade_area ="";
    var  mVad      = require('./module_validator'), //表单校验
        swal      = require('./module_swal'), //弹窗 alert
        E = require('./module_effect'), //特效
        C = require('./page_exchange_common');
    U = require('./module_utils'),
        order=require('./page_exchange_order');
    core      =require('./module_core');
    var mMd5 = require('./module_md5');

    function tradeConnect() {
        if (trade) {
            console.log("restart");
            Close(trade);
        }
        try {
            trade = new WebSocket($("#ws_url").val() + $("#base_asset").val() + "_" + $("#quote_asset").val() + '@trade');
        } catch (e) {
            console.log("trade connect error");
            setTimeout(tradeConnect, timeout);
            return;
        }
        trade.onopen = tradeOpen;
        trade.onerror = tradeError;
        trade.onmessage = tradeMessage;
        trade.onclose = tradeClose;
    }

    function tradeOpen() {
        console.log('trade connect open!');
        reload_trade = 1;
    }

    function tradeError(e) {
        console.log("tradeError " + e);
    }

    function tradeMessage(msg) {

        if (Loading['trade'] == undefined) {
            Loading['trade'] = 1;
        }
        if (msg == null) {
            console.log('msg is null');
            return false;
        }

        var data = JSON.parse(msg.data);
        if (data.length < 1) {
            return;
        } else {
            if (reload_trade == 1) {
                $(".origin_trade").remove();
                reload_trade = 0;
            }else{
                if(msg.init=="trade"){
                    return ;
                }
            }
        }
        var pairs = $("#base_asset").val() + "_" + $("#quote_asset").val();
        var old_book_last_price = ''
        for (var i = 0; i < data.length; i++) {
            var id = "#trade_green";
            var dom = $('<tr><td class="tl"><span class="txt green price">0</span></td><td class="tr"><span class="txt white volume">0</span></td><td class="tr time">0</td></tr>');
            dom.find(".volume").text( U.formatcoin(data[i][2],  PairsDecimal[pairs]['amt_decimal']));
            dom.find(".time").text(formatTime(data[i][0]));
            dom.show().addClass("origin_trade");
            var price = Number(data[i][1]);
            if(data[i][3] == 'sell'){
                dom.find(".price").text( U.formatcoin(data[i][1],  PairsDecimal[pairs]['price_decimal'])).addClass("red").removeClass("green").attr("data", data[i][1]);
            }else if(data[i][3] == 'buy'){
                dom.find(".price").text( U.formatcoin(data[i][1],  PairsDecimal[pairs]['price_decimal'])).addClass("green").removeClass("red").attr("data", data[i][1]);
            }
            // if (U.bccomp(price, last_price) < 0) {
            //     dom.find(".price").text( U.formatcoin(data[i][1],  PairsDecimal[pairs]['price_decimal'])).addClass("red").removeClass("green").attr("data", data[i][1]);
            // } else {
            //     dom.find(".price").text( U.formatcoin(data[i][1],  PairsDecimal[pairs]['price_decimal'])).addClass("green").removeClass("red").attr("data", data[i][1]);
            // }
            old_book_last_price = last_price
            last_price = price;
            $("#trade_body").prepend(dom);
        }
        var asset = $("#quote_asset").val();
        var rate = C.againstCNY(asset);
        $(".book_symbol").find(".price").text(C.guzhi(U.bcmul(last_price, rate)));
        var pairs = $("#base_asset").val() + "_" + $("#quote_asset").val();
        //var old_book_last_price =  U.unFormatcoin($(".book_symbol").find(".last_price").first().text());
        if (U.bccomp(last_price, old_book_last_price) < 0) {
            $(".book_symbol").find(".last_price").text( U.formatcoin(last_price,  PairsDecimal[pairs]['price_decimal'])).addClass("red").removeClass("green");
            $(".book_symbol").find(".ico_arrow").addClass("ico_arr_b").removeClass("ico_arr_t");
        } else if (U.bccomp(last_price, old_book_last_price) == 0) {
            $(".book_symbol").find(".last_price").text( U.formatcoin(last_price,  PairsDecimal[pairs]['price_decimal'])).removeClass("red").removeClass("green");
            $(".book_symbol").find(".ico_arrow").addClass("ico_arr_b").removeClass("ico_arr_t").removeClass("ico_arr_b");
        } else {
            $(".book_symbol").find(".last_price").text( U.formatcoin(last_price,  PairsDecimal[pairs]['price_decimal'])).addClass("green").removeClass("red");
            $(".book_symbol").find(".ico_arrow").addClass("ico_arr_t").removeClass("ico_arr_b");
        }
        document.title =  U.formatcoin(last_price,  PairsDecimal[pairs]['price_decimal']) + ' (' + $("#base_asset").val() + "/" + $("#quote_asset").val() + ')';
        $("#trade_body").find("tr:gt(66)").remove();
    }

    function tradeClose(e) {
        console.log("trade connect closed:" + e.code);
        setTimeout(tradeConnect, timeout);
    }

    function tradeSend() {
        stompClient.send("/init_trade", {}, $("#base_asset").val() + "_" + $("#quote_asset").val());
    }

    /**
     * socket 连接
     */
    function socketConnect() {
        if (book) {
            console.log("book restart");
            Close(book);
        }
        if (trade) {
            console.log("restart");
            Close(trade);
        }
        try {

            // var socket = new SockJS($("#wss_url").val());
            var socket = new SockJS($("#wss_url").val());
            window.MARKET_LINE = stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                bookOpen();
                tradeOpen();


                if(loadend) {
                    bookSend();
                    tradeSend();
                }else{
                    setTimeout(bookSend, 1000);
                    setTimeout(tradeSend, 1000);
                }


                stompClient.subscribe("/exchange/" + $("#base_asset").val() + "_" + $("#quote_asset").val() +'@user_'+$("#getUserIds").val()+'', function(respnose){ //订阅/topic/getResponse 目标发送的消息。这个是在控制器的@SendTo中定义的。
                    listMessage(JSON.parse(respnose.body));
                });


                // console.log('Connected: ' + frame);
                stompClient.subscribe("/exchange/" + $("#base_asset").val() + "_" + $("#quote_asset").val() + '@book', function(respnose){ //订阅/topic/getResponse 目标发送的消息。这个是在控制器的@SendTo中定义的。
                    bookMessage(JSON.parse(respnose.body));
                });

                stompClient.subscribe("/exchange/" + $("#base_asset").val() + "_" + $("#quote_asset").val() + '@trade', function(respnose){ //订阅/topic/getResponse 目标发送的消息。这个是在控制器的@SendTo中定义的。

                    tradeMessage(JSON.parse(respnose.body));
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

    function bookConnect() {
        if (book) {
            console.log("book restart");
            Close(book);
        }
        try {
            book = new WebSocket($("#ws_url").val()  + $("#base_asset").val() + "_" + $("#quote_asset").val() + '@book');


        } catch (e) {
            console.log("book connect error");
            setTimeout(bookConnect, timeout);
            return;
        }
        book.onopen = bookOpen;
        book.onerror = bookError;
        book.onmessage = bookMessage;
        book.onclose = bookClose;
    }

    function bookOpen() {
        console.log('book connect open!');
        reload_book = 1;
        clearInterval(max_volume_intervel);
        max_volume_intervel = setInterval(function () {
            max_volume = 0;
            $(".origin_book").each(function () {
                var v = Number($(this).find(".volume").attr('data'));
                if (v > max_volume) {
                    max_volume = v;
                }
            });
            $(".origin_book").each(function () {
                var v = Number($(this).find(".volume").attr('data'));
                var vw;
                if (v >= max_volume) {
                    vw = 280;
                } else {
                    vw = 280 * v / max_volume;
                    if (vw < 10) {
                        vw = 10;
                    }
                }
                $(this).find(".zhuzhuangBg").width(vw);
            });
        }, 1000);
    }

    function bookError(e) {
        console.log("book error " + e);
    }

    function listMessage(msg) {
        if (Loading['book'] == undefined) {
            Loading['book'] = 1;
        }
        if (msg == null) {
            console.log('msg is null');
            return false;
        }
        if (reload_book == 1) {
            $(".origin_book").remove();
            reload_book = 0;
        }
        var data = JSON.parse(msg.data);
            //定价币
          $("#quote_account").text(data.lastMoney);
          //交易币
            $("#base_account").text(data.lastNum);

    }
    function bookMessage(msg) {


        if (Loading['book'] == undefined) {
            Loading['book'] = 1;
        }
        if (msg == null) {
            console.log('msg is null');
            return false;
        }
        if (reload_book == 1) {
            $(".origin_book").remove();
            reload_book = 0;
        }
        var data = JSON.parse(msg.data);
        if (data.length < 1) {
            return;
        }
        var a = data['sell'];
        var b = data['buy'];
        var len = 12 - a.length;
        if (len > 0) {
            for (var i = 0; i < len; i++) {
                $('#a').prepend('<tr><td></td><td></td><td></td></tr>');
            }
        }

        for (var i = 0; i < a.length; i++) {

            book_add(a[i], "#a", "oa", "ac");
        }
        for (var i = 0; i < b.length; i++) {
            book_add(b[i], "#b", "ob", "bc");
        }
    }

    function bookClose(e) {
        console.log("book closed:" + e.code);
        setTimeout(bookConnect, timeout);
    }

    function bookSend() {
        stompClient.send("/init_book", {}, $("#base_asset").val() + "_" + $("#quote_asset").val());
    }

    function book_add(order, pid, copy, pc) {
        var c = (pc + order[0]).replace(".", "");
        var volume = Number(order[1]);
        if (volume == 0) {
            $(pid).find("." + c).remove();
            return;
        } else {
            if (volume > max_volume) {
                max_volume;
            }
        }
        var w = volume / max_volume * 280;
        var dom = $(pid).find("." + c);
        var pairs = $("#base_asset").val() + "_" + $("#quote_asset").val();
        if (dom.length > 0) {
            dom.find(".price").text( U.formatcoin(order[0],  PairsDecimal[pairs]['price_decimal'])).attr("data", order[0]);
            dom.find(".volume").text( U.formatcoin(order[1],  PairsDecimal[pairs]['amt_decimal'])).attr("data", order[1]);
            var money = order[0] * order[1];
            dom.find(".money").text( U.formatcoin(money,  PairsDecimal[pairs]['amount_decimal']));
            dom.find(".zhuzhuangBg").width(w);
        } else {
            var dom = $("." + copy).clone(true);
            dom.find(".price").text( U.formatcoin(order[0],  PairsDecimal[pairs]['price_decimal'])).attr("data", order[0]);
            dom.find(".volume").text( U.formatcoin(order[1],  PairsDecimal[pairs]['amt_decimal'])).attr("data", order[1]);
            var money = order[0] * order[1];
            dom.find(".money").text( U.formatcoin(money,  PairsDecimal[pairs]['amount_decimal']));
            if (book_is_show()) {
 // debugger
                dom.addClass(c).removeClass(copy).addClass("origin_book").show();
            } else {
                dom.addClass(c).removeClass(copy).addClass("origin_book");
            }
            dom.find(".zhuzhuangBg").width(w);
            var now_p = Number(order[0]);
            $(pid).find("tr").each(function () {
                var this_p = Number($(this).find(".price").attr("data"));
                if (this_p < now_p) {
                    $(this).before(dom);
                    return false;
                }
            });
        }
    }

    function get_width(id, volume) {
        var size = $(id).find(".origin_book").length;
        if (size == 0) {
            return 50;
        }
        var max = 0;
        $(id).find(".origin_book").each(function () {
            var v = Number($(this).find(".volume").attr('data'));
            if (v > max) {
                max = v;
            }
        });
        var w;
        if (volume >= max) {
            w = 280;
        } else {
            w = 280 * volume / max;
            if (w < 30) {
                w = 30;
            }
        }
        $(id).find(".origin_book").each(function () {
            var v = Number($(this).find(".volume").attr('data'));
            var vw;
            if (v >= max) {
                vw = 280;
            } else {
                vw = 280 * v / max;
                if (vw < 30) {
                    vw = 30;
                }
            }
            $(this).find(".zhuzhuangBg").width(vw);
        });
        return w;
    }

    function depthMergeBind() {
        $(".selectDiv").hover(function () {
            $(this).find("ul").slideDown("fast");
        }, function () {
            $(this).find("ul").hide();
        });
        $(".selectDiv .select li").on("click", function () {
            $(".selectDiv .select").hide();
            $("#depth_num").text($(this).text()).attr("data", $(this).attr("data"));
        });
    }

    function depthMerge() {
        $("#bb").empty();
        $("#aa").empty();
        if (book_is_show()) {
            $("#bb").empty();
            $("#aa").empty();
            $(".origin_book").show();
        } else {
            var volume = 0;
            var price = 0;
            var money = 0;
            var pid = '#aa';
            var opid = '#a';
            var copy = 'oa';
            var num = $("#depth_num").attr("data");
            var abook = [];
            var bbook = [];
            $("#a").find("tr").each(function () {
                var item = [];
                item[0] = Number($(this).find(".price").text()).toFixed(num);
                item[1] = Number($(this).find(".volume").text());
                item[2] = Number($(this).find(".money").text());
                abook.push(item);
            });
            $("#b").find("tr").each(function () {
                var item = [];
                item[0] = Number($(this).find(".price").text()).toFixed(num);
                item[1] = Number($(this).find(".volume").text());
                item[2] = Number($(this).find(".money").text());
                bbook.push(item);
            });
            abook.sort(function (x, y) {
                return y[0] - x[0];
            });
            for (var i = 0; i < abook.length; i++) {
                var np = Number(abook[i][0]);
                if (price == np) {
                    volume = volume + Number(abook[i][1]);
                    money = money + Number(abook[i][2]);
                } else {
                    book_merge_add(pid, copy, price, volume, num, opid, money);
                    price = np;
                    volume = Number(abook[i][1]);
                    money = Number(abook[i][2]);
                }
            }
            book_merge_add(pid, copy, price, volume, num, opid, money);
            volume = 0;
            price = 0;
            money = 0;
            pid = '#bb';
            opid = '#b';
            copy = 'ob';
            bbook.sort(function (x, y) {
                return y[0] - x[0];
            });
            for (var i = 0; i < bbook.length; i++) {
                var np = Number(bbook[i][0]);
                if (price == np) {
                    volume = volume + Number(bbook[i][1]);
                    money = money + Number(bbook[i][2]);
                } else {
                    book_merge_add(pid, copy, price, volume, num, opid, money);
                    price = np;
                    volume = Number(bbook[i][1]);
                    money = Number(bbook[i][2]);
                }
            }
            book_merge_add(pid, copy, price, volume, num, opid, money);
            $(".origin_book").hide();
        }
    }

    function book_merge_add(pid, copy, price, volume, num, opid, money) {
        if (Number(volume) == 0) {
            return false;
        }
        var w = get_width(opid, volume);
        var dom = $("." + copy).clone(true);
        dom.find(".price").text(price.toFixed(num));
        dom.find(".volume").text(volume.toFixed(8).toString().substr(0, 10));
        dom.find(".money").text(money.toFixed(8).toString().substr(0, 10));
        dom.removeClass(copy).show();
        dom.find(".zhuzhuangBg").width(w);
        $(pid).append(dom).show();
    }
    function subimtPassow(id){
        if(id == 'buy'){
            var mspassowd = Number(U.unFormatcoin($("#tradePwd").text()))
        }else {
            var mspassowd = Number(U.unFormatcoin($("#selltradePwd").text()))
        }
        var  reg=/^\d{6}$/;
        if(U.bccomp(mspassowd) ==""){
            layer.prompt({
                formType:1,
                value: '',
                title: '请输入交易密码！'
            }, function(value,index){
                if(!reg.test(value)){
                    layer.msg('请输入六位数交易密码！', {time: 2000, icon: 7});
                    return false;
                }
                layer.close(index);
                var data = $("#" + id).serializeArray();
                var _data =[];
                $.each(data, function (i, field) {
                    if (field['name'] == 'tradePwd' ) {
                        field['value'] =  mMd5.hbmd5(value);
                        _data.push(field)
                    }else {
                        _data.push(field)
                    }
                });
                pdw_submit(_data,id);
            });
        }
    }

    function book_is_show() {
        var data = $("#depth_num").attr("data");
        var odata = $("#depth_num").attr("odata");
        if (data != odata) {
            return false
        } else {
            return true;
        }
    }
    var needPassword=false;
    function form_submit(id) {
        var price = Number($("#" + id).find(".price").val());
        if (price <= 0) {
            msg = Lang.price_is_required;
            layer.msg(msg, {time: 2000, icon: 2});
            return false;
        }
        var origin_amount = Number($("#" + id).find('.origin_amount').val());
        if (origin_amount <= 0) {
            msg = Lang.amount_is_required;
            layer.msg(msg, {time: 2000, icon: 2});
            return false;
        }
        //判断账户余额
        if(id == 'buy') {
            var amt_buy = Number(U.unFormatcoin($("#amt_buy").text()))//交易额
            var current = Number(U.unFormatcoin($('#quote_account').text())) //账户余额
            if(U.bccomp(amt_buy, current) > 0 ) {
                layer.msg('账户余额不足', {time: 2000, icon: 2});
                return false;
            }
             if(needPassword){
                subimtPassow(id);
             }else {
                 var data = $("#" + id).serializeArray();
                 var _data =[];
                 $.each(data, function (i, field) {
                     if (field['name'] == 'tradePwd' ) {
                     }else {
                         _data.push(field)
                     }
                 });
                 pdw_submit(_data,id);
             }

        }else if(id == 'sell'){
            var amt_sell = Number(U.unFormatcoin($("#amount_sell").text()))
            var current = Number(U.unFormatcoin($('#base_account').text())) //总
            if(U.bccomp(amt_sell, current) > 0) {
                layer.msg('账户余额不足', {time: 2000, icon: 2});
                return false;
            }
            if(needPassword){
                subimtPassow(id);
            }else {
                var data = $("#" + id).serializeArray();
                var _data =[];
                $.each(data, function (i, field) {
                    if (field['name'] == 'tradePwd' ) {

                    }else {
                        _data.push(field)
                    }
                });
                pdw_submit(_data,id);
            }


        }
    }
    function pdw_submit(vallist,id) {
        $.post("/exchange/trade/entrust",vallist, function (data) {
            order.trigger_asset_entrust();
            if(data.success){
                if (id == 'buy') {
                    var quote_account = $("#quote_account").text();
                    quote_account = Number(U.unFormatcoin(quote_account));
                    var amt_buy = $("#amt_buy").text();
                    amt_buy = Number(U.unFormatcoin(amt_buy));
                    quote_account = U.bcsub(quote_account, amt_buy);
                    // $("#quote_account").text(quote_account);
                    // $.ajax({url:"/exchange/coins_area",async:true}).then(function (data) {
                    //     var _data = JSON.parse(data);
                    //     for (var item in _data) {
                    //         trade_area[item] = JSON.parse(_data[item]);
                    //     }
                    //     var data = trade_area[code];
                    //
                    //     for (var items in data) {
                    //         data[items].last = data[items].last.toFixed(2);
                    //         data[items].duihuan = data[items].duihuan.toFixed(2);
                    //         // data[items].duihuan =list_most;
                    //
                    //     }
                    //     ;
                    //     tpl.render({"tplId": "#coin_area_tpl", "renderId": "#coin_area_render", "data": data});
                    //
                    // });
                } else {
                    var amount_sell=  $("#amount_sell").text();
                    var base_account = $("#base_account").text();
                    base_account = Number(U.unFormatcoin(base_account));
                    var amount_sell = $("#amt_sell").text();
                    amount_sell = Number(U.unFormatcoin(amount_sell));
                    base_account = U.bcsub(base_account, amount_sell);
                    // $("#base_account").text(base_account);
                    // $.ajax({url:"/exchange/coins_area",async:true}).then(function (data) {
                    //     var _data = JSON.parse(data);
                    //     for (var item in _data) {
                    //         trade_area[item] = JSON.parse(_data[item]);
                    //     }
                    //     var data = trade_area[code];
                    //
                    //     for (var items in data) {
                    //         data[items].last = data[items].last.toFixed(2);
                    //         data[items].duihuan = data[items].duihuan.toFixed(2);
                    //         // data[items].duihuan =list_most;
                    //
                    //     }
                    //     ;
                    //     tpl.render({"tplId": "#coin_area_tpl", "renderId": "#coin_area_render", "data": data});
                    //
                    // });
                }
                if (id == 'buy') {
                    $("#amount_buy").val("");
                    $("#tradePwd").val("");
                    $("#amt_buy").text("0");
                } else {
                    $("#tradePwd").val("");
                    $("#amount_sell").val("");
                    $("#amt_sell").text("0");
                }
            }else{
                if(data.data == "input_password"){
                    subimtPassow(id);
                    return;
                }else if(data.data == "password" ){
                    needPassword = true;
                }else if(data.data == "setTradepwd"){
                    needPassword = true;
                    window.location.href ="/member/security/settingdatapwd";
                }else{
                    needPassword = false;
                }
            }
            U.response(data);
        });

    }
    function change_amount(op, rate) {
        if (op != 1 && op != 2) {
            console.log("op err");
            return false;
        }
        if (op == 1) {

            var price = Number($("#price_buy").val());
            if (price <= 0) {
                console.log("price err");
                $("#amt_buy").text(0);
                return false;
            }
            var q_account=$("#quote_account").text();
            var quote_account = Number(U.unFormatcoin(q_account));
            if (quote_account <= 0) {
                $("#amt_buy").text(0);
                return false;
            }

            var n = quote_account * rate / price;
            var nn = n.toFixed(8);
            $("#amount_buy").val(nn);
        } else {

            var b_account=$("#base_account").text();

            var base_account =Number(U.unFormatcoin(b_account));
            if (base_account <= 0) {
                $("#amt_sell").text(0);
                return false;
            }
            $("#amount_sell").val((base_account * rate).toFixed(8));
        }
        change_amt(op);
    }
    function change_amt(op) {
        if (op != 1 && op != 2) {
            console.log("op err");
            return false;
        }
        if (op == 1) {
            var price_buy = $("#price_buy").val();
            var price_buys = price_buy.split(".");
            var price_decaimal = Number($("#price_decimal").val());
            if (price_buys[1] !== undefined) {
                if (price_buys[1].length > price_decaimal) {
                    if (price_decaimal > 0) {
                        price_buy = price_buys[0] + "." + price_buys[1].substring(0, price_decaimal);
                    } else {
                        price_buy = price_buys[0];
                    }
                    $("#price_buy").val(price_buy);
                }
            }
            var amount_buy = $("#amount_buy").val().split(".");
            var amt_decimal = Number($("#amt_decimal").val());
            if (amount_buy[1] !== undefined) {
                if (amount_buy[1].length > amt_decimal) {
                    if (amt_decimal > 0) {
                        amount_buy = amount_buy[0] + "." + amount_buy[1].substring(0, amt_decimal);
                    } else {
                        amount_buy = amount_buy[0];
                    }
                    $("#amount_buy").val(amount_buy);
                }
            }
            if (isNaN($("#price_buy").val())) {
                console.log("price err");
                $("#price_buy").val(0)
                $("#amt_buy").text(0);
                return false;
            }
            var price = Number($("#price_buy").val());
            if (price <= 0) {
                console.log("price 0");
                $("#amt_buy").text(0);
                return false;
            }
            if (isNaN($("#amount_buy").val())) {
                $("#amt_buy").text(0);
                console.log("amount_buy err");
                return false;
            }
            var amount = Number($("#amount_buy").val());
            if (amount <= 0) {
                $("#amt_buy").text(0);
                return false;
            }
            var amt = (amount * price).toFixed(8);
            $("#amt_buy").text(amt);
        } else {
            var price_sell = $("#price_sell").val().split(".");
            var price_decaimal = Number($("#price_decimal").val());
            if (price_sell[1] !== undefined) {
                if (price_sell[1].length > price_decaimal) {
                    if (price_decaimal > 0) {
                        price_sell = price_sell[0] + "." + price_sell[1].substring(0, price_decaimal);
                    } else {
                        price_sell = price_sell[0];
                    }
                    $("#price_sell").val(price_sell);
                }
            }
            var amount_sell = $("#amount_sell").val().split(".");
            var amt_decimal = Number($("#amt_decimal").val());
            if (amount_sell[1] !== undefined) {
                if (amount_sell[1].length > amt_decimal) {
                    if (amt_decimal > 0) {
                        amount_sell = amount_sell[0] + "." + amount_sell[1].substring(0, amt_decimal);
                    } else {
                        amount_sell = amount_sell[0];
                    }
                    $("#amount_sell").val(amount_sell);
                }
            }
            if (isNaN($("#price_sell").val())) {
                console.log("price err");
                $("#amt_sell").text(0);
                return false;
            }
            if (isNaN($("#amount_sell").val())) {
                console.log("amount_sell err");
                $("#amt_sell").text(0);
                return false;
            }
            var price = Number($("#price_sell").val());
            if (price <= 0) {
                console.log("price err");
                $("#amt_sell").text(0);
                return false;
            }
            var amount = $("#amount_sell").val();
            if (amount <= 0) {

                $("#amt_sell").text(0);
                return false;
            }
            var amt = (amount * price).toFixed(8);
            $("#amt_sell").text(amt);
        }
    }



    function Close(ws) {
        ws.close();
    }

    function add0(m) {
        return m < 10 ? '0' + m : m
    }

    function formatTime(needTime) {
        var time = new Date(parseInt(needTime));
        var h = time.getHours();
        var mm = time.getMinutes();
        var s = time.getSeconds();
        return add0(h) + ':' + add0(mm) + ':' + add0(s);
    }
    function init(){
        $(document).ready(function () {
            C.buy_sell_first();
            socketConnect();
            depthMergeBind();
            $(document).on("click", ".origin_trade,.origin_book", function () {
                var price = $(this).find('.price').attr("data");
                var red = $(this).find('.red').attr("data");
                if(red !=undefined ){
                    $("#price_buy").val(price);
                    change_amt(1);
                }else{
                    $("#price_sell").val(price);
                    change_amt(2);
                }
            });
            $(".btn-buy-x").on("click",function () {
                form_submit('buy');
            })
            $(".btn-sell-x").on("click",function () {
                form_submit('sell');
            })
            
            // 进度
            $(".slider-25").on("click",function () {
                change_amount(1,0.25);
            })
            $(".slider-50").on("click",function () {
                change_amount(1,0.5);
            })
            $(".slider-75").on("click",function () {
                change_amount(1,0.75);
            })
            $(".slider-100").on("click",function () {
                change_amount(1,1);
            })

            $(".slider-sell-25").on("click",function () {
                change_amount(2,0.25);
            })
            $(".slider-sell-50").on("click",function () {
                change_amount(2,0.5);
            })
            $(".slider-sell-75").on("click",function () {
                change_amount(2,0.75);
            })
            $(".slider-sell-100").on("click",function () {
                change_amount(2,1);
            })

            $("input[id='price_buy']").bind('input propertychange', function() {
                change_amt(1)
            });
            $("input[id='amount_buy']").bind('input oninput', function() {
                change_amt(1)
            });

            $("input[id='price_sell']").bind('input propertychange', function() {
                change_amt(2)
            });
            $("input[id='amount_sell']").bind('input oninput', function() {
                change_amt(2)
            });
        })
    }

    exports.init=init;
});