/**
 * Created by Bat Admin on 2015/6/1. 1
 */
define(function (require, exports, module) {

    /**
     *
     * @param title
     * @param text
     * @param state  "success"  "error" "warning"
     */
    function datepicker(options){
        var _default = {
            todayBtn: "linked",
            autoclose: true,
            format: "yyyy-mm-dd",
            container_css:".date-control"
        },
        _option    = $.extend(_default, options);

        $(_option.container_css).datepicker(_option);
    }


    exports.datepicker    = datepicker;


});


