/**
 * Created by Bat Admin on 2015/6/1.
 */
define(function (require, exports, module) {

    var _default = {
        //表单对象
        side:"left",
        closeOnClick:false,
        pushBody:false,
        onBeforeOpen: function () {
            $(".col-lg-0").attr("class","col-lg-2");
            $(".col-lg-12").attr("class","col-lg-10");
        },
        onBeforeClose: function () {
            $(".col-lg-2").attr("class","col-lg-0");
            $(".col-lg-10").attr("class","col-lg-12");
        }};
    function init(options){
        var _option    = $.extend(_default, options);
        $('#bootSideMenu').BootSideMenu(_option);
    }

    exports.init    = init;


});


