/**
 *<p>page_otc.js</p>
 * Copyright:    http://www.batsoft.cn
 * @author:      Bat Admin
 * @version:     V1.0
 * @Date:        2017-05-12 20:18:59
 */
define(function (require, exports) {

    /**
     * 初始数据
     * @type {{coin: string}}
     * @private
     */
    var _default={coin:'BTC'}

    var  mVad      = require('./module_validator'), //表单校验
        swal      = require('./module_swal'), //弹窗 alert
        effect = require('./module_effect'), //特效
        tpl = require('./module_tpl'),
        table = require('./module_table'),
        U = require('./module_utils'),

        core      =require('./module_core');

    function calculate(){
        var calcFormula = defaultBourse;
        var currency=encodeURIComponent($("#chooseCurrency").val());
        var market=$("#exchange").val();
        if(currency!=""&&market!=""){
            $.ajax({
                type: "POST",
                url:BASE_PATH+ "/ad/ajaxGetCalcFormula",
                data: {
                    currency: currency,
                    market: $("#exchange").val(),
                    time: Math.random()
                },
                success: function (msg) {
                    if (msg != null && msg != '') {
                        calcFormula = msg + getPremiumPrice();
                    } else {
                        if (parseFloat($("#premium").val()) == 0) {
                            calcFormula += $("#chooseCurrency")[0].options[$("#chooseCurrency")[0].selectedIndex].getAttribute("hl")
                        } else {
                            calcFormula += $("#chooseCurrency")[0].options[$("#chooseCurrency")[0].selectedIndex].getAttribute("hl") + getPremiumPrice();
                        }
                    }
                    $("#calcFormula").val(calcFormula);
                    getPrice();
                },
                error: function () {
                    if (parseFloat($("#premium").val()) == 0) {
                        calcFormula += $("#chooseCurrency")[0].options[$("#chooseCurrency")[0].selectedIndex].getAttribute("hl")
                    } else {
                        calcFormula += $("#chooseCurrency")[0].options[$("#chooseCurrency")[0].selectedIndex].getAttribute("hl") + getPremiumPrice();
                    }
                    $("#calcFormula").val(calcFormula);
                    getPrice();
                }
            });
        }
    }
    /**
     * 通过选择国家修改币种等信息
     * @param flag
     */
    function getCurrencyByCountry(flag) {
        var currency = $("#countryCode")[0].options[$("#countryCode")[0].selectedIndex].getAttribute("currency");
        $("#chooseCurrency").select2("val", currency);

        if($("#peggingPriceUnit").length>0) {
            $("#peggingPriceUnit").text(currency + "/" + $("input[name=coin]:checked").val());
        }
        if (!!flag) {
            //calculate();
        }
    }
    function getPayMethodByCountry() {
        $.ajax({
            type: "POST",
            url: BASE_PATH+"/ad/ajaxGetPayMethod",
            data: "country=" + $("#countryCode").val(),
            success: function (data) {
                var topPayList = data.topPayList;
                var countryPayList = data.countryPayList;
                var otherPayList = data.otherPayList;
                var code = new String('');
                var list = code.split(',');
                var paymethod = $("#countryPayMethod .chosen-select");
                paymethod.html("");
                var html = "";
                if (topPayList.length > 0) {
                    var topLength = topPayList.length >= 3 ? 3 : topPayList.length;
                    html += "<optgroup label='热门'>";
                    for (i = 0; i < topLength; i++) {
                        if (true) {
                            html += "<option  value='" + topPayList[i].payCode + "' name='" + topPayList[i].name + "'>" + topPayList[i].name + "</option>";
                        }
                        if (false) {
                            html += "<option  value='" + topPayList[i].payCode + "' name='" + topPayList[i].enName + "'>" + topPayList[i].enName + "</option>";
                        }
                    }
                    html += "</optgroup>"
                }
                if (countryPayList.length > 0) {
                    html += "<optgroup  label='支持'>";
                    for (i = 0; i < countryPayList.length; i++) {
                        if (true) {
                            if (list.length > 0 && list.indexOf(countryPayList[i].payCode) > -1) {
                                html += "<option selected='selected'  value='" + countryPayList[i].payCode + "' name='" + countryPayList[i].name + "'>" + countryPayList[i].name + "</option>";
                            } else {
                                html += "<option  value='" + countryPayList[i].payCode + "' name='" + countryPayList[i].name + "'>" + countryPayList[i].name + "</option>";
                            }
                        }
                        if (false) {
                            if (list.length > 0 && list.indexOf(countryPayList[i].payCode) > -1) {
                                html += "<option selected='selected'  value='" + countryPayList[i].payCode + "' name='" + countryPayList[i].enName + "'>" + countryPayList[i].enName + "</option>";
                            } else {
                                html += "<option  value='" + countryPayList[i].payCode + "' name='" + countryPayList[i].enName + "'>" + countryPayList[i].enName + "</option>";
                            }
                        }
                    }
                    html += "</optgroup>"
                }
                if (otherPayList.length > 0) {
                    html += "<optgroup  label='其他'>";
                    for (i = 0; i < otherPayList.length; i++) {
                        if (true) {
                            if (list.length > 0 && list.indexOf(otherPayList[i].payCode) > -1) {
                                html += ("<option selected='selected' value='" + otherPayList[i].payCode + "' name='" + otherPayList[i].name + "'>" + otherPayList[i].name + "</option>");
                            } else {
                                html += ("<option  value='" + otherPayList[i].payCode + "' name='" + otherPayList[i].name + "'>" + otherPayList[i].name + "</option>");
                            }
                        }
                        if (false) {
                            if (list.length > 0 && list.indexOf(otherPayList[i].payCode) > -1) {
                                html += ("<option selected='selected' value='" + otherPayList[i].payCode + "' name='" + otherPayList[i].enName + "'>" + otherPayList[i].enName + "</option>");
                            } else {
                                html += ("<option  value='" + otherPayList[i].payCode + "' name='" + otherPayList[i].enName + "'>" + otherPayList[i].enName + "</option>");
                            }
                        }
                    }
                    html += "</optgroup>"
                }
                paymethod.append(html);
                paymethod.trigger("liszt:updated");
                paymethod.chosen();
            }
        });
    }

    /**
     * 货币修改方法
     * @param id
     */
    function currencyChange(id) {
        var currency = $("#"+id).select2("val");
        var digitCurrency = $("input[name=coin]:checked").val(); //document.getElementById("chooseDigitCurrency").options[digitSelect].value;

        document.getElementById("peggingPriceUnit").innerText = currency + "/" + digitCurrency;
        $(".digitCurrencySpan").html(digitCurrency);
        $(".currency-type").html(currency);

        //getMarketTicker(digitCurrency,1,0);
    }

    /**
     * otc 首页
     */
    function index(){

        effect.select2();

        /**
         * 加载采购信息
         *购买列表加载出售信息
         *出售列表加载购买信息
         */
        core.list({url:BASE_PATH+ "/otc/ad/"+_default.coin+"/4/0"}).then(function (data1) {
            tpl.render({"tplId": "#buyTpl", "renderId": "#buyRender", "data": data1});
        })
        core.list({url:BASE_PATH+ "/otc/ad/"+_default.coin+"/4/1"}).then(function (data1) {
            tpl.render({"tplId": "#sellTpl", "renderId": "#sellRender", "data": data1});
        })
    }

    /**
     * 加载list数据
     * @param coin
     */
    function loadAd(){
        /**
         * 操作
         * @returns {string}
         */
        function actionFormatter(value, row, index) {
            var actions = new Array();
            var handler ="";
            /**
             * 出售数据 对应按钮 为 购买
             * 购买数据 对应按钮 为 出售
             */

            if(row.otcType==0) {
                 handler = '<div class="pull-right ">' +
                    '<a class="btn btn-info btn-xs " href="/otc/info/'+row.id+'" >' +
                    '<i class="fa fa-edit"></i>购买'+row.coin+'</a>';
            }
            if(row.otcType==1) {
                handler = '<div class="pull-right ">' +
                    '<a class="btn btn-info btn-xs " href="/otc/info/'+row.id+'" >' +
                    '<i class="fa fa-edit"></i>出售'+row.coin+'</a>';
            }

            actions.push(handler)
            return actions.join('');
        }
        /**
         * table conf
         * @type {{url: string, removeUrl: string, columns: [*]}}
         */
        var conf = {
            url:BASE_PATH+ '/otc/findAdList',
            columns: [
                {
                    field: 'userNick',
                    title: '用户' ,
                    formatter: function (value, row, index) {
                       return ' <a href="/otc/user/'+row.userId+'"><img class="user-head img-circle" style="width: 40px;height: 40px" src='+FILE_PATH+row.userAvatar+' alt=""></a><a href="/otc/user/'+row.userId+'">   '+value+'</a><span class="state ONLINE"></span>';
                    }
                }, {
                    field: 'trustNum',
                    align: 'center',
                    title: '信用',
                    formatter: function (value, row, index) {
                        return '<span>交易笔数:'+row.exchangeNum+'笔</span> | <span>信任人数:'+row.trustNum+'人</span>';
                    }
                }, {
                    field: 'payType',
                    align: 'center',
                    title: '付款方式',
                }, {
                    field: 'minMoney',
                    align: 'center',
                    title: '限额',
                    formatter: function (value, row, index) {
                        return '<span>'+value+'</span> - <span>'+row.maxMoney+'</span>  '+row.currency;
                    }
                }
                , {
                    field: 'price',
                    title: '价格',
                    align: 'right',
                    formatter: function (value, row, index) {
                        return value+' '+row.currency;
                    }
                }, {
                    title: '操作',
                    align: 'center',
                    formatter: actionFormatter
                }]
        };

        /**
         * 初始化
         */
        table.initTable($('#table'), conf);
    }
    /**
     * otc list
     */
    function list(){
        loadAd();
        effect.select2();
        /**
         * 修改国家事件
         */
        $("#countryCode").change(function () {
            getCurrencyByCountry(true);
        });
        /**
         * 加载币种信息
         */
        core.list({url:BASE_PATH+ "/otc/coins"}).then(function (data) {
            tpl.render({"tplId": "#coinsTpl", "renderId": "#coinsRender", "data": data});
            effect.select2();
            loadAd();
        })
    }
    /**
     * 保存页面
     */
    function add(){

        effect.select2();
        /**
         * 修改货币事件
         */
        $("#chooseCurrency").change(function () {
            currencyChange('chooseCurrency');
        });

        /**
         * 修改国家事件
         */
        $("#countryCode").change(function () {
            getCurrencyByCountry(true);
            getPayMethodByCountry();
        });

        /**
         * 加载币种信息
         */
        core.list({url:BASE_PATH+ "/otc/coins"}).then(function (data) {
            tpl.render({"tplId": "#coinsTpl", "renderId": "#coinsRender", "data": data});
            getCurrencyByCountry(false);
            if($(".coin_def").length>0){
                $("input:radio[name='coin'][value='" + $(".coin_def").val() + "']").prop("checked", "checked");
                currencyChange('chooseCurrency');
            }
            $("input:radio[name=coin]").change(function () {
                currencyChange('chooseCurrency');
               // calculate();
            });
        });



        formSubmit();
    }

    /**
     * 用户个人信息界面
     */
    function user(){
        var uId=$("#uId").val();
        core.get({url:BASE_PATH+ "/member/user/findBaseInfo/"+uId}).then(function (data) {
            tpl.render({"tplId": "#userTpl", "renderId": "#userRender", "data": data.data});
            return data;
        })
        /**
         * 操作
         * @returns {string}
         */
        function actionFormatter(value, row, index) {
            var actions = new Array();
            var handler ="";
            /**
             * 出售数据 对应按钮 为 购买
             * 购买数据 对应按钮 为 出售
             */

            if(row.otcType==0) {
                handler = '<div class="pull-right ">' +
                    '<a class="btn btn-info btn-xs " href="/otc/info/'+row.id+'" >' +
                    '<i class="fa fa-edit"></i>购买'+row.coin+'</a>';
            }
            if(row.otcType==1) {
                handler = '<div class="pull-right ">' +
                    '<a class="btn btn-info btn-xs " href="/otc/info/'+row.id+'" >' +
                    '<i class="fa fa-edit"></i>出售'+row.coin+'</a>';
            }

            actions.push(handler)
            return actions.join('');
        }
        /**
         * table conf
         * @type {{url: string, removeUrl: string, columns: [*]}}
         */
        var conf = {
            url: BASE_PATH+'/otc/findAdList',
            columns: [
                {
                    field: 'userNick',
                    title: '用户' ,
                    formatter: function (value, row, index) {
                        return ' <a href="#"><img class="user-head img-circle" style="width: 40px;height: 40px" src='+FILE_PATH+row.userAvatar+' alt=""></a><a href="">   '+value+'</a><span class="state ONLINE"></span>';
                    }
                }, {
                    field: 'trustNum',
                    align: 'center',
                    title: '信用',
                    formatter: function (value, row, index) {
                        return '<span>交易笔数:'+row.exchangeNum+'笔</span> | <span>信任人数:'+row.trustNum+'人</span>';
                    }
                }, {
                    field: 'payType',
                    align: 'center',
                    title: '付款方式',
                }, {
                    field: 'minMoney',
                    align: 'center',
                    title: '限额',
                    formatter: function (value, row, index) {
                        return '<span>'+value+'</span> - <span>'+row.maxMoney+'</span>  '+row.currency;
                    }
                }
                , {
                    field: 'price',
                    title: '价格',
                    align: 'right',
                    formatter: function (value, row, index) {
                        return value+' '+row.currency;
                    }
                }, {
                    title: '操作',
                    align: 'center',
                    formatter: actionFormatter
                }]
        };

        /**
         * 初始化
         */
        table.initTable($('#table'), conf);
    }

    /**
     * 我发布的项目
     */
    function userProject(){

        /**
         * 操作事件
         */
        window.operateEvents = {
            'click .edit': function (e, value, row, index) {

                window.location.href='/otc/user/project/update/'+row.id;
            },
            'click .open': function (e, value, row, index) {
                core.save({url:BASE_PATH+ "/otc/user/project/check",data:{id:row.id}}).then(function (data) {
                    if(data.success){
                        swal.swalBase(data.msg,"","success");
                    }else{
                        var errInfo="";
                        if( data.data!=undefined){
                            errInfo=data.data.join("");
                        }
                        swal.swalError(data.msg,errInfo);
                    }
                }).fail(function(jqXHR, textStatus){
                    swal.swalError(textStatus,jqXHR.status);
                    _option.callback({},form);
                });
            },'click .cancel': function (e, value, row, index) {
                core.save({url:BASE_PATH+ "/otc/user/project/cancel",data:{id:row.id}}).then(function (data) {
                    if(data.success){
                        swal.swalBase(data.msg,"","success");
                    }else{
                        var errInfo="";
                        if( data.data!=undefined){
                            errInfo=data.data.join("");
                        }
                        swal.swalError(data.msg,errInfo);
                    }
                }).fail(function(jqXHR, textStatus){
                    swal.swalError(textStatus,jqXHR.status);
                    _option.callback({},form);
                });
            },
            'click .remove': function (e, value, row, index) {
                var ids=new Array();
                ids.push(row.id);
                core.save({url:BASE_PATH+ "/otc/user/project/remove",data:{ids:row.id}}).then(function (data) {
                    if(data.success){
                        swal.swalBase(data.msg,"","success");
                    }else{
                        var errInfo="";
                        if( data.data!=undefined){
                            errInfo=data.data.join("");
                        }
                        swal.swalError(data.msg,errInfo);
                    }
                }).fail(function(jqXHR, textStatus){
                    swal.swalError(textStatus,jqXHR.status);
                    _option.callback({},form);
                });
            }
        };

        /**
         * 操作
         * @returns {string}
         */
        function actionFormatter(value, row, index) {
            var actions = new Array();
            var status="";
            switch (row.status) {
                case 0:
                    status = '<div class="pull-right ">' +
                        '<button class="open">' +
                        '<i class="fa fa fa-eye"></i>发布</button>';
                    break;
                case 1:
                    status = '<div class="pull-right ">' +
                        '<a class="cancel" href="javascript:void(0)" >' +
                        '<i class="fa fa-eye-slash"></i>关闭</a>';
                    break;

            }
              var  edit = '<div class="pull-right ">' +
                    '<a class="edit" href="javascript:void(0)" >' +
                    '<i class="fa fa-edit"></i>修改</a>';

            var remove = '<div class="pull-right ">' +
                '<a class="remove" href="javascript:void(0)" >' +
                '<i class="fa fa-trash-o"></i>删除</a>';
            actions.push(edit);
            actions.push(status);
            actions.push(remove);
            return actions.join('');
        }
        /**
         * table conf
         * @type {{url: string, removeUrl: string, columns: [*]}}
         */
        var conf = {
            url:BASE_PATH+ '/otc/user/projectData',
            columns: [
                {
                    field: 'projectNum',
                    align: 'center',
                    title: '广告编号',
                },{
                    field: 'otcType',
                    align: 'center',
                    title: '广告类型',
                    formatter: function (value, row, index) {
                        var type="";
                        if(row.otcType==0) type="出售";
                        if(row.otcType==1) type="购买";
                        return type+"("+row.coin+")";
                    }
                },{
                    field: 'country',
                    align: 'center',
                    title: '国家'
                }, {
                    field: 'price',
                    title: '价格',
                    align: 'right',
                    formatter: function (value, row, index) {
                        return value+' '+row.currency;
                    }
                },{
                    field: 'payType',
                    align: 'center',
                    title: '付款方式',
                }, {
                    field: 'minMoney',
                    align: 'center',
                    title: '限额',
                    formatter: function (value, row, index) {
                        return '<span>'+value+'</span> - <span>'+row.maxMoney+'</span>  '+row.currency;
                    }
                }, {
                    field: 'status',
                    align: 'center',
                    title: '状态',
                    formatter: function (value, row, index) {
                        switch (value) {
                            case 0:
                                return "未发布";
                                break;
                            case 1:
                                return "已发布";
                                break;
                            case 2:
                                return "已完成";
                                break;
                            case 3:
                                return "已关闭";
                                break;

                        }
                    }
                },{
                    field: 'createTime',
                    align: 'center',
                    title: '创建时间',
                    formatter:function(value, row, index){
                       return  U.dateFormat(value,"yyyy-MM-dd");
                    }
                }
                , {
                    title: '操作',
                    align: 'center',
                    events: operateEvents,
                    formatter: actionFormatter
                }]
        };

        /**
         * 初始化
         */
        table.initTable($('#table'), conf);
    }
    /**
     * 我的订单
     */
    function userOrder(){

        $("#order_search").find("li>a").each(function(e){
            $(this).on("click",function(){
                $("#order_search").find("li>a").each(function(){
                    $(this).removeClass("active");
                })
                $(this).addClass("active");

                var status=$(this).attr("data-status");
                $("#status").val(status);

                table.refresh($('#table'));
            })
        })


        /**
         * 操作
         * @returns {string}
         */
        function actionFormatter(value, row, index) {
            var actions = new Array();

            var currentStatus=$("#status").val();
            var  cancel = '<div class="pull-right ">' +
                '<a class="cancel" href="javascript:void(0)" >' +
                '取消</a>';

            var paySucess = '<div class="pull-right ">' +
                '<a class="paySucess" href="javascript:void(0)" >' +
                '已付款</a>';

            var orderSuccess = '<div class="pull-right ">' +
                '<a class="orderSuccess" href="javascript:void(0)" >' +
                '已完成</a>';

            if(currentStatus==0) {
                actions.push(cancel);
                actions.push(paySucess);
            }
            if(currentStatus==1) {
                actions.push(orderSuccess);
            }

            return actions.join('');
        }
        /**
         * table conf
         * @type {{url: string, removeUrl: string, columns: [*]}}
         */
        var conf = {
            url: BASE_PATH+'/otc/order/user/listData',
            columns: [
                {
                    field: 'orderNum',
                    align: 'center',
                    title: '订单编号',
                },{
                    field: 'orderType',
                    align: 'center',
                    title: '类型',
                    formatter: function (value, row, index) {
                        var type="";
                        if(value==0) type="出售";
                        if(value==1) type="购买";
                        return type+"（"+row.orderCoin+"）";
                    }
                },{
                    field: 'projectUserNick',
                    align: 'center',
                    title: '交易对象',
                    formatter: function (value, row, index) {

                        return "<a class='text-info' href='/otc/user/"+row.projectUserId+"'>"+value+"</a>";
                    }
                }, {
                    field: 'orderCount',
                    title: '数量',
                    align: 'right',
                    formatter: function (value, row, index) {
                        return value;
                    }
                },{
                    field: 'orderMoney',
                    align: 'right',
                    title: '金额',
                    formatter: function (value, row, index) {
                        return value+' '+row.currency;
                    }
                }, {
                    field: 'orderFee',
                    align: 'right',
                    title: '手续费'
                },{
                    field: 'createTime',
                    align: 'center',
                    title: '创建时间',
                    formatter:function(value, row, index){
                        return  U.dateFormat(value,"yyyy-MM-dd");
                    }
                },{
                    field: 'orderRemarkNum',
                    align: 'center',
                    title: '付款参考号'
                }, {
                    title: '操作',
                    align: 'center',
                   // events: operateEvents,
                    formatter: actionFormatter
                }]
        };

        /**
         * 初始化
         */
        table.initTable($('#table'), conf);
    }

    /**
     * 项目详情
     */
    function info(){
        var adId=$("#adId").val();
        core.list({url:BASE_PATH+ "/otc/infoData/"+adId}).then(function (data) {
            data.userAuth=core.isLogin();
            tpl.render({"tplId": "#infoTpl", "renderId": "#infoRender", "data": data});
            orderFormSubmit();
            return data;
        }).then(function (data) {
            core.get({url:BASE_PATH+ "/member/user/findBaseInfo/"+data.userId}).then(function (data) {
                tpl.render({"tplId": "#userTpl", "renderId": "#userRender", "data": data.data});
            })
        })


    }
    /**
     * 订单数据提交
     */
    function orderFormSubmit(){

// 表单校验规则
        var rules= {
            rules: {
                projectId: {
                    required: true,
                    maxlength:64,
                    number:true,

                },
                orderType: {
                    required: true,
                    maxlength:5,
                    number:true,

                },

                orderCoin: {
                    required: true,
                    maxlength:30,

                },
                currency: {
                    required: true,
                    maxlength:30,

                },


                orderMoney: {
                    required: true,
                    maxlength:20,
                    number:true,

                },
                orderCount: {
                    required: true,
                    maxlength:20,
                    number:true,

                }
            },
            messages: {
            }
        };
//表单提交
        mVad({
            forms:$('.form'),
            tip:'.form_tip',
            rules:rules,
            postData:function(data){

                return data;
            },
            formSubmit:function(_option,form){
                core.save(_option.ajaxData).then(function(data){
                    if(data.success){
                        swal.swalBase(data.msg,"","success");
                    }else{
                        var errInfo="";
                        if( data.data!=undefined){
                            errInfo=data.data.join("");
                        }
                        swal.swalError(data.msg,errInfo);
                    }
                    _option.callback(data,form);
                }).fail(function(jqXHR, textStatus){
                    swal.swalError(textStatus,jqXHR.status);
                    _option.callback({},form);
                });
            }
        });
    }

    /**
     * 数据提交
     */
    function formSubmit(){

// 表单校验规则
        var rules= {
            rules: {

                otcType: {
                    required: true,
                    maxlength:5,
                    number:true,

                },

                coin: {
                    required: true,
                    maxlength:30,

                },
                country: {
                    required: true,
                    maxlength:50,

                },
                currency: {
                    required: true,
                    maxlength:30,

                },

                price: {
                    required: true,
                    maxlength:20,
                    number:true,

                },
                exchangeName: {
                    required: true,
                    maxlength:50,

                },
                overflowMoney: {
                    maxlength:20,
                    number:true,

                },
                payType: {
                    required: true,
                    maxlength:30,

                },
                payRemarks: {
                    maxlength:255,

                },
                transaction: {
                    maxlength:255,

                },

                maxMoney: {
                    maxlength:20,
                    number:true,

                },
                minMoney: {
                    maxlength:20,
                    number:true,

                },

                status: {
                    maxlength:5,
                    number:true,

                },
                adress: {
                    maxlength:255,

                },
            },
            messages: {
            }
        };
//表单提交
        mVad({
            forms:$('.form'),
            tip:'.form_tip',
            rules:rules,
            postData:function(data){
                var fixed=$("#fixed").val();
                var automatic=$("#automatic").val();
                var belived=$("#belived").val();
                var security=$("#security").val();
                data.push({"name":"fixed","value":fixed=="on"?1:0});
                data.push({"name":"automatic","value":automatic=="on"?1:0});
                data.push({"name":"belived","value":belived=="on"?1:0});
                data.push({"name":"security","value":security=="on"?1:0});
                return data;
            },
            formSubmit:function(_option,form){
                core.save(_option.ajaxData).then(function(data){
                    if(data.success){
                        swal.swalBase(data.msg,"","success");
                    }else{
                        var errInfo="";
                        if( data.data!=undefined){
                            errInfo=data.data.join("");
                        }
                        swal.swalError(data.msg,errInfo);
                    }
                    _option.callback(data,form);
                }).fail(function(jqXHR, textStatus){
                    swal.swalError(textStatus,jqXHR.status);
                    _option.callback({},form);
                });
            }
        });
    }

    /**
     * 我的账户
     */
    function userFinance(){
        core.list({url:BASE_PATH+ "/otc/user/financeData"}).then(function (data) {
            tpl.render({"tplId": "#financeTpl", "renderId": "#financeRender", "data": data.data});
        })
    }
    /**
     * 充值
     */
    function recharge(){
        /**
         * 加载币种信息
         */
        core.list({url:BASE_PATH+ "/otc/coins"}).then(function (data) {

            tpl.render({"tplId": "#coinsTpl", "renderId": "#coinsRender", "data": data});
        })
    }
    /**
     * 提币
     */
    function deposit(){

    }
    /**
     * otc list
     */
    function init() {


    }



    exports.list=list;
    exports.init = init;
    exports.add = add;
    exports.index=index;
    exports.info=info;
    exports.user=user;
    exports.userProject=userProject;
    exports.userOrder=userOrder;
    exports.userFinance=userFinance;
    exports.recharge=recharge;
    exports.deposit=deposit;
});