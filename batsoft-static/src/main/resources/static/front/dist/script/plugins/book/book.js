/**
 * Created by Administrator on 2017/9/7.
 */
!function (e, t) {
    function r(e) {
        return function (t) {
            return {}.toString.call(t) == "[object " + e + "]"
        }
    }

    function n() {
        return R++
    }

    function i(e, t, r) {
        r = r || this;
        for (var n = 0, i = e.length; n < i; n++)void 0 !== e[n] && t.call(r, e[n], n, e)
    }

    function a(e) {
        return e.match(k)[0]
    }

    function o(e) {
        for (e = e.replace(X, "/"), e = e.replace(M, "$1/"); e.match(H);)e = e.replace(H, "/");
        return e
    }

    function s(e) {
        var t = e.length - 1, r = e.charAt(t);
        return "#" === r ? e.substring(0, t) : ".css" === e.substring(t - 3) ? e : ".js" === e.substring(t - 2) || e.indexOf("?") > 0 || "/" === r ? e : e + ".js"
    }

    function u(e) {
        var t = B.alias;
        return t && D(t[e]) ? t[e] : e
    }

    function c(e) {
        var t;
        if (e.indexOf(".") > -1 && (t = J.exec(e))) {
            var r;
            (r = t[1]) && (e = e.substring(0, e.lastIndexOf(r))), r = ".js" + (r || ""), e = e.split(".").join("/") + r
        }
        return e
    }

    function f(e) {
        var t, r = B.paths;
        return r && (t = e.match(Q)) && D(r[t[1]]) && (e = r[t[1]] + t[2]), e
    }

    function l(e) {
        var t = B.vars;
        return t && e.indexOf("{") > -1 && (e = e.replace(V, function (e, r) {
            return D(t[r]) ? t[r] : e
        })), e
    }

    function v(e) {
        var t = B.map, r = e;
        if (t)for (var n = 0, i = t.length; n < i; n++) {
            var a = t[n];
            if ((r = U(a) ? a(e) || e : e.replace(a[0], a[1])) !== e)break
        }
        return r
    }

    function d(e, t) {
        var r, n = e.charAt(0);
        if (K.test(e)) r = e; else if ("." === n) r = o((t ? a(t) : B.cwd) + e); else if ("/" === n) {
            var i = B.cwd.match(P);
            r = i ? i[0] + e.substring(1) : e
        } else r = B.base + e;
        return 0 === r.indexOf("//") && (r = location.protocol + r), r
    }

    function h(e, t) {
        if (!e)return "";
        e = u(e), e = c(e), e = u(e), e = f(e), e = l(e), e = s(e);
        var r = d(e, t);
        return r = v(r)
    }

    function g(e, t, r) {
        var n = ae.test(e), i = W.createElement(n ? "link" : "script");
        if (r) {
            var a = U(r) ? r(e) : r;
            a && (i.charset = a)
        }
        return p(i, t, n, e), n ? (i.rel = "stylesheet", i.href = e) : (i.async = !0, i.src = e), te = i, ie ? ne.insertBefore(i, ie) : ne.appendChild(i), te = null, i
    }

    function p(e, t, r, n) {
        function i() {
            e.onload = e.onerror = e.onreadystatechange = null, r || B.debug || ne.removeChild(e), e = null, t()
        }

        var a = "onload" in e;
        if (r && (oe || !a))return void setTimeout(function () {
            m(e, t)
        }, 1);
        a ? (e.onload = i, e.onerror = function () {
                $("error", {uri: n, node: e}), i()
            }) : e.onreadystatechange = function () {
                /loaded|complete/.test(e.readyState) && i()
            }
    }

    function m(e, t) {
        var r, n = e.sheet;
        if (oe) n && (r = !0); else if (n)try {
            n.cssRules && (r = !0)
        } catch (e) {
            "NS_ERROR_DOM_SECURITY_ERR" === e.name && (r = !0)
        }
        setTimeout(function () {
            r ? t() : m(e, t)
        }, 20)
    }

    function b() {
        if (te)return te;
        if (re && "interactive" === re.readyState)return re;
        for (var e = ne.getElementsByTagName("script"), t = e.length - 1; t >= 0; t--) {
            var r = e[t];
            if ("interactive" === r.readyState)return re = r
        }
    }

    function y(e) {
        var t = fe.exec(e), r = ue;
        if (!t)return [];
        "require" !== (t = t[1]) && (r = r.toString().replace(/require/g, t), r = r.slice(1, r.length - 2), r = new RegExp(r, "g"));
        var n = [];
        return e.replace(ce, "").replace(r, function (e, t, r) {
            r && n.push(r)
        }), n
    }

    function E(e, t) {
        this.uri = e, this.dependencies = t || [], this.exports = null, this.status = 0, this._waitings = {}, this._remain = 0
    }

    function x(e) {
        var t = B.combo;
        switch (typeof t) {
            case"string":
                return -1 === e.indexOf(t);
            default:
                return !1
        }
    }

    function S(e) {
        var t = e.length;
        if (!(t < 2) && B.combo) {
            B.comboSyntax && (xe = B.comboSyntax), B.comboMaxLength && (Se = B.comboMaxLength), B.comboSuffix && (be = B.comboSuffix), me = B.comboExcludes;
            for (var r = [], n = 0; n < t; n++) {
                var i = e[n];
                if (!Ee[i] && !x(i)) {
                    E.get(i).status < ye && !T(i) && !w(i) && !N(i) && r.push(i)
                }
            }
            r.length > 1 && _(r)
        }
    }

    function O(e) {
        B.combo && (e.requestUri = Ee[e.uri] || e.uri)
    }

    function _(e) {
        var t = [], r = Oe.exec(e[0])[1], n = r.length;
        i(e, function (e) {
            t.push(e.substr(n))
        }), A(r, t)
    }

    function A(e, t) {
        for (var r = [], n = 0, i = t.length; n < i; n++)r[n] = t[n].replace(/\?.*$/, "");
        var a = e + xe[0] + r.join(xe[1]);
        be && (a += be);
        var o = t.length > Se;
        if (t.length > 1 && o) {
            var s = L(t, Se);
            A(e, s[0]), A(e, s[1])
        } else {
            if (o)throw new Error("The combo url is too long: " + a);
            for (var n = 0, i = t.length; n < i; n++)Ee[e + t[n]] = a
        }
    }

    function L(e, t) {
        for (var r = 0, n = e.length; r < n; r++)if (r > t - 1)return [e.splice(0, r), e]
    }

    function T(e) {
        return _e.test(e)
    }

    function w(e) {
        if (me)return me.test ? me.test(e) : me(e)
    }

    function N(e) {
        var t = B.comboSyntax || xe, r = t[0], n = t[1];
        return r && e.indexOf(r) > 0 || n && e.indexOf(n) > 0
    }

    if (e.LBF)var F = e.LBF;
    var exports = e.LBF = {version: "2.0.0"}, B = exports.data = {};
    exports.noConflict = function () {
        F && (e.LBF = F)
    };
    var q = r("Object"), D = r("String"), C = Array.isArray || r("Array"), U = r("Function"), I = r("Number"), j = r("RegExp"), R = 0, G = B.events = {};
    exports.on = function (e, t) {
        return (G[e] || (G[e] = [])).push(t), exports
    }, exports.off = function (e, t) {
        if (!e && !t)return G = B.events = {}, exports;
        var r = G[e];
        if (r)if (t)for (var n = r.length - 1; n >= 0; n--)r[n] === t && r.splice(n, 1); else delete G[e];
        return exports
    };
    var $ = exports.emit = function (e, t) {
        var r = G[e];
        if (r) {
            r = r.slice();
            for (var n = 0, i = r.length; n < i; n++)r[n](t)
        }
        return exports
    }, k = /[^?#]*\//, X = /\/\.\//g, H = /\/[^\/]+\/\.\.\//, M = /([^:\/])\/+\//g, Q = /^([^\/:]+)(\/.+)$/, V = /{([^{]+)}/g, J = /^[\w-_]*(?:\.[\w-_]+)*(\?[\w-_&=]*)?$/, K = /^\/\/.|:\//, P = /^.*?\/\/.*?\//, W = document, Y = location.href && 0 !== location.href.indexOf("about:") ? a(location.href) : "", z = W.scripts, Z = W.getElementById("LBFnode") || z[z.length - 1], ee = a(function (e) {
            return e.hasAttribute ? e.src : e.getAttribute("src", 4)
        }(Z) || Y);
    exports.resolve = h;
    var te, re, ne = W.head || W.getElementsByTagName("head")[0] || W.documentElement, ie = ne.getElementsByTagName("base")[0], ae = /\.css(?:\?|$)/i, oe = +navigator.userAgent.replace(/.*(?:AppleWebKit|AndroidWebKit)\/(\d+).*/, "$1") < 536;
    exports.request = g;
    var se, ue = /"(?:\\"|[^"])*"|'(?:\\'|[^'])*'|\/\*[\S\s]*?\*\/|\/(?:\\\/|[^\/\r\n])+\/(?=[^\/])|\/\/.*|\.\s*require|(?:^|[^$])\brequire\s*\(\s*(["'])(.+?)\1\s*\)/g, ce = /\\\\/g, fe = /^function[\s]*\([\s]*([^\s,\)]+)/, le = exports.cache = {}, ve = {}, de = {}, he = {}, ge = E.STATUS = {
        FETCHING: 1,
        SAVED: 2,
        LOADING: 3,
        LOADED: 4,
        EXECUTING: 5,
        EXECUTED: 6
    };
    E.prototype.resolve = function () {
        for (var e = this, t = e.dependencies, r = [], n = B.ignoreCss, i = 0, a = t.length; i < a; i++)n && -1 !== t[i].indexOf(".css") && (!0 === n || C(n) && -1 !== inArray(e.id, n)) || r.push(E.resolve(t[i], e.uri));
        return r
    }, E.prototype.load = function () {
        var e = this;
        if (!(e.status >= ge.LOADING)) {
            e.status = ge.LOADING;
            var t = e.resolve();
            $("beforeload", t), $("load", t);
            for (var r, n = e._remain = t.length, i = 0; i < n; i++)r = E.get(t[i]), r.status < ge.LOADED ? r._waitings[e.uri] = (r._waitings[e.uri] || 0) + 1 : e._remain--;
            if (0 === e._remain)return void e.onload();
            var a = {};
            for (i = 0; i < n; i++)r = le[t[i]], r.status < ge.FETCHING ? r.fetch(a) : r.status === ge.SAVED && r.load();
            for (var o in a)a.hasOwnProperty(o) && a[o]()
        }
    }, E.prototype.onload = function () {
        var e = this;
        e.status = ge.LOADED, e.callback && e.callback();
        var t, r, n = e._waitings;
        for (t in n)n.hasOwnProperty(t) && (r = le[t], r._remain -= n[t], 0 === r._remain && r.onload());
        delete e._waitings, delete e._remain
    }, E.prototype.fetch = function (e) {
        function t() {
            exports.request(a.requestUri, a.onRequest, a.charset)
        }

        function r() {
            delete ve[o], de[o] = !0, se && (E.save(i, se), se = null);
            var e, t = he[o];
            for (delete he[o]; e = t.shift();)e.load()
        }

        var n = this, i = n.uri;
        n.status = ge.FETCHING;
        var a = {uri: i};
        $("fetch", a);
        var o = a.requestUri || i;
        return !o || de[o] ? void n.load() : ve[o] ? void he[o].push(n) : (ve[o] = !0, he[o] = [n], $("request", a = {
                    uri: i,
                    requestUri: o,
                    onRequest: r,
                    charset: B.charset
                }), void(a.requested || (e ? e[a.requestUri] = t : t())))
    }, E.prototype.exec = function () {
        function require(e) {
            return E.get(require.resolve(e)).exec()
        }

        var e = this;
        if (e.status >= ge.EXECUTING)return e.exports;
        e.status = ge.EXECUTING;
        var t = e.uri;
        require.resolve = function (e) {
            return E.resolve(e, t)
        }, require.async = function (e, r) {
            return E.use(e, r, t + "_async_" + n()), require
        };
        var r = e.factory, exports = U(r) ? r(require, e.exports = {}, e) : r;
        return void 0 === exports && (exports = e.exports), delete e.factory, e.exports = exports, e.status = ge.EXECUTED, $("exec", e), exports
    }, E.resolve = function (e, t) {
        var r = {id: e, refUri: t};
        return $("resolve", r), r.uri || exports.resolve(r.id, t)
    }, E.define = function (e, t, r) {
        var n = arguments.length;
        1 === n ? (r = e, e = void 0) : 2 === n && (r = t, C(e) ? (t = e, e = void 0) : t = void 0), !C(t) && U(r) && (t = y(r.toString()));
        var i = {id: e, uri: E.resolve(e), deps: t, factory: r};
        if (!i.uri && W.attachEvent) {
            var a = b();
            a && (i.uri = a.src)
        }
        $("define", i), i.uri ? E.save(i.uri, i) : se = i
    }, E.save = function (e, t) {
        var r = E.get(e);
        r.status < ge.SAVED && (r.id = t.id || e, r.dependencies = t.deps || [], r.factory = t.factory, r.status = ge.SAVED, $("save", r))
    }, E.get = function (e, t) {
        return le[e] || (le[e] = new E(e, t))
    }, E.use = function (t, r, n) {
        var t = function (e) {
            var e = C(e) ? e : [e], t = {}, r = [], n = 0, i = B.deps;
            for (n = 0; n < e.length; n++)if (!t[e[n]]) {
                t[e[n]] = !0, r.push(e[n]);
                for (var a = i[e[n]] || [], o = 0; o < a.length; o++)e.push(a[o])
            }
            return r
        }(t), i = E.get(n, C(t) ? t : [t]);
        i.callback = function () {
            for (var exports = [], t = i.resolve(), n = 0, a = t.length; n < a; n++)exports[n] = le[t[n]].exec();
            r && r.apply(e, exports), delete i.callback
        }, i.load()
    }, E.preload = function (e) {
        var t = B.preload, r = t.length;
        r ? E.use(t, function () {
                t.splice(0, r), E.preload(e)
            }, B.cwd + "_preload_" + n()) : e()
    }, exports.use = function (e, t) {
        return E.preload(function () {
            E.use(e, t, B.cwd + "_use_" + n())
        }), exports
    }, E.define.cmd = {}, e.define = exports.define = E.define, exports.Module = E, B.fetchedList = de, B.cid = n, exports.require = function (e) {
        var t = E.get(E.resolve(e));
        return t.status < ge.EXECUTING && (t.onload(), t.exec()), t.exports
    }, B.base = ee, B.dir = ee, B.cwd = Y, B.charset = "utf-8", B.preload = [], B.deps = {}, exports.config = function (e) {
        for (var t in e) {
            var r = e[t], n = B[t];
            if (n && q(n))for (var i in r)n[i] = r[i]; else C(n) ? r = n.concat(r) : "base" === t && ("/" !== r.slice(-1) && (r += "/"), r = d(r)), B[t] = r
        }
        return $("config", e), exports
    };
    var pe = [["globalSettings", exports.data], ["lang.forEach", i], ["lang.isType", r], ["lang.isObject", q], ["lang.isString", D], ["lang.isArray", C], ["lang.isFunction", U], ["lang.isNumber", I], ["lang.isRegExp", j], ["util.request", g]];
    e.JSON && pe.push(["lang.JSON", e.JSON]), e.jQuery && 0 === (e.jQuery.version || "").indexOf("1.7") && pe.push(["lib.jQuery", e.jQuery]), i(pe, function (e) {
        exports.define(e[0], function (require, exports, module) {
            module.exports = e[1]
        })
    });
    var me, be, E = LBF.Module, ye = E.STATUS.FETCHING, B = LBF.data, Ee = B.comboHash = {}, xe = ["c/=/", ",/"], Se = 20;
    LBF.on("load", S), LBF.on("fetch", O);
    var Oe = /^(\S+:\/{2,3}[^\/]+\/)/, _e = /\.css(?:\?.*)?$/;
    if (B.test) {
        var Ae = LBF.test || (LBF.test = {});
        Ae.uris2paths = _, Ae.paths2hash = paths2hash
    }
    LBF.config({
        alias: {globalSettings: ee + "globalSettings"},
        vars: {theme: ee + "ui/themes/default"},
        paths: {
            app: ee + "app",
            lang: ee + "lang",
            monitor: ee + "monitor",
            lib: ee + "lib",
            ui: ee + "ui",
            util: ee + "util",
            format: ee + "format"
        }
    }), LBF.config({})
}(this);