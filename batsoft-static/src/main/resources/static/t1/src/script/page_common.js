/**
 * Created by Bat Admin on 2015/6/1.
 */
define(function (require, exports, module) {

    var A = require('./module_ajax'),
        tpl = require('./module_tpl'),
        U = require('./module_utils'),
        core = require('./module_core');



    /**
     * 加载主导航
     * @param options
     */
    function loadTopMenu(options) {

        var op = options || {};
        var data = JSON.parse(GLOBAL.config.webNav);
        var navData = [];
        for (i in data) {
            if (data[i].remark1 == 0) {
                navData.push(data[i]);
            }
        }
        // 数据渲染
        tpl.render({"tplId": "#topNavTmpl", "renderId": ".top-nav", "data": navData});

    }

    /**
     * 加载用户导航
     * @param options
     */
    function loadUserMenu(options) {
        var op = options || {};
        var data = JSON.parse(GLOBAL.config.webNav);
        var navData = [];
        for (i in data) {
            if (data[i].remark1 == 1) {
                navData.push(data[i]);
            }
        }
        // 数据渲染
        tpl.render({"tplId": "#userNavTmpl", "renderId": "#userNav", "data": navData});

        $("#userNav a").each(function(){
            $(this).removeClass("active");
            var href=$(this).attr('href');
            if(href==U.windowsUrlInfo().pathname){
                $(this).parent().addClass("active");
            }
        })

    }


    exports.loadTopMenu = loadTopMenu;
    exports.loadUserMenu = loadUserMenu;

});


