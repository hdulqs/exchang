/**
 * Created by oker on 2017/9/19.
 */

var okLog = (function () {
    var url = location.protocol + '//' + location.host + '/v2/futures/eventLog.do';
    if (location.host.indexOf("local") > -1) {
        url = 'http://47.94.253.250:8081/eventLog';
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



    return {
        eventLog: function (params) {
            var data = {
                platform: params.platform || 3,
                eventId: params.eventId || "",
                cookie: params.cookie || ""
            };
            doAjax(data);
        }
    }
})();