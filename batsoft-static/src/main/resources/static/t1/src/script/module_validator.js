/**
 * Created by Bat Admin on 2017/4/6.
 */
;(function(window){

    var BitValidator = function(options){

        // 防重提交 按钮
        var load_btn=null;
        var _default = {
                //表单对象
                forms:null,
                /*默认设置*/
                /*表单验证前*/
                beforeSend:function(_form){
                    // to to
                },
                /*表单验证中未通过*/
                process:null,
                rules:{},
                /*表单验证前*/
                beforeValidation:function(){

                },
                /*表单验证通过提交前*/
                beforeSubmit:function(_form){
                    var  _btn      = _form.find(':submit');
                    load_btn = Ladda.create(_btn[0]);
                    load_btn.start();
                    _btn.prop('disabled',true);
                },
                /*表单每次提交*/
                formSubmit:null,
                /*提交后回调*/
                callback:function(_data,_form){
                    var  _btn      = _form.find(':submit');
                    load_btn.stop();
                    _btn.prop('disabled',false);
                },
                /*单项校验返回结果:[元素,1:为空 2:未通过]*/
                single:null,
                /*change 返回当前DOM元素*/
                changes:null,
                /*是否忽略隐藏元素*/
                ignoreHidden:true,
                /*ajax方式提交*/
                ajaxPost:true,
                /*ajax提交出错*/
                ajaxError:null,
                /*ajax Data*/
                ajaxData:null,
                /*是否即时验证*/
                instant:false,
                /*自定义提交数据*/
                postData: null,
                /*tip提示*/
                tip:'',
                /*验证表单错误后是否获取input焦点*/
                is_focus:true,
                dataType:{}
            },

            _option    = $.extend(_default, options),
            _form      = $(options.forms),

            _init      = {

                /*初始化*/
                fn_init:function(obj){

                    /*按钮初始化*/
                    var _submit = obj.find(':submit');
                    _submit.prop('disabled') && !_submit.data('disabled') && _submit.prop('disabled',false);
                    /*验证前执行*/
                    _option.beforeSend && _option.beforeSend(obj);

                    /**
                     * 错误提示位置
                     * @param error
                     * @param element
                     */
                    _option.rules.errorPlacement=function(error,element) {
                        if(element.next().length>0) {
                            error.insertAfter(element.parent("div"));
                        }else{
                            error.insertAfter(element);
                        }
                    }
                    // jquery validate
                    obj.validate(_option.rules);

                    // reset validate
                    obj.find(":reset").click(function(){
                        obj.validate().resetForm();
                    });
                    /*提交时校验*/
                    obj.submit(function(e){
                        e.preventDefault();

                        /*表单验证前 处理方法*/
                        _option.beforeValidation && _option.beforeValidation($(this));


                        if(obj.validate(_option.rules).form()){
                        /*表单验证通过*/
                        var isBeforeSubmit = _option.beforeSubmit && _option.beforeSubmit($(this));

                        if(isBeforeSubmit===false){
                            return false
                        }
                        /*是否使用ajax方式提交*/
                        if(_option.ajaxPost){
                            /*ajax提交*/
                             _init.fn_ajax($(this));
                            return false
                        }else{

                        }

                         }else{
                        /*表单验证未通过*/
                        // _option.process && _option.process();
                         return false
                         }
                    });

                },

                /**ajax表单提交*/
                fn_ajax:function(obj){
                    var _url,_data,_method;
                    _url = obj.attr('action');
                    _method = obj.attr('method') || 'post';
                    _data = _option.postData ?  _option.postData(obj.serializeArray()) : obj.serializeArray();
                    var _async = true;
                    if(obj.attr('data-async') == 1){ //如果form中有data-async 这个ajax请求为同步请求
                        _async = false;
                    }


                    _option.ajaxData={"data":_data,"url":_url,"type":_method,"async":_async};
                    _option.formSubmit(_option,obj);

                },

            };
        /*表单走起*/
        if(!!_form &&_form.length>0){

            _form.each(function(){
                var _this = $(this);
                _init.fn_init(_this);
            });

        }
    };
    "function" == typeof define ? define(function() {
        return BitValidator
    }) : "undefined" != typeof exports ? module.exports = BitValidator : window.BitValidator = BitValidator
})(window);