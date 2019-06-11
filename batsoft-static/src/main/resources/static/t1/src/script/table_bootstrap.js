/**
 * Created by Bat Admin on 2017/3/21.
 */
define(function (require, exports, module) {

    //<!-- Bootstrap-table CSS -->
    require("static/front/dist/script/plugins/bootstrap-table/css/bootstrap-table.min.css");
    require("static/front/dist/script/plugins/bootstrap-table/css/bootstrap-editable.css");
    require("static/front/dist/script/plugins/bootstrap-table/css/examples.css");

    //初化化bootstrap table
    require("bootstrap_table");
    require("static/front/dist/script/plugins/bootstrap-table/js/bootstrap-table-zh-CN.js");
    require("static/front/dist/script/plugins/bootstrap-table/js/bootstrap-table-export.js");
    require("static/front/dist/script/plugins/bootstrap-table/js/tableExport.js");
    require("static/front/dist/script/plugins/bootstrap-table/js/bootstrap-table-editable.js");
    require("static/front/dist/script/plugins/bootstrap-table/js/bootstrap-editable.js");
    // require("static/front/dist/script/plugins/bootstrap-table/js/ga.js")

    module.exports = {

        //init
        Init : function(){
            // to do;
        }
    }

});


