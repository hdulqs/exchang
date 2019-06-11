$.browser = {};
$.browser.msie = false;
$.browser.version = 0;
if (navigator.userAgent.match(/MSIE ([0-9]+)./)) {
    $.browser.msie = true;
    $.browser.version = RegExp.$1
}
$title = document.title;

function SlideBox(a, c) {
    $(a).mouseover(function () {
        $(c).slideDown(100)
    });
    $(a).mouseleave(function () {
        $(c).fadeOut(10)
    })
}

function getScrollWidth() {
    var c, d, a = document.createElement("DIV");
    a.style.cssText = "position:absolute; top:-1000px; width:100px; height:100px; overflow:hidden;";
    c = document.body.appendChild(a).clientWidth;
    a.style.overflowY = "scroll";
    d = a.clientWidth;
    document.body.removeChild(a);
    return c - d
}

function set_menu_hover(a) {
    $(".d_menu1 li").removeClass("on");
    $(".d_menu1 li.m" + a + "").addClass("on")
}

function Set2DivHeight() {
}

function miniBrand() {
    var a = true;
    if (top.location != self.location) {
        a = false
    }
    var c = $(window).width();
    if (c < 1200 && a) {
        if ($("#mini_css").length == 0) {
            $("head").append("<link rel=stylesheet id='mini_css' type=text/css href='" + zb.staticDomain + "/statics/css/cnbtc2014_mini.css?" + numberID() + "' />")
        }
    } else {
        if ($("#mini_css").length >= 1) {
            $("#mini_css").remove()
        }
    }
}

$(document).ready(function () {
    $(function () {
        $(document).scrollTop(1);
        if ($(document).scrollTop() == 0) {
            $(".b_menu1 .wrap").css("width", getScrollWidth() + 1002 + "px")
        }
        $(document).scrollTop(0);
        window.onload = window.onresize = function () {
            Set2DivHeight();
            miniBrand()
        };
        var c = $(".b_menu2");
        if (c.length > 0) {
            var a = $(c).offset().top;
            $.event.add(window, "scroll", function () {
                var d = $(window).scrollTop();
                $(c).css("position", ((d) > a) ? "fixed" : "static");
                $(c).css("top", ((d) > a) ? "0px" : "");
                $(c).css("left", ((d) > a) ? "0px" : "");
                $(c).css("z-index", ((d) > a) ? "9999" : "")
            })
        }
    });
    jQuery("#btcMarket").slide({
        titCell: "none",
        mainCell: ".hd",
        autoPage: true,
        effect: "topLoop",
        easing: "easeOutElastic",
        mouseOverStop: false,
        autoPlay: true,
        interTime: 5000,
        delayTime: 1000,
        startFun: function () {
            $("#btcMarket").addClass("on")
        },
        endFun: function () {
            $("#btcMarket").removeClass("on")
        }
    });
    jQuery("#ltcMarket").slide({
        titCell: "none",
        mainCell: ".hd",
        autoPage: true,
        effect: "topLoop",
        easing: "easeOutElastic",
        mouseOverStop: false,
        autoPlay: true,
        interTime: 5000,
        delayTime: 1000,
        startFun: function () {
            $("#ltcMarket").addClass("on")
        },
        endFun: function () {
            $("#ltcMarket").removeClass("on")
        }
    });
    jQuery("#btqMarket").slide({
        titCell: "none",
        mainCell: ".hd",
        autoPage: true,
        effect: "topLoop",
        easing: "easeOutElastic",
        mouseOverStop: false,
        autoPlay: true,
        interTime: 5000,
        delayTime: 1000,
        startFun: function () {
            $("#btqMarket").addClass("on")
        },
        endFun: function () {
            $("#btqMarket").removeClass("on")
        }
    });
    $(".close").click(function () {
        $(this).closest(".ctips").slideUp(100)
    });
    jQuery(".h_10xingbao").slide({mainCell: ".bd", titCell: ".hd ul li", trigger: "click"});
    jQuery(".b_banner").slide({mainCell: ".bd ul", effect: "fold", interTime: 5000, autoPlay: true});
    jQuery(".b_video").slide({mainCell: ".bd ul", effect: "left", autoPlay: true});
    jQuery(".d_hanqing").slide({
        titCell: ".hd ul",
        mainCell: "ul",
        autoPage: true,
        effect: "topLoop",
        interTime: 3000,
        autoPlay: true
    });
    $(window).resize(function () {
        if ($(window).width() < 1007) {
            $(".b_banner .bd ul,.b_banner .bd ul li").width(1007)
        } else {
            $(".b_banner .bd ul,.b_banner .bd ul li").width($(window).width())
        }
    })
});

function voids(a) {
}

String.prototype.replaceAll = function (c, a) {
    var d = new RegExp(c.replace(/([\(\)\[\]\{\}\^\$\+\-\*\?\.\"\'\|\/\\])/g, "\\$1"), "ig");
    return this.replace(d, a)
};
Object.extend = function (a, d) {
    for (var c in d) {
        a[c] = d[c]
    }
    return a
};
Object.extend(Object, {
    inspect: function (a) {
        try {
            if (a == undefined) {
                return "undefined"
            }
            if (a == null) {
                return "null"
            }
            return a.inspect ? a.inspect() : a.toString()
        } catch (c) {
            if (c instanceof RangeError) {
                return "..."
            }
            throw c
        }
    }, keys: function (a) {
        var c = [];
        for (var d in a) {
            c.push(d)
        }
        return c
    }, values: function (c) {
        var a = [];
        for (var d in c) {
            a.push(c[d])
        }
        return a
    }, clone: function (a) {
        return Object.extend({}, a)
    }
});

function getX(a) {
    return a.offsetLeft + (a.offsetParent ? getX(a.offsetParent) : a.x ? a.x : 0)
}

function getY(a) {
    return (a.offsetParent ? a.offsetTop + getY(a.offsetParent) : a.y ? a.y : 0)
}

var Style = {
    getFinalStyle: function (c, a) {
        if (window.getComputedStyle) {
            return window.getComputedStyle(c, null)[a]
        } else {
            if (c.currentStyle) {
                return c.currentStyle[a]
            } else {
                return c.style[a]
            }
        }
    }, height: function (c) {
        if (this.getFinalStyle(c, "display") !== "none") {
            return c.offsetHeight || c.clientHeight
        } else {
            c.style.display = "block";
            var a = c.offsetHeight || c.clientHeight;
            c.style.display = "none";
            return a
        }
    }, width: function (c) {
        if (this.getFinalStyle(c, "display") !== "none") {
            return c.offsetWidth || c.clientWidth
        } else {
            c.style.display = "block";
            var a = c.offsetWidth || c.clientWidth;
            c.style.display = "none";
            return a
        }
    }
};

function numberID() {
    return Math.round(Math.random() * 10000) * Math.round(Math.random() * 10000)
}

function T$(a) {
    return document.getElementById(a)
}

var sysCloseCount = 0;
NetBox = function () {
    var u, z, D, i, y, d, k, x, g, C = 0;
    var F, c, E, e, r, A;
    var j = false;
    var q = false;
    var n = "";
    var v;
    return {
        show: function (L, O, J, I, f, N) {
            v = N;
            g = f;
            if (!C) {
                u = document.createElement("div");
                u.id = "tinybox";
                z = document.createElement("div");
                z.id = "tinymask";
                D = document.createElement("div");
                D.id = "tinycontent";
                document.body.appendChild(z);
                document.body.appendChild(u);
                u.appendChild(D);
                C = 1
            }
            n = L;
            var P = (NetPage.height() / 2) - (J / 2);
            P = P < 10 ? 10 : P;
            var H = 0;
            if (J < 100) {
                H = (P + NetPage.top() - 60)
            } else {
                H = (P + NetPage.top())
            }
            var K = (NetPage.width() / 2) - (O / 2);
            if (!j) {
                y = L;
                d = O;
                k = J;
                if (f) {
                    r = F = getY(f);
                    A = c = getX(f);
                    E = Style.width(f) + 12;
                    e = Style.height(f) + 12
                }
                u.style.backgroundImage = "none";
                u.innerHTML = "";
                if (f) {
                    u.style.width = (E - 12) + "px";
                    u.style.height = (e - 12) + "px";
                    u.style.top = (F - 6) + "px";
                    u.style.left = (c - 6) + "px"
                } else {
                    u.style.width = O + "px";
                    if (J > 99) {
                        u.style.height = J + "px";
                        u.style.top = (H - 6) + "px";
                        u.style.left = (K - 6) + "px"
                    } else {
                        u.style.height = "auto";
                        u.style.top = (H + 37) + "px";
                        u.style.left = (K) + "px"
                    }
                }
                this.mask();
                if (f) {
                    u.style.display = "block";
                    $("#tinybox").animate({left: K, top: H, width: O, height: J}, 150, "", function () {
                        u.innerHTML = n;
                        u.style.height = "auto"
                    })
                } else {
                    u.innerHTML = n;
                    $(u).show()
                }
            } else {
                u.style.backgroundImage = "none";
                u.style.display = "block";
                if (J < 100) {
                    H = H - 50
                }
                u.style.height = $(u).height() + "px";
                u.innerHTML = n;
                var G = O;
                var M = 0;
                if (J == 99) {
                    if ($(u).find("iframe:first").length > 0) {
                        M = $(u).find("iframe:first").height()
                    } else {
                        M = $(u).find("div:first").height()
                    }
                    this.resize(G, M, null, 99)
                } else {
                    M = J;
                    this.resize(G, M)
                }
                this.mask()
            }
            j = true;
            if (I) {
                sysCloseCount = 0;
                setTimeout(function () {
                    if (sysCloseCount == 0) {
                        $("#tinybox").animate({top: -300}, 300);
                        NetBox.hide()
                    }
                }, 1000 * I)
            }
        }, hide: function (f) {
            sysCloseCount = -1;
            j = false;
            $(z).fadeOut(50);
            if (g) {
                u.style.height = $(u).height() + "px";
                u.innerHTML = "";
                $("#tinybox").animate({
                    left: (c - 6),
                    top: (F) - 6,
                    width: (E - 12),
                    height: (e - 12)
                }, 200, "", function () {
                    u.style.display = "none"
                })
            } else {
                $(u).fadeOut(50)
            }
        }, resize: function (f, K, L, H) {
            var I = (NetPage.height() / 2) - (K / 2);
            I = I < 10 ? 10 : I;
            var J = (I + NetPage.top());
            var G = (NetPage.width() / 2) - (f / 2);
            if (d == f) {
                f = 0
            }
            if (k == K) {
                K = 0
            }
            if (f > 0 && K > 0) {
                $("#tinybox").animate({left: G, top: J, width: f, height: K}, 150, "", function () {
                    if (H == 99) {
                        u.style.height = "auto"
                    }
                    $("#fancybox-frame").css("height", "100%");
                    if (L != null) {
                        L
                    }
                })
            } else {
                if (f > 0) {
                    $("#tinybox").animate({left: G, width: f}, 150, "", function () {
                        if (H == 99) {
                            u.style.height = "auto"
                        }
                        $("#fancybox-frame").css("height", "100%");
                        if (L != null) {
                            L
                        }
                    })
                } else {
                    if (K > 0) {
                        $("#tinybox").animate({top: J, height: K}, 150, "", function () {
                            if (H == 99) {
                                u.style.height = "auto"
                            }
                            $("#fancybox-frame").css("height", K - 44);
                            if (L != null) {
                                L
                            }
                        })
                    } else {
                        if (H == 99) {
                            u.style.height = "auto"
                        }
                        if (L != null) {
                            L
                        }
                    }
                }
            }
        }, mask: function () {
            z.style.display = "block";
            z.style.height = NetPage.theight() + "px";
            z.style.width = NetPage.twidth() + "px";
            z.style.opacity = 0.4;
            z.style.filter = "alpha(opacity=40)";
            if (v) {
                z.style.cursor = "pointer";
                z.title = "鍙屽嚮鍏抽棴";
                z.ondblclick = function () {
                    NetBox.hide()
                }
            }
        }, pos: function () {
        }, size: function (J, L, I, O) {
            J = typeof J == "object" ? J : T$(J);
            clearInterval(J.si);
            var f = J.offsetWidth, M = J.offsetHeight, K = f - parseInt(J.style.width),
                N = M - parseInt(J.style.height);
            var G = f - K > L ? -1 : 1, H = (M - N > I) ? -1 : 1;
            J.si = setInterval(function () {
                NetBox.twsize(J, L, K, G, I, N, H, O)
            }, 10)
        }, twsize: function (J, L, K, G, I, N, H, O) {
            var f = J.offsetWidth - K, M = J.offsetHeight - N;
            if (f == L && M == I) {
                clearInterval(J.si);
                u.style.backgroundImage = "none";
                D.style.display = "block";
                u.innerHTML = n
            } else {
                if (f != L) {
                    J.style.width = f + (Math.ceil(Math.abs(L - f) / O) * G) + "px"
                }
                if (M != I) {
                    J.style.height = M + (Math.ceil(Math.abs(I - M) / O) * H) + "px"
                }
                this.pos();
                if (a == f && B == M) {
                    clearInterval(J.si)
                }
                a = f;
                B = M
            }
        }
    };
    var a = 0;
    var B = 0
}();
NetPage = function () {
    return {
        top: function () {
            return document.body.scrollTop || document.documentElement.scrollTop
        }, width: function () {
            return self.innerWidth || document.documentElement.clientWidth
        }, height: function () {
            return self.innerHeight || document.documentElement.clientHeight
        }, theight: function () {
            var f = document, a = f.body, c = f.documentElement;
            return Math.max(Math.max(a.scrollHeight, c.scrollHeight), Math.max(a.clientHeight, c.clientHeight))
        }, twidth: function () {
            var f = document, a = f.body, c = f.documentElement;
            return Math.max(Math.max(a.scrollWidth, c.scrollWidth), Math.max(a.clientWidth, c.clientWidth))
        }
    }
}();
$.fn.extend({
    jscroll: function (a) {
        return this.each(function () {
            a = a || {};
            a.Bar = a.Bar || {};
            a.Btn = a.Btn || {};
            a.Bar.Bg = a.Bar.Bg || {};
            a.Bar.Bd = a.Bar.Bd || {};
            a.Btn.uBg = a.Btn.uBg || {};
            a.Btn.dBg = a.Btn.dBg || {};
            var e = {
                W: "15px",
                BgUrl: "",
                Bg: "#efefef",
                Bar: {
                    Pos: "up",
                    Bd: {Out: "#b5b5b5", Hover: "#ccc"},
                    Bg: {Out: "#fff", Hover: "#fff", Focus: "orange"}
                },
                Btn: {
                    btn: true,
                    uBg: {Out: "#ccc", Hover: "#fff", Focus: "orange"},
                    dBg: {Out: "#ccc", Hover: "#fff", Focus: "orange"}
                },
                Fn: function () {
                }
            };
            a.W = a.W || e.W;
            a.BgUrl = a.BgUrl || e.BgUrl;
            a.Bg = a.Bg || e.Bg;
            a.Bar.Pos = a.Bar.Pos || e.Bar.Pos;
            a.Bar.Bd.Out = a.Bar.Bd.Out || e.Bar.Bd.Out;
            a.Bar.Bd.Hover = a.Bar.Bd.Hover || e.Bar.Bd.Hover;
            a.Bar.Bg.Out = a.Bar.Bg.Out || e.Bar.Bg.Out;
            a.Bar.Bg.Hover = a.Bar.Bg.Hover || e.Bar.Bg.Hover;
            a.Bar.Bg.Focus = a.Bar.Bg.Focus || e.Bar.Bg.Focus;
            a.Btn.btn = a.Btn.btn != undefined ? a.Btn.btn : e.Btn.btn;
            a.Btn.uBg.Out = a.Btn.uBg.Out || e.Btn.uBg.Out;
            a.Btn.uBg.Hover = a.Btn.uBg.Hover || e.Btn.uBg.Hover;
            a.Btn.uBg.Focus = a.Btn.uBg.Focus || e.Btn.uBg.Focus;
            a.Btn.dBg.Out = a.Btn.dBg.Out || e.Btn.dBg.Out;
            a.Btn.dBg.Hover = a.Btn.dBg.Hover || e.Btn.dBg.Hover;
            a.Btn.dBg.Focus = a.Btn.dBg.Focus || e.Btn.dBg.Focus;
            a.Fn = a.Fn || e.Fn;
            var f = this;
            var u, D = 0, F = 0;
            $(f).css({overflow: "hidden", position: "relative", padding: "0px"});
            var r = $(f).width(), B = $(f).height() - 1;
            var x = a.W ? parseInt(a.W) : 21;
            var C = r - x;
            var A = a.Btn.btn == true ? x : 0;
            if ($(f).children(".jscroll-c").height() == null) {
                $(f).wrapInner("<div class='jscroll-c' style='top:0px;z-index:8000;zoom:1;position:relative'></div>");
                $(f).children(".jscroll-c").prepend("<div style='height:0px;overflow:hidden'></div>");
                $(f).append("<div class='jscroll-e' unselectable='on' style=' height:100%;top:0px;right:1px;-moz-user-select:none;position:absolute;overflow:hidden;z-index:8002;'><div class='jscroll-u' style='position:absolute;top:0px;width:100%;left:0;background:blue;overflow:hidden'></div><div class='jscroll-h'  unselectable='on' style='background:green;position:absolute;left:0;-moz-user-select:none;border:1px solid'></div><div class='jscroll-d' style='position:absolute;bottom:0px;width:100%;left:0;background:blue;overflow:hidden'></div></div>")
            }
            var n = $(f).children(".jscroll-c");
            var j = $(f).children(".jscroll-e");
            var i = j.children(".jscroll-h");
            var c = j.children(".jscroll-u");
            var k = j.children(".jscroll-d");
            if ($.browser.msie) {
                document.execCommand("BackgroundImageCache", false, true)
            }
            n.css({"padding-right": x});
            j.css({width: x, background: a.Bg, "background-image": a.BgUrl});
            i.css({
                top: A,
                background: a.Bar.Bg.Out,
                "background-image": a.BgUrl,
                "border-color": a.Bar.Bd.Out,
                width: x - 2
            });
            c.css({height: A, background: a.Btn.uBg.Out, "background-image": a.BgUrl});
            k.css({height: A, background: a.Btn.dBg.Out, "background-image": a.BgUrl});
            i.hover(function () {
                if (F == 0) {
                    $(this).css({
                        background: a.Bar.Bg.Hover,
                        "background-image": a.BgUrl,
                        "border-color": a.Bar.Bd.Hover
                    })
                }
            }, function () {
                if (F == 0) {
                    $(this).css({background: a.Bar.Bg.Out, "background-image": a.BgUrl, "border-color": a.Bar.Bd.Out})
                }
            });
            c.hover(function () {
                if (F == 0) {
                    $(this).css({background: a.Btn.uBg.Hover, "background-image": a.BgUrl})
                }
            }, function () {
                if (F == 0) {
                    $(this).css({background: a.Btn.uBg.Out, "background-image": a.BgUrl})
                }
            });
            k.hover(function () {
                if (F == 0) {
                    $(this).css({background: a.Btn.dBg.Hover, "background-image": a.BgUrl})
                }
            }, function () {
                if (F == 0) {
                    $(this).css({background: a.Btn.dBg.Out, "background-image": a.BgUrl})
                }
            });
            var d = n.height();
            var E = (B - 2 * A) * B / d;
            if (E < 10) {
                E = 10
            }
            var g = E / 6;
            var q = 0, z = false;
            i.height(E);
            if (d <= B) {
                n.css({padding: 0});
                j.css({display: "none"})
            } else {
                z = true
            }
            if (a.Bar.Pos != "up") {
                q = B - E - A;
                y()
            }
            i.bind("mousedown", function (I) {
                a.Fn && a.Fn.call(f);
                F = 1;
                i.css({background: a.Bar.Bg.Focus, "background-image": a.BgUrl});
                var H = I.pageY, G = parseInt($(this).css("top"));
                $(document).mousemove(function (J) {
                    q = G + J.pageY - H;
                    y()
                });
                $(document).mouseup(function () {
                    F = 0;
                    i.css({background: a.Bar.Bg.Out, "background-image": a.BgUrl, "border-color": a.Bar.Bd.Out});
                    $(document).unbind()
                });
                return false
            });
            c.bind("mousedown", function (G) {
                a.Fn && a.Fn.call(f);
                F = 1;
                c.css({background: a.Btn.uBg.Focus, "background-image": a.BgUrl});
                f.timeSetT("u");
                $(document).mouseup(function () {
                    F = 0;
                    c.css({background: a.Btn.uBg.Out, "background-image": a.BgUrl});
                    $(document).unbind();
                    clearTimeout(u);
                    D = 0
                });
                return false
            });
            k.bind("mousedown", function (G) {
                a.Fn && a.Fn.call(f);
                F = 1;
                k.css({background: a.Btn.dBg.Focus, "background-image": a.BgUrl});
                f.timeSetT("d");
                $(document).mouseup(function () {
                    F = 0;
                    k.css({background: a.Btn.dBg.Out, "background-image": a.BgUrl});
                    $(document).unbind();
                    clearTimeout(u);
                    D = 0
                });
                return false
            });
            f.timeSetT = function (I) {
                var G = this;
                if (I == "u") {
                    q -= g
                } else {
                    q += g
                }
                y();
                D += 2;
                var H = 500 - D * 50;
                if (H <= 0) {
                    H = 0
                }
                u = setTimeout(function () {
                    G.timeSetT(I)
                }, H)
            };
            j.bind("mousedown", function (G) {
                a.Fn && a.Fn.call(f);
                q = q + G.pageY - i.offset().top - E / 2;
                v();
                return false
            });

            function v() {
                if (q < A) {
                    q = A
                }
                if (q > B - E - A) {
                    q = B - E - A
                }
                i.stop().animate({top: q}, 100);
                var G = -((q - A) * d / (B - 2 * A));
                n.stop().animate({top: G}, 1000)
            }

            function y() {
                if (q < A) {
                    q = A
                }
                if (q > B - E - A) {
                    q = B - E - A
                }
                i.css({top: q});
                var G = -((q - A) * d / (B - 2 * A));
                n.css({top: G})
            }

            $(f).mousewheel(function () {
                if (z != true) {
                    return
                }
                a.Fn && a.Fn.call(f);
                if (this.D > 0) {
                    q -= g
                } else {
                    q += g
                }
                y()
            })
        })
    }
});
$.fn.extend({
    mousewheel: function (a) {
        return this.each(function () {
            var c = this;
            c.D = 0;
            if ($.browser.msie || $.browser.safari) {
                c.onmousewheel = function () {
                    c.D = event.wheelDelta;
                    event.returnValue = false;
                    a && a.call(c)
                }
            } else {
                c.addEventListener("DOMMouseScroll", function (d) {
                    c.D = d.detail > 0 ? -1 : 1;
                    d.preventDefault();
                    a && a.call(c)
                }, false)
            }
        })
    }
});

function Close() {
    NetBox.hide()
}

var jqTransformGetLabel = function (d) {
    var a = $(d.get(0).form);
    var e = d.next();
    if (!e.is("label")) {
        e = d.prev();
        if (e.is("label")) {
            var c = d.attr("id");
            if (c) {
                e = a.find('label[for="' + c + '"]')
            }
        }
    }
    if (e.is("label")) {
        return e.css("cursor", "pointer")
    }
    return false
};

function Ask(a) {
    var d = {
        CloseTime: 0,
        Msg: "锛�",
        Title: "锛�",
        Height: 99,
        callback: "Close()",
        callback2: "Close()",
        fromObj: null
    };
    a = Object.extend(d, a);
    if (a.Title != "锛�") {
        a.Msg = a.Title
    }
    var c = '<div class="AlertMessage"><div class="MessageTitle"><div class="MessageTitle">' + zb.L("ZB鎻愮ず鎮�") + '</div><div class="MessageCloseC"><a class="MessageClose" onfocus="this.blur()" href="javascript:Close()" title="鍏抽棴">Close</a></div></div><div class="MessageHelp"><div class="Message">' + a.Msg + '</div></div><div class="MessageControl"><div class="MessageControl2"><a onfocus="this.blur()" id="nobackButton_1"  class="noback" onclick="' + a.callback2 + '" href="javascript:Void();"><h4>' + zb.L("鍙栨秷") + '</h4></a><a onfocus="this.blur()" id="okButton_1" class="ok" onclick="' + a.callback + '" href="javascript:Void();"><h4>' + zb.L("纭畾") + "</h4></a></div></div></div>";
    NetBox.show(c, 550, a.Height, a.CloseTime, a.fromObj);
    $("body").bind("keyup", function (e) {
        if (e.keyCode == "13") {
            $("#okButton_1").trigger("click")
        }
    })
}

function Ask2(a) {
    var d = {
        call: function (e) {
        }, data: "", CloseTime: 0, Msg: "锛�", Height: 99, Title: "锛�", callback: "", callback2: "Close()", fromObj: null
    };
    a = $.extend(d, a);
    if (a.Title != "锛�") {
        a.Msg = a.Title
    }
    var c = '<div class="AlertMessage"><div class="MessageTitle"><div class="MessageTitle">' + zb.L("ZB鎻愮ず鎮�") + '</div><div class="MessageCloseC"><a class="MessageClose" onfocus="this.blur()" href="javascript:Close()" title="鍏抽棴">Close</a></div></div><div class="MessageHelp"><div class="Message">' + a.Msg + '</div></div><div class="MessageControl"><div class="MessageControl2"><a onfocus="this.blur()" id="nobackButton_2"  class="noback" onclick="' + a.callback2 + '" href="javascript:Void();"><h4>' + zb.L("鍙栨秷") + '</h4></a><a onfocus="this.blur()" id="okButton_2" class="ok" onclick="' + a.callback + '" href="javascript:Void();"><h4>' + zb.L("纭畾") + "</h4></a></div></div></div>";
    NetBox.show(c, 550, a.Height, a.CloseTime, a.fromObj);
    $("#okButton_2").bind("click", function () {
        a.call(a.data);
        return false
    });
    $("body").bind("keyup", function (e) {
        if (e.keyCode == "13") {
            $("#okButton_2").trigger("click")
        }
    })
}

$.fn.Ask = function (a) {
    $(this).click(function () {
        Ask2(a);
        return false
    })
};
$.fn.LineOne = function () {
    $(this).bind("focus", function () {
        if (this.blur) {
            this.blur()
        }
    })
};

function Message(c, g, d, a) {
    var f = {
        CloseTime: 0,
        Msg: "",
        Style: "Alert",
        Tit: "Title",
        Height: 99,
        callback: "Close()",
        fromObj: null,
        call: null
    };
    a = Object.extend(f, a);
    if (g.length > 0) {
        a.Msg = g
    }
    if (c != "Title") {
        a.Tit = c
    }
    if (d) {
        a.Style = d
    }
    var e = '<div class="AlertMessage">';
    if (a.CloseTime == 0) {
        e += '<div class="MessageTitle"><div class="MessageTitle">' + a.Tit + '</div><div class="MessageCloseC"><a class="MessageClose" onfocus="this.blur()" href="javascript:Close()" title="鍏抽棴">Close</a></div></div>'
    }
    e += '<div class="Message' + a.Style + '"><div class="Message">' + a.Msg + "</div></div>";
    if (a.CloseTime == 0) {
        e += '<div class="MessageControl"><div class="MessageControl2"><a onfocus="this.blur()" id="okButton_2013"  class="ok" onclick="' + a.callback + '" href="javascript:Void();"><h4>' + zb.L("纭畾") + "</h4></a></div></div>"
    }
    e += "</div>";
    NetBox.show(e, 550, a.Height, a.CloseTime, a.fromObj);
    if (typeof a.call == "function") {
        $("#okButton_2013").attr("href", "javascript:;");
        $("#okButton_2013").click(function () {
            a.call();
            return false
        })
    }
    $("body").bind("keyup", function (i) {
        if (i.keyCode == "13") {
            $("#okButton_2013").trigger("click")
        }
    })
}

function Alert(c, a) {
    Message(zb.L("ZB鎻愮ず鎮�"), c + "", "Alert", a)
}

function Info(c, a) {
    Message(zb.L("ZB鎻愮ず鎮�"), c + "", "Info", a)
}

function msg(c, a) {
    Message(zb.L("ZB鎻愮ず鎮�"), c + "", "Info", a)
}

function Msg(c, a) {
    Message(zb.L("ZB鎻愮ず鎮�"), c + "", "Info", a)
}

function Wrong(c, a) {
    Message(zb.L("ZB鎻愮ず鎮�"), c + "", "Wrong", a)
}

function Right(c, a) {
    Message(zb.L("ZB鎻愮ず鎮�"), c + "", "Right", a)
}

function Help(c, a) {
    Message(zb.L("ZB鎻愮ず鎮�"), c + "", "Help", a)
}

function Iframe(c) {
    var f = {
        Url: "",
        zoomSpeedIn: 100,
        zoomSpeedOut: 100,
        Width: 540,
        Height: 190,
        Title: "",
        overlayShow: false,
        modal: true,
        isShowIframeTitle: true,
        isShowClose: true,
        isIframeAutoHeight: false,
        scrolling: "auto",
        overlayOpacity: 0.5,
        overlayColor: "#000000",
        padding: 0,
        IsShow: false,
        IsCloseOnModal: false,
        fromObj: null
    };
    c = Object.extend(f, c);
    var e = (c.height - 44) + "px";
    var d = "";
    if (c.isShowIframeTitle) {
        var a = "";
        if (!c.isShowClose) {
            a = " noClose"
        }
        if (c.isIframeAutoHeight) {
            d = '<div class="popIframeTitle' + a + '"><div class="popIframeTitle">' + c.Title + '</div><div class="popIframeCloseC"><a class="popIframeClose" onfocus="this.blur()" href="javascript:Close()" title="鍏抽棴">Close</a></div></div><div class="popIframeContent"><iframe id="fancybox-frame" name="fancybox-frame' + new Date().getTime() + '" frameborder="0"   hspace="0" ' + ($.browser.msie ? 'allowtransparency="true""' : "") + ' scrolling="' + c.scrolling + '" onload="$(this).height($(this).contents().height());NetBox.resize(0,($(this).contents().height()+44));" src="' + c.Url + '"></iframe></div>'
        } else {
            d = '<div class="popIframeTitle' + a + '"><div class="popIframeTitle">' + c.Title + '</div><div class="popIframeCloseC"><a class="popIframeClose" onfocus="this.blur()" href="javascript:Close()" title="鍏抽棴">Close</a></div></div><div class="popIframeContent"><iframe id="fancybox-frame" name="fancybox-frame' + new Date().getTime() + '" frameborder="0" hspace="0"  scrolling="' + c.scrolling + '" style="height:' + (c.Height - 44) + 'px"  src="' + c.Url + '"></iframe></div>'
        }
    } else {
        if (c.isIframeAutoHeight) {
            d = '<div class="popIframeContent"><iframe id="fancybox-frame" name="fancybox-frame' + new Date().getTime() + '" frameborder="0" hspace="0" ' + ($.browser.msie ? 'allowtransparency="true""' : "") + '  onload="$(this).height($(this).contents().height());NetBox.resize(0,($(this).contents().height()));" scrolling="' + c.scrolling + '" src="' + c.Url + '"></iframe></div>'
        } else {
            d = '<div class="popIframeContent"><iframe id="fancybox-frame" name="fancybox-frame' + new Date().getTime() + '" frameborder="0" hspace="0" ' + ($.browser.msie ? 'allowtransparency="true""' : "") + ' scrolling="' + c.scrolling + '" src="' + c.Url + '"></iframe></div>'
        }
    }
    NetBox.show(d, c.Width, c.Height, 0, c.fromObj, c.IsCloseOnModal)
}

$.fn.Iframe = function (a) {
    $(this).click(function () {
        Iframe(a);
        return false
    })
};
$.extend({
    Iframe: function (a) {
        Iframe(a)
    }
});
jQuery.cookie = function (c, n, u) {
    if (typeof n != "undefined") {
        u = u || {};
        if (n === null) {
            n = "";
            u.expires = -1
        }
        var f = "";
        if (u.expires && (typeof u.expires == "number" || u.expires.toUTCString)) {
            var g;
            if (typeof u.expires == "number") {
                g = new Date();
                g.setTime(g.getTime() + (u.expires * 24 * 60 * 60 * 1000))
            } else {
                g = u.expires
            }
            f = "; expires=" + g.toUTCString()
        }
        var r = u.path ? "; path=" + (u.path) : "";
        var j = u.domain ? "; domain=" + (u.domain) : "";
        var a = u.secure ? "; secure" : "";
        document.cookie = [c, "=", encodeURIComponent(n), f, r, j, a].join("")
    } else {
        var e = null;
        if (document.cookie && document.cookie != "") {
            var q = document.cookie.split(";");
            for (var k = 0; k < q.length; k++) {
                var d = jQuery.trim(q[k]);
                if (d.substring(0, c.length + 1) == (c + "=")) {
                    e = decodeURIComponent(d.substring(c.length + 1));
                    break
                }
            }
        }
        return e
    }
};
$.fn.DropSelecter = function (k) {
    var d = {
        StyleHover: "hover",
        StyleSelect: "",
        ControlSeleter: "",
        Left: 0,
        Top: 0,
        IsShow: false,
        Auto: true,
        call: function () {
        }
    };
    k = $.extend(d, k);
    if (k.StyleSelect == "") {
        k.StyleSelect = k.StyleHover
    }
    var g = $(this);
    var j = $(k.ControlSeleter);
    var c;
    var i = 100;
    var e = g.position();
    if (!e) {
        return
    }
    j.css({left: e.left + k.Left, top: e.top + g.height() + k.Top});
    var f = true;
    var a = "";
    if (!$(this).data("DropSelecterId")) {
        a = numberID();
        g.data("DropSelecterId", a);
        if (!k.Auto) {
            $("body").click(function () {
                if (g.data("opened")) {
                    j.hide();
                    g.removeClass(k.StyleSelect);
                    g.removeData("opened")
                }
            });
            g.click(function (n) {
                if (g.data("opened")) {
                    j.hide();
                    g.removeClass(k.StyleSelect);
                    g.removeData("opened")
                } else {
                    $("body").trigger("click");
                    g.data("opened", true);
                    j.show();
                    g.addClass(k.StyleSelect)
                }
                n.stopPropagation()
            });
            j.click(function (n) {
                n.stopPropagation()
            })
        } else {
            j.mouseenter(function () {
                clearTimeout(c);
                c = setTimeout(function () {
                    j.show();
                    g.addClass(k.StyleHover)
                }, i)
            }).mouseleave(function () {
                clearTimeout(c);
                c = setTimeout(function () {
                    j.hide();
                    g.removeClass(k.StyleHover)
                }, i)
            })
        }
        g.focus(function () {
            $(this).blur()
        }).mouseover(function () {
            clearTimeout(c);
            c = setTimeout(function () {
                e = g.position();
                j.css({left: e.left + k.Left, top: e.top + g.height() + k.Top});
                if (k.Auto) {
                    j.show();
                    if (f) {
                        (function (n) {
                            return k.call(n)
                        })(1)
                    }
                }
                g.addClass(k.StyleHover);
                f = false
            }, i)
        }).mouseleave(function () {
            clearTimeout(c);
            c = setTimeout(function () {
                if (k.Auto) {
                    j.hide()
                }
                g.removeClass(k.StyleHover)
            }, i)
        });
        $(window).resize(function () {
            e = g.position();
            j.css({left: e.left + k.Left, top: e.top + g.height() + k.Top})
        })
    }
};
$.fn.UiTitle = function () {
    $(this).MyTitle({defaultCss: "tipsr", title: $(this).attr("mytitle"), html: true})
};
$.fn.UIButton = function () {
    if ($(this).attr("NewStyle")) {
        return
    }
    $(this).attr("NewStyle", "true");
    $(this).wrap("<a  href='javascript:void(0)'></a>");
    if (!$(this).attr("StyleName")) {
        $(this).attr("StyleName", "buttonCommon")
    }
    if ($(this).attr("disabled")) {
        $(this).parent().addClass($(this).attr("StyleName") + "Disabled")
    } else {
        $(this).parent().addClass($(this).attr("StyleName"))
    }
    $(this).bind("focus", function () {
        if (this.blur) {
            this.blur()
        }
    })
};
$.fn.UiText = function () {
    if (!$(this).attr("NewStyle")) {
        $(this).attr("NewStyle");
        var a = $(this);
        if (a.attr("mytitle")) {
            a.MyTitle({defaultCss: "tipsr", title: a.attr("mytitle"), trigger: "manual"})
        }
        if ($(this).attr("valueDemo")) {
            if ($(this).val() == "") {
                $(this).val($(this).attr("valueDemo"));
                $(this).css({color: "#C7C7C7"})
            }
        }
        $(this).bind("focus", function () {
            $(this).removeAttr("errorStyle");
            if ($(this).attr("valueDemo")) {
                if ($(this).attr("valueDemo") == $(this).val()) {
                    $(this).val("");
                    $(this).css({color: "#333333"})
                }
            }
            a.addClass("inputFocue");
            if (a.data("tipsy")) {
                a.MyTitle("show")
            }
        }).bind("blur", function () {
            if ($(this).attr("valueDemo")) {
                if ($(this).val() == "") {
                    $(this).val($(this).attr("valueDemo"));
                    $(this).css({color: "#C7C7C7"})
                }
            }
            a.removeClass("inputFocue");
            if (a.data("tipsy")) {
                a.MyTitle("hide")
            }
            if ($(this).val().length > 0) {
                ($(this))
            }
        });
        if (!$(this).attr("noHide") && a.data("tipsy")) {
            a.keydown(function () {
                a.MyTitle("hide")
            })
        }
    }
};
$.fn.UiCheckbox = function (a) {
    $(":checkbox+label", this).each(function () {
        $(this).addClass("checkbox");
        if ($(this).prev().is(":disabled") == false) {
            if ($(this).prev().is(":checked")) {
                $(this).addClass("checked")
            }
        } else {
            $(this).addClass("disabled")
        }
    }).click(function (c) {
        if (!$(this).prev().is(":checked")) {
            $(this).addClass("checked");
            $(this).prev()[0].checked = true
        } else {
            $(this).removeClass("checked");
            $(this).prev()[0].checked = false
        }
        c.stopPropagation()
    }).prev().hide()
};

function EnableRadio(c) {
    var d = $(c);
    var a = d.parent().find("a:first");
    a.removeClass("jqTransformRadioEnabled").css({cursor: "pointer"});
    d.removeAttr("disabled");
    d.change(function () {
        d[0].checked && a.addClass("jqTransformRadioChecked") || a.removeClass("jqTransformRadioChecked");
        return true
    });
    a.click(function () {
        if (d.attr("disabled")) {
            return false
        }
        d.trigger("click").trigger("change");
        $('input[name="' + d.attr("name") + '"]').not(d).each(function () {
            $(this).attr("type") == "radio" && $(this).trigger("change")
        });
        return false
    })
}

function DisenableRadio(c) {
    var d = $(c);
    var a = d.parent().find("a:first");
    d.attr("disabled", "disabled");
    a.addClass("jqTransformRadioEnabled").css({cursor: "default"});
    a.unbind("click");
    d.unbind("change")
}

$.fn.UiRadio = function () {
    return this.each(function () {
        if (!$(this).attr("NewStyle")) {
            if ($(this).hasClass("jqTransformHidden")) {
                return
            }
            var d = $(this);
            var c = this;
            d.addClass("jqTransformHidden");
            var a;
            if ($(this).parent().hasClass("jqTransformRadioWrapper")) {
                a = d.parent().find("a:first")
            } else {
                a = $('<a  class="jqTransformRadio" style="cursor:pointer;"></a>');
                d.wrap('<span class="jqTransformRadioWrapper"></span>').parent().prepend(a)
            }
            if (d.attr("disabled")) {
                a.addClass("jqTransformRadioEnabled").css({cursor: "default"});
                return
            } else {
                d.change(function () {
                    c.checked && a.addClass("jqTransformRadioChecked") || a.removeClass("jqTransformRadioChecked");
                    return true
                });
                a.click(function () {
                    if (d.attr("disabled")) {
                        return false
                    }
                    d.trigger("click").trigger("change");
                    $('input[name="' + d.attr("name") + '"]', c.form).not(d).each(function () {
                        $(this).attr("type") == "radio" && $(this).trigger("change")
                    });
                    return false
                });
                c.checked && a.addClass("jqTransformRadioChecked")
            }
            d.hide()
        }
    })
};
$.fn.UiSelect = function (d) {
    var g = {
        StyleNormal: "SelectGray",
        StyleHover: "SelectBlue",
        StyleDropDown: "SelectDropDown",
        itemHeight: 24,
        InnerWidth: 0,
        InnerWidthOffset: -15,
        OuterWidth: 0,
        OuterWidthOffset: 12,
        MaxShow: 10,
        Top: 0,
        Left: 0,
        ControlSeleter: "",
        Auto: false,
        IsShow: true
    };
    if ($(this).attr("NewStyle")) {
        return
    }
    d = $.extend(g, d);
    var f = $(this);
    var c = $(this);
    var i = "0";
    var e = false;
    if (f.attr("SelectId")) {
        i = f.attr("SelectId").split("_")[1];
        c = $("#select_" + i);
        c.removeData("dropDownSelected");
        c.find("span i").text(f.find("option:selected").text());
        e = true
    } else {
        i = numberID();
        f.attr("SelectId", "select_" + i);
        c = $("<div id='select_" + i + "' class='" + d.StyleNormal + "'><span><i><i><span><div>").insertAfter(f);
        c.find("span i").text(f.find("option:selected").text());
        c.mouseover(function () {
            c.addClass(d.StyleHover)
        }).mouseout(function () {
            c.removeClass(d.StyleHover)
        })
    }
    if ($.browser.safari) {
        d.InnerWidthOffset = d.InnerWidthOffset + 26;
        d.OuterWidthOffset = d.OuterWidthOffset + 26
    }
    var a = f.selectedIndex;
    if (d.InnerWidth == 0) {
        d.InnerWidth = f.width() + d.InnerWidthOffset
    }
    if (d.OuterWidth == 0) {
        d.OuterWidth = f.width() + d.OuterWidthOffset
    }
    c.find("span i").css("width", d.InnerWidth);
    c.mouseover(function () {
        d.InnerWidth = f.width() + d.InnerWidthOffset;
        d.OuterWidth = f.width() + d.OuterWidthOffset;
        e = $("#down_" + i).length > 0;
        if (!$(this).data("dropDownSelected")) {
            $(this).data("dropDownSelected", true);
            var r = "";
            if (e) {
                r = "<span id='downDiv_" + i + "' >"
            } else {
                r = "<p id='down_" + i + "' ><span id='downDiv_" + i + "' >"
            }
            var q = 0;
            $("option", f).each(function (u) {
                if (a == u) {
                    r += '<a href="javascript:void(0)"  class="selected" index="' + u + '">' + $(this).text() + "</a>"
                } else {
                    r += '<a href="javascript:void(0)"  index="' + u + '">' + $(this).text() + "</a>"
                }
                q++
            });
            if (e) {
                r += "</span>"
            } else {
                r += "</span></p>"
            }
            var k = false;
            var n = q * d.itemHeight;
            if (d.MaxShow < q) {
                k = true;
                n = d.MaxShow * d.itemHeight
            }
            var j = $(r);
            if (e) {
                j = $("#down_" + i).html(r)
            }
            j.css({width: d.OuterWidth, height: n});
            j.find("span").css({width: d.OuterWidth, height: n});
            j.find("a").click(function () {
                if (c.data("tipsy")) {
                    c.MyTitle("hide")
                }
                f[0].selectedIndex = $(this).attr("index");
                j.find("a").removeClass("selected");
                $(this).addClass("selected");
                c.find("span i").text($(this).text());
                f.trigger("change");
                $(".TitleErrorRight:visible").hide("fast");
                $(".TitleErrorTop:visible").hide("fast");
                $("body").trigger("click");
                return false
            });
            j.find("a").focus(function () {
                $(this).blur()
            });
            if (!e) {
                j.appendTo(c)
            }
            c.DropSelecter({
                ControlSeleter: "#down_" + i,
                StyleHover: d.StyleHover,
                StyleSelect: d.StyleDropDown,
                Left: 0,
                Top: -13,
                Auto: false,
                IsShow: true
            });
            if (k) {
                j.css("padding-bottom", "3px");
                $("#down_" + i).show();
                $("#downDiv_" + i).jscroll({
                    W: "17px",
                    BgUrl: "url(/body/images/form/s_bg.png)",
                    Bg: "right 0 repeat-y",
                    Bar: {
                        Pos: "up",
                        Bd: {Out: "#a3c3d5", Hover: "#b7d5e6"},
                        Bg: {Out: "-51px 0 repeat-y", Hover: "-66px 0 repeat-y", Focus: "-81px 0 repeat-y"}
                    },
                    Btn: {
                        btn: true,
                        uBg: {Out: "0 0", Hover: "-17px 0", Focus: "-34px 0"},
                        dBg: {Out: "0 -21px", Hover: "-17px -21px", Focus: "-34px -21px"}
                    },
                    Fn: function () {
                    }
                })
            }
            j.hide()
        }
    });
    $(this).hide()
};

function changeCheckBox(d) {
    var c = T$("ck_" + d);
    var a = T$(d);
    if (a.checked) {
        c.className = "checkbox";
        a.checked = false
    } else {
        c.className = "checkbox checked";
        a.checked = true
    }
    $(a).trigger("change")
}

function UICheckbox(c) {
    if ($(c).attr("newStyle")) {
        return
    }
    var a = c.getAttribute("id");
    if (a) {
        if ($("#" + a).attr("checked")) {
            $(c).after("<label onclick=\"changeCheckBox('" + a + "')\" class='checkbox checked' id='ck_" + a + "'></label>")
        } else {
            $(c).after("<label onclick=\"changeCheckBox('" + a + "')\" class='checkbox' id='ck_" + a + "'></label>")
        }
    } else {
        var d = numberID();
        c.setAttribute("id", d);
        if ($("#" + d).attr("checked")) {
            $(c).after("<label onclick=\"changeCheckBox('" + d + "')\" class='checkbox' id='ck_" + d + "'></label>")
        } else {
            $(c).after("<label onclick=\"changeCheckBox('" + d + "')\" class='checkbox checked' id='ck_" + d + "'></label>")
        }
    }
}

function matchof(c, d, e) {
    var a = $("#" + d).val();
    a = a.replaceAll("(", "锛�");
    a = a.replaceAll(")", "锛�");
    a = a.replaceAll("\\", "|");
    a = a.replace(/["'\n\r\t]/g, " ");
    if (a == c) {
        return true
    } else {
        return false
    }
}

function num(c, d) {
    var a = /^[0-9]{1,20}$id/;
    if (c.search("^-?\\d+(\\.\\d+)?$") == 0) {
        return true
    } else {
        if (c.toString().length == 0) {
            return true
        } else {
            return false
        }
    }
}

function cnChar(c, d) {
    var a = new RegExp("[^\x00-\xff]");
    return a.exec(c)
}

function strDateTime(f, g) {
    if (f == null) {
        return true
    }
    if (f == "") {
        return true
    }
    var a = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
    var c = f.match(a);
    if (c == null) {
        return false
    }
    var e = new Date(c[1], c[3] - 1, c[4], c[5], c[6], c[7]);
    return (e.getFullYear() == c[1] && (e.getMonth() + 1) == c[3] && e.getDate() == c[4] && e.getHours() == c[5] && e.getMinutes() == c[6] && e.getSeconds() == c[7])
}

function limit(f, e, d, g) {
    var c = f;
    var a = c.match(/[^ -~]/g) == null ? c.length : c.length + c.match(/[^ -~]/g).length;
    if (a >= e && a <= d) {
        return true
    } else {
        return false
    }
}

function notmatch(c, a, d) {
    if (c == a) {
        return false
    } else {
        return true
    }
}

function email(a, c) {
    if (a.length == 0) {
        return false
    }
    if (a.indexOf(".") > 0 && a.indexOf("@") > 0) {
        return true
    } else {
        return false
    }
}

function telphone(c, d) {
    var a = new RegExp("[0-9]{2})+-([0-9]{4})+-([0-9]{4}");
    if (!a.exec(c)) {
        return false
    } else {
        return true
    }
}

function isMobile(a) {
    var c = /^1[3|4|5|8][0-9]\d{8}$/;
    if (c.test(a)) {
        return true
    } else {
        return false
    }
}

function isPhone(a) {
    var c = /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/;
    if (c.test(a)) {
        return true
    } else {
        return false
    }
}

function Ui(f) {
    var g = "";
    if (!$(f)) {
        return null
    }
    var a = T$(f).getElementsByTagName("*");
    for (var d = 0; d < a.length; d++) {
        if (a[d].tagName != undefined) {
            var c = a[d].tagName.toLowerCase();
            var e = a[d].getAttribute("id");
            if (c == "input" || c == "textarea") {
                if (a[d].type.toLowerCase() == "text" || a[d].type.toLowerCase() == "hidden" || a[d].type.toLowerCase() == "password" || a[d].tagName.toLowerCase() == "textarea") {
                    $(a[d]).UiText()
                } else {
                    if (a[d].type.toLowerCase() == "radio") {
                        $(a[d]).UiRadio()
                    } else {
                        if (a[d].type.toLowerCase() == "checkbox") {
                            UICheckbox(a[d])
                        } else {
                            if (a[d].type.toLowerCase() == "button") {
                                $(a[d]).UIButton()
                            }
                        }
                    }
                }
            } else {
                if (c == "select") {
                    $(a[d]).UiSelect()
                }
            }
        }
    }
}

$.fn.Ui = function () {
    var a = [];
    if ($(this)[0]) {
        a = $(this)[0].getElementsByTagName("*")
    }
    for (var d = 0; d < a.length; d++) {
        if (a[d].tagName != undefined) {
            var c = a[d].tagName.toLowerCase();
            var e = a[d].getAttribute("id");
            if (c == "input" || c == "textarea") {
                if (a[d].type.toLowerCase() == "text" || a[d].type.toLowerCase() == "hidden" || a[d].type.toLowerCase() == "password" || a[d].tagName.toLowerCase() == "textarea") {
                    $(a[d]).UiText()
                } else {
                    if (a[d].type.toLowerCase() == "radio") {
                        $(a[d]).UiRadio()
                    } else {
                        if (a[d].type.toLowerCase() == "checkbox") {
                            UICheckbox(a[d])
                        } else {
                            if (a[d].type.toLowerCase() == "button") {
                                $(a[d]).UIButton()
                            }
                        }
                    }
                }
            } else {
                if (c == "select") {
                    $(a[d]).UiSelect()
                }
            }
        }
    }
};
$.fn.AStop = function (a) {
    $(this).click(function (d) {
        var d = d ? d : window.event, c;
        d.preventDefault();
        if (navigator.appName == "Microsoft Internet Explorer") {
            c = d.srcElement
        } else {
            c = d.target
        }
        if (c.getAttribute("disabled")) {
            return false
        }
    })
};
jQuery.fn.fixedIn = function (c) {
    var d = {offsetX: 0, offsetY: 0, selecter: ""};
    c = $.extend(d, c);
    var f = $(this);
    var e = numberID();
    var a = $(c.selecter);
    $(window).scroll(function () {
        var k = $(window).scrollTop();
        var n = f.offset() ? f.offset().top : 0;
        var i = n + f.height() - 2 * c.offsetY - a.height();
        var j = k - n + c.offsetY;
        var g = k - n + c.offsetY;
        if (k > n && k <= i) {
            if (jQuery.browser.msie && jQuery.browser.version == "6.0") {
                a.css({position: "absolute", top: j + "px", left: c.offsetX + "px"})
            } else {
                a.css({position: "fixed", top: c.offsetY + "px", left: (f.offset().left + c.offsetX) + "px"})
            }
        } else {
            if (k > i) {
                a.css({position: "absolute", top: (f.height() - c.offsetY - a.height()) + "px", left: c.offsetX + "px"})
            } else {
                a.css({position: "absolute", top: c.offsetY + "px", left: c.offsetX + "px"})
            }
        }
    });
    $(window).trigger("scroll")
};
$.fn.Loadding = function (a) {
    var e = {
        StyleName: "Loadding",
        Str: "鍔犺浇涓�...",
        Width: 0,
        Height: 0,
        Position: "on",
        IsShow: true,
        OffsetY: 0,
        OffsetX: 0,
        OffsetYGIF: 0,
        OffsetXGIF: 0
    };
    a = $.extend(e, a);
    if (!a.IsShow) {
        var d = $(this).attr("LoddingId");
        $("#Lodding_" + d).remove();
        $(this).removeAttr("LoddingId");
        return
    }
    var f = numberID();
    var c = ";position:absolute;";
    if (a.Position == "top") {
        c += "margin-top:-" + $(this).outerHeight() + "px;"
    } else {
        if (a.Position == "bottom") {
            c += "margin-top:0;"
        } else {
            if (a.Position == "left") {
                c += "margin-left:-" + $(this).outerWidth() + "px;"
            } else {
                if (a.Position == "right") {
                    c += "margin-right:" + $(this).outerWidth() + "px;"
                } else {
                    if ($(this).offset()) {
                        c += "top:" + $(this).offset().top + "px;left:" + $(this).offset().left + "px;"
                    }
                }
            }
        }
    }
    if (a.Width == 0) {
        c += "width:" + $(this).outerWidth() + "px;"
    } else {
        if (a.Width > 0) {
            c += "width:" + a.Width + "px;"
        }
    }
    if (a.OffsetX == 0) {
        a.OffsetXGIF = $(this).outerWidth() / 2 - 110
    } else {
        a.OffsetXGIF = $(this).outerWidth() / 2 - 110 + a.OffsetXGIF
    }
    if (a.Height == 0) {
        c += "height:" + $(this).outerHeight() + "px;"
    } else {
        if (a.Height > 0) {
            c += "height:" + a.Height + "px;"
        }
    }
    $(this).after('<div id="Lodding_' + f + '" class="' + a.StyleName + '" style="' + c + '" ><div class="GIF">' + a.Str + "</div></div>");
    $(this).attr("LoddingId", f);
    $("#Lodding_" + f).fadeTo("fast", 0.6);
    $(this).fixedIn({selecter: "#Lodding_" + f + " .GIF", offsetX: a.OffsetXGIF, offsetY: a.OffsetYGIF})
};

function stopDefault(a) {
    if (a && a.preventDefault) {
        a.preventDefault()
    } else {
        window.event.returnValue = false
    }
    return false
}

function checkPageInput(c, a) {
    c.keyCode == 13 && $("#JumpButton").trigger("click")
}

jQuery.fn.ScrollTo = function (a) {
    var c = {Speed: 300, Top: 0};
    a = $.extend(c, a);
    o = jQuery.speed(a.Speed);
    return this.each(function () {
        new jQuery.fx.ScrollTo(this, o, a.Top)
    })
};
jQuery.fx.ScrollTo = function (c, f, a) {
    var d = this;
    d.o = f;
    d.e = c;
    d.p = jQuery.getPos(c);
    d.s = jQuery.getScroll();
    d.clear = function () {
        clearInterval(d.timer);
        d.timer = null
    };
    d.t = (new Date).getTime();
    d.step = function () {
        var e = (new Date).getTime();
        var g = (e - d.t) / d.o.duration;
        if (e >= d.o.duration + d.t) {
            d.clear();
            setTimeout(function () {
                d.scroll(d.p.y, d.p.x)
            }, 13)
        } else {
            st = ((-Math.cos(g * Math.PI) / 2) + 0.5) * (d.p.y - d.s.t) + d.s.t;
            sl = ((-Math.cos(g * Math.PI) / 2) + 0.5) * (d.p.x - d.s.l) + d.s.l;
            d.scroll(st, sl)
        }
    };
    d.scroll = function (g, e) {
        window.scrollTo(e - a, g - a)
    };
    d.timer = setInterval(function () {
        d.step()
    }, 13)
};
jQuery.intval = function (a) {
    a = parseInt(a);
    return isNaN(a) ? 0 : a
};
jQuery.getClient = function (a) {
    if (a) {
        w = a.clientWidth;
        h = a.clientHeight
    } else {
        w = (window.innerWidth) ? window.innerWidth : (document.documentElement && document.documentElement.clientWidth) ? document.documentElement.clientWidth : document.body.offsetWidth;
        h = (window.innerHeight) ? window.innerHeight : (document.documentElement && document.documentElement.clientHeight) ? document.documentElement.clientHeight : document.body.offsetHeight
    }
    return {w: w, h: h}
};
jQuery.getScroll = function (a) {
    if (a) {
        t = a.scrollTop;
        l = a.scrollLeft;
        w = a.scrollWidth;
        h = a.scrollHeight
    } else {
        if (document.documentElement && document.documentElement.scrollTop) {
            t = document.documentElement.scrollTop;
            l = document.documentElement.scrollLeft;
            w = document.documentElement.scrollWidth;
            h = document.documentElement.scrollHeight
        } else {
            if (document.body) {
                t = document.body.scrollTop;
                l = document.body.scrollLeft;
                w = document.body.scrollWidth;
                h = document.body.scrollHeight
            }
        }
    }
    return {t: t, l: l, w: w, h: h}
};
jQuery.getPos = function (i) {
    var c = 0;
    var d = 0;
    var a = jQuery.intval(jQuery.css(i, "width"));
    var f = jQuery.intval(jQuery.css(i, "height"));
    var g = i.offsetWidth;
    var j = i.offsetHeight;
    while (i.offsetParent) {
        c += i.offsetLeft + (i.currentStyle ? jQuery.intval(i.currentStyle.borderLeftWidth) : 0);
        d += i.offsetTop + (i.currentStyle ? jQuery.intval(i.currentStyle.borderTopWidth) : 0);
        i = i.offsetParent
    }
    c += i.offsetLeft + (i.currentStyle ? jQuery.intval(i.currentStyle.borderLeftWidth) : 0);
    d += i.offsetTop + (i.currentStyle ? jQuery.intval(i.currentStyle.borderTopWidth) : 0);
    return {x: c, y: d, w: a, h: f, wb: g, hb: j}
};
jQuery.fx.ScrollTo = function (c, f, a) {
    var d = this;
    d.o = f;
    d.e = c;
    d.p = jQuery.getPos(c);
    d.s = jQuery.getScroll();
    d.clear = function () {
        clearInterval(d.timer);
        d.timer = null
    };
    d.t = (new Date).getTime();
    d.step = function () {
        var e = (new Date).getTime();
        var g = (e - d.t) / d.o.duration;
        if (e >= d.o.duration + d.t) {
            d.clear();
            setTimeout(function () {
                d.scroll(d.p.y, d.p.x)
            }, 13)
        } else {
            st = ((-Math.cos(g * Math.PI) / 2) + 0.5) * (d.p.y - d.s.t) + d.s.t;
            sl = ((-Math.cos(g * Math.PI) / 2) + 0.5) * (d.p.x - d.s.l) + d.s.l;
            d.scroll(st, sl)
        }
    };
    d.scroll = function (g, e) {
        window.scrollTo(e - a, g - a)
    };
    d.timer = setInterval(function () {
        d.step()
    }, 13)
};

function ajaxNextPage(c) {
    var a = $(this).attr("href").replace("ForBuy", "ForBuy/ajaxList");
    ajaxUrl(a, true, true)
}

Title = function () {
    var d, c, i, g, j, a, e = 0;
    return {
        Show: function (f) {
            var k = {CloseTime: 0, Height: 99, callback: "Close()", fromObj: null};
            f = Object.extend(k, f);
            if (!e) {
                d = document.createElement("div");
                p.id = "tinybox";
                c = document.createElement("div");
                m.id = "tinymask";
                b = document.createElement("div");
                b.id = "tinycontent";
                document.body.appendChild(m);
                document.body.appendChild(p);
                p.appendChild(b);
                m.onclick = NetBox.hide;
                window.onresize = NetBox.resize;
                e = 1
            }
        }, Close: function () {
        }
    }
}();
$.fn.Title = function (a) {
    var d = {StyleName: "TitleError", Title: "Message", Position: "w", Offset: 10, OffsetY: 5, IsShow: true};
    a = $.extend(d, a);
    if ($(this).attr("TitleStyleName")) {
        a.StyleName = $(this).attr("TitleStyleName")
    }
    if ($(this).attr("title")) {
        a.Title = $(this).attr("title")
    }
    if (a.Title.indexOf("#") == 0) {
        a.Title = $(a.Title).html()
    }
    if ($(this).attr("Offset")) {
        a.Offset = $(this).attr("Offset")
    }
    if ($(this).attr("TitlePosition")) {
        a.Position = $(this).attr("TitlePosition")
    }
    var e = $(this).attr("id") + a.StyleName;
    var c = $(this).offset();
    if (a.IsShow) {
        $(this).MyTitle({defaultCss: "tipsr", title: zb.L("ZB鎻愮ず鎮�"), trigger: "manual"});
        if (!$(this).attr("errmsg")) {
            $(this).attr("errmsg", a.Title)
        }
        $(this).attr("errorStyle", "tipsr");
        $(this).MyTitle("show")
    } else {
        $(this).MyTitle("hide");
        $(this).removeAttr("errorStyle")
    }
};

function CheckTextBox(obj) {
    var pattern = obj.attr("pattern");
    if (obj.attr("webpattern")) {
        pattern = obj.attr("webpattern")
    }
    if (pattern != null && pattern != undefined && pattern != "") {
        if ($(obj).attr("valueDemo")) {
            if ($(obj).attr("valueDemo") == $(obj).val()) {
                if (!$(obj).data("tipsy")) {
                    $(obj).MyTitle({defaultCss: "tipsr", title: "An error occurs here!", trigger: "manual"})
                }
                $(obj).attr("errorStyle", "tipsr");
                $(obj).MyTitle("show");
                return false
            }
        }
        var subs = pattern.split(";");
        for (var j = 0; j < subs.length; j++) {
            var cValue = obj.val();
            cValue = cValue.replaceAll("(", "锛�");
            cValue = cValue.replaceAll(")", "锛�");
            cValue = cValue.replaceAll("\\", "|");
            var paraValue = cValue.replace(/["'\n\r\t]/g, " ");
            var execStr = subs[j].replace("(", '("' + paraValue + '",');
            if (execStr.indexOf(",)") > 0) {
                execStr = execStr.replace(")", "'" + obj.name + "')")
            } else {
                execStr = execStr.replace(")", ",'" + obj.name + "')")
            }
            if (execStr && !this.eval(execStr)) {
                var srcEm = $(obj).attr("errormsg");
                if (zb.form.error.length > 0) {
                    $(obj).attr("errormsg", zb.form.error)
                }
                if (!$(obj).data("tipsy")) {
                    $(obj).MyTitle({defaultCss: "tipsr", title: "璇烽噸鏂板～鍐欐湰淇℃伅銆�", trigger: "manual"})
                }
                $(obj).attr("errorStyle", "tipsr");
                $(obj).MyTitle("show");
                if (zb.form.error.length > 0) {
                    $(obj).attr("errormsg", srcEm);
                    zb.form.error = ""
                }
                return false
            } else {
                $(obj).removeAttr("errorStyle");
                showRightIcon(obj)
            }
        }
        if ($(obj).data("tipsy")) {
            $(obj).MyTitle("hide")
        }
        return true
    } else {
        return true
    }
}

function showRightIcon(c) {
    var d = $(c).offset();
    var a = $("<div class='rightIcon' style='left:" + (d.left + $(c).width() + 20) + "px;top:" + (d.top + 5) + "px'></div>").appendTo("body");
    $(c).bind("focus", function () {
        a.remove()
    });
    $(c).bind("keydown", function () {
        a.remove()
    })
}

function CheckSelect(a) {
    var c = a.selectedIndex;
    var e = a.getAttribute("pattern");
    if (a.getAttribute("webpattern")) {
        e = a.getAttribute("webpattern")
    }
    if (e != null && e != undefined && c <= 0) {
        var d = $("#" + $(a).attr("selectid"));
        if (!d.data("tipsy")) {
            d.MyTitle({defaultCss: "tipsr", title: "璇烽€夋嫨璇ラ€夐」銆�", trigger: "manual"})
        }
        if ($(a).attr("errormsg")) {
            d.attr("errormsg", $(a).attr("errormsg"))
        }
        d.attr("errorStyle", "tipsr");
        d.MyTitle("show");
        return false
    } else {
        return true
    }
}

function GoOn(a) {
    var c = window.eventList[a];
    window.eventList[a] = null;
    if (c.NextStep) {
        c.NextStep()
    } else {
        c()
    }
}

function Pause(d, e) {
    if (window.eventList == null) {
        window.eventList = new Array()
    }
    var c = -1;
    for (var a = 0; a < window.eventList.length; a++) {
        if (window.eventList[a] == null) {
            window.eventList[a] = d;
            c = a;
            break
        }
    }
    if (c == -1) {
        c = window.eventList.length;
        window.eventList[c] = d
    }
    setTimeout("GoOn(" + c + ")", e)
}

function FormToStr(a) {
    return FormToStrFun(a, false)
}

function errCalback(a) {
    Close()
}

function errSelectCalback(a) {
    Close()
}

var currentErrorObj = null;

function FormToStrFun(f, e) {
    var g = "";
    if (!T$(f)) {
        return null
    }
    var a = T$(f).getElementsByTagName("*");
    for (var d = 0; d < a.length; d++) {
        if (a[d].tagName != undefined) {
            if (a[d].tagName.toLowerCase() == "input" || a[d].tagName.toLowerCase() == "textarea") {
                if (a[d].type.toLowerCase() == "text" || a[d].type.toLowerCase() == "hidden" || a[d].type.toLowerCase() == "password" || a[d].tagName.toLowerCase() == "textarea") {
                    if (a[d].name.length > 0) {
                        if ($(a[d]).attr("valueDemo") != $(a[d]).val()) {
                            g += "&" + encodeURIComponent(a[d].name) + "=" + encodeURIComponent($.trim(a[d].value))
                        } else {
                            g += "&" + encodeURIComponent(a[d].name) + "= "
                        }
                    }
                    if ($(a[d]).is(":visible") && !CheckTextBox($(a[d]))) {
                        if ($(a[d]).attr("errorName")) {
                            currentErrorObj = a[d]
                        } else {
                            if (e) {
                                $(a[d]).ScrollTo({Top: 100})
                            }
                        }
                        return null
                    }
                } else {
                    if (a[d].type.toLowerCase() == "checkbox") {
                        if (a[d].checked) {
                            if (g.indexOf("&" + encodeURIComponent(a[d].name) + "=") > -1) {
                                g = g.replace("&" + encodeURIComponent(a[d].name) + "=", "&" + encodeURIComponent(a[d].name) + "=" + encodeURIComponent(a[d].value) + encodeURIComponent("\u2229"))
                            } else {
                                g += "&" + encodeURIComponent(a[d].name) + "=" + encodeURIComponent(a[d].value)
                            }
                        }
                    } else {
                        if (a[d].type.toLowerCase() == "radio" || a[d].type.toLowerCase() == "checkbox") {
                            if (a[d].checked) {
                                g += "&" + encodeURIComponent(a[d].name) + "=" + encodeURIComponent(a[d].value)
                            }
                        }
                    }
                }
            } else {
                if (a[d].tagName.toLowerCase() == "select") {
                    var c = a[d].selectedIndex;
                    if (a[d].getAttribute("pattern") != null && a[d].getAttribute("pattern") != undefined && c <= 0) {
                        if (!CheckSelect(a[d])) {
                            if ($(a[d]).attr("errorName")) {
                                currentErrorObj = a[d];
                                Wrong("璇烽€夋嫨" + $(a[d]).attr("errorName") + "锛�", {callback: "errSelectCalback(" + e + ")"})
                            } else {
                                if (e) {
                                    $(a[d]).parent().ScrollTo({Top: 100})
                                }
                            }
                            return null
                        }
                    } else {
                    }
                    if (c >= 0) {
                        if (a[d].options) {
                            if (a[d].name.length > 0) {
                                g += "&" + encodeURIComponent(a[d].name) + "=" + encodeURIComponent(a[d].options[c].value)
                            }
                        }
                    }
                }
            }
        }
    }
    return g.substring(1, g.length)
}

function Redirect(a) {
    self.location = a
}

(function (d) {
    function a(e) {
        if (e.attr("title") || typeof(e.attr("mytitle")) != "string") {
            e.attr("mytitle", e.attr("title") || "").removeAttr("title")
        }
    }

    function c(f, e) {
        this.$element = d(f);
        this.options = e;
        this.enabled = true;
        a(this.$element)
    }

    c.prototype = {
        show: function () {
            var u = this.getTitle();
            if (u && this.enabled) {
                var i = this.tip();
                i.addClass("tipsr");
                var x = this.$element.attr("errorStyle");
                if (!x) {
                    if (this.$element.attr("mytitle")) {
                        i.find(".inner-inner")[this.options.html ? "html" : "text"](this.$element.attr("mytitle"))
                    } else {
                        i.find(".inner-inner")[this.options.html ? "html" : "text"](u)
                    }
                    i[0].className = "tipsy"
                } else {
                    var n = this.$element.attr("errormsg");
                    if (this.$element.attr("errmsg")) {
                        n = this.$element.attr("errmsg")
                    }
                    i.find(".inner-inner")[this.options.html ? "html" : "text"](n);
                    i[0].className = "tipsy " + x
                }
                i.remove().css({top: 0, left: 0, visibility: "hidden", display: "block"}).appendTo(document.body);
                var v = i.width();
                i.find(".tipsy-inner").css({width: "236px"});
                v = 236;
                var q = d.extend({}, this.$element.offset(), {
                    width: this.$element[0].offsetWidth,
                    height: this.$element[0].offsetHeight
                });
                var e = i[0].offsetWidth, j = i[0].offsetHeight;
                var y = (typeof this.options.gravity == "function") ? this.options.gravity.call(this.$element[0]) : this.options.gravity;
                var g = this.$element.attr("position");
                if (g) {
                    y = g
                }
                var k = d(window).width();
                var z = d(window).height();
                if ((k < (q.left + q.width + v + 10)) && (y.charAt(0) == "w")) {
                    y = "n"
                }
                if (((z < (q.top + q.height + j + 10)) || k < (q.left + q.width / 2 + v / 2 + 10)) && (y.charAt(0) == "n")) {
                    y = "e"
                }
                if ((q.left < (v + 10)) && (y.charAt(0) == "e")) {
                    y = "s"
                }
                if ((q.top < (j + 10)) && (y.charAt(0) == "s")) {
                    y = "n"
                }
                var r;
                switch (y.charAt(0)) {
                    case"n":
                        r = {top: q.top + q.height + this.options.offset, left: q.left + q.width / 2 - e / 2};
                        break;
                    case"s":
                        var f = i.find(".tipsy-arrow");
                        f.css({top: j - f.height()});
                        r = {top: q.top - j - this.options.offset, left: q.left + q.width / 2 - e / 2};
                        break;
                    case"e":
                        r = {top: q.top + q.height / 2 - j / 2, left: q.left - e - this.options.offset};
                        break;
                    case"w":
                        r = {top: q.top + q.height / 2 - j / 2, left: q.left + q.width + this.options.offset};
                        break
                }
                if (y.length == 2) {
                    if (y.charAt(1) == "w") {
                        r.left = q.left + q.width / 2 - 15
                    } else {
                        r.left = q.left + q.width / 2 - e + 15
                    }
                }
                i.css(r).addClass("tipsy-" + y);
                if (this.options.fade) {
                    i.stop().css({
                        opacity: 0,
                        display: "block",
                        visibility: "visible"
                    }).animate({opacity: this.options.opacity})
                } else {
                    i.css({visibility: "visible", opacity: this.options.opacity})
                }
            }
        }, hide: function () {
            if (this.options.fade) {
                this.tip().stop().fadeOut(function () {
                    d(this).remove()
                })
            } else {
                this.tip().remove()
            }
        }, getTitle: function () {
            var g, e = this.$element, f = this.options;
            a(e);
            var g, f = this.options;
            if (typeof f.title == "string") {
                g = f.title == "title" ? e.attr("mytitle") : f.title
            } else {
                if (typeof f.title == "function") {
                    g = f.title.call(e[0])
                }
            }
            g = ("" + g).replace(/(^\s*|\s*$)/, "");
            return g || f.fallback
        }, tip: function () {
            if (!this.$tip) {
                this.$tip = d('<div class="tipsy"></div>').html('<div class="tipsy-arrow"></div><div class="tipsy-inner"><div class="inner-inner"></div></div></div>')
            }
            return this.$tip
        }, validate: function () {
            if (!this.$element[0].parentNode) {
                this.hide();
                this.$element = null;
                this.options = null
            }
        }, enable: function () {
            this.enabled = true
        }, disable: function () {
            this.enabled = false
        }, toggleEnabled: function () {
            this.enabled = !this.enabled
        }
    };
    d.fn.MyTitle = function (j) {
        if (j === true) {
            return this.data("tipsy")
        } else {
            if (typeof j == "string") {
                if (this.data("tipsy")) {
                    return this.data("tipsy")[j]()
                } else {
                    return null
                }
            }
        }
        j = d.extend({}, d.fn.MyTitle.defaults, j);

        function i(q) {
            var r = d.data(q, "tipsy");
            if (!r) {
                r = new c(q, d.fn.MyTitle.elementOptions(q, j));
                d.data(q, "tipsy", r)
            }
            return r
        }

        function n() {
            var q = i(this);
            q.hoverState = "in";
            if (j.delayIn == 0) {
                q.show()
            } else {
                setTimeout(function () {
                    if (q.hoverState == "in") {
                        q.show()
                    }
                }, j.delayIn)
            }
        }

        function g() {
            var q = i(this);
            q.hoverState = "out";
            if (j.delayOut == 0) {
                q.hide()
            } else {
                setTimeout(function () {
                    if (q.hoverState == "out") {
                        q.hide()
                    }
                }, j.delayOut)
            }
        }

        if (!j.live) {
            this.each(function () {
                i(this)
            })
        }
        if (j.trigger != "manual") {
            var e = j.live ? "live" : "bind", k = j.trigger == "hover" ? "mouseenter" : "focus",
                f = j.trigger == "hover" ? "mouseleave" : "blur";
            this[e](k, n)[e](f, g)
        }
        return this
    };
    d.fn.MyTitle.defaults = {
        defaultCss: " ",
        delayIn: 0,
        delayOut: 0,
        fade: false,
        fallback: "",
        gravity: "w",
        html: true,
        live: false,
        offset: 0,
        opacity: 1,
        title: "title",
        trigger: "hover"
    };
    d.fn.MyTitle.elementOptions = function (f, e) {
        return d.metadata ? d.extend({}, e, d(f).metadata()) : e
    };
    d.fn.MyTitle.autoNS = function () {
        return d(this).offset().top > (d(document).scrollTop() + d(window).height() / 2) ? "s" : "n"
    };
    d.fn.MyTitle.autoWE = function () {
        return d(this).offset().left > (d(document).scrollLeft() + d(window).width() / 2) ? "e" : "w"
    }
})(jQuery);

function changeTitle(c, a) {
    if (a.length > 0) {
        $(c).attr("errorStyle", a)
    } else {
        $(c).removeAttr("errorStyle")
    }
}

function redirecToWithReferer(c) {
    var a = document.createElement("a");
    a.href = c;
    document.body.appendChild(a);
    a.click()
}

jQuery.fn.FixedHeader = function (a) {
    var d = jQuery.extend({headerrowsize: 1, highlightrow: false, highlightclass: "highlight"}, a);
    this.each(function (j) {
        var q = $(this);
        var k = $(this).parent();
        var f = q.find("tr:lt(" + d.headerrowsize + ")");
        var g = "th";
        if (f.find(g).length == 0) {
            g = "td"
        }
        if (f.find(g).length > 0) {
            f.find(g).each(function () {
                $(this).css("width", $(this).width())
            });
            var n = q.clone().empty();
            var e = c(q);
            n.attr("id", "fixedtableheader" + j).css({
                position: "fixed",
                top: "0",
                left: q.offset().left
            }).append(f.clone()).width(e).hide().appendTo(k);
            if (d.highlightrow) {
                $("tr:gt(" + (d.headerrowsize - 1) + ")", q).hover(function () {
                    $(this).addClass(d.highlightclass)
                }, function () {
                    $(this).removeClass(d.highlightclass)
                })
            }
            $(window).scroll(function () {
                if (jQuery.browser.msie && jQuery.browser.version == "6.0") {
                    n.css({position: "absolute", top: $(window).scrollTop(), left: q.offset().left})
                } else {
                    n.css({position: "fixed", top: "0", left: q.offset().left - $(window).scrollLeft()})
                }
                var i = $(window).scrollTop();
                var r = f.offset().top;
                if (i > r && i <= (r + q.height() - f.height())) {
                    n.show()
                } else {
                    n.hide()
                }
            });
            $(window).resize(function () {
                if (n.outerWidth() != q.outerWidth()) {
                    f.find(g).each(function (r) {
                        var i = $(this).width();
                        $(this).css("width", i);
                        n.find(g).eq(r).css("width", i)
                    });
                    n.width(q.outerWidth())
                }
                n.css("left", q.offset().left)
            })
        }
    });

    function c(f) {
        var e = f.outerWidth();
        return e
    }
};

function encodeURI(a) {
    a = a + "";
    a = a.replaceAll("-", "__");
    a = a.replaceAll(".", "___");
    a = a.replaceAll("[.]", "___");
    return encodeURIComponent(a)
}

$.extend({
    loadScript: function (c) {
        var k = new Date().getTime();
        var g = typeof c == "string" ? [c] : c;
        var n = $("head");
        for (var a = 0; a < g.length; a++) {
            var e = document.getElementsByTagName("head")[0];
            var j = document.createElement("script");
            j.type = "text/javascript";
            j.id = k + "-" + a;
            j.src = g[a] + "?" + k;
            e.appendChild(j)
        }
    }, getServerDate: function (a) {
        return new Date(a.getResponseHeader("Date"))
    }, getServerTime: function (a) {
        return this.getServerDate(a).getTime()
    }
});

function Void() {
}

var urlreplace = "/ajaxList-";

function newUrl(d, a) {
    var c = $(a).attr("href");
    if (c && c.indexOf("javascript") != 0 && c.indexOf("-") > 0) {
        c = c.replace("-", urlreplace);
        ajaxUrl(c, true, false, "text")
    }
    if (d && d.preventDefault) {
        d.preventDefault()
    } else {
        window.event.returnValue = false
    }
    return false
}

function checkPageInput(c, a) {
    c.keyCode == 13 && $("#JumpButton").trigger("click")
}

function jumpPage(e) {
    var d = T$("PagerInput").value;
    if (d > e) {
        Wrong("page number is too large", {CloseTime: 1});
        return
    }
    if (d < 1) {
        if (d == "") {
            Info("please input page number", {CloseTime: 1})
        } else {
            Wrong("page number is too small", {CloseTime: 1})
        }
        return
    } else {
        var c = /^[0-9]+.?[0-9]*$/;
        if (c.test(d)) {
            var a = getPageUrl();
            if (a.length > 0) {
                ajaxUrl(a, true, true, "text")
            }
        } else {
            Wrong("Page number you entered is not valid, please re-enter.", {CloseTime: 1})
        }
    }
}

var zb = {
    cookiKeys: {
        uon: JsCommon.uon,
        uname: JsCommon.uname,
        uid: JsCommon.uid,
        aid: JsCommon.aid,
        rid: JsCommon.rid,
        vip: JsCommon.vip,
        aname: JsCommon.aname,
        note: JsCommon.note,
        lan: JsCommon.lan,
        other: JsCommon.other
    },
    mainDomain: JsCommon.mainDomain,
    vipDomain: JsCommon.vipDomain,
    p2pDomain: JsCommon.p2pDomain,
    transDomain: JsCommon.transDomain,
    staticDomain: JsCommon.staticDomain,
    urlsAjax: {},
    ajax: function (d) {
        var c = true;
        if (d.needLoading == undefined) {
            c = true
        } else {
            c = d.needLoading == false ? false : true
        }
        var f = this;
        var a = d.url.indexOf("?") > -1 ? d.url.substring(0, d.url.indexOf("?")) : d.url;
        var e = f.urlsAjax[a];
        if (e == null) {
            f.urlsAjax[a] = {loading: false, needLoading: c, url: a};
            e = f.urlsAjax[a]
        }
        new this.ajaxDeal(d, e)
    },
    ajaxDeal: function (e, j) {
        if (!e.url) {
            Wrong("url鍙傛暟蹇呴』浼犻€掞紒")
        }
        if (j.loading) {
            return
        }
        var i = this, a = "", c = e.div || null, d = e.needLogin || false, g = e.timeout || 60000;
        var f = true;
        if (e.async == undefined) {
            f = true
        } else {
            f = e.async == false ? false : true
        }
        i.dataType = e.dataType || "xml";
        i.type = e.type || "post";
        if (j.needLogin && !zb.user.checkLogin()) {
            return
        }
        if (e.formId) {
            a = FormToStr(e.formId);
            if (a == null) {
                return
            }
        }
        j.loading = true;
        if (c && j.needLoading) {
            $("#" + c).Loadding({OffsetXGIF: 0, OffsetYGIF: 0})
        }
        $.ajax({
            async: f, cache: false, type: i.type, dataType: i.dataType, url: e.url, data: a, error: function (k) {
                j.loading = false;
                if (c) {
                    $("#" + c).Loadding({IsShow: false})
                }
            }, timeout: g, success: function (k) {
                j.loading = false;
                if (c) {
                    $("#" + c).Loadding({IsShow: false})
                }
                if (i.dataType == "xml") {
                    if ($(k).find("State").text() == "true") {
                        if (typeof e.suc == "function") {
                            (function (n) {
                                return e.suc(n)
                            })(k)
                        } else {
                            Right($(k).find("Des").text())
                        }
                    } else {
                        if (typeof e.err == "function") {
                            (function (n) {
                                return e.err(n)
                            })(k)
                        } else {
                            Wrong($(k).find("Des").text())
                        }
                    }
                } else {
                    if (i.dataType.indexOf("json") == 0) {
                        if (k.isSuc) {
                            if (typeof e.suc == "function") {
                                (function (n) {
                                    return e.suc(n)
                                })(k)
                            } else {
                                Right(k.des)
                            }
                        } else {
                            if (typeof e.err == "function") {
                                (function (n) {
                                    return e.err(n)
                                })(k)
                            } else {
                                Wrong(k.des)
                            }
                        }
                    } else {
                        if (typeof e.suc == "function") {
                            (function (n) {
                                return e.suc(n)
                            })(k)
                        }
                        if (typeof e.err == "function") {
                            (function (n) {
                                return e.err(n)
                            })(k)
                        }
                    }
                }
            }
        })
    },
    top: {
        init: function () {
            zb.user.init()
        }, close_city: function () {
            $("#selectcity_div").hide()
        }, show_city: function () {
            $("#selectcity_div").show()
        }, changeCity: function (a) {
            $(".tuangouflsy_pf_chengshi").each(function () {
                $(this).hide()
            });
            $("#city_span_" + a).show()
        }
    },
    tool: {
        param: function (a) {
            var c = window.location.search.substr(1).match(new RegExp("(^|&)" + a + "=([^&]*)(&|$)", "i"));
            if (c != null) {
                return unescape(c[2])
            }
            return null
        }, addBookmark: function (c) {
            var a = location.href;
            if (window.sidebar) {
                window.sidebar.addPanel(c, a, "")
            } else {
                if (document.all) {
                    window.external.AddFavorite(a, c)
                } else {
                    if (window.opera && window.print) {
                        return true
                    }
                }
            }
        }, initBackBtn: function () {
            var a = $.cookie(zb.cookiKeys.rid);
            if (a > 0) {
                var c = "/admin/Module/list.js";
                $.loadScript(c)
            }
        }, getTimeShowByMillSeconds: function (a) {
            var d = parseInt(a / (24 * 60 * 60 * 1000));
            var c = parseInt(a / (60 * 60 * 1000) - d * 24);
            c = c >= 10 ? c : "0" + c;
            var e = parseInt((a / (60 * 1000)) - d * 24 * 60 - c * 60);
            e = e >= 10 ? e : "0" + e;
            var f = parseInt(a / 1000 - d * 24 * 60 * 60 - c * 60 * 60 - e * 60);
            f = f >= 10 ? f : "0" + f;
            return "" + d + "澶�" + c + "灏忔椂" + e + "鍒�" + f + "绉�"
        }, isFloat: function (a) {
            if (a) {
                var c = /^[0-9]*\.?[0-9]*$/;
                if (!c.test(a)) {
                    return false
                }
            } else {
                return false
            }
            return true
        }
    },
    user: {
        cookieInit: false, loginStatus: false, inAjaxing: false, lastPrice: 0, tickJson: null, isLogin: function () {
            if (!this.cookieInit) {
                this.init()
            }
            return this.loginStatus
        }, init: function () {
            this.cookieInit = true;
            this.loginStatus = $.cookie(zb.cookiKeys.uon) == "1";
            var e = this;
            if (e.firstTitle.length == 0) {
                e.firstTitle = document.title
            }
            var d = document.location.pathname;
            if (e.loginStatus) {
                e.zcticker()
            }
            setInterval(function () {
            }, 32000);
            var c = document.location.href;
            if (JuaBox.isMySelf() && d.indexOf("/u") == -1 && d.indexOf("/self/") == -1) {
                localStorage[ZNAME + "fromurl"] = c
            }
            if (e.loginStatus) {
                var a = $.cookie(zb.cookiKeys.uname);
                $(".logined").show();
                $(".nologin").hide();
                $("#myAccount").hide();
                $(".d_kefu").hide();
                $(".d_assets").show();
                $("#assets").show();
                $("#logOut").show();
                $("#assets .u_name").text(this.showTime() + "锛�" + a);
                $(".d_menu1 .m2").show();
                $("#assets i").attr("class", "u-level-" + $.cookie(zb.cookiKeys.vip));
                e.balance();
                e.zcticker();
                set_menu_hover(2);
                this.assets()
            } else {
                $(".logined").hide();
                $(".nologin").show();
                $("#myAccount").show();
                $("#assets").hide();
                $(".d_assets").hide();
                $("#myAccountInfo a").show();
                $("#finaPanelDown1").hide();
                $(".d_kefu").show();
                $(".d_menu1 .m2").hide();
                set_menu_hover(1)
            }
        }, showTime: function () {
            var c = new Date();
            var a = c.getHours();
            return a >= 0 && a < 6 ? zb.L("鏃╀笂濂�") : a >= 6 && a < 12 ? zb.L("涓婂崍濂�") : a >= 12 && a < 13 ? zb.L("涓崍濂�") : a >= 13 && a < 18 ? zb.L("涓嬪崍濂�") : zb.L("鏅氫笂濂�")
        }, firstTitle: "", lastP: [0, 0], upOrDown: function (a, e, d, c) {
            if (a != 0) {
                return
            }
            if (this.lastP[e] == 0) {
                this.lastP[e] = d[0]
            } else {
                if (d[0] > this.lastP[e]) {
                    $("#statisticsDiv li." + c).addClass("up").removeClass("down")
                } else {
                    if (d[0] < this.lastP[e]) {
                        $("#statisticsDiv li." + c).addClass("down").removeClass("up")
                    }
                }
                this.lastP[e] = d[0]
            }
        }, ticker: function () {
        }, ticker_deleted: function () {
            var a = this;
            $.getJSON(zb.transDomain + "/line/topall?jsoncallback=?", function (c) {
                $("#statisticsDiv li.btc").each(function () {
                    $(this).find("em").each(function (d) {
                        $(this).text(c[0].btcdefault_hotdata[d]);
                        a.upOrDown(d, 0, c[0].btcdefault_hotdata, "btc")
                    })
                });
                $("#statisticsDiv li.ltc").each(function () {
                    $(this).find("em").each(function (d) {
                        $(this).text(c[0].ltcdefault_hotdata[d]);
                        a.upOrDown(d, 1, c[0].ltcdefault_hotdata, "btc")
                    })
                });
                if ($("#btcMarket").length == 1) {
                    $("#btcMarket .hd b").text(CurrencyFormatted(c[0].btcdefault_hotdata[0]));
                    $("#btcMarket .bd span.m1 b").text(CurrencyFormatted(c[0].btcdefault_hotdata[1]));
                    $("#btcMarket .bd span.m2 b").text(CurrencyFormatted(c[0].btcdefault_hotdata[2]));
                    $("#ltcMarket .hd b").text(CurrencyFormatted(c[0].ltcdefault_hotdata[0]));
                    $("#ltcMarket .bd span.m1 b").text(CurrencyFormatted(c[0].ltcdefault_hotdata[1]));
                    $("#ltcMarket .bd span.m2 b").text(CurrencyFormatted(c[0].ltcdefault_hotdata[2]));
                    $("#btqMarket .hd b").text(c[0].btqdefault_hotdata[0]);
                    $("#btqMarket .bd span.m1 b").text(c[0].btqdefault_hotdata[1]);
                    $("#btqMarket .bd span.m2 b").text(c[0].btqdefault_hotdata[2])
                }
            })
        }, uticker: function () {
            if (this.isLogin()) {
                var a = this;
                $.getJSON(zb.vipDomain + "/user/userticker?callback=?", function (d) {
                    var c = $("#indexUserFunds p span");
                    if (c.length == 4) {
                        c.eq(0).text(d.total)
                    }
                    $("#finaPanelDown1 .rmb .d1 b").html(d.total)
                })
            }
        }, exeTickerJson: function () {
            if (this.tickJson != null) {
                var a = this.tickJson.ticker;
                if (($("#userPayAccountInfo").length > 0) && a != null) {
                    accountInfoShow(a.rmb, a.btcs, a.frmb, a.fbtc, a.buyBtc, a.sellRmb, a.total, a.buy, a.sell, a.btq, a.noBtc, a.nextBtc, a.ltc, a.fltc, a.buyLtc, a.sellLtc, a.nextLtcs)
                }
            }
        }, zcticker: function () {
        }, zcticker_deleted: function () {
            $.getJSON(zb.vipDomain + "/user/zcticker?callback=?", function (c) {
                var a = c.assets;
                $("#finaPanelDown1 .rmb b").each(function (d) {
                    $(this).text(a[d])
                });
                $("#M_userDrop1 .assetNum").each(function (d) {
                    $(this).text(a[d])
                });
                if ($("#myTotalBalance").length > 0) {
                    $("#myTotalBalance").text(a[0])
                }
            })
        }, assets: function () {
            var d = this;
            var a = $("#finaPanelDown1").width();
            var c = parseInt($("#finaPanelDown1").width()) - a
        }, resetUserFunds: function () {
            var a = this;
            $.getJSON(zb.vipDomain + "/u/resetUserFunds?callback=?", function (c) {
                if (c.isSuc) {
                    Right("璧勯噾鍒锋柊鎴愬姛", {callback: "Close()"});
                    a.balance()
                } else {
                    Info(c.des, {callback: "Close()"})
                }
            })
        }, balance: function () {
        }, balance_deleted: function () {
            if (this.isLogin()) {
                $.getJSON(zb.vipDomain + "/u/getBalance?callback=?", function (a) {
                    var g = a.funds;
                    if ($("#finaPanelDown2 b").length > 0) {
                        $("#finaPanelDown2 b").each(function (j) {
                            $(this).text(g[j])
                        });
                        var d = g[15] == 1 ? zb.L("鍊熷叆") : zb.L("鍊熷嚭");
                        $("#finaPanelDown2 .dai").html($("#finaPanelDown2 .dai").html().replaceAll("[XX]", d));
                        $(".etinfo .dai li").css("background", "none")
                    }
                    if ($("#M_userDrop2").length > 0) {
                        $("#M_userDrop2 .assetNum").each(function (j) {
                            if (j < 18) {
                                $(this).text(g[j])
                            }
                        });
                        if (g[9] != 0 || g[10] != 0 || g[11] != 0) {
                            $(".btqDisplay").show()
                        }
                        $("#M_userDrop2 .daiNum").eq(0).text(g[12]);
                        $("#M_userDrop2 .daiNum").eq(1).text(g[13]);
                        $("#M_userDrop2 .daiNum").eq(2).text(g[14]);
                        var d = g[15] == 1 ? zb.L("鍊熷叆") : zb.L("鍊熷嚭");
                        $("#M_userDrop2 .daiTit").html(d);
                        var f = $("#M_userDrop2").find('tr[data-id="ethRow"]');
                        f.find('span[data-id="total"]').text(g[19]);
                        f.find('span[data-id="usable"]').text(g[20])
                    }
                    if ($(".b_tradinfo .bd em").length > 0) {
                        $(".b_tradinfo .bd em").each(function (j) {
                            $(this).text(g[j])
                        });
                        $(".b_tradinfo h3").text(a.funds[16])
                    }
                    var c = $("#indexUserFunds p span");
                    if (c.length == 4) {
                        c.each(function (j) {
                            if (j > 0) {
                                $(this).text(g[3 * (j - 1)])
                            } else {
                                $(this).text(g[16])
                            }
                        })
                    }
                    var e = false;
                    if (g.length >= 18) {
                        e = g[17] > 0
                    }
                    if (g[12] > 0 || g[13] > 0 || g[14] > 0 || e) {
                        $(".p2pLoanOut").show()
                    } else {
                        $(".p2pLoanOut").hide()
                    }
                })
            }
        }, userInfo: {}, login: function (c, f, e) {
            var a = f || true;
            var d = c || "Close";
            Iframe({
                Url: zb.vipDomain + "/user/loginAuthentication?callback=" + d + "&needClose=" + e,
                isShowIframeTitle: a,
                fromObj: T$("login"),
                Width: 550,
                Height: 350,
                scrolling: "no",
                isShowClose: false,
                Title: "鐢ㄦ埛鐧诲綍",
                isShowIframeTitle: true
            })
        }, checkLogin: function (d, f, c) {
            this.loginStatus = $.cookie(zb.cookiKeys.uon) == "1";
            var a = f ? f : true;
            var e = c;
            if (!this.loginStatus) {
                this.login(d, a, e);
                return false
            }
            return true
        }, loginScuess: function (c, a) {
            if (a) {
                Close()
            } else {
                Right(zb.L("鐧婚檰鎴愬姛锛�"))
            }
            $("#userNameCookie").text(c);
            this.loginStatus = true;
            this.init()
        }, showP2p: function () {
            if (this.isLogin()) {
                $.getJSON(zb.vipDomain + "/u/getBalance?callback=?", function (a) {
                    var d = a.funds;
                    var c = false;
                    if (d.length >= 18) {
                        c = d[17] > 0
                    }
                    console.log(c);
                    if (d[12] > 0 || d[13] > 0 || d[14] > 0 || c) {
                        $(".b_usermenu .m3").show();
                        $(".p2pLoanOut").show()
                    } else {
                        $(".b_usermenu .m3").hide();
                        $(".p2pLoanOut").hide()
                    }
                })
            }
        }
    },
    form: {error: ""},
    getLan: function () {
        var a = $.cookie(zb.cookiKeys.lan);
        if (a == "en") {
            return "en"
        }
        return "cn"
    },
    L: function (c, a) {
        try {
            var g = "";
            try {
                if (this.getLan() == "cn") {
                    g = this.tips[c][0]
                } else {
                    g = this.tips[c][1]
                }
            } catch (d) {
                g = c
            }
            if (a && a.length > 0) {
                for (var f in a) {
                    g = g.replace(a[f].k, a[f].v)
                }
            }
            return g
        } catch (d) {
            return c
        }
    },
    addTips: function (a, c) {
        if (this.getLan() == "cn") {
            zb.tips[a] = [c, ""]
        } else {
            zb.tips[a] = ["", c]
        }
    },
    tips: {
        "鐧婚檰鎴愬姛锛�": ["鐧婚檰鎴愬姛锛�", "Login Success!"],
        "鎮ㄧ‘瀹氭敞閿€鐧诲綍涔堬紵": ["鎮ㄧ‘瀹氭敞閿€鐧诲綍涔堬紵", "Are you sure you want to log out!"],
        "璇锋鏌ョ綉缁滐紝鍙兘鏄綉缁滆繃鎱㈠鑷磋秴鏃舵垨鑰呰繙绋嬫湇鍔″嚭鐜版晠闅�!": ["璇锋鏌ョ綉缁滐紝鍙兘鏄綉缁滆繃鎱㈠鑷磋秴鏃舵垨鑰呰繙绋嬫湇鍔″嚭鐜版晠闅�!", "net errors!"],
        "纭畾": ["纭畾", "Ok"],
        "鍙栨秷": ["鍙栨秷", "Cancel"],
        "纭畾鍒犻櫎": ["鎮ㄧ‘瀹氳鍒犻櫎璇ラ」鍚楋紵鍒犻櫎鍚庢棤娉曟仮澶嶏紒", "Are you sure you want to delete that? Deleted can not be recovered!"],
        "纭畾瑕佸彇娑堟湰娆″厖鍊煎悧锛�": ["纭畾瑕佸彇娑堟湰娆″厖鍊煎悧锛�", "Sure you want to cancel the recharge?"],
        "纭畾鍒犻櫎鏈」鍚楋紵": ["纭畾鍒犻櫎鏈」鍚楋紵", "make sure delete this items?"],
        "纭畾鍙栨秷鍚楋紵": ["纭畾鍙栨秷鍚楋紵", "make sure cancle?"],
        "鐢ㄦ埛鐧诲綍": ["鐢ㄦ埛鐧诲綍", "User Login"],
        "鐧诲綍": ["鐧诲綍", "Login"],
        "娉ㄥ唽": ["娉ㄥ唽", "Sign up"],
        "鏀朵欢绠�": ["鏀朵欢绠�", "Inbox"],
        "鏈閭欢": ["鏈閭欢", ""],
        "璇烽€夋嫨涓€椤�": ["璇烽€夋嫨涓€椤�", "Please select a letter to be deleted!"],
        "纭畾瑕佸垹闄ら€変腑鐨勯」鍚楋紵": ["纭畾瑕佸垹闄ら€変腑鐨勯」鍚楋紵", "Are you sure you want to delete the selected messages?"],
        "鍗曚环": ["鍗曚环", "Unit Price"],
        "鍊熷叆": ["鍊熷叆", "Borrow "],
        "鍊熷嚭": ["鍊熷嚭", "Lend "],
        "鏃╀笂濂�": ["鏃╀笂濂�", "Morning"],
        "涓婂崍濂�": ["涓婂崍濂�", "Morning"],
        "涓崍濂�": ["涓崍濂�", "Afternoon "],
        "涓嬪崍濂�": ["涓嬪崍濂�", "Afternoon "],
        "鏅氫笂濂�": ["鏅氫笂濂�", "Evening "],
        "涔板叆": ["涔板叆", "Buy "],
        "鍗栧嚭": ["鍗栧嚭", "Sell "],
        "寮€鍚�": ["寮€鍚�", "Open"],
        "鍏抽棴": ["鍏抽棴", "Close"],
        "鏄剧ず": ["鏄剧ず", "show"],
        "绉掓洿鏂�": ["绉掓洿鏂�", "s ref"],
        "鏁版嵁姝ｅ父鏇存柊閫熷害鍔犲揩涓簒1绉�,鍙兘闇€瑕佸嚑绉掗挓鐢熸晥锛�": ["鏁版嵁姝ｅ父鏇存柊閫熷害鍔犲揩涓簒1绉�,鍙兘闇€瑕佸嚑绉掗挓鐢熸晥锛�", " Normal data update speed for x1 seconds, may take a few seconds to take effect!"],
        "宸茬粡鏄渶蹇殑鏇存柊閫熷害浜�": ["宸茬粡鏄渶蹇殑鏇存柊閫熷害浜�", "It was already the fastest speed"],
        "鏁版嵁姝ｅ父鏇存柊閫熷害鍑忔參涓簒1绉�,鍙兘闇€瑕佸嚑绉掗挓鐢熸晥锛�": ["鏁版嵁姝ｅ父鏇存柊閫熷害鍑忔參涓簒1绉�,鍙兘闇€瑕佸嚑绉掗挓鐢熸晥锛�", "Normal data update at a slower pace for x1 seconds, may take a few seconds to take effect!"],
        "涓嶆敮鎸佹洿鎱㈢殑鏇存柊閫熷害": ["涓嶆敮鎸佹洿鎱㈢殑鏇存柊閫熷害", "Does not support the renewal speed of slower"],
        "宸茬粡鏄渶澶氱殑妗ｄ綅鏄剧ず浜�": ["宸茬粡鏄渶澶氱殑妗ｄ綅鏄剧ず浜�", "Is one of the most gear shows"],
        "鏌ョ湅": ["鏌ョ湅", "view"],
        "妗�": ["妗�", " gear"],
        "鏁版嵁鏄剧ず妗ｄ綅璋冩暣涓簒1妗�,鍙兘闇€瑕佸嚑绉掗挓鐢熸晥锛�": ["鏁版嵁鏄剧ず妗ｄ綅璋冩暣涓簒1妗�,鍙兘闇€瑕佸嚑绉掗挓鐢熸晥锛�", "According to gear is adjusted for x1, may take a few seconds to take effect! "],
        "宸茬粡鏄渶灏戠殑妗ｄ綅鏄剧ず浜�": ["宸茬粡鏄渶灏戠殑妗ｄ綅鏄剧ず浜�", "Is the least gears shows"],
        "涔�": ["涔�", "bid "],
        "鍗�": ["鍗�", "ask "],
        "妗ｄ綅": ["妗ｄ綅", "gear"],
        "绯荤粺蹇欑锛岃绋嶅€欙紒": ["绯荤粺蹇欑锛岃绋嶅€欙紒", "System is busy, please wait!"],
        "寰呮垚浜�": ["寰呮垚浜�", "Wating"],
        "宸叉垚浜�": ["宸叉垚浜�", "has"],
        "宸插彇娑�": ["宸插彇娑�", "canceled"],
        "璁″垝涓�": ["璁″垝涓�", "in plan"],
        "涓撲笟鐗堢敵璇锋湭閫氳繃瀹℃牳锛�": ["涓撲笟鐗堢敵璇锋湭閫氳繃瀹℃牳锛�", "Professional Edition application is not approved,"],
        "璐靛鐗堢敵璇锋湭閫氳繃瀹℃牳锛�": ["璐靛鐗堢敵璇锋湭閫氳繃瀹℃牳锛�", "VIP version of the application is not approved,"],
        "鏌ョ湅鍘熷洜": ["鏌ョ湅鍘熷洜", "To see why."],
        "鐢宠涓撲笟鐗�": ["鐢宠涓撲笟鐗�", "Application VIP Edition"],
        "鐢宠璐靛鐗�": ["鐢宠璐靛鐗�", "Application VIP Edition"],
        "鎮ㄦ彁浜ょ殑涓撲笟鐗堢敵璇锋鍦ㄥ鏍镐腑锛岃鑰愬績绛夊緟銆�": ["鎮ㄦ彁浜ょ殑涓撲笟鐗堢敵璇锋鍦ㄥ鏍镐腑锛岃鑰愬績绛夊緟銆�", "Professional Edition application is being reviewed your submission, please be patient."],
        "涓撲笟鐗堢敵璇蜂腑": ["涓撲笟鐗堢敵璇蜂腑", "Professional Edition application"],
        "鎮ㄦ彁浜ょ殑璐靛鐗堢敵璇锋鍦ㄥ鏍镐腑锛岃鑰愬績绛夊緟銆�": ["鎮ㄦ彁浜ょ殑璐靛鐗堢敵璇锋鍦ㄥ鏍镐腑锛岃鑰愬績绛夊緟銆�", "VIP version of the application is being reviewed your submission, please be patient."],
        "璐靛鐗堢敵璇蜂腑": ["璐靛鐗堢敵璇蜂腑", "VIP Edition application."],
        "鎮ㄧ‘瀹氳鐢宠鎴愪负涓撲笟鐗堢敤鎴峰悧锛岀敵璇烽€氳繃鍚庡皢涓嶈兘浣跨敤澶т紬鐗堬紵": ["鎮ㄧ‘瀹氳鐢宠鎴愪负涓撲笟鐗堢敤鎴峰悧锛岀敵璇烽€氳繃鍚庡皢涓嶈兘浣跨敤澶т紬鐗堬紵", "Are you sure you want to apply to become a Pro user yet, will not be used by the public version of the application?"],
        "鎮ㄧ‘瀹氳鎴愪负璐靛鐗堢敤鎴峰悧锛�": ["鎮ㄧ‘瀹氳鎴愪负璐靛鐗堢敤鎴峰悧锛�", "Are you sure you want to become a VIP version of the user?"],
        "鎮ㄧ‘瀹氳寮€鍚畨鍏ㄥ瘑鐮佸悧锛�": ["鎮ㄧ‘瀹氳寮€鍚畨鍏ㄥ瘑鐮佸悧锛�", "Are you sure you want to open the security code?"],
        "璇锋纭緭鍏ユ偍鐨勫畨鍏ㄥ瘑鐮�,6-16浣嶃€�": ["璇锋纭緭鍏ユ偍鐨勫畨鍏ㄥ瘑鐮�,6-16浣嶃€�", "Please enter your security code correctly, 6 to 16. "],
        "姝ｅ湪涔板叆": ["姝ｅ湪涔板叆", "Buying"],
        "姝ｅ湪鍗栧嚭": ["姝ｅ湪鍗栧嚭", "Being sold"],
        "鏆傛椂娌℃湁绗﹀悎瑕佹眰鐨勮褰�": ["鏆傛椂娌℃湁绗﹀悎瑕佹眰鐨勮褰�", "Temporarily not conform to the requirements of the record"],
        "璇疯緭鍏1 X2鐨勫崟浠�": ["璇疯緭鍏1 X2鐨勫崟浠�", "Please enter the price of X1 X2"],
        "璇疯緭鍏1 X2鐨勬暟閲�": ["璇疯緭鍏1 X2鐨勬暟閲�", "Please enter the number of X1 X2"],
        "鎴愬姛鍙栨秷浜哫1涓鎵樿鍗曪紒": ["鎴愬姛鍙栨秷浜哫1涓鎵樿鍗曪紒", "Success is cancelled X1 commissioned orders! "],
        "鎮ㄧ殑X1浣欓锛�": ["鎮ㄧ殑X1浣欓锛�", "Your X1 balance:"],
        "X1浣欓锛歑2锛屾渶澶氬彲濮旀墭锛�": ["X1浣欓锛歑2锛屾渶澶氬彲濮旀墭锛�", "X1 Balance: X2, up commission:"],
        "鎴愬姛鎵归噺濮旀墭X1绗旓紝涓€鍏盭2涓紝鎬婚噾棰�:": ["鎴愬姛鎵归噺濮旀墭X1绗旓紝涓€鍏盭2涓紝鎬婚噾棰�:", "Successful batch entrust X1, total X2 amount:"],
        "鍦板潃宸茶澶嶅埗鍒板壀璐存澘锛岃鏍稿锛�": ["鍦板潃宸茶澶嶅埗鍒板壀璐存澘锛岃鏍稿锛�", "Address has been copied to the clipboard, please check it: "],
        "ZB鎻愮ず鎮�": ["ZB鎻愮ず鎮�", "ZB prompts you"],
        "璇疯緭鍏ユ墜鏈哄彿鐮併€�": ["璇疯緭鍏ユ墜鏈哄彿鐮併€�", "Please enter the phone number."],
        "璇疯緭鍏ユ偍鐨勫畨鍏ㄥ瘑鐮併€�": ["璇疯緭鍏ユ偍鐨勫畨鍏ㄥ瘑鐮併€�", "Please enter your security code."],
        "鑾峰彇楠岃瘉鐮�": ["鑾峰彇楠岃瘉鐮�", "Get verification code"],
        "x1绉掑悗閲嶆柊鑾峰彇": ["x1绉掑悗閲嶆柊鑾峰彇", "To obtain the x1 seconds"],
        "閲嶆柊鑾峰彇璇煶楠岃瘉鐮�": ["閲嶆柊鑾峰彇璇煶楠岃瘉鐮�", "Retrieve audio captcha"],
        "x1绉掑悗閲嶆柊鑾峰彇璇煶楠岃瘉鐮�": ["x1绉掑悗閲嶆柊鑾峰彇璇煶楠岃瘉鐮�", "x1 seconds to get voice verification code"],
        "鎻愪氦鎴愬姛": ["鎻愪氦鎴愬姛", "Submitted successfully"],
        "璇烽€夋嫨瑕佸垹闄ょ殑姣旂壒甯佸湴鍧€": ["璇烽€夋嫨瑕佸垹闄ょ殑姣旂壒甯佸湴鍧€", "Please select the currency you want to remove bits of address"],
        "鍒犻櫎姣旂壒甯佸湴鍧€": ["鍒犻櫎姣旂壒甯佸湴鍧€", "Delete bitcoin address"],
        "璇︾粏璁板綍": ["璇︾粏璁板綍", "All"],
        "": ["", ""],
        "": ["", ""],
        "": ["", ""],
        "": ["", ""],
        "": ["", ""],
        "": ["", ""],
        "": ["", ""],
        "": ["", ""],
        "": ["", ""],
        "": ["", ""]
    },
    setLan: function (a) {
        $.getJSON(zb.mainDomain + "/setlan?callback=?&lan=" + a, function (c) {
            if (c.isSuc) {
                location.reload()
            }
        })
    }
};
zb.list = {
    basePath: "", funcName: "", isInit: false, ui: function (c) {
        if (!c) {
            c = {}
        }
        var d = c.formId || "searchContaint";
        var a = $("#" + d);
        if (a.length > 0) {
            a.Ui()
        }
        this.pageInit();
        this.tabInit()
    }, tabInit: function () {
        var a = zb.tool.param("tab");
        if (a != null) {
            this.dealTab(a)
        }
    }, aoru: function (f) {
        if (!f) {
            f = {}
        }
        if (!f.id) {
            Wrong("ID鍙傛暟蹇呴』浼犻€掞紒")
        }
        var g = f.id, c = f.width || 560, e = f.height || 360, d = f.title || "娣诲姞/缂栬緫" + this.funcName,
            a = f.scroll || "auto";
        otherParam = f.otherParam || "", _this = this, url = f.url || _this.basePath + "aoru?id=" + g + otherParam;
        Iframe({Url: url, Width: c, Height: e, scrolling: a, Title: d})
    }, del: function (a) {
        if (!a) {
            a = {}
        }
        if (!a.id) {
            Wrong("ID鍙傛暟蹇呴』浼犻€掞紒")
        }
        var d = a.id, c = this;
        Ask2({
            Title: zb.L("纭畾鍒犻櫎鏈」鍚楋紵"), call: function () {
                zb.ajax({
                    url: c.basePath + "doDel?id=" + d, suc: function (e) {
                        c.reload();
                        Right($(e).find("Des").text())
                    }
                })
            }
        })
    }, dealTab: function (a) {
        $("#" + a).parent("div").find("a").removeClass("current");
        $("#" + a).parent("li").parent("ul").find("li a").removeClass("current");
        $("#" + a).addClass("current")
    }, search: function (c) {
        if (!c) {
            c = {}
        }
        var q = c.formId || "searchContaint", a = c.div || this.defaultDiv, g = c.page || 1, e = "",
            i = c.special || false, n = c.validate || false, d = c.tab, f = this;
        var r = true;
        if (c.needLoading == undefined) {
            r = true
        } else {
            r = c.needLoading == false ? false : true
        }
        if ($("#" + q).length > 0) {
            if (d) {
                var k = $("#" + q).find("input[name='tab']");
                if (k.length > 0) {
                    k.val(d)
                } else {
                    $("<input name='tab' value='" + d + "' type='hidden'>").appendTo($("#" + q))
                }
                this.dealTab(d)
            }
            e = FormToStr(q);
            if (n) {
                if (e == null) {
                    return
                }
            } else {
                e = e == null ? "" : e
            }
            e = "?" + e + "&page=" + g
        }
        var j = "ajax" + e;
        zb.ajax({
            url: f.basePath + j, needLoading: r, suc: function (u) {
                if (i) {
                    $("#" + a).html(u)
                } else {
                    $("#" + a).html(u)
                }
                f.pageInit()
            }, dataType: "text", div: a
        })
    }, defaultDiv: "shopslist", ajaxPage: function (d, f) {
        if (d.needInit && !this.isInit) {
            return
        }
        if (!d) {
            d = {}
        }
        if (!d.url) {
            Wrong("url鍙傛暟蹇呴』浼犻€掞紒")
        }
        var a = d.url, j = d.div || this.defaultDiv, i = d.timeout || 60000, g = this;
        var c = true;
        if (d.needLoading == undefined) {
            c = true
        } else {
            c = d.needLoading == false ? false : true
        }
        zb.ajax({
            url: a, needLoading: c, suc: function (k) {
                var e = k != $("#" + j).html();
                if (e) {
                    $("#" + j).html(k)
                }
                if (d.needInit) {
                    g.pageInit()
                }
                if (typeof d.suc == "function") {
                    (function () {
                        d.suc(e)
                    }())
                }
            }, dataType: "text", div: j, timeout: i
        });
        if (f) {
            if (f && f.preventDefault) {
                f.preventDefault()
            } else {
                window.event.returnValue = false
            }
            return false
        }
    }, jumpPage: function (d) {
        var c = $("#PagerInput").val();
        if (c > d) {
            Wrong("鎮ㄨ緭鍏ョ殑椤电爜杩囧ぇ锛岃閲嶆柊杈撳叆銆�", {CloseTime: 1});
            return
        }
        if (c < 1) {
            if (c == "") {
                Info("璇疯緭鍏ラ〉鐮�", {CloseTime: 1})
            } else {
                Wrong("鎮ㄨ緭鍏ョ殑椤电爜澶皬浜嗭紝璇烽噸鏂拌緭鍏ャ€�", {CloseTime: 1})
            }
            return
        } else {
            var a = /^[0-9]+.?[0-9]*$/;
            if (a.test(c)) {
                this.search({page: c})
            } else {
                Wrong("鎮ㄨ緭鍏ヤ笉鏄湁鏁堥〉鐮侊紝璇烽噸鏂拌緭鍏ャ€�", {CloseTime: 1})
            }
        }
    }, resetForm: function (a) {
        if (!a) {
            a = {}
        }
        var c = "", d = this;
        c = a.formId || "searchContaint";
        $("#" + c).each(function () {
            this.reset();
            d.search()
        })
    }, config: function () {
        $(".item_list_bd").each(function (a) {
            $(this).mouseover(function () {
                $(this).css("background", "#fff8e1")
            }).mouseout(function () {
                $(this).css("background", "#ffffff")
            })
        })
    }, reload: function (a) {
        if (!a) {
            a = {}
        }
        a.page = $("#PagerInput").val() || 1;
        this.search(a)
    }, look: function (f) {
        if (!f) {
            f = {}
        }
        if (!f.url) {
            Wrong("url鍙傛暟蹇呴』浼犻€掞紒")
        }
        var d = f.url, a = f.width || 820, c = f.height || 716,
            g = f.isShowIframeTile == undefined ? true : f.isShowIframeTile, i = f.scrolling || "auto",
            e = f.needLogin || false, j = this;
        if (e && !zb.user.checkLogin()) {
            return
        }
        Iframe({Url: d, Width: a, Height: c, scrolling: i, isShowIframeTitle: g, Title: "鏌ョ湅" + j.funcName})
    }, noPass: function (a) {
        Iframe({
            Url: "/admin/user/reason?id=" + a,
            zoomSpeedIn: 200,
            zoomSpeedOut: 200,
            Width: 600,
            Height: 460,
            Title: "濉啓涓嶉€氳繃鐨勫師鍥�"
        })
    }, url: function (c) {
        if (!c) {
            c = {}
        }
        if (!c.url) {
            Wrong("url鍙傛暟蹇呴』浼犻€掞紒")
        }
        var a = c.url;
        zb.ajax({url: a})
    }, pageInit: function () {
        $("#ListTable").FixedHeader();
        $("#pagin a,#page_navA a").each(function () {
            $(this).AStop()
        });
        $("#PagerInput").UiText();
        this.isInit = true
    }, reloadAsk: function (a) {
        if (!a) {
            a = {}
        }
        if (!a.title) {
            Wrong("璇蜂紶閫掓偍鐨勮闂爣棰橈紒");
            return
        }
        if (!a.url) {
            Wrong("璇蜂紶閫掓偍鐨勫悗鍙皍rl锛�");
            return
        }
        var c = this;
        Ask2({
            Msg: a.title, call: function () {
                zb.ajax({
                    url: a.url, suc: function (d) {
                        Right($(d).find("Des").text(), {
                            call: function () {
                                c.reload();
                                Close()
                            }
                        })
                    }, err: function (d) {
                        Wrong($(d).find("Des").text())
                    }
                })
            }
        })
    }
};

function CurrencyFormatted(c) {
    var a = parseFloat(c);
    if (isNaN(a)) {
        a = 0
    }
    var d = "";
    if (a < 0) {
        d = "-"
    }
    a = Math.abs(a);
    a = parseInt((a + 0.005) * 100);
    a = a / 100;
    s = new String(a);
    if (s.indexOf(".") < 0) {
        s += ".00"
    }
    if (s.indexOf(".") == (s.length - 2)) {
        s += "0"
    }
    s = d + s;
    return s
}

Date.prototype.format = function (a) {
    var d = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        S: this.getMilliseconds()
    };
    if (/(y+)/.test(a)) {
        a = a.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var c in d) {
            if (new RegExp("(" + c + ")").test(a)) {
                a = a.replace(RegExp.$1, (RegExp.$1.length == 1) ? (d[c]) : (("00" + d[c]).substr(("" + d[c]).length)))
            }
        }
    }
    return a
};
String.prototype.format = function (c) {
    var a = this;
    if (arguments.length > 0) {
        if (arguments.length == 1 && typeof(c) == "object") {
            for (var e in c) {
                if (c[e] != undefined) {
                    var f = new RegExp("({" + e + "})", "g");
                    a = a.replace(f, c[e])
                }
            }
        } else {
            for (var d = 0; d < arguments.length; d++) {
                if (arguments[d] != undefined) {
                    var f = new RegExp("({[" + d + "]})", "g");
                    a = a.replace(f, arguments[d])
                }
            }
        }
    }
    return a
};
var webSocket = {};
var ajaxRun = true;
webSocket.socket = null;
var t1 = new Date().getTime();
var wsNum = 0;
var isWatching = false;
webSocket.init = function (g) {
    if ($("#M_userName").length < 1) {
        try {
            if (isWSPushData == undefined || !isWSPushData) {
                return
            }
        } catch (f) {
            return
        }
    }
    var d = this;
    wsNum++;
    if ((d.socket == null || (d.socket && d.socket.readyState != WebSocket.OPEN)) && wsNum == 1) {
        d.socketConnection()
    }
    if (!d.socket) {
        return
    }
    d.socket.onclose = function () {
        ajaxRun = true;
        console.info("socket close");
        setTimeout(function () {
            if (wsNum > 0) {
                wsNum--
            }
        }, 1000);
        var e = new Date().getTime()
    };
    d.socket.onerror = function () {
        ajaxRun = true;
        console.info("socket error");
        setTimeout(function () {
            if (wsNum > 0) {
                wsNum--
            }
        }, 1000);
        var e = new Date().getTime()
    };
    var c = 0;
    if ($.isFunction(g)) {
        var a = setInterval(function () {
            c += 20;
            if (c > 10000) {
                clearInterval(a)
            }
            if (d.socket.readyState == WebSocket.OPEN) {
                g();
                clearInterval(a)
            }
        }, 20);
        if (!isWatching) {
            webSocket.watching(g)
        }
    }
};
webSocket.watching = function (a) {
    setInterval(function () {
        if (ajaxRun) {
            if (wsNum > 1) {
                wsNum = 0;
                webSocket.socket = null
            }
            if ($.isFunction(a)) {
                webSocket.init(a)
            }
        }
        isWatching = true
    }, 30000)
};
webSocket.socketConnection = function () {
    var $this = this;
    var targetUrl = "wss://kline.zb.com:2443/websocket";
    if (typeof ZNAME != "undefined") {
        if (ZNAME == "z") {
            targetUrl = "wss://kline.zb.com:2443/websocket"
        } else {
            if (ZNAME == "t") {
                targetUrl = "ws://tkline.zb.com:8580/websocket"
            } else {
                if (ZNAME == "t2") {
                    targetUrl = "ws://tkline2.zb.com:8581/websocket"
                } else {
                    if (ZNAME == "w") {
                        targetUrl = "ws://wkline.zb.com:8580/websocket"
                    } else {
                        if (ZNAME == "w2") {
                            targetUrl = "ws://wkline2.zb.com:8581/websocket"
                        }
                    }
                }
            }
        }
    } else {
        return
    }
    if (!window.WebSocket) {
        window.WebSocket = window.MozWebSocket
    }
    if (window.WebSocket) {
        $this.socket = new WebSocket(targetUrl)
    } else {
        ajaxRun = true;
        return
    }
    $this.socket.onopen = function () {
        ajaxRun = false;
        wsNum--;
        var t2 = new Date().getTime()
    };
    var ichecked = 0;
    if (!!$this.socket) {
        $this.socket.onmessage = function (result) {
            var json = null;
            if (result.data instanceof Blob) {
                var blob = result.data;
                var reader = new FileReader();
                reader.readAsText(blob);
                reader.onload = function (evt) {
                    if (evt.target.readyState == FileReader.DONE) {
                        var before = evt.target.result.length;
                        ungzip(evt.target.result, function (result) {
                            if (ichecked < 10) {
                                ichecked++
                            }
                            if (result.indexOf("(") != 0) {
                                json = eval("(" + result + ")")
                            } else {
                                json = eval(result)
                            }
                            $this.dealMessage(json)
                        })
                    }
                }
            } else {
                ungzip(result.data, function (result) {
                    if (result.indexOf("(") != 0) {
                        json = eval("(" + result + ")")
                    } else {
                        json = eval(result)
                    }
                    $this.dealMessage(json)
                })
            }
        }
    }
};
webSocket.sendMessage = function (c) {
    var e = this;
    var a = 0;
    var d = setInterval(function () {
        a += 50;
        if (a >= 10000) {
            clearInterval(d)
        }
        if (e.socket.readyState == WebSocket.OPEN) {
            e.socket.send(c);
            clearInterval(d)
        }
    }, 50)
};
webSocket.dealMessage = function (a) {
    var c = a.channel;
    if (!c) {
        c = a[0].channel
    }
    seajs.use(["module_market", "module_asset", "module_user", "module_trans"], function (j, f, d, e) {
        if (c == "top_all") {
            j.getMarketSocket(a)
        } else {
            if (c == "pushLoginData") {
                d.qrcodeLoginBySocket(a)
            } else {
                if (c == "push_user_asset") {
                    f.getUserAssetBySocket(a)
                } else {
                    if (c.indexOf("dish_depth_") >= 0 || c.indexOf("dish_length_") >= 0) {
                        e.setDish(a)
                    } else {
                        if (c == "push_user_record") {
                            e.setEntrustRecordSocket(a)
                        } else {
                            if (c.indexOf("_kline_") > 0) {
                                var i = document.getElementById("marketFrame");
                                var g = i.window || i.contentWindow;
                                g.updateKlineData(a)
                            } else {
                                if (c.endWith("_lasttrades") || c.endWith("_depth")) {
                                    var i = document.getElementById("marketFrame");
                                    var g = i.window || i.contentWindow;
                                    if (c.endWith("_lasttrades")) {
                                        g.kline.pushTrades(a.data);
                                        g.kline.klineTradeInit = true;
                                        g.clear_refresh_counter()
                                    }
                                    if (c.endWith("_depth")) {
                                        g.kline.updateDepth(a)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    })
};
webSocket.init();
window.onbeforeunload = function () {
    if (webSocket.socket) {
        return webSocket.socket.close()
    }
};
String.prototype.startWith = function (a) {
    if (a == null || a == "" || this.length == 0 || a.length > this.length) {
        return false
    }
    if (this.substr(0, a.length) == a) {
        return true
    } else {
        return false
    }
    return true
};
String.prototype.endWith = function (a) {
    if (a == null || a == "" || this.length == 0 || a.length > this.length) {
        return false
    }
    if (this.substring(this.length - a.length) == a) {
        return true
    } else {
        return false
    }
    return true
};

function ungzip(c, d) {
    var a = "";
    seajs.use(["module_pako"], function (i) {
        if (c.indexOf("channel") >= 0) {
            a = c
        } else {
            try {
                var n = atob(c);
                var g = n.split("").map(function (e) {
                    return e.charCodeAt(0)
                });
                var f = new Uint8Array(g);
                var j = i.inflate(f);
                a = String.fromCharCode.apply(null, new Uint16Array(j))
            } catch (k) {
                a = c
            }
        }
        if ($.isFunction(d)) {
            d(a)
        }
    })
}

function isZipData() {
    var a = true;
    var c = window.navigator.userAgent.toLowerCase();
    if (c.indexOf("trident") >= 0 || c.indexOf("msie") >= 0) {
        console.info("浣跨敤IE鍐呮牳妯″紡");
        a = false
    } else {
        if (JuaBox.isMobile()) {
            a = false
        }
    }
    return a
};