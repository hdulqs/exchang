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
        mVad = require('./module_validator'), //表单校验
        v = require('./module_validate'),
        effect = require('./module_effect'), //特效
        swal = require('./module_swal'), //弹窗 alert
        tpl = require('./module_tpl'),
        U = require('./module_utils'),
        F = require('./module_upload'),

        core = require('./module_core');

    /**
     * 用户头部公共信息加载
     */
    function globalInit() {
        //安全信息图表
        var userInfo = GLOBAL.user;
        userInfo.oldLoginTime = U.dateFormat(new Date(userInfo.oldLoginTime), "yyyy-MM-dd hh:mm:ss")
        tpl.render({"tplId": "#userInfoTmpl", "renderId": "#userInfoRender", "data": userInfo});
        effect.piechart();

    }

    /**
     * 账户安全
     */
    function setTradePassword() {
        /**
         * 设置资金密码提交
         * 表单校验规则
         */
        var rules = {
            rules: {

                password: {
                    required: true,
                    password: true

                },
                confirm_password: {
                    required: true,
                    password: true,
                    equalTo: "#password"
                }
            },
            messages: {}
        };


//表单提交
        mVad({
            forms: $('.form'),
            tip: '.form_tip',
            rules: rules,
            postData: function (data) {
                var _data = [];
                $.each(data, function (i, field) {
                    if (field['name'] != 'confirm_password') {
                        field['name'] != 'password' ? _data.push(field) : _data.push({
                            'name': 'password',
                            'value': mMd5.hbmd5(field['value'])
                        });
                    }
                });
                return _data;
            },
            formSubmit: function (_option, form) {
                core.save(_option.ajaxData).then(function (data) {
                    if (data.success) {
                        swal.swalBase(data.msg, "", "success");
                    } else {
                        var errInfo = "";
                        if (data.data != undefined) {
                            errInfo = data.data.join("");
                        }
                        swal.swalError(data.msg, errInfo);
                    }
                    _option.callback(data, form);
                }).fail(function (jqXHR, textStatus) {
                    swal.swalError(textStatus, jqXHR.status);
                    _option.callback({}, form);
                });
            }
        });

    }
    /**
     * 注册用户
     */
    function registerPost() {
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
        var rules = {
            rules: {
                validCode: {
                    required: true,
                    minlength: 4,
                    maxlength: 4,
                },

                mobileCode:{
                    required: true,
                    minlength: 6,
                    maxlength: 6,
                },
                userName: {
                    required: true,
                    minlength : 11,
                    mobile:true
                },
                password: {
                    required: true,
                    pwd:true,
                    minlength: 6,
                    maxlength: 20,
                },
                tradePwd: {
                    required: true,
                    password:true,
                    minlength: 6,
                    maxlength: 6,
                },
                passwords: {
                    required: true,
                    password:true,
                    minlength: 6,
                    maxlength: 6,
                },
                confirm_password:{
                    required: true,
                    minlength: 6,
                    maxlength: 20,
                    equalTo:"#password"
                },
                tradePwd: {
                    required: true,
                    tradePwd: true,
                },
                passwords: {
                    required: true,
                    equalTo:"#password_his"
                },
            },
            messages: {}
        };
        var url = document.location.toString();//获取url地址
        var urlParmStr = url.slice(url.indexOf('?')+1);//获取问号后所有的字符串
        var arr = urlParmStr.split('&');//通过&符号将字符串分割转成数组
        var courseId = arr[0].split("=")[1];//获取数组中第一个参数//第二个参数
        unit_title=decodeURI(courseId);//转码将解码方式unscape换为decodeURI，将中文参数获取
//登录表单
        if(unit_title == "undefined"){
            $("#id_price1").val("")
        }else {
            $("#id_price1").val(unit_title)
        }
        mVad({
            forms: $('.form'),
            tip: '.tip',
            rules: rules,
            beforeValidation:function(){
                if(!$("#cr").is(':checked') ){
                    $(".tip").removeClass("p-hide").html("请先阅读用户协议！");
                    return false;
                }
                return true;
            },
            postData: function (data) {
                var _data = [];
                $.each(data, function (i, field) {
                    if(field['name'] != 'confirm_password'&&  field['name'] != 'passwords'){
                        if( field['name'] == 'password' ){
                            _data.push({
                                'name': 'password',
                                'value': mMd5.hbmd5(field['value'])
                            });
                        }else if( field['name'] == 'tradePwd') {
                            _data.push({
                                'name': 'tradePwd',
                                'value': mMd5.hbmd5(field['value'])
                            });
                        }else {
                            _data.push(field)
                        }
                    }

                });
                return _data;
            },
            formSubmit: function (_option, form) {
                core.save(_option.ajaxData).then(function (data) {
                    if (data.success) {
                       var ju = $("#kiu").text();
                        layer.msg(ju, {time: 2000, icon: 6});
                        setTimeout('window.location = "https://bttmall.com'+data.data.loginUrl+'"',1000);
                    } else {
                        captchaIns.refresh();
                        $(_option.tip).removeClass("p-hide").html(data.msg);
                    }
                    _option.callback(data, form);
                }).fail(function (jqXHR, textStatus) {
                    $(_option.tip).removeClass("p-hide").html(data.msg);
                    _option.callback({}, form);
                });
            }
        });


        $(document).ready(function(){
            $("#userName").blur(
                function(){
                    passValid()
                }
            );

            $("#password").blur(
                function(){
                    pass1Valid()
                }
            );
            $("#confirm_password").blur(
                function(){
                    pass2Valid()
                }
            );
            $("#password_his").blur(
                function(){
                    pass3Valid()
                }
            );
            $("#password_pis").blur(
                function(){
                    pass4Valid()
                }
            );

            $("#submit_pass").click(function () {
                updateFrom();
            });
        })


        function passValid(){
            if($("#userName").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                //$("#userName").addClass("error");
                return false;
            }else{
                $("#error").hide();
               // $("#userName").removeClass("error");
                return true;
            }
        }
        function pass1Valid(){

            if($("#password").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#password").addClass("error");
                return false;
            }else{
                if(U.isPass($("#password").val())){
                    $("#error").hide();
                    $("#password").removeClass("error");
                    return true;
                }else{
                    $("#error").show();
                    $("#error_show").html(Lang.passvalid);
                    $("#password").addClass("error");
                    return false;
                }

            }
        }

        function pass2Valid(){
            if($("#confirm_password").val()!=$("#password").val()) {
                $("#error").show();
                $("#error_show").html(Lang.password_diff);
                $("#confirm_password").addClass("error");
                return false;
            }else{
                if($("#confirm_password").val().length==0){
                    $("#error").show();
                    $("#error_show").html(Lang.input_cant_empty);
                    $("#confirm_password").addClass("error");
                    return false;
                }else{
                    if(U.isPass($("#confirm_password").val())){
                        $("#error").hide();
                        $("#confirm_password").removeClass("error");
                        return true;
                    }else{
                        $("#error").show();
                        $("#error_show").html(Lang.passvalid);
                        $("#confirm_password").addClass("error");
                        return false;
                    }
                        }
            }
        }
        function pass3Valid(){
            if($("#password_his").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#password_his").addClass("error");
                return false;
            }else{
                if(U.isQass($("#password_his").val())){
                    $("#error").hide();
                    $("#password_his").removeClass("error");
                    return true;
                }else{
                    $("#error").show();
                    $("#error_show").html(Lang.passvalider);
                    $("#password_his").addClass("error");
                    return false;
                }

            }
        }

        function pass4Valid(){
            if($("#password_pis").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#password_pis").addClass("error");
                return false;
            }else{

                if($("#password_pis").val()!=$("#password_his").val()) {
                    $("#error").show();
                    $("#error_show").html(Lang.password_diff);
                    $("#password_pis").addClass("error");
                    return false;
                }else{
                    if(U.isQass($("#password_pis").val())){
                        $("#error").hide();
                        $("#password_pis").removeClass("error");
                        return true;
                    }else{
                        $("#error").show();
                        $("#error_show").html(Lang.passvalider);
                        $("#password_pis").addClass("error");
                        return false;
                    }

                }
            }

        }


    }


    /**
     * 注册用户
     */
    // function registerPost() {
    //     debugger
    //     //是否图形验证通过
    //     var verity = false;
    //     var validate;
    //     //验证码初始化
    //     var captchaIns;
    //     initNECaptcha({
    //         captchaId: '87cc815627084c41bb8cb582e9378997',
    //         element: '#captcha',
    //         mode: 'float',
    //         width: 400,
    //         onVerify: function (err, data) {
    //             if(err!=null){
    //                 $(".tip").removeClass("p-hide").html($('#validCodeError').val());
    //                 verity = false;
    //             }else{
    //                 validate = data.validate;
    //                 $(".tip").addClass("p-hide");
    //                 verity = true;
    //             }
    //         }
    //     }, function onload (instance) {
    //         // 初始化成功后，用户输入对应用户名和密码，以及完成验证后，直接点击登录按钮即可
    //         captchaIns = instance;
    //     }, function onerror (err) {
    //         // 验证码初始化失败处理逻辑，例如：提示用户点击按钮重新初始化
    //         $(".tip").removeClass("p-hide").html(err);
    //         verity = false;
    //     })
    //     // 发送验证码
    //     $(".sendCode").on("click", function () {
    //         debugger
    //         var _this = $(this);
    //         var mobile_v = $(".form").validate().element($("#userName"));
    //         if(!verity){
    //             $(".tip").removeClass("p-hide").html($('#validCode').val());
    //             return;
    //         }
    //         if (mobile_v && verity) {
    //             _this.attr("disabled", "true");
    //             var options={"url":BASE_PATH+"/sendCode","data":{"mobile":$("#userName").val(),"validate":validate,"areaCode":$("#areaCode").val()}}
    //             core.get(options).then(function (data) {
    //                 if(data.success){
    //                     U.timer(data.data.codeTimeOut,$(".sendCode"));
    //                     $(".tip").addClass("p-hide").html("");
    //                 }else {
    //                     _this.removeAttr("disabled");
    //                     $(".tip").removeClass("p-hide").html(data.msg);
    //                 }
    //             }).fail(function (jqXHR, textStatus) {
    //                 $(".tip").removeClass("p-hide").html("网络异常");
    //             });
    //         }else{
    //             $(".tip").removeClass("p-hide").html("请确保手机号和图形验证码正确");
    //         }
    //     })
    //     function updateFrom(){
    //         debugger
    //         if(passValid() && pass1Valid() && pass2Valid() && pass3Valid() && pass4Valid()){
    //             true_submit();
    //         }
    //     }
    //     function true_submit(){
    //         debugger
    //         var  data=$("#form").serializeArray();
    //         var _data = [];
    //         $.each(data, function (i, field) {
    //             if (field['name'] == 'tradePassword' || field['name'] == 'mobileCode' ) {
    //                 _data.push(field)
    //
    //             }
    //         });
    //
    //         $.post("/member/security/updateTradePwd",_data,function(data){
    //             debugger
    //             if (data.success == true) {
    //                 layer.msg('注册成功！', {time: 2000, icon: 6});
    //                 // layer.closeAll();
    //                 // swal.swalSuccess(data.msg, "",function () {
    //                 //     window.location.href = "/ex/member/user";
    //                 // });
    //             } else {
    //                 captchaIns.refresh();
    //                 var errInfo = "";
    //                 if (data.data != undefined) {
    //                     errInfo = data.data.join("");
    //                 }
    //                 swal.swalError(data.msg, errInfo);
    //             }
    //         });
    //     }
    //     $(document).ready(function(){
    //         $("#userName").blur(
    //             function(){
    //                 passValid()
    //             }
    //         );
    //
    //         $("#password").blur(
    //             function(){
    //                 pass1Valid()
    //             }
    //         );
    //         $("#confirm_password").blur(
    //             function(){
    //                 pass2Valid()
    //             }
    //         );
    //         $("#password_his").blur(
    //             function(){
    //                 pass3Valid()
    //             }
    //         );
    //         $("#password_pis").blur(
    //             function(){
    //                 pass4Valid()
    //             }
    //         );
    //
    //         $("#submit_pass").click(function () {
    //             alert("2222")
    //             updateFrom();
    //         });
    //     })
    //
    //     function passValid(){
    //         if($("#userName").val().length==0){
    //             $("#error").show();
    //             $("#error_show").html(Lang.input_cant_empty);
    //             //$("#userName").addClass("error");
    //             return false;
    //         }else{
    //             $("#error").hide();
    //             // $("#userName").removeClass("error");
    //             return true;
    //         }
    //     }
    //     function pass1Valid(){
    //
    //         if($("#password").val().length==0){
    //             $("#error").show();
    //             $("#error_show").html(Lang.input_cant_empty);
    //             $("#password").addClass("error");
    //             return false;
    //         }else{
    //             if(U.isPass($("#password").val())){
    //                 $("#error").hide();
    //                 $("#password").removeClass("error");
    //                 return true;
    //             }else{
    //                 $("#error").show();
    //                 $("#error_show").html(Lang.passvalid);
    //                 $("#password").addClass("error");
    //                 return false;
    //             }
    //
    //         }
    //     }
    //
    //     function pass2Valid(){
    //         if($("#confirm_password").val()!=$("#password").val()) {
    //             $("#error").show();
    //             $("#error_show").html(Lang.password_diff);
    //             $("#confirm_password").addClass("error");
    //             return false;
    //         }else{
    //             if($("#confirm_password").val().length==0){
    //                 $("#error").show();
    //                 $("#error_show").html(Lang.input_cant_empty);
    //                 $("#confirm_password").addClass("error");
    //                 return false;
    //             }else{
    //                 if(U.isPass($("#confirm_password").val())){
    //                     $("#error").hide();
    //                     $("#confirm_password").removeClass("error");
    //                     return true;
    //                 }else{
    //                     $("#error").show();
    //                     $("#error_show").html(Lang.passvalid);
    //                     $("#confirm_password").addClass("error");
    //                     return false;
    //                 }
    //             }
    //         }
    //     }
    //     function pass3Valid(){
    //         if($("#password_his").val().length==0){
    //             $("#error").show();
    //             $("#error_show").html(Lang.input_cant_empty);
    //             $("#password_his").addClass("error");
    //             return false;
    //         }else{
    //             if(U.isQass($("#password_his").val())){
    //                 $("#error").hide();
    //                 $("#password_his").removeClass("error");
    //                 return true;
    //             }else{
    //                 $("#error").show();
    //                 $("#error_show").html(Lang.passvalider);
    //                 $("#password_his").addClass("error");
    //                 return false;
    //             }
    //
    //         }
    //     }
    //
    //     function pass4Valid(){
    //         if($("#password_pis").val().length==0){
    //             $("#error").show();
    //             $("#error_show").html(Lang.input_cant_empty);
    //             $("#password_pis").addClass("error");
    //             return false;
    //         }else{
    //
    //             if($("#password_pis").val()!=$("#password_his").val()) {
    //                 $("#error").show();
    //                 $("#error_show").html(Lang.password_diff);
    //                 $("#password_pis").addClass("error");
    //                 return false;
    //             }else{
    //                 if(U.isQass($("#password_pis").val())){
    //                     $("#error").hide();
    //                     $("#password_pis").removeClass("error");
    //                     return true;
    //                 }else{
    //                     $("#error").show();
    //                     $("#error_show").html(Lang.passvalider);
    //                     $("#password_pis").addClass("error");
    //                     return false;
    //                 }
    //             }
    //         }
    //
    //     }
    //
    // }
    function sendMobileCode() {

        //是否图形验证通过
        var verity = false;
        var validate;
        //验证码初始化
        var captchaIns;
        initNECaptcha({
            captchaId: '87cc815627084c41bb8cb582e9378997',
            element: '#captcha',
            mode: 'float',
            width: 350,
            height:80,
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
        function updateFrom(){
            if(passValid() && pass1Valid() && pass2Valid()){
                true_submit();
            }
        }
        function true_submit(){
            var  data=$("#form").serializeArray();
            var _data = [];
            $.each(data, function (i, field) {
           if (field['name'] == 'tradePassword'  ) {
                _data.push({
                        'name': 'tradePassword',
                        'value': mMd5.hbmd5(field['value'])
                    });
                }else {
                    _data.push(field)
                }
            });
            $.post("/member/security/updateTradePwd",_data,function(data){
                if (data.success) {
                    // layer.closeAll();
                    swal.swalSuccess(data.msg, "",function () {
                        window.location.href = "/ex/member/user";
                    });
                } else {
                    var errInfo = "";
                    if (data.data != undefined) {
                        errInfo = data.data.join("");
                    }
                    swal.swalError(data.msg, errInfo);
                }
            });
        }
        $(document).ready(function(){
            $("#userName").blur(
                function(){
                    passValid()
                }
            );

            $("#password").blur(
                function(){
                    pass1Valid()
                }
            );
            $("#confirm_password").blur(
                function(){
                    pass2Valid()
                }
            );

            $("#submit_pass").click(function () {

                updateFrom();
            });
        })


        function passValid(){
            if($("#password").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#password").addClass("error");
                return false;
            }else{
                $("#error").hide();
                $("#password").removeClass("error");
                return true;
            }
        }
        function pass1Valid(){
            if($("#password").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#password").addClass("error");
                return false;
            }else{
                if(U.isQass($("#password").val())){
                    $("#error").hide();
                    $("#password").removeClass("error");
                    return true;
                }else{
                    $("#error").show();
                    $("#error_show").html(Lang.passvalider);
                    $("#password").addClass("error");
                    return false;
                }

            }
        }

        function pass2Valid(){
            if($("#confirm_password").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#confirm_password").addClass("error");
                return false;
            }else{

                if($("#confirm_password").val()!=$("#password").val()) {
                    $("#error").show();
                    $("#error_show").html(Lang.password_diff);
                    $("#confirm_password").addClass("error");
                    return false;
                }else{
                    if(U.isQass($("#confirm_password").val())){
                        $("#error").hide();
                        $("#confirm_password").removeClass("error");
                        return true;
                    }else{
                        $("#error").show();
                        $("#error_show").html(Lang.passvalider);
                        $("#confirm_password").addClass("error");
                        return false;
                    }
                }
            }

        }

    }

    function addMobileCode() {
        // 发送验证码
        $(".sendCode").on("click", function () {
            var _this = $(this);
            var mobile_v = $(".form").validate().element($("#userName"));
            var validCode_v = $(".form").validate().element($("#validCode"));
            if (mobile_v && validCode_v) {
                _this.attr("disabled", "true");
                var options={"url":BASE_PATH+"/sendCode","data":{"mobile":$("#userName").val(),"validCode":$("#validCode").val(),"areaCode":$("#areaCode").val()}}
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
        function updateFrom(){

            if(passValid() && pass1Valid() && pass2Valid()){
                true_submit();
            }
        }
        function true_submit(){
            var  data=$("#form").serializeArray();
            var _data = [];
            $.each(data, function (i, field) {
                if (field['name'] == 'tradePassword' || field['name'] == 'mobileCode' ) {
                    _data.push(field)

                }
            });

            $.post("/member/security/addTradePwd",_data,function(data){
                if (data.success) {
                    // layer.closeAll();
                    swal.swalSuccess(data.msg, "",function () {
                        window.location.href = "/ex/member/user";
                    });
                } else {
                    var errInfo = "";
                    if (data.data != undefined) {
                        errInfo = data.data.join("");
                    }
                    swal.swalError(data.msg, errInfo);
                }
            });
        }
        $(document).ready(function(){

            $("#userName").blur(
                function(){
                    passValid()
                }
            );

            $("#password").blur(
                function(){
                    pass1Valid()
                }
            );
            $("#confirm_password").blur(
                function(){
                    pass2Valid()
                }
            );

            $("#submit_pass").click(function () {

                updateFrom();
            });
        })


        function passValid(){
            if($("#password").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#password").addClass("error");
                return false;
            }else{
                $("#error").hide();
                $("#password").removeClass("error");
                return true;
            }
        }
        function pass1Valid(){
            if($("#password").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#password").addClass("error");
                return false;
            }else{
                if(!U.isPass($("#password").val())){
                    $("#error").hide();
                    $("#password").removeClass("error");
                    return true;
                }else{
                    $("#error").show();
                    $("#error_show").html(Lang.passvalid);
                    $("#password").addClass("error");
                    return false;
                }

            }
        }

        function pass2Valid(){
            if($("#confirm_password").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#confirm_password").addClass("error");
                return false;
            }else{
                if(!U.isPass($("#confirm_password").val())){
                    $("#error").hide();
                    $("#confirm_password").removeClass("error");
                    return true;
                }else{
                    $("#error").show();
                    $("#error_show").html(Lang.passvalid);
                    $("#confirm_password").addClass("error");
                    return false;
                }

            }
        }



    }
    /**
     * 修改资金密码/登录密码
     */
    function updatePassword() {
        function updateFrom(){
            if(passValid() && pass1Valid() && pass2Valid()){
                    true_submit();
            }
        }
        function true_submit(){
            var  data=$("#form-data").serializeArray();
            var _data = [];
            $.each(data, function (i, field) {
                if (field['name'] != 'confirm_password') {
                    if (field['name'] =='password' || field['name'] == 'oldPassword') {
                        _data.push({
                            'name': field['name'],
                            'value': mMd5.hbmd5(field['value'])

                        });
                    } else {

                        _data.push(field)
                    }
                }
            });
            $.post("/member/security/editPassword",_data,function(data){
                if (data.success) {
                    layer.closeAll();
                    swal.swalSuccess(data.msg, "",function () {
                        window.location.href = "/ex/member/user";
                    });
                } else {
                    var errInfo = "";
                    if (data.data != undefined) {
                        errInfo = data.data.join("");
                    }
                    swal.swalError(data.msg, errInfo);
                }
            });
        }
        $(document).ready(function(){
            $("#pass").blur(
                function(){
                    passValid();
                }
            );

            $("#pass1").blur(
                function(){
                    pass1Valid();
                }
            );

            $("#pass2").blur(
                function(){
                    pass2Valid();
                }
            );
            $("#submit_pass").click(function () {
                updateFrom();
            });
        })


        function passValid(){
            if($("#pass").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#pass").addClass("error");
                return false;
            }else{
                $("#error").hide();
                $("#pass").removeClass("error");
                return true;
            }
        }
        function pass1Valid(){
            if($("#pass1").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#pass1").addClass("error");
                return false;
            }else{

                if(U.isPass($("#pass1").val())){
                    $("#error").hide();
                    $("#error_show").html();
                    $("#pass1").removeClass("error");
                    return true;
                }else{
                    $("#error").show();
                    $("#error_show").html(Lang.passvalid);
                    $("#pass1").addClass("error");
                    return false;
                }
            }
        }

        function pass2Valid(){
            if($("#pass2").val().length==0){
                $("#error").show();
                $("#error_show").html(Lang.input_cant_empty);
                $("#pass2").addClass("error");
                return false;
            }else{
                if($("#pass2").val()!=$("#pass1").val()) {
                    $("#error").show();
                    $("#error_show").html(Lang.password_diff);
                    $("#pass2").addClass("error");
                    return false;
                }else{
                    if(U.isPass($("#pass2").val())){
                        $("#error").hide();
                        $("#pass2").removeClass("error");
                        return true;
                    }else{
                        $("#error").show();
                        $("#error_show").html(Lang.passvalid);
                        $("#pass2").addClass("error");
                        return false;
                    }
                }
            }
        }


    }

    /**
     * 绑定邮箱
     */
    function setEmail() {
        /**
         * 绑定邮箱
         * 表单校验规则
         */
        var rules = {
            rules: {
                email: {
                    required: true,
                    email: true
                },
                code: {
                    required: true,
                    minlength: 6,
                    maxlength: 6,

                }
            },
            messages: {}
        };
//表单提交
        mVad({
            forms: $('.form'),
            tip: '.form_tip',
            rules: rules,
            postData: function (data) {

                return data;
            },
            formSubmit: function (_option, form) {
                core.save(_option.ajaxData).then(function (data) {
                    if (data.success) {
                        swal.swalBase(data.msg, "", "success");
                    } else {
                        var errInfo = "";
                        if (data.data != undefined) {
                            errInfo = data.data.join("");
                        }
                        swal.swalError(data.msg, errInfo);
                    }
                    _option.callback(data, form);
                }).fail(function (jqXHR, textStatus) {
                    swal.swalError(textStatus, jqXHR.status);
                    _option.callback({}, form);
                });
            }
        });

    }


    /**
     * 实名认证界面效果
     */
    function userAuthView() {
        $(document).ready(function () {
            $("body").keydown(function (e) {
                var curKey = e.which;
                if (curKey == 13) {
                    if ($(".identity_class").val() == 1) {
                        form_submit_en();
                    } else {
                        form_submit();
                    }
                    return false;
                }
            });
            $("#first_name").blur(function () {
                if (isEmpty("#first_name")) {
                    return false;
                }
            });
            $("#last_name").blur(function () {
                if (isEmpty("#last_name")) {
                    return false;
                }
            });
            $("#identity").blur(function () {
                if (!identityValid()) {
                    return false;
                }
            });
            $("#first_name_en").blur(function () {
                if (isEmpty("#first_name_en")) {
                    return false;
                }
            });
            $("#last_name_en").blur(function () {
                if (isEmpty("#last_name_en")) {
                    return false;
                }
            });
            $("#identity_en").blur(function () {
                if (isEmpty("#identity_en")) {
                    return false;
                }
            });
            var input_obj = "#first_name,#last_name,#identity,#first_name_en,#last_name_en,#identity_en";
            $(document).on("input propertychange", input_obj, function () {
                checkIdentifyForm();
            });
            $("#country").data("init", false);
            $('.s-blk-menu').on('click', 'li', function () {
                if ($("#country").data("init") == false) {
                     effect.divSelect('country');
                    $("#country").data("init", true);
                }
            });
            // 绑定上传事件

            $("div .s-upload").each(function (item) {
                $(this).bind("click",function () {
                    uploadClick($(this).attr("data-to"));
                })

            })

            // 绑定提交上传文件 uploadFile
            $("input[type='file']").each(function (item) {
                $(this).bind("change",function () {
                    uploadFile("#"+$(this).parent().attr("id"),"#"+$(this).attr("id"),this);
                })

            })

            $(".subm-zh").bind("click",function () {

                form_submit();
                
            })
            $(".subm-en").bind("click",function () {

                form_submit_en();

            })

        });

        function checkIdentifyForm() {
            var identity_class = $(".identity_class").val();
            var pass = 1;
            if (identity_class == 0) {
                var first_name = $("#first_name").val();
                var last_name = $("#last_name").val();
                var identity = $("#identity").val();
                var front_pic1 = $("#front_pic1").val();
                var back_pic1 = $("#back_pic1").val();
                var hand_pic1 = $("#hand_pic1").val();
                var result_identity = IdCardValid(identity);
                if (first_name.length == 0 || last_name.length == 0) {
                    pass = 0;
                } else if (identity.length == 0 || result_identity['pass'] == false) {
                    pass = 0;
                } else if (front_pic1.length == 0 || back_pic1.length == 0 || hand_pic1.length == 0) {
                    pass = 0
                }
            } else {
                var first_name_en = $("#first_name_en").val();
                var last_name_en = $("#last_name_en").val();
                var identity_en = $("#identity_en").val();
                var front_pic1 = $("#front_pic_en1").val();
                var back_pic1 = $("#back_pic_en1").val();
                var hand_pic1 = $("#hand_pic_en1").val();
                if (first_name_en.length == 0 || last_name_en.length == 0) {
                    pass = 0;
                } else if (identity_en.length == 0) {
                    pass = 0;
                } else if (front_pic1.length == 0 || back_pic1.length == 0 || hand_pic1.length == 0) {
                    pass = 0
                }
            }
            if (pass == 1) {
                $(".s-btn-wrap .s-btn:visible").removeClass("submitBtDisabled");
            } else {
                $(".s-btn-wrap .s-btn:visible").addClass("submitBtDisabled");
            }
        }

        function change_sex(sex) {
            if (sex == 1) {
                $("#sex0").removeClass("cur");
                $("#sex1").addClass("cur");
            } else {
                $("#sex1").removeClass("cur");
                $("#sex0").addClass("cur");
            }
            $("#sex").val(sex);
        }

        $("#mainland-tab").click(function () {
            change("mainland")
        });
        $("#otherarea-tab").click(function () {
            change("otherarea")
        });

        function change(id) {
            if (id == "otherarea") {
                $(".mainland").removeClass("cur");
                $(".otherarea").addClass("cur");
                $("#otherarea").removeClass("hide");
                $("#mainland").addClass("hide");
                $(".identity_class").val(1);
            } else {
                $(".otherarea").removeClass("cur");
                $(".mainland").addClass("cur");
                $("#mainland").removeClass("hide");
                $("#otherarea").addClass("hide");
                $(".identity_class").val(0);
            }
        }

        function isEmpty(id) {
            if ($(id).val().length == 0) {
                $(id).addClass("error").next().show().find(".tips").html(Lang.input_cant_empty);
                return true;
            } else {
                $(id).removeClass("error").next().hide();
                return false;
            }
        }

        function picEmpty(id) {
            if ($(id).val().length == 0) {
                $(id).next().show().find(".tips").html(Lang.input_cant_empty);
                return true;
            } else {
                $(id).next().hide();
                return false;
            }
        }

        function identityValid() {
            var id = "#identity";
            if ($(id).val().length == 0) {
                $(id).addClass("error").next().show().find(".tips").html(Lang.input_cant_empty);
                return false;
            } else {
                var result_valid = IdCardValid($(id).val());
                if (result_valid['pass'] == true) {
                    $(id).removeClass("error").next().hide();
                    return true;
                } else {
                    console.log(result_valid['msg']);
                    $(id).addClass("error").next().show().find(".tips").html(Lang.id_card_valid);
                    return false;
                }
            }
        }

        function isChinaName(name) {
            var pattern = /^[\u4E00-\u9FA5]{1,6}$/;
            return pattern.test(name);
        }

        function isChineseName(name) {
            var rule = /^[\u4E00-\u9FA5\uf900-\ufa2d·s]{1,6}$/;
            if (rule.test(name)) {
                return true;
            }
            return false;
        }

        function isCardNo(card) {
            var pattern = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
            return pattern.test(card);
        }

        function IdCardValid(code) {
            var city = {
                11: "北京",
                12: "天津",
                13: "河北",
                14: "山西",
                15: "内蒙古",
                21: "辽宁",
                22: "吉林",
                23: "黑龙江 ",
                31: "上海",
                32: "江苏",
                33: "浙江",
                34: "安徽",
                35: "福建",
                36: "江西",
                37: "山东",
                41: "河南",
                42: "湖北 ",
                43: "湖南",
                44: "广东",
                45: "广西",
                46: "海南",
                50: "重庆",
                51: "四川",
                52: "贵州",
                53: "云南",
                54: "西藏 ",
                61: "陕西",
                62: "甘肃",
                63: "青海",
                64: "宁夏",
                65: "新疆",
                71: "台湾",
                81: "香港",
                82: "澳门",
                91: "国外"
            };
            var row = {'pass': true, 'msg': '验证成功'};
            if (!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|[xX])$/.test(code)) {
                row = {'pass': false, 'msg': '身份证号格式错误'};
            } else if (!city[code.substr(0, 2)]) {
                row = {'pass': false, 'msg': '身份证号地址编码错误'};
            } else {
                if (code.length == 18) {
                    code = code.split('');
                    var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
                    var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
                    var sum = 0;
                    var ai = 0;
                    var wi = 0;
                    for (var i = 0; i < 17; i++) {
                        ai = code[i];
                        wi = factor[i];
                        sum += ai * wi;
                    }
                    if (parity[sum % 11] != code[17].toUpperCase()) {
                        row = {'pass': false, 'msg': '身份证号校验位错误'};
                    }
                }
            }
            return row;
        }

        function form_submit() {
            if (isEmpty("#first_name") || isEmpty("#last_name")) {
                console.log("first_name last_name empty");
                scroll_error_msg();
                return false;
            }
            if (!isChineseName($("#first_name").val())) {
                console.log("first_name is must in chinese");
                $("#first_name").addClass("error").next().show().find(".tips").html(Lang['first_name_is_chinese']);
                scroll_error_msg();
                return false;
            }
            if (!isChineseName($("#last_name").val())) {
                console.log("last_name is must in chinese");
                $("#last_name").addClass("error").next().show().find(".tips").html(Lang['last_name_is_chinese']);
                scroll_error_msg();
                return false;
            }
            if (!identityValid()) {
                console.log("identityValid fail");
                scroll_error_msg();
                return false;
            }
            if (picEmpty("#front_pic1") || picEmpty("#back_pic1") || picEmpty("#hand_pic1")) {
                console.log("front_pic1 back_pic1 hand_pic1 empty");
                scroll_error_msg();
                return false;
            }
            $.post("/member/security/addUserAuth", $("#form").serialize(), function (data) {

                if (data.success) {
                    layer.msg(data.msg, {time: 2000, icon: 6});
                    window.location.href = "/ex/member/user";
                } else {
                    layer.msg(data.msg, {time: 2000, icon: 7});
                }

                U.response(data);
            });
        }

        function scroll_error_msg() {
            var position = $(".idErrorTip:visible:eq(0)").offset();
            $(window).scrollTop(position.top);
        }

        function form_submit_en() {
            if (isEmpty("#first_name_en") || isEmpty("#last_name_en")) {
                console.log("first_name_en last_name_en empty");
                scroll_error_msg();
                return false;
            }
            if (isEmpty("#identity_en")) {
                console.log("identity_en fail");
                scroll_error_msg();
                return false;
            }
            if (picEmpty("#front_pic_en1") || picEmpty("#back_pic_en1") || picEmpty("#hand_pic_en1")) {
                console.log("front_pic_en1 back_pic_en1 hand_pic_en1 empty");
                scroll_error_msg();
                return false;
            }
            $.post("/member/security/addUserAuth", $("#form_en").serialize(), function (data) {

                if (data.success) {
                    layer.msg(data.msg, {time: 2000, icon: 6});
                    window.location.href = "/ex/member/user";
                } else {
                    layer.msg(data.msg, {time: 2000, icon: 7});
                }
                U.response(data);
            });
        }

        function uploadClick(id) {
            $(id).click();
        }

        function uploadFile(formid, id, obj) {
            var filepath = $(obj).val();
            var extStart = filepath.lastIndexOf(".");
            var ext = filepath.substring(extStart, filepath.length).toUpperCase();
            if (ext != ".PNG" && ext != ".JPG" && ext != ".JPEG") {
                console.log("图片格式不合法");
                layer.msg(Lang.images_ext_limit, {icon: 2, time: 2000});
                return false;
            }
            var file_size = 0;
            var size_limit = 5;
            if (/msie/.test(navigator.userAgent.toLowerCase())) {
                var img = new Image();
                img.src = filepath;
                while (true) {
                    if (img.fileSize > 0) {
                        if (img.fileSize > size_limit * 1024 * 1024) {
                            console.log("图片不能大于5M");
                            layer.msg(Lang.images_size_limit, {icon: 2, time: 2000});
                            return false;
                        }
                        break;
                    }
                }
            } else {
                file_size = obj.files[0].size;
                var size = file_size / 1024;
                if (size > 1024 * size_limit) {
                    console.log("图片不能大于5M");
                    layer.msg(Lang.images_size_limit, {icon: 2, time: 2000});
                    return false;
                }
                filepath = URL.createObjectURL(obj.files[0]);
            }
            var index = layer.load();
            var options = {
                type: "POST", url: "/upload/file", dataType: "json", success: function (data) {
                    layer.close(index);
                    if (!data.success) {
                        layer.msg(data['msg'], {icon: 2, time: 2000});
                    } else {
                        $(id + "_show").attr("src", filepath);
                        $(id + "1").val(data['result']);
                        checkIdentifyForm();
                    }
                }
            };
            $(formid).ajaxForm(options).submit();
        }
    }

    /**
     * 实名认证
     */
    function userAuth() {
        userAuthView();

        var options = {}

        /**
         * 表单校验规则
         */
        var rules = {
            rules: {
                realName: {
                    required: true,
                    minlength: 2,
                    maxlength: 20,
                },
                userCardNumber: {
                    required: true,
                    minlength: 10,
                    maxlength: 25,

                },
                userCardFront: {
                    required: true

                },
                userCardBack: {
                    required: true

                },
                userCardAll: {
                    required: true

                }
            },
            messages: {}
        };
//表单提交
        mVad({
            forms: $('.form'),
            tip: '.form_tip',
            rules: rules,
            postData: function (data) {

                return data;
            },
            formSubmit: function (_option, form) {

                core.save(_option.ajaxData).then(function (data) {
                    if (data.success) {
                        swal.swalBase(data.msg, "", "success");
                    } else {
                        var errInfo = "";
                        if (data.data != undefined) {
                            errInfo = data.data.join("");
                        }
                        swal.swalError(data.msg, errInfo);
                    }
                    _option.callback(data, form);
                }).fail(function (jqXHR, textStatus) {
                    swal.swalError(textStatus, jqXHR.status);
                    _option.callback({}, form);
                });
            }
        });
    }

    /**
     * 设置头像
     */
    function userAvatar() {

        /**
         * 表单校验规则
         */
        var rules = {
            rules: {
                userAvatar: {
                    required: true
                }
            },
            messages: {}
        };
//表单提交
        mVad({
            forms: $('.form'),
            tip: '.form_tip',
            rules: rules,
            postData: function (data) {

                return data;
            },
            formSubmit: function (_option, form) {

                core.save(_option.ajaxData).then(function (data) {
                    if (data.success) {
                        swal.swalBase(data.msg, "", "success");
                    } else {
                        var errInfo = "";
                        if (data.data != undefined) {
                            errInfo = data.data.join("");
                        }
                        swal.swalError(data.msg, errInfo);
                    }
                    _option.callback(data, form);
                }).fail(function (jqXHR, textStatus) {
                    swal.swalError(textStatus, jqXHR.status);
                    _option.callback({}, form);
                });
            }
        });
    }

    /**
     * googleValid
     * @returns {boolean}
     */
    function googleValid() {
        if ($.trim($("#google_code").val()).length == 0) {
            $("#error").show();
            $("#error_show").html(Lang.input_cant_empty);
            $("#google_code").addClass("error");
            return false;
        } else {
            $("#error").hide();
            $("#google_code").removeClass("error");
            return true;
        }
    }
    function checkGoogleAuthForm() {
        var password = $.trim($("#password").val());
        var google_secret = $.trim($("#google_secret").val());
        var secret = $.trim($("#secret").val());
        var google_code = $.trim($("#google_code").val());
        var pass = 1;
        if (google_secret.length == 0 || google_secret != secret) {
            pass = 0;
        } else if (password.length == 0) {
            pass = 0;
        } else if (google_code.length == 0) {
            pass = 0;
        }
        if (pass == 1) {
            $("#step4 .submitBt").removeClass("submitBtDisabled");
        } else {
            $("#step4 .submitBt").addClass("submitBtDisabled");
        }
    }
    function secretValid() {
        if ($.trim($("#google_secret").val()).length == 0) {
            $("#error").show();
            $("#error_show").html(Lang.input_cant_empty);
            $("#google_secret").addClass("error");
            return false;
        } else {
            if ($.trim($("#google_secret").val()) != $.trim($("#secret").val())) {
                $("#error").show();
                $("#error_show").html(Lang.google_secret_16_error);
                $("#google_secret").addClass("error");
                return false;
            } else {
                $("#error").hide();
                $("#google_secret").removeClass("error");
                return true;
            }
        }
    }
    /**
     * google 认证 关闭认证页面用到效果
     */
    function googleAuthView() {
        $(document).ready(function () {
            $("#google_secret").blur(function () {
                secretValid();
            });
            $("#password").blur(function () {
                if (U.isEmpty("#password")) {
                    return false;
                }
            });
            $("#google_code").blur(function () {
                googleValid();
            });
            $(document).on("blur", "#password,#google_code", function () {
                if ($(this).parents("li.input").find(".errorTip").length > 0) {
                    var content = $(this).val();
                    if (content.length == 0) {
                        $(this).addClass("error");
                        $(this).parents("li.input").find(".errorTip .error_show").text(Lang.input_cant_empty);
                        $(this).parents("li.input").find(".errorTip").removeClass("hide");
                        return false;
                    } else {
                        $(this).removeClass("error");
                        $(this).parents("li.input").find(".errorTip").addClass("hide");
                        return false;
                    }
                }
            });
            $(document).on("input propertychange", "#google_secret,#password,#google_code", function () {
                if ($("#google_secret").length > 0) {
                    checkGoogleAuthForm();
                } else {
                    var password = $.trim($("#password").val());
                    var google_code = $.trim($("#google_code").val());
                    var pass = 1;
                    if (password.length == 0 || google_code.length == 0) {
                        pass = 0;
                    }
                    if (pass == 1) {
                        $("#form .submitBt").removeClass("submitBtDisabled");
                    } else {
                        $("#form .submitBt").addClass("submitBtDisabled");
                    }
                }
            });
        });
    }
    /**
     * 关闭google 认证
     */
    function  closeGoogleAuth() {

        googleAuthView();
        function close_valid() {
            if ($("#password").val().length == 0) {
                $("#password").addClass("error");
                $("#password").parents("li.input").find(".errorTip .error_show").text(Lang.input_cant_empty);
                $("#password").parents("li.input").find(".errorTip").removeClass("hide");
                return false;
            }
            if ($("#google_code").val().length == 0) {
                $("#google_code").addClass("error");
                $("#google_code").parents("li.input").find(".errorTip .error_show").text(Lang.input_cant_empty);
                $("#google_code").parents("li.input").find(".errorTip").removeClass("hide");
                return false;
            }

        }

        /**
         * 表单校验规则
         */
        var rules = {
            rules: {

                password: {
                    required: true,
                    minlength:4,

                },
                code: {
                    required: true,
                    maxlength: 6,
                }
            },
            messages: {}
        };
//表单提交
        mVad({
            forms: $('.form'),
            tip: '.form_tip',
            rules: rules,
            beforeValidation:function(){
                if(close_valid() && !U.isEmpty("#password") && googleValid()){
                    return true;
                }
            },
            postData: function (data) {

                var _data = [];
                $.each(data, function(i, field){
                    field['name'] != 'password' ? _data.push(field) : _data.push({'name':'password','value':mMd5.hbmd5(field['value'])});
                });
                return _data;
            },
            formSubmit: function (_option, form) {


                core.save(_option.ajaxData).then(function (data) {

                    if (data.success) {
                        swal.swalSuccess(data.msg, "",function () {
                            window.location.href = "/ex/member/user";
                        });
                    } else {
                        var errInfo = "";
                        if (data.data != undefined) {
                            errInfo = data.data.join("");
                        }
                        swal.swalError(data.msg, errInfo);
                    }
                    _option.callback(data, form);
                }).fail(function (jqXHR, textStatus) {
                    swal.swalError(textStatus, jqXHR.status);
                    _option.callback({}, form);
                });
            }
        });
    }
    /**
     * google实名认证
     */
    function googleAuth() {
        googleAuthView();

        $(document).ready(function () {
            $("#qrcode").qrcode({render: "canvas", width: 110, height: 110, text: U.toUtf8($("#qrcode_text").val()),});
        });
        /**
         * 表单校验规则
         */
        var rules = {
            rules: {

                password: {
                    required: true,
                    minlength:4,
                    maxlength: 20,

                },
                googleCode: {
                    required: true,
                    maxlength: 6,
                },
                secret: {
                    required: true,
                    maxlength: 16,

}
},
messages: {}
};
//表单提交
mVad({
    forms: $('.form'),
    tip: '.form_tip',
    rules: rules,
    beforeValidation:function(){

        if(secretValid() && !U.isEmpty("#password") && googleValid()){
            return true;
        }
    },
    postData: function (data) {
        var _data = [];
        $.each(data, function(i, field){
            field['name'] != 'password' ? _data.push(field) : _data.push({'name':'password','value':mMd5.hbmd5(field['value'])});
        });
        return _data;
    },
    formSubmit: function (_option, form) {

        core.save(_option.ajaxData).then(function (data) {
            if (data.success) {
                swal.swalSuccess(data.msg, "",function () {
                    window.location.href = "/ex/member/user";
                });
            } else {
                var errInfo = "";
                if (data.data != undefined) {
                    errInfo = data.data.join("");
                }
                swal.swalError(data.msg, errInfo);
            }
            _option.callback(data, form);
        }).fail(function (jqXHR, textStatus) {
            swal.swalError(textStatus, jqXHR.status);
            _option.callback({}, form);
        });
    }
});
}


exports.userAvatar = userAvatar;
exports.googleAuth = googleAuth;
exports.closeGoogleAuth=closeGoogleAuth;
exports.userAuth = userAuth;
exports.setEmail = setEmail;
exports.globalInit = globalInit;
exports.updatePassword = updatePassword;
exports.setTradePassword = setTradePassword;
exports.addMobileCode = addMobileCode;
exports.sendMobileCode = sendMobileCode;
exports.registerPost = registerPost;


});