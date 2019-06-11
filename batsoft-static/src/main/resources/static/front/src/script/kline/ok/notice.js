;(function(win, document) {
    var $ = jQuery.noConflict();

    var isLogin = $('#banner_is_login').val();

    if (Number(isLogin) === 1) {

        var notification = {
            hasUnread: false,

            // 初始化方法
            init: function () {
                var _this = this;
                OkAjax.getNoticesList({
                    success: function (data) {
                        _this.renderNotices(data);
                        //20171207注销，不再ajax轮询，为了减少服务器压力，页面刷新时请求一次即可，老板说的
                        // setInterval(function () {
                        //
                        //     _this.refreshNoticeList();
                        // }, 20 * 1000);
                    }
                });
            },
            // 渲染公告列表 （param: list）
            renderNotices: function (list) {
                var _this = this;

                if (!list || !list.length) {
                    return;
                }

                var $notice = $('#noticeWrapper');


                var listContent = '';

                $.each(list, function (index, item) {
                    var maskClass = '';
                    if (Number(item.read) === 0) {
                        _this.hasUnread = true;
                        $notice.find('#unreadFlag').show();

                    } else {
                        maskClass = 'mask';
                    }

                    var createdDate = new Date(item.createdDate).Format('yyyy-MM-dd hh:mm:ss');

                    listContent += '<li class="' + maskClass + '" data-id="' + item.id + '"><dl><dt><a href="' + item.url + '">' + item.title + '</a></dt><dd>' + createdDate + '</dd></dl></li>'
                });

                $notice.find('#noticeContentList').html(listContent);
            },

            // 请求公告列表
            refreshNoticeList: function () {
                var _this = this;

                OkAjax.getNoticesList({
                    success: function (data) {
                        _this.renderNotices(data);
                    }
                })
            },

            // 显示公告列表
            showNoticeList: function () {
                var $notice = $('#noticeWrapper');
                var length = $notice.find('#noticeContentList li').length;

                $notice.find('#noticeContent').show();
            },

            // 隐藏公告列表
            hideNoticeList: function () {
                var $notice = $('#noticeWrapper');
                $notice.find('#noticeContent').hide();
            },

            // 发送单条公告状态
            sendNoticeStatus: function (id) {
                var $notice = $('#noticeWrapper');

                var option = {
                    dynamicId : id
                };

                OkAjax.sendNoticeId({
                    data: option,
                    success: function () {
                        $notice.find('#noticeContent').hide();
                    }
                })
            }
        }
    };

    win.notification = notification;

    // 公告初始化
    notification && notification.init();

    // 公告显影
    $('#noticeWrapper').hover(function () {

        notification && notification.showNoticeList();
    }, function () {

        notification && notification.hideNoticeList();
    });

    // 点击公告
    $('#noticeContentList').on('click', 'li', function () {

        var id = $(this).attr('data-id');

        notification.sendNoticeStatus(id);
    })

})(window, document);

