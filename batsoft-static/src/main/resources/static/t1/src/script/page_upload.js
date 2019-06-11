/**
 * Created by Bat Admin on 2015/6/1.
 */
define(function (require, exports, module) {

   var F = require('./module_upload');
    /**
     * 图片上传
     */
    function imageUpload() {
        var options = {}
        F.dropzoneUpload(options);
    }
    exports.imageUpload = imageUpload;
});


