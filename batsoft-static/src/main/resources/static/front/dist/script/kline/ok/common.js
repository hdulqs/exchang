define([],function(){return{init:function(){},senMsgCode:function(msgType,successCallback,errorCallback){var BASE_AJAX_POST_URL=location.protocol+"//"+location.host;location.host.indexOf("local")>-1&&(BASE_AJAX_POST_URL="http://local.okex.com");var preUrl=BASE_AJAX_POST_URL+"/api/sendMsgCode.do",data={isClient:5,type:msgType},request=function(){jQuery.ajax({url:preUrl,type:"post",data:data,xhrFields:{withCredentials:!0},crossDomain:!0,success:function(response){successCallback&&successCallback(response)},fail:$.proxy(function(msg,response,xhr){errorCallback&&errorCallback(response)},this)})};request()},showDialog:function(text){var dialogStr=' <div class="ok-normal-dialog" id="okNormalDialog">\n        <div class="ok-normal-dialog-bg"></div>\n        <div class="ok-normal-dialog-box" id="okNormalDialogBox">\n            <div class="ok-normal-dialog-box-title">OKEX 提示\n                  <span id="okNormalDialogClose" class="dialog_closed"></span>\n            </div>\n            <div class="text" >'+text+'</div>\n            <div class="enter-btn-box">\n                <button class="enter-btn" id="okNormalDialogEnter">确定</button>\n            </div>\n        </div>\n    </div>';$("#okNormalDialog")&&$("#okNormalDialog").remove(),$("body").append(dialogStr),$("#okNormalDialogClose").on("click",function(){$("#okNormalDialog").remove()}),$("#okNormalDialogEnter").on("click",function(){$("#okNormalDialog").remove()})},showDialogFix:function(text,btnTitle){var dialogStr=' <div class="ok-normal-dialog" id="okNormalDialog">\n        <div class="ok-normal-dialog-bg"></div>\n        <div class="ok-normal-dialog-box" id="okNormalDialogBox">\n            <div class="ok-normal-dialog-box-title">OKEX 提示\n                  <span id="okNormalDialogClose" class="dialog_closed"></span>\n            </div>\n            <div class="text" >'+text+'</div>\n            <div class="enter-btn-box">\n                <button class="enter-btn" id="okNormalDialogEnter">'+btnTitle+"</button>\n            </div>\n        </div>\n    </div>";$("#okNormalDialog")&&$("#okNormalDialog").remove(),$("body").append(dialogStr),$("#okNormalDialogClose").on("click",function(){$("#okNormalDialog").remove()}),$("#okNormalDialogEnter").on("click",function(){$("#okNormalDialog").remove()})},getUrlParameter:function(name){var reg=new RegExp("(^|&)"+name+"=([^&]*)(&|$)"),r=window.location.search.substr(1).match(reg);return null!=r?unescape(r[2]):null},circulatePoint:function(elementId,elementValue){var index=0;circulatePointTimer=setInterval(function(){return 3==index?(index=0,void(document.getElementById(elementId).value=elementValue)):(document.getElementById(elementId).value+=".",void index++)},500)},waitingStation:function(elementId,elementValue){var _this=this;""!=elementValue&&document.getElementById(elementId)&&(document.getElementById(elementId).value=elementValue,document.getElementById(elementId).style.background="#ebebeb",document.getElementById(elementId).style.color="#686868",document.getElementById(elementId).style.cursor="auto",document.getElementById(elementId).style.border="1px solid #ebebeb",document.getElementById(elementId).disabled=!0,_this.circulatePoint(elementId,elementValue))},cancelWaiting:function(elementId,elementValue){""!=elementValue&&document.getElementById(elementId)&&(document.getElementById(elementId).value=elementValue,document.getElementById(elementId).style.background="#0096e0",document.getElementById(elementId).style.color="#fff",document.getElementById(elementId).style.cursor="pointer",document.getElementById(elementId).style.border="1px solid #0096e0",document.getElementById(elementId).disabled=!1,document.getElementById(elementId).value=elementValue,clearInterval(circulatePointTimer))}}});