/**
 * Created by Bat Admin on 2016/03/21.
 * 系统主页
 */
define(function (require,exports) {
    var core     =require('./module_core'),
        effect     =require('./module_effect'),
        common = require("./page_common");
    function index(){
        $(document).ready(function() {
            effect.select2();
        });
    }
    exports.index    = index;
});