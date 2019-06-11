/**
 *<p>page_cms_article.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-06-13 17:06:28
 */
define(function (require, exports) {

    var common = require('./page_common'), //common
        tpl = require('./module_tpl'),
        page = require('./module_page'),
        core = require('./module_core');

    var defaultSize=5;

    /**
     * 文章首页
     */
    function index() {
        // 查询文章类别
        core.list({url: BASE_PATH + "/article/channelArticles"}).then(function (data) {
            tpl.render({"tplId": "#articleTmpl", "renderId": "#articleRender", "data": data.data});
        })
    }

    /**
     * 文章类别列表导航
     */
    function listChannels() {
        // 查询文章类别
        core.list({url: BASE_PATH + "/article/listChannels"}).then(function (data) {
            tpl.render({"tplId": "#articleNavTmpl", "renderId": ".article-nav", "data": data.data});
        })
    }
    /**
     * 文章列表
     */
    function listArticles() {
        var type=$("#article_type").val();
        page.page({url:BASE_PATH+"/article/page/"+type,showId:"articlePage",parameter:{pageSize:2}},function (data) {
            tpl.render({"tplId": "#articleListTmpl", "renderId": "#articleList", "data": data.rows});
        });
    }


    exports.index=index;
    exports.listChannels = listChannels;
    exports.listArticles = listArticles;
});