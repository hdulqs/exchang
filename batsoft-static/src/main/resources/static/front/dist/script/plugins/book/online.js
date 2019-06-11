if (top == window && navigator.onLine) {
    var userInfo = Q.userInfo, hmsr = Q.getURI("hmsr");
    hmsr && Q.cookie("c_csc", hmsr);
    Q.setLoginInfo = function (a) {
        Q.isLogin() ? (Q.jsonp("http://userapi.17k.com/user/getUserInfo.action?size=50&userId=" + userInfo.userId, function (b) {
                a.innerHTML = '<img src="' + b.headImgUrl + '">';
                a.style.boxShadow = "0 0 3px rgba(0, 0, 0, .5)"
            }), a.href = "http://h5.17k.com/h5/user/info.action") : a.onclick = function () {
                Q.login()
            }
    };
    window.addEventListener("DOMContentLoaded", function () {
        (new Image).src = "http://api.share.baidu.com/s.gif?l=" +
            encodeURIComponent(location.href) + (document.referrer ? "&r=" + encodeURIComponent(document.referrer) : "");
        var a = Q.cookie("TOP_DON_COOKIE");
        if (!a) {
            var b = document.createElement("style");
            b.setAttribute("text", "text/css");
            b.innerText = ".all_don,.btn_don,.mid_don,.top_don{-webkit-transition: all .3s ease;transition: all .3s ease;position:fixed;max-width:600px;font-size:0;z-index:5}.all_don img,.btn_don img,.mid_don img,.top_don img{width:100%}.all_don span,.btn_don span,.mid_don span,.top_don span{display:inline-block;position:absolute;bottom:0;left:0}.all_don a,.btn_don a,.mid_don a,.top_don a{display:inline-block}.all_don{position:fixed;z-index:300;top:0;right:0;left:0;bottom:0}.all_don img{height:100%}.all_don span{width:48.5%;height:8.8%;left:26%;top:29.5%}.all_don a{position:absolute;width:48.5%;height:8.8%;left:26%;top:20%}.top_don{position:relative;z-index:6}.top_don span{z-index:6;width:11.5%;height:100%}.top_don a img{width:100%}.btn_don{bottom:0}.btn_don span{width:11.5%;height:100%}.mid_don{bottom:-25%;overflow:hidden;}.mid_don span{background: url(http://img.17k.com/subject/wuyuepay/skin/close.png) center center no-repeat;width: 10%;height: 25%;position: absolute;top: 0;left: 90%;z-index:3;}.mid_don a{width: 100%;height: 100%;position: absolute;}.book_home_float {position: fixed;top: 17.5em;z-index: 1;right: 3px;width: 5em;}.book_home_float a{display:block;width: 5em;height: 5.1em;background: url(http://img.17k.com/subject/scfq/skin/book_home_float.png) center center no-repeat;background-size: 100%;}";
            document.head.appendChild(b)
        }
        if (!a && (GetUrlRelativePath("/") || GetUrlRelativePath("/topv3") || GetUrlRelativePath("/list") || GetUrlRelativePath("/chapter/")) && -1 == Q.cookie("c_csc").indexOf("smss")) {
            a = '<span class="top_don_span" onclick="deldon(this)"></span><a onclick="anorios(this)"  href="http://h5.17k.com/download/index.html?hmsr=shufuceng500"><img src="http://img.17k.com/subject/scfq/skin/top_don_bg.jpg" alt=""></a>';
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
        GetUrlRelativePath("/book/") && createfirstChild('<a href="http://h5.17k.com/inc/yindao02.html" target="_blank"></a>',
            "book_home_float")
    });
    var GetUrlRelativePath = function (a) {
        return "/book/" == a ? /^(http:\/\/h5.17k.com\/book\/)(\d+)(\.html)([^\/]*$)/.test(location.href) : "/" == a ? /^(http:\/\/h5.17k.com\/$)/.test(location.href) : 0 <= location.pathname.indexOf(a)
    }, createfirstChild = function (a, b) {
        var c = document.createElement("div");
        c.className = b;
        c.innerHTML = a;
        document.body.insertBefore(c, document.body.firstChild)
    }, weixinTip = function (a) {
        a.href = "http://h5.17k.com/download/index.html";
        window.event ? window.event.returnValue = !1 : a.preventDefault();
        createfirstChild(' <div class="wxtip" id="JweixinTip"><img style="width:100%" src="http://img.17k.com/touch/skin/download/weixinTip_yinliu.jpg" /></div>');
        a = document.getElementById("JweixinTip");
        a.style.cssText = "z-index:999;background:rgba(255,255,255,.9);text-align:center;position:fixed;left:0;top:0;width:100%;height:100%;z-index:100;display:none";
        a.style.display = "block";
        a.onclick = function () {
            this.style.display = "none"
        }
    }, anorios = function (a) {
        var b = navigator.userAgent, c = !!b.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
            d = -1 < b.indexOf("Android") || -1 < b.indexOf("Adr"), b = !!/MicroMessenger/i.test(b);
        (c || d) && c && b && weixinTip(a)
    }, deldon = function (a) {
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
