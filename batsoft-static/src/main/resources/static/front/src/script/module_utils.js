define(function (require, exports, module) {

    module.exports = {

        /**
         *是否包涵字符串
         * @param str 全字符串
         * @param substr 被包含字符串
         * @returns {boolean}
         */
        strContains: function (str, substr) {
            return new RegExp(substr).test(str);
        },

        /**
         * 全部替换
         * @param str 字符串
         * @param chr 被替换
         * @param newChar 替换后
         */
        replaceAll:function(str,chr,newChar){
            return str.replace(new RegExp(chr,"gm"), newChar);
        },

        /**
         * 字符串截取
         * @param s 被截取字符串
         * @param l 长度 默认 30字节
         * @param r 超出后替换字符
         * @returns {string}
         */
        strSubstring: function (s, l, r) {
            var replace = "";
            var len = l ? l : 30;
            var value = s ? s : '';
            var length = value.length;
            if (length && length > len) {
                length = len;
                replace = r ? r : '...';
            }
            return "<span title ='" + s + "'>" + value.substring(0, length) + replace + "</span>";
        },
        /**
         * 验证码获取倒计时
         * @param countdown
         */
        timer: function (countdown, btn) {
            var _this = btn;
            //设置button效果，开始计时
            _this.attr("disabled", "true");
            _this.html(countdown + "秒后重新获取");
            //启动计时器，1秒执行一次
            var timer = setInterval(function () {
                if (countdown == 0) {
                    clearInterval(timer);//停止计时器
                    _this.removeAttr("disabled");//启用按钮
                    _this.html("重新发送验证码");
                }
                else {
                    countdown--;
                    _this.html(countdown + "秒后重新获取");
                }
            }, 1000);
        },

        /**
         * 日期格式化函数
         * var value="2017-01-01 00:00:00";
         * var date=new Date(value);
         * return dateFormat(date,'yyyy-MM-dd');
         * @param date
         * @param mask
         */
        dateFormat: function (date, format) {
            var d = new Date(date);
            var zeroize = function (value, length) {
                if (!length) length = 2;
                value = String(value);
                for (var i = 0, zeros = ''; i < (length - value.length); i++) {
                    zeros += '0';
                }
                return zeros + value;
            };

            return format.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0) {
                switch ($0) {
                    case 'd':
                        return d.getDate();
                    case 'dd':
                        return zeroize(d.getDate());
                    case 'ddd':
                        return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
                    case 'dddd':
                        return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
                    case 'M':
                        return d.getMonth() + 1;
                    case 'MM':
                        return zeroize(d.getMonth() + 1);
                    case 'MMM':
                        return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
                    case 'MMMM':
                        return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
                    case 'yy':
                        return String(d.getFullYear()).substr(2);
                    case 'yyyy':
                        return d.getFullYear();
                    case 'h':
                        return d.getHours() % 12 || 12;
                    case 'hh':
                        return zeroize(d.getHours() % 12 || 12);
                    case 'H':
                        return d.getHours();
                    case 'HH':
                        return zeroize(d.getHours());
                    case 'm':
                        return d.getMinutes();
                    case 'mm':
                        return zeroize(d.getMinutes());
                    case 's':
                        return d.getSeconds();
                    case 'ss':
                        return zeroize(d.getSeconds());
                    case 'l':
                        return zeroize(d.getMilliseconds(), 3);
                    case 'L':
                        var m = d.getMilliseconds();
                        if (m > 99) m = Math.round(m / 10);
                        return zeroize(m);
                    case 'tt':
                        return d.getHours() < 12 ? 'am' : 'pm';
                    case 'TT':
                        return d.getHours() < 12 ? 'AM' : 'PM';
                    case 'Z':
                        return d.toUTCString().match(/[A-Z]+$/);
                    // Return quoted strings with the surrounding quotes removed
                    default:
                        return $0.substr(1, $0.length - 2);
                }
            });
        },

        /**
         * 获取url参数
         * @param name
         * @param parent 是否查询父窗口 主要用于iframe 页面  default false
         * @returns {null}
         * @constructor
         */
        getUrlParameter: function(name,parent) {
            if (parent===undefined) parent=false;
            var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
            var r;
            if(parent){
                r= window.parent.location.search.substr(1).match(reg);
            }else{
               r = window.location.search.substr(1).match(reg);
            }

            if(r!=null)return  unescape(r[2]); return null;
        },

        /**
         * 获取浏览器信息
         * @returns {{hash: string, host: string, hostname: string, href: string, pathname: string, port: string, protocol: string, search: *}}
         */
        windowsUrlInfo: function () {
            var info = {
                "hash": window.location.hash,
                "host": window.location.host,
                "hostname": window.location.hostname,
                "href": window.location.href,
                "pathname": window.location.pathname,
                "port": window.location.port,
                "protocol": window.location.protocol,
                "search": window.location.search
            }

            return info;
        }
    }

});