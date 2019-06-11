/**
 * Created by Bat Admin on 2015/6/4.
 * Hurongbi Ajax
 */
;(function () {
    var HbAjax = function (options, callback) {
        var _op = options || {},
            _url = _op.url ? _op.url : '/account/ajax.do',
            _data = _op.data || {},
            _type = _op.type || 'GET',
            _processData= _op.processData?_op.processData: true,
            _contentType= _op.contentType? _op.contentType: 'application/x-www-form-urlencoded',
            _datatype = _op.datatype ? _op.datatype : '',
            _jsonp = _op.jsonp ? _op.jsonp : '',
            _jsonpc = _op.jsonpcallback ? _op.jsonpcallback : '',
            _this = this,
            _location = window.location,
            _vUrl = '/account/account.do?a=validate_all&jump_url=' + _location.pathname + _location.search;

        return $.ajax({
            headers: {
                Accept: "application/json; charset=utf-8"
            },
            url: _url,
            type: _type,
            data: _data,
            dataType: _datatype,
            // async:false,
            jsonp: _jsonp,
            jsonpCallback: _jsonpc,
            processData: _processData,
            contentType: _contentType,
            beforeSend: ABeforeSend,
            success: ACallback,
            error: AError,
            complete: AComplete
        });


        function ABeforeSend() {
            callback && callback({"code": "loading", "msg": "loading"})
        }

        function ACallback(data) {
            if (data.code == "401.1" || data.code == "401.2") {
                callback && callback({"code": "error", "msg": "操作异常", data: data})
            } else {
                callback && callback({"code": "success", "msg": "success", data: data})
            }
        }

        function AError() {
            callback && callback({"code": "error", "msg": "网络异常"})
        }

        function AComplete(xhr, ts) {
            callback && callback({"code": "complete", "xhr": xhr, "ts": ts, "msg": 'complete'})
        }

    };

    "function" == typeof define ? define(function () {
            return HbAjax
        }) : "undefined" != typeof exports ? module.exports = HbAjax : window.HbAjax = HbAjax;

})();
