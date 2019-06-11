/**
 * Created by Bat Admin on 2015/6/1.
 */
define(function (require, exports, module) {

    function toastrTip(title,content,timeOut){
        if(timeOut===undefined) timeOut=3*1000;
        toastr.options = {
            closeButton: false,
            progressBar: true,
            showMethod: 'slideDown',
            positionClass: "toast-top-center",
            timeOut: timeOut
        };
        toastr.error(title, content);
    }
    exports.toastrTip    = toastrTip;
});


