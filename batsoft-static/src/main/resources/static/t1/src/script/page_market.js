/**
 *<p>page_market.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {
    var trade_area={};
    var
        U = require('./module_utils'),
        core = require('./module_core');
    /**
     * 初始数据
     * @type {{coin: string}}
     * @private
     */
    var _default = {coin: 'BTC'}



    /**
     * 行情图表
     */
    function init() {
        var loadIntervel;

        var removeLoading = function () {
            var index = ['trade', 'book', 'kline', 'ticker'];
            var rmload = 1;
            for (var j in index) {
                if (Loading[index[j]] == undefined) {
                    rmload = 0;
                    break;
                }
            }
            if (rmload == 1) {
                window.clearInterval(loadIntervel);
                $("#chart_loading").remove();
            }
        }
        // kline.symbol = $("#base_asset").val() + "_" + $("#quote_asset").val();
        _sortIndex = 0;
        $(function () {
            changeWindow("", "", _sortIndex);
            bindResize(document.getElementById('dragLine'));
            $(window).bind('resize scroll',
                function (e) {
                    changeWindow("", "", _sortIndex);
                });



            hour_min();

            //头部下拉框的选择
            $(".userStatus").hover(
                function () {
                    $(this).find(".userItemSon").show();
                },
                function () {
                    $(this).find(".userItemSon").hide();
                }
            );

            $(document).on("click", ".product_cont tr", function (event) {
                if ($(event.target).is(".product_cont .tc span")) {
                    return false;
                }
                window.location = $(this).find(".pairs").parents("td a").attr("href");
            });
        });

        window.onload = function () {
            loadIntervel = window.setInterval(removeLoading, 1000);
        };



        var hour_min = function () {
            var obj = $(".hour_ul").find(".selected");
            if (obj.length > 0) {
                $(".hour_ul").prepend(obj.parent().show());
                $(".hour_ul").find("li:gt(0)").hide();
            } else {
                obj = $(".min_ul").find(".selected");
                if (obj.length > 0) {
                    $(".min_ul").prepend(obj.parent().show());
                    $(".min_ul").find("li:gt(0)").hide();
                }
            }
            $(".hour_ul").hover(
                function () {
                    $(this).addClass("hour_min_up").removeClass("hour_min_down").find("li").show();
                },
                function () {
                    $(this).addClass("hour_min_down").removeClass("hour_min_up").prepend($(this).find(".selected").parent());
                    $(this).find("li:gt(0)").hide();
                }
            );
            $(".min_ul").hover(
                function () {
                    $(this).addClass("hour_min_up").removeClass("hour_min_down").find("li").show();
                },
                function () {
                    $(this).addClass("hour_min_down").removeClass("hour_min_up").prepend($(this).find(".selected").parent());
                    $(this).find("li:gt(0)").hide();
                }
            );
        }
        var changeWindow = function (_windowH, _otherH_r, _sortIndex) {
            var _otherH_l = $(".header").height() + $(".area_l_b").height() + 11;
//        var _windowH = $(window).height();
//        if ((navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))) {
//            var _windowH = $(window).height() * 1200 / $(window).width();
//            $("body,html").height(_windowH);
//        } else {
            var _windowH = $(window).height();
            $("body,html").height("100%");
//        }

            var _otherH_r = $(".header").height() + $(".area_r_b").height() + 11;
            // $("#kLineMain").height(_windowH - _otherH_l);
            $("#kLineMain").height(_windowH - _otherH_l-10);
            $(".area_r_t_H").height(_windowH - _otherH_r);
            $(".marketList ").height(_windowH - _otherH_r - $(".marketHeader").height());
            changeTableBox_H(_windowH, _otherH_r, _sortIndex);
        }

        var changeTableBox_H = function (_windowH, _otherH_r, _sortIndex) {
            var _h = _windowH - _otherH_r - $(".buy_sell_tableHeader").height() - 25 - $(".colBar_2").height();  //---20180201
            if (_sortIndex == 0) {
                var scrollHeight = $('.sortList_wrap.sortListSell').prop("scrollHeight");
                $(".tableBox_H").removeClass("scrollStyle");
                $(".sortList_wrap").show();
                // $('.sortList_wrap.sortListSell').scrollTop(scrollHeight,200);
                $("#sortSellBox").css("position", "absolute");
                if ((/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent))) {
                    $(".tableBox_H").height(225);
                }else {
                    $(".tableBox_H").height(235);
                    $(".area_r_t").height(590);
                    $(".area_r_box1").height(590);
                    $(".area_r_box2").height(590);
                    $(".marketList").height(590);
                    // $(".tableBox").height(590);

                    // $(".tableBox_H").height((_windowH - _otherH_r - $(".buy_sell_tableHeader").height() * 2 - 55) / 2);
                }
                // $(".sortListSell .scrollStyle").scrollTop($("#sortSellBox").height() - $(".sortListSell .scrollStyle").height());
            } else {
                $(".tableBox_H").addClass("scrollStyle");
                if (_sortIndex == 1) {
                    $(".sortList_wrap.sortListBuy").show();
                    $(".sortList_wrap.sortListSell").hide();
                    $(".tableBox_H").height(590);  //---20180201
                    $(".area_r_box1").height(590);
                    $(".area_r_box2").height(590);
                    $(".area_r_t").height(590);
                    $(".marketList").height(590);

                }
                $(".tableBox_H").addClass("scrollStyle");
                if (_sortIndex == 2) {
                    $(".sortList_wrap.sortListBuy").hide();
                    $(".sortList_wrap.sortListSell").show();
                    $(".tableBox_H").height(590);
                    $(".area_r_box1").height(590);
                    $(".area_r_box2").height(590);
                    $(".marketList").height(590);
                    $(".area_r_t").height(590);
                    // $(".sortList_wrap").height(524);


                }
            }
        }

        //---排序方式
        $(".sortBox li").on("click", function () {


            $(this).siblings("li").removeClass("curr");
            $(this).addClass("curr");
            _sortIndex = $(".sortBox li").index(this);
            changeWindow("", "", _sortIndex);
        });

        $(".zhibiaoBt li").on("click", function () {
            $(".selectDiv ul").hide();
        });


        //---交易框折叠
        $(".tabsSwitchBt").on("click", function () {
            $(this).toggleClass("showCon");
            $(".tabsSwitchCon").toggle();
            var _windowH = $(window).height();
            var _otherH_r = $(".header").height() + $(".area_r_b").height() + 11;
            changeWindow(_windowH, _otherH_r, _sortIndex);
        });

        //--products切换显示隐藏
        // $(".coin_select").on("click", function () {
        //     $(this).toggleClass("show");
        //     $(".products").slideToggle();
        // });
        // $(document).bind("click", function (e) {
        //     var target = $(e.target);
        //     if (target.closest(".products").length == 0 && target.closest(".coin_select").length == 0) {
        //         $(".products").hide();
        //         $(".coin_select").removeClass("show");
        //     }
        // });
        //----深度图
        $("#depthBt").on("click", function () {
            $(".coverDepth").show();
        });
        $("#kLineBt").on("click", function () {
            $(".coverDepth").hide();
        });
        //----以下拖拽
        var bindResize = function (el) {
//初始化参数
//             var els = document.getElementById('kLineMain').style;
//鼠标的 X 和 Y 轴坐标
            x = 0;
            $(el).mousedown(function (e) {
//按下元素后，计算当前鼠标与对象计算后的坐标
                y = e.clientY - el.offsetHeight - $("#kLineMain").height() - 90;
//在支持 setCapture 做些东东
                el.setCapture ? (
//捕捉焦点
                    el.setCapture(),
//设置事件
                        el.onmousemove = function (ev) {
                            mouseMove(ev || event);
                        },
                        el.onmouseup = mouseUp
                ) : (
//绑定事件
                    $(document).bind("mousemove", mouseMove).bind("mouseup", mouseUp)
                );
//防止默认事件发生
                e.preventDefault();
            });

//移动事件
            function mouseMove(e) {
                if (e.clientY > 200) {
                    $("#mainBottom").height($(window).height() - 45 - e.clientY - y + 5);
                    $(".areaTable").height($("#mainBottom").height() - 32 - 31);
                    $(window).trigger("resize");  //20161217
                }
            }

//停止事件
            function mouseUp() {
                el.releaseCapture ? (
//释放焦点
                    el.releaseCapture(),
//移除事件
                        el.onmousemove = el.onmouseup = null
                ) : (
//卸载事件
                    $(document).unbind("mousemove", mouseMove).unbind("mouseup", mouseUp)
                );
            }
        }



       // return kline;
    }

    exports.init = init;
});