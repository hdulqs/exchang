function Kline(){}Kline.prototype={browerState:0,klineWebsocket:null,klineTradeInit:!1,tradeDate:new Date,tradesLimit:100,lastDepth:null,depthShowSize:15,priceDecimalDigits:6,amountDecimalDigits:4,symbol:null,curPrice:null,title:"",reset:function(a){this.refreshUrl(a),this.symbol=a,this.lastDepth=null,this.curPrice=null,this.klineTradeInit=!1,$("#gasks .table").empty(),$("#gbids .table").empty(),$("#asks .table").empty(),$("#bids .table").empty(),this.websocketRedister(a)},setTitle:function(){document.title=(null==this.curPrice?"":this.curPrice+" ")+this.title},dateFormatTf:function(a){return(a<10?"0":"")+a},dateFormat:function(a){return a.getFullYear()+"-"+this.dateFormatTf(a.getMonth()+1)+"-"+this.dateFormatTf(a.getDate())+" "+this.dateFormatTf(a.getHours())+":"+this.dateFormatTf(a.getMinutes())+":"+this.dateFormatTf(a.getSeconds())},dateInit:function(b){var a=new Date;b&&a.setTime(b),$(".m_rightbot").text(this.dateFormat(a));var c=this;setInterval(function(){a.setTime(a.getTime()+1e3),$(".m_rightbot").text(c.dateFormat(a))},1e3)},websocketRedister:function(symbol){},pushTrades:function(l){},updateDepth:function(e){if(window._set_current_depth(e),e)if($("#gasks .table").html(this.getgview(this.getgasks(e.asks))),$("#gbids .table").html(this.getgview(this.getgbids(e.bids))),null==this.lastDepth)this.lastDepth={},this.lastDepth.asks=this.getAsks(e.asks,this.depthShowSize),this.depthInit(this.lastDepth.asks,$("#asks .table")),this.lastDepth.bids=this.getBids(e.bids,this.depthShowSize),this.depthInit(this.lastDepth.bids,$("#bids .table"));else{var b=$("#asks .table");b.find("div.remove").remove(),b.find("div.add").removeClass("add");var f=this.getAsks(e.asks,this.depthShowSize),a=this.lastDepth.asks;this.lastDepth.asks=f,this.asksAndBids(f.slice(0),a,b);var d=$("#bids .table");d.find("div.remove").remove(),d.find("div.add").removeClass("add");var g=this.getBids(e.bids,this.depthShowSize),c=this.lastDepth.bids;this.lastDepth.bids=g,this.asksAndBids(g.slice(0),c,$("#bids .table"))}},depthInit:function(f,h){if(h.empty(),f&&0<f.length){for(var g,b="",e=0;e<f.length;e++){var a=(f[e][0]+"").split("."),d=this.getPrice(a,g);g=a[0],a=(f[e][1]+"").split(".");var c=this.getAmount(a);b+="<div class='row'><span class='price'>"+d[0]+"<g>"+d[1]+"</g></span> <span class='amount'>"+c[0]+"<g>"+c[1]+"</g></span></div>"}h.append(b),b=null}},asksAndBids:function(b,c,l){for(var f=0;f<c.length;f++){for(var n=!1,d=0;d<b.length;d++)if(c[f][0]==b[d][0]){if(n=!0,c[f][1]!=b[d][1]){var a=l.find("div:eq("+f+") .amount");a.addClass(c[f][1]>b[d][1]?"red":"green");var g=this.getAmount((b[d][1]+"").split("."));setTimeout(function(j,i){return function(){j.html(i[0]+"<g>"+i[1]+"</g>"),j.removeClass("red").removeClass("green"),i=j=null}}(a,g),500)}b.splice(d,1);break}n||(l.find("div:eq("+f+")").addClass("remove"),c[f][2]=-1)}for(d=0;d<c.length;d++)for(f=0;f<b.length;f++)if(b[f][0]>c[d][0]){var k=(b[f][1]+"").split(".");g=this.getAmount(k);l.find("div:eq("+d+")").before("<div class='row add'><span class='price'></span> <span class='amount'>"+g[0]+"<g>"+g[1]+"</g></span></div>"),c.splice(d,0,b[f]),b.splice(f,1);break}var m,i,j,h="";for(f=0;f<b.length;f++){c.push(b[f]);k=(b[f][1]+"").split(".");h+="<div class='row add'><span class='price'></span> <span class='amount'>"+(g=this.getAmount(k))[0]+"<g>"+g[1]+"</g></span></div>"}0<h.length&&l.append(h),h=null;for(f=0;f<c.length;f++){var o=l.find("div:eq("+f+")");if(!(3<=c[f].length&&-1==c[f][2])){k=(c[f][0]+"").split(".");var e=this.getPrice(k,m);m=k[0],o.find(".price").html(e[0]+"<g>"+e[1]+"</g>")}}c=b=null,l.find("div.add").slideDown(800),setTimeout((i=l.find("div.remove"),j=l.find("div.add"),function(){i.slideUp(500,function(){$(this).remove()}),j.removeClass("add")}),1e3)},getAsks:function(b,a){return b.length>a&&b.splice(0,b.length-a),b},getBids:function(b,a){return b.length>a&&b.splice(a,b.length-1),b},getgview:function(c){for(var e,d="",b=0;b<c.length;b++){var a=c[b][0].split(".");1==a.length||a[0]!=e?(d+="<div class='row'><span class='price'>"+c[b][0]+"</span> <span class='amount'>"+c[b][1]+"</span></div>",e=a[0]):d+="<div class='row'><span class='price'><h>"+a[0]+".</h>"+a[1]+"</span> <span class='amount'>"+c[b][1]+"</span></div>"}return d},getgasks:function(j){var k=j[j.length-1][0],e=j[0][0],a=e-k,d=this.getBlock(a,100),b=Math.abs(Number(Math.log(d)/Math.log(10))).toFixed(0);a/d<2&&(d/=2,b++),1<=d&&(b=0),k=parseInt(k/d)*d,e=parseInt(e/d)*d;for(var h=[],g=0,f=j.length-1;0<=f;f--){if(j[f][0]>k){var c=parseInt(g,10);if(0<c&&h.unshift([Number(k).toFixed(b),c]),e<=k)break;k+=d}g+=j[f][1]}return h},getgbids:function(j){var k=j[j.length-1][0],e=j[0][0],a=e-k,d=this.getBlock(a,100),b=Math.abs(Number(Math.log(d)/Math.log(10))).toFixed(0);a/d<2&&(d/=2,b++),1<=d&&(b=0),k=parseInt(k/d)*d,e=parseInt(e/d)*d;for(var h=[],g=0,f=0;f<j.length;f++){if(j[f][0]<e){var c=parseInt(g,10);if(0<c&&h.push([Number(e).toFixed(b),c]),e<=k)break;e-=d}g+=j[f][1]}return h},getBlock:function(a,c){return c<a?c:(c/=10,this.getBlock(a,c))},getZeros:function(b){for(var a="";0<b;)b--,a+="0";return a},getPrice:function(a,d){var c=a[0];d==c?c="<h>"+c+".</h>":c+=".";var b="";return b=1==a.length?(c+="0",this.getZeros(this.priceDecimalDigits-1)):(c+=a[1],this.getZeros(this.priceDecimalDigits-a[1].length)),[c,b]},getAmount:function(a){var c=a[0],b="",d=this.amountDecimalDigits-c.length+1;return 0<d&&(b=".",1==a.length?b+=this.getZeros(d):d>a[1].length?b+=a[1]+this.getZeros(d-a[1].length):d==a[1].length?b+=a[1]:b+=a[1].substring(0,d)),[c,b]},setTopTickers:function(c){},setMarketShow:function(e,b,d,c){},refreshPage:function(a){},refreshUrl:function(a){}};