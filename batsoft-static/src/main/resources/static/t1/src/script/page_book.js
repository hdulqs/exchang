/**
 * Created by Bat Admin on 2016/03/21.
 * book主页
 */
define(function (require, exports) {
    var core = require('./module_core'),
        mMd5 = require('./module_md5'),
        mVad = require('./module_validator'), //表单校验
        tpl = require('./module_tpl'),
        swal = require('./module_swal'), //弹窗 alert
        v = require('./module_validate');

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
            for (var c = location.href.substr(1).split("&"), b = 0, d = {}, e; e = c[b++];)
            // for (var c = location.search.substr(1).split("&"), b = 0, d = {}, e; e = c[b++];)
                e = e.split("="), d[e[0]] = e[1];
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
            return !!GLOBAL.user
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
            d = "domain=" + (d || "aishu8.com") + ";";
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
            Q.bookBigData.json ? Q.bookBigData.next() : Q.loadjs("http://www.aishu8.com/book/" + Q.book().id + ".js?r=" + (new Date).getTime().toString().substr(0, 8));
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
            b.src = "/book/search_home#" + a;
            c.appendChild(b);
            document.body.appendChild(c)
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
        c || (c = a() + a() + "-" + a() + "-" + a() + "-" + a() + "-" + a() + a() + a(), Q.cookie("GUID", c, 365, "aishu8.com", "/"));
        return c
    }();
    Q.referrer = function () {
        var a = document.referrer;
        -1 == a.indexOf("aishu8.com") && Q.cookie("c_referer_aishu8", a, 0);
        return Q.cookie("c_referer_aishu8")
    }();
    window.addEventListener("DOMContentLoaded", function () {
        var a = Q.reLoadImages();
        a && window.addEventListener("scroll", function () {
            a = Q.reLoadImages(a)
        });
        Q.isLocalStorage || Q.tips("\u63d0\u793a\uff1a\u60a8\u7684\u6d4f\u89c8\u5668\u8bbe\u7f6e\u4e86\u65e0\u75d5\u6d4f\u89c8\uff0c\u4f1a\u5f71\u54cd\u90e8\u5206\u529f\u80fd\u3002\u53ef\u901a\u8fc7\u70b9\u51fb\u6d4f\u89c8\u5668\u53f3\u4e0b\u89d2\u5173\u95ed\u3002")
    });

    function index() {
        //nanner图循环
        core.list({url: "/banner/listByType",data:{"type":"book"},type:"POST"}).then(function (data, a, b) {
            // tpl.render({"tplId": "#swiper-wrapper_Tmpl", "renderId": "#index-swiper-wrapper", "data": data.data});
            var Tmpl = $("#swiper-wrapper_Tmpl").render(data.data);
            var wrapper = $("#index-swiper-wrapper");
            wrapper.append(Tmpl);
            new Swiper('.swiper-container', {
                loop: true,
                pagination: '.swiper-pagination',
                autoplay: 4000,
                speed: 300,
                autoplayDisableOnInteraction: false
            })
        });
        // 精品推荐
        core.list({url: "/book/recommendBooks?size=12"}).then(function (data, a, b) {
            tpl.render({"tplId": "#recommendNavTmpl", "renderId": "#jinxuantuijian_ID", "data": data});
            new Swiper('.swiper-jinxuantuijian', {
                loop: true,
                //pagination: '.swiper-pagination',
                autoplay: 6000,
                speed: 400,
                autoplayDisableOnInteraction: false
            })
        });
        // 男生最爱
        core.list({url: "/book/sexTypeBooks?sexType=1"}).then(function (data, a, b) {
            var manNavMore = $("#shudan_slide_id_Tmpl").render(data);
            var shudan_slide = $("#shudan_slide_id");
            shudan_slide.append(manNavMore);

            (function (shudan_slide) {
                var height = shudan_slide.clientHeight;
                var pages = Math.floor(shudan_slide.scrollHeight / height);
                var index = 0;
                document.getElementById('Shudan').addEventListener('click', function () {
                    index++;
                    if (index == pages) {
                        index = 0;
                    }
                    shudan_slide.scrollTop = index * height;
                });
            }(document.querySelector('.shudan_slide')));
        });
        //女生最爱
        core.list({url: "/book/sexTypeBooks?sexType=0&size=12"}).then(function (data, a, b) {
            tpl.render({"tplId": "#recommendNavTmpl", "renderId": "#nvshengzuiai_id", "data": data});
            new Swiper('.swiper-huobaorexiao', {
                loop: true,
                //pagination: '.swiper-pagination',
                autoplay: 6000,
                speed: 400,
                autoplayDisableOnInteraction: false
            })
        });

        $("#search_input").on("click",function(){
            Q.search($(this).val());
        })
    }

    function online() {
        if (top == window && navigator.onLine) {
            var userInfo = GLOBAL.user;
            Q.setLoginInfo = function (a) {
                if(Q.isLogin()){
                    if(userInfo.userAvatar!=undefined){
                        a.innerHTML = '<img src="'  +GLOBAL.config.fileHost+ userInfo.userAvatar + '">';
                    }else{
                        a.innerHTML = '<img src="'  +HTTP_STATIC_DOMAIN +"/static/front/dist/img/book/default_4.jpg"+'">';
                    }

                    a.style.boxShadow = "0 0 3px rgba(0, 0, 0, .5)";
                    a.href = "/book/user/info";
                }else{
                    a.onclick = function () {
                        window.location.href="/h5/login";
                    }
                }

            };
            window.addEventListener("DOMContentLoaded", function () {
                (new Image).src = "http://api.share.baidu.com/s.gif?l=" +
                    encodeURIComponent(location.href) + (document.referrer ? "&r=" + encodeURIComponent(document.referrer) : "");
                var a = Q.cookie("TOP_DON_COOKIE");
                if (!a) {
                    var b = document.createElement("style");
                    b.setAttribute("text", "text/css");
                    b.innerText = ".all_don,.btn_don,.mid_don,.top_don{-webkit-transition: all .3s ease;transition: all .3s ease;position:fixed;max-width:600px;font-size:0;z-index:5}.all_don img,.btn_don img,.mid_don img,.top_don img{width:100%}.all_don span,.btn_don span,.mid_don span,.top_don span{display:inline-block;position:absolute;bottom:0;left:0}.all_don a,.btn_don a,.mid_don a,.top_don a{display:inline-block}.all_don{position:fixed;z-index:300;top:0;right:0;left:0;bottom:0}.all_don img{height:100%}.all_don span{width:48.5%;height:8.8%;left:26%;top:29.5%}.all_don a{position:absolute;width:48.5%;height:8.8%;left:26%;top:20%}.top_don{position:relative;z-index:6}.top_don span{z-index:6;width:11.5%;height:100%}.top_don a img{width:100%}.btn_don{bottom:0}.btn_don span{width:11.5%;height:100%}.mid_don{bottom:-25%;overflow:hidden;}.mid_don span{background: url(http://img.aishu8.com/subject/wuyuepay/skin/close.png) center center no-repeat;width: 10%;height: 25%;position: absolute;top: 0;left: 90%;z-index:3;}.mid_don a{width: 100%;height: 100%;position: absolute;}.book_home_float {position: fixed;top: 17.5em;z-index: 1;right: 3px;width: 5em;}.book_home_float a{display:block;width: 5em;height: 5.1em;background: url(http://img.aishu8.com/subject/scfq/skin/book_home_float.png) center center no-repeat;background-size: 100%;}";
                    document.head.appendChild(b)
                }
                if (!a && (GetUrlRelativePath("/") || GetUrlRelativePath("/topv3") || GetUrlRelativePath("/list") || GetUrlRelativePath("/chapter/")) && -1 == Q.cookie("c_csc").indexOf("smss")) {
                    a = '<span class="top_don_span" onclick="deldon(this)"></span><a onclick="anorios(this)"  href="http://h5.aishu8.com/download/index.html?hmsr=shufuceng500"><img src="http://img.aishu8.com/subject/scfq/skin/top_don_bg.jpg" alt=""></a>';
                    GetUrlRelativePath("/chapter/") && (a = "");
                    createfirstChild(a, "top_don");
                    var c = document.querySelector(".top_don");
                    if (GetUrlRelativePath("/")) {
                        var d = document.body, e = document.querySelector(".Head");
                        e.style.cssText = "position:relative;";
                        d.style.marginTop = "0";
                        var f = c.offsetHeight;
                        window.addEventListener("scroll", function () {
                            document.body.scrollTop <= f ? (d.style.marginTop = "0", e.style.cssText = "position:relative;", c.style.display = "block") : (d.style.marginTop = "3em", e.style.cssText = "position:fixed;top:0;", c.style.display = "none")
                        })
                    }
                }
                GetUrlRelativePath("/book/") && createfirstChild('<a href="http://h5.aishu8.com/inc/yindao02.html" target="_blank"></a>',
                    "book_home_float")
            });
            var GetUrlRelativePath = function (a) {
                return "/book/" == a ? /^(http:\/\/h5.aishu8.com\/book\/)(\d+)(\.html)([^\/]*$)/.test(location.href) : "/" == a ? /^(http:\/\/h5.aishu8.com\/$)/.test(location.href) : 0 <= location.pathname.indexOf(a)
            },
                createfirstChild = function (a, b) {
                var c = document.createElement("div");
                c.className = b;
                c.innerHTML = a;
                document.body.insertBefore(c, document.body.firstChild)
            },
                weixinTip = function (a) {
                a.href = "http://h5.aishu8.com/download/index.html";
                window.event ? window.event.returnValue = !1 : a.preventDefault();
                createfirstChild(' <div class="wxtip" id="JweixinTip"><img style="width:100%" src="http://img.aishu8.com/touch/skin/download/weixinTip_yinliu.jpg" /></div>');
                a = document.getElementById("JweixinTip");
                a.style.cssText = "z-index:999;background:rgba(255,255,255,.9);text-align:center;position:fixed;left:0;top:0;width:100%;height:100%;z-index:100;display:none";
                a.style.display = "block";
                a.onclick = function () {
                    this.style.display = "none"
                }
            },
                anorios = function (a) {
                var b = navigator.userAgent, c = !!b.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
                    d = -1 < b.indexOf("Android") || -1 < b.indexOf("Adr"), b = !!/MicroMessenger/i.test(b);
                (c || d) && c && b && weixinTip(a)
            },
                deldon = function (a) {
                if ("top_don_span" == a.className && (Q.cookie("TOP_DON_COOKIE", "1", 1 / 24), GetUrlRelativePath("/"))) {
                    var b = document.getElementsByTagName("body")[0], c = document.getElementsByClassName("Head")[0];
                    b.style.marginTop = "3em";
                    c.style.position = "fixed";
                    top_donclick = c.style.top = 0
                }
                a.parentNode.remove()
            }
        }
        ;

        Q.setLoginInfo(document.getElementById('UserLoginIcon'));
    }

    /**
     * 初始化类别页面
     */
    function category() {
        //分类。男生
        core.list({url: "/book/sexTypeBooks?sexType=1&size=5"}).then(function (data, a, b) {
            var category_parts = $("#category_parts_Tmpl").render(data);
            var category_part2 = $("#category_part2_id");
            category_part2.append(category_parts);
        });
        //分类。女生
        core.list({url: "/book/sexTypeBooks?sexType=0&size=5"}).then(function (data1, a, b) {
            var category_parts = $("#category_parts_Tmpl").render(data1);
            var category_part3 = $("#category_part3_id");
            category_part3.append(category_parts);
        });
        //分类。综合
        core.list({url: "/book/sexTypeBooks?sexType=2&size=5"}).then(function (data2, a, b) {
            var category_parts = $("#category_parts_Tmpl").render(data2);
            var category_part4 = $("#category_part4_id");
            category_part4.append(category_parts);
        });
        var getCategoryList = function (site) {
            Q.jsonp('http://api.aishu8.com/v2/book/category/' + site,
                {
                    app_key: 1351550300
                },
                function (data) {
                    if (data.status.code == 0) {
                        var html = ['<dl class="fenlei_tab">'];
                        data.data.child_node.forEach(function (c_1) {
                            html.push('<dt><a href="all.html?site=' + site + '&category_1=' + c_1.id + '"><span>' + c_1.name + '</span></a></dt>');
                            html.push('<dd>');
                            c_1.child_node.forEach(function (c_2) {
                                html.push('<a href="all.html?site=' + site + '&category_1=' + c_1.id + '&category_2=' + c_2.id + '">' + c_2.name + '</a>')
                            });
                            html.push('</dd>');
                        });
                        html.push('</dl>');
                        document.querySelector("#channles").innerHTML = html.join('');
                    }
                });
        };
        (function () {
            //tab事件
            var label = document.querySelectorAll(".NAV label");
            for (var i = 0; i < label.length; i++) {
                label[i].addEventListener('click', function () {
                    for (var i = 0; i < label.length; i++) {
                        label[i].className = '';
                    }
                    this.className = 'now';
                    getCategoryList(this.getAttribute('rel'));
                });
            }
            label[0].click();
            //根据来源默认选择tab
            if (window.location.href.indexOf("?f=mm") == 0) {
                document.querySelector("#mmNav").click();
            }
        }())

    }

    function free() {
        core.list({url: "/book/findBooksByTypeId"}).then(function (data, a, b) {
            var free_bookList = $("#free_bookList_id");
            var typeId = "";
            var htmlTemplate = "";
            if(data.length>0){
                for(var i=0;i<data.length;i++){
                    var section = '<section class="bookList">'
                    +'<div><span>'+data[i].typeValue+'</span><a href="/book/all/'+data[i].typeId+'">查看全部&gt;&gt;</a> </div>'
                       + '<dl>';
                    var _section = '</dl> </section>';
                    // var ddValue = '<dd><a href="http://h5.aishu8.com/book/389907.html"><img alt="'+data[i].name+'" src="http://img.aishu8.com/images/bookcover/2012/1949/9/389907-1352196505000_1.jpg" /><span>'+data[i].name+'</span></a></dd>';
                    var ddValue = '<dd><a href="/book/info/'+data[i].id+'">';
                       if(data[i].logo==null||data[i].logo==''){
                           ddValue = ddValue+'<img alt="'+data[i].name+'" src="@{${@globalService.staticUrl()}+\'/static/front/src/img/book/noImage.jpg\'}" />';
                       }else{
                           ddValue = ddValue+'<img alt="'+data[i].name+'" src="'+data[i].logo+'" />';
                       }
                        ddValue = ddValue+'<span>'+data[i].name+'</span></a></dd>';

                    if(i==0){
                        // free_bookList.append(section).append(ddValue);
                        htmlTemplate=htmlTemplate+section+ddValue;
                    }else{
                        if(data[i].typeId!=typeId){
                            htmlTemplate=htmlTemplate+_section+section+ddValue;
                            // free_bookList.append(_section).append(section).append(ddValue);
                        }else{
                            if(i==data.length-1){
                                htmlTemplate = htmlTemplate+ddValue+_section;
                                // free_bookList.append(ddValue).append(_section);
                            }else{
                                htmlTemplate=htmlTemplate+ddValue;
                                // free_bookList.append(ddValue);
                            }
                        }
                    }
                    typeId = data[i].typeId;
                }
                free_bookList.append(htmlTemplate);
            }
        });
    }
    
    function rank() {
        var sexType = 2;
        var page = 1;
        var size = 3;
        var styles = document.querySelectorAll('label');
        var dingyue = $("#part1_paihangbang_id");  //订阅榜
        var newdj = $("#part2_paihangbang_id");   //新书点击
        var dj = $("#part3_paihangbang_id");       //点击榜
        var wanben = $("#part4_paihangbang_id");    //完本榜

        var templateId = $("#paihangbang_Tmpl");
        var callBack = function (data,navId,templateId) {
            var dataTemplate = templateId.render(data);
            navId.append(dataTemplate);
        }

        //查询
        dingyueF(sexType,page,size,callBack,dingyue,templateId);
        newdjF(sexType,page,size,callBack,newdj,templateId);
        djF(sexType,page,size,callBack,dj,templateId);
        wanbenF(sexType,page,size,callBack,wanben,templateId);
        //总榜
        $("#rank_part1").on("click",function(){
            dingyue.empty();
            newdj.empty();
            dj.empty();
            wanben.empty();
            dingyueF(sexType,page,size,callBack,dingyue,templateId);
            newdjF(sexType,page,size,callBack,newdj,templateId);
            djF(sexType,page,size,callBack,dj,templateId);
            wanbenF(sexType,page,size,callBack,wanben,templateId);
            cannelStle(styles);
            document.querySelector("#rank_part1").className = 'now'
        });
        //男生
        $("#rank_part2").on("click",function(){
            var sexType0 = 1;
            dingyue.empty();
            newdj.empty();
            dj.empty();
            wanben.empty();
            dingyueF(sexType0,page,size,callBack,dingyue,templateId);
            newdjF(sexType0,page,size,callBack,newdj,templateId);
            djF(sexType0,page,size,callBack,dj,templateId);
            wanbenF(sexType0,page,size,callBack,wanben,templateId);
            cannelStle(styles);
            document.querySelector("#rank_part2").className = 'now'
        });
        //女生
        $("#rank_part3").on("click",function(){
            var sexType0 = 0;
            dingyue.empty();
            newdj.empty();
            dj.empty();
            wanben.empty();
            dingyueF(sexType0,page,size,callBack,dingyue,templateId);
            newdjF(sexType0,page,size,callBack,newdj,templateId);
            djF(sexType0,page,size,callBack,dj,templateId);
            wanbenF(sexType0,page,size,callBack,wanben,templateId);
            cannelStle(styles);
            document.querySelector("#rank_part3").className = 'now'
        });
        //个性化
        $("#rank_part4").on("click",function(){
            var sexType0 = 3;
            dingyue.empty();
            newdj.empty();
            dj.empty();
            wanben.empty();
            dingyueF(sexType0,page,size,callBack,dingyue,templateId);
            newdjF(sexType0,page,size,callBack,newdj,templateId);
            djF(sexType0,page,size,callBack,dj,templateId);
            wanbenF(sexType0,page,size,callBack,wanben,templateId);
            cannelStle(styles);
            document.querySelector("#rank_part4").className = 'now'
        });

    }

    function types(){
        var type = $("#typeAll_typeId").val();
        var page = $("#typeAll_page").val();
        var size = 20;
        var sexType = 2;
        var navId = $("#typeAll_paihangbang_id");
        var templateId = $("#typeAll_paihangbang_Tmpl");
        var callBack = function (data,navId,templateId) {
            var dataTemplate = templateId.render(data);
            navId.append(dataTemplate);
        }
        switch (type){
            case "dingyue":dingyueF(sexType,page,size,callBack,navId,templateId); break;
            case "hongbao":dingyueF(sexType,page,size,callBack,navId,templateId); break;
            case "xinshu":newdjF(sexType,page,size,callBack,navId,templateId); break;
            case "huiyuan":djF(sexType,page,size,callBack,navId,templateId); break;
            case "wanben":wanbenF(sexType,page,size,callBack,navId,templateId); break;
        }
    }
    //订阅榜查询
    function dingyueF(sexType0,page0,size0,callBack,navId,templateId) {
        core.list({url: "/book/findByShelfAndSex",data:{"sexType":sexType0,"size":size0,"page":page0},type:"POST"}).then(function (data, a, b) {
            callBack(data.data,navId,templateId);
        });
    }
    //新书点击查询
    function newdjF(sexType0,page0,size0,callBack,navId,templateId) {
        core.list({url: "/book/findNewFictions",data:{"sexType":sexType0,"size":size0,"page":page0},type:"POST"}).then(function (data, a, b) {
            callBack(data.data,navId,templateId);
        });
    }
    //点击榜查询
    function djF(sexType0,page0,size0,callBack,navId,templateId) {
        core.list({url: "/book/findByClickNum",data:{"sexType":sexType0,"size":size0,"page":page0},type:"POST"}).then(function (data, a, b) {
            callBack(data.data,navId,templateId);
        });
    }
    //wanben查询
    function wanbenF(sexType0,page0,size0,callBack,navId,templateId) {
        core.list({url: "/book/findByStatusAndNum",data:{"sexType":sexType0,"size":size0,"page":page0},type:"POST"}).then(function (data, a, b) {
            callBack(data.data,navId,templateId);
        });
    }
    function all() {
        var typeId = $("#all_bookTypeId").val();
        var page = 1;
        var size = 10;
        var type = 0;
        var call = function (dataV) {
            var booksContent_Tmpl = $("#booksContent_Tmpl").render(dataV);
            var booksContent = $("#booksContent");
            booksContent.empty();
            booksContent.append(booksContent_Tmpl);
        }
        var styles = document.querySelectorAll('label');
        allList(type,typeId,page,size,call);
        //分类填充
        core.list({url: "/book/findTypes"}).then(function (data, a, b) {
            var fenleiNav_Tmpl = $("#fenleiNav_Tmpl").render(data);
            var fenleiNav = $(".fenleiNav");
            fenleiNav.append(fenleiNav_Tmpl);
        });
        //点击榜切换
        $("#sort_type-4").on("click",function(){
            allList(type,typeId,page,size,call);
            cannelStle(styles);
            document.querySelector("#sort_type-4").className = 'now'
        });
        //收藏切换
        $("#sort_type-7").on("click",function(){
            var typeId1 = $("#all_bookTypeId").val();
            var type = 1;
            allList(type,typeId1,page,size,call);
            cannelStle(styles);
            document.querySelector("#sort_type-7").className = 'now'
        });
        //免费切换
        $("#book_free-1").on("click",function(){
            var typeId1 = $("#all_bookTypeId").val();
            var type1 = 2;
            allList(type1,typeId1,page,size,call);
            cannelStle(styles);
            document.querySelector("#book_free-1").className = 'now'
        });
        //完本切换
        $("#book_status-3").on("click",function(){
            var typeId1 = $("#all_bookTypeId").val();
            var type1 = 3;
            allList(type1,typeId1,page,size,call);
            cannelStle(styles);
            document.querySelector("#book_status-3").className = 'now'
        });
        //分类
        $("#book_fenlei").on("click",function(){
            var box = document.querySelector('.fenleiNav');
            if (box.clientHeight) {
                box.style.display = 'none';
            } else {
                box.style.display = 'block';
            }
            cannelStle(styles);
            document.querySelector("#book_fenlei").className = 'now'
        });
    }
    function allList(type,bookTypeId,page,size,call) {
        var dataV = "";
        core.list({url: "/book/allByType",data:{"type":type,"bookTypeId":bookTypeId,"page":page,"size":size}}).then(function (data, a, b) {
            if(data.success){
                dataV = data.data;
            }else{
                dataV = null;
            }
            call(dataV);
        });
    }
    function cannelStle(styles) {
        for(var i=0;i<styles.length;i++){
            styles[i].className = '';
        }
    }
    function info() {
        //同类小说
        var typeId = $("#fiction_typeId").val();
        var bookId = $("#fiction_bookId").val();
        var readerLikeList = $("#readerLikeList");
        core.list({url: "/book/findFictionsByBookTypeId?typeId="+typeId+"&size=4"}).then(function (data2, a, b) {
            var readerLikeList_Tmpl = $("#readerLikeList_Tmpl").render(data2);
            readerLikeList.append(readerLikeList_Tmpl);
        });
        (function(readerLikeList) {
            var height = readerLikeList.clientHeight;
            var pages = Math.floor(readerLikeList.scrollHeight / height);
            var index = 0;
            document.getElementById('change_Btn').addEventListener('click', function() {
                index++;
                if(index == pages) {
                    index = 0;
                }
                readerLikeList.scrollTop = index * height;
            });
        }(document.querySelector('#readerLikeList')));
        //立即阅读
        document.getElementById("nowRead").href = "/book/chapter/"+bookId+"/0?type=0";
        //查询本书是否已收藏
        core.list({"url": "/book/findExistBookshelf","data": {"bookId": bookId}}).then(function (data, a, b) {
            if(data.success){
                document.querySelector("#joined").style.display = "block";
                document.querySelector("#join").style.display = 'none';
            }else{
                document.querySelector("#joined").style.display = "none";
                document.querySelector("#join").style.display = 'block';
            }
        });
        //加入书架--------------------------
        $("#join").on("click",function(){
            core.post({
                "url": "/book/member/addBookshelf",
                "data": {"bookId": bookId},
            }).then(function (data) {
                if(data.success){
                    document.querySelector("#joined").style.display = "block";
                    document.querySelector("#join").style.display = 'none';
                }else{
                    document.querySelector("#joined").style.display = "none";
                    document.querySelector("#join").style.display = 'block';
                }
                alert(data.msg);
            })
        })
    }
    function chapterList() {
        //---------查询小说目录的分页情况--------------
        var num = $("#list_num0").val();
        // var bookId = $("#list_BookId").val();
        var page = $("#list_nowPage").val();
        if(!num>0){
            num=1;
        }
        var template = "";
        var select1 = $("#list_select1");
        var select2 = $("#list_select2");
        for(var i=1;i<=num;i++){
            var value = ((i-1)*50+1)+"-"+i*50;
            template = template + '<option value="'+i+'">'+value+'</option>';
        }
        select1.append(template);
        select2.append(template);
        //----------------end----------------------
        //---------------根据分页情况查询对应的方法查询对应的列表
        selectPage(page);

        //对list页面按钮进行渲染
        $(".but_prev").on("click", function () {
            changePage(-1);
        });
        $(".but_next").on("click", function () {
            changePage(1);
        });
        var select = $(".list_select");
        // select.on("onchange",function (){
        //     var page = select.options[this.options.selectedIndex].value;
        //     selectPage(page);
        // });
        select.change(function (e) {
            var page = e.target.options[this.options.selectedIndex].value;
            selectPage(page);
        });
    }

    /**
     * 对页数进行修改
     * @param size
     */
    function changePage(size){
        var nowPage = $("#list_nowPage").val();
        var endPage = $("#list_num0").val();
        var page = Number(nowPage)+Number(size);
        if(!page>0){
            alert("当前已经是第一页了");
        }else if(page>endPage){
            alert('这已经是最后一页了');
        }else{
            selectPage(page);
        }
    }
    /**
     * 根据页数加载对应的文章列表
     * @param page
     */
    function selectPage(page){
        var bookId = $("#list_BookId").val();
        core.list({url: "/book/findChapters/"+bookId+"/"+page}).then(function (data, a, b) {
            var listCont_Tmpl = $("#listCont_id_Tmpl").render(data.data);
            var listCont = $("#listCont_id");
            listCont.empty();
            listCont.append(listCont_Tmpl);
            $("#list_nowPage").val(page);
        });

    }

    /**
     * 初始化注册页
     */
    function register() {

        var rules = {
            rules: {

                validCode: {
                    required: true,
                    minlength: 4,
                    maxlength: 4,
                },

                mobileCode: {
                    required: true,
                    minlength: 6,
                    maxlength: 6,
                },
                userName: {
                    required: true,
                    minlength: 11,
                    mobile: true

                },
                password: {
                    required: true,
                    password: true,
                    minlength: 6,
                    maxlength: 20,

                },
                confirm_password: {
                    required: true,
                    minlength: 6,
                    maxlength: 20,
                    equalTo: "#password"
                }
            },
            messages: {}
        };
//登录表单
        mVad({
            forms: $('.form'),
            tip: '.tip',
            rules: rules,
            postData: function (data) {

                var _data = [];
                $.each(data, function (i, field) {
                    if (field['name'] != 'confirm_password') {
                        field['name'] != 'password' ? _data.push(field) : _data.push({
                                'name': 'password',
                                'value': mMd5.hbmd5(field['value'])
                            });
                    }
                });
                return _data;
            },
            formSubmit: function (_option, form) {
                core.save(_option.ajaxData).then(function (data) {
                    if (data.success) {
                        window.location.href = "/login";
                    } else {
                        $(_option.tip).removeClass("p-hide").html(data.msg);
                    }
                    _option.callback(data, form);
                }).fail(function (jqXHR, textStatus) {
                    $(_option.tip).removeClass("p-hide").html(data.msg);
                    _option.callback({}, form);
                });
            }

        });
    }

    /**
     * 初始化登录页
     */
    function login() {

        var rules = {
            rules: {

                validCode: {
                    required: true,
                    minlength: 4,
                    maxlength: 4,

                },
                userName: {
                    required: true,
                    maxlength: 16,

                },
                password: {
                    required: true,
                    maxlength: 100,

                }
            },
            messages: {}
        };


//登录表单
        mVad({
            forms: $('.form'),
            tip: '.tip',
            rules: rules,
            postData: function (data) {

                var _data = [];
                $.each(data, function (i, field) {
                    field['name'] != 'password' ? _data.push(field) : _data.push({
                            'name': 'password',
                            'value': mMd5.hbmd5(field['value'])
                        });
                });
                var rememberMe = $('#rememberMe').is(':checked');
                _data.push({"name": "rememberMe", "value": rememberMe})
                return _data;


            },
            formSubmit: function (_option, form) {
                core.save(_option.ajaxData).then(function (data) {
                    if (data.success) {
                        window.location.href = data.successURL;
                    } else {
                        $(_option.tip).removeClass("p-hide").html(data.msg);
                    }
                    _option.callback(data, form);
                }).fail(function (jqXHR, textStatus) {
                    $(_option.tip).removeClass("p-hide").html(data.msg);
                    _option.callback({}, form);
                });
            }

        });
    }

    /**
     * book 详情
     */
    function chapter() {
        //渲染小说内容
        var content = $("#chapter_content").val();
        var contentDiv = $("#chapter_content_div");
        // contentDiv.append(content);
        // contentDiv.innerHTML = content;
        contentDiv.html(content);
        var id = $("#chapter_id").val();
        var sort = $("#chapter_sort").val();
        var bookId = $("#chapter_bookId").val();
        //文章的上一页和下一页
        var template = '<dt></dt>'+
            '<dd><label><a href="/book/chapter/'+bookId+'/'+id+'?type=-1">上一章</a></label>'+
            '<label><a href="/book/list/'+bookId+'">目录</a></label>'+
            '<label><a href="/book/chapter/'+bookId+'/'+id+'?type=1">下一章</a></dd>';
        var ChapterBar = $(".ChapterBar");
        ChapterBar.html(template);
    }
    function bookShelf() {
        core.list({url: "/book/member/findBookshelfs","data":{"page":1}}).then(function (data, a, b) {
            if(data.success){
                var BookCoverList_Tmpl = $("#bookShelf_BookCoverList_Tmpl").render(data.data);
                var bookShelfs = $("#bookShelf_BookCoverList");
                bookShelfs.append(BookCoverList_Tmpl);
            }
        });
    }

    /**
     * 用戶中心
     */
    function userInfo(){

        (function(nodes) {
            var a,b;
            for(var i = 0,j; j = nodes[i]; i++) {
                if(location.href==j.href||location.href+'/'==j.href){
                    a=j;
                }else if(new RegExp(j.href, 'i').test(location.href)) {
                    b=j
                }
            }
            var k = a?a:b;
            if(k)
                k.className += ' now';
        }(document.querySelectorAll('section.nav_bottom a')))

         /*余额及代金券信息等数据*/
        var member_wrap=document.getElementById('member');
        (function(){
            var userId=57391304;
            var url='http://api.aishu8.com/v2/user/'+userId+'?app_key=2406394919&month_read_info=1&_access_version=2';
            Q.jsonp(url,{},function(data){
                if(data.status.code='0'){
                    if(data.data.month_read.status==2){
                        document.querySelector('.memberIcon').style.display='inline-block';
                    }else{
                        document.querySelector('.memberIcon').style.display='none';
                    }

                    document.getElementById('ye').innerHTML='余额：'+data.data.account.balance+'K币';
                    document.getElementById('djq').innerHTML=data.data.account.couponbalance;
                    var d = new Date(data.data.month_read.end_time);
                    var time=d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
                    if(data.data.month_read.old_member){
                        member_wrap.innerHTML='<a>'+
                            '<span class="member">全站会员</span>'+
                            '<span>'+time+'</span>'+
                            '</a>'
                    }else{
                        if(data.data.month_read.expire!=0){
                            member_wrap.innerHTML='<a href="http://pay.aishu8.com/v2/h5/pay/baoyue.do">'+
                                '<span class="member">开通包月</span>'+
                                '<span></span>'+
                                '</a>'
                        }else{
                            member_wrap.innerHTML='<a href="http://pay.aishu8.com/v2/h5/pay/baoyue.do">'+
                                '<span class="member">开通包月</span>'+
                                '<span>'+time+'</span>'+
                                '</a>'
                        }
                    }
                }
            })
        })()
}
    exports.index = index;
    exports.online = online;
    exports.register = register;
    exports.login = login;
    exports.category = category;
    exports.chapter = chapter;
    exports.userInfo=userInfo;
    exports.free=free;
    exports.info=info;
    exports.all=all;
    exports.chapterList=chapterList;
    exports.selectPage=selectPage;
    exports.bookShelf=bookShelf;
    exports.rank=rank;
    exports.types=types;
});