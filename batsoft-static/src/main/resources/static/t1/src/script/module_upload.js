/**
 * Created by Bat Admin on 2015/6/1.
 */
define(function (require, exports, module) {
    /**
     * dropzone 插件上传
     */
    function dropzoneUpload(options){

        var _default = {
                container:".dropzone",
                url: "/upload/file", //必须填写
                paramName: "file",
                maxFilesize: 2.0, // MB
                maxFiles:1,//一次性上传的文件数量上限
                acceptedFiles: ".xls,.jpg,.png,.gif,.pdf,.txt,.doc,.docx",
                addRemoveLinks : true,//添加移除文件
                autoProcessQueue: true,//自动上传
                dictCancelUploadConfirmation:'你确定要取消上传吗？',
                dictMaxFilesExceeded: "您一次最多只能上传{{maxFiles}}个文件",
                dictFileTooBig:"文件过大({{filesize}}MB). 上传文件最大支持: {{maxFilesize}}MB.",
                dictDefaultMessage :'<i class="fa fa-cloud-upload  fa-3x"></i>',
                dictResponseError: '文件上传失败!',
                dictInvalidFileType: "你不能上传该类型文件",
                dictCancelUpload: "取消上传",
                dictCancelUploadConfirmation: "你确定要取消上传吗?",
                dictRemoveFile: "移除文件",
                uploadMultiple:false,
                //change the previewTemplate to use Bootstrap progress bars
                //previewTemplate: "<div class=\"dz-preview dz-file-preview\">\n  <div class=\"dz-details\">\n    <div class=\"dz-filename\"><span data-dz-name></span></div>\n    <div class=\"dz-size\" data-dz-size></div>\n    <img data-dz-thumbnail />\n  </div>\n  <div class=\"progress 　　　　　　　　progress-small progress-striped active\"><div class=\"progress-bar progress-bar-success\" data-dz-uploadprogress></div></div>\n  <div 　　　　　　　　class=\"dz-success-mark\"><span></span></div>\n  <div class=\"dz-error-mark\"><span></span></div>\n  <div class=\"dz-error-message　　　　　　　　\"><span data-dz-errormessage></span></div>\n</div>",
                init: function() {

                    //添加了文件的事件
                    this.on("addedfile", function () {

                    });
                    this.on("queuecomplete",function(file) {
                        //上传完成后触发的方法
                    });
                    this.on("removedfile",function(file){
                        //删除文件时触发的方法
                    });
                    this.on("success", function(file,data) {
                        if(!data.success){
                            alert("上传失败！");
                        }else{
                            $(this.element.getElementsByTagName("input")).val(data.result);
                            
                            if($(this.element).parent().next().length>0){
                                var path=GLOBAL.config.fileHost+data.result;
                                $(this.element).parent().next().find("img").attr('src',path);
                            }
                        }
                    });
                    this.on("error", function(file) {
                        alert("文件上传失败！");
                    });
                },
            },
            _option    = $.extend(_default, options);
        $(_option.container).dropzone(_option)
    }

    exports.dropzoneUpload    = dropzoneUpload;
});


