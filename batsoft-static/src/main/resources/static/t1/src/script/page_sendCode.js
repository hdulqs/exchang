/**
 *<p>page_sendCode.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {

    var U = require('./module_utils'),
        tpl = require('./module_tpl'),
        core = require('./module_core');
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
     * 登录用户短信验证
     */
    function sendInnerMobileCode() {
        var userInfo = GLOBAL.user;
        tpl.render({"tplId": "#mobleInfoTemp", "renderId": "#mobleInfoRander", "data": userInfo});
        // 发送验证码
        $(".sendCode").on("click", function () {
            var mobile_v = $(".form").validate().element($("#userName"));
            var validCode_v = $(".form").validate().element($("#validCode"));
            if (mobile_v && validCode_v) {
                $(this).attr("disabled", "true");
                var options={"url":BASE_PATH+"/sendCode","data":{"mobile":$("#userName").val(),"validCode":$("#validCode").val(),"areaCode":$("#areaCode").val()}}
                core.get(options).then(function (data) {
                    if(data.success){
                        U.timer(data.data.codeTimeOut,$(".sendCode"));
                        $(".tip").addClass("p-hide").html("");
                    }else {
                        $(".tip").removeClass("p-hide").html(data.msg);
                    }
                }).fail(function (jqXHR, textStatus) {
                    $(".tip").removeClass("p-hide").html("网络异常");
                });
            }else{
                $(".tip").removeClass("p-hide").html("请确保手机号和图形验证码正确");
            }
        })


    }
    /**
     * 初始化发送邮件验证码
     */
    function sendEmailCode() {
        // 发送验证码
        $(".sendCode").on("click", function () {
            var email_v = $(".form").validate().element($("#email"));
            if (email_v) {
                $(this).attr("disabled", "true");
                var options={"url":BASE_PATH+"/member/security/sendEmail","data":{"email":$("#email").val()}}
                core.post(options).then(function (data) {
                    if(data.success){
                        U.timer(data.data.codeTimeOut,$(".sendCode"));
                        $(".tip").addClass("p-hide").html("");
                    }else {
                        $(".tip").removeClass("p-hide").html(data.msg);
                    }
                }).fail(function (jqXHR, textStatus) {
                    $(".tip").removeClass("p-hide").html("网络异常");
                });
            }
        })


    }


    exports.sendInnerMobileCode = sendInnerMobileCode;
    exports.sendEmailCode = sendEmailCode;
});