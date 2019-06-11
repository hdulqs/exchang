/**
 *<p>page_exchange_eventLog.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {

    var url = location.protocol + '//' + location.host + '/exchange/eventLog';
    if (location.host.indexOf("local") > -1) {
        url = 'http://47.94.253.250:8081/exchange/eventLog';
    }


    var doAjax = function (ajaxData) {
        jQuery.ajax({
            type: "post",
            url: url,
            data: ajaxData,
            dataType: "json",
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function (data) {
                if (data.code == 200) {
                    console.log("eventLog:" + ajaxData.eventId + " is recorded");
                } else {
                    throw data.errorMsg;
                }
            },
            error: function (err) {
                throw err;
            }
        });

    }
    
    function eventLog(params) {
        var data = {
            platform: params.platform || 3,
            eventId: params.eventId || "",
            cookie: params.cookie || ""
        };
        doAjax(data);
    }

    function init(){
        window.eventLog=eventLog;
    }


    exports.init=init;
    exports.eventLog=eventLog;
});