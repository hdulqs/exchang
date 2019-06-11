/**
 * Created by Bat Admin on 2015/6/1.
 */
define(function (require, exports, module) {

    /**
     * jquery分页方法
     * @param options
     * ** options.url 请求方法
     * ** options.data 请求参数
     * ** options.showId 渲染ID
     * ** options.pageSize 页面条数
     * @param callback
     */
    function page(options, callback){
        var _op = options || {},
            _url = _op.url,
            _showId = _op.showId?_op.showId:"pageId",
            _data = _op.data || {},   //url参数
            _page_parameter = _op.parameter || {},  //page样式参数
            _firstBtnText = _page_parameter.firstBtnText? _page_parameter.firstBtnText : '首页',
            _lastBtnText = _page_parameter.lastBtnText? _page_parameter.lastBtnText : '尾页',
            _prevBtnText = _page_parameter.prevBtnText? _page_parameter.prevBtnText : '上一页',
            _nextBtnText = _page_parameter.nextBtnText? _page_parameter.nextBtnText : '下一页',
            _pageSize = _page_parameter.pageSize?_page_parameter.pageSize:10;

         $("#"+_showId).html("");
        /* 重新生成容器 */
        $("#"+_showId).html("<div class='m-pagination'></div>");
        $(".m-pagination").eq(0).page({
            firstBtnText : _firstBtnText,
            lastBtnText : _lastBtnText,
            prevBtnText : _prevBtnText,
            nextBtnText : _nextBtnText,
            showInfo : true,
            showJump : true,
            jumpBtnText : '跳转',
            showPageSizes : true,
            pageSize:_pageSize,
            infoFormat : '{start} ~ {end}条，共{total}条',
            remote : {
                url : _url, // 请求地址
                params : {
                    'param' : _data
                    // 自定义请求参数
                },
                beforeSend : function(XMLHttpRequest) {
                    // to do beforeSend
                    // callback && callback({"code": "loading", "msg": "loading"})
                },
                success : function(data, pageIndex) {
                    //data为响应返回的json数据
                    callback && callback(data)
                },
                complete : function(XMLHttpRequest, textStatu) {
                    //...
                },
                pageIndexName : 'page', //请求参数，当前页数，索引从0开始
                pageSizeName : 'pageSize', //请求参数，每页数量
                totalName : 'total' //指定返回数据的总数据量的字段名
            }
        });
    }
    exports.page    = page;
});