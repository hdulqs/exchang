/**
 *<p>page_user.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {

    var effect = require('./module_effect'), //特效
        core = require('./module_core');

    /**
     * ico list
     */
    function list() {
        effect.countDown();

    }



    exports.list = list;

});