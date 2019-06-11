/**
 *<p>page_firend.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      lucl
 * @version:     V1.0
 * @Date:        2017-11-30 17:06:28
 */
define(function (require, exports) {

    var common = require('./page_common'), //common
        core = require('./module_core'),
        tpl = require('./module_tpl');


    /**
     *  list
     */
    function list() {
        var tplId = "friendLink_temp";
        var renderId = "friendLinkId";
        friends(tplId,renderId);
    };
    function changeList(){
        var tplId = "friendLink_change_temp";
        var renderId = "friendLink_changeId";
        friends(tplId,renderId);
    }
    /**
     * 加載友情鏈接圖片
     */
    function friends(tplId,renderId){
        core.list({url:BASE_PATH+"/friend/list"}).then(function (data) {
            tpl.render({"tplId": "#"+tplId, "renderId": "#"+renderId, "data": data.data});
        })
    }



    exports.list = list;
    exports.changeList = changeList;
});