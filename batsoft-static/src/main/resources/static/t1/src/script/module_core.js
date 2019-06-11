/**
 * Created by Bat Admin on 2015/6/1. 。
 */
define(function (require, exports, module) {

    var A = require('./module_ajax'),
        E = require('./module_effect'),
        U=require('./module_utils');
    /**
     * 是否登录
     */
    function isLogin() {
        if (!$.isEmptyObject(GLOBAL.user)) {
            return  true;
        }
        return false;
    }
    /**
     *是否具有权限
     * @param permission
     */
    function hasPermission(permission){
       return U.strContains(Global.perm,permission)
    }

    /**
     * 错误处理 主要处理全局登录超时和权限错误
     * @param data
     */
    function error(data){
        if(data!=undefined){
            if(data.code="401.1"){
                var timeOut=3000;
                E.toastr({title:timeOut/1000+"秒后跳转到登录页...",content:data.msg});
                setTimeout(function() {
                    window.location.href= data.data;
                }, timeOut);
            }
        }
    }


    /**
     * loadPage 页面加载
     * @param options container:容器 loading：加载前信息 loadUrl：加载url
     * @param fn回调函数
     * @returns {*}
     */
    function loadPage(options,fn){
        var op   = options || {};
        $(op.container).html(op.loading).load(op.loadUrl,function(response,status,xhr){
            if(response.startsWith("{")){
               var data= JSON.parse(response);
                error(data);
            }
            if(op.callBack){
                if(options.data){
                    op.callBack(options.data);
                }else{
                    op.callBack();
                }

            }
            //页面回调事件
            if(fn){
                fn();
            }

        });

    }


    /**
     * save
     * @param options
     * @returns {*}
     */
    function save(options){
        var op   = options || {},
            Fn;
        Fn =  new A({"url":op.url,"data":op.data,"type":"POST","datatype":op.datatype,"processData":op.processData,"contentType":op.contentType},function(data){
            var _code = data.code;

            switch (_code){
                case 'loading':
                    // loading
                    $("#"+op.msg).html("加载中...");
                    break;

                case 'error':
                    error(data.data);
                    break;
                case 'success':
                    $("#"+op.msg).html("");
                //to do

            }
        });
        return Fn;
    }

    /**
     * remove
     * @param options
     * @returns {*}
     */
    function remove(options){

        var op   = options || {},
            Fn;
        Fn =  new A({"url":op.url,"data":op.data,"type":op.type},function(data){
            var _code = data.code;

            switch (_code){
                case 'loading':
                    // loading
                    $("#"+op.msg).html("删除中...");
                    break;

                case 'error':
                    error(data.data);
                    break;
                case 'success':
                    $("#"+op.msg).html("");
                //to do

            }
        });
        return Fn;
    }
    /**
     * list
     * @param options
     * @returns {A|*}
     */
     function list(options){
        var op   = options || {},
            Fn;
            Fn =  new A({"url":op.url,"data":op.data,"type":op.type},function(data){
            	var _code = data.code;

                switch (_code){
                    case 'loading':
                        // loading
                        $("#"+op.msg).html("加载中...");
                        break;

                    case 'error':
                        error(data.data);
                        break;
                    case 'success':
                        $("#"+op.msg).html("");
                       //to do

                }
            });
        return Fn;
    }

    /**
     * get
     * @param options
     * @returns {A|*}
     */
    function get(options){
        var op   = options || {},
            Fn;
        Fn =  new A({"url":op.url,"data":op.data,"type":"GET"},function(data){
            var _code = data.code;
            switch (_code){
                case 'loading':
                    // loading
                    $("#"+op.msg).html("加载中...");
                    break;
                case 'error':
                    error(data.data);
                    break;
                case 'success':
                    $("#"+op.msg).html("");
                //to do
            }
        });
        return Fn;
    }
    /**
     * post
     * @param options
     * @returns {A|*}
     */
    function post(options){
        var op   = options || {},
            Fn;
        Fn =  new A({"url":op.url,"data":op.data,"type":"POST"},function(data){
            var _code = data.code;
            switch (_code){
                case 'loading':
                    // loading
                    $("#"+op.msg).html("加载中...");
                    break;
                case 'error':
                    error(data.data);
                    break;
                case 'success':
                    $("#"+op.msg).html("");
                //to do
            }
        });
        return Fn;
    }
    exports.loadPage    =loadPage;
    exports.save    = save;
    exports.remove    = remove;
    exports.list    = list;
    exports.get     =get;
    exports.post     =post;
    exports.hasPermission=hasPermission;
    exports.isLogin=isLogin;

});


