/**
 * Created by Bat Admin on 2015/6/1.
 */
define(function (require, exports, module) {

    /**
     *
     * @param title
     * @param text
     * @param state  "success"  "error" "warning"
     */
    function swalBase(title,text,state){
        swal({
            title: title,
            text: text,
            type: state===undefined?"":state
        });
    }

    /**
     *
     * @param title
     * @param text
     */
    function swalSuccess(title,text,callback){
        swal({
            title: title,
            text: text,
            type: "success"
        },callback);
    }
    /**
     *
     * @param title
     * @param text
     */
    function swalError(title,text){
        swal({
            title: title,
            text: text,
            type: "error"
        });
    }
    /**
     *
     * @param title
     * @param text
     */
    function swalWarning(title,text){
        swal({
            title: title,
            text: text,
            type: "warning"
        });
    }

    /**
     *
     * @param title
     * @param text
     * @param confirmButtonText
     * @param callback  callback 中要用 swalBase swalSuccess swalError swalWarning 等方法提示 否则confirm 窗口无法关闭！
     */

    function swalConfirm(title,text,confirmButtonText,callback){
        swal({
            title: title===undefined?"确认该操作吗?":title,
            text: text===undefined?"":text,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: confirmButtonText===undefined?"确认":confirmButtonText,
            closeOnConfirm: false
        },callback);
    }



    exports.swalBase    = swalBase;
    exports.swalSuccess    = swalSuccess;
    exports.swalError    = swalError;
    exports.swalWarning    = swalWarning;
    exports.swalConfirm   =swalConfirm;

});


