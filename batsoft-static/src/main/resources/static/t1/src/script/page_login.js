/**
 *<p>page_login.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {

    var mMd5 = require('./module_md5'),
        mVad = require('./module_validator'), //表单校验
        core = require('./module_core');

    /**
     * 初始化登录页
     */

    function init() {

        var rules = {
            rules: {
                userName: {
                    required: true,
                    maxlength: 16,

                },
                password: {
                    required: true,
                    maxlength: 100,

                }
            },
            messages: {}
        };
        //是否图形验证通过
        var verity = false;
        //验证码初始化
        var captchaIns;
        initNECaptcha({
            captchaId: '87cc815627084c41bb8cb582e9378997',
            element: '#captcha',
            mode: 'float',
            width: 320,
            onVerify: function (err, data) {
                if(err!=null){
                    $(".tip").removeClass("p-hide").html($('#validCodeError').val());
                    verity = false;
                }else{
                    $(".tip").addClass("p-hide");
                    verity = true;
                    $("#captcha").css("display", "none");
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
//登录表单
        mVad({
            forms: $('.form'),
            tip: '.tip',
            rules: rules,
            beforeSubmit:function(_form){
                if(!verity) {
                   $(".tip").removeClass("p-hide").html($('#validCode').val());
                    return false;
                }
                return true;
            },
            postData:function(data){
                var _data = [];
                $.each(data, function(i, field){
                    field['name'] != 'password' ? _data.push(field) : _data.push({'name':'password','value':mMd5.hbmd5(field['value'])});
                });
                var rememberMe =$('#rememberMe').is(':checked');
                _data.push({"name":"rememberMe","value":rememberMe})
                var redirectUrl = $('#redirectUrl').val();
                if(redirectUrl != "" && redirectUrl != null){
                    _data.push({"name":"redirectUrl","value":redirectUrl})
                }
                return _data;
            },
            formSubmit: function (_option, form) {
                core.save(_option.ajaxData).then(function (data) {
                    if (data.success) {
                        if(data.successURL == "shop"){
                            window.location.href= "https://shop.bttmall.com?token="+data.shopToken;
                        }else{
                            // window.location.href= data.successURL;
                            window.location = BASE_PATH;

                        }
                        localStorage.setItem("shopToken",data.shopToken);
                        $(_option.tip).addClass("p-hide").html(data.msg);
                    } else {
                        $("#captcha").css("display","block");
                        $(_option.tip).removeClass("p-hide").html(data.msg);
                        captchaIns.refresh();
                    }
                    _option.callback(data, form);
                }).fail(function (jqXHR, textStatus) {
                    $(_option.tip).removeClass("p-hide").html(data.msg);
                    _option.callback({}, form);
                });
            }

        });
    }


    exports.init = init;
});