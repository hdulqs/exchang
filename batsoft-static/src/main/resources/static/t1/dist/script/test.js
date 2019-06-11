var order,timeout=5e3;function orderConnect(){order&&Close(order);try{order=new WebSocket($("#ws_url").val()+"all@order")}catch(e){return void setTimeout(function(){orderConnect()},timeout)}order.onopen=orderOpen,order.onerror=orderError,order.onmessage=orderMessage,order.onclose=orderClose}function orderOpen(){null==Loading.order&&(Loading.order=1)}function orderError(e){}function orderMessage(msg){if(null==msg)return!1;var i=JSON.parse(msg.data);i[1]&&init_order_data(i)}function orderClose(e){setTimeout(function(){orderConnect()},timeout)}function orderSend(){}function Close(ws){ws.close()}function blind_date(start_id,end_id){var date_lang="cn";0==$("#languageBt").hasClass("cn")&&(date_lang="en"),laydate.render({elem:"#"+start_id,lang:date_lang,done:function(value){var end_date=$("#"+end_id).val();if(0<end_date.length&&-1==compare_date(value,end_date))return $("#"+start_id).val(""),!1}}),laydate.render({elem:"#"+end_id,lang:date_lang,done:function(value){var start_date=$("#"+start_id).val();if(0<start_date.length&&-1==compare_date(start_date,value))return $("#"+end_id).val(""),!1}})}function current_entrust(){$.ajax({type:"GET",url:"/trade/entrust",data:{type:1},dataType:"json",success:function(result_json){if(0!=result_json.code)return!1;var list=result_json.data.list;if(0==list.length)return $("#current_entrust .areaTable .no_data_tpl").show(),!1;$("#current_entrust .areaTable .no_data_tpl").hide();var html="";for(var key in list)html+=build_current_entrust_html(list[key]);var obj_html=$(html);obj_html.find("*[timestamp]").each(function(){var timestamp=$(this).attr("timestamp");$(this).text(new Date(1e3*timestamp).format("yyyy-MM-dd hh:mm:ss"))}),$("#current_entrust .areaTable table tbody").html(obj_html),hide_other_pairs()}})}function history_entrust(){var start_time,end_time,dateType=$("#history_entrust_search").data("dateType"),p=$("#history_entrust_search").data("p"),start_date=$("#history_entrust_start_date").val(),end_date=$("#history_entrust_end_date").val();switch(parseInt(dateType)){case 1:start_time=(result_days=getNowDiffDays(1)).start_time,end_time=result_days.end_time;break;case 2:start_time=(result_days=getNowDiffDays(7)).start_time,end_time=result_days.end_time;break;case 3:start_time=(result_days=getNowDiffDays(30)).start_time,end_time=result_days.end_time;break;case 4:var result_days;start_time=(result_days=getNowDiffDays(90)).start_time,end_time=result_days.end_time;break;case 5:0==is_null(start_date)&&0<start_date.length&&(start_time=new Date(start_date.replace(/-/g,"/")+" 00:00:00").getTime()/1e3),0==is_null(end_date)&&0<end_date.length&&(end_time=new Date(end_date.replace(/-/g,"/")+" 23:59:59").getTime()/1e3)}1==is_null(p)&&(p=0),1==(p+=1)&&$("#history_entrust .areaTable table tbody tr").remove(),$("#history_entrust_search").data("p",p),$.ajax({type:"GET",url:"/trade/entrust",data:{type:2,dateType:dateType,start_time:start_time,end_time:end_time,p:p},dataType:"json",beforeSend:function(){$("#history_entrust_search").data("is_requesting",1)},success:function(result_json){if(1==p&&$("#history_entrust .areaTable table tbody tr").remove(),0!=result_json.code)return!1;var list=result_json.data.list;if(0==list.length)return 1==p&&$("#history_entrust .areaTable .no_data_tpl").show(),!1;$("#history_entrust .areaTable .no_data_tpl").hide();var html="";for(var key in list)html+=build_history_entrust_html(list[key]);var obj_html=$(html);obj_html.find("*[timestamp]").each(function(){var timestamp=$(this).attr("timestamp");$(this).text(new Date(1e3*timestamp).format("yyyy-MM-dd hh:mm:ss"))}),$("#history_entrust .areaTable table tbody").append(obj_html),hide_other_pairs()},complete:function(){$("#history_entrust_search").data("is_requesting",0)}})}function history_trade(){var start_time,end_time,dateType=$("#history_trade_search").data("dateType"),p=$("#history_trade_search").data("p"),start_date=$("#history_trade_start_date").val(),end_date=$("#history_trade_end_date").val();switch(parseInt(dateType)){case 1:start_time=(result_days=getNowDiffDays(1)).start_time,end_time=result_days.end_time;break;case 2:start_time=(result_days=getNowDiffDays(7)).start_time,end_time=result_days.end_time;break;case 3:start_time=(result_days=getNowDiffDays(30)).start_time,end_time=result_days.end_time;break;case 4:var result_days;start_time=(result_days=getNowDiffDays(90)).start_time,end_time=result_days.end_time;break;case 5:0==is_null(start_date)&&0<start_date.length&&(start_time=new Date(start_date.replace(/-/g,"/")+" 00:00:00").getTime()/1e3),0==is_null(end_date)&&0<end_date.length&&(end_time=new Date(end_date.replace(/-/g,"/")+" 23:59:59").getTime()/1e3)}1==is_null(p)&&(p=0),1==(p+=1)&&$("#history_trade .areaTable table tbody tr").remove(),$("#history_trade_search").data("p",p),$.ajax({type:"GET",url:"/trade/history_trade",data:{dateType:dateType,start_time:start_time,end_time:end_time,p:p},dataType:"json",beforeSend:function(){$("#history_trade_search").data("is_requesting",1)},success:function(result_json){if(1==p&&$("#history_trade .areaTable table tbody tr").remove(),0!=result_json.code)return!1;var list=result_json.data.list;if(0==list.length)return 1==p&&$("#history_trade .areaTable .no_data_tpl").show(),!1;$("#history_trade .areaTable .no_data_tpl").hide();var html="";for(var key in list)html+=build_history_trade_html(list[key]);var obj_html=$(html);obj_html.find("*[timestamp]").each(function(){var timestamp=$(this).attr("timestamp");$(this).text(new Date(1e3*timestamp).format("yyyy-MM-dd hh:mm:ss"))}),$("#history_trade .areaTable table tbody").append(obj_html),hide_other_pairs()},complete:function(){$("#history_trade_search").data("is_requesting",0)}})}function asset_manage(){$.ajax({type:"GET",url:"/trade/asset_manage",dataType:"json",success:function(result_json){if(0!=result_json.code)return!1;var list=result_json.data.list;if(0==list.length)return $("#asset_manage .areaTable .no_data_tpl").show(),!1;$("#asset_manage .areaTable .no_data_tpl").hide();var html="";for(var key in list)html+=build_asset_manage_html(list[key]);$("#asset_manage .areaTable table tbody").html(html),asset_guzhi(null)}})}function build_current_entrust_html(data){var pairs=data.base_asset+"_"+data.quote_asset;return'<tr oid="'+data.order_id+'"><td width="1%">&nbsp;</td><td width="16%"><span timestamp="'+data.created_at_timestamp+'"></span></td><td width="12%">'+data.base_asset+"/"+data.quote_asset+'</td><td width="8%">'+Lang["order_type"+data.type]+'</td><td width="8%"><span class="'+{1:"green",2:"red"}[data.side]+'">'+Lang["side"+data.side]+'</span></td><td width="9%">'+formatKKcoin(data.price,PairsDecimal[pairs].price_decimal)+'</td><td width="12%">'+formatKKcoin(data.origin_amount,PairsDecimal[pairs].amt_decimal)+'</td><td width="12%">'+getPercent(bcdiv(data.executed_amount,data.origin_amount))+'</td><td width="12%">'+formatKKcoin(data.executed_quote_amount,PairsDecimal[pairs].amount_decimal)+'</td><td width="10%"><a href="javascript:void(0)" class="button" onclick="cancel_order(this,\''+data.order_id+"');\">"+Lang.cancel+"</a></td></tr>"}function build_history_entrust_html(data){var pairs=data.base_asset+"_"+data.quote_asset;return'<tr><td width="10%" class="tc"><span timestamp="'+data.created_at_timestamp+'"></span></td><td width="10%" class="tc">'+data.base_asset+"/"+data.quote_asset+'</td><td width="10%" class="tc">'+Lang["order_type"+data.type]+'</td><td width="10%" class="tc"><span class="'+{1:"green",2:"red"}[data.side]+'">'+Lang["side"+data.side]+'</span></td><td width="10%" class="tc">'+formatKKcoin(data.executed_price,PairsDecimal[pairs].price_decimal)+'</td><td width="10%" class="tc">'+formatKKcoin(data.price,PairsDecimal[pairs].price_decimal)+'</td><td width="10%" class="tc">'+formatKKcoin(data.executed_amount,PairsDecimal[pairs].amt_decimal)+'</td><td width="10%" class="tc">'+formatKKcoin(data.origin_amount,PairsDecimal[pairs].amt_decimal)+'</td><td width="10%" class="tc">'+formatKKcoin(data.executed_quote_amount,PairsDecimal[pairs].amount_decimal)+'</td><td width="10%" class="tc">'+Lang["order_status"+data.status]+"</td></tr>"}function build_history_trade_html(data){var pairs=data.base_asset+"_"+data.quote_asset;return'<tr><td width="14%" class="tc"><span timestamp="'+data.created_at_timestamp+'"></span></td><td width="14%" class="tc">'+data.base_asset+"/"+data.quote_asset+'</td><td width="14%" class="tc"><span class="'+{1:"green",2:"red"}[data.side]+'">'+Lang["side"+data.side]+'</span></td><td width="14%" class="tc">'+formatKKcoin(data.price,PairsDecimal[pairs].price_decimal)+'</td><td width="14%" class="tc">'+formatKKcoin(data.amount,PairsDecimal[pairs].amt_decimal)+'</td><td width="14%" class="tc">'+formatKKcoin(data.quote_amount,PairsDecimal[pairs].amount_decimal)+'</td><td width="16%" class="tc">'+formatKKcoin(data.fee,PairsDecimal[pairs].amount_decimal)+data.fee_asset+"</td></tr>"}function build_asset_manage_html(data){return'<tr><td width="20%">'+data.asset_symbol+'</td><td width="20%" class="bal">'+formatKKcoin(rtrim_zero(data.bal))+'</td><td width="20%">'+formatKKcoin(rtrim_zero(data.available_bal))+'</td><td width="20%">'+formatKKcoin(rtrim_zero(data.frozen_bal))+'</td><td width="20%" class="guzhi" symbol="'+data.asset_symbol+'"></td></tr>'}function hide_other_pairs(){if(1==$(".toggleBt").hasClass("item_hide")){var current_pairs=$.trim($("#current_pairs .coin_select").text());current_pairs=current_pairs.replace(" ",""),$("#mainBottom .areaTable tr").hide(),$("#mainBottom .areaTable tr:contains('"+current_pairs+"')").show()}else $("#mainBottom .areaTable tr").show()}function cancel_order(obj,order_id){if(1==$(obj).hasClass("lock"))return!1;$.ajax({type:"POST",url:"/entrust/cancel",async:!0,data:{order_id:order_id,_token:$("meta[name='csrf-token']").attr("content")},dataType:"json",beforeSend:function(){$(obj).addClass("lock")},success:function(result){if($(obj).removeClass("lock"),0<result.code)return layer.alert(result.msg,{title:Lang.alert_title,btn:Lang.confirm_button,closeBtn:0},function(index){layer.close(index),null!=result.url&&(window.location=result.url)}),!1;layer.msg(Lang.cancel_success,{anim:5,time:2e3},function(){$(obj).parents(".areaTable tr").remove()})}})}function all_cancel(obj){return 0!=$("#current_entrust .areaTable tr[oid]").length&&(1!=$(obj).hasClass("lock")&&void layer.confirm(Lang.cancel_confirm,{icon:3,title:Lang.confirm_title,button:[Lang.confirm_button,Lang.cancel_button],closeBtn:0},function(index){layer.close(index),$.ajax({type:"GET",url:"/entrust/cancel",async:!0,dataType:"json",beforeSend:function(){$(obj).addClass("lock")},success:function(result){if($(obj).removeClass("lock"),0<result.code)return layer.alert(result.msg,{title:Lang.alert_title,btn:Lang.confirm_button,closeBtn:0},function(index){layer.close(index),null!=result.url&&(window.location=result.url)}),!1;$("#current_entrust .areaTable table tbody tr").remove()},complete:function(){$(obj).removeClass("lock")}})}))}function asset_guzhi(aset_symbol){var rate=Number($("#exchange_rate").val()),lang=getCookie("lang");null==aset_symbol||null==aset_symbol||""==aset_symbol?$("#asset_manage .areaTable .guzhi[symbol]").each(function(){var symbol=$(this).attr("symbol"),tr=$(this).parents("#asset_manage .areaTable table tr"),bal=unFormatKKcoin(tr.find(".bal").text());if("en"==lang)var guzhi=bal*againstCNY(symbol)/rate;else guzhi=bal*againstCNY(symbol);$(this).text(formatKKcoin(guzhi.toFixed(2)))}):$("#asset_manage .areaTable .guzhi[symbol='"+symbol+"']").each(function(){var symbol=$(this).attr("symbol"),tr=$(this).parents("#asset_manage .areaTable table tr"),bal=unFormatKKcoin(tr.find(".bal").text());if("en"==lang)var guzhi=bal*againstCNY(symbol)/rate;else guzhi=bal*againstCNY(symbol);$(this).text(formatKKcoin(guzhi.toFixed(2)))})}function trigger_asset_entrust(){return!1}function init_order_data(i){var pairs=i[1].split("_"),data={order_id:i[0],created_at_timestamp:bcdiv(i[10],1e3),base_asset:pairs[0],quote_asset:pairs[1],type:i[2],side:i[3],price:i[4],origin_amount:i[5],executed_amount:i[7],executed_quote_amount:i[8],status:i[9]},obj=$("#current_entrust .areaTable tr[oid='"+data.order_id+"']");if(2==data.status||4==data.status)return obj.remove(),0==$("#current_entrust .areaTable tr").length&&$("#current_entrust .no_data_tpl").show(),bal(),!1;if($("#current_entrust .no_data_tpl").hide(),3==data.status&&0<obj.length)obj.find("td:eq(7)").text(getPercent(bcdiv(data.executed_amount,data.origin_amount))),obj.find("td:eq(8)").text(data.executed_quote_amount+data.quote_asset);else if(1==data.status){var html=build_current_entrust_html(data),obj_html=$(html);obj_html.find("*[timestamp]").each(function(){var timestamp=$(this).attr("timestamp");$(this).text(new Date(1e3*timestamp).format("yyyy-MM-dd hh:mm:ss"))}),$("#current_entrust .areaTable table tbody").prepend(obj_html),hide_other_pairs()}bal()}function getNowDiffDays(day){var result={},now_date=(new Date).format("yyyy/MM/dd"),limit_timestamp=new Date(now_date+" 23:59:59").getTime()/1e3;return result.start_time=limit_timestamp-86400*day+1,result.end_time=limit_timestamp,result}$(function(){$(document).on("click","#mainBottom .t_menu li",function(){var dataType=$(this).attr("dataType");if($("#mainBottom .t_menu li").removeClass("cur"),$(this).addClass("cur"),$("#mainBottom .t_cont").hide(),1==dataType)$("#current_entrust").show(),$(".toggleBt").show();else if(2==dataType){$("#history_entrust").show(),$(".toggleBt").show();var dateType=$("#history_entrust_search .h_bt.curr").attr("dateType");$("#history_entrust_search").data("dateType",dateType),$("#history_entrust_search").data("p",0),history_entrust()}else if(3==dataType){$("#history_trade").show(),$(".toggleBt").show();dateType=$("#history_trade_search .h_bt.curr").attr("dateType");$("#history_trade_search").data("dateType",dateType),$("#history_trade_search").data("p",0),history_trade()}else 4==dataType&&($("#asset_manage").show(),$(".toggleBt").hide(),asset_manage())}),current_entrust(),blind_date("history_entrust_start_date","history_entrust_end_date"),$(document).on("click","#history_entrust span.h_bt[dateType]",function(){if(1==$("#history_entrust_search").data("is_requesting"))return!1;$("#history_entrust span.h_bt[dateType]").removeClass("curr"),$(this).addClass("curr");var dateType=$(this).attr("dateType");$("#history_entrust_search").data("dateType",dateType),$("#history_entrust_search").data("p",0),history_entrust()}),$(document).on("click","#history_entrust .s_bt",function(){if(1==$("#history_entrust_search").data("is_requesting"))return!1;var start_date=$.trim($("#history_entrust_start_date").val()),end_date=$.trim($("#history_entrust_end_date").val());if(0==start_date.length||0==end_date.length)return!1;$("#history_entrust_search").data("dateType",5),$("#history_entrust_search").data("p",0),history_entrust()}),$("#history_entrust .scrollStyle").scroll(function(){var scroll_top=$("#history_entrust .scrollStyle")[0].scrollTop;if($("#history_entrust .scrollStyle")[0].scrollHeight-50<=scroll_top+$("#history_entrust .scrollStyle").height()){if(1==$("#history_entrust_search").data("is_requesting"))return!1;history_entrust()}}),blind_date("history_trade_start_date","history_trade_end_date"),$(document).on("click","#history_trade span.h_bt[dateType]",function(){if(1==$("#history_trade_search").data("is_requesting"))return!1;$("#history_trade span.h_bt[dateType]").removeClass("curr"),$(this).addClass("curr");var dateType=$(this).attr("dateType");$("#history_trade_search").data("dateType",dateType),$("#history_trade_search").data("p",0),history_trade()}),$(document).on("click","#history_trade .s_bt",function(){if(1==$("#history_trade_search").data("is_requesting"))return!1;var start_date=$.trim($("#history_trade_start_date").val()),end_date=$.trim($("#history_trade_end_date").val());if(0==start_date.length||0==end_date.length)return!1;$("#history_trade_search").data("dateType",5),$("#history_trade_search").data("p",0),history_trade()}),$("#history_trade .scrollStyle").scroll(function(){var scroll_top=$("#history_trade .scrollStyle")[0].scrollTop;if($("#history_trade .scrollStyle")[0].scrollHeight-50<=scroll_top+$("#history_trade .scrollStyle").height()){if(1==$("#history_trade_search").data("is_requesting"))return!1;history_trade()}}),$(".toggleBt").on("click",function(){$(this).hasClass("item_hide")?($(this).removeClass("item_hide"),$(this).text(Lang.hide_other_pairs)):($(this).addClass("item_hide"),$(this).text(Lang.show_other_pairs)),hide_other_pairs()})});