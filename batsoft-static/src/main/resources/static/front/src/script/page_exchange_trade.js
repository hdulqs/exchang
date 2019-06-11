/**
 *<p>page_exchange_eventLog.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {
    var  mVad      = require('./module_validator'), //表单校验
        swal      = require('./module_swal'), //弹窗 alert
        E = require('./module_effect'), //特效
        tpl = require('./module_tpl'),
        table = require('./module_table'),
        U = require('./module_utils'),
        core      =require('./module_core');

    function init(){
        slider();
        entrust();
    }

    /**
     * slider
     */
    function slider(){
        jQuery("#slider5").slider({
            value:100,
            animate: true,
            min: 0,
            max: 500,
            step: 50,
            range: "min",
            slide: function(event, ui) {
                jQuery("#donation").val(ui.value);
            }
        });

        jQuery("#donation").val(jQuery("#slider5").slider("value"));
        jQuery("#donation").blur(function() {
            jQuery("#slider5").slider("value", jQuery(this).val());
        });
    }

    /**
     * 委托
     */
    function entrust(){
        formSubmit();
    }

    /**
     * 数据提交
     */
    function formSubmit(){

// 表单校验规则
        var rules = {
            rules: {

                type: {
                    required: true,
                    maxlength: 2,

                },
                symbol: {
                    required: true,
                    maxlength: 20,

                },
                priceSymbol: {
                    required: true,
                    maxlength: 20,

                },

                price: {
                    required: true,
                    maxlength: 20,
                    number: true,

                },
                coins: {
                    required: true,
                    maxlength: 20,
                    number: true,

                },

                category: {
                    required: true,
                    maxlength: 5,
                    number: true,

                }
            },
            messages: {}
        };
//表单提交
        mVad({
            forms:$('.form'),
            tip:'.form_tip',
            rules:rules,
            formSubmit:function(_option,form){
                core.save(_option.ajaxData).then(function(data){
                    if(data.success){
                        //swal.swalBase(data.msg,"","success");
                        E.toastr({title:"",content:data.msg});
                    }else{
                        var errInfo="";
                        if( data.data!=undefined){
                            errInfo=data.data.join("");
                        }
                        E.toastr({title:data.msg,content:errInfo,type:"error"});
                       // swal.swalError(data.msg,errInfo);
                    }
                    _option.callback(data,form);
                }).fail(function(jqXHR, textStatus){
                   // swal.swalError(textStatus,jqXHR.status);
                    E.toastr({title:"",content:textStatus,type:"error"});
                    _option.callback({},form);
                });
            }
        });
    }




    exports.init=init;
});