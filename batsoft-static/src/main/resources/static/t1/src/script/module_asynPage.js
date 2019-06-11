/**
 * Created by Bat Admin on 2015/6/1.
 */
define(function (require, exports, module) {
    var core = require('./module_core'),
    tpl = require('./module_tpl');
    /**
     * 页面调用  动态刷新页面
     * @param page  页码
     * @param pageSize 每页条数
     * @param url  数据请求URl
     * @param model_tpl 渲染模板
     * @param model_render 页面渲染位置
     */
    function page(page,pageSize,url,model_tpl,model_render,callBack,data){

        if(pageSize>0){

        }else{
            pageSize= 10;
        }

        data = data||{};
        data.page=page;
        data.pageSize=pageSize;
        initData(page);
        //初始化分页
        function initPage(pagesize,total,current) {
            $.jqPaginator('#pagination', {
                totalCounts: total,
                visiblePages: 10,
                pageSize:pagesize,
                currentPage: current,
                onPageChange: function (page, type) {
                    data = data||{};
                    data.page=page;
                    data.pageSize=pageSize;
                    core.list({url: BASE_PATH + url,"type":"POST",data:data}).then(function (data) {
                        
                        if(data.rows.length>0){
                            tpl.render({"tplId": "#"+model_tpl, "renderId": "#"+model_render, "data": data.rows});
                        }else{
                            $("#"+model_render).html("<tr class=\"no-data-tr\">\n" +
                                "                        <td colspan=\"100\">\n" +
                                "                            <div class=\"no-data\">\n" +
                                "                              <span th:text=\"#{no_data}\"> 暂无数据 </span> </div>\n" +
                                "                        </td>\n" +
                                "                    </tr>");
                        }
                    }).then(function () {
                        if(callBack){
                            callBack();
                        }
                    });
                }
            });
        }
        //初始化数据
        function initData(page){
            //加载当前委托数据
            core.list({url: BASE_PATH + url,"type":"POST",data:data}).then(function (data) {
                // obj.removeClass("lock");
                if(data.rows.length>0){
                    tpl.render({"tplId": "#"+model_tpl, "renderId": "#"+model_render, "data": data.rows});
                }else{
                    $("#"+model_render).html("<tr class=\"no-data-tr\">\n" +
                        "                        <td colspan=\"100\">\n" +
                        "                            <div class=\"no-data\">\n" +
                        "                              <span th:text=\"#{no_data}\">  </span> </div>\n" +
                        "                        </td>\n" +
                        "                    </tr>");
                }
                if(data.pagination.current==1) {
                    initPage(data.pagination.pageSize, data.pagination.total, data.pagination.current);
                }
            });
        }
    }


    function pagelist(page,pageSize,url,model_tpl,model_render,callBack,data){

        if(pageSize>0){

        }else{
            pageSize= 10;
        }

        data = data||{};
        data.page=page;
        data.pageSize=pageSize;
        initData(page);
        //初始化分页
        function initPage(pagesize,total,current) {
            $.jqPaginator('#pagination', {
                totalCounts: total,
                visiblePages: 10,
                pageSize:pagesize,
                currentPage: current,
                onPageChange: function (page, type) {
                    data = data||{};
                    data.page=page;
                    data.pageSize=pageSize;
                    core.list({url: BASE_PATH + url,"type":"POST",data:data}).then(function (data) {

                        if(data.data.rows.length>0){
                            tpl.render({"tplId": "#"+model_tpl, "renderId": "#"+model_render, "data": data.data.rows});
                        }else{
                            $("#"+model_render).html("<tr class=\"no-data-tr\">\n" +
                                "                        <td colspan=\"100\">\n" +
                                "                            <div class=\"no-data\">\n" +
                                "                              <span th:text=\"#{no_data}\">  </span> </div>\n" +
                                "                        </td>\n" +
                                "                    </tr>");
                        }
                    }).then(function () {
                        if(callBack){
                            callBack();
                        }
                    });
                }
            });
        }
        //初始化数据
        function initData(page){
            //加载当前委托数据
            core.list({url: BASE_PATH + url,"type":"POST",data:data}).then(function (data) {
                // obj.removeClass("lock");
                if(data.data.rows.length>0){
                    tpl.render({"tplId": "#"+model_tpl, "renderId": "#"+model_render, "data": data.data.rows});
                }else{
                    $("#"+model_render).html("<tr class=\"no-data-tr\">\n" +
                        "                        <td colspan=\"100\">\n" +
                        "                            <div class=\"data.data.total\" style=\" text-align: center;line-height: 300px;height: 300px;\" >\n" +
                        "                              <span th:text=\"#{no_data}\">  </span> </div>\n" +
                        "                        </td>\n" +
                        "                    </tr>");
                }
                if(data.data.total>1) {
                    initPage(data.data.pageSize, data.data.total, data.data.current);
                }
            });
        }
    }
    function pageKiss(page,pageSize,url,model_tpl,model_render,callBack,data){

        if(pageSize>0){

        }else{
            pageSize= 10;
        }

        data = data||{};
        data.page=page;
        data.pageSize=pageSize;
        initData(page);
        //初始化分页
        function initPage(pagesize,total,current) {
            $.jqPaginator('#pagination', {
                totalCounts: total,
                visiblePages: 10,
                pageSize:pagesize,
                currentPage: current,
                onPageChange: function (page, type) {
                    data = data||{};
                    data.page=page;
                    data.pageSize=pageSize;

                    core.list({url: BASE_PATH + url,"type":"GET",data:data}).then(function (data) {
                        if(data.data.rows.length > 0){
                            tpl.render({"tplId": "#"+model_tpl, "renderId": "#"+model_render, "data": data.data.rows});
                        }else{
                            $("#"+model_render).html("<tr class=\"no-data-tr\">\n" +
                                "                        <td colspan=\"100\">\n" +
                                "                            <div class=\"no-data\">\n" +
                                "                              <span th:text=\"#{no_data}\"> 暂无数据！ </span> </div>\n" +
                                "                        </td>\n" +
                                "                    </tr>");
                        }
                    }).then(function () {
                        if(callBack){
                            callBack();
                        }
                    });
                }
            });
        }
        //初始化数据
        function initData(page){
            //加载当前委托数据
            core.list({url: BASE_PATH + url,"type":"GET",data:data}).then(function (data) {
                // obj.removeClass("lock");
                if(data.data.rows.length>0){
                    tpl.render({"tplId": "#"+model_tpl, "renderId": "#"+model_render, "data": data.data.rows});
                }else{
                    $("#"+model_render).html("<tr class=\"no-data-tr\">\n" +
                        "                        <td colspan=\"100\">\n" +
                        "                            <div class=\"data.data.total\" style=\" text-align: center;line-height: 300px;height: 300px;\" >\n" +
                        "                              <span th:text=\"#{no_data}\">  </span> </div>\n" +
                        "                        </td>\n" +
                        "                    </tr>");
                }
                if(data.data.total>1) {
                    initPage(data.data.pageSize, data.data.total, data.data.current);
                }
            });
        }
    }
    exports.page    = page;
    exports.pagelist    = pagelist;
    exports.pageKiss =pageKiss;
});