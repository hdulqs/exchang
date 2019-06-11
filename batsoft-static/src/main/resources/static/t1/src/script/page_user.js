/**
 *<p>page_user.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {

    var mMd5 = require('./module_md5'),
        table = require('./module_table'),
        tpl = require('./module_tpl'),
        U = require('./module_utils'),
        P = require('./module_asynPage'),
        effect = require('./module_effect'), //特效
        core = require('./module_core');
    var balances =  500

    /**
     * 用户头部公共信息加载
     */
    function globalInit() {
        var userInfo = GLOBAL.user;
        userInfo.oldLoginTime = U.dateFormat(new Date(userInfo.oldLoginTime), "yyyy-MM-dd hh:mm:ss")
        tpl.render({"tplId": "#userInfoTmpl", "renderId": "#userInfoRender", "data": userInfo});
        //银行信息
        core.list({url:BASE_PATH+ "/member/security/findBindCard"}).then(function (data) {
            if(data.data){
                tpl.render({"tplId": "#bankInfoTmpl_bind", "renderId": "#bankInfoRender", "data": data.data});
            }else{
                tpl.render({"tplId": "#bankInfoTmpl_unbind", "renderId": "#bankInfoRender", "data": data.data});
            }
        })
        //登陆日志列表
        core.list({url:BASE_PATH+ "/ex/member/user/findLogs"}).then(function (data) {
            tpl.render({"tplId": "#memberLoginTmpl", "renderId": "#memberLoginRender", "data": data.data});
        })
    }


    /**
     * 用户首页
     */
    function init() {
        $(function () {
            $("*[timestamp]").each(function () {
                var timestamp = $(this).attr('timestamp');
                $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
            });
            if ($("#google_blind_pop").length > 0) {
                $("#google_blind_pop").find(".closePopBt").hide();
                U.showGoogleBlinding();
            }
            $("#identity_auth").on("click", function (e) {
                if ($("#google_blind_pop").length > 0) {
                    $("#google_blind_pop").find(".closePopBt").hide();
                    U.showGoogleBlinding();
                    U.stopDefault(e);
                }
            });
        });
    }


    /**
     * 加载安全日志 list
     */
    function logList() {
        init();
        /**
         * table conf
         * @type {{url: string, removeUrl: string, columns: [*]}}
         */
        var conf = {
            url: BASE_PATH + '/member/log/list',
            columns: [
                {
                    field: 'state',
                    checkbox: true
                }
                , {
                    field: 'id',
                    class: 'hidde',
                    title: 'id'
                }
                , {
                    field: 'userName',
                    title: '登录时间',
                    sortable: true
                }
                , {
                    field: 'password',
                    title: 'ip地址',
                    sortable: true
                }
                , {
                    field: 'status',
                    title: '状态',
                    sortable: true
                }]
        };

        /**
         * 初始化
         */
        table.initTable($('#table'), conf);

    };


    /**
     * 我的资产
     */
    function account() {
        $(function () {
            $(document).on("mouseleave", ".span_inline ul,.span_tips", function (event) {
                $(this).addClass("hide");
                $(this).parents(".span_inline").find(".editCommBt").removeClass("button_gold");
            });
            $(document).on("click", ".span_inline .editCommBt", function () {
                var show_ul = $(".span_inline ul:visible");
                show_ul.parents(".span_inline").find(".editCommBt").removeClass("button_gold");
                show_ul.addClass("hide");

                $(".span_inline .span_tips:visible").parents(".span_inline").find(".editCommBt").removeClass("button_gold");
                $(".span_inline .span_tips:visible").addClass("hide");

                var ul = $(this).parents(".span_inline").find("ul");
                if (ul.find("li").length == 0) {
                    $(this).parents(".span_inline").find(".span_tips").removeClass("hide")
                } else {
                    ul.removeClass("hide");
                }
                $(this).addClass("button_gold");
            });

            $(window).bind('resize', function () {
                var _windowH = $(window).height();
                changeFullScreen(_windowH);
            });
            $(".questionTip").hover(function () {
                var tip = $(this).attr("tip");
                layer.tips(tip, this);
            }, function () {
                layer.closeAll();
            });
            $(".hideAssetsBt").on("click", function () {
                $(this).toggleClass("hide");
                var min_amt = $(this).attr("min_amt");
                if ($(this).hasClass("hide") == true) {
                    $(this).find("span").text(Lang['show_small_amt']);
                    $(".marketsTable tr:not('.theader')").each(function () {
                        var bal = U.unFormatcoin($(this).find("td:eq(3)").text());
                        if (parseFloat(bal) < parseFloat(min_amt)) {
                            $(this).addClass("min_amt_hide");
                        }
                    });
                } else {
                    $(this).find("span").text(Lang['hide_small_amt']);
                    $(".marketsTable tr.min_amt_hide").removeClass("min_amt_hide");
                }
            });
            $(".disabledTip").hover(function () {
                layer.tips($(this).attr("notice"), this, {time: 0});
            }, function () {
                layer.closeAll();
            });
            $(".searchBox input").on('compositionstart', function () {
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
            $(".searchBt").bind('click', function () {
                search();
            });
        });


        function search() {
            var txt = $.trim($(".searchInput").val().toUpperCase());
            if (txt.length > 0) {
                $(".wrapMain h3").hide();
                $(".marketsTable:eq(1)").hide();
                $(".marketsTable:first tr:not('.theader'):not('.min_amt_hide')").addClass("hide").each(function () {
                    var keyword1 = $(this).find("td:eq(1)").text().toUpperCase();
                    var keyword2 = $(this).find("td:eq(2)").text().toUpperCase();
                    if (keyword1.indexOf(txt) >= 0 || keyword2.indexOf(txt) >= 0) {
                        $(this).removeClass("hide");
                    }
                });
            } else {
                $(".marketsTable:first tr.tr_hide").addClass("hide");
                $(".marketsTable:first tr:not('.theader'):not('.tr_hide'):not('.min_amt_hide')").removeClass("hide");
                $(".wrapMain h3").show();
                $(".marketsTable:eq(1)").show();
            }
        }

        function changeFullScreen(_windowH) {
            if ($(document.body).height() < _windowH) {
                var _fullScreenH = _windowH - $(".fullScreen_h").offset().top - $(".footerNavBar").height() - $(".footerBar").height() - 15 - 30;
                $(".fullScreen_h").css("min-height", _fullScreenH + "px");
            }
        }

        // 加载正常币种
        core.list({url: BASE_PATH + "/ex/member/account/financeData"}).then(function (data) {
            // var data_list = JSON.parse(data.data.customerAccountVos);
             tpl.render({"tplId": "#coin_tpl", "renderId": "#coin_render", "data": data.data.customerAccountVos});
            var data_money = data.data;
            $("#balances").text(data_money.total+" " +"CNY")

        })
        // 加载下架币种
        // core.list({url: BASE_PATH + "/ex/member/account/financeUnder"}).then(function (data) {
        //     var datas = data.data.customerAccountVos;
        //     for (var k = 0;k < datas.length;k++) {
        //         var postallowRecharge = datas[k].allowRecharge;
        //         var postallowWithdraw = datas[k].allowWithdraw;
        //         if(postallowRecharge == 0){
        //             $("body").find(".editCommBt1").each(function () {
        //                 $(this).mouseover(function(){
        //                     $(this).css("color","yellow");
        //                 });
        //             })
        //
        //         }
        //         if(postallowWithdraw == 0){
        //             $('#pc').removeAttr('href');
        //             $('#pc').removeAttr('onclick');
        //             $('.editCommBt2').removeAttr('href');
        //             $('.editCommBt2').removeAttr('onclick');
        //             $('.editCommBt2').css('color','#C7CCE6');
        //         }
        //     }
        //     tpl.render({"tplId": "#coin_under_tpl", "renderId": "#coin_under_render", "data": data.data});
        // })

    }


    /**
     * 提币
     */

    function withdraw() {
        //是否图形验证通过
        var verity = false;
        var validate;
        //验证码初始化
        var captchaIns;
        initNECaptcha({
            captchaId: '87cc815627084c41bb8cb582e9378997',
            element: '#captcha',
            mode: 'popup',
            width: 320,
            onClose: function () {
                // 弹出关闭结束后将会触发该函数
                $(".sendCode").on("click", function () {
                    var _this = $(this);
                    var mobile_v = $(".form").validate().element($("#userName"));
                    if(!verity){
                        $(".tip").removeClass("p-hide").html($('#validCode').val());
                        return;
                    }
                    if (mobile_v && verity) {
                        _this.attr("disabled", "true");

                    }else{
                        $(".tip").removeClass("p-hide").html("请确保手机号和图形验证码正确");
                    }
                })
            },
            onVerify: function (err, data) {
                if(err!=null){
                    $(".tip").removeClass("p-hide").html($('#validCodeError').val());
                    verity = false;
                }else{
                    validate = data.validate;
                    $(".tip").addClass("p-hide");
                    verity = true;
                    var options={"url":BASE_PATH+"/sendCode","data":{"mobile":$("#userName").val(),"validate":validate,"areaCode":$("#areaCode").val()}}
                    core.get(options).then(function (data) {
                        if(data.success == true){
                            U.timer(data.data.codeTimeOut,$(".sendCode"));
                            $(".tip").addClass("p-hide").html("");
                        }else {
                            $(".sendCode").removeAttr("disabled");
                            $(".tip").removeClass("p-hide").html(data.msg);
                        }
                    }).fail(function (jqXHR, textStatus) {
                        $(".tip").removeClass("p-hide").html("网络异常");
                    });
                }
            }
        }, function onload (instance) {
            // 初始化成功后，用户输入对应用户名和密码，以及完成验证后，直接点击登录按钮即可
            captchaIns = instance;
        }, function onerror (err) {
            // 验证码初始化失败处理逻辑，例如：提示用户点击按钮重新初始化
            $(".tip").removeClass("p-hide").html(err);
            verity = false;
        }),
        // 监听button的点击事件，`弹出验证码
        document.getElementById('button_pot').addEventListener('click', function (e) {
            e.preventDefault();
            if(captchaIns){
                captchaIns.refresh();
                captchaIns.popUp();
            }
        })
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
            if ($.inArray(withdraw_data['coinCode'], show_memo_symbol) < 0) {
                $("input[name='memo']").parents("#withdraw-form li").addClass("hide");
            } else {
                $("input[name='memo']").parents("#withdraw-form li").removeClass("hide");
            }
        }

        function init_page_data(result) {
            $("#cashAddressBar").data("withdraw_data", result);
            $(".rechargeWrap .unit").text(result['coin']['coinCode']);
            $("#bal").text(U.formatcoin(U.rtrim_zero(result['account']['bal'])));
            $("#frozen_bal").text(U.formatcoin(U.rtrim_zero(result['account']['freeze'])));
            $("#available_bal").text(U.formatcoin(U.rtrim_zero(result['account']['available'])));
            /*  if (result['coin']['alert_msg'].length > 0) {
                  $("#symbol_tips").text(result['symbol_info']['alert_msg']);
                  $(".withdraw_forbid").removeClass("hide");
                  $(".withdraw_operation").addClass("hide");
                  return false;
              }*/
            var input_amt = $("input[name='amt']").attr("placeholder");
            var input_placeholder = input_amt.split("：");
            $("input[name='amt']").attr("placeholder", input_placeholder[0] + "：" + U.formatcoin(U.rtrim_zero(result['account']['available'])));
            $("input[name='amt']").val("");
            if($('#coin_code').val().trim() === 'BTC'){
                $("#fee").text(0.001)
            }else if($('#coin_code').val().trim() === 'ETH'){
                $("#fee").text(0.01)
            }else if($('#coin_code').val().trim() === 'USDT'){
                $("#fee").text(2)
            }else {
                $("#fee").text(U.formatcoin("0"));
            }
            $("#trueamt").text(U.formatcoin("0"));
            $("#withdraw_min_amt").text(U.formatcoin(U.rtrim_zero(result['coin']['withdrawMin'])));
            if (U.bccomp(result['coin']['noAuthWithdrawMax'], 0) > 0 && $("#notauth_max_withdraw_amt").length > 0) {
                $(".not_auth_withdraw_tips").removeClass("hide");
            } else {
                $(".not_auth_withdraw_tips").addClass("hide");
            }
            $("#notauth_max_withdraw_amt").text(U.formatcoin(U.rtrim_zero(result['coin']['noAuthWithdrawMax'])));
            if (U.bccomp(result['coin']['withdrawMax'], 0) == 0) {
                $("#withdraw_ac_max_amt").parents(".withdraw_ac_max_amt_tips").addClass("hide");
            } else {
                $("#withdraw_ac_max_amt").parents(".withdraw_ac_max_amt_tips").removeClass("hide");
            }
            $("#withdraw_ac_max_amt").text(U.formatcoin(U.rtrim_zero(result['coin']['withdrawMax'])));
            var html_address = "";
            $.each(result['address'], function (key, value) {
                html_address += '<li class="address_li"><a href="javascript:void(0);" selectid="' + value['id'] + '">' + value['remark'] + ' - - ' +" "+ value['coinAddress'] + '</a><span class="icon_del"></span></li>';
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
            var withdraw_decimal = Number(config_data['coin']['calculationLen']);
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
            var min_amt = Number(config_data['coin']['withdrawMin']);
            var max_amt = Number(config_data['coin']['withdrawMax']);
            var base_fee_amt = Number(config_data['coin']['withdrawBaseFee']);
            var fee_type = Number(config_data['coin']['withdrawFeeType']);
            var fee_data = Number(config_data['coin']['withdrawRate']);
            var fee_coins =config_data['coin']['coinCode'];

            var fee = 0;
            // if (fee_type == 0) {
            //     fee = base_fee_amt ;
            // }
            // if (fee_type == 1){
                if(fee_coins == 'BTC'){
                    $("#fee").text(0.001);
                    var trueamt =($("#withdraw_input").val() -0.001).toFixed(4);
                    if (trueamt < 0) {
                        trueamt = 0;
                    }
                    $("#trueamt").text(U.formatcoin(trueamt));
                    $("#withdraw_input").bind("input propertychange",function(event){
                        $("#fee").text(0.001);
                    });
                }else if(fee_coins == 'ETH'){
                    $("#fee").text(0.01);
                    var trueamt =($("#withdraw_input").val() -0.01).toFixed(4);
                    if (trueamt < 0) {
                        trueamt = 0;
                    }
                    $("#trueamt").text(U.formatcoin(trueamt));
                    $("#withdraw_input").bind("input propertychange",function(event){
                        $("#fee").text(0.01);
                    });
                }else if(fee_coins == 'USDT'){
                    $("#fee").text(2);
                    var trueamt =($("#withdraw_input").val() -2).toFixed(4);
                    if (trueamt < 0) {
                        trueamt = 0;
                    }
                    $("#trueamt").text(U.formatcoin(trueamt));
                    $("#withdraw_input").bind("input propertychange",function(event){
                        $("#fee").text(2);
                    });
                }else {
                    var fee = (fee_data*0.001*Number($("#withdraw_input").val())).toFixed(4);
                     $("#fee").text(fee)
                    var trueamt = U.bcsub(amt, fee);
                    if (trueamt < 0) {
                        trueamt = 0;
                    }
                    $("#trueamt").text(U.formatcoin(trueamt));
                    $("#withdraw_input").bind("input propertychange",function(event){
                        $("#fee").text(fee);
                    });
                }
            //
            // }


            //实际到账

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
            var min_amt = Number(config_data['coin']['withdrawMin']);
            var max_amt = Number(config_data['coin']['withdrawMax']);
            var base_fee_amt = Number(config_data['coin']['withdrawBaseFee']);
            var fee_type = Number(config_data['coin']['withdrawFeeType']);
            var fee_data = Number(config_data['coin']['withdrawRate']);
            var withdraw_decimal = Number(config_data['coin']['calculationLen']);
            var notauth_max_withdraw_amt = Number(config_data['coin']['noAuthWithdrawMax']);
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
                fee = U.bcadd(base_fee_amt, U.bcmul(fee_data, amt));
            }
            var trueamt = U.bcsub(amt, fee);
            if (trueamt < 0) {
                layer.tips(Lang['noAuthWithdrawMax'], "form#withdraw-form input[name='amt']", {
                    tips: 1,
                    time: 2000
                });
                return false;
            }
            if (U.bccomp(amt, config_data['account']['available']) > 0) {
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
                url: "/ex/member/account/withdrawOrder",
                async: true,
                data: $("form#withdraw-form").serialize(),
                dataType: "json",
                beforeSend: function () {
                    form.addClass("lock");
                },
                success: function (result) {

                    $('.gold_button').removeClass('click')
                    form.removeClass("lock");
                    if (result.success) {

                        window.location = BASE_PATH + "/ex/member/account/money_info?type=2";
                        return false;
                    } else {

                        layer.alert(result.msg, {
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
                        /*layer.closeAll();
                        layer.open({
                            type: 1,
                            title: false,
                            closeBtn: 0,
                            scrollbar: false,
                            shade: 0.8,
                            area: '560px',
                            shadeClose: false,
                            content: $('#withdraw_success_pop'),
                        });*/
                    }
                }
            });
        }


        function initDataCallback(data) {
            var page_data = data;
            $('.submitBt').attr('openGoogleAuth', data.openGoogleAuth)
            $("#page_data").val("");
            $("#cashAddressBar").data("withdraw_data", page_data);
            $("input[name='memo']").data("show_memo_symbol", page_data['memo']);

            $(function () {
                init_page_data(data);
                $(window).bind('resize', function (e) {
                    var _windowH = $(window).height();
                    changeFullScreen(_windowH);
                });

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
                    var  address_info;

                    config_data['address'].forEach(function(e,index){
                        if(e.id==address_id) address_info=e;

                    })
                    $("input[name='tag']").val(address_info['remark']);
                    $("input[name='address']").val(address_info['coinAddress']);
                    $("input[name='memo']").val(address_info['memo']);
                    $("#addNewAddressBox").show();
                });

                $(document).on("click", "#cashAddressBar .addAddressBt", function () {

                    layer.open({
                        type: 1,
                        title: '新增提现地址',
                        area: ['500px', '270px'],
                        shadeClose: true,
                        scrollbar:false ,
                        content: $("#show_div"),
                    });

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
                        url: "/exchange/withdrawAddress/remove",
                        async: true,
                        data: {id: id},
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

                    $("#withdraw_submit").click(function (){
                        var pawList = mMd5.hbmd5($("#password_trd").val());
                        $.ajax({
                            type: "POST",
                            url: "/ex/member/account/withdrawCoin",
                            dataType: 'json',
                            async: true,
                            data:{"tag":$("#reskerlist").val(),"address":$("#wqw").text(),"amt":$("#withdraw_input").val(),"symbol":data.account.coinCode,"mobileCode":$("#mobileCode").val(),"tradePwd":pawList,"memo":$("#memo_trd").val(),"googleCode":$("#googleCode").val()},
                            success: function (data) {
                                if(data.success== true){
                                    layer.msg(data.msg, {time: 2000, icon: 6});

                                    setTimeout('window.location = "/ex/member/account/withdraw_history";',1000);
                                }else {
                                    layer.msg(data.msg, {time: 2000, icon: 7});
                                }
                            }

                        })
                    })
                $("#googleCode").bind("input propertychange",function(event){
                       if($("#googleCode").val().length == 6) {
                           var pawList = mMd5.hbmd5($("#password_trd").val());
                           $.ajax({
                               type: "POST",
                               url: "/ex/member/account/withdrawCoin",
                               dataType: 'json',
                               async: true,
                               data:{"tag":$("#reskerlist").val(),"address":$("#wqw").text(),"amt":$("#withdraw_input").val(),"symbol":data.account.coinCode,"mobileCode":$("#mobileCode").val(),"tradePwd":pawList,"memo":$("#memo_trd").val(),"googleCode":$("#googleCode").val()},
                               success: function (data) {
                                   if(data.success== true){
                                       layer.msg('保存成功！', {time: 2000, icon: 6});
                                       refresh();
                                   }else {
                                       layer.msg(data.msg, {time: 2000, icon: 7});
                                   }
                               }

                           })
                       }
                });
                function refresh(){
                    window.location.reload();//刷新当前页面.

                    //或者下方刷新方法
                    //parent.location.reload()刷新父亲对象（用于框架）--需在iframe框架内使用
                    // opener.location.reload()刷新父窗口对象（用于单开窗口
                    //top.location.reload()刷新最顶端对象（用于多开窗口）
                }

                // $("form#withdraw-form .submitBt").on("click", function () {
                //
                //     var bool = withdraw_valid();
                //     var openGoogleAuth = $(this).attr('openGoogleAuth')
                //     // if(openGoogleAuth == 0) {
                //     //     window.location.href = '/ex/member/user'
                //     //     return false
                //     // }
                //     if (bool == true) {
                //         layer.open({
                //             type: 1,
                //             title: Lang['google_verify'],
                //             area: ['400px', 'auto'],
                //             shadeClose: true,
                //             content: $("#googleVerifyPop"),
                //             success: function () {
                //                 $("#googleVerifyPop .google_input").val("").focus();
                //                 var click_submit = function () {
                //                     var code = $.trim($("#googleVerifyPop .google_input").val());
                //                     if (code.length > 0) {
                //                         $("#withdraw-form input[name='valid_code']").val(code);
                //                     } else {
                //                         return false;
                //                     }
                //                     if ($("form#withdraw-form").hasClass("lock") == true) {
                //                         return false;
                //                     }
                //                     withdraw_submit();
                //                 };
                //                 /*$("#googleVerifyPop .google_input").on("keyup", function () {
                //                     if ($(this).val().length == 6) {
                //                         click_submit();
                //                     }
                //                 });*/
                //                 $(".googleVerify .gold_button").on("click", function (){
                //
                //                     if(!$(this).hasClass('click')) {
                //                         click_submit();
                //                         $(this).addClass('click')
                //                     }
                //
                //                 });
                //             }
                //         });
                //     }
                // });
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

            $("input[name='amt']").bind('input propertychange', function() {
                calculate();
            });
            $("input[name='amt']").bind('input oninput', function() {
                calculate();
            });

        }

        // 加载提币币种数据
        core.list({url: BASE_PATH + "/ex/member/account/findWithdrawData/" + $("#coin_code").val()}).then(function (data) {
             // $(".goldlist").text(data.account.totalMoney);
            tpl.render({"tplId": "#coin_tpl", "renderId": "#coin_render", "data": data});
            tpl.render({"tplId": "#coin_address_tpl", "renderId": "#coin_address_render", "data": data});
            if(data.openGoogleAuth === 1) {
                $("#google_math").css("display", "block");
            }
            //当前提币为USDT的时候有特殊提示
            $('#coin_code').val().trim() === 'USDT' ? $('#red_green').show() : $('#red_green').hide();
            initDataCallback(data);
            $("#buttoniop").on("click", function () {
                $.ajax({
                    type: "POST",
                    url: "/exchange/withdrawAddress/save",
                    contentType: 'application/json; charset=UTF-8',
                    async: false,
                    data: JSON.stringify({"coinAddress": $("#addresslist").val(),"coinCode":data.account.coinCode,"remark":$("#reskerlist").val()}),
                    dataType: "json",
                    success: function (data) {
                        if(data.success == true){
                            if($("#addresslist").val().length== 0 || $("#reskerlist").val().length==0 ){
                                layer.msg('新增地址或标签不能为空！', {time: 2000, icon: 7});
                            }else {
                                refresh()
                                layer.msg('新增成功！', {time: 2000, icon: 6});
                                layer.closeAll('page');
                            }
                        }else {
                            layer.msg(data.msg,{time: 2000, icon: 7});
                        }
                    }
                })
            })
            function refresh(){
                window.location.reload();//刷新当前页面.
                //或者下方刷新方法
            }
            $("#wqw").on("click", function () {
                $.ajax({
                    type: "GET",
                    url: "/exchange/withdrawAddress/listAll",
                    dataType: "json",
                    success: function (result) {

                    }
                })
            })

        })

        // 加载币种数据
        core.list({url: BASE_PATH + "/exchange/coins?withdraw=true"}).then(function (data) {
            tpl.render({"tplId": "#coin_select_tpl", "renderId": "#coin_select_render", "data": data});
        })
        // $("#withdraw_submit").click(function(){
        //     $.ajax({
        //         type: "POST",
        //         url: "/exchange/account/withdrawCoin",
        //         dataType: 'json',
        //         async: true,
        //         data:{"tag":$('#reskerlist').val(),"address":$("#wqw").val(),"amt":$("#withdraw_input").val(),"symbol":data.account.coinCode,"valid_code":$("#mobileCode").val(),"tradePwd":$("#password_trd").val(),"memo":$("#memo_trd").val},
        //         success: function (data) {
        //             alert("ooo")
        //         }
        //     })
        // })
    }

    /**
     * 充币
     */
    function deposit() {

        jQuery.divselect = function (divselectid, inputselectid) {
            $(divselectid + " cite").on("click", function () {
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
            $(divselectid + " ul li a").click(function () {
                var txt = $(this).html();
                $(this).parents(divselectid).find("cite").html(txt);
                var value = $(this).attr("selectid");
                $(this).parents(divselectid).find(inputselectid).val(value);
                $(divselectid + " ul").hide();
            });
            $('body').click(function (e) {
                if ($(e.target).parents(divselectid).length == 0) {
                    if ($(divselectid + " ul").is(':visible')) {
                        $(divselectid + " ul").hide();
                    }
                }
            })
        };
        $(function () {
            /*var page_data = $("#page_data").val();
            $("#page_data").val("");
            $("#divSelect").data("symbol_data", $.parseJSON(page_data));*/
            $.divselect(".divSelect", ".selectInput");


            $(window).bind('resize', function (e) {
                var _windowH = $(window).height();
                changeFullScreen(_windowH);
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
        });

        function initPage() {

            $('.chargeBt.getAddress').on('click', function () {

                var code=$(this).attr("data-code")

                // 加载充值币种数据
                core.get({url: BASE_PATH + "/ex/member/account/findAddress/" + code}).then(function (data) {
                    if(data.address!=null){
                        $("#chargeAddress").html(data.address);
                        $('.chargeBt.getAddress').hide();
                    }
                })

            })

        }

        function initQRcode() {
            $('.chargeBt.QRcode').on('click', function () {
                var dataId = $(this).attr("dataId");
                var content = $("#" + dataId).text();
                var symbol = $("#symbol").val();
                var title = "";
                if (dataId == 'chargeAddress') {
                    title = symbol + " " + Lang['deposit_address'];
                } else {
                    title = symbol + " " + Lang['deposit_remark'];
                }
                layer.open({
                    type: 1,
                    title: title,
                    area: ['400px', 'auto'],
                    shadeClose: true,
                    content: '<div class="popQRcodPic">' +
                        '<div id="popQRcodeBox" style="width:200px;hegiht:200px;margin:0 auto;"></div>' +
                        '</div>' +
                        '<div class="popAddress">' + content + '</div>',
                    success: function () {
                        $("#popQRcodeBox").qrcode({
                            render: "canvas",
                            width: 200,
                            height: 200,
                            text: U.toUtf8(content),
                        });
                    }
                });
            });
            var clipboard = new Clipboard(".copy[dataId='chargeAddress']", {
                text: function () {
                    return $("#chargeAddress").text();
                }
            });
            clipboard.on('success', function () {
                layer.msg(Lang['copy_clipboard_success']);
            });
            clipboard.on('error', function () {
                layer.msg(Lang['copy_clipboard_fail']);
            });
            var clipboard2 = new Clipboard(".copy[dataId='memo']", {
                text: function () {
                    return $("#memo").text();
                }
            });
            clipboard2.on('success', function () {
                layer.msg(Lang['copy_clipboard_success']);
            });
            clipboard2.on('error', function () {
                layer.msg(Lang['copy_clipboard_fail']);
            });
        }

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

        function init_page_data(result) {
            var symbol = $("#symbol").val();
            var config_data = $("#divSelect").data("symbol_data");
            var symbol_info = config_data['symbol'][symbol];
            if (symbol_info['alert_msg'].length > 0) {
                $(".deposit_symbol_tips").removeClass("hide").find(".symbol_tips").text(symbol_info['alert_msg']);
                $(".deposit_memo_tips").addClass("hide");
                $(".chargeBt").parents(".tr").addClass("disabled");
                $(".chargeAddress_box").addClass("hide");
            } else {
                $(".deposit_symbol_tips").addClass("hide").find(".symbol_tips").text(symbol_info['alert_msg'])
                $(".chargeAddress_box").removeClass("hide");
                $(".chargeBt").parents(".tr").removeClass("disabled");
                if (symbol_info['deposit_memo_is_required'] == 1) {
                    $(".deposit_memo_tips").removeClass("hide").find(".memo_tips").text(symbol_info['memo_tips']);
                } else {
                    $(".deposit_memo_tips").addClass("hide").find(".memo_tips").text(symbol_info['memo_tips']);
                }
            }
            $(".unit").text(coin['symbol']);
            $("#bal").text(result['acc_info']['bal']);
            $("#frozen_bal").text(result['acc_info']['frozen_bal']);
            $("#available_bal").text(result['acc_info']['available_bal']);
            $("#chargeAddress").text(result['address']['address']);
            $("#memo").text(result['address']['memo']);
            if (symbol_info['alert_msg'].length == 0) {
                if (symbol_info['deposit_memo_is_required'] == 0) {
                    $(".chargeTip_box").addClass("hide");
                } else {
                    $(".chargeTip_box").removeClass("hide");
                }
            }
            if (symbol_info['min_deposit_tips'].length > 0) {
                $("#min_deposit_tips").html(symbol_info['min_deposit_tips']).show();
            } else {
                $("#min_deposit_tips").hide();
            }
            $("#deposit_notice").text(result['address']['deposit_notice']);
            $("#deposit_confirm_tips").html(symbol_info['deposit_confirm_tips']);
            var trade_html = "";
            $.each(symbol_info['pairs'], function (key, value) {
                trade_html += '<li><a href="/trade?symbol=' + symbol_info['symbol'] + '_' + value + '">' + symbol_info['symbol'] + '/' + value + '</a></li>';
            });
            if (trade_html.length > 0) {
                $(".conTrade").removeClass("hide").find("ul").html(trade_html);
            } else {
                $(".conTrade").addClass("hide").find("ul").html(trade_html);
            }
            if (symbol_info['introduce_url'].length > 0) {
                $(".conIntro").removeClass("hide").find("a").attr("url", symbol_info['introduce_url']);
            } else {
                $(".conIntro").addClass("hide").find("a").attr("url", symbol_info['introduce_url']);
            }
        }

        // 加载充值币种数据
        core.list({url: BASE_PATH + "/ex/member/account/findCoinAccount/" + $("#coin_code").val()}).then(function (data) {
            tpl.render({"tplId": "#coin_tpl", "renderId": "#coin_render", "data": data});
            tpl.render({"tplId": "#coin_address_tpl", "renderId": "#coin_address_render", "data": data});
            initQRcode();
            initPage();
            //当前提币为USDT的时候有特殊提示
            $('#coin_code').val().trim() === 'USDT' ? $('#sa_kop').show() : $('#sa_kop').hide();
        })

        // 加载币种数据
        core.list({url: BASE_PATH + "/exchange/coins?recharge=true"}).then(function (data) {
            tpl.render({"tplId": "#coin_select_tpl", "renderId": "#coin_select_render", "data": data});
        })


    }
    function c2c() {
        //商户卖出记录
        core.list({url: BASE_PATH + "/c2c/findProducts"}).then(function (data) {
            tpl.render({"tplId": "#c2c_product_tpl", "renderId": "#c2c_product_render", "data": data.data});
            if(data.data.length>0){
                var product = data.data[0];
                tpl.render({"tplId": "#c2c_productInfo_tpl", "renderId": "#c2c_productInfo_render", "data": product});
            }
        }).then(function (data) {
            var coinCode = $(".t_menu li.cur").attr("type");
            renderByCoinCode(coinCode);
            changeTabs();
            handleInputChange();

        })

    }

    function handleInputChange(){
        $( ".buy_numberClass" ).keyup(function() {
            try{
                var eachPrice = parseFloat($( "#buy_price" ).val());
                var num = parseFloat(this.value);
                var total = eachPrice * num;
                total = total.toFixed(2);
                $("#buy_total_price").text(total);
            }catch(e){}
        });

        $( ".sell_numberClass" ).keyup(function() {
            try{
                var eachPrice = parseFloat($( "#sell_price" ).val());
                var num = parseFloat(this.value);
                var total = eachPrice * num;
                total = total.toFixed(2);
                $("#sell_total_price").text(total);
            }catch(e){}
        });
    }

    function changeTabs() {
        var obj = $(".t_menu");
        $('.tabs').each(function () {
            var it = $(this);
            var menus = $('.t_menu li', it);
            var conts = $('.t_cont', it);
            menus.on('click', function () {
                if (obj.hasClass("lock") == true) {
                    return false;
                }
                var _this = $(this);
                var _index = _this.index();
                _this.addClass('cur').siblings().removeClass('cur');
                // conts.hide(0).eq(_index).stop().fadeIn();
                var type = $(".t_menu li.cur").attr("type");

                core.list({url: BASE_PATH + "/c2c/findProduct?coinCode="+type}).then(function (data0) {
                    tpl.render({"tplId": "#c2c_productInfo_tpl", "renderId": "#c2c_productInfo_render", "data": data0.data});
                });

                renderByCoinCode(type);
            });
            //买单
            $(document).on("click", "#buy-submit", function () {
                // $("#buy_form").submit();
                var coinCode = $(".t_menu li.cur").attr("type");
                var number = $("#buy_vol").val();
                $.ajax({
                    type: "POST",
                    url: BASE_PATH+"/c2c/transactionC2c",
                    async: true,
                    data: {"coinCode": coinCode,"type":"buy","number":number},
                    dataType: "json",
                    beforeSend: function () {
                        $("#buy-submit").addClass("lock");
                    },
                    success: function (result) {

                        $("#buy-submit").removeClass("lock");
                        if (result.success) {
                            tpl.render({"tplId": "#c2c_pay_tpl", "renderId": "#c2c_pay_render", "data": result.data});
                            layer.open({
                                type: 1,
                                // title: Lang['deposit_remark'],
                                area: ['500px', 'auto'],
                                shadeClose: true,
                                content: $("#c2c-order-info"),
                                success: function () {
                                    $(".c2c-order-info .gold_button").on("click", function () {
                                        window.location = BASE_PATH + "/c2c";
                                    });
                                }
                            });
                        }
                        U.response(result);
                    }
                });
            });
            //卖单
            $(document).on("click", "#sell-submit", function () {
                // $("#sell_form").submit();
                var coinCode = $(".t_menu li.cur").attr("type");
                var number = $("#sell_vol").val();
                $.ajax({
                    type: "POST",
                    url: BASE_PATH+"/c2c/transactionC2c",
                    async: true,
                    data: {"coinCode": coinCode,"type":"sell","number":number},
                    dataType: "json",
                    beforeSend: function () {
                        $("#buy-submit").addClass("lock");
                    },
                    success: function (result) {
                        $("#buy-submit").removeClass("lock");
                        U.response(result);
                        if(result.success){
                            // window.location = BASE_PATH + "/c2c";
                            $(".sell_numberClass").val(0);
                        }
                    }
                });
            });
        });
    }
    function renderByCoinCode(coin){

        //商户卖出记录
        core.list({url: BASE_PATH + "/c2c/findC2cOrders?coinCode="+coin+"&type=1"}).then(function (data) {
            tpl.render({"tplId": "#c2c_sell_tpl", "renderId": "#c2c_sell_render", "data": data.data});
        });
        //商户买入
        core.list({url: BASE_PATH + "/c2c/findC2cOrders?coinCode="+coin+"&type=2"}).then(function (data) {
            tpl.render({"tplId": "#c2c_buy_tpl", "renderId": "#c2c_buy_render", "data": data.data});
        });
        //用户买卖信息
        core.list({url: BASE_PATH + "/c2c/findUserOrders?coinCode="+coin+"&type=2"}).then(function (data) {

            tpl.render({"tplId": "#c2c_userOrder_tpl", "renderId": "#c2c_userOrder_render", "data": data.data});
        }).then(function () {
            var orders = $("#c2c_userOrder_render");

            $('#c2c_userOrder_render tr').each(function(i){ // 遍历 tr
                $(this).children('td').each(function(j) {  // 遍历 tr 的各个 td
                    if(j==6){
                        $(this).on('click', function () {

                            var id = $(this)[0].id;
                            $.ajax({
                                type: "POST",
                                url: BASE_PATH+"/c2c/findC2cUserVoById",
                                async: true,
                                data: {"id": id},
                                dataType: "json",
                                success: function (result) {

                                    if(result.success){
                                        if (result.success) {
                                            tpl.render({"tplId": "#c2c_pay_tpl", "renderId": "#c2c_pay_render", "data": result.data});
                                            layer.open({
                                                type: 1,
                                                // title: Lang['deposit_remark'],
                                                area: ['500px', 'auto'],
                                                shadeClose: true,
                                                content: $("#c2c-order-info"),
                                                success: function () {
                                                    $(".c2c-order-info .gold_button").on("click", function () {
                                                        window.location = BASE_PATH + "/c2c";
                                                    });
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        });
                    }
                })
            });
        });
    }

    function moneyInfo() {
        var body_date ="";
        var pageSize =10;
        $(function () {
            var date_lang = 'cn';
            if ($("#languageBt").hasClass("cn") == false) {
                date_lang = 'en';
            }
            laydate.render({
                elem: "#test19",
                lang: date_lang,
                max: new Date().format("yyyy-MM-dd"),
                done: function (value) {
                    body_date = value;
                }
            });
            $(window).bind('resize', function (e) {
                var _windowH = $(window).height();
                changeFullScreen(_windowH);
            });
            $("*[timestamp]").each(function () {
                var timestamp = $(this).attr('timestamp');
                $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
            });
            var obj = $(".t_menu");
            // 加载充值取现数据
            obj.addClass("lock");
            // $.ajax({
            //     type: "get",
            //     url: BASE_PATH+"/ex/member/account/rechargeInfo",
            //     async: true,
            //     data: {"page":1,"pageSize":10},
            //     dataType: "json",
            //     success: function (data) {
            //         obj.removeClass("lock");
            //             if (data.success) {
            //                 tpl.render({"tplId": "#marketsTable_tpl", "renderId": "#marketsTable_render", "data": data.data.rows});
            //
            //             }
            //     }
            // });
            // core.list({url:  '/ex/member/account/rechargeInfo'},{data:{"page":1,"pageSize":10}}).then(function (data) {
            //     console.log(data,"sss")
            //     obj.removeClass("lock");
            //     tpl.render({"tplId": "#marketsTable_tpl", "renderId": "#marketsTable_render", "data": data.data.rows});
            //
            //
            // });
            // P.page(1,pageSize,'/ex/member/account/entrustList','entrust_tpl','entrust_render',cancel);
            P.pagelist(1,pageSize, '/ex/member/account/rechargeInfo','marketsTable_tpl','marketsTable_render');
            // $(".marketsTableBox").show();

            //
            // $('.tabs').each(function () {
            //     var it = $(this);
            //     var menus = $('.t_menu li', it);
            //     var conts = $('.t_cont', it);
            //     menus.on('click', function () {
            //         // var obj = $(".t_menu");
            //         if (obj.hasClass("lock") == true) {
            //             return false;
            //         }
            //         var _this = $(this);
            //         var _index = _this.index();
            //         _this.addClass('cur').siblings().removeClass('cur');
            //         // conts.hide(0).eq(_index).stop().fadeIn();
            //         var type = $(".t_menu li.cur").attr("type");
            //         var config_type = {"1": "/ex/member/account/rechargeInfo", "2": "/ex/member/account/withdrawInfo","3": "/ex/member/account/tradeMiningRecord"};
            //         // 加载充值取现数据
            //         core.list({url: BASE_PATH + config_type[type]},{data:{"page":1,"pageSize":10}}).then(function (data) {
            //             console.log()
            //             obj.removeClass("lock");
            //             if(type == 1){
            //                 tpl.render({
            //                     "tplId": "#marketsTable_tpl",
            //                     "renderId": "#marketsTable_render",
            //                     "data": data.data.rows
            //                 });
            //                 // $("#tablelist").css("display","none");
            //                 // $("#tablemoney").css("display","block");
            //                 $(".HDA").css("display","none");
            //                 $(".fre").css("display","none");
            //
            //             }else if(type == 2){
            //                 obj.removeClass("lock");
            //                 tpl.render({
            //                     "tplId": "#marketsTable_tpl",
            //                     "renderId": "#marketsTable_render",
            //                     "data": data.data.rows
            //                 });
            //                 $(".fre").css("display","none");
            //                 // $("#tablelist").css("display","none");
            //                 // $("#tablemoney").css("display","block");
            //                 $(".HDA").css("display","block");
            //
            //
            //             }else {
            //                 tpl.render({
            //                     "tplId": "#marketsTable_tpl",
            //                     "renderId": "#marketsTable_render",
            //                     "data": data.data.rows
            //                 });
            //                 console.log( data.data.rows)
            //                 // $("#tablelist").css("display","block");
            //                 // $("#tablemoney").css("display","none");
            //                 obj.removeClass("lock");
            //                 $(".HDA").css("display","none");
            //                 $(".fre").css("display","block");
            //                 $(".frea").css("display","none");
            //
            //                 // $.ajax({
            //                 //     type: "get",
            //                 //     url: "/ex/member/account/tradeMiningRecord",
            //                 //     dataType: 'json',
            //                 //     async: true,
            //                 //     data:{"page":1},
            //                 //     success: function (data) {
            //                 //         console.log(data.rows)
            //                 //         var datalist = data.rows;
            //                 //         // var strlist = '';
            //                 //         // for (var i=0;i<datalist.length;i++) {
            //                 //         //     var tmap = datalist[i];
            //                 //         //     strlist+='<td style=" text-align: center;"> </td>';
            //                 //         //     strlist+='<td style=" text-align: center;" id="nat">'+tmap.tradeCoinCode+'_'+tmap.tradeCoinRealTimePrice+'</td>';
            //                 //         //     strlist+='<td style=" text-align: center;"  id="nat_1">'+tmap.money+'</td>';
            //                 //         //     strlist+='<td style=" text-align: center;" id="nat_2">'+tmap.coinCode+'</td>';
            //                 //         //     strlist+='<td style=" text-align: center;" id="nat_3">'+tmap.createTime+'</td>';
            //                 //         //     strlist+='<td style=" text-align: center;" id="nat_4">'+tmap.remark+'</td>';
            //                 //         // }
            //                 //         // $("#marketsTable_render1 #template").html(strlist)
            //                 //         $.each(datalist, function (i, n) {
            //                 //             var row = $("#template").clone();
            //                 //             row.find("#nat").text(n.tradeCoinCode + "_" + n.tradeCoinRealTimePrice);
            //                 //             row.find("#nat_1").text(n.money);
            //                 //             row.find("#nat_2").text(n.coinCode);
            //                 //             row.find("#nat_3").text(n.createTime);
            //                 //             row.find("#nat_4").text(n.remark);
            //                 //             row.appendTo("#tablelist");//添加到模板的容器中
            //                 //
            //                 //         });
            //                 //         // $("#tablelist").empty();
            //                 //
            //                 //
            //                 //     }
            //                 // })
            //
            //             }
            //         });
            //
            //     });
            //     $(document).on("click", ".fold .arrBt", function () {
            //         $(this).parents("tr").find(".fold_detail,.fold").toggleClass("open");
            //     });
            //     $(document).on("click", ".page a", function (e) {
            //         stopDefault(e);
            //         var obj = $(".t_menu");
            //         if (obj.hasClass("lock") == true) {
            //             return false;
            //         }
            //         var url = $(this).attr("href");
            //         $.ajax({
            //             type: "GET", url: url, async: true, dataType: "html", beforeSend: function () {
            //                 obj.addClass("lock");
            //             }, success: function (html) {
            //                 obj.removeClass("lock");
            //                 if (isJson(html) == true) {
            //                     var result = $.parseJSON(html);
            //                     if (result['code'] > 0) {
            //                         layer.alert(result['msg'], {
            //                             title: Lang['alert_title'],
            //                             btn: Lang['confirm_button'],
            //                             closeBtn: 0,
            //                         }, function (index) {
            //                             layer.close(index);
            //                             if (result['url'] != undefined) {
            //                                 window.location = result['url'];
            //                             }
            //                         });
            //                         return false;
            //                     }
            //                 }
            //                 var obj_html = $(html);
            //                 obj_html.find("*[timestamp]").each(function () {
            //                     var timestamp = $(this).attr('timestamp');
            //                     $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
            //                 });
            //                 $(".marketsTableBox").empty().html(obj_html);
            //             }
            //         });
            //     });
            // });

            function changeFullScreen(_windowH) {
                if ($(document.body).height() < _windowH) {
                    var _fullScreenH = _windowH - $(".fullScreen_h").offset().top - $(".footerNavBar").height() - $(".footerBar").height() - 15 - 30;
                    $(".fullScreen_h").css("min-height", _fullScreenH + "px");
                }
            }

            function cancel_withdraw(obj, txno) {
                if ($(obj).hasClass("disabled") == true) {
                    return false;
                }
                if ($(obj).hasClass("lock") == true) {
                    return false;
                }
                layer.confirm(Lang['cancel_confirm'], {
                    icon: 3,
                    title: Lang['confirm_title'],
                    btn: [Lang['confirm_button'], Lang['cancel_button']],
                    closeBtn: 0,
                }, function (index) {
                    layer.close(index);
                    $.ajax({
                        type: "POST",
                        url: '/withdraw/cancel',
                        async: true,
                        data: {txno: txno, _token: $("meta[name='csrf-token']").attr("content")},
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
                                refresh_list();
                            }
                        }
                    });
                });
            }

            function resend_email(obj, txno) {
                if ($(obj).hasClass("disabled") == true) {
                    return false;
                }
                if ($(obj).hasClass("lock") == true) {
                    return false;
                }
                $.ajax({
                    type: "POST",
                    url: '/withdraw/resend_email',
                    async: true,
                    data: {txno: txno, _token: $("meta[name='csrf-token']").attr("content")},
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
                            layer.msg(Lang['send_email_success']);
                        }
                    }
                });
            }

            function refresh_list() {
                if ($(".page .active").length == 0) {
                    $(".t_menu li.cur").click();
                    return false;
                }
                var p = $(".page .active").text();
                var url = $(".page a:first").attr("href");
                url = url.replace(/\?p=(\d)/, '?p=' + p);
                url = url.replace(/&p=(\d)/, '&p=' + p);
                $.ajax({
                    type: "GET", url: url, async: true, dataType: "html", success: function (html) {
                        if (isJson(html) == true) {
                            var result = $.parseJSON(html);
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
                            }
                        }
                        $(".marketsTableBox").empty().html(html);
                    }
                });
            }
        })
    }

    /**
     * 委托管理
     */
    function entrust() {
        var pageSize=10;

        //回调循环 撤销方法
        function cancel(){
            $('#entrust_render tr').each(function(i){ // 遍历 tr
                $(this).find("td").find("a").on('click', function () {
                    var id = $(this)[0].id;
                    var coinPair = $(this).parent().parent().find("td").eq(2).text();  //交易对
                    $.ajax({
                        type: "POST",
                        url: BASE_PATH+"/exchange/trade/cancel",
                        async: true,
                        data: {"orderId": id,"coinPair":coinPair},
                        dataType: "json",
                        success: function (result) {
                            //设置延时
                            U.sleep(800);
                            U.response(result);
                            //加载当前委托数据
                            P.page(1,pageSize,'/ex/member/account/entrustList','entrust_tpl','entrust_render',cancel);
                        }
                    });
                });
            });
        }
        $(function () {
            //澶у睆鏃堕珮搴﹀叏灞�
            //var _windowH = $(window).height();
            //changeFullScreen(_windowH);
            $(window).bind('resize', function (e) {
                var _windowH = $(window).height();
                changeFullScreen(_windowH);
            });
            $("*[timestamp]").each(function () {
                var timestamp = $(this).attr('timestamp');
                $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
            });
            $(".marketsTableBox").show();

            //加载当前委托数据
            P.page(1,pageSize,'/ex/member/account/entrustList','entrust_tpl','entrust_render',cancel);
            // initData(1,10);
            //鍙栨秷褰撳墠鐨勫鎵橈細娴眰灞曠ず
            $(document).on("click", ".revokeAll", function () {
                if ($("#entrust_render .no-data-tr").length == 0) {
                    return false;
                }
                layer.open({
                    id: "revokeAll",
                    type: 1,
                    title: false,
                    shadeClose: true,
                    closeBtn: 0,
                    scrollbar: false,
                    shade: 0.8,
                    area: '560px',
                    shadeClose: false,
                    content: $('#popBox')
                });
            });
            $(document).on("click", "#popBox .bt1,#popBox .closePopBt", function () {
                layer.close(layer.index);
            });
            $(document).on("click", "#popBox .bt2", function () {
                layer.close(layer.index);
                cancel_order(this, '0');
            });

            //涓嬫媺鍒楄〃
            $.divselect("#base_asset_box", ".selectInput");
            $.divselect("#quote_asset_box", ".selectInput");
            $.divselect("#side_box", ".selectInput");
            //琛ㄥ崟鏌ヨ
            $(document).on("click", "#search_form .searchBt", function () {
                var obj = $(this);
                if (obj.hasClass("lock") == true) {
                    return false;
                }
                var url = $("#search_form").attr("action");
                var form_data = $("#search_form").serializeJson();
                if (U.is_null(form_data.start_date) == false && form_data.start_date.length > 0) {
                    form_data.start_time = (new Date(form_data.start_date.replace(/-/g, "/") + " 00:00:00").getTime()) / 1000;
                }
                if (U.is_null(form_data.end_date) == false && form_data.end_date.length > 0) {
                    form_data.end_time = (new Date(form_data.end_date.replace(/-/g, "/") + " 23:59:59").getTime()) / 1000;
                }
                delete form_data.start_date;
                delete form_data.end_date;

                //return false;
                $.ajax({
                    type: "GET",
                    url: url,
                    async: true,
                    data: form_data,
                    dataType: "html",
                    beforeSend: function () {
                        obj.addClass("lock");
                    },
                    success: function (html) {
                        obj.removeClass("lock");
                        if (isJson(html) == true) {
                            var result = $.parseJSON(html);
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
                            }
                        }
                        var obj_html = $(html);
                        obj_html.find("*[timestamp]").each(function () {
                            var timestamp = $(this).attr('timestamp');
                            $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
                        });
                        $(".marketsTableBox").empty().html(obj_html);
                    }
                });
            });
            $(document).on("click", "#search_form .trigger_submit", function () {
                $("#search_form .searchBt").click();
            });
            //鍘嗗彶濮旀墭锛屽巻鍙茶鍗曢噸缃�
            $(document).on("click", "#search_form .resetBt", function () {

                $(".inputIcoCalendar").val("");
                $("#start_date").val(today);
                if ($("#base_asset_box").length > 0) {
                    var quote_asset = $("#quote_asset_box").find("a[selectid='']").text();
                    $("#base_asset_box").find("cite input").val("");
                    $("#quote_asset_box").find("cite").text(quote_asset);
                    $("#quote_asset_box").find(".selectInput").val("");
                }
                if ($("#side_box").length > 0) {
                    var side = $("#side_box").find("a[selectid='0']").text();
                    $("#side_box").find("cite").text(side);
                    $("#side_box").find(".selectInput").val("0");
                    $("input.trigger_submit").prop("checked", false);
                }
                // $("#search_form .searchBt").click();
            });
        });

//澶у睆鏃堕珮搴﹀叏灞�
        function changeFullScreen(_windowH) {
            if ($(document.body).height() < _windowH) {
                var _fullScreenH = _windowH - $(".fullScreen_h").offset().top - $(".footerNavBar").height() - $(".footerBar").height() - 15 - 30;
                $(".fullScreen_h").css("min-height", _fullScreenH + "px");
            }
        }

        //全部撤销订单
        function cancel_order(obj, order_id) {
            if ($(obj).hasClass("lock") == true) {
                return false;
            }
            $.ajax({
                type: "POST",
                url: '/exchange/trade/cancelAll',
                async: true,
                beforeSend: function () {
                    $(obj).addClass("lock");
                },
                success: function (result) {
                    $(obj).removeClass("lock");
                    U.response(result);
                    //加载当前委托数据
                    P.page(1,pageSize,'/ex/member/account/entrustList','entrust_tpl','entrust_render');
                    /* if (result.success) {
                     } else {
                     }*/
                }
            });
        }

//鍒锋柊鍒楄〃椤甸潰
        function refresh_list() {
            if ($(".page .active").length == 0) {
                window.location.reload(true);
                return false;
            }
            var p = $(".page .active").text();
            var url = $(".page a:first").attr("href");
            url = url.replace(/\?p=(\d)/, '?p=' + p);
            url = url.replace(/&p=(\d)/, '&p=' + p);
            $.ajax({
                type: "GET",
                url: url,
                async: true,
                dataType: "html",
                success: function (html) {
                    if (isJson(html) == true) {
                        var result = $.parseJSON(html);
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
                        }
                    }
                    $(".marketsTableBox").empty().html(html);
                }
            });
        }

//涓嬫媺鍒楄〃
        jQuery.divselect = function (divselectid, inputselectid) {
            $(divselectid + " cite").on("click", function () {
                var ul = $(this).parent(divselectid).find("ul");
                if (ul.css("display") == "none") {
                    ul.show();
                } else {
                    ul.hide();
                }
            });

            //鎼滅储: obj鏄緭鍏ユ鐨勫璞�
            var select_search = function (obj) {
                var ul = $(obj).parents(divselectid).find("ul");
                ul.show();
                var txt = $.trim($(obj).val().toUpperCase());
                if (txt.length > 0) {
                    ul.find("li").hide().each(function () {
                        var keyword = $(this).text().toUpperCase();
                        if (keyword.indexOf(txt) >= 0) {
                            $(this).show();
                        }
                    });
                } else {
                    ul.find("li").show();
                }
            };

            //鐩戞帶杈撳叆妗�
            $(divselectid + " cite input").on('compositionstart', function () {
                $(this).prop('comStart', true);
            }).on('compositionend', function () {
                $(this).prop('comStart', false);
            }).on("input propertychange", function () {
                // 涓枃杈撳叆杩囩▼涓笉鎴柇
                if ($(this).prop('comStart') == true) {
                    return false;
                }
                select_search(this);
            }).keyup(function (event) {
                if (!event) {
                    event = window.event;
                }
                if (event.keyCode == 13 || event.keyCode == 32 || event.keyCode == 16) {
                    select_search(this);
                }
            });

            $(divselectid + " ul li a").click(function () {
                var txt = $(this).html();
                if ($(this).parents(divselectid).find("cite input").length == 0) {
                    $(this).parents(divselectid).find("cite").html(txt);
                }
                var value = $(this).attr("selectid");
                $(this).parents(divselectid).find(inputselectid).val(value);
                $(divselectid + " ul").hide();
                $(divselectid + " ul li").show();
            });
            $('body').click(function (e) {
                if ($(e.target).parents(divselectid).length == 0) {
                    if ($(divselectid + " ul").is(':visible')) {
                        $(divselectid + " ul").hide();
                        $(divselectid + " ul li").show();
                    }
                }
            });
        };

//鏌ョ湅璇︽儏
        function open_trade(obj, order_id) {
            var tr = $(obj).parents(".marketsTable tr");
            var tr_open = tr.next();
            //濡傛灉鏄睍寮€鐘舵€侊紝灏辨敹璧�
            if (tr_open.css("display") != "none") {
                tr_open.hide();
                tr.removeClass("openNext_tr");
                return false;
            }
            //濡傛灉鏈夋暟鎹紝鐩存帴灞曠ず
            if (tr_open.text().length > 0) {
                tr_open.css("display", "table-row");
                tr.addClass("openNext_tr");
                return false;
            }
            if ($(obj).hasClass("lock") == true) {
                return false;
            }
            //濡傛灉娌℃湁鏁版嵁锛岃幏鍙栨暟鎹�
            $.ajax({
                type: "GET",
                url: "/entrust/entrust_info",
                async: true,
                data: {order_id: order_id},
                dataType: "html",
                beforeSend: function () {
                    $(obj).addClass("lock");
                },
                success: function (html) {
                    $(obj).removeClass("lock");
                    if (isJson(html) == true) {
                        var result = $.parseJSON(html);
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
                        }
                        return false;
                    }
                    tr_open.html(html);
                    tr_open.css("display", "table-row");
                    tr.addClass("openNext_tr");
                }
            });
        }
    }

    /**
     * 币种查询
     */
    function searchCoins() {
        //所有币种查询
        core.list({url: BASE_PATH + '/exchange/coins'}).then(function (data) {
            tpl.render({"tplId": "#base_asset_tpl", "renderId": "#base_asset_render", "data": data});

            $.divselect("#base_asset_box", ".selectInput");
        });
        //定价币查询
        core.list({url: BASE_PATH + '/exchange/price/coins'}).then(function (data) {
            tpl.render({"tplId": "#base_asset_price_tpl", "renderId": "#base_asset_price_render", "data": data});

            $.divselect("#quote_asset_box", ".selectInput");
        });
    }

    /**
     * 历史委托
     */
    function historyEntrust() {
        var pageSize=10;
        var today = (new Date()).format("yyyy-MM-dd");
        $("#start_date").val(today);
        $(function () {
            //澶у睆鏃堕珮搴﹀叏灞�
            //var _windowH = $(window).height();
            //changeFullScreen(_windowH);
            $(window).bind('resize', function (e) {
                var _windowH = $(window).height();
                changeFullScreen(_windowH);
            });
            $("*[timestamp]").each(function () {
                var timestamp = $(this).attr('timestamp');
                $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
            });
            $(".marketsTableBox").show();
            var formDate0 = $("#search_form").serializeJson();
            P.page(1,pageSize,'/ex/member/account/history_entrustList','entrust_history_tpl','entrust_history_render','',formDate0);
            searchCoins();




            //查询
            $(document).on("click", ".white", function () {
                var formDate = $("#search_form").serializeJson();
                P.page(1,pageSize,'/ex/member/account/history_entrustList','entrust_history_tpl','entrust_history_render','',formDate);
            });
            //隐藏已撤销
            $(document).on("click", ".trigger_submit", function () {
                var formDate = $("#search_form").serializeJson();
                P.page(1,pageSize,'/ex/member/account/history_entrustList','entrust_history_tpl','entrust_history_render','',formDate);
            });
            //鍙栨秷褰撳墠鐨勫鎵橈細娴眰灞曠ず
            $(document).on("click", ".revokeAll", function () {
                if ($(".marketsTable tr:not(.theader):not(.no-data-tr)").length == 0) {
                    return false;
                }
                layer.open({
                    id: "revokeAll",
                    type: 1,
                    title: false,
                    shadeClose: true,
                    closeBtn: 0,
                    scrollbar: false,
                    shade: 0.8,
                    area: '560px',
                    shadeClose: false,
                    content: $('#popBox')
                });
            });
            $(document).on("click", "#popBox .bt1,#popBox .closePopBt", function () {
                layer.close(layer.index);
            });
            $(document).on("click", "#popBox .bt2", function () {
                layer.close(layer.index);
                cancel_order(this, '0');
            });
            //缈婚〉鏄剧ず
            $(document).on("click", ".page a", function (e) {
                // stopDefault(e);
                var obj = $(".page");
                if (obj.hasClass("lock") == true) {
                    return false;
                }
                var url = $(this).attr("href");
                $.ajax({
                    type: "GET",
                    url: url,
                    async: true,
                    dataType: "html",
                    beforeSend: function () {
                        obj.addClass("lock");
                    },
                    success: function (html) {
                        obj.removeClass("lock");
                        if (isJson(html) == true) {
                            var result = $.parseJSON(html);
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
                            }
                        }
                        var obj_html = $(html);
                        obj_html.find("*[timestamp]").each(function () {
                            var timestamp = $(this).attr('timestamp');
                            $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
                        });
                        $(".marketsTableBox").empty().html(obj_html);
                    }
                });
            });
            //涓嬫媺鍒楄〃

            $.divselect("#side_box", ".selectInput");
            //琛ㄥ崟鏌ヨ
            // $(document).on("click", "#search_form .searchBt", function () {
            //     var obj = $(this);
            //     if (obj.hasClass("lock") == true) {
            //         return false;
            //     }
            //     var url = $("#search_form").attr("action");
            //     var form_data = $("#search_form").serializeJson();
            //     if (U.is_null(form_data.start_date) == false && form_data.start_date.length > 0) {
            //         form_data.start_time = (new Date(form_data.start_date.replace(/-/g, "/") + " 00:00:00").getTime()) / 1000;
            //     }
            //     // if (U.is_null(form_data.end_date) == false && form_data.end_date.length > 0) {
            //     //     form_data.end_time = (new Date(form_data.end_date.replace(/-/g, "/") + " 23:59:59").getTime()) / 1000;
            //     // }
            //     // delete form_data.start_date;
            //     // delete form_data.end_date;
            //
            //     //return false;
            //     // $.ajax({
            //     //     type: "GET",
            //     //     url: url,
            //     //     async: true,
            //     //     data: form_data,
            //     //     dataType: "html",
            //     //     beforeSend: function () {
            //     //         obj.addClass("lock");
            //     //     },
            //     //     success: function (html) {
            //     //         obj.removeClass("lock");
            //     //         if (isJson(html) == true) {
            //     //             var result = $.parseJSON(html);
            //     //             if (result['code'] > 0) {
            //     //                 layer.alert(result['msg'], {
            //     //                     title: Lang['alert_title'],
            //     //                     btn: Lang['confirm_button'],
            //     //                     closeBtn: 0,
            //     //                 }, function (index) {
            //     //                     layer.close(index);
            //     //                     if (result['url'] != undefined) {
            //     //                         window.location = result['url'];
            //     //                     }
            //     //                 });
            //     //                 return false;
            //     //             }
            //     //         }
            //     //         var obj_html = $(html);
            //     //         obj_html.find("*[timestamp]").each(function () {
            //     //             var timestamp = $(this).attr('timestamp');
            //     //             $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
            //     //         });
            //     //         $(".marketsTableBox").empty().html(obj_html);
            //     //     }
            //     // });
            // });
            $(document).on("click", "#search_form .trigger_submit", function () {
                $("#search_form .searchBt").click();
            });
            //鍘嗗彶濮旀墭锛屽巻鍙茶鍗曢噸缃�
            $(document).on("click", "#search_form .resetBt", function () {
                $(".inputIcoCalendar").val("");
                $("#start_date").val(today);
                if ($("#base_asset_box").length > 0) {
                    var quote_asset = $("#quote_asset_box").find("a[selectid='']").text();
                    $("#base_asset_box").find("cite input").val("");
                    $("#quote_asset_box").find("cite").text(quote_asset);
                    $("#quote_asset_box").find(".selectInput").val("");
                }
                if ($("#side_box").length > 0) {
                    var side = $("#side_box").find("a[selectid='0']").text();
                    $("#side_box").find("cite").text(side);
                    $("#side_box").find(".selectInput").val("0");
                    $("input.trigger_submit").prop("checked", false);
                }
                // $("#search_form .searchBt").click();
            });
        });




//澶у睆鏃堕珮搴﹀叏灞�
        function changeFullScreen(_windowH) {
            if ($(document.body).height() < _windowH) {
                var _fullScreenH = _windowH - $(".fullScreen_h").offset().top - $(".footerNavBar").height() - $(".footerBar").height() - 15 - 30;
                $(".fullScreen_h").css("min-height", _fullScreenH + "px");
            }
        }

//鍙栨秷璁㈠崟
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
                            if (order_id == '0') {
                                window.location.reload(true);
                            } else {
                                refresh_list();
                            }
                        });

                    }
                }
            });
        }

//鍒锋柊鍒楄〃椤甸潰
        function refresh_list() {
            if ($(".page .active").length == 0) {
                window.location.reload(true);
                return false;
            }
            var p = $(".page .active").text();
            var url = $(".page a:first").attr("href");
            url = url.replace(/\?p=(\d)/, '?p=' + p);
            url = url.replace(/&p=(\d)/, '&p=' + p);
            $.ajax({
                type: "GET",
                url: url,
                async: true,
                dataType: "html",
                success: function (html) {
                    if (isJson(html) == true) {
                        var result = $.parseJSON(html);
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
                        }
                    }
                    $(".marketsTableBox").empty().html(html);
                }
            });
        }

//涓嬫媺鍒楄〃
        jQuery.divselect = function (divselectid, inputselectid) {
            $(divselectid + " cite").on("click", function () {
                var ul = $(this).parent(divselectid).find("ul");
                if (ul.css("display") == "none") {
                    ul.show();
                } else {
                    ul.hide();
                }
            });

            //鎼滅储: obj鏄緭鍏ユ鐨勫璞�
            var select_search = function (obj) {
                var ul = $(obj).parents(divselectid).find("ul");
                ul.show();
                var txt = $.trim($(obj).val().toUpperCase());
                if (txt.length > 0) {
                    ul.find("li").hide().each(function () {
                        var keyword = $(this).text().toUpperCase();
                        if (keyword.indexOf(txt) >= 0) {
                            $(this).show();
                        }
                    });
                } else {
                    ul.find("li").show();
                }
            };

            //鐩戞帶杈撳叆妗�
            $(divselectid + " cite input").on('compositionstart', function () {
                $(this).prop('comStart', true);
            }).on('compositionend', function () {
                $(this).prop('comStart', false);
            }).on("input propertychange", function () {
                // 涓枃杈撳叆杩囩▼涓笉鎴柇
                if ($(this).prop('comStart') == true) {
                    return false;
                }
                select_search(this);
            }).keyup(function (event) {
                if (!event) {
                    event = window.event;
                }
                if (event.keyCode == 13 || event.keyCode == 32 || event.keyCode == 16) {
                    select_search(this);
                }
            });

            $(divselectid + " ul li a").click(function () {
                // $(divselectid + " ul").find('li').find('a').click(function () {
                var txt = $(this).html();
                if ($(this).parents(divselectid).find("cite input").length == 0) {
                    $(this).parents(divselectid).find("cite").html(txt);
                }
                var value = $(this).attr("selectid");
                $(this).parents(divselectid).find(inputselectid).val(value);
                $(divselectid + " ul").hide();
                $(divselectid + " ul li").show();
            });
            $('body').click(function (e) {
                if ($(e.target).parents(divselectid).length == 0) {
                    if ($(divselectid + " ul").is(':visible')) {
                        $(divselectid + " ul").hide();
                        $(divselectid + " ul li").show();
                    }
                }
            });
        };

//鏌ョ湅璇︽儏
        function open_trade(obj, order_id) {
            var tr = $(obj).parents(".marketsTable tr");
            var tr_open = tr.next();
            //濡傛灉鏄睍寮€鐘舵€侊紝灏辨敹璧�
            if (tr_open.css("display") != "none") {
                tr_open.hide();
                tr.removeClass("openNext_tr");
                return false;
            }
            //濡傛灉鏈夋暟鎹紝鐩存帴灞曠ず
            if (tr_open.text().length > 0) {
                tr_open.css("display", "table-row");
                tr.addClass("openNext_tr");
                return false;
            }
            if ($(obj).hasClass("lock") == true) {
                return false;
            }
            //濡傛灉娌℃湁鏁版嵁锛岃幏鍙栨暟鎹�
            $.ajax({
                type: "GET",
                url: "/entrust/entrust_info",
                async: true,
                data: {order_id: order_id},
                dataType: "html",
                beforeSend: function () {
                    $(obj).addClass("lock");
                },
                success: function (html) {
                    $(obj).removeClass("lock");
                    if (isJson(html) == true) {
                        var result = $.parseJSON(html);
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
                        }
                        return false;
                    }
                    tr_open.html(html);
                    tr_open.css("display", "table-row");
                    tr.addClass("openNext_tr");
                }
            });
        }


        $(function () {
            var date_lang = 'cn';
            if ($("#languageBt").hasClass("cn") == false) {
                date_lang = 'en';
            }
            laydate.render({
                elem: "#start_date",
                lang: date_lang,
                max: new Date().format("yyyy-MM-dd"),
                done: function (value) {
                    // var end_date = $("#end_date").val();
                    // if (end_date.length > 0) {
                    //     if (U.compare_date(value, end_date) == -1) {
                    //         $("#start_date").val("");
                    //         return false;
                    //     }
                    // }
                }
            });
            // laydate.render({
            //     elem: '#end_date',
            //     lang: date_lang,
            //     max: new Date().format("yyyy-MM-dd"),
            //     done: function (value) {
            //         var start_date = $("#start_date").val();
            //         if (start_date.length > 0) {
            //             if (U.compare_date(start_date, value) == -1) {
            //                 $("#end_date").val("");
            //                 return false;
            //             }
            //         }
            //     }
            // });
        });
    }

    /**
     * 历史成交
     */
    function historyTrade() {
        var pageSize=10;
        var today = (new Date()).format("yyyy-MM-dd");
        $("#start_date").val(today);
        $(function () {
            var date_lang = 'cn';
            if ($("#languageBt").hasClass("cn") == false) {
                date_lang = 'en';
            }
            laydate.render({
                elem: "#start_date",
                lang: date_lang,
                max: new Date().format("yyyy-MM-dd"),
                done: function (value) {
                    // var end_date = $("#end_date").val();
                    // if (end_date.length > 0) {
                    //     if (U.compare_date(value, end_date) == -1) {
                    //         $("#start_date").val("");
                    //         return false;
                    //     }
                    // }
                }
            });
            // laydate.render({
            //     elem: '#end_date',
            //     lang: date_lang,
            //     max: new Date().format("yyyy-MM-dd"),
            //     done: function (value) {
            //         var start_date = $("#start_date").val();
            //         if (start_date.length > 0) {
            //             if (U.compare_date(start_date, value) == -1) {
            //                 $("#end_date").val("");
            //                 return false;
            //             }
            //         }
            //     }
            // });
            $(".marketsTableBox").show();
            //加载当前委托数据
            var formDate0 = $("#search_form").serializeJson();
            P.page(1,pageSize,'/ex/member/account/history_tradeList','trade_history_tpl','trade_history_render','',formDate0);
            searchCoins();

            //查询
            $(document).on("click", ".white", function () {
                var formDate = $("#search_form").serializeJson();
                P.page(1,pageSize,'/ex/member/account/history_tradeList','trade_history_tpl','trade_history_render','',formDate);
            });
            $(document).on("click", "#search_form .resetBt", function () {
                $(".inputIcoCalendar").val("");
                $("#start_date").val(today);
                if ($("#base_asset_box").length > 0) {
                    var quote_asset = $("#quote_asset_box").find("a[selectid='']").text();
                    $("#base_asset_box").find("cite input").val("");
                    $("#quote_asset_box").find("cite").text(quote_asset);
                    $("#quote_asset_box").find(".selectInput").val("");
                }
                if ($("#side_box").length > 0) {
                    var side = $("#side_box").find("a[selectid='0']").text();
                    $("#side_box").find("cite").text(side);
                    $("#side_box").find(".selectInput").val("0");
                    $("input.trigger_submit").prop("checked", false);
                }
                // $("#search_form .searchBt").click();
            });

            jQuery.divselect = function (divselectid, inputselectid) {

                $(divselectid + " cite").on("click", function () {
                    var ul = $(this).parent(divselectid).find("ul");
                    /* if (ul.css("display") == "none") {
                         ul.show();
                     } else {
                         ul.hide();
                     }*/

                    ul.show();
                });

                //鎼滅储: obj鏄緭鍏ユ鐨勫璞�
                var select_search = function (obj) {
                    var ul = $(obj).parents(divselectid).find("ul");
                    ul.show();
                    var txt = $.trim($(obj).val().toUpperCase());
                    if (txt.length > 0) {
                        ul.find("li").hide().each(function () {
                            var keyword = $(this).text().toUpperCase();
                            if (keyword.indexOf(txt) >= 0) {
                                $(this).show();
                            }
                        });
                    } else {
                        ul.find("li").show();
                    }
                };

                //鐩戞帶杈撳叆妗�
                $(divselectid + " cite input").on('compositionstart', function () {
                    $(this).prop('comStart', true);
                }).on('compositionend', function () {
                    $(this).prop('comStart', false);
                }).on("input propertychange", function () {
                    // 涓枃杈撳叆杩囩▼涓笉鎴柇
                    if ($(this).prop('comStart') == true) {
                        return false;
                    }
                    select_search(this);
                }).keyup(function (event) {
                    if (!event) {
                        event = window.event;
                    }
                    if (event.keyCode == 13 || event.keyCode == 32 || event.keyCode == 16) {
                        select_search(this);
                    }
                });

                $(divselectid + " ul li a").click(function () {
                    // $(divselectid + " ul").find('li').find('a').click(function () {
                    var txt = $(this).html();
                    if ($(this).parents(divselectid).find("cite input").length == 0) {
                        $(this).parents(divselectid).find("cite").html(txt);
                    }
                    var value = $(this).attr("selectid");
                    $(this).parents(divselectid).find(inputselectid).val(value);
                    $(divselectid + " ul").hide();
                    $(divselectid + " ul li").show();
                });
                $('body').click(function (e) {
                    if ($(e.target).parents(divselectid).length == 0) {
                        if ($(divselectid + " ul").is(':visible')) {
                            $(divselectid + " ul").hide();
                            $(divselectid + " ul li").show();
                        }
                    }
                });
            };
        });
    }

    function bindCard(){
        //是否图形验证通过
        var verity = false;
        var validate;
        //验证码初始化
        var captchaIns;
        initNECaptcha({
            captchaId: '87cc815627084c41bb8cb582e9378997',
            element: '#captcha',
            mode: 'popup',
            width: 320,
            onClose: function () {
                // 弹出关闭结束后将会触发该函数
                $(".sendCode").on("click", function () {
                    var _this = $(this);
                    var mobile_v = $(".form").validate().element($("#userName"));
                    if(!verity){
                        $(".tip").removeClass("p-hide").html($('#validCode').val());
                        return;
                    }
                    if (mobile_v && verity) {
                        _this.attr("disabled", "true");

                    }else{
                        $(".tip").removeClass("p-hide").html("请确保手机号和图形验证码正确");
                    }
                })
            },
            onVerify: function (err, data) {
                if(err!=null){
                    $(".tip").removeClass("p-hide").html($('#validCodeError').val());
                    verity = false;
                }else{
                    validate = data.validate;
                    $(".tip").addClass("p-hide");
                    verity = true;
                    var options={"url":BASE_PATH+"/sendCode","data":{"mobile":$("#userName").val(),"validate":validate,"areaCode":$("#areaCode").val()}}
                    core.get(options).then(function (data) {
                        if(data.success == true){
                            U.timer(data.data.codeTimeOut,$(".sendCode"));
                            $(".tip").addClass("p-hide").html("");
                        }else {
                            $(".sendCode").removeAttr("disabled");
                            $(".tip").removeClass("p-hide").html(data.msg);
                        }
                    }).fail(function (jqXHR, textStatus) {
                        $(".tip").removeClass("p-hide").html("网络异常");
                    });
                }
            }
        }, function onload (instance) {
            // 初始化成功后，用户输入对应用户名和密码，以及完成验证后，直接点击登录按钮即可
            captchaIns = instance;
        }, function onerror (err) {
            // 验证码初始化失败处理逻辑，例如：提示用户点击按钮重新初始化
            $(".tip").removeClass("p-hide").html(err);
            verity = false;
        })

// 监听button的点击事件，`弹出验证码
        document.getElementById('button-des').addEventListener('click', function (e) {
            e.preventDefault();
            if(captchaIns){
                captchaIns.refresh();
                captchaIns.popUp();
            }
        })


        core.list({url:BASE_PATH+"/system/type/appDictionary/findByKey/root_bank"}).then(function (data) {
            tpl.render({"tplId": "#bank_tpl", "renderId": "#bankName", "data": data[0].children});
            effect.divSelect('bankName');
        })


        $("#bindCard_button").click(function(){
            // $(document).on("click", "bindCard_button", function () {
            //检查必填项是否缺失

            var bankName = $("#bankName").val();
            var bankCard = $("#bankCard").val();
            // var bankCard1 = $("#bankCard1").val();
            if(U.isNotEmpty(bankName)&&U.isNotEmpty(bankCard)){

            }else{
                if(!U.isNotEmpty(bankName)){
                    $("#error").show();
                    $("#error_show").html(Lang.input_cant_empty);
                    $("#bankName").addClass("error");
                    return false;
                }else if(!U.isNotEmpty(bankCard)){
                    $("#error").show();
                    $("#error_show").html(Lang.input_cant_empty);
                    $("#bankCard").addClass("error");
                    return false;
                }
            }

            var  data=$("#form").serializeArray();
            var _data = [];
            $.each(data, function (i, field) {
                if (field['name'] != 'confirm_password') {

                    if (field['name'] != 'password' && field['name'] != 'oldPassword') {
                        _data.push(field)
                    } else {
                        _data.push({
                            'name': field['name'],
                            'value': field['value']
                        });
                    }
                }
            });

            $.post("/member/security/addBankCard",_data,function(data){
                if (data.success) {
                    data.url="/ex/member/user";
                }
                U.response(data)
            });

        });
    }
    function withdraw_info() {
            var body_date ="";
            var pageSize =10;
            $(function () {
                var date_lang = 'cn';
                if ($("#languageBt").hasClass("cn") == false) {
                    date_lang = 'en';
                }
                laydate.render({
                    elem: "#test19",
                    lang: date_lang,
                    max: new Date().format("yyyy-MM-dd"),
                    done: function (value) {
                        body_date = value;
                    }
                });
                $(window).bind('resize', function (e) {
                    var _windowH = $(window).height();
                    changeFullScreen(_windowH);
                });
                $("*[timestamp]").each(function () {
                    var timestamp = $(this).attr('timestamp');
                    $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
                });
                var obj = $(".t_menu");
                $(".marketsTableBox").show();

                // P.page(1,pageSize,'/ex/member/account/entrustList','entrust_tpl','entrust_render',cancel);
                P.pagelist(1,pageSize, '/ex/member/account/withdrawInfo','marketsTable_tpl','marketsTable_render');
                // $(".marketsTableBox").show();

                //
                // $('.tabs').each(function () {

                //         var config_type = {"1": "/ex/member/account/rechargeInfo", "2": "/ex/member/account/withdrawInfo","3": "/ex/member/account/tradeMiningRecord"};
                //         // 加载充值取现数据
                //         core.list({url: BASE_PATH + config_type[type]},{data:{"page":1,"pageSize":10}}).then(function (data) {
                //             console.log()
                //             obj.removeClass("lock");
                //             if(type == 1){
                //                 tpl.render({
                //                     "tplId": "#marketsTable_tpl",
                //                     "renderId": "#marketsTable_render",
                //                     "data": data.data.rows
                //                 });
                //                 // $("#tablelist").css("display","none");
                //                 // $("#tablemoney").css("display","block");
                //                 $(".HDA").css("display","none");
                //                 $(".fre").css("display","none");
                //
                //             }else if(type == 2){
                //                 obj.removeClass("lock");
                //                 tpl.render({
                //                     "tplId": "#marketsTable_tpl",
                //                     "renderId": "#marketsTable_render",
                //                     "data": data.data.rows
                //                 });
                //                 $(".fre").css("display","none");
                //                 // $("#tablelist").css("display","none");
                //                 // $("#tablemoney").css("display","block");
                //                 $(".HDA").css("display","block");
                //
                //
                //             }else {
                //                 tpl.render({
                //                     "tplId": "#marketsTable_tpl",
                //                     "renderId": "#marketsTable_render",
                //                     "data": data.data.rows
                //                 });
                //                 console.log( data.data.rows)
                //                 // $("#tablelist").css("display","block");
                //                 // $("#tablemoney").css("display","none");
                //                 obj.removeClass("lock");
                //                 $(".HDA").css("display","none");
                //                 $(".fre").css("display","block");
                //                 $(".frea").css("display","none");
                //
                //                 // $.ajax({
                //                 //     type: "get",
                //                 //     url: "/ex/member/account/tradeMiningRecord",
                //                 //     dataType: 'json',
                //                 //     async: true,
                //                 //     data:{"page":1},
                //                 //     success: function (data) {
                //                 //         console.log(data.rows)
                //                 //         var datalist = data.rows;
                //                 //         // var strlist = '';
                //                 //         // for (var i=0;i<datalist.length;i++) {
                //                 //         //     var tmap = datalist[i];
                //                 //         //     strlist+='<td style=" text-align: center;"> </td>';
                //                 //         //     strlist+='<td style=" text-align: center;" id="nat">'+tmap.tradeCoinCode+'_'+tmap.tradeCoinRealTimePrice+'</td>';
                //                 //         //     strlist+='<td style=" text-align: center;"  id="nat_1">'+tmap.money+'</td>';
                //                 //         //     strlist+='<td style=" text-align: center;" id="nat_2">'+tmap.coinCode+'</td>';
                //                 //         //     strlist+='<td style=" text-align: center;" id="nat_3">'+tmap.createTime+'</td>';
                //                 //         //     strlist+='<td style=" text-align: center;" id="nat_4">'+tmap.remark+'</td>';
                //                 //         // }
                //                 //         // $("#marketsTable_render1 #template").html(strlist)
                //                 //         $.each(datalist, function (i, n) {
                //                 //             var row = $("#template").clone();
                //                 //             row.find("#nat").text(n.tradeCoinCode + "_" + n.tradeCoinRealTimePrice);
                //                 //             row.find("#nat_1").text(n.money);
                //                 //             row.find("#nat_2").text(n.coinCode);
                //                 //             row.find("#nat_3").text(n.createTime);
                //                 //             row.find("#nat_4").text(n.remark);
                //                 //             row.appendTo("#tablelist");//添加到模板的容器中
                //                 //
                //                 //         });
                //                 //         // $("#tablelist").empty();
                //                 //
                //                 //
                //                 //     }
                //                 // })
                //
                //             }
                //         });
                //
                //     });
                //     $(document).on("click", ".fold .arrBt", function () {
                //         $(this).parents("tr").find(".fold_detail,.fold").toggleClass("open");
                //     });
                //     $(document).on("click", ".page a", function (e) {
                //         stopDefault(e);
                //         var obj = $(".t_menu");
                //         if (obj.hasClass("lock") == true) {
                //             return false;
                //         }
                //         var url = $(this).attr("href");
                //         $.ajax({
                //             type: "GET", url: url, async: true, dataType: "html", beforeSend: function () {
                //                 obj.addClass("lock");
                //             }, success: function (html) {
                //                 obj.removeClass("lock");
                //                 if (isJson(html) == true) {
                //                     var result = $.parseJSON(html);
                //                     if (result['code'] > 0) {
                //                         layer.alert(result['msg'], {
                //                             title: Lang['alert_title'],
                //                             btn: Lang['confirm_button'],
                //                             closeBtn: 0,
                //                         }, function (index) {
                //                             layer.close(index);
                //                             if (result['url'] != undefined) {
                //                                 window.location = result['url'];
                //                             }
                //                         });
                //                         return false;
                //                     }
                //                 }
                //                 var obj_html = $(html);
                //                 obj_html.find("*[timestamp]").each(function () {
                //                     var timestamp = $(this).attr('timestamp');
                //                     $(this).text(new Date(timestamp * 1000).format("yyyy-MM-dd hh:mm:ss"));
                //                 });
                //                 $(".marketsTableBox").empty().html(obj_html);
                //             }
                //         });
                //     });
                // });

                function changeFullScreen(_windowH) {
                    if ($(document.body).height() < _windowH) {
                        var _fullScreenH = _windowH - $(".fullScreen_h").offset().top - $(".footerNavBar").height() - $(".footerBar").height() - 15 - 30;
                        $(".fullScreen_h").css("min-height", _fullScreenH + "px");
                    }
                }

                function cancel_withdraw(obj, txno) {
                    if ($(obj).hasClass("disabled") == true) {
                        return false;
                    }
                    if ($(obj).hasClass("lock") == true) {
                        return false;
                    }
                    layer.confirm(Lang['cancel_confirm'], {
                        icon: 3,
                        title: Lang['confirm_title'],
                        btn: [Lang['confirm_button'], Lang['cancel_button']],
                        closeBtn: 0,
                    }, function (index) {
                        layer.close(index);
                        $.ajax({
                            type: "POST",
                            url: '/withdraw/cancel',
                            async: true,
                            data: {txno: txno, _token: $("meta[name='csrf-token']").attr("content")},
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
                                    refresh_list();
                                }
                            }
                        });
                    });
                }

                function resend_email(obj, txno) {
                    if ($(obj).hasClass("disabled") == true) {
                        return false;
                    }
                    if ($(obj).hasClass("lock") == true) {
                        return false;
                    }
                    $.ajax({
                        type: "POST",
                        url: '/withdraw/resend_email',
                        async: true,
                        data: {txno: txno, _token: $("meta[name='csrf-token']").attr("content")},
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
                                layer.msg(Lang['send_email_success']);
                            }
                        }
                    });
                }

                function refresh_list() {
                    if ($(".page .active").length == 0) {
                        $(".t_menu li.cur").click();
                        return false;
                    }
                    var p = $(".page .active").text();
                    var url = $(".page a:first").attr("href");
                    url = url.replace(/\?p=(\d)/, '?p=' + p);
                    url = url.replace(/&p=(\d)/, '&p=' + p);
                    $.ajax({
                        type: "GET", url: url, async: true, dataType: "html", success: function (html) {
                            if (isJson(html) == true) {
                                var result = $.parseJSON(html);
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
                                }
                            }
                            $(".marketsTableBox").empty().html(html);
                        }
                    });
                }
            })
        }
        function referral(){
        $(".seep_1_2_2").on("click", function () {
            layer.open({
                type: 1,
                title: '分享好友',
                area: ['500px', '610px'],
                shade: 0.8,//遮罩透明度
                shadeClose:true,//点击遮罩关闭层
                skin:'layui-layer-rim',
                bgcolor: '#181B2A',
                border: [0, 0, ' #F9F9F9'],
                scrollbar: false,
                content: $("#show_div"),
                // success: function () {
                //
                //     $(".mate_lisa_H").empty();
                //     // setTimeout(() => {
                //         html2canvas(document.querySelector("#show_img"), {useCORS: true}).then((canvas) => {
                //             let imgData = canvas.toDataURL()
                //             let img = document.createElement('img')
                //             img.src = imgData
                //             // $(".mate_lisa_H").removeAttr("src");
                //             // document.getElementsByClassName('.mate_lisa_H').innerHTML=""
                //             document.querySelector('.mate_lisa_H').appendChild(img);
                //         })
                // // }, 500);
                //
                // }
            });
        })
            var  pageSize =10 ;
            P.pageKiss(1,pageSize, '/promoterList','marketsTable_tpl','marketsTable_render');
            $.ajax({
                type: "get",
                url: "/promoterList",
                dataType: 'json',
                async: false,
                data:{"page":1,"pageSize":10},
                success: function (data) {
                    if(data.success){
                        $("#text_res").text(data.data.pagination.totalPromotionNum+" \xa0"+"人");
                        $("#text_resc").text(data.data.pagination.totalAward  +" \xa0"+"BT");
                        $("#text_resd").text(data.data.pagination.totalRealAward  +" \xa0"+"BT");
                    }
                }
            });
        }
        function advertise(){
            $.ajax({url:"/exchange/coins_area",async:false}).then(function (data) {
                var BT_data =JSON.parse(JSON.parse(data).USDT);
                for(var i = 0; i < BT_data.length; i++){
                    $("#BT_USDT").text(BT_data[2].last+"USDT / ¥ "+BT_data[2].duihuan)
                }
            });
        }

    exports.globalInit = globalInit;
    exports.logList = logList;

    exports.account = account;
    exports.balances = balances;
    exports.referral=referral;
    exports.deposit = deposit;
    exports.withdraw = withdraw;
    exports.moneyInfo = moneyInfo;
    exports.c2c = c2c;
    exports.entrust = entrust;
    exports.historyEntrust = historyEntrust;
    exports.historyTrade = historyTrade;
    exports.init = init;
    exports.bindCard = bindCard;
    exports.advertise=advertise;
    exports.withdraw_info =withdraw_info

});