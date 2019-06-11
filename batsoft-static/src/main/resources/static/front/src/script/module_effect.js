/**
 * 特效处理
 */
define(function (require, exports, module) {
    module.exports = {


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
            }else if (_option.type == "warning"){
                toastr.warning(_option.title, _option.content)
            }else if (_option.type == "error") {
                toastr.error(_option.title, _option.content)
            }else if (_option.type == "info"){ toastr.info(_option.title, _option.content)}
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