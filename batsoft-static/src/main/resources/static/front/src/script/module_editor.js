/**
 * Created by Bat Admin on 2015/6/1.。
 */
define(function (require, exports, module) {

    /**
     * init 初始化编辑器
     * @param options
     * @returns {*}
     */
    function init(options) {
        var _default = {
                container: ".summernote"
            },
            _option = $.extend(_default, options);
        $(_option.container).summernote();
    }

    /**
     * getContent 获取内容
     * @param options
     * @returns {*}
     */
    function getContent(options) {
        var _default = {
                container: ".summernote"
            },
            _option = $.extend(_default, options);
       return $(_option.container).summernote('code');
    }

    exports.init = init;
    exports.getContent = getContent;
});


