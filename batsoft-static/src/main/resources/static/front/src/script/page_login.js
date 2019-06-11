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

                validCode: {
                    required: true,
                    minlength:4,
                    maxlength: 4,

                },
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

        



//登录表单
        mVad({
            forms: $('.form'),
            tip: '.tip',
            rules: rules,
            postData:function(data){
                var _data = [];
                $.each(data, function(i, field){
                    field['name'] != 'password' ? _data.push(field) : _data.push({'name':'password','value':mMd5.hbmd5(field['value'])});
                });
                var rememberMe =$('#rememberMe').is(':checked');
                _data.push({"name":"rememberMe","value":rememberMe})
                return _data;
            },
            formSubmit: function (_option, form) {
                core.save(_option.ajaxData).then(function (data) {
                    if (data.success) {
                        window.location.href= data.successURL;
                    } else {
                        $(_option.tip).removeClass("p-hide").html(data.msg);
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