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
        v=require('./module_validate'),
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
        var userInfo=GLOBAL.user;
        userInfo.oldLoginTime=U.dateFormat(new Date(userInfo.oldLoginTime),"yyyy-MM-dd hh:mm:ss")
        tpl.render({"tplId": "#userInfoTmpl", "renderId":"#userInfoRender", "data": userInfo});
        effect.piechart();
        //登陆日志列表
        core.list({url:BASE_PATH+ "/findLogs"}).then(function (data) {
            tpl.render({"tplId": "#memberLoginTmpl", "renderId": "#memberLoginRender", "data": data});
        })
    }
    /**
     * 用户首页
     */
    function init() {
        effect.countTo();
        effect.pickers(searchByDate);
        effect.toggle();
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
            url:BASE_PATH+ '/member/log/list',
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
     * 按照时间查询
     * @param start
     * @param end
     */
    function searchByDate(start, end) {
        alert(start + "--" + end);
    }

    /**
     * 账户安全
     */
    function setTradePassword(){
        /**
         * 设置资金密码提交
         * 表单校验规则
         */
        var rules = {
            rules: {

                password: {
                    required: true,
                    password:true

                },
                confirm_password:{
                    required: true,
                    password:true,
                    equalTo:"#password"
                }
            },
            messages: {
            }
        };
//表单提交
        mVad({
            forms: $('.form'),
            tip: '.form_tip',
            rules: rules,
            postData: function (data) {
                var _data = [];
                $.each(data, function (i, field) {
                    if(field['name'] != 'confirm_password'){
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
     * 修改资金密码/登录密码
     */
    function updatePassword(){
        /**
         * 修改资金密码提交
         * 表单校验规则
         */
        var rules = {
            rules: {
                oldPassword: {
                    required: true,
                },
                password: {
                    required: true,
                    password:true

                },
                confirm_password:{
                    required: true,
                    password:true,
                    equalTo:"#password"
                }
            },
            messages: {
            }
        };
//表单提交
        mVad({
            forms: $('.form'),
            tip: '.form_tip',
            rules: rules,
            postData: function (data) {
                var _data = [];
                $.each(data, function (i, field) {
                    if(field['name'] != 'confirm_password'){

                        if(field['name'] != 'password'&&field['name'] != 'oldPassword'){
                            _data.push(field)
                        }else{
                            _data.push({
                                'name': field['name'],
                                'value': mMd5.hbmd5(field['value'])
                            });
                        }
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
     * 绑定邮箱
     */
    function setEmail(){
        /**
         * 绑定邮箱
         * 表单校验规则
         */
        var rules = {
            rules: {
                email: {
                    required: true,
                    email:true
                },
                code: {
                    required: true,
                    minlength: 6,
                    maxlength: 6,

                }
            },
            messages: {
            }
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
     * 实名认证
     */
    function userAuth(){
        var options={
        }
        F.dropzoneUpload(options);
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
            messages: {
            }
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
    function userAvatar(){

        /**
         * 表单校验规则
         */
        var rules = {
            rules: {
                userAvatar: {
                    required: true
                }
            },
            messages: {
            }
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
     * google实名认证
     */
    function googleAuth(){
        /**
         * 表单校验规则
         */
        var rules = {
            rules: {
                ga: {
                    required: true,
                    minlength: 2,
                    maxlength: 10,
                }
            },
            messages: {
            }
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

    exports.userAvatar=userAvatar;
    exports.googleAuth=googleAuth;
    exports.userAuth=userAuth;
    exports.setEmail=setEmail;
    exports.globalInit=globalInit;
    exports.updatePassword=updatePassword;
    exports.setTradePassword = setTradePassword;
    exports.init = init;
    exports.logList = logList;
});