!function($){function addClickEvent(){var runClick=function(self){var data_symbol=$(self).attr("data-symbol");if(data_symbol&&data_symbol.indexOf("_")>-1){var arr=data_symbol.split("_");if(2===arr.length)window.location.href="/spot/trade/index.do#symbol="+arr[0]+"_"+arr[1],location.pathname.indexOf("/spot/trade/index.do")>-1&&location.reload();else if(3===arr.length){var symbol="BTC"===arr[0].toUpperCase()?"Btc":"Ltc",contractId=$(self).attr("data-contract");$.cookie("treatyType_"+symbol,contractId);var key="BTC"===arr[0].toUpperCase()?0:1;window.location.href="/future/future.do?symbol="+key}}else $("#coinListBox").hide()};$("#coinListBox table").on("click","tr",function(){runClick(this)}).on("dblclick","tr",function(){runClick(this)})}var isNoLoad="/"===location.pathname,futureBannerTicker={priceDefine:{futures_cny:1,futures_usd:2},symbolDefine:{0:"BTC",1:"LTC",2:"ETH",4:"ETC",5:"BCH"},allFuturesArr:[],refreshSelectedTickersByFutures:function(item){var cls=item.changePercent.toString().indexOf("-")>-1?"red":"green",clsArrow=item.changePercent.toString().indexOf("-")>-1?"banner-arrow-down":"banner-arrow-up",current_symbol=$("#current_symbol").val().toString(),priceTruncate="0"===current_symbol?this.priceDefine.futures_usd:this.priceDefine.futures_cny,$selectedItem=$("#bannerTickerFutures"),$name=null,$sign=null,$price=null,$arrow=null;0===item.symbol?($name=$selectedItem.find("span").eq(0),$sign=$selectedItem.find("i").eq(0),$price=$selectedItem.find("span").eq(1),$arrow=$selectedItem.find("i").eq(1)):1===item.symbol&&($name=$selectedItem.find("span").eq(2),$sign=$selectedItem.find("i").eq(2),$price=$selectedItem.find("span").eq(3),$arrow=$selectedItem.find("i").eq(3)),$selectedItem&&$selectedItem.length&&($sign.attr("class",cls),$name.html(languageJson.bannerTickerQuarter+this.symbolDefine[item.symbol]+item.contractId.toString().substr(4,4)+":"),"0"===current_symbol?$price.html(Calculate.ShowDownTruncation(item.last,priceTruncate)).attr("class",cls):$price.html(Calculate.ShowDownTruncation(Calculate.accMul_z(item.last,item.usdCnyRate),priceTruncate)).attr("class",cls),$arrow.attr("class",clsArrow))},refreshPanelTickersByFutures:function(list,isBatch){if(0!==list.length){var current_symbol=$("#current_symbol").val().toString(),_this=this,baseCurr="0"===current_symbol?"USD":"CNY",sign="0"===current_symbol?"$":"￥",priceTruncate="0"===current_symbol?this.priceDefine.futures_usd:this.priceDefine.futures_cny;if(!isBatch){if(list.sort(function(v1,v2){return v2.contractType-v1.contractType}),0===this.allFuturesArr.length)return void(this.allFuturesArr=list);this.allFuturesArr.length>0&&this.allFuturesArr.length<6&&(1===list[0].symbol?list=list.concat(this.allFuturesArr):0===list[0].symbol&&(list=this.allFuturesArr.concat(list)),this.allFuturesArr=list)}var allRowContent="";$.each(list,function(i,item){var desc=item.contractType<4?2===item.contractType?languageJson.bannerTickerNextWeek:languageJson.bannerTickerWeek:languageJson.bannerTickerQuarter;desc+=_this.symbolDefine[item.symbol]+item.contractId.toString().substr(4,4);var unit=languageJson.bannerTickerUnit,tradeCurr=_this.symbolDefine[item.symbol],color=item.changePercent.toString().indexOf("-")>-1?"red":"green-new",arrow=item.changePercent.toString().indexOf("-")>-1?"banner-arrow-down":"banner-arrow-up",vol=Calculate.formatNumber(item.volume),changePercent=Calculate.ShowDownTruncation(item.changePercent,2);if(changePercent.toString().indexOf("-")===-1&&(changePercent=changePercent.toString().indexOf("+")>-1?changePercent.toString():"+"+changePercent.toString()),"0"===current_symbol)var last=Calculate.ShowDownTruncation(item.last,priceTruncate),high=Calculate.ShowDownTruncation(item.high,priceTruncate),low=Calculate.ShowDownTruncation(item.low,priceTruncate);else var last=Calculate.ShowDownTruncation(Calculate.accMul_z(item.last,item.usdCnyRate),priceTruncate),high=Calculate.ShowDownTruncation(Calculate.accMul_z(item.high,item.usdCnyRate),priceTruncate),low=Calculate.ShowDownTruncation(Calculate.accMul_z(item.low,item.usdCnyRate),priceTruncate);if(isBatch)return allRowContent+="<tr data-symbol="+(tradeCurr+"_"+baseCurr+"_"+item.contractType).toLowerCase()+' data-desc="'+desc+'" data-contract='+item.contractId+' class="coinRow bannerFutureCoinRow"><td id="bannerCoin" class="coinCell">'+desc+'：</td><td id="bannerLast" style="width:148px;" class="coinCell '+color+'"><em class="mar-right2">'+sign+'</em><span class="bannerLast-inner">'+last+'</span><em class="'+arrow+'"></em></td><td id="bannerHigh"><em class="mar-right2">'+sign+"</em><span>"+high+'</span></td><td id="bannerLow"><em class="mar-right2">'+sign+"</em><span>"+low+'</span></td><td id="bannerChangePercentage" class="'+color+'"><span>'+changePercent+'</span><em>%</em></td><td id="bannerDeal"><span>'+vol+'</span><em class="mar-left5 gray1 vol-currency">'+unit+"</em></td></tr>",void(i==list.length-1&&($(".bannerFutureCoinRow").remove(),$('#coinListBox table[data-key="futures"] tbody').append(allRowContent)));var $row=$('#coinListBox table[data-key="futures"] tr[data-symbol='+(tradeCurr+"_"+baseCurr+"_"+item.contractType).toLowerCase()+"]");if($row&&$row.length)$row.find("#bannerCoin").html(desc+"："),$row.find("#bannerLast").attr("class","coinCell "+color).find(".mar-right2").html(sign).next().html(last).next().attr("class",arrow),$row.find("#bannerHigh").find("em").html(sign).next().html(high),$row.find("#bannerLow").find("em").html(sign).next().html(low),$row.find("#bannerChangePercentage").attr("class",color).find("span").html(changePercent).next().html("%"),$row.find("#bannerDeal").find("span").html(vol).next().html(unit);else{var $futures=$('#coinListBox table[data-key="futures"] tbody');$futures.append("<tr data-symbol="+(tradeCurr+"_"+baseCurr+"_"+item.contractType).toLowerCase()+' data-desc="'+desc+'" data-contract='+item.contractId+' class="coinRow"><td id="bannerCoin" class="coinCell">'+desc+'：</td><td id="bannerLast" class="coinCell '+color+'"><em class="mar-right2">'+sign+'</em><span class="bannerLast-inner">'+last+'</span><em class="'+arrow+'"></em></td><td id="bannerHigh"><em class="mar-right2">'+sign+"</em><span>"+high+'</span></td><td id="bannerLow"><em class="mar-right2">'+sign+"</em><span>"+low+'</span></td><td id="bannerChangePercentage" class="'+color+'"><span>'+changePercent+'</span><em>%</em></td><td id="bannerDeal"><span>'+vol+'</span><em class="mar-left5 gray1 vol-currency">'+unit+"</em></td></tr>")}})}},refreshFutures:function(list){if(0!==list.length){for(var i=0;i<list.length;i++){var ticker=list[i];if(4===ticker.contractType){this.refreshSelectedTickersByFutures(ticker);break}}this.refreshPanelTickersByFutures(list)}},refreshFuturesDataSource:function(){var _this=this;$.get("/future/api/inner/ticker.do?symbol=-1",function(data){futureBannerTicker.refreshPanelTickersByFutures(data.ticker,!0),window.refreshFutureContractList&&window.refreshFutureContractList(data.ticker),$.each(data.ticker,function(index,item){var isBtcOrLtc=0===item.symbol||1===item.symbol,canUpdate=isBtcOrLtc&&4===item.contractType;canUpdate&&_this.refreshSelectedTickersByFutures(item)})},"JSON")}};addClickEvent(),futureBannerTicker.refreshFuturesDataSource(),isNoLoad||setInterval(function(){futureBannerTicker.refreshFuturesDataSource()},2e4),"function"==typeof define&&define.amd?define([],function(){return futureBannerTicker}):window.futureBannerTicker=futureBannerTicker}(jQuery);