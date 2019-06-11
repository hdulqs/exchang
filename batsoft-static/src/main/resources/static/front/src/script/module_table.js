define(function(require, exports, module) {

    var swal = require('./module_swal'), //弹窗 alert
    core = require('./module_core');
    module.exports = {

        /**
         * 获得表格选中的ID
         */
        getIdSelects : function(table){
            return $.map(table.bootstrapTable('getSelections'), function(row) {
                return row.id
            });
        },

        refresh:function(table){
            var $table = table;
            $table.bootstrapTable('refresh');
        },


        /**
		 * 初始表格
         * @param table
         * @param conf
         */
        initTable : function(table, conf) {
            var _default =  {
                    toggle:"table",
                    showRefresh: false,
                    showExport: false,
                    minimumCountColumns:"2",
                    pagination:true,
                    pageList:"[10, 25, 50, 100, ALL]",
                    showFooter:false,
                    sidePagination:"server",
                    queryParams:queryParams,
                    url:"",
				    removeCallBack:remove,
                    callBack:null,
				    removeUrl:""

            },
             $table = table,
			 selections = [],
             $remove = $('button.remove'),
			 $search = $('.search'),
            _conf    = $.extend(_default, conf);
            // 查询操作
            $search.click(function () {
                $table.bootstrapTable('refresh');
            });

            /**
             * 只有选中才让删除按钮生效
             */
            $table.on('check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table', function () {
                $remove.prop('disabled', !$table.bootstrapTable('getSelections').length);
                $(".btn-disable").prop('disabled', !$table.bootstrapTable('getSelections').length);
            });
            /**
             * 数据加载完成后执行
             */
            $table.on('load-success.bs.table', function (e, arg1, arg2) {
                // load success to  do
            });


            //删除事件
            $remove.on("click", function () {
                var ids =getIdSelects($table);
                if(ids.length>0) {
                    swal.swalConfirm("确认删除吗?", "", "确认删除", function () {
                        _conf.removeCallBack({"ids": ids, "url": _conf.removeUrl});
                        $remove.prop('disabled', true);
                    })
                }else{
                    swal.swalError("请选择删除项","");
                }

            })
            /**
             * 删除方法 批量删除/单独删除
			 * 可以通过回调自己处理 conf={removeCallBack:callback} 需要传入 删除url
             * ids:[{"id":"1","id":"2"}]
             */
            function remove(_option) {
                var op = _option || {};
                core.remove({
                    "url": op.url,
                    "data": {"ids": op.ids},
                    "type": "POST"
                }).then(function (data) {
                    var successList = data.data;
                    if (data.success) {
                        for (var id in successList) {
                        	//to do
                        }
                        $table.bootstrapTable('refresh');
                        swal.swalBase(data.msg, "", "success");
                    }

                })
            }

            /**
             * 获得表格选中的ID
             */
           function getIdSelects(){
                return $.map($table.bootstrapTable('getSelections'), function(row) {
                    return row.id
                });
            }
            /**
             * table queryParams
             * @param params
             * @returns {{page: *, pageSize: *, sortName, sortOrder}}
             */
            function queryParams(params) {
                var params = {
                    page: params.page,
                    pageSize: params.pageSize,
                    sortName: params.sort,
                    sortOrder: params.order
                };
                $('.paramsPanel').find('input[name]').each(function () {
                    params[$(this).attr('name')] = $(this).val();
                });
                $('.paramsPanel').find('select[name]').each(function () {
                    params[$(this).attr('name')] = $(this).val();
                });
                return params;
            }

            /**
             * bootstrap table  init
             */
            $table.bootstrapTable(_conf);

        }

    }

});