/**
 * 特效处理
 */
define(function (require, exports, module) {

    /**
     * 实名认证页面用到
     * @param id
     * @param divId
     * @param className
     * @constructor
     */
    function DivSelect(id, divId, className) {
        this.id = id;
        this.divId = divId;
        this.divClassName = className || 'selectView';
        this.ele = this.$(this.id);
        if (!this.ele) {
            return
        }
        ;var that = this;
        this.status = "close";
        this.parentObj = this.ele.parentNode;
        while (this.readStyle(this.parentObj, "display") != "block") {
            if (this.parentObj.parentNode) {
                this.parentObj = this.parentObj.parentNode
            } else {
                break
            }
        }
        ;this.parentObj.style.position = "relative";
        var sp = this.absPosition(this.ele, this.parentObj);
        this.ele.style.visibility = "hidden";
        this.__div = document.createElement("div");
        if (divId) {
            this.__div.id = divId
        }
        ;
        if (this.divClassName) {
            this.__div.className = this.divClassName
        }
        ;this.parentObj.appendChild(this.__div);
        this.__div.style.width = this.ele.offsetWidth + "px";
        this.__div.style.position = "absolute";
        this.__div.style.left = sp.left + "px";
        this.__div.style.top = sp.top + "px";
        this.__div.onclick = function () {
            that.click()
        };
        this.__div_count = document.createElement("div");
        this.__div.appendChild(this.__div_count);
        this.__div_count.className = "ds_cont";
        this.__div_title = document.createElement("div");
        this.__div_count.appendChild(this.__div_title);
        this.__div_title.className = "ds_title";
        this.__div_button = document.createElement("div");
        this.__div_count.appendChild(this.__div_button);
        this.__div_button.className = "ds_button";
        this.__div_list = document.createElement("div");
        this.__div.appendChild(this.__div_list);
        this.__div_list.className = "ds_list";
        this.__div_list.style.display = "none";
        this.__div_listCont = document.createElement("div");
        this.__div_list.appendChild(this.__div_listCont);
        this.__div_listCont.className = "dsl_cont";
        this.list = [];
        var temp;
        for (var i = 0; i < this.ele.options.length; i++) {
            temp = document.createElement("p");
            this.list.push(temp);
            this.__div_listCont.appendChild(temp);
            temp.innerHTML = this.ele.options[i].innerHTML;
            if (this.ele.selectedIndex == i) {
                this.__div_title.innerHTML = temp.innerHTML
            }
            ;temp.num = i;
            temp.onmouseover = function () {
                that.showSelectIndex(this.num)
            };
            temp.onclick = function () {
                that.select(this.innerHTML)
            }
        }
    };DivSelect.prototype = {
        showSelectIndex: function (num) {
            if (typeof(num) === 'undefined') {
                num = this.ele.selectedIndex
            }
            ;
            if (typeof(this.showIndex) !== 'undefined') {
                this.list[this.showIndex].className = ''
            }
            ;this.showIndex = num;
            this.list[this.showIndex].className = 'selected'
        }, select: function (txt) {
            for (var i = 0; i < this.ele.options.length; i++) {
                if (this.ele.options[i].innerHTML == txt) {
                    this.ele.selectedIndex = i;
                    if (this.ele.onchange) {
                        this.ele.onchange()
                    }
                    ;this.__div_title.innerHTML = txt;
                    break
                }
            }
        }, setIndex: function (num) {
            if (num < 0 || num >= this.list.length) {
                return
            }
            this.ele.selectedIndex = num;
            if (this.ele.onchange) {
                this.ele.onchange()
            }
            ;this.__div_title.innerHTML = this.list[num].innerHTML
        }, clickClose: function (e) {
            var thisObj = e.target ? e.target : event.srcElement;
            var that = this;
            do {
                if (thisObj == that.__div) {
                    return
                }
                ;
                if (thisObj.tagName == "BODY") {
                    break
                }
                ;thisObj = thisObj.parentNode
            } while (thisObj.parentNode);
            that.close()
        }, keyDown: function (e) {
            var num = this.showIndex;
            if (e.keyCode == 38) {
                num--;
                if (num < 0) {
                    num = this.list.length - 1
                }
                ;this.showSelectIndex(num);
                this.stopDefault(e)
            }
            ;
            if (e.keyCode == 40) {
                num++;
                if (num >= this.list.length) {
                    num = 0
                }
                ;this.showSelectIndex(num);
                this.stopDefault(e)
            }
            ;
            if (e.keyCode == 13 || e.keyCode == 9) {
                this.setIndex(num);
                this.stopDefault(e);
                this.close()
            }
            ;
            if (e.keyCode == 27) {
                this.close()
            }
        }, open: function () {
            var that = this;
            this.showSelectIndex();
            this.__div_list.style.display = "block";
            this.status = "open";
            this.__closeFn = function (e) {
                that.clickClose(e)
            };
            this.__keyFn = function (e) {
                that.keyDown(e)
            };
            this.addEvent(document, "click", this.__closeFn);
            this.addEvent(document, "keydown", this.__keyFn)
        }, close: function () {
            var that = this;
            this.__div_list.style.display = "none";
            this.status = "close";
            this.delEvent(document, "click", this.__closeFn);
            this.delEvent(document, "keydown", this.__keyFn)
        }, click: function () {
            if (this.status == "open") {
                this.close()
            } else {
                this.open()
            }
        }, $: function (objName) {
            if (document.getElementById) {
                return eval('document.getElementById("' + objName + '")')
            } else {
                return eval('document.all.' + objName)
            }
        }, addEvent: function (obj, eventType, func) {
            if (obj.attachEvent) {
                obj.attachEvent("on" + eventType, func)
            } else {
                obj.addEventListener(eventType, func, false)
            }
        }, delEvent: function (obj, eventType, func) {
            if (obj.detachEvent) {
                obj.detachEvent("on" + eventType, func)
            } else {
                obj.removeEventListener(eventType, func, false)
            }
        }, readStyle: function (i, I) {
            if (i.style[I]) {
                return i.style[I]
            } else if (i.currentStyle) {
                return i.currentStyle[I]
            } else if (document.defaultView && document.defaultView.getComputedStyle) {
                var l = document.defaultView.getComputedStyle(i, null);
                return l.getPropertyValue(I)
            } else {
                return null
            }
        }, absPosition: function (obj, parentObj) {
            var left = obj.offsetLeft;
            var top = obj.offsetTop;
            var tempObj = obj.offsetParent;
            var sss = "";
            try {
                while (tempObj.id != document.body && tempObj.id != document.documentElement && tempObj != parentObj && tempObj != null) {
                    sss += tempObj.tagName + " , ";
                    tempObj = tempObj.offsetParent;
                    left += tempObj.offsetLeft;
                    top += tempObj.offsetTop
                }
            } catch (e) {
            }
            ;
            return {left: left, top: top}
        }, stopDefault: function (e) {
            if (e.preventDefault) {
                e.preventDefault()
            } else {
                e.returnValue = false
            }
        }
    };
    module.exports = {
        /**
         * div 下拉框
         * @param id
         * @param divId
         * @param className
         */

        divSelect: function (id,divId,className) {

            new DivSelect(id, divId, className);

        },


        /**
         * 圆圈进度条
         */
        piechart: function () {
            /** Easy Pie Chart
             ************************* **/
            var _container = jQuery(".piechart");

            if (_container.length > 0) {

                jQuery(".piechart").each(function () {
                    var _t = jQuery(this),
                        _size = _t.attr('data-size') || 150,
                        _animate = _t.attr('data-animate') || "3000";

                    _t.easyPieChart({
                        size: _size,
                        animate: _animate,
                        scaleColor: false,
                        trackColor: _t.attr('data-trackcolor') || 'rgba(0,0,0,0.04)',
                        lineWidth: _t.attr('data-width') || '2',
                        lineCap: 'square',
                        barColor: _t.attr('data-color') || '#0093BF'
                    });

                    /* fix element positioning */
                    jQuery("*", this).attr('style', "line-height:" + _size + "px !important; height:" + _size + "px !important; width:" + _size + "px !important");

                });

            }

        },

        /**
         * 数字跑马灯效果
         */
        countTo: function () {
            // Count To
            jQuery(".countTo").appear(function () {
                var _t = jQuery(this),
                    _from = _t.attr('data-from') || 0,
                    _speed = _t.attr('data-speed') || 1300,
                    _refreshInterval = _t.attr('data-refreshInterval') || 60;


                _t.countTo({
                    from: parseInt(_from),
                    to: _t.html(),
                    speed: parseInt(_speed),
                    refreshInterval: parseInt(_refreshInterval),
                });

            });
        },

        /** Countdown
         **************************************************************** **/
        countDown: function () {
            var _container = jQuery(".countdown"),
                _container2 = jQuery(".countdown-download");

            if (_container.length > 0 || _container2.length > 0) {

                /** On Page Load **/
                _container.each(function () {
                    var _t = jQuery(this),
                        _date = _t.attr('data-from'),
                        _labels = _t.attr('data-labels');

                    if (_labels) {
                        _labels = _labels.split(",");
                    }

                    if (_date) {
                        var _d = new Date(_date);
                        jQuery(this).countdown({
                            until: new Date(_d),
                            labels: _labels || ["Years", "Months", "Weeks", "Days", "Hours", "Minutes", "Seconds"]
                        });
                    }
                });

                /** Download **/
                _container2.bind("click", function (e) {
                    e.preventDefault();

                    var _t = jQuery(this),
                        cd_container = _t.attr('data-for'),
                        _countdown = jQuery("#" + cd_container + ' span.download-wait>.countdown'),
                        _seconds = parseInt(_t.attr('data-seconds')),
                        _dataURL = _t.attr('href');

                    _t.fadeOut(250, function () {
                        jQuery("#" + cd_container).fadeIn(250, function () {

                            var currentDate = new Date();
                            currentDate.setSeconds(currentDate.getSeconds() + _seconds);

                            _countdown.countdown({
                                until: currentDate,
                                format: 'S',
                                expiryUrl: _dataURL,
                                onExpiry: function () {
                                    jQuery("#" + cd_container + ' span.download-message').removeClass('hide');
                                    jQuery("#" + cd_container + ' span.download-wait').addClass('hide');
                                }
                            });

                        });
                    });

                    return false;

                });

            }

        },


        /**  Toggle
         **************************************************************** **/
        toggle: function () {

            var $_t = this,
                previewParClosedHeight = 25;

            jQuery("div.toggle.active > p").addClass("preview-active");
            jQuery("div.toggle.active > div.toggle-content").slideDown(400);
            jQuery("div.toggle > label").click(function (e) {

                var parentSection = jQuery(this).parent(),
                    parentWrapper = jQuery(this).parents("div.toggle"),
                    previewPar = false,
                    isAccordion = parentWrapper.hasClass("toggle-accordion");

                if (isAccordion && typeof(e.originalEvent) != "undefined") {
                    parentWrapper.find("div.toggle.active > label").trigger("click");
                }

                parentSection.toggleClass("active");

                if (parentSection.find("> p").get(0)) {

                    previewPar = parentSection.find("> p");
                    var previewParCurrentHeight = previewPar.css("height");
                    var previewParAnimateHeight = previewPar.css("height");
                    previewPar.css("height", "auto");
                    previewPar.css("height", previewParCurrentHeight);

                }

                var toggleContent = parentSection.find("> div.toggle-content");

                if (parentSection.hasClass("active")) {

                    jQuery(previewPar).animate({height: previewParAnimateHeight}, 350, function () {
                        jQuery(this).addClass("preview-active");
                    });
                    toggleContent.slideDown(350);

                } else {

                    jQuery(previewPar).animate({height: previewParClosedHeight}, 350, function () {
                        jQuery(this).removeClass("preview-active");
                    });
                    toggleContent.slideUp(350);

                }

            });
        },


        /** Pickers
         **************************************************************** **/
        pickers: function (callBack) {
            /** Date Picker
             <input type="text" class="form-control datepicker" data-format="yyyy-mm-dd" data-lang="en" data-RTL="false">
             ******************* **/
            var _container_1 = jQuery('.datepicker');

            if (_container_1.length > 0) {

                if (jQuery().datepicker) {

                    _container_1.each(function () {
                        var _t = jQuery(this),
                            _lang = _t.attr('data-lang') || 'en';

                        /*if (_lang != 'en' && _lang != '') { // load language file
                         loadScript(plugin_path + 'bootstrap.datepicker/locales/bootstrap-datepicker.' + _lang + '.min.js');
                         }*/

                        jQuery(this).datepicker({
                            format: _t.attr('data-format') || 'yyyy-mm-dd',
                            language: _lang,
                            rtl: _t.attr('data-RTL') == "true" ? true : false,
                            changeMonth: _t.attr('data-changeMonth') == "false" ? false : true,
                            todayBtn: _t.attr('data-todayBtn') == "false" ? false : "linked",
                            calendarWeeks: _t.attr('data-calendarWeeks') == "false" ? false : true,
                            autoclose: _t.attr('data-autoclose') == "false" ? false : true,
                            todayHighlight: _t.attr('data-todayHighlight') == "false" ? false : true,

                            onRender: function (date) {
                                // return date.valueOf() < nowDate.valueOf() ? 'disabled' : '';
                            }
                        }).on('changeDate', function (ev) {

                            // AJAX POST - OPTIONAL

                        }).data('datepicker');
                    });

                }

            }


            /** Range Picker
             <input type="text" class="form-control rangepicker" value="2015-01-01 - 2016-12-31" data-format="yyyy-mm-dd" data-from="2015-01-01" data-to="2016-12-31">
             ******************* **/
            var _container_2 = jQuery('.rangepicker');

            if (_container_2.length > 0) {

                if (jQuery().datepicker) {

                    _container_2.each(function () {

                        var _t = jQuery(this),
                            _format = _t.attr('data-format').toUpperCase() || 'YYYY-MM-DD';

                        _t.daterangepicker(
                            {
                                format: _format,
                                startDate: _t.attr('data-from'),
                                endDate: _t.attr('data-to'),

                                ranges: {
                                    'Today': [moment(), moment()],
                                    // 'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                                    'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                                    'Last 30 Days': [moment().subtract(29, 'days'), moment()]
                                    //'This Month': [moment().startOf('month'), moment().endOf('month')],
                                    // 'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                                }
                            },
                            function (start, end, label) {

                                if (callBack) {
                                    callBack(start.format('YYYY-MM-DD'), end.format('YYYY-MM-DD'));
                                }
                                // alert("A new date range was chosen: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
                            });

                    });

                }

            }


            /** Time Picker
             <input type="text" class="form-control timepicker" value="11 : 55 : PM">
             ******************* **/
            var _container_3 = jQuery('.timepicker');

            if (_container_3.length > 0) {


                if (jQuery().timepicki) {

                    _container_3.timepicki();

                }

            }


        },

        /** Select2
         **************************************************************** **/
        select2: function () {
            var _container = jQuery('select.select2');

            if (_container.length > 0) {

                _container.each(function () {
                    var _t = jQuery(this);

                    if (!_t.hasClass('select2-custom')) {
                        _t.select2();
                    }

                })
            }

        },


        /**  Background Image
         class="boxed" should be added to body.
         Add to body - example: data-background="assets/images/boxed_background/1.jpg"
         **************************************************************** **/
        bgimage: function () {

            // Section Background Slideshow 1
            var _t = jQuery('section[data-background], section div[data-background]');

            if (_t.length > 0) {
                jQuery(_t).each(function () {
                    var _this = jQuery(this),
                        data_background = _this.attr('data-background') || '';

                    if (data_background != '') {

                        var data_background_delay = _this.attr('data-background-delay') || 3000,
                            data_background_fade = _this.attr('data-background-fade') || 750;
                        var instance;


                        var _db = data_background.split(',');
                        var _db = data_background.replace(" ", "").split(',');

                        if (_db[1]) {
                            _this.backstretch(_db, {
                                duration: parseInt(data_background_delay),
                                fade: parseInt(data_background_fade)
                            });
                        } else {
                            _this.backstretch(_db);
                        }


                        /** NEXT|PREV
                         <!-- Backstretch Navigation -->
                         <a id="bs-next" href="#"></a>
                         <a id="bs-prev" href="#"></a>
                         **/
                        jQuery(".bs-next", _this).bind("click", function (e) {
                            e.preventDefault();

                            _this.data('backstretch').next();
                        });

                        jQuery(".bs-prev", _this).bind("click", function (e) {
                            e.preventDefault();

                            _this.data('backstretch').prev();
                        });


                        /** BACKSTRETCH BUGFIX
                         For some reason, this plugin has some issues on window resize
                         We use a small trick to force it to resize the background
                         **/
                        jQuery(window).resize(function () {
                            if (window.afterResizeBkstr) {
                                clearTimeout(window.afterResizeBkstr);
                            }

                            window.afterResizeBkstr = setTimeout(function () {

                                _this.data('backstretch').next();

                            }, 350);

                        });


                    }

                })
            }

            // BODY BACKGROUND ONLY
            var data_background = jQuery('body').attr('data-background') || '';

            if (data_background != '') {

                if (data_background) {
                    jQuery.backstretch(data_background);
                    jQuery('body').addClass('transparent'); // remove backround color of boxed class
                }
            }
        },

        /**
         * {title:"",content:"",timeOut:2000,type:"Success"}
         * type:success, info ,warning, error
         * @param options
         */
        toastr: function (options) {
            var _default = {
                    title: "",
                    content: "",
                    type: "info",
                    closeButton: false,
                    progressBar: true,
                    showMethod: 'slideDown',
                    positionClass: "toast-top-center",
                    timeOut: 3000
                },
                _option = $.extend(_default, options);
            toastr.options = _option;
            if (_option.type == "success") {
                toastr.success(_option.title, _option.content)
            } else if (_option.type == "warning") {
                toastr.warning(_option.title, _option.content)
            } else if (_option.type == "error") {
                toastr.error(_option.title, _option.content)
            } else if (_option.type == "info") {
                toastr.info(_option.title, _option.content)
            }
        },


        /** 00. Slider Full Height
         **************************************************************** **/
        sliderFull: function () {
            _headerHeight = 0;

            if (jQuery("#header").hasClass('transparent') || jQuery("#header").hasClass('translucent')) {
                _headerHeight = 0;
            } else {
                _headerHeight = jQuery("#header").outerHeight() || 0;

                if (jQuery("#topBar").length > 0) {
                    _topBarHeight = jQuery("#topBar").outerHeight() || 0;
                    _headerHeight = _headerHeight + _topBarHeight;
                }
            }


            _screenHeight = jQuery(window).height() - _headerHeight;

            if (jQuery(".banner-bottom-menu").length > 0) {
                _screenHeight = _screenHeight - jQuery(".banner-bottom-menu").height();
            }

            if (jQuery("#header").hasClass('static'))
                _screenHeight = jQuery(window).height();

            if (jQuery("#slider").hasClass('halfheight')) {
                jQuery("#slider.halfheight").height(_screenHeight / 2);
            }

            if (jQuery("#slider").hasClass('thirdheight')) {
                jQuery("#slider.thirdheight").height(_screenHeight / 1.5);
            }

            if (jQuery("#slider").hasClass('fullheight')) {
                jQuery("#slider.fullheight").height(_screenHeight);
                jQuery("#slider.fullheight-min").css({"min-height": _screenHeight + "px"});
            }

            if (window.width < 960) {
                jQuery("#slider.mobile-fullheight").height(_screenHeight);
            }
        },

        /**
         *resize
         */
        afterResize: function () {

            // On Resize
            jQuery(window).resize(function () {

                if (window.afterResizeApp) {
                    clearTimeout(window.afterResizeApp);
                }

                window.afterResizeApp = setTimeout(function () {

                    /**
                     After Resize Code
                     .................
                     **/

                    //sliderFull();

                    window.width = jQuery(window).width();
                    window.height = jQuery(window).height();

                    // Resize Flex Slider if exists!
                    if (jQuery('.flexslider').length > 0) {
                        jQuery('.flexslider').resize();
                    }


                }, 300);

            });
        }


    }

});