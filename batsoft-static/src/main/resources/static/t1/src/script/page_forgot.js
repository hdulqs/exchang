/**
 *<p>page_forgot.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {

    var U = require('./module_utils'),
        tpl = require('./module_tpl'),
        mMd5 = require('./module_md5'),
        core = require('./module_core');

    /**
     * 初始化发送短信验证码
     */
    function forgot() {
        //是否图形验证通过
        var verity = false;
        var validate;
        //验证码初始化
        var captchaIns;
        initNECaptcha({
            captchaId: '87cc815627084c41bb8cb582e9378997',
            element: '#captcha',
            mode: 'float',
            width: 400,
            onVerify: function (err, data) {
                if(err!=null){
                    $(".tip").removeClass("p-hide").html($('#validCodeError').val());
                    verity = false;
                }else{
                    validate = data.validate;
                    $(".tip").addClass("p-hide");
                    verity = true;
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
        // 发送验证码
        $(".sendCode").on("click", function () {
            var _this = $(this);
            var mobile_v = $(".form").validate().element($("#userName"));
            if(!verity){
                $(".tip").removeClass("p-hide").html($('#validCode').val());
                return;
            }
            if (mobile_v && verity) {
                _this.attr("disabled", "true");
                var options={"url":BASE_PATH+"/sendCode","data":{"mobile":$("#userName").val(),"validate":validate,"areaCode":$("#areaCode").val()}}
                core.get(options).then(function (data) {
                    if(data.success){
                        U.timer(data.data.codeTimeOut,$(".sendCode"));
                        $(".tip").addClass("p-hide").html("");
                    }else {
                        _this.removeAttr("disabled");
                        $(".tip").removeClass("p-hide").html(data.msg);
                    }
                }).fail(function (jqXHR, textStatus) {
                    $(".tip").removeClass("p-hide").html("网络异常");
                });
            }else{
                $(".tip").removeClass("p-hide").html("请确保手机号和图形验证码正确");
            }
        })

        var obj = $(".t_menu");
        $("#google_forgot").css('display','none');
        $("#mobile_forgot").css('display','block');
        // googleValid();
        mobileValid();
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

                if(type=='1'){   //google校验
                    $("#google_forgot").css('display','block');
                    $("#mobile_forgot").css('display','none');
                    googleValid();
                }else if(type='2') {  //短信校验
                    $("#google_forgot").css('display','none');
                    $("#mobile_forgot").css('display','block');
                    mobileValid();
                }
            });
        });
    }

    /**
     * google验证码校验
     */
    function googleValid(){
        $("#pass1").blur(
            function(){
                pass1Valid('');
            }
        );

        $("#pass2").blur(
            function(){
                pass2Valid('');
            }
        );

        // 发送验证码
        $("#googleValid_submit").on("click", function () {
            if(pass1Valid('') && pass2Valid('')&&$("#google_input").val().length==6) {
                true_submit("google_form");
            }
        });
    }

    /**
     * 短信验证码校验
     */
    function mobileValid() {
        $("#pass1_tel").blur(
            function () {
                pass1Valid();
            }
        );

        $("#pass2_tel").blur(
            function () {
                pass2Valid();
            }
        );
        $("#userName").blur(
            function () {
                passValid();
            }
        )

        // 确认 更改密码
        $("#mobileValid_submit").on("click", function () {
            if (pass1Valid() && pass2Valid() && passValid() && $("#mobileCode").val().length > 3) {
                true_submit("tel_form");
            }
        });
    }
    $(document).ready(function() {
        mobileValid()
    })
    function passValid(){
        if($("#userName").val().length==0){
            $("#error").show();
            $("#error_show").html(Lang.input_cant_empty);
            $("#userName").addClass("error");
            return false;
        }else{
            $("#error").hide();
            $("#userName").removeClass("error");
            return true;
        }
    }

    function pass1Valid(){
        if($("#pass1_tel").val().length==0){
            $("#pass1_tel").show();
            $("#error_show").html(Lang.input_cant_empty);
            $("#pass1_tel").addClass("error");
            return false;
        }else{
            if(U.isPass($("#pass1_tel").val())){
                $("#error").hide();
                $("#error_show").html();
                $("#pass1_tel").removeClass("error");
                return true;
            }else{
                $("#error").show();
                $("#error_show").html(Lang.passvalid);
                $("#pass1_tel").addClass("error");
                return false;
            }

        }
    }

    function pass2Valid(){
        if($("#pass2_tel").val().length==0){
            $("#error").show();
            $("#error_show").html(Lang.input_cant_empty);
            $("#pass2_tel").addClass("error");
            return false;
        }else{
            if($("#pass2_tel").val()!=$("#pass1_tel").val()) {
                $("#error").show();
                $("#error_show").html(Lang.password_diff);
                $("#pass2_tel").addClass("error");
                return false;
            }else{
                if(U.isPass($("#pass2_tel").val())){
                    $("#error").hide();
                    $("#pass2_tel").removeClass("error");
                    return true;
                }else{
                    $("#error").show();
                    $("#error_show").html(Lang.passvalid);
                    $("#pass2_tel").addClass("error");
                    return false;
                }
            }
        }
    }

    function true_submit(form){
            var  data=$("#"+form).serializeArray();
            var _data = [];
            $.each(data, function (i, field) {
                if (field['name'] != 'confirm_password') {

                    if (field['name'] != 'password' && field['name'] != 'oldPassword') {
                        _data.push(field)
                    } else {
                        _data.push({
                            'name': field['name'],
                            'value': mMd5.hbmd5(field['value'])
                        });
                    }
                }
            });

            $.post("/forgotPassword",_data,function(data){
                if (data.success == true) {
                   var forget_paw = $("#forget_H").text();
                    layer.msg(forget_paw, {time: 2000, icon: 6});
                    setTimeout('window.location = "https://bttmall.com'+data.data.loginUrl+'"',1000);
                    // if(U.isNotEmpty(data.data.loginUrl)){
                    //     window.location = BASE_PATH + data.data.loginUrl;
                    // }else{
                    //     window.location = BASE_PATH + "/login";
                    // }
                } else {
                    // captchaIns.refresh();
                    U.response(data);
                }
            });
    }

    exports.forgot = forgot;
});