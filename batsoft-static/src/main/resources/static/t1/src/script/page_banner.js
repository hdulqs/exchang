/**
 *<p>page_banner.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      lucl
 * @version:     V1.0
 * @Date:        2017-11-30 17:06:28
 */
define(function (require, exports) {

    var common = require('./page_common'), //common
        core = require('./module_core'),
        E = require('./module_effect'),
        tpl = require('./module_tpl');


    /**
     *  list
     */
    function list() {
        banners();

    };
    /**
     * banner图作为background 展示方法
     */
    function banners(){

        var banners = "";  //banner地址拼接
        core.list({url:BASE_PATH+"/banner/list"}).then(function (data) {
            for(var i=0;i<data.data.length;i++){
                banners = banners+FILE_PATH+data.data[i].image+",";
            }
            banners = banners.substr(0,banners.length-1);
            var result = {"images":banners};
            tpl.render({"tplId": "#banner_background_Temp", "renderId": "#banner_background", "data": result});
            E.bgimage();
            E.sliderFull();
        })
    }



    exports.list = list;
});