define("readnovelm/js/common/page/index.0.25.js", ["lib.Zepto", "util.ejs2", "util.Cookie", "xiaoyue.login", "qidian.report", "readnovelm/js/common/utils/debounce.0.7.js", "readnovelm/js/common/components/Toggle.0.7.js", "readnovelm/js/common/components/Tips.0.9.js", "readnovelm/js/common/components/Loading.0.8.js", "readnovelm/js/common/store/searchHistory.0.7.js"], function (require, exports, module) {
    var t = require("lib.Zepto"), o = require("util.ejs2"), e = require("util.Cookie"), i = require("xiaoyue.login"), r = require("qidian.report"), n = require("readnovelm/js/common/utils/debounce.0.7.js"), a = require("readnovelm/js/common/components/Toggle.0.7.js");
    require("readnovelm/js/common/components/Tips.0.9.js"), require("readnovelm/js/common/components/Loading.0.8.js");
    var c = require("readnovelm/js/common/store/searchHistory.0.7.js");
    module.exports = {
        init: function () {
            this.initGlobalAjaxSetting(), this.initGlobalLogin(), this.initHeader(), this.initFooter(), r.config({cgi: "//" + g_data.domains.commonReport + "/qreport"}), r.init({
                path: "subweb",
                cname: "XYmlog"
            }), t("#aGoPCSite").on("click", function () {
                e.set("tf", 1, "readnovel.com", "/")
            })
        }, initGlobalAjaxSetting: function () {
            var o = this, i = {dataType: "json"}, r = e.get("_csrfToken");
            r && (i.data = {_csrfToken: r});
            var n = location.protocol ? location.protocol : "https:";
            t.ajaxSettings = t.extend(!0, t.ajaxSettings, i), t(document).on("ajaxError", function (e, i, r) {
                401 === i.status ? o.gotoLogin({returnurl: n + "//" + g_data.domainPrefix + "m.readnovel.com/loginSuccess?jumpUrl=" + encodeURIComponent(location.href)}) : t.isFunction(r.unexpectError) ? r.unexpectError.call(r.context, i, r) : r.noErrorTips || t.tips("网络异常，请稍后重试"), t.unloading(), t(".loading").removeClass("loading")
            }), t.getJSONSlient = function (o, e, i) {
                return t.isFunction(e) && (i = e, e = void 0), t.ajax({
                    type: "get",
                    url: o,
                    data: e,
                    success: i,
                    noErrorTips: !0
                })
            }, t.postJSON = function (o, e, i) {
                return r && (o += (o.indexOf("?") > -1 ? "&" : "?") + "_csrfToken=" + r), t.ajax({
                    type: "POST",
                    url: o,
                    contentType: "application/json",
                    data: JSON.stringify(e),
                    success: i
                })
            }, t.postSlient = function (o, e, i) {
                return t.isFunction(e) && (i = e, e = void 0), t.ajax({
                    type: "post",
                    url: o,
                    data: e,
                    success: i,
                    noErrorTips: !0
                })
            }
        }, initGlobalLogin: function () {
            var o = this;
            i.init(!0), i.setCallback("success", function () {
                location.href = location.href.split("#")[0]
            }), t("a.jsLogin").removeClass(".jsLogin").on("click", function () {
                var e = t(this), i = e.prop("href").indexOf("http") > -1 ? e.prop("href") : "", r = location.protocol ? location.protocol : "https:";
                return o.gotoLogin({returnurl: r + "//" + g_data.domainPrefix + "m.readnovel.com/loginSuccess?jumpUrl=" + encodeURIComponent(i)}), !1
            })
        }, gotoLogin: function (t) {
            t || (t = {});
            var o = t.returnurl || location.href;
            o.indexOf("?") > -1 ? o += "&from=login" : o += "?from=login", t.returnurl = o, location.href = i.getMLoginUrl(t)
        }, initHeader: function () {
            var o, e = this, i = t(".jsBack").on("click", function () {
                if (/^javas/.test(this.href))return history.go(-1), !1
            });
            t("#openSearchPopup").on("click", function () {
                var i = t(this).data("keyword") || "";
                return t("#searchPopup").removeAttr("hidden"), t("#keyword").attr("placeholder", i).focus(), o || (o = !0, e.initSearch()), !1
            });
            var r;
            t("#newOpen").on("click", function () {
                var o = t(this).data("keyword") || "", i = t(this).data("id");
                return t("#listsearchPopup").removeAttr("hidden"), t("#listkeyword").attr("placeholder", o).focus(), t("#listId").val(i), r || (r = !0, e.initlistSearch(i)), !1
            }), new a("#openGuide", function (t) {
                var o = t.attr("title"), i = t.parents("header");
                /收起/.test(o) ? (t.attr("title", o.replace("收起", "")), i.removeAttr("open")) : (i.attr("open", ""), t.attr("title", "收起" + o), e.elBacktop && e.elBacktop.trigger("click"))
            }), t("#guideOverlay").on("click", function () {
                t("#openGuide").trigger("click")
            });
            var n = t("#guide").on("touchmove", function (t) {
                t.preventDefault()
            });
            n.length && (window.addEventListener("resize", function () {
                e.elBacktop && n.hasClass("active") && e.elBacktop.trigger("click")
            }), t("#header").on("touchmove", function (t) {
                n.hasClass("active") && t.preventDefault()
            })), t("#header nav a").on("click", function (t) {
                var o = this.href;
                !1 === /^#/.test(o) && !1 === /^javasc/.test(o) && (t.preventDefault(), history.replaceState(null, document.title, o.split("#")[0] + "#"), location.replace(""))
            }).each(function () {
                var o = t(this);
                o.parent("h3").attr({role: "navigation", title: o.hasClass("active") ? "已选择" : ""})
            });
            var c = document.referrer;
            "string" == typeof c && "" === c && i.attr("href", "/")
        }, initFooter: function () {
            var o = t(window), e = t(".jsBackToTop").on("click", function () {
                var t = o.scrollTop(), e = function () {
                    t *= .5, t < 1 ? o.scrollTop(0) : (o.scrollTop(t), requestAnimationFrame(e))
                };
                return e(), !1
            });
            this.elBacktop = e;
            var i = t("#footerApp");
            o.on("scroll", function () {
                var t = o.scrollTop(), r = o.height();
                if (t > r && (!i.length || t + r - i.position().top <= 0))return void e.css({
                    opacity: 1,
                    visibility: "visible"
                });
                e.css({opacity: 0, visibility: "hidden"})
            });
            var r = navigator.userAgent && /\bAndroid ([\d\.]+)/.test(navigator.userAgent);
            t(".jsDownloadLink").each(function () {
                var o = t(this);
                /^javas/.test(o.attr("href")) && (o.attr("href", r ? o.data("android") : o.data("ios")), o.removeAttr("data-android"), o.removeAttr("data-ios"))
            })
        }, initSearch: function () {
            t("#closeSearchPopup").on("click", function () {
                t("#searchPopup").attr("hidden", ""), t("body").removeClass("open")
            }), this.initSearchForm(), this.initSearchPopularWords(), this.initSearchHistory()
        }, initSearchForm: function () {
            var e = t("#keyword"), i = t("#clearSearchKeyword"), r = t("#searchHotHistory"), a = t("#searchList"), s = e.val(), l = function () {
                s && t.getJSON("/majax/search/auto?kw=" + s, function (t) {
                    if (0 === t.code) {
                        if (!s)return;
                        r.attr("hidden", ""), a.removeAttr("hidden").html(o.render("#tpl-search-list.html", t))
                    }
                })
            }, d = n(l, 300);
            e.on("input", function () {
                s = t(this).val().trim(), s ? (i.removeAttr("hidden"), d()) : (i.attr("hidden", ""), r.removeAttr("hidden"), a.attr("hidden", ""))
            }), i.on("click", function () {
                e.val("").trigger("input").focus()
            }), s && (i.removeAttr("hidden"), r.attr("hidden", ""), l()), t("#searchPopup").on("click", ".jsSearchLink", function () {
                s = t(this).data("keyword"), c.addKeyword(s), e.val(s)
            }).on("touchmove", function (t) {
                t.preventDefault()
            }), t("#searchForm").on("submit", function () {
                s = e.val().trim(), !s && e.attr("placeholder") && (s = e.attr("placeholder"), e.val(s)), c.addKeyword(s)
            })
        }, initSearchPopularWords: function () {
            t.getJSON("/majax/search/auto?kw=", function (e) {
                if (0 === e.code) {
                    var i = o.render("#tpl-search-popular-words.html", e), r = t("#searchPopularWords").html(i);
                    t("#keyword").val() ? r.height("auto") : r.unloading(200)
                }
            })
        }, initSearchHistory: function () {
            var e = c.getKeywords(), i = o.render("#tpl-search-history.html", {data: e}), r = t("#searchHistory").html(i);
            t("#clearSearchHistory").on("click", function () {
                c.clearKeywords(), r.attr("hidden", "")
            })
        }, initlistSearch: function (o) {
            t("#listcloseSearchPopup").on("click", function () {
                t("#listsearchPopup").attr("hidden", ""), t("body").removeClass("open")
            }), this.initlistSearchForm(o), this.initlistSearchPopularWords(o), this.initlistSearchHistory(o)
        }, initlistSearchForm: function () {
            var e = t("#listkeyword"), i = t("#listclearSearchKeyword"), r = t("#listsearchHotHistory"), a = t("#listsearchList"), s = e.val(), l = function () {
                s && t.getJSON("/majax/search/auto?kw=" + s, function (t) {
                    if (0 === t.code) {
                        if (!s)return;
                        r.attr("hidden", ""), a.removeAttr("hidden").html(o.render("#tpl-listsearch-list.html", t))
                    }
                })
            }, d = n(l, 300);
            e.on("input", function () {
                s = t(this).val().trim(), s ? (i.removeAttr("hidden"), d()) : (i.attr("hidden", ""), r.removeAttr("hidden"), a.attr("hidden", ""))
            }), i.on("click", function () {
                e.val("").trigger("input").focus()
            }), s && (i.removeAttr("hidden"), r.attr("hidden", ""), l()), t("#listsearchPopup").on("click", ".jsSearchLink", function () {
                s = t(this).data("keyword"), c.addKeyword(s), e.val(s)
            }).on("touchmove", function (t) {
                t.preventDefault()
            }), t("#listsearchForm").on("submit", function () {
                s = e.val().trim(), !s && e.attr("placeholder") && (s = e.attr("placeholder"), e.val(s)), c.addKeyword(s)
            })
        }, initlistSearchPopularWords: function (e) {
            t.getJSON("/majax/search/auto?kw=", function (i) {
                if (0 === i.code) {
                    i.data.id = e;
                    var r = o.render("#tpl-listsearch-popular-words.html", i), n = t("#listsearchPopularWords").html(r);
                    t("#listkeyword").val() ? n.height("auto") : n.unloading(200)
                }
            })
        }, initlistSearchHistory: function (e) {
            var i = c.getKeywords(), r = o.render("#tpl-listsearch-history.html", {data: [i, e]}), n = t("#listsearchHistory").html(r);
            t("#listclearSearchHistory").on("click", function () {
                c.clearKeywords(), n.attr("hidden", "")
            })
        }
    }
});
define("readnovelm/js/homepage/index.0.21.js", ["lib.Zepto", "util.ejs2", "xiaoyue.login", "readnovelm/js/common/page/merge.0.7.js", "readnovelm/js/common/mixin/lazyLoad.0.7.js", "readnovelm/js/common/components/Aside.0.7.js", "readnovelm/js/common/components/Loading.0.8.js", "readnovelm/js/common/components/Tab.0.7.js", "readnovelm/js/common/components/Swipe.0.7.js", "readnovelm/js/common/components/hScroll.0.7.js", "readnovelm/js/common/components/Inertia.0.7.js", "readnovelm/js/common/store/recentlyRead.0.8.js"], function (require, exports, module) {
    var o = require("lib.Zepto"), e = require("util.ejs2"), n = require("xiaoyue.login"), t = require("readnovelm/js/common/page/merge.0.7.js"), i = require("readnovelm/js/common/mixin/lazyLoad.0.7.js"), a = require("readnovelm/js/common/components/Aside.0.7.js");
    require("readnovelm/js/common/components/Loading.0.8.js");
    var s = require("readnovelm/js/common/components/Tab.0.7.js"), r = require("readnovelm/js/common/components/Swipe.0.7.js"), l = require("readnovelm/js/common/components/hScroll.0.7.js"), d = require("readnovelm/js/common/components/Inertia.0.7.js"), c = require("readnovelm/js/common/store/recentlyRead.0.8.js");
    module.exports = t({
        mixins: [i], init: function () {
            l("ol.module-slide-ol"), this.initSlide(), this.initFree(), this.initTab(), this.initAside(), this.initMoveAble(), this.initUser()
        }, initSlide: function () {
            var e = o("#slideContainer"), n = o("#slideDot i");
            new r(e[0], {
                auto: 3e3, transitionEnd: function (o) {
                    n.removeClass("active"), n.eq(o).addClass("active")
                }
            })
        }, initFree: function () {
            var e = o("#time");
            o.getJSONSlient("/majax/free/sysTime", function (n) {
                var t = e.children(), i = parseInt(e.attr("data-endtime"), 10), a = i - parseInt(n.data.sysTime, 10), s = null, r = function (o) {
                    return o > 0 ? o <= 9 ? "0" + o : "" + o : "00"
                };
                a || (a = 0);
                var l = function () {
                    if (a >= 0) {
                        var e = [r(Math.floor(a / 86400)), r(Math.floor(a % 86400 / 3600)), r(Math.floor(a % 3600 / 60)), r(a % 60)];
                        t.each(function (n) {
                            void 0 !== e[n] && o(this).html(e[n])
                        })
                    } else t.html("00"), clearInterval(s);
                    a -= 1
                };
                l(), s = setInterval(l, 1e3), e.removeAttr("data-seconds")
            })
        }, initTab: function () {
            var e = this, n = /iphone.*micromess/i.test(navigator.userAgent);
            o(".moduleTab").each(function () {
                new s(o(this).find("a"), {
                    aria: "h3", callback: function (o, t, i) {
                        n && i.addClass("WX"), e.updateLazyLoad()
                    }
                })
            })
        }, initAside: function () {
            new a(o("#asideTrigger"), {
                onInit: function () {
                    var n = {data: c.getBooks()};
                    o("#recentlyRead").html(e.render("#tpl-recently-read.html", n))
                }
            })
        }, initMoveAble: function () {
            var e = o("#asideTrigger"), n = new d(e[0]), t = o(g_data.container || window);
            "" === n.getPosition() && t.on("scroll", function () {
                var o = t.height(), n = t.scrollTop();
                document.documentElement.scrollHeight <= o + n + 1 ? e.css("visibility", "hidden") : e.css("visibility", "visible")
            })
        }, initUser: function () {
            var e = this, n = !1;
            o.getJSONSlient("/majax/user/userInfo", function (t) {
                0 === t.code && (t.data && t.data.user.isLogin ? (o(".jsGuestWrapper").attr("hidden", "").css("display", "none"), o(".jsUserWrapper").removeAttr("hidden").css("display", ""), o("#avatar").attr("src", t.data.user.avatar).attr("alt", "用户" + t.data.user.nickName + "的个人中心"), o(".page").addClass("login").removeClass("unlogin"), o("body").addClass("login").removeClass("unlogin"), o(".elformark").addClass("el-booklist-mark"), n = !0, e.initMark(n)) : (o(".jsGuestWrapper").removeAttr("hidden").css("display", ""), o(".jsUserWrapper").attr("hidden", "").css("display", "none"), o("#aUserCenter").addClass("jsLogin"), o(".elformark").addClass("jsBookListLogin"), n = !1, e.initMark(n)))
            })
        }, gotoLogin: function (o) {
            o || (o = {});
            var e = o.returnurl || location.href;
            e.indexOf("?") > -1 ? e += "&from=login" : e += "?from=login", o.returnurl = e, location.href = n.getMLoginUrl(o)
        }, initMark: function (e) {
            var t = this, i = o(".elformark"), a = [], s = 0;
            i.forEach(function (e) {
                a.push(o(e).attr("data-id"))
            }), o.postSlient("/majax/booklist/countCollectedBatch", {ids: a.join(",")}, function (e) {
                0 === e.code && a.forEach(function (n) {
                    o('span[data-id="' + n + '"]').find(".el-booklist-count").text(e.data[n]), o('span[data-id="' + n + '"]').next(".el-booklist-markdone").find(".el-mark-count").text(e.data[n])
                })
            });
            var r = function () {
                o.postSlient("/majax/booklist/checkCollectBatch", {ids: a.join(",")}, function (e) {
                    0 === e.code && a.forEach(function (n) {
                        1 == e.data[n] && (o('span[data-id="' + n + '"]').addClass("hidden"), o('span[data-id="' + n + '"]').next(".el-booklist-markdone").removeClass("hidden"))
                    })
                })
            };
            e ? r() : n.setCallback("success", r), o(".booklist-module").on("click", ".el-booklist-mark", function (e) {
                e.preventDefault();
                var n = o(this), t = n.attr("data-id");
                s = parseInt(n.find(".el-booklist-count").text(), 10);
                var i = {id: t};
                o.postSlient("/majax/booklist/collect", i, function (e) {
                    0 === e.code ? (n.addClass("hidden"), n.next(".el-booklist-markdone").removeClass("hidden"), n.next(".el-booklist-markdone").find(".el-mark-count").text(s + 1), o.tips("收藏成功")) : o.tips(e.msg)
                })
            }), o(".jsBookListLogin").on("click", function (o) {
                o.preventDefault();
                var e = location.protocol ? location.protocol : "https:";
                t.gotoLogin({returnurl: e + "//" + g_data.domainPrefix + "m.readnovel.com/loginSuccess?jumpUrl=" + encodeURIComponent("")})
            })
        }
    })
});