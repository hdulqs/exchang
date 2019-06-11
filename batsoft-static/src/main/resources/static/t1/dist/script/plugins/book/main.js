if (!window.Promise)var Promise = function (a) {
    var c, b;
    a(function (a) {
        c = a;
        e()
    }, function (a) {
        b = a;
        e()
    });
    var d = [], e = function () {
        var a, e;
        void 0 !== c ? (a = "resolve", e = c) : void 0 !== b && (a = "reject", e = b);
        for (; a && e && d[0];)d.shift()[a](e)
    };
    this.then = function (a, b) {
        d.push({resolve: a || new Function, reject: b || new Function});
        e();
        return this
    }
};
var Q = {
    isLocalStorage: function () {
        try {
            return localStorage.setItem("test", "test"), localStorage.removeItem("test"), !0
        } catch (a) {
            return !1
        }
    }(),
    isEmpty: function (a) {
        if ("" === a || void 0 === a)return !0;
        if ("object" === typeof a) {
            if (!(a instanceof Array && 0 === a.length))for (var c in a)return !1;
            return !0
        }
        return !1
    },
    getURI: function (a) {
        for (var c = location.search.substr(1).split("&"), b = 0, d = {}, e; e = c[b++];)e = e.split("="), d[e[0]] = e[1];
        return d[a] || ""
    },
    storage: function (a, c) {
        if (void 0 != c && Q.isLocalStorage)return c = c.toString(),
            localStorage.removeItem(a), localStorage.setItem(a, c), c;
        null === c && localStorage.removeItem(a);
        return localStorage.getItem(a)
    },
    tips: function (a, c) {
        var b = document.createElement("div");
        b.className = "tips";
        b.innerHTML = a;
        document.body.insertBefore(b, document.body.firstChild);
        c && setTimeout(function () {
            document.body.removeChild(b)
        }, 1E3 * c)
    },
    alert: function (a, c) {
        var b = document.createElement("div");
        b.addEventListener("click", function (a) {
            a.stopPropagation()
        }, !1);
        b.className = "alert";
        var d = document.createElement("div");
        d.className = "alert_bg";
        var e = {
            close: function (a) {
                a && a.preventDefault();
                a && a.stopPropagation();
                "function" == typeof c && c(b);
                d.removeChild(b);
                b.innerHTML = "";
                document.body.removeChild(d);
                d = null
            }
        }, f = document.createElement("div");
        f.className = "close";
        f.addEventListener("click", e.close, !1);
        f.addEventListener("touchstart", e.close, !1);
        "function" === typeof a && (a = a.call(e));
        "string" === typeof a ? b.innerHTML = a : a.appendChild && b.appendChild(a);
        b.appendChild(f);
        d.appendChild(b);
        document.body.appendChild(d);
        setTimeout(function () {
            b.className =
                "alert open";
            b.clientHeight < window.innerHeight && (b.style.top = (window.innerHeight - b.clientHeight) / 2 + "px")
        }, 1);
        return e
    },
    confirm: function (a, c, b) {
        Q.alert(function () {
            var b = this, e = document.createElement("div");
            "string" == typeof a ? e.innerHTML = a : e.appendChild(a);
            var f = document.createElement("span");
            f.className = "btn definite";
            f.innerHTML = "\u786e\u5b9a";
            f.onclick = function () {
                c(!0);
                b.close()
            };
            var g = document.createElement("span");
            g.className = "btn cancel";
            g.innerHTML = "\u53d6\u6d88";
            g.onclick = function () {
                c(!1);
                b.close()
            };
            e.appendChild(f);
            e.appendChild(g);
            return e
        }, b)
    },
    book: function (a) {
        if (!a)var c = location.pathname.match(/\/(?:[a-z]+)\/(\d+).+html$/i), c = c ? c[1] : 0;
        var b = "bookid_" + (a || c), d = localStorage.getItem(b);
        d || (d = '{"chapter":"", "list":"", "page":1, "listOrderType":0}', Q.storage(b, d));
        d = JSON.parse(d);
        return {
            id: Number(a || c), get listOrderType() {
                return d.listOrderType || 0
            }, set listOrderType(a) {
                d.listOrderType = a;
                Q.storage(b, JSON.stringify(d))
            }, get chapter() {
                if (a)return d.chapter;
                var b = location.pathname.match(/^\/(?:vip)*chapter\/\d+\/(\d+)\.html$/i);
                return b ? b[1] : 0
            }, set chapter(a) {
                d.chapter = a;
                Q.storage(b, JSON.stringify(d))
            }, get list() {
                return d.list || 1
            }, set list(a) {
                d.list = a;
                Q.storage(b, JSON.stringify(d))
            }, get page() {
                return d.page
            }, set page(a) {
                d.page = a;
                Q.storage(b, JSON.stringify(d))
            }
        }
    },
    postFormData: function (a, c) {
        var b = "";
        if ("object" == typeof a)if (c || !window.FormData)for (var d in a)b += d + "=" + encodeURIComponent(a[d]) + "&"; else for (d in b = new FormData, a)b.append(d, a[d]);
        return b
    },
    isLogin: function () {
        return !!Q.cookie("c_u")
    },
    get userInfo() {
        var a = {}, c =
            Q.cookie("c_u");
        if (!c)return a;
        var c = c.replace('"', "").split("&"), b;
        b = c[0];
        var d = [];
        b = b.split("l");
        for (var e = 0; e < b.length; e++)d.push(String.fromCharCode(b[e]));
        b = d.join("");
        a.nickName = b;
        a.userId = String(c[1]);
        c = +new Date;
        b = Q.cookie("info_cookie_last_time");
        b && 108E5 > c - b || (Q.jsonp("http://passport.17k.com/info_cookie?type=4", {}, new Function), Q.cookie("info_cookie_last_time", c, 0));
        c = Q.cookie("c_i").toString().split("&") || Array(5);
        a.flower = c[0];
        a.isAuthor = c[1];
        a.pkVote = c[2];
        a.replay = c[3];
        a.at = c[4];
        return a
    },
    checkRegister: function (a, c) {
        var b = new XMLHttpRequest;
        b.open("post", a, !1);
        b.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        b.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        b.send(Q.postFormData(c, !0));
        return JSON.parse(b.responseText)
    },
    net: function (a, c, b, d) {
        return a.match(/^http/i) && !(new RegExp("^" + location.origin, "i")).test(a) ? (Q.jsonp(a, c, b), "jsonp") : "function" === typeof b ? (Q.ajax(a, c, b, d), "ajax") : Q.load(a, c)
    },
    ajax: function (a, c, b, d) {
        var e = "post";
        "function" == typeof c &&
        (e = "get", d = b, b = c);
        var f = new XMLHttpRequest;
        f.addEventListener("load", function () {
            if (404 != this.status) {
                var a = this.responseText;
                "json" === d && (a = JSON.parse(a));
                b && b.call(f, a)
            }
        });
        f.open(e, a, !0);
        f.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        f.send(Q.postFormData(c, !0))
    },
    post: function (a, c) {
        var b;
        b = document.createElement("iframe");
        b.name = "Q_post_artwc_iframe";
        b.style.display = "none";
        document.body.appendChild(b);
        b.onload = function () {
            document.body.removeChild(b);
            b = null
        };
        var d, e = document.createElement("form");
        e.target = "Q_post_artwc_iframe";
        e.action = a;
        e.method = "post";
        for (var f in c)d = document.createElement("input"), d.type = "hidden", d.name = f, d.value = c[f], e.appendChild(d);
        document.body.appendChild(e);
        e.submit();
        document.body.removeChild(e)
    },
    load: function (a, c) {
        try {
            var b = new XMLHttpRequest;
            b.open(c ? "post" : "get", a, !1);
            c && b.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            b.send(c ? Q.postFormData(c, !0) : null);
            return b.responseText
        } catch (d) {
            return ""
        }
    },
    loadjs: function (a, c) {
        var b = document.createElement("script");
        b.src = a;
        c && c.tagName ? c.appendChild(b) : document.querySelector("head").appendChild(b);
        "function" === typeof c && (b.onload = c)
    },
    loadCss: function (a, c) {
        var b = document.createElement("link");
        b.setAttribute("text", "text/css");
        b.setAttribute("rel", "stylesheet");
        b.href = a;
        c && (b.onload = c);
        document.querySelector("head").appendChild(b)
    },
    jsonp: function (a, c, b) {
        "function" == typeof c && (c = [b, b = c][0]);
        var d = document.querySelector("head"), e = "Jsonp" + String((new Date).getTime()) + String(Math.floor(1E3 * Math.random()));
        Q[e] = function (a) {
            b(a,
                e);
            d.removeChild(f);
            f = Q[e] = null
        };
        var f = document.createElement("script");
        a.match(/\?/) || (a += "?");
        a = (a + ("&callback=Q." + e + "&jsonp=Q." + e)).replace("?&", "?");
        for (var g in c)a += "&" + encodeURIComponent(g) + "=" + encodeURIComponent(c[g]);
        f.src = a;
        d.appendChild(f)
    },
    cookie: function (a, c, b, d, e) {
        d = "domain=" + (d || "17k.com") + ";";
        e = "path=" + (e || "/") + ";";
        var f = new Date, g = new RegExp(a + "=(.*?)(;|$)", "i");
        switch (c) {
            case void 0:
                a = document.cookie.match(g);
                null != a && (a = decodeURIComponent(a[1]));
                break;
            case null:
                document.cookie =
                    a + "=;" + d + e + "expires=" + (new Date(0)).toUTCString();
                a = !0;
                break;
            default:
                0 == b ? b = "" : (f.setTime(f.getTime() + 864E5 * (b || 30)), b = "expires=" + f.toUTCString()), document.cookie = a + "=" + encodeURI(c) + ";" + d + e + b, a = !0
        }
        return a || ""
    },
    getPlatform: function () {
        var a = navigator.userAgent;
        switch (!0) {
            case !!a.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/):
                return "ios";
            case -1 < a.indexOf("Android") || -1 < a.indexOf("Adr"):
                return "android";
            case !!/MicroMessenger/i.test(a):
                return "weixin";
            default:
                return ""
        }
    },
    bookBigData: function (a) {
        !Q.bookBigData.fn &&
        (Q.bookBigData.fn = []);
        a && Q.bookBigData.fn.push(a);
        Q.bookBigData.json ? Q.bookBigData.next() : Q.loadjs("http://www.17k.com/book/" + Q.book().id + ".js?r=" + (new Date).getTime().toString().substr(0, 8));
        Q.bookBigData.load = function (a) {
            if (!a.list) {
                a.list = [];
                for (var b = 0, c; c = a.volumes[b]; b++)for (var e = 0, f; f = c.chapters[e]; e++)a.list.push(f.id)
            }
            (b = a.client_first) && b.chapter_first && (c = b.chapter_first[Q.book().chapter]) && (a.appTime = 1E3 * (c + b.second_first), a.appTime < (new Date).getTime() && (a.appTime = 0));
            Q.bookBigData.json =
                a;
            Q.bookBigData()
        };
        Q.bookBigData.next = function () {
            if (!Q.bookBigData.isRuning && Q.bookBigData.fn.length && !Q.isEmpty(Q.bookBigData.json)) {
                Q.bookBigData.isRuning = !0;
                var a = Q.bookBigData.fn[0](Q.bookBigData.json);
                Q.bookBigData.fn.shift();
                Q.bookBigData.isRuning = !1;
                a ? (Q.bookBigData.json = null, Q.bookBigData()) : Q.bookBigData.next()
            }
        }
    },
    search: function (a) {
        var c = document.createElement("div");
        c.className = "search_iframe";
        var b = document.createElement("iframe");
        b.frameBorder = 0;
        b.src = "/search_iframe.html#" + a;
        c.appendChild(b);
        document.body.appendChild(c)
    },
    login: function (a) {
        a = a || encodeURIComponent(location.href);
        var c = document.createElement("div");
        c.className = "UserLoginPop";
        var b = document.createElement("a");
        b.className = "qq";
        b.href = "http://passport.17k.com/sns/connect/2?s=4&nextUrl=" + a;
        b.onclick = function () {
            window.sa && sa.track("Login", {task: "h5", guid: Q.guid, logintype: "qq"})
        };
        var d = document.createElement("a");
        d.className = "weibo";
        d.href = "http://passport.17k.com/sns/connect/3?s=4&nextUrl=" + a;
        d.onclick = function () {
            window.sa &&
            sa.track("Login", {task: "h5", guid: Q.guid, logintype: "weibo"})
        };
        var e = document.createElement("a");
        e.className = "renren";
        e.href = "http://passport.17k.com/sns/connect/1?s=4&nextUrl=" + a;
        e.onclick = function () {
            window.sa && sa.track("Login", {task: "h5", guid: Q.guid, logintype: "renren"})
        };
        var f = document.createElement("div");
        f.className = "K17";
        f.innerHTML = '<a href="http://passport.17k.com/m/?url=' + a + "\" onclick=\"sa.trace('Login',{'task':'h5','guid':Q.guid,'logintype':'local'})\">17k\u8d26\u53f7\u767b\u5f55</a><a href=\"http://wap.passport.17k.com/sns/connect/4?s=4&nextUrl=" +
            a + "\" onclick=\"sa.trace('Login',{'task':'h5','guid':Q.guid,'logintype':'wapQQ'})\">wapQQ\u767b\u9646</a><a href=\"http://passport.17k.com/m/register\">\u6ce8\u518c\u8d26\u53f7</a>";
        var g = document.createElement("div");
        g.className = "close";
        g.onclick = function () {
            document.body.removeChild(c)
        };
        document.body.appendChild(c);
        c.style.top = "100%";
        setTimeout(function () {
            c.style.top = 0;
            c.appendChild(b);
            c.appendChild(d);
            c.appendChild(e);
            c.appendChild(f);
            c.appendChild(g)
        }, 200)
    }, 
    reLoadImages: function (a) {
        a = a || [].slice.call(document.getElementsByTagName("img")).filter(function (a) {
                return !!a.getAttribute("data_src")
            });
        var c = document.documentElement.clientHeight, b = document.documentElement.clientWidth;
        a.filter(function (a) {
            var d = a.getBoundingClientRect(), f = a.getAttribute("data_src");
            f && 0 < d.top + a.clientHeight && d.top < c && 0 < d.left + a.clientWidth && d.left < b && (a.src = f, a.removeAttribute("data_src"))
        });
        return a
    },
    when: function (a, c) {
        var b = function () {
            !0 === a() ? c() : setTimeout(b)
        };
        b()
    }
};
Q.guid = function () {
    function a() {
        return (65536 * (1 + Math.random()) | 0).toString(16).substring(1)
    }

    var c = Q.cookie("GUID");
    c || (c = a() + a() + "-" + a() + "-" + a() + "-" + a() + "-" + a() + a() + a(), Q.cookie("GUID", c, 365, "17k.com", "/"));
    return c
}();
Q.referrer = function () {
    var a = document.referrer;
    -1 == a.indexOf("17k.com") && Q.cookie("c_referer_17k", a, 0);
    return Q.cookie("c_referer_17k")
}();
window.addEventListener("DOMContentLoaded", function () {
    var a = Q.reLoadImages();
    a && window.addEventListener("scroll", function () {
        a = Q.reLoadImages(a)
    });
    Q.isLocalStorage || Q.tips("\u63d0\u793a\uff1a\u60a8\u7684\u6d4f\u89c8\u5668\u8bbe\u7f6e\u4e86\u65e0\u75d5\u6d4f\u89c8\uff0c\u4f1a\u5f71\u54cd\u90e8\u5206\u529f\u80fd\u3002\u53ef\u901a\u8fc7\u70b9\u51fb\u6d4f\u89c8\u5668\u53f3\u4e0b\u89d2\u5173\u95ed\u3002")
});
