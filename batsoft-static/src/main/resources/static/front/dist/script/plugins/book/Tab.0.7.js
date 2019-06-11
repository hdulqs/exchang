!function (t, e) {
    "object" == typeof exports && "undefined" != typeof module ? module.exports = e() : "function" == typeof define && (define.amd || define.cmd) ? define("readnovelm/js/common/components/Tab.0.7.js", [], e) : t.Tab = e()
}(this, function (require) {
    "use strict";
    var t;
    "function" == typeof require && (t = require("readnovelm/js/common/utils/url.0.7.js"));
    var e, a = "active";
    return $.fn.tab = function (t) {
        new e($(this), t)
    }, $.fn.eqAttr = function (t) {
        t = t || "data-rel";
        var e = $(this).attr(t);
        if (!e)return $();
        var a = $("#" + e);
        return a.length ? a : $("." + e)
    }, e = function (e, r) {
        var n = $(), i = $();
        if (e = $(e), 0 !== e.length) {
            r = r || {};
            var s = {
                eventType: "click",
                history: "auto",
                prepend: "auto",
                index: "auto",
                aria: "a",
                callback: function () {
                }
            }, o = $.extend({}, s, r);
            1 == e.length && 0 == e.is("a") && (n = e, e = n.find("a"), "auto" == o.history && (o.history = !0), "auto" == o.prepend && (o.prepend = !0, i = $("<line></line>"), n.prepend(i)));
            var l = this;
            l.callback = function (e) {
                if (!0 === o.history && history.pushState) {
                    var a = e.attr("data-rel");
                    a && history.replaceState(null, document.title, t.setParam("tab", a.toLowerCase()))
                }
                i.length && i.css({width: e.width(), left: e.position().left}), o.callback.apply(this, arguments)
            };
            var c = e.closest(o.aria).attr("role", "tab"), d = o.index;
            e.each(function (t) {
                "number" != typeof d && $(this).hasClass(a) && (d = t), $(this).data("index", t)
            }), "number" != typeof d && (d = 0), e.on(o.eventType, function (t) {
                var r, n, i, s = $(this);
                if (s.hasClass(a))return void((!1 === t.isTrusted || t.hasOwnProperty("_args")) && (c.attr("aria-selected", "false"), s.closest(o.aria).attr("aria-selected", "true"), l.callback.call(this, s, $(), $(), $())));
                r = e.eq(d).removeClass(a), c.eq(d).attr("aria-selected", "false"), s.addClass(a), s.closest(o.aria).attr("aria-selected", "true"), n = r.eqAttr().removeClass(a), i = s.eqAttr().addClass(a), d = s.data("index"), l.callback.call(this, s, r, i, n)
            }), $(function () {
                e.eq(d).trigger(o.eventType)
            }), l.el = {tab: e}, l.params = o
        }
    }
});