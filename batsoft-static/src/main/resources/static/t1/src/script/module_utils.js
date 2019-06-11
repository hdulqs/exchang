define(function (require, exports, module) {

    module.exports = {
        setLang:function(lang) {
        setCookie("lang", lang);
        location.reload();
    },

     setCookie:function(name, value) {
        var Days = 30;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
        document.cookie = name + "=" + escape(value) + ";path=/;expires=" + exp.toGMTString();
    },

     getCookie:function(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg)) {
            console.log(arr[2]);
            return unescape(arr[2]);
        } else {
            return null;
        }
    },

     delCookie:function(name) {
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval = getCookie(name);
        if (cval != null) {
            document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
        }
    },

     isEmail:function(str) {
        var reg = /\w[-\w.+]*[A-Za-z0-9]@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/;
        return reg.test(str);
    },
        //大写 +字母+数字
    //  isPass:function(str) {
    //    // var reg = /(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[A-Za-z0-9]{6,20}/;
    //      // var reg = /(?=.*[a-z])(?=.*[0-9])[A-Za-z0-9]{6,20}/;
    //      var reg=  /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_]+$)(?![a-z0-9]+$)(?![a-z\W_]+$)(?![0-9\W_]+$)[a-zA-Z0-9\W_]{6,20}$/
    //     return reg.test(str);
    // },
        isPass:function(str) {
            //大写 +字母+数字
            // var reg = /(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[A-Za-z0-9]{6,20}/;
            var reg = /\d[a-zA-Z]|[a-zA-Z]\d/;
            return reg.test(str);
        },
        isQass:function(str) {
            //大写 +字母+数字
            // var reg = /(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[A-Za-z0-9]{6,20}/;
            var reg = /^\d{6}$/;
            return reg.test(str);
        },


     isEmpty:function(id) {
        if ($(id).val().length == 0) {
            $("#error").show();
            $("#error_show").html(Lang.input_cant_empty);
            $(id).addClass("error");
            return true;
        } else {
            $("#error").hide();
            $(id).removeClass("error");
            return false;
        }
    },

     response:function(data) {

        if (data.msg != undefined && data.msg.length > 0) {
            if (data.code == undefined) {
                data.code = 0;
            }
            var icon = data.success == true ? 6 : 2;
            var time = data.msg.length < 6 ? 2000 : 4000;
            layer.msg(data.msg, {time: time, icon: icon}, function () {
                if(data.code=="401.1"){
                    window.location.href = data['data'];
                }
                if (data['url'] != undefined && data['url'].length > 0) {
                    window.location.href = data['url'];
                }
            });
        } else {
            if (data['url'] != undefined && data['url'].length > 0) {
                window.location.href = data['url'];
            }
        }
    },

     isJson:function(str) {
        try {
            $.parseJSON(str);
        } catch (e) {
            return false;
        }
        return true;
    },

     stopDefault:function(e) {
        if (e && e.preventDefault) {
            e.preventDefault();
        } else {
            window.event.returnValue = false;
        }
    },

     compare_date:function(min_time, max_time) {
        var min_date = new Date(min_time.replace(/-/g, "/"));
        var max_date = new Date(max_time.replace(/-/g, "/"));
        if (min_date > max_date) {
            return -1;
        } else if (min_date == max_date) {
            return 0
        } else if (min_date < max_date) {
            return 1;
        }
    },

     toUtf8:function(str) {
        var out, i, len, c;
        out = "";
        len = str.length;
        for (i = 0; i < len; i++) {
            c = str.charCodeAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                out += str.charAt(i);
            } else if (c > 0x07FF) {
                out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
                out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
                out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
            } else {
                out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
                out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
            }
        }
        return out;
    },

     rtrim_zero:function(number) {
        return Number(number).toString();
    },

     formatcoin:function(number, len) {
        number = number.toString();
        if (len == undefined || len === null) {
            if (number.indexOf('.') == -1) {
                len = 0;
            } else {
                len = number.slice(number.indexOf('.') + 1).length;
            }
        }
        return this.number_format(number, len, ",", 3);
    },

     number_format:function(number, bit, sign, gapnum) {
        number = number || 0;
        bit = !isNaN(bit = Math.abs(bit)) ? bit : 2;
        sign = sign || ",";
        gapnum = gapnum ? gapnum : 3;
        var decimal = ".";
        var negative = number < 0 ? "-" : "";
        var i = parseInt(number = Math.abs(+number || 0).toFixed(bit), 10) + "";
        var j = (j = i.length) > gapnum ? j % gapnum : 0;
        var rule = eval("/(\\d{" + gapnum + "})(?=\\d)/g");
        return negative + (j ? i.substr(0, j) + sign : "") + i.substr(j).replace(rule, "$1" + sign) + (bit ? decimal + Math.abs(number - i).toFixed(bit).slice(2) : "");
    },

     bcadd:function(a, b) {
        if (a == undefined || a == null) {
            a = 0;
        }
        if (b == undefined || b == null) {
            b = 0;
        }
        var number_a = numeral(a);
        var result = number_a.add(b).value();
        return result;
    },

     bcsub:function(a, b) {
        if (a == undefined || a == null) {
            a = 0;
        }
        if (b == undefined || b == null) {
            b = 0;
        }
        var number_a = numeral(a);
        var result = number_a.subtract(b).value();
        return result;
    },

     bcmul:function(a, b) {
        if (a == undefined || a == null) {
            a = 0;
        }
        if (b == undefined || b == null) {
            b = 0;
        }
        var number_a = numeral(a);
        var result = number_a.multiply(b).value();
        return result;
    },

     bcdiv:function(a, b) {
        if (a == undefined || a == null) {
            a = 0;
        }
        if (b == undefined || b == null) {
            return 0;
        }
        var number_a = numeral(a);
        var result = number_a.divide(b).value();
        return result;
    },

     bccomp:function(a, b) {
        a = Number(a);
        b = Number(b);
        if (a > b) {
            return 1;
        } else if (a < b) {
            return -1;
        } else {
            return 0;
        }
    },

     showGoogleBlinding:function() {
        var index = layer.open({
            type: 1,
            title: false,
            shadeClose: true,
            closeBtn: 0,
            scrollbar: false,
            shade: 0.8,
            area: '590px',
            shadeClose: false,
            content: $('#google_blind_pop')
        });
        $("#google_blind_pop .closePopBt,.skipBt").on("click", function () {
            layer.close(index);
        });
    },

     input_number_decimal:function(obj, decimal) {
        var position = $(obj).getCurPos();
        var number_rule = decimal == 0 ? /[^0-9]/g : /[^0-9.]/g;
        var amt = $.trim($(obj).val().replace(number_rule, ''));
        if (amt.indexOf('.') >= 0) {
            var amt_decimal = amt.slice(amt.indexOf('.') + 1).length;
            if (amt_decimal > decimal) {
                amt = amt.substr(0, amt.length - (amt_decimal - decimal));
            }
        }
        $(obj).val(amt).setCurPos(position, position);
    },

     getPercent:function(a) {
        return this.bcmul(a, 100).toFixed(2) + "%";
    },

     getCeil:function(number, double) {
        return parseInt(number * Math.pow(10, double)) / Math.pow(10, double);
    },

     unFormatcoin:function(str) {
        return str.replace(/,/g, "");
    },

     is_null:function(a) {
        if (a == undefined || a == null || a == "") {
            return true;
        }
        return false;
    },




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
        isNotEmpty: function(str){
            if(!str){
                return false;
            }
            if(str.replace(/(^s*)|(s*$)/g, "").length ==0){
                return false;
            }
            if(str==null){
                return false;
            }
            return true;
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
        },
        /**
         * 页面sleep方法
          * @param numberMillis
         */
    sleep : function (numberMillis) {
        var now = new Date();
        var exitTime = now.getTime() + numberMillis;
        while (true) {
            now = new Date();
            if (now.getTime() > exitTime)
                return;
        }
    }
    }

});