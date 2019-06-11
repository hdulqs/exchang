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

$(function () {
    $(document).on("click", "#mainBottom .t_menu li", function () {
        var dataType = $(this).attr("dataType");
        $("#mainBottom .t_menu li").removeClass("cur");
        $(this).addClass("cur");
        $("#mainBottom .t_cont").hide();
        if (dataType == 1) {
            $("#current_entrust").show();
            $(".toggleBt").show();
        } else if (dataType == 2) {
            $("#history_entrust").show();
            $(".toggleBt").show();
            var dateType = $("#history_entrust_search .h_bt.curr").attr("dateType");
            $("#history_entrust_search").data("dateType", dateType);
            $("#history_entrust_search").data("p", 0);
            history_entrust();
        } else if (dataType == 3) {
            $("#history_trade").show();
            $(".toggleBt").show();
            var dateType = $("#history_trade_search .h_bt.curr").attr("dateType");
            $("#history_trade_search").data("dateType", dateType);
            $("#history_trade_search").data("p", 0);
            history_trade();
        } else if (dataType == 4) {
            $("#asset_manage").show();
            $(".toggleBt").hide();
            asset_manage();
        }
    });
    current_entrust();
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
        type: "GET", url: "/trade/entrust", data: {type: 1}, dataType: "json", success: function (result_json) {

            if (result_json['code'] != 0) {
                return false;
            }
            var list = result_json['data']['list'];
            if (list.length == 0) {
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
            $("#current_entrust .areaTable table tbody").html(obj_html);
            hide_other_pairs();
        }
    });
}

function history_entrust() {
    var dateType = $("#history_entrust_search").data("dateType");
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
            if (is_null(start_date) == false && start_date.length > 0) {
                start_time = (new Date(start_date.replace(/-/g, "/") + " 00:00:00").getTime()) / 1000;
            }
            if (is_null(end_date) == false && end_date.length > 0) {
                end_time = (new Date(end_date.replace(/-/g, "/") + " 23:59:59").getTime()) / 1000;
            }
            break;
    }
    if (is_null(p) == true) {
        p = 0;
    }
    p = p + 1;
    if (p == 1) {
        $("#history_entrust .areaTable table tbody tr").remove();
    }
    $("#history_entrust_search").data("p", p);
    $.ajax({
        type: "GET",
        url: "/trade/entrust",
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
            var list = result_json['data']['list'];
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
            if (is_null(start_date) == false && start_date.length > 0) {
                start_time = (new Date(start_date.replace(/-/g, "/") + " 00:00:00").getTime()) / 1000;
            }
            if (is_null(end_date) == false && end_date.length > 0) {
                end_time = (new Date(end_date.replace(/-/g, "/") + " 23:59:59").getTime()) / 1000;
            }
            break;
    }
    if (is_null(p) == true) {
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
        type: "GET", url: "/trade/asset_manage", dataType: "json", success: function (result_json) {
            if (result_json['code'] != 0) {
                return false;
            }
            var list = result_json['data']['list'];
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
    var pairs = data['base_asset'] + "_" + data['quote_asset'];
    html = '<tr oid="' + data['order_id'] + '">' +
        '<td width="1%">&nbsp;</td>' +
        '<td width="16%"><span timestamp="' + data['created_at_timestamp'] + '"></span></td>' +
        '<td width="12%">' + data['base_asset'] + '/' + data['quote_asset'] + '</td>' +
        '<td width="8%">' + Lang['order_type' + data['type']] + '</td>' +
        '<td width="8%"><span class="' + config_color[data['side']] + '">' + Lang['side' + data['side']] + '</span></td>' +
        '<td width="9%">' + formatKKcoin(data['price'], PairsDecimal[pairs]['price_decimal']) + '</td>' +
        '<td width="12%">' + formatKKcoin(data['origin_amount'], PairsDecimal[pairs]['amt_decimal']) + '</td>' +
        '<td width="12%">' + getPercent(bcdiv(data['executed_amount'], data['origin_amount'])) + '</td>' +
        '<td width="12%">' + formatKKcoin(data['executed_quote_amount'], PairsDecimal[pairs]['amount_decimal']) + '</td>' +
        '<td width="10%"><a href="javascript:void(0)" class="button" onclick="cancel_order(this,\'' + data['order_id'] + '\');">' + Lang['cancel'] + '</a></td>' +
        '</tr>';
    return html;
}

function build_history_entrust_html(data) {
    var config_color = {1: "green", 2: "red"};
    var html = "";
    var pairs = data['base_asset'] + "_" + data['quote_asset'];
    html = '<tr>' +
        '<td width="10%" class="tc"><span timestamp="' + data['created_at_timestamp'] + '"></span></td>' +
        '<td width="10%" class="tc">' + data['base_asset'] + '/' + data['quote_asset'] + '</td>' +
        '<td width="10%" class="tc">' + Lang['order_type' + data['type']] + '</td>' +
        '<td width="10%" class="tc"><span class="' + config_color[data['side']] + '">' + Lang['side' + data['side']] + '</span></td>' +
        '<td width="10%" class="tc">' + formatKKcoin(data['executed_price'], PairsDecimal[pairs]['price_decimal']) + '</td>' +
        '<td width="10%" class="tc">' + formatKKcoin(data['price'], PairsDecimal[pairs]['price_decimal']) + '</td>' +
        '<td width="10%" class="tc">' + formatKKcoin(data['executed_amount'], PairsDecimal[pairs]['amt_decimal']) + '</td>' +
        '<td width="10%" class="tc">' + formatKKcoin(data['origin_amount'], PairsDecimal[pairs]['amt_decimal']) + '</td>' +
        '<td width="10%" class="tc">' + formatKKcoin(data['executed_quote_amount'], PairsDecimal[pairs]['amount_decimal']) + '</td>' +
        '<td width="10%" class="tc">' + Lang['order_status' + data['status']] + '</td>' +
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
        '<td width="14%" class="tc">' + formatKKcoin(data['price'], PairsDecimal[pairs]['price_decimal']) + '</td>' +
        '<td width="14%" class="tc">' + formatKKcoin(data['amount'], PairsDecimal[pairs]['amt_decimal']) + '</td>' +
        '<td width="14%" class="tc">' + formatKKcoin(data['quote_amount'], PairsDecimal[pairs]['amount_decimal']) + '</td>' +
        '<td width="16%" class="tc">' + formatKKcoin(data['fee'], PairsDecimal[pairs]['amount_decimal']) + data['fee_asset'] + '</td>' +
        '</tr>';
    return html;
}

function build_asset_manage_html(data) {
    var html = "";
    html = '<tr>' +
        '<td width="20%">' + data['asset_symbol'] + '</td>' +
        '<td width="20%" class="bal">' + formatKKcoin(rtrim_zero(data['bal'])) + '</td>' +
        '<td width="20%">' + formatKKcoin(rtrim_zero(data['available_bal'])) + '</td>' +
        '<td width="20%">' + formatKKcoin(rtrim_zero(data['frozen_bal'])) + '</td>' +
        '<td width="20%" class="guzhi" symbol="' + data['asset_symbol'] + '"></td>' +
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

function cancel_order(obj, order_id) {
    if ($(obj).hasClass("lock") == true) {
        return false;
    }
    $.ajax({
        type: "POST",
        url: '/entrust/cancel',
        async: true,
        data: {order_id: order_id, _token: $("meta[name='csrf-token']").attr("content")},
        dataType: "json",
        beforeSend: function () {
            $(obj).addClass("lock");
        },
        success: function (result) {
            $(obj).removeClass("lock");
            if (result['code'] > 0) {
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
                    $(obj).parents(".areaTable tr").remove();
                });
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
            type: "GET", url: '/entrust/cancel', async: true, dataType: "json", beforeSend: function () {
                $(obj).addClass("lock");
            }, success: function (result) {
                $(obj).removeClass("lock");
                if (result['code'] > 0) {
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
                    $("#current_entrust .areaTable table tbody tr").remove();
                }
            }, complete: function () {
                $(obj).removeClass("lock");
            }
        });
    });
}

function asset_guzhi(aset_symbol) {
    var rate = Number($("#exchange_rate").val());
    var lang = getCookie("lang");
    if (aset_symbol == undefined || aset_symbol == null || aset_symbol == "") {
        $("#asset_manage .areaTable .guzhi[symbol]").each(function () {
            var symbol = $(this).attr("symbol");
            var tr = $(this).parents("#asset_manage .areaTable table tr");
            var bal = unFormatKKcoin(tr.find(".bal").text());
            if (lang == 'en') {
                var guzhi = bal * againstCNY(symbol) / rate;
            } else {
                var guzhi = bal * againstCNY(symbol);
            }
            $(this).text(formatKKcoin(guzhi.toFixed(2)));
        });
    } else {
        $("#asset_manage .areaTable .guzhi[symbol='" + symbol + "']").each(function () {
            var symbol = $(this).attr("symbol");
            var tr = $(this).parents("#asset_manage .areaTable table tr");
            var bal = unFormatKKcoin(tr.find(".bal").text())
            if (lang == 'en') {
                var guzhi = bal * againstCNY(symbol) / rate;
            } else {
                var guzhi = bal * againstCNY(symbol);
            }
            $(this).text(formatKKcoin(guzhi.toFixed(2)));
        });
    }
}

function trigger_asset_entrust() {
    return false;
    var dataType = $("#mainBottom .t_menu li.cur").attr("dataType");
    if (dataType == 4) {
        asset_manage();
    }
}

function init_order_data(i) {
    var pairs = i[1].split("_");
    var data = {
        order_id: i[0],
        created_at_timestamp: bcdiv(i[10], 1000),
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
        bal();
        return false;
    }
    $("#current_entrust .no_data_tpl").hide();
    if (data['status'] == 3 && obj.length > 0) {
        obj.find("td:eq(7)").text(getPercent(bcdiv(data['executed_amount'], data['origin_amount'])));
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
    bal();
}

function getNowDiffDays(day) {
    var result = {};
    var now_date = (new Date()).format("yyyy/MM/dd");
    var limit_timestamp = (new Date(now_date + " 23:59:59")).getTime() / 1000;
    result.start_time = limit_timestamp - 24 * 60 * 60 * day + 1;
    result.end_time = limit_timestamp;
    return result;
}