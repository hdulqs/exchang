/**
 *<p>page_market.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {
    var
        U = require('./module_utils'),
        core = require('./module_core'),
        C = require('./page_exchange_common');
    var order;
    var timeout = 5000;


    function orderConnect() {
        if (order) {
            console.log("restart");
            Close(order);
        }
        try {
            order = new WebSocket($("#ws_url").val() + 'all@order');
        } catch (e) {
            console.log("order connect error");
            setTimeout(function () {
                orderConnect();
            }, timeout);
            return;
        }
        order.onopen = orderOpen;
        order.onerror = orderError;
        order.onmessage = orderMessage;
        order.onclose = orderClose;
    }

    function orderOpen() {
        console.log('order connect open!');
        if (Loading['order'] == undefined) {
            Loading['order'] = 1;
        }
    }

    function orderError(e) {
        console.log("error " + e);
    }

    function orderMessage(msg) {
        if (msg == null) {
            console.log('msg is null');
            return false;
        }
        
        var i = JSON.parse(msg.data);
        if (!i[1]) {
            return;
        }
        init_order_data(i);
    }

    function orderClose(e) {
        console.log("connect closed:" + e.code);
        setTimeout(function () {
            orderConnect();
        }, timeout);
    }

    function orderSend() {
    }

    function Close(ws) {
        ws.close();
    }


    function blind_date(start_id, end_id) {
        var date_lang = 'cn';
        if ($("#languageBt").hasClass("cn") == false) {
            date_lang = 'en';
        }
        laydate.render({
            elem: "#" + start_id, lang: date_lang, done: function (value) {
                var end_date = $("#" + end_id).val();
                if (end_date.length > 0) {
                    if (compare_date(value, end_date) == -1) {
                        $("#" + start_id).val("");
                        return false;
                    }
                }
            }
        });
        laydate.render({
            elem: '#' + end_id, lang: date_lang, done: function (value) {
                var start_date = $("#" + start_id).val();
                if (start_date.length > 0) {
                    if (compare_date(start_date, value) == -1) {
                        $("#" + end_id).val("");
                        return false;
                    }
                }
            }
        });
    }

    function current_entrust() {
        $.ajax({
            type: "GET", url: "/exchange/trade/findOwnerEntrust/"+$("#symbol").val(),  dataType: "json", success: function (result_json) {

                if (result_json['success'] == false ) {
                        return false;
                    }
                var list = result_json['data']['ing'];
                if (list.length == 0) {
                    $("#current_entrust .areaTable table tbody").empty();
                    $("#current_entrust .areaTable .no_data_tpl").show();
                    return false;
                }
                $("#current_entrust .areaTable .no_data_tpl").hide();
                var html = "";
                for (var key in list) {
                    html += build_current_entrust_html(list[key]);
                }
                var obj_html = $(html);
                obj_html.find("*[timestamp]").each(function () {
                    var timestamp = $(this).attr('timestamp');
                    $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
                });

                obj_html.find("*[data-action]").each(function (a,e) {
                    var data = $(e).attr('data-action');
                    var type = $(e).attr('data-type')
                    $(e).on("click",function () {
                        cancel_order($(e),data, type);
                    })
                    build_current_entrust_html

                });



                $("#current_entrust .areaTable table tbody").html(obj_html);

                hide_other_pairs();
            }
        });
    }

    function history_entrust() {
        var dateType = $("#history_entrust_search").data("dateType") ? $("#history_entrust_search").data("dateType") : 1;
        var p = $("#history_entrust_search").data("p");
        var start_date = $("#history_entrust_start_date").val();
        var end_date = $("#history_entrust_end_date").val();
        var start_time, end_time;
        switch (parseInt(dateType)) {
            case 1:
                var result_days = getNowDiffDays(1);
                start_time = result_days.start_time;
                end_time = result_days.end_time;
                break;
            case 2:
                var result_days = getNowDiffDays(7);
                start_time = result_days.start_time;
                end_time = result_days.end_time;
                break;
            case 3:
                var result_days = getNowDiffDays(30);
                start_time = result_days.start_time;
                end_time = result_days.end_time;
                break;
            case 4:
                var result_days = getNowDiffDays(90);
                start_time = result_days.start_time;
                end_time = result_days.end_time;
                break;
            case 5:
                if (U.is_null(start_date) == false && start_date.length > 0) {
                    start_time = (new Date(start_date.replace(/-/g, "/") + " 00:00:00").getTime()) / 1000;
                }
                if (U.is_null(end_date) == false && end_date.length > 0) {
                    end_time = (new Date(end_date.replace(/-/g, "/") + " 23:59:59").getTime()) / 1000;
                }
                break;
        }
        if (U.is_null(p) == true) {
            p = 0;
        }
        p = p + 1;
        if (p == 1) {
            $("#history_entrust .areaTable table tbody tr").remove();
        }
        $("#history_entrust_search").data("p", p);
        $.ajax({
            type: "GET",
            url: "/exchange/trade/findHistoryEntrust/"+$("#symbol").val(),
            data: {type: 2, dateType: dateType, start_time: start_time, end_time: end_time, p: p},
            dataType: "json",
            beforeSend: function () {
                $("#history_entrust_search").data("is_requesting", 1);
            },
            success: function (result_json) {
                if (p == 1) {
                    $("#history_entrust .areaTable table tbody tr").remove();
                }
                if (result_json['code'] != 0) {
                    return false;
                }
                var list = result_json['data']['history'];
                if (list.length == 0) {
                    if (p == 1) {
                        $("#history_entrust .areaTable .no_data_tpl").show();
                    }
                    return false;
                }
                $("#history_entrust .areaTable .no_data_tpl").hide();
                var html = "";
                for (var key in list) {
                    html += build_history_entrust_html(list[key]);
                }
                var obj_html = $(html);
                obj_html.find("*[timestamp]").each(function () {
                    var timestamp = $(this).attr('timestamp');
                    $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
                });
                $("#history_entrust .areaTable table tbody").append(obj_html);
                hide_other_pairs();
            },
            complete: function () {
                $("#history_entrust_search").data("is_requesting", 0);
            }
        });
    }

    function history_trade() {
        var dateType = $("#history_trade_search").data("dateType");
        var p = $("#history_trade_search").data("p");
        var start_date = $("#history_trade_start_date").val();
        var end_date = $("#history_trade_end_date").val();
        var start_time, end_time;
        switch (parseInt(dateType)) {
            case 1:
                var result_days = getNowDiffDays(1);
                start_time = result_days.start_time;
                end_time = result_days.end_time;
                break;
            case 2:
                var result_days = getNowDiffDays(7);
                start_time = result_days.start_time;
                end_time = result_days.end_time;
                break;
            case 3:
                var result_days = getNowDiffDays(30);
                start_time = result_days.start_time;
                end_time = result_days.end_time;
                break;
            case 4:
                var result_days = getNowDiffDays(90);
                start_time = result_days.start_time;
                end_time = result_days.end_time;
                break;
            case 5:
                if (U.is_null(start_date) == false && start_date.length > 0) {
                    start_time = (new Date(start_date.replace(/-/g, "/") + " 00:00:00").getTime()) / 1000;
                }
                if (U.is_null(end_date) == false && end_date.length > 0) {
                    end_time = (new Date(end_date.replace(/-/g, "/") + " 23:59:59").getTime()) / 1000;
                }
                break;
        }
        if (U.is_null(p) == true) {
            p = 0;
        }
        p = p + 1;
        if (p == 1) {
            $("#history_trade .areaTable table tbody tr").remove();
        }
        $("#history_trade_search").data("p", p);
        $.ajax({
            type: "GET",
            url: "/trade/history_trade",
            data: {dateType: dateType, start_time: start_time, end_time: end_time, p: p},
            dataType: "json",
            beforeSend: function () {
                $("#history_trade_search").data("is_requesting", 1);
            },
            success: function (result_json) {
                if (p == 1) {
                    $("#history_trade .areaTable table tbody tr").remove();
                }
                if (result_json['code'] != 0) {
                    return false;
                }
                var list = result_json['data']['list'];
                if (list.length == 0) {
                    if (p == 1) {
                        $("#history_trade .areaTable .no_data_tpl").show();
                    }
                    return false;
                }
                $("#history_trade .areaTable .no_data_tpl").hide();
                var html = "";

                for (var key in list) {
                    html += build_history_trade_html(list[key]);
                }
                var obj_html = $(html);
                obj_html.find("*[timestamp]").each(function () {
                    var timestamp = $(this).attr('timestamp');
                    $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
                });
                $("#history_trade .areaTable table tbody").append(obj_html);
                hide_other_pairs();
            },
            complete: function () {
                $("#history_trade_search").data("is_requesting", 0);
            }
        });
    }

    function asset_manage() {
        $.ajax({
            type: "GET", url: "/ex/member/account/asset", dataType: "json", success: function (result_json) {
                if (result_json['code'] == 0) {
                    return false;
                }
                var list = result_json['data'];
                if (list.length == 0) {
                    $("#asset_manage .areaTable .no_data_tpl").show();
                    return false;
                }
                $("#asset_manage .areaTable .no_data_tpl").hide();
                var html = "";
                for (var key in list) {
                    html += build_asset_manage_html(list[key]);
                }
                $("#asset_manage .areaTable table tbody").html(html);
                asset_guzhi(null);


            }
        });
    }



    function build_current_entrust_html(data) {
        var config_color = {1: "green", 2: "red"};
        var html = "";
            var pairs = data['tradeCoinCode'] + "_" + data['pricingCoinCode'];
            html = '<tr oid="' + data['orderId'] + '">' +

                '<td width="10%"><span timestamp="' + data['entrustTime'] + '"></span></td>' +
                '<td width="10%">' + data['tradeCoinCode'] + '/' + data['pricingCoinCode'] + '</td>' +
                '<td width="10%">' + Lang['order_category' + data['category']] + '</td>' +
                '<td width="8%"><span class="' + config_color[data['entrustType']] + '">' + Lang['side_' + data['entrustType']] + '</span></td>' +
                '<td width="7%" class="price_decimal_sym">' + U.formatcoin(data['entrustPrice'], PairsDecimal[pairs]['price_decimal']) + '</td>' +
                '<td width="10%" class="amt_decimal_sym">' + U.formatcoin(data['entrustAmout'], PairsDecimal[pairs]['amt_decimal']) + '</td>' +
                '<td width="10%">' + U.formatcoin(data['entrustAmoutSql'], PairsDecimal[pairs]['amt_decimal']) + '</td>' +
                '<td width="10%">' + data['ratio'] + '</td>' +
                '<td width="10%">' + data['transactionAmount'] + '</td>' +
                // '<td width="10%">' + U.formatcoin(0, PairsDecimal[pairs]['transactionAmount']) + '</td>' +
                '<td width="10%"><a href="javascript:void(0)" data-type="'+ Lang['side_' + data['entrustType']] +'" data-action="' + data['orderId'] + '" class="button" >' + Lang['cancel'] + '</a></td>' +
                '</tr>';
        return html;
    }

    function build_history_entrust_html(data) {

        var config_color = {1: "green", 2: "red"};
        var html = "";

            var pairs = data['tradeCoinCode'] + "_" + data['pricingCoinCode'];
            html = '<tr>' +
                '<td width="10%" class="tc"><span timestamp="' + data['entrustTime'] + '"></span></td>' +
                '<td width="10%" class="tc">' + data['tradeCoinCode'] + '/' + data['pricingCoinCode'] + '</td>' +
                '<td width="10%" class="tc">' + Lang['order_category' + data['category']] + '</td>' +
                '<td width="10%" class="tc"><span class="' + config_color[data['entrustType']] + '">' + Lang['side_' + data['entrustType']] + '</span></td>' +
                '<td width="10%" class="tc">' + U.formatcoin(data['executedPrice'], PairsDecimal[pairs]['price_decimal']) + '</td>' +
                '<td width="10%" class="tc">' + U.formatcoin(data['entrustPrice'], PairsDecimal[pairs]['price_decimal']) + '</td>' +
                '<td width="10%" class="tc">' + U.formatcoin(data['volume'], PairsDecimal[pairs]['amt_decimal']) + '</td>' +
                '<td width="10%" class="tc">' + data['transactionAmount'] + '</td>' +
                '</tr>';
        return html;
    }

    function build_history_trade_html(data) {
        var config_color = {1: "green", 2: "red"};
        var html = "";
        var pairs = data['base_asset'] + "_" + data['quote_asset'];
        html = '<tr>' +
            '<td width="14%" class="tc"><span timestamp="' + data['created_at_timestamp'] + '"></span></td>' +
            '<td width="14%" class="tc">' + data['base_asset'] + '/' + data['quote_asset'] + '</td>' +
            '<td width="14%" class="tc"><span class="' + config_color[data['side']] + '">' + Lang['side' + data['side']] + '</span></td>' +
            '<td width="14%" class="tc">' + U.formatcoin(data['price'], PairsDecimal[pairs]['price_decimal']) + '</td>' +
            '<td width="14%" class="tc">' + U.formatcoin(data['amount'], PairsDecimal[pairs]['amt_decimal']) + '</td>' +
            '<td width="14%" class="tc">' + U.formatcoin(data['quote_amount'], PairsDecimal[pairs]['amount_decimal']) + '</td>' +
            '<td width="16%" class="tc">' + U.formatcoin(data['fee'], PairsDecimal[pairs]['amount_decimal']) + data['fee_asset'] + '</td>' +
            '</tr>';
        return html;
    }

    function build_asset_manage_html(data) {
        var html = "";
        html = '<tr>' +
            '<td width="25%">' + data['coinCode'] + '</td>' +
            '<td width="25%" class="bal">' + U.formatcoin(U.rtrim_zero(data['totalMoney'])) + '</td>' +
            '<td width="25%">' + U.formatcoin(U.rtrim_zero(data['available'])) + '</td>' +
            '<td width="25%">' + U.formatcoin(U.rtrim_zero(data['freeze'])) + '</td>' +
            '</tr>';
        return html;
    }

    function hide_other_pairs() {
        if ($(".toggleBt").hasClass("item_hide") == true) {
            var current_pairs = $.trim($("#current_pairs .coin_select").text());
            current_pairs = current_pairs.replace(" ", "");
            $("#mainBottom .areaTable tr").hide();
            $("#mainBottom .areaTable tr:contains('" + current_pairs + "')").show();
        } else {
            $("#mainBottom .areaTable tr").show();
        }
    }

    function cancel_order(obj, order_id, type) {
        if ($(obj).hasClass("lock") == true) {
            return false;
        }
        $.ajax({
            type: "POST",
            url: '/exchange/trade/cancel',
            async: true,
            data: {orderId: order_id,coinPair:$("#symbol").val(), _token: $("meta[name='csrf-token']").attr("content")},
            dataType: "json",
            beforeSend: function () {
                $(obj).addClass("lock");
            },
            success: function (result) {
                $(obj).removeClass("lock");
                if (!result.success ){
                    layer.alert(result['msg'], {
                        title: Lang['alert_title'],
                        btn: Lang['confirm_button'],
                        closeBtn: 0,
                    }, function (index) {
                        layer.close(index);
                        if (result['url'] != undefined) {
                            window.location = result['url'];
                        }
                    });
                    return false;
                } else {
                    layer.msg(Lang['cancel_success'], {anim: 5, time: 2000}, function () {

                    });
                    var amt_decimal_sym = $(obj).parent().parent().find('.amt_decimal_sym').text()

                    amt_decimal_sym = Number(U.unFormatcoin(amt_decimal_sym));
                    if(type == '买入') {
                        var quote_account = $("#quote_account").text();
                        quote_account = Number(U.unFormatcoin(quote_account));

                        var price_decimal_sym = $(obj).parent().parent().find('.price_decimal_sym').text()
                        price_decimal_sym = Number(U.unFormatcoin(price_decimal_sym));

                        quote_account = U.bcadd(quote_account, U.bcmul(amt_decimal_sym, price_decimal_sym))

                        $("#quote_account").text(quote_account);

                    }else if(type == '卖出'){
                        var base_account = $("#base_account").text();
                        base_account = Number(U.unFormatcoin(base_account));
                        base_account = U.bcadd(base_account, amt_decimal_sym)
                        $("#base_account").text(base_account)
                    }
                    

                    $(obj).parents(".areaTable tr").remove();
                }
            }
        });
    }

    function all_cancel(obj) {
        if ($("#current_entrust .areaTable tr[oid]").length == 0) {
            return false;
        }
        if ($(obj).hasClass("lock") == true) {
            return false;
        }
        layer.confirm(Lang['cancel_confirm'], {
            icon: 3,
            title: Lang['confirm_title'],
            button: [Lang['confirm_button'], Lang['cancel_button']],
            closeBtn: 0,
        }, function (index) {
            layer.close(index);
            $.ajax({
                type: "GET", url: '/exchange/trade/cancelAll?coinPair='+$("#symbol").val(), async: true, dataType: "json",
                beforeSend: function () {
                    $(obj).addClass("lock");
                }, success: function (result) {
                    $(obj).removeClass("lock");
                    if (!result.success) {
                        layer.alert(result['msg'], {
                            title: Lang['alert_title'],
                            btn: Lang['confirm_button'],
                            closeBtn: 0,
                        }, function (index) {
                            layer.close(index);
                            if (result['url'] != undefined) {
                                window.location = result['url'];
                            }
                        });
                        return false;
                    } else {
                        var quote_account = $("#quote_account").text();
                        var amount_buy = Number(U.unFormatcoin(quote_account))

                        var base_account = $("#base_account").text();
                        var amount_sell = Number(U.unFormatcoin(base_account));

                        $("#current_entrust .areaTable table tbody tr").each(function(index,e){
                            var type = $(e).find('.button').attr('data-type')
                            var amt_decimal_sym = $(e).find('.amt_decimal_sym').text()
                            amt_decimal_sym = Number(U.unFormatcoin(amt_decimal_sym));

                            if(type == '买入') {
                                var price_decimal_sym = $(e).find('.price_decimal_sym').text()
                                price_decimal_sym = Number(U.unFormatcoin(price_decimal_sym));
                                amount_buy = U.bcadd(amount_buy, U.bcmul(amt_decimal_sym, price_decimal_sym))
                            }else if(type == '卖出'){
                                amount_sell = U.bcadd(amount_sell, amt_decimal_sym)
                            }
                        })
                        $("#quote_account").text(amount_buy)
                        $("#base_account").text(amount_sell)

                        $("#current_entrust .areaTable table tbody tr").remove();
                    }
                }, complete: function () {
                    $(obj).removeClass("lock");
                }
            });
        });
    }

    function asset_guzhi(aset_symbol) {
        if (aset_symbol == undefined || aset_symbol == null || aset_symbol == "") {
            $("#asset_manage .areaTable .guzhi[symbol]").each(function () {
                var symbol = $(this).attr("symbol");
                var tr = $(this).parents("#asset_manage .areaTable table tr");
                var bal = U.unFormatcoin(tr.find(".bal").text())
                var guzhi = bal * C.againstCNY(symbol);
                $(this).text(U.formatcoin(guzhi.toFixed(2)));
            });
        } else {
            $("#asset_manage .areaTable .guzhi[symbol='" + symbol + "']").each(function () {
                var symbol = $(this).attr("symbol");
                var tr = $(this).parents("#asset_manage .areaTable table tr");
                var bal = U.unFormatcoin(tr.find(".bal").text())
                var guzhi = bal * C.againstCNY(symbol);
                $(this).text(U.formatcoin(C.guzhi.toFixed(2)));
            });
        }
    }

    function trigger_asset_entrust() {
        current_entrust();
        history_entrust();
        return false;
        var dataType = $("#mainBottom .t_menu li.cur").attr("dataType");
        if (dataType == 4) {
            if(core.isLogin()){
                asset_manage();

            }else{
                $("#asset_manage .areaTable .no_data_tpl").show();
            }
        }
    }

    function init_order_data(i) {
        var pairs = i[1].split("_");
        var data = {
            order_id: i[0],
            created_at_timestamp: U.bcdiv(i[10], 1000),
            base_asset: pairs[0],
            quote_asset: pairs[1],
            type: i[2],
            side: i[3],
            price: i[4],
            origin_amount: i[5],
            executed_amount: i[7],
            executed_quote_amount: i[8],
            status: i[9],
        };
        var obj = $("#current_entrust .areaTable tr[oid='" + data['order_id'] + "']");
        if (data['status'] == 2 || data['status'] == 4) {
            obj.remove();
            if ($("#current_entrust .areaTable tr").length == 0) {
                $("#current_entrust .no_data_tpl").show();
            }
            C.bal();
            return false;
        }
        $("#current_entrust .no_data_tpl").hide();
        if (data['status'] == 3 && obj.length > 0) {
            obj.find("td:eq(7)").text(U.getPercent(U.bcdiv(data['executed_amount'], data['origin_amount'])));
            obj.find("td:eq(8)").text(data['executed_quote_amount'] + data['quote_asset']);
        } else if (data['status'] == 1) {
            var html = build_current_entrust_html(data);
            var obj_html = $(html);
            obj_html.find("*[timestamp]").each(function () {
                var timestamp = $(this).attr('timestamp');
                $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
            });
            $("#current_entrust .areaTable table tbody").prepend(obj_html);
            hide_other_pairs();
        }
        C.bal();
    }

    function getNowDiffDays(day) {
        var result = {};
        var now_date = (new Date()).format("yyyy/MM/dd");
        var limit_timestamp = (new Date(now_date + " 23:59:59")).getTime() / 1000;
        result.start_time = limit_timestamp - 24 * 60 * 60 * day + 1;
        result.end_time = limit_timestamp;
        return result;
    }

    function init() {

        $(function () {

            $(".all_cancel").on("click",function () {
                all_cancel(this);
            })

            if ($(".userStatus .icoUser").length > 0) {
                //orderConnect();
            }
            $(document).on("click", "#mainBottom .t_menu li", function () {
                var dataType = $(this).attr("dataType");
                $("#mainBottom .t_menu li").removeClass("cur");
                $(this).addClass("cur");
                $("#mainBottom .t_cont").hide();
                if (dataType == 1) {
                    $("#current_entrust").show();
                    $(".toggleBt").show();
                    current_entrust();
                } else if (dataType == 2) {
                    $("#history_entrust").show();
                    $(".toggleBt").show();
                    var dateType = $("#history_entrust_search .h_bt.curr").attr("dateType");
                    $("#history_entrust_search").data("dateType", dateType);
                    $("#history_entrust_search").data("p", 0);


                    if(core.isLogin()){
                        history_entrust();
                    }else{
                        $("#history_entrust .areaTable .no_data_tpl").show();
                    }

                } else if (dataType == 3) {
                    $("#history_trade").show();
                    $(".toggleBt").show();
                    var dateType = $("#history_trade_search .h_bt.curr").attr("dateType");
                    $("#history_trade_search").data("dateType", dateType);
                    $("#history_trade_search").data("p", 0);
                    if(core.isLogin()){
                        history_trade();
                    }else{
                        $("#history_trade .areaTable .no_data_tpl").show();
                    }

                } else if (dataType == 4) {
                    $("#asset_manage").show();
                    $(".toggleBt").hide();
                    if(core.isLogin()){
                        asset_manage()
                    }else{
                        $("#asset_manage .areaTable .no_data_tpl").show();
                    }

                }
            });
            if(core.isLogin()){
                current_entrust();
            }else{
                $("#current_entrust .areaTable .no_data_tpl").show();
            }

            blind_date("history_entrust_start_date", "history_entrust_end_date");
            $(document).on("click", "#history_entrust span.h_bt[dateType]", function () {
                if ($("#history_entrust_search").data("is_requesting") == 1) {
                    return false;
                }
                $("#history_entrust span.h_bt[dateType]").removeClass("curr");
                $(this).addClass("curr");
                var dateType = $(this).attr("dateType");
                $("#history_entrust_search").data("dateType", dateType);
                $("#history_entrust_search").data("p", 0);
                history_entrust();
            });
            $(document).on("click", "#history_entrust .s_bt", function () {
                if ($("#history_entrust_search").data("is_requesting") == 1) {
                    return false;
                }
                var start_date = $.trim($("#history_entrust_start_date").val());
                var end_date = $.trim($("#history_entrust_end_date").val());
                if (start_date.length == 0 || end_date.length == 0) {
                    return false;
                }
                $("#history_entrust_search").data("dateType", 5);
                $("#history_entrust_search").data("p", 0);
                history_entrust();
            });
            $("#history_entrust .scrollStyle").scroll(function () {
                var scroll_top = $("#history_entrust .scrollStyle")[0].scrollTop;
                var scroll_height = $("#history_entrust .scrollStyle")[0].scrollHeight;
                var client_height = $("#history_entrust .scrollStyle").height();
                var limit_height = scroll_height - 50;
                if ((scroll_top + client_height) >= limit_height) {
                    if ($("#history_entrust_search").data("is_requesting") == 1) {
                        return false;
                    }
                    history_entrust();
                }
            });
            blind_date("history_trade_start_date", "history_trade_end_date");
            $(document).on("click", "#history_trade span.h_bt[dateType]", function () {
                if ($("#history_trade_search").data("is_requesting") == 1) {
                    return false;
                }
                $("#history_trade span.h_bt[dateType]").removeClass("curr");
                $(this).addClass("curr");
                var dateType = $(this).attr("dateType");
                $("#history_trade_search").data("dateType", dateType);
                $("#history_trade_search").data("p", 0);
                history_trade();
            });
            $(document).on("click", "#history_trade .s_bt", function () {
                if ($("#history_trade_search").data("is_requesting") == 1) {
                    return false;
                }
                var start_date = $.trim($("#history_trade_start_date").val());
                var end_date = $.trim($("#history_trade_end_date").val());
                if (start_date.length == 0 || end_date.length == 0) {
                    return false;
                }
                $("#history_trade_search").data("dateType", 5);
                $("#history_trade_search").data("p", 0);
                history_trade();
            });
            $("#history_trade .scrollStyle").scroll(function () {
                var scroll_top = $("#history_trade .scrollStyle")[0].scrollTop;
                var scroll_height = $("#history_trade .scrollStyle")[0].scrollHeight;
                var client_height = $("#history_trade .scrollStyle").height();
                var limit_height = scroll_height - 50;
                if ((scroll_top + client_height) >= limit_height) {
                    if ($("#history_trade_search").data("is_requesting") == 1) {
                        return false;
                    }
                    history_trade();
                }
            });
            $(".toggleBt").on("click", function () {
                if ($(this).hasClass("item_hide")) {
                    $(this).removeClass("item_hide");
                    $(this).text(Lang['hide_other_pairs']);
                } else {
                    $(this).addClass("item_hide");
                    $(this).text(Lang['show_other_pairs']);
                }
                hide_other_pairs();
            });
        });

    }

    exports.trigger_asset_entrust=trigger_asset_entrust;
    exports.asset_guzhi=asset_guzhi;
    exports.init = init;
});