/**
 * Created by Bat Admin on 2015/6/1.
 */
define(function (require, exports, module) {

    var U = require('./module_utils');

    /**
     * render
     * @param options tplId :模版id  renderId：渲染id  data:渲染数据
     * @returns {*}
     */
    function render(options){
        var op   = options || {};
        var tpl = $.templates(op.tplId);
        tpl.link(op.renderId, op.data);
        helpers();
    }

    function helpers() {
        $.views.helpers({
            "formatterIcon": function(icon){
                if(icon.match(/^fa /)){
                    return icon.replace("fa ","");
                }
            },
            "strNewLine": function(str,chr){
                if(str!=null) {
                    return U.replaceAll(str,chr,'<br>')
                }else{
                    return "";
                }

            },
            "formatDate": function(date,format){
              return U.dateFormat(date,format);

            }
        });
    }

    exports.render    = render;


});


