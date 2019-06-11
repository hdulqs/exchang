/**
 *<p>page_exchange_eventLog.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {
    var mMd5 = require('./module_md5'),

        tpl = require('./module_tpl'),
        U = require('./module_utils'),

        core = require('./module_core');
    function init() {
      /*  $(function(){
            if($("#google_blind_pop").length>0){
                $("#google_blind_pop").find(".closePopBt").hide();
                $("#google_blind_pop").find(".prompt .tr").hide();
                U.showGoogleBlinding();
            }
        });*/
        jQuery.divselect = function (divselectid, inputselectid) {
            $(document).on("click", divselectid + " cite", function () {
                if ($(this).hasClass("lock") == true) {
                    return false;
                }
                var ul = $(this).parent(divselectid).find("ul");
                if (ul.css("display") == "none") {
                    ul.show();
                } else {
                    ul.hide();
                }
            });
            $(document).on("click", divselectid + " ul li a", function () {
                var txt = $(this).html();
                $(this).parents(divselectid).find("cite").html(txt);
                var value = $(this).attr("selectid");
                $(this).parents(divselectid).find(inputselectid).val(value);
                $(divselectid + " ul").hide();
            });
            $(document).on("click", "body", function (e) {
                if ($(e.target).parents(divselectid).length == 0) {
                    if ($(divselectid + " ul").is(':visible')) {
                        $(divselectid + " ul").hide();
                    }
                }
            });
        };
        $(function () {
            $(window).bind('resize', function (e) {
                var _windowH = $(window).height();
                changeFullScreen(_windowH);
            });
            var page_data = $.parseJSON($("#page_data").val());
            $("#page_data").val("");
            $("#cashAddressBar").data("withdraw_data", page_data);
            $("input[name='memo']").data("show_memo_symbol", page_data['show_memo_symbol']);
            $.divselect("#divSelect", ".selectInput");
            $("#divSelect ul li a").on("click", function () {
                $("#divSelect ul li:gt(0)").show();
                $("#divSelect .recharSearch input").val("");
                var symbol = $(this).attr("selectid");
                var alert_msg = $(this).attr("alert_msg");
                $("input[name='symbol']").val(symbol);
                $("#alert_msg").val(alert_msg);
                if ($("#divSelect cite").hasClass("lock") == true) {
                    return false;
                }
                $.ajax({
                    type: "GET",
                    url: "/asset/get_withdraw",
                    async: true,
                    data: {"symbol": symbol},
                    dataType: "json",
                    beforeSend: function () {
                        $("#divSelect cite").addClass("lock");
                    },
                    success: function (result) {
                        $("#divSelect cite").removeClass("lock");
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
                            init_page_data(result['data']);
                        }
                    }
                });
            });
            $.divselect("#cashAddressBar", ".selectInput");
            $(document).on("click", "#cashAddressBar .selectBt", function () {
                $("#cashAddressBar cite").click();
            });
            $(document).on("click", "#cashAddressBar ul li:not(.tc) a", function () {
                init_address();
                $("#cashAddressBar").removeClass("bgG");
                var config_data = $("#cashAddressBar").data("withdraw_data");
                var address_id = $(this).attr("selectid");
                var address_info = config_data['address'][address_id];
                $("input[name='tag']").val(address_info['tag']);
                $("input[name='address']").val(address_info['address']);
                $("input[name='memo']").val(address_info['memo']);
                $("#addNewAddressBox").show();
            });
            $(document).on("click", "#cashAddressBar .addAddressBt", function () {
                init_address();
                $("#cashAddressBar ul").toggle();
                $("#addNewAddressBox").show();
                $("#cashAddressBar cite").text(Lang['input_withdraw_address']);
                $("#cashAddressBar").addClass("bgG")
                $("#cashAddressBar .selectInput").val("0");
            });
            $("#all_withdraw").on("click", function () {
                var available_bal = U.unFormatcoin($("#available_bal").text());
                $("input[name='amt']").val(available_bal);
                calculate();
            });
            $(document).on("click", ".address_li .icon_del", function () {
                var li = $(this).parents(".address_li");
                var id = li.find("a").attr("selectid");
                if (li.hasClass("lock") == true) {
                    return false;
                }
                $.ajax({
                    type: "POST",
                    url: "/withdraw/del_address",
                    async: true,
                    data: {id: id, _token: $("meta[name='csrf-token']").attr("content")},
                    dataType: "json",
                    beforeSend: function () {
                        li.addClass("lock");
                    },
                    success: function (result) {
                        li.removeClass("lock");
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
                            li.remove();
                        }
                    }
                });
            });
            $("form#withdraw-form .submitBt").on("click", function () {
                var bool = withdraw_valid();
                if (bool == true) {
                    layer.open({
                        type: 1,
                        title: Lang['google_verify'],
                        area: ['400px', 'auto'],
                        shadeClose: true,
                        content: $("#googleVerifyPop"),
                        success: function () {
                            $("#googleVerifyPop .google_input").val("").focus();
                            var click_submit = function () {
                                var code = $.trim($("#googleVerifyPop .google_input").val());
                                if (code.length > 0) {
                                    $("#withdraw-form input[name='google_code']").val(code);
                                } else {
                                    return false;
                                }
                                if ($("form#withdraw-form").hasClass("lock") == true) {
                                    return false;
                                }
                                withdraw_submit();
                            };
                            $("#googleVerifyPop .google_input").on("keyup", function () {
                                if ($(this).val().length == 6) {
                                    click_submit();
                                }
                            });
                            $(".googleVerify .gold_button").on("click", function () {
                                click_submit();
                            });
                        }
                    });
                }
            });
            $(".recharSearch input").on('compositionstart', function () {
                $(this).prop('comStart', true);
            }).on('compositionend', function () {
                $(this).prop('comStart', false);
            }).on("input propertychange", function () {
                if ($(this).prop('comStart') == true) {
                    return false;
                } else {
                    search();
                }
            }).keyup(function (event) {
                if (!event) {
                    event = window.event;
                }
                if (event.keyCode == 13 || event.keyCode == 32 || event.keyCode == 16) {
                    search();
                }
            });
            $(document).on("click", "#withdraw_success_pop .closePopBt", function () {
                layer.closeAll();
                window.location.reload(true);
            });
        });

        function changeFullScreen(_windowH) {
            if ($(document.body).height() < _windowH) {
                var _marginBottom = _windowH - $(".rechargeWrap").offset().top - $(".rechargeWrap").height() - $(".footerNavBar").height() - $(".footerBar").height() - 42 - 45;
                $(".rechargeWrap").css("margin-bottom", _marginBottom + "px");
            }
        }

        function search() {
            var txt = $.trim($("#divSelect .recharSearch input").val().toUpperCase());
            if (txt != "") {
                $("#divSelect ul li:gt(0)").hide().each(function () {
                    var keyword1 = $(this).find("strong").text().toUpperCase();
                    var keyword2 = $(this).find("span.no-blinding").text().toUpperCase();
                    if (keyword1.indexOf(txt) >= 0 || keyword2.indexOf(txt) >= 0) {
                        $(this).show();
                    }
                });
            } else {
                $("#divSelect ul li:gt(0)").show();
            }
        }

        function init_address() {
            $("#addNewAddressBox").hide();
            $("input[name='tag']").val("");
            $("input[name='address']").val("");
            $("input[name='memo']").val("");
            var withdraw_data = $("#cashAddressBar").data("withdraw_data");
            var show_memo_symbol = $("input[name='memo']").data("show_memo_symbol");
            if ($.inArray(withdraw_data['symbol_info']['blockchain'], show_memo_symbol) < 0) {
                $("input[name='memo']").parents("#withdraw-form li").addClass("hide");
            } else {
                $("input[name='memo']").parents("#withdraw-form li").removeClass("hide");
            }
        }

        function init_page_data(result) {
            $("#cashAddressBar").data("withdraw_data", result);
            $(".rechargeWrap .unit").text(result['symbol_info']['symbol']);
            $("#bal").text(U.formatcoin(rtrim_zero(result['acc_info']['bal'])));
            $("#frozen_bal").text(U.formatcoin(rtrim_zero(result['acc_info']['frozen_bal'])));
            $("#available_bal").text(U.formatcoin(rtrim_zero(result['acc_info']['available_bal'])));
            if (result['symbol_info']['alert_msg'].length > 0) {
                $("#symbol_tips").text(result['symbol_info']['alert_msg']);
                $(".withdraw_forbid").removeClass("hide");
                $(".withdraw_operation").addClass("hide");
                return false;
            }
            var input_amt = $("input[name='amt']").attr("placeholder");
            var input_placeholder = input_amt.split("锛�");
            $("input[name='amt']").attr("placeholder", input_placeholder[0] + "锛�" + U.formatcoin(rtrim_zero(result['acc_info']['available_bal'])));
            $("input[name='amt']").val("");
            $("#fee").text(U.formatcoin("0"));
            $("#trueamt").text(U.formatcoin("0"));
            $("#withdraw_min_amt").text(U.formatcoin(rtrim_zero(result['symbol_info']['withdraw_min_amt'])));
            if (U.bccomp(result['symbol_info']['notauth_max_withdraw_amt'], 0) > 0 && $("#notauth_max_withdraw_amt").length > 0) {
                $(".not_auth_withdraw_tips").removeClass("hide");
            } else {
                $(".not_auth_withdraw_tips").addClass("hide");
            }
            $("#notauth_max_withdraw_amt").text(U.formatcoin(rtrim_zero(result['symbol_info']['notauth_max_withdraw_amt'])));
            if (U.bccomp(result['symbol_info']['withdraw_ac_max_amt'], 0) == 0) {
                $("#withdraw_ac_max_amt").parents(".withdraw_ac_max_amt_tips").addClass("hide");
            } else {
                $("#withdraw_ac_max_amt").parents(".withdraw_ac_max_amt_tips").removeClass("hide");
            }
            $("#withdraw_ac_max_amt").text(U.formatcoin(rtrim_zero(result['symbol_info']['withdraw_ac_max_amt'])));
            var html_address = "";
            $.each(result['address'], function (key, value) {
                html_address += '<li class="address_li"><a href="javascript:void(0);" selectid="' + value['id'] + '">' + value['tag'] + ' - ' + value['address'] + '</a><span class="icon_del"></span></li>';
            });
            $("#cashAddressBar ul li:not(.tc)").remove();
            $("#cashAddressBar ul").append(html_address);
            $("#cashAddressBar cite").text(Lang['select_withdraw_address']);
            $("input[name='selected_address']").val("0");
            $("#cashAddressBar").removeClass("bgG");
            init_address();
            $(".withdraw_forbid").addClass("hide");
            $(".withdraw_operation").removeClass("hide");
        }

        function calculate() {
            var config_data = $("#cashAddressBar").data("withdraw_data");
            var position = $("input[name='amt']").getCurPos();
            var amt = $.trim($("input[name='amt']").val().replace(/[^0-9.]/g, ''));
            var withdraw_decimal = Number(config_data['symbol_info']['withdraw_decimal']);
            if (amt.indexOf('.') >= 0) {
                var amt_decimal = amt.slice(amt.indexOf('.') + 1).length;
                if (amt_decimal > withdraw_decimal) {
                    amt = amt.substr(0, amt.length - (amt_decimal - withdraw_decimal));
                }
            }
            $("input[name='amt']").val(amt).setCurPos(position, position);
            if (amt.length == 0 || isNaN(amt) == true) {
                $("#fee").text(U.formatcoin("0"));
                $("#trueamt").text(U.formatcoin("0"));
                return false;
            }
            amt = Number(amt);
            var min_amt = Number(config_data['symbol_info']['withdraw_min_amt']);
            var max_amt = Number(config_data['symbol_info']['withdraw_max_amt']);
            var base_fee_amt = Number(config_data['symbol_info']['withdraw_base_fee_amt']);
            var fee_type = Number(config_data['symbol_info']['withdraw_fee_type']);
            var fee_data = Number(config_data['symbol_info']['withdraw_fee']);
            var fee = 0;
            if (fee_type == 0) {
                fee = U.bcadd(base_fee_amt, fee_data);
            } else {
                fee = U.bcadd(base_fee_amt, bcmul(fee_data, amt));
            }
            var trueamt = U.bcsub(amt, fee);
            if (trueamt < 0) {
                trueamt = 0;
            }
            $("#fee").text(U.formatcoin(fee));
            $("#trueamt").text(U.formatcoin(trueamt));
        }

        function withdraw_valid() {
            var form = $("form#withdraw-form");
            if (form.hasClass("lock") == true) {
                return false;
            }
            if (form.find("#addNewAddressBox").is(":visible") == false) {
                return false;
            }
            var tag = form.find("input[name='tag']").val();
            var address = form.find("input[name='address']").val();
            if (tag.length == 0) {
                layer.tips(Lang['tag_no_empty'], "form#withdraw-form input[name='tag']", {time: 2000});
                return false;
            }
            if (address.length == 0) {
                layer.tips(Lang['address_no_empty'], "form#withdraw-form input[name='address']", {time: 2000});
                return false;
            }
            var amt = $.trim(form.find("input[name='amt']").val());
            if (amt.length == 0) {
                layer.tips(Lang['input_amt'], "form#withdraw-form input[name='amt']", {tips: 1, time: 2000});
                return false;
            }
            if (amt < 0) {
                layer.tips(Lang['input_amt_error'], "form#withdraw-form input[name='amt']", {tips: 1, time: 2000});
                return false;
            }
            if (isNaN(amt) == true) {
                layer.tips(Lang['input_right_amt'], "form#withdraw-form input[name='amt']", {tips: 1, time: 2000});
                return false;
            }
            var config_data = $("#cashAddressBar").data("withdraw_data");
            var min_amt = Number(config_data['symbol_info']['withdraw_min_amt']);
            var max_amt = Number(config_data['symbol_info']['withdraw_max_amt']);
            var base_fee_amt = Number(config_data['symbol_info']['withdraw_base_fee_amt']);
            var fee_type = Number(config_data['symbol_info']['withdraw_fee_type']);
            var fee_data = Number(config_data['symbol_info']['withdraw_fee']);
            var withdraw_decimal = Number(config_data['symbol_info']['withdraw_decimal']);
            var notauth_max_withdraw_amt = Number(config_data['symbol_info']['notauth_max_withdraw_amt']);
            if (amt.indexOf('.') >= 0) {
                var amt_decimal = amt.slice(amt.indexOf('.') + 1).length;
                if (amt_decimal > withdraw_decimal) {
                    amt = amt.substr(0, amt.length - (amt_decimal - withdraw_decimal));
                }
            }
            var fee = 0;
            if (fee_type == 0) {
                fee = U.bcadd(base_fee_amt, fee_data);
            } else {
                fee = U.bcadd(base_fee_amt, bcmul(fee_data, amt));
            }
            var trueamt = U.bcsub(amt, fee);
            if (trueamt < 0) {
                layer.tips(Lang['withdraw_no_reach_min'], "form#withdraw-form input[name='amt']", {
                    tips: 1,
                    time: 2000
                });
                return false;
            }
            if (U.bccomp(amt, config_data['acc_info']['available_bal']) > 0) {
                layer.tips(Lang['input_right_amt'], "form#withdraw-form input[name='amt']", {tips: 1, time: 2000});
                return false;
            }
            if (U.bccomp(min_amt, amt) > 0) {
                console.log(U.formatcoin(min_amt));
                var tips = Lang['withdraw_no_reach_min'];
                layer.tips(tips, "form#withdraw-form input[name='amt']", {tips: 1, time: 2000});
                return false;
            }
            if (U.bccomp(notauth_max_withdraw_amt, 0) > 0 && $("#notauth_max_withdraw_amt").length > 0) {
                if (U.bccomp(notauth_max_withdraw_amt, amt) < 0) {
                    layer.tips(Lang['not_auth_withdraw_beyond_limit'], "form#withdraw-form input[name='amt']", {
                        tips: 1,
                        time: 2000
                    });
                    return false;
                }
            }
            if (U.bccomp(max_amt, amt) < 0) {
                layer.tips(Lang['withdraw_beyond_limit'], "form#withdraw-form input[name='amt']", {
                    tips: 1,
                    time: 2000
                });
                return false;
            }
            return true;
        }

        function withdraw_submit() {
            var form = $("form#withdraw-form");
            if (form.hasClass("lock") == true) {
                return false;
            }
            $.ajax({
                type: "POST",
                url: "/asset/withdraw",
                async: true,
                data: $("form#withdraw-form").serialize(),
                dataType: "json",
                beforeSend: function () {
                    form.addClass("lock");
                },
                success: function (result) {
                    form.removeClass("lock");
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
                        layer.closeAll();
                        layer.open({
                            type: 1,
                            title: false,
                            closeBtn: 0,
                            scrollbar: false,
                            shade: 0.8,
                            area: '560px',
                            shadeClose: false,
                            content: $('#withdraw_success_pop'),
                        });
                    }
                }
            });
        }
    }

    exports.init = init;
});