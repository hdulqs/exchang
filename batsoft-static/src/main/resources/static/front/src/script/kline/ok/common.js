define([], function () {
    return {
        init: function () {
            console.log("common, init~");
        },
        senMsgCode: function (msgType, successCallback, errorCallback) {
            var BASE_AJAX_POST_URL = location.protocol + '//' + location.host;
            if (location.host.indexOf("local") > -1) {
                BASE_AJAX_POST_URL = 'http://local.okex.com';
            }
            var preUrl = BASE_AJAX_POST_URL + "/api/sendMsgCode.do";
            var data = {
                isClient: 5,
                type: msgType
            };
            var request = function () {
                var xhr = jQuery.ajax({
                    url: preUrl,
                    type: 'post',
                    data: data,
                    xhrFields: {
                        withCredentials: true
                    },
                    crossDomain: true,
                    success: function (response) {
                        if (successCallback) {
                            //  resultCode 172  次数限制
                            successCallback(response);
                        }
                    },
                    fail: $.proxy(function (msg, response, xhr) {
                        if (errorCallback) {
                            errorCallback(response);
                        }
                    }, this)
                });
            };

            request();
        },

        /***
         * 普通弹框
         * @param text 提示文本
         */
        showDialog: function (text) {

            var dialogStr = ' <div class="ok-normal-dialog" id="okNormalDialog">\n' +
                '        <div class="ok-normal-dialog-bg"></div>\n' +
                '        <div class="ok-normal-dialog-box" id="okNormalDialogBox">\n' +
                '            <div class="ok-normal-dialog-box-title">OKEX 提示\n' +
                '                  <span id="okNormalDialogClose" class="dialog_closed"></span>\n' +
                '            </div>\n' +
                '            <div class="text" >' + text + '</div>\n' +
                '            <div class="enter-btn-box">\n' +
                '                <button class="enter-btn" id="okNormalDialogEnter">确定</button>\n' +
                '            </div>\n' +
                '        </div>\n' +
                '    </div>';

            if ($('#okNormalDialog')) {
                $('#okNormalDialog').remove();
            }

            $('body').append(dialogStr);

            // 关闭 绑定
            $('#okNormalDialogClose').on('click', function () {
                $('#okNormalDialog').remove();
            });

            // 确定 绑定
            $('#okNormalDialogEnter').on('click', function () {
                $('#okNormalDialog').remove();
            })
        },

        showDialogFix: function (text,btnTitle) {

            var dialogStr = ' <div class="ok-normal-dialog" id="okNormalDialog">\n' +
                '        <div class="ok-normal-dialog-bg"></div>\n' +
                '        <div class="ok-normal-dialog-box" id="okNormalDialogBox">\n' +
                '            <div class="ok-normal-dialog-box-title">OKEX 提示\n' +
                '                  <span id="okNormalDialogClose" class="dialog_closed"></span>\n' +
                '            </div>\n' +
                '            <div class="text" >' + text + '</div>\n' +
                '            <div class="enter-btn-box">\n' +
                '                <button class="enter-btn" id="okNormalDialogEnter">'+btnTitle+'</button>\n' +
                '            </div>\n' +
                '        </div>\n' +
                '    </div>';

            if ($('#okNormalDialog')) {
                $('#okNormalDialog').remove();
            }

            $('body').append(dialogStr);

            // 关闭 绑定
            $('#okNormalDialogClose').on('click', function () {
                $('#okNormalDialog').remove();
            });

            // 确定 绑定
            $('#okNormalDialogEnter').on('click', function () {
                $('#okNormalDialog').remove();
            })
        },

        /**
         * 获取url参数
         * @param name
         * @returns {null}
         * @constructor
         */
        getUrlParameter: function(name) {
            var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r!=null)return  unescape(r[2]); return null;
        },

        /**
         * 按钮提交后，防止重复提交，等待状态...
         * @param elementId		对象id
         * @param elementValue	对象国际化值
         */
        circulatePoint: function(elementId,elementValue){
            var index = 0;
            circulatePointTimer = setInterval(function(){
                if(index==3){
                    index=0;
                    document.getElementById(elementId).value=elementValue;
                    return ;
                }else{
                    document.getElementById(elementId).value+=".";
                    index++;
                }
            }, 500);
        },
        /**
         * 按钮点击后让其置灰不可点并出现动态小点点
         * @param elementId		对象id
         * @param elementValue	对象国际化值
         */
        waitingStation: function(elementId,elementValue){
            var _this = this;
            if(elementValue != "" && document.getElementById(elementId)){
                document.getElementById(elementId).value=elementValue;
                document.getElementById(elementId).style.background="#ebebeb";
                document.getElementById(elementId).style.color = "#686868";
                document.getElementById(elementId).style.cursor = "auto";
                document.getElementById(elementId).style.border = "1px solid #ebebeb";
                document.getElementById(elementId).disabled = true;
                _this.circulatePoint(elementId,elementValue);
            }
        },
        /**
         * 按钮恢复可点击并去年动态小点点
         * @param elementId		对象id
         * @param elementValue	对象国际化值
         */
        cancelWaiting: function(elementId,elementValue){
            if(elementValue != "" && document.getElementById(elementId)){
                document.getElementById(elementId).value=elementValue;
                document.getElementById(elementId).style.background="#0096e0";
                document.getElementById(elementId).style.color = "#fff";
                document.getElementById(elementId).style.cursor = "pointer";
                document.getElementById(elementId).style.border = "1px solid #0096e0";
                document.getElementById(elementId).disabled = false;
                document.getElementById(elementId).value=elementValue;
                clearInterval(circulatePointTimer);
            }
        }

    }
});