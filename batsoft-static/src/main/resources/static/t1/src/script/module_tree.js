/**
 * Created by Bat Admin on 2015/6/1.
 */
define(function (require, exports, module) {

    /**
     * init 初始化树
     * @param options
     * @returns {*}
     */
    function init(options){

        var _default =  {
                container:"tree",
                treeUrl:null,
                plugins : [/*"dnd","contextmenu","checkbox","wholerow",*/"themes","types"],
                callback : true, // enable all modifications
                call_back:function(e,data){
                    // 回调函数
                },
                ready_call_back:function(){
                    //加载完成回调
                }
        },
        _option    = $.extend(_default, options);
        $('#'+_option.container).jstree({
            'types': {
                "default" : {
                    "icon" : ""
                },
            },
            'core' : {
                "check_callback" : _option.callback, // enable all modifications
                'data' : {
                    'url' : function (node) {
                        console.log("=======>>"+node.id);
                        return node.id === '#' ? _option.treeUrl+"?pid=0" : _option.treeUrl+"?pid="+node.id;
                    },
                    "cache":true, //缓存
                    "dataType" : "json",
                    'data' : function (node) {
                        return { 'id' : node.id };
                    }
                }
            },"plugins" : _option.plugins

        });

        // 选中事件
        $('#'+_option.container).on("select_node.jstree", function (e, data) {
            if(data) {
                _option.call_back(e, data);
            }
        });

        // 加载完成事件
        $('#'+_option.container).on("ready.jstree", function (e,data) {
            if(data) {
                _option.ready_call_back(e, data);
            }
        });


    }

    /**
     * refresh  刷新树
     * @param options
     */
    function refresh(options){

        var _default =  {
                container:"tree",
                treeUrl:null,
            },
            _option    = $.extend(_default, options);
        $.ajax({
            url:_option.treeUrl+"?v="+Math.random(),
            type: 'get',
            dataType   : 'json',
            success    : function(data){
                $('#'+_option.container).jstree(true).settings.core.data=data;
                $('#'+_option.container).jstree(true).refresh();
            }
        })
    }

    /**
     * 全选
     * @param options
     */
    function selectAll(options){
        var _default =  {
                container:"tree"
            },
            _option    = $.extend(_default, options);
        $('#'+_option.container).jstree(true).select_all();
    }

    /**
     * 全选不选
     * @param options
     */
    function deselectAll(options){
        var _default =  {
                container:"tree"
            },
            _option    = $.extend(_default, options);
        $('#'+_option.container).jstree(true).deselect_all();
    }

    /**
     * 展开
     * @param options
     */
    function openAll(options){
        var _default =  {
                container:"tree"
            },
            _option    = $.extend(_default, options);
        $('#'+_option.container).jstree(true).open_all();
    }
    /**
     * 展开
     * @param options
     */
    function closeAll(options){
        var _default =  {
                container:"tree"
            },
            _option    = $.extend(_default, options);
        $('#'+_option.container).jstree(true).close_all();
    }

    /**
     * 获取选中项
     * @param options
     */
    function getSelected(options){
        var _default =  {
                container:"tree"
            },
            _option    = $.extend(_default, options);
       return $('#'+_option.container).jstree(true).get_selected();
    }

    /**
     * 获取选中项 包含半选中状态的值
     * @param options
     */
    function getAllChecked(options){
        var _default =  {
                container:"tree"
            },
            _option    = $.extend(_default, options);
        var tree=$('#'+_option.container).jstree(true);
        return tree.get_checked = function(full) {
            var tmp=new Array();
            for(var i in tree._model.data){
                if(tree.is_undetermined(i)||tree.is_checked(i)){
                    if(0!=i&&"#"!=i)
                    {
                        tmp.push(full?tree._model.data[i]:i);
                    }

                }
            }
            return tmp;
        }();
    }



    /**
     * 选中节点
     * @param options
     * @returns {*|jQuery}
     */
    function selectNode(options){
        var _default =  {
                container:"tree"
            },
            _option    = $.extend(_default, options);
        $('#'+_option.container).jstree(true).select_node(_option.nodes);
    }

    /**
     * 销毁树
     * @param options
     * @returns {*|jQuery}
     */
    function destroy(options){
        var _default =  {
                container:"tree"
            },
            _option    = $.extend(_default, options);
        $('#'+_option.container).jstree().destroy();
    }

    exports.init    = init;
    exports.refresh =refresh;
    exports.selectAll =selectAll;
    exports.deselectAll =deselectAll;
    exports.openAll =openAll;
    exports.closeAll =closeAll;
    exports.getSelected =getSelected;
    exports.selectNode =selectNode;
    exports.getAllChecked =getAllChecked;
    exports.destroy=destroy;
});


