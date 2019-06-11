/**
 * Created by Bat Admin on 2017/3/21.
 */
define(function (require, exports, module) {



    $(function () {
        $(".userStatus").hover(
        function () {
            $(this).find(".userItemSon").show();
        },
        function () {
            $(this).find(".userItemSon").hide();
        }
    );

        $(".headerUserBar li.item").hover(function () {
            $(this).find(".userItemSon").show();
        }, function () {
            $(this).find(".userItemSon").hide();
        });
        $("#languageBt").hover(function () {
            $(".languageSwitch").show();
        }, function () {
            $(".languageSwitch").hide();
        });
    });
    Date.prototype.format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1,
            "d+": this.getDate(),
            "h+": this.getHours(),
            "m+": this.getMinutes(),
            "s+": this.getSeconds(),
            "q+": Math.floor((this.getMonth() + 3) / 3),
            "S": this.getMilliseconds()
        };
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            }
        }
        return fmt;
    };
    $.fn.serializeJson = function () {
        var serializeObj = {};
        var disabled = $(this).find(':disabled');
        disabled.removeAttr('disabled');
        var array = this.serializeArray();
        disabled.attr('disabled', 'disabled');
        $(array).each(function () {
            if (serializeObj[this.name] != undefined) {
                if ($.isArray(serializeObj[this.name])) {
                    serializeObj[this.name].push(this.value);
                } else {
                    serializeObj[this.name] = [serializeObj[this.name], this.value];
                }
            } else {
                serializeObj[this.name] = this.value;
            }
        });
        return serializeObj;
    };
    (function ($) {
        $.fn.extend({
            getCurPos: function () {
                var curCurPos = '';
                var all_range = '';
                if (navigator.userAgent.indexOf("MSIE") > -1) {
                    if ($(this).get(0).tagName == "TEXTAREA") {
                        all_range = document.body.createTextRange();
                        all_range.moveToElementText($(this).get(0));
                    } else {
                        all_range = $(this).get(0).createTextRange();
                    }
                    $(this).focus();
                    var cur_range = document.selection.createRange();
                    cur_range.moveEnd('character', -cur_range.text.length)
                    cur_range.setEndPoint("StartToStart", all_range);
                    curCurPos = cur_range.text.length;
                } else {
                    $(this).focus();
                    curCurPos = $(this).get(0).selectionStart;
                }
                return curCurPos;
            }, setCurPos: function (start, end) {
                if (navigator.userAgent.indexOf("MSIE") > -1) {
                    var all_range = '';
                    if ($(this).get(0).tagName == "TEXTAREA") {
                        all_range = document.body.createTextRange();
                        all_range.moveToElementText($(this).get(0));
                    } else {
                        all_range = $(this).get(0).createTextRange();
                    }
                    $(this).focus();
                    all_range.moveStart('character', start);
                    all_range.moveEnd('character', -(all_range.text.length - (end - start)));
                    all_range.select();
                } else {
                    $(this).focus();
                    $(this).get(0).setSelectionRange(start, end);
                }
            },
        });
    })(jQuery);
    module.exports = {
        //init
        Init : function(){
            // to do;
        }
    }

});


