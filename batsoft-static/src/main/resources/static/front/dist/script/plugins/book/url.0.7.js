define("readnovelm/js/common/utils/url.0.7.js", [], function (require, exports) {
    g_data.domains;
    exports.isValid = function (e) {
        return new RegExp("^(http(s)?(://))?(www.)?[a-zA-Z0-9-_.]+").test(e)
    }, exports.getParamByName = function (e, n) {
        n || (n = window.location.href), e = e.replace(/[\[\]]/g, "\\$&");
        var t = new RegExp("[?&]" + e + "(=([^&#]*)|&|#|$)"), r = t.exec(n);
        return r ? r[2] ? decodeURIComponent(r[2].replace(/\+/g, " ")) : "" : null
    }, exports.setParam = function (e, n, t) {
        t || (t = window.location.href);
        var r = null, l = "", o = t.split("?"), a = o[0], s = o[1], u = "";
        if (s) {
            var p = s.split("#"), f = p[0];
            for (r = p[1], r && (s = f), o = s.split("&"), i = 0; i < o.length; i++)o[i].split("=")[0] != e && (l += u + o[i], u = "&")
        } else {
            var p = a.split("#"), f = p[0];
            r = p[1], f && (a = f)
        }
        return r && (n += "#" + r), a + "?" + l + u + e + "=" + n
    }, exports.removeParam = function (e, n) {
        n || (n = window.location.href);
        var t = n.split("?"), r = [];
        if ("string" == typeof e) r.push(e); else for (var i = 0; i < e.length; i++)r.push(e[i]);
        for (var l = t[1].split(/[&;]/g), i = 0; i < r.length; i++) {
            for (var o = encodeURIComponent(r[i]) + "=", a = l, s = 0; s < a.length; s++)-1 !== a[s].lastIndexOf(o, 0) && a.splice(s, 1);
            _url = a.length > 0 ? "?" + a.join("&") : ""
        }
        return _url = t[0] + _url, _url
    }
});