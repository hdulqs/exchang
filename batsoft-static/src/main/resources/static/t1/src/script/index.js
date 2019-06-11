define(function(require, exports, module) {

  (function() {
    !function(t,e){"object"==typeof exports&&"undefined"!=typeof module?e(exports):"function"==typeof define&&define.amd?define(["exports"],e):e(t.TradingView=t.TradingView||{})}(this,function(t){"use strict";function e(t,o){var i=n({},t);for(var s in o)"object"!=typeof t[s]||null===t[s]||Array.isArray(t[s])?void 0!==o[s]&&(i[s]=o[s]):i[s]=e(t[s],o[s]);return i}function o(){return"1.12 (internal id e816a7a6 @ 2018-03-26 08:21:11.837530)"}function i(t){window.addEventListener("DOMContentLoaded",t,!1)}var n=Object.assign||function(t){for(var e,o=arguments,i=1,n=arguments.length;i<n;i++){e=o[i];for(var s in e)Object.prototype.hasOwnProperty.call(e,s)&&(t[s]=e[s])}return t},s={mobile:{disabled_features:["left_toolbar","header_widget","timeframes_toolbar","edit_buttons_in_legend","context_menus","control_bar","border_around_the_chart"],enabled_features:[]}},r={width:800,height:500,symbol:"AA",interval:"D",timezone:"UTC",container_id:"",library_path:"",locale:"en",widgetbar:{details:!1,watchlist:!1,watchlist_settings:{default_symbols:[]}},overrides:{"mainSeriesProperties.showCountdown":!1},studies_overrides:{},brokerConfig:{configFlags:{}},fullscreen:!1,autosize:!1,disabled_features:[],enabled_features:[],debug:!1,logo:{},time_frames:[{text:"5y",resolution:"W"},{text:"1y",resolution:"W"},{text:"6m",resolution:"120"},{text:"3m",resolution:"60"},{text:"1m",resolution:"30"},{text:"5d",resolution:"5"},{text:"1d",resolution:"1"}],client_id:"0",user_id:"0",charts_storage_api_version:"1.0",favorites:{intervals:[],chartTypes:[]}},a=function(){function t(t){if(this._id="tradingview_"+(1048576*(1+Math.random())|0).toString(16).substring(1),this._ready=!1,this._readyHandlers=[],this._onWindowResize=this._autoResizeChart.bind(this),!t.datafeed)throw new Error("Datafeed is not defined");if(this._options=e(r,t),t.preset){var o=s[t.preset];o?(void 0!==this._options.disabled_features?this._options.disabled_features=this._options.disabled_features.concat(o.disabled_features):this._options.disabled_features=o.disabled_features,void 0!==this._options.enabled_features?this._options.enabled_features=this._options.enabled_features.concat(o.enabled_features):this._options.enabled_features=o.enabled_features):console.warn("Unknown preset: `"+t.preset+"`")}this._create()}return t.prototype.onChartReady=function(t){this._ready?t.call(this):this._readyHandlers.push(t)},t.prototype.onGrayedObjectClicked=function(t){this._innerAPI().onGrayedObjectClicked(t)},t.prototype.onShortcut=function(t,e){this._innerWindow().createShortcutAction(t,e)},t.prototype.subscribe=function(t,e){this._innerAPI().subscribe(t,e)},t.prototype.unsubscribe=function(t,e){this._innerAPI().unsubscribe(t,e)},t.prototype.chart=function(t){return this._innerAPI().chart(t)},t.prototype.setLanguage=function(t){this.remove(),this._options.locale=t,this._create()},t.prototype.setSymbol=function(t,e,o){this._innerAPI().changeSymbol(t,e+"",o)},t.prototype.remove=function(){window.removeEventListener("resize",this._onWindowResize),this._readyHandlers.splice(0,this._readyHandlers.length),delete window[this._id];var t=this._getIFrameElement();t.contentWindow.destroyChart(),t.parentNode&&t.parentNode.removeChild(t)},t.prototype.closePopupsAndDialogs=function(){this._innerAPI().closePopupsAndDialogs()},t.prototype.selectLineTool=function(t){this._innerAPI().selectLineTool(t)},t.prototype.selectedLineTool=function(){return this._innerAPI().selectedLineTool()},t.prototype.save=function(t){this._innerAPI().saveChart(t)},t.prototype.load=function(t,e){this._innerAPI().loadChart({json:t,extendedData:e})},t.prototype.getSavedCharts=function(t){this._innerAPI().getSavedCharts(t)},t.prototype.loadChartFromServer=function(t){this._innerAPI().loadChartFromServer(t)},t.prototype.saveChartToServer=function(t,e,o,i){this._innerAPI().saveChartToServer(t,e,o,i)},t.prototype.removeChartFromServer=function(t,e){this._innerAPI().removeChartFromServer(t,e)},t.prototype.onContextMenu=function(t){this._innerAPI().onContextMenu(t)},t.prototype.createButton=function(t){return this._innerWindow().createButton(t)},t.prototype.showNoticeDialog=function(t){this._innerAPI().showNoticeDialog(t)},t.prototype.showConfirmDialog=function(t){this._innerAPI().showConfirmDialog(t)},t.prototype.showLoadChartDialog=function(){this._innerAPI().showLoadChartDialog()},t.prototype.showSaveAsChartDialog=function(){this._innerAPI().showSaveAsChartDialog()},t.prototype.symbolInterval=function(){return this._innerAPI().getSymbolInterval()},t.prototype.mainSeriesPriceFormatter=function(){return this._innerAPI().mainSeriesPriceFormatter()},t.prototype.getIntervals=function(){return this._innerAPI().getIntervals()},t.prototype.getStudiesList=function(){return this._innerAPI().getStudiesList()},t.prototype.addCustomCSSFile=function(t){this._innerWindow().addCustomCSSFile(t)},t.prototype.applyOverrides=function(t){this._options=e(this._options,{overrides:t}),this._innerWindow().applyOverrides(t)},t.prototype.applyStudiesOverrides=function(t){this._innerWindow().applyStudiesOverrides(t)},t.prototype.watchList=function(){return this._innerAPI().watchlist()},t.prototype.activeChart=function(){return this._innerAPI().activeChart()},t.prototype.chartsCount=function(){return this._innerAPI().chartsCount()},t.prototype.layout=function(){return this._innerAPI().layout()},t.prototype.setLayout=function(t){this._innerAPI().setLayout(t)},t.prototype._getIFrameElement=function(){var t=document.getElementById(this._id);if(null===t)throw new Error("There is no such iframe");return t},t.prototype._innerAPI=function(){return this._getIFrameElement().contentWindow.tradingViewApi},t.prototype._innerWindow=function(){return this._getIFrameElement().contentWindow},t.prototype._autoResizeChart=function(){this._options.fullscreen&&(this._getIFrameElement().style.height=window.innerHeight+"px")},t.prototype._create=function(){var t=this,e=this._render(),o=document.getElementById(this._options.container_id);if(null===o)throw new Error("There is no such element - #"+this._options.container_id);o.innerHTML=e;var i=this._getIFrameElement();(this._options.autosize||this._options.fullscreen)&&(i.style.width="100%",this._options.fullscreen||(i.style.height="100%")),window.addEventListener("resize",this._onWindowResize),this._onWindowResize();var n=function(){i.removeEventListener("load",n,!1),i.contentWindow.widgetReady(function(){t._ready=!0;for(var e=0,o=t._readyHandlers;e<o.length;e++){o[e].call(t)}i.contentWindow.initializationFinished()})};i.addEventListener("load",n,!1)},t.prototype._render=function(){var t=window;t[this._id]={datafeed:this._options.datafeed,customFormatters:this._options.customFormatters,brokerFactory:this._options.brokerFactory,overrides:this._options.overrides,studiesOverrides:this._options.studies_overrides,disabledFeatures:this._options.disabled_features,enabledFeatures:this._options.enabled_features,brokerConfig:this._options.brokerConfig,restConfig:this._options.restConfig,favorites:this._options.favorites,logo:this._options.logo,numeric_formatting:this._options.numeric_formatting,rss_news_feed:this._options.rss_news_feed,newsProvider:this._options.news_provider,loadLastChart:this._options.load_last_chart,saveLoadAdapter:this._options.save_load_adapter,loading_screen:this._options.loading_screen,settingsAdapter:this._options.settings_adapter},this._options.saved_data&&(t[this._id].chartContent={json:this._options.saved_data});var e=(this._options.library_path||"")+"static/tv-chart.e816a7a6edc9de3ed709.html#localserver=1&symbol="+encodeURIComponent(this._options.symbol)+"&interval="+encodeURIComponent(this._options.interval)+(this._options.timeframe?"&timeframe="+encodeURIComponent(this._options.timeframe):"")+(this._options.toolbar_bg?"&toolbarbg="+this._options.toolbar_bg.replace("#",""):"")+(this._options.studies_access?"&studiesAccess="+encodeURIComponent(JSON.stringify(this._options.studies_access)):"")+"&widgetbar="+encodeURIComponent(JSON.stringify(this._options.widgetbar))+(this._options.drawings_access?"&drawingsAccess="+encodeURIComponent(JSON.stringify(this._options.drawings_access)):"")+"&timeFrames="+encodeURIComponent(JSON.stringify(this._options.time_frames))+"&locale="+encodeURIComponent(this._options.locale)+"&uid="+encodeURIComponent(this._id)+"&clientId="+encodeURIComponent(String(this._options.client_id))+"&userId="+encodeURIComponent(String(this._options.user_id))+(this._options.charts_storage_url?"&chartsStorageUrl="+encodeURIComponent(this._options.charts_storage_url):"")+(this._options.charts_storage_api_version?"&chartsStorageVer="+encodeURIComponent(this._options.charts_storage_api_version):"")+(this._options.indicators_file_name?"&indicatorsFile="+encodeURIComponent(this._options.indicators_file_name):"")+(this._options.custom_css_url?"&customCSS="+encodeURIComponent(this._options.custom_css_url):"")+(this._options.auto_save_delay?"&autoSaveDelay="+encodeURIComponent(String(this._options.auto_save_delay)):"")+"&debug="+this._options.debug+(this._options.snapshot_url?"&snapshotUrl="+encodeURIComponent(this._options.snapshot_url):"")+(this._options.timezone?"&timezone="+encodeURIComponent(this._options.timezone):"")+(this._options.study_count_limit?"&studyCountLimit="+encodeURIComponent(String(this._options.study_count_limit)):"")+(this._options.symbol_search_request_delay?"&ssreqdelay="+encodeURIComponent(String(this._options.symbol_search_request_delay)):"");return'<iframe id="'+this._id+'" name="'+this._id+'"  src="'+e+'"'+(this._options.autosize||this._options.fullscreen?"":' width="'+this._options.width+'" height="'+this._options.height+'"')+' frameborder="0" allowTransparency="true" scrolling="no" allowfullscreen style="display:block;"></iframe>'},t}(),d=a;window.TradingView=window.TradingView||{},window.TradingView=t,t.version=o,t.onready=i,t.widget=d,Object.defineProperty(t,"__esModule",{value:!0})});
  })()
var Datafeeds = class Datafeeds {
  constructor(mgr, updateFrequency) {
    this._mgr = mgr
    this._configuration = undefined

    // this._barsPulseUpdater = new DataPulseUpdater(this, updateFrequency || 10 * 1000)
    this._barsPulseUpdater = new DataPulseUpdater(this)

    this._enableLogging = false
    this._initializationFinished = false
    this._callbacks = {}

    this._initialize()
  }
  /**
   * 默认配置
   */
  defaultConfiguration() {
    return {
      supports_search: true,
      supports_group_request: false,
      supported_resolutions: ['1', '5', '15', '30', '60', '240', '1D', '1W'],
      supports_marks: true,
      supports_timescale_marks: true,
      supports_time: true,
      exchanges: [{
        value: '',
        name: 'All Exchanges',
        desc: ''
      }],
      symbols_types: [{
        name: 'All types',
        value: ''
      }]
    }
  }
  /**
   * 获取服务端时间
   * @param {*Function 回调函数} callback
   */
  getServerTime(callback) {
    if (this._configuration.supports_time) {
      const self = this
      setTimeout(function () {
        callback(self._mgr.getServerTime())
      }, 10)
    }
  }
  /**
   * 绑定事件
   * @param {*String 事件名} event
   * @param {*Function 回调函数} callback
   */
  on(event, callback) {
    if (!this._callbacks.hasOwnProperty(event)) {
      this._callbacks[event] = []
    }

    this._callbacks[event].push(callback)
    return this
  }
  /**
   * 运行事件
   * @param {*String 事件名} event
   * @param {*Undefined 参数} argument
   */
  _fireEvent(event, argument) {
    if (this._callbacks.hasOwnProperty(event)) {
      const callbacksChain = this._callbacks[event]
      for (let i = 0; i < callbacksChain.length; ++i) {
        callbacksChain[i](argument)
      }

      this._callbacks[event] = []
    }
  }
  /**
   * 初始化结束
   */
  onInitialized() {
    this._initializationFinished = true
    this._fireEvent('initialized')
  }
  /**
   * 打印信息
   * @param {*String 信息} message
   */
  _logMessage(message) {
    if (this._enableLogging) {
      console.log(new Date().toLocaleTimeString() + ' >> ', message)
    }
  }
  /**
   * 初始化
   */
  _initialize() {
    const configurationData = this._mgr.getConfig()
    const defaultConfig = this.defaultConfiguration()
    if (configurationData) {
      const conf = Object.assign({}, defaultConfig, configurationData)
      this._setupWithConfiguration(conf)
    } else {
      this._setupWithConfiguration(defaultConfig)
    }
  }
  /**
   * 填充配置数据
   * @param {*Function 回调函数} callback
   */
  onReady(callback) {
    const that = this
    if (this._configuration) {
      setTimeout(function () {
        callback(that._configuration)
      }, 0)
    } else {
      this.on('configuration_ready', function () {
        callback(that._configuration)
      })
    }
  }
  /**
   * 安装配置数据
   * @param {*Object 配置数据} configurationData
   */
  _setupWithConfiguration(configurationData) {
    this._configuration = configurationData

    if (!this._configuration.exchanges) {
      this._configuration.exchanges = []
    }

    if (this._configuration.supports_group_request) {
      console.error(' >> ：Sorry unsupports group request')
      return
    }
    this.onInitialized()
    this._fireEvent('configuration_ready')
    this._logMessage('Initialized with ' + JSON.stringify(configurationData))
  }
  /**
   * 通过商品名称解析商品信息
   * @param {*String 商品名称或ticker} symbolName
   * @param {*Function(SymbolInfo)} onSymbolResolvedCallback
   * @param {*Function(reason)} onResolveErrorCallback
   */
  resolveSymbol(symbolName, onSymbolResolvedCallback, onResolveErrorCallback) {
    const that = this

    if (!this._initializationFinished) {
      this.on('initialized', function () {
        that.resolveSymbol(symbolName, onSymbolResolvedCallback, onResolveErrorCallback)
      })

      return
    }

    var resolveRequestStartTime = Date.now()
    that._logMessage('Resolve requested')

    function onResultReady(data) {
      let postProcessedData = data
      if (that.postProcessSymbolInfo) {
        postProcessedData = that.postProcessSymbolInfo(postProcessedData)
      }

      that._logMessage('Symbol resolved: ' + (Date.now() - resolveRequestStartTime))

      onSymbolResolvedCallback(postProcessedData)
    }

    if (!this._configuration.supports_group_request) {
      setTimeout(function () {
        const data = that._mgr.resolveTVSymbol(symbolName ? symbolName.toUpperCase() : '')
        if (data) {
          onResultReady(data)
        } else {
          that._logMessage('Error resolving symbol: ' + symbolName)
          onResolveErrorCallback('unknown_symbol')
        }
      }, 10)
    }
  }
  /**
   * 搜索商品
   * @param {*String 用户在商品搜索框中输入的文字} userInput
   * @param {*String 请求的交易所（由用户选择）。空值表示没有指定} exchange
   * @param {*String 请求的商品类型：指数、股票、外汇等等（由用户选择）。空值表示没有指定} symbolType
   * @param {*Function 回调函数} onResultReadyCallback
   */
  searchSymbols(userInput, exchange, symbolType, onResultReadyCallback) {
    if (this._configuration.supports_search) {
      console.log(' >> 搜索商品：', userInput, exchange, symbolType)
      // step 1：向服务端发起请求
      // your code
      // step 2：返回结果
      onResultReadyCallback([
        // https://b.aitrade.ga/books/tradingview/book/JS-Api.html#searchSymbolsuserinput-exchange-symboltype-onresultreadycallback
        {
          "symbol": 'AAFR',
          "full_name": 'BTCUSD',
          "description": '',
          "exchange": '',
          "ticker": '',
          "type": "stock" | "futures" | "bitcoin" | "forex" | "index"
        }
      ])
    }
  }
  /**
   * 获取时间刻度
   * @param {*Object 商品信息} symbolInfo
   * @param {*Number unix时间戳 (UTC)} startDate
   * @param {*Number unix时间戳 (UTC)} endDate
   * @param {*Function 回调函数} onDataCallback
   * @param {*String 分辨率} resolution
   */
  getTimescaleMarks(symbolInfo, startDate, endDate, onDataCallback, resolution) {
    if (this._configuration.supports_timescale_marks) {
      // console.log(' >> 获取时间刻度：', symbolInfo, startDate, endDate, resolution)
      // step 1：向服务端发起请求
      // your code
      // step 2：返回结果
      // 注释掉起始点的刻度
      // onDataCallback([
      //   {
      //     color: 'red',
      //     id: 'tsm1',
      //     label: 'A',
      //     time: 1492041600,
      //     tooltip: 'test1'
      //   }
      // ])
    }
  }
  /**
   * 获取K线标记
   * @param {*Object 商品信息} symbolInfo
   * @param {*Number unix时间戳 (UTC)} startDate
   * @param {*Number unix时间戳 (UTC)} endDate
   * @param {*Function 回调函数} onDataCallback
   * @param {*String 分辨率} resolution
   */
  getMarks(symbolInfo, startDate, endDate, onDataCallback, resolution) {
    if (this._configuration.supports_marks) {
      // console.log(' >> 获取K线标记：', symbolInfo, startDate, endDate, resolution)
      // step 1：向服务端发起请求
      // your code
      // step 2：返回结果
      onDataCallback([
        {
          color: 'red',
          id: 'tsm1',
          text: 'AAA',
          label: 'A',
          time: 1492041600,
          labelFontColor: '',
          minSize: 28
        }
      ])
    }
  }
  /**
   *
   * @param {*Object 商品信息对象} symbolInfo
   * @param {*String 分辨率} resolution
   * @param {*Number 时间戳、最左边请求的K线时间} rangeStartDate
   * @param {*Number 时间戳、最右边请求的K线时间} rangeEndDate
   * @param {*Function 回调函数} onDataCallback
   * @param {*Function 回调函数} onErrorCallback
   */
  getBars(symbolInfo, resolution, rangeStartDate, rangeEndDate, onDataCallback, onErrorCallback) {
    if (rangeStartDate > 0 && (rangeStartDate + '').length > 10) {
      throw new Error(['Got a JS time instead of Unix one.', rangeStartDate, rangeEndDate])
    }

    const onLoadedCallback = function (data) {

      if (data) {
        const nodata = data.s === 'no_data'

        if (data.s !== 'ok' && !nodata) {
          if (!!onErrorCallback) {
            onErrorCallback(data.s)
          }

          return
        }

        const bars = data.bars || []
        onDataCallback(bars, { noData: nodata, nextTime: data.nextTime })
      } else {
        console.warn(['getBars(): error'])

        if (!!onErrorCallback) {
          onErrorCallback(' error: ')
        }
      }
    }

    this._mgr.getBars(symbolInfo.ticker.toUpperCase(), resolution, rangeStartDate, rangeEndDate, onLoadedCallback)
  }
  /**
   * 订阅K线数据
   * @param {*Object 商品信息对象} symbolInfo
   * @param {*String 分辨率} resolution
   * @param {*Function 回调函数} onRealtimeCallback
   * @param {*String 监听的唯一标识符} listenerGUID
   * @param {*Function 回调函数} onResetCacheNeededCallback
   */
  subscribeBars(symbolInfo, resolution, onRealtimeCallback, listenerGUID, onResetCacheNeededCallback) {
    // console.log('subscribeBars: ' + '😊' + symbolInfo.name + '😊 ' + resolution + ', ' + listenerGUID);
    // console.log('subscribeBars: ' + symbolInfo.name + resolution);

    // const params = {
    //   channel: 'kline_tick',
    //   subject: this.onSetSubject(resolution),
    //   symbol: symbolInfo['name']
    // }

    console.log('订阅数据')
    window.MARKET_LINE.subscribe('/exchange/market/' + symbolInfo['name'] + '@kline_' + this.onSetSubject(resolution), (res) => {
      console.log(JSON.parse(JSON.parse(res.body).data))
      const data = JSON.parse(JSON.parse(res.body).data)
      const tick_bar = {
        close: Number(data['close']),
        high: Number(data['high']),
        low: Number(data['low']),
        open: Number(data['open']),
        time: Number(data['time']),
        volume: Number(data['volume'])
      }
      this._barsPulseUpdater.subscribeDataListener(symbolInfo, resolution, onRealtimeCallback(tick_bar), listenerGUID, onResetCacheNeededCallback)
    })

  }
  // 处理请求的subject
  onSetSubject(v) {
    switch (v) {
      case '1':
        return '1m';
        break;
      case '5':
        return '5m';
        break;
      case '15':
        return '15m';
        break;
      case '30':
        return '30m';
        break;
      case '60':
        return '1h';
        break;
      case '240':
        return '4h';
        break;
      case '1D':
        return '1d';
        break;
      case '1W':
        return '1w';
        break;
      default:
        return '1d';
        break;
    }
  }
  /**
   * 取消订阅K线数据
   * @param {*String 监听的唯一标识符} listenerGUID
   */
  unsubscribeBars(listenerGUID) {
    this._barsPulseUpdater.unsubscribeDataListener(listenerGUID)
  }
}
/*
  This is a pulse updating components for ExternalDatafeed.
  They emulates realtime updates with periodic requests.
*/
var DataPulseUpdater = class DataPulseUpdater {
  constructor(datafeed, updateFrequency) {
    this._datafeed = datafeed
    this._datafeed._logMessage('DataPulseUpdater init ' + updateFrequency)

    this._subscribers = {}

    this._requestsPending = 0
    const that = this

    const update = function () {
      if (that._requestsPending > 0) {
        return
      }

      for (let listenerGUID in that._subscribers) {
        const subscriptionRecord = that._subscribers[listenerGUID]
        const resolution = subscriptionRecord.resolution

        const datesRangeRight = that._datafeed._mgr.getServerTime()

        //	BEWARE: please note we really need 2 bars, not the only last one
        //	see the explanation below. `10` is the `large enough` value to work around holidays
        const datesRangeLeft = datesRangeRight - that.periodLengthSeconds(resolution, 10)

        that._requestsPending++

        (function (_subscriptionRecord) {
          that._datafeed.getBars(_subscriptionRecord.symbolInfo, resolution, datesRangeLeft, datesRangeRight, function (bars) {
                that._requestsPending--

                //	means the subscription was cancelled while waiting for data
                if (!that._subscribers.hasOwnProperty(listenerGUID)) {
                  return
                }

                if (bars.length === 0) {
                  return
                }

                const lastBar = bars[bars.length - 1]
                if (!isNaN(_subscriptionRecord.lastBarTime) && lastBar.time < _subscriptionRecord.lastBarTime) {
                  return
                }

                const subscribers = _subscriptionRecord.listeners

                //	BEWARE: this one isn't working when first update comes and this update makes a new bar. In this case
                //	_subscriptionRecord.lastBarTime = NaN
                const isNewBar = !isNaN(_subscriptionRecord.lastBarTime) && lastBar.time > _subscriptionRecord.lastBarTime

                //	Pulse updating may miss some trades data (ie, if pulse period = 10 secods and new bar is started 5 seconds later after the last update, the
                //	old bar's last 5 seconds trades will be lost). Thus, at fist we should broadcast old bar updates when it's ready.
                if (isNewBar) {
                  if (bars.length < 2) {
                    throw new Error('Not enough bars in history for proper pulse update. Need at least 2.')
                  }

                  const previousBar = bars[bars.length - 2]
                  for (let i = 0; i < subscribers.length; ++i) {
                    subscribers[i](previousBar)
                  }
                }

                _subscriptionRecord.lastBarTime = lastBar.time

                for (let j = 0; j < subscribers.length; ++j) {
                  subscribers[j](lastBar)
                }
              },

              //	on error
              function () {
                that._requestsPending--
              })
        })(subscriptionRecord)
      }
    }

    if (typeof updateFrequency != 'undefined' && updateFrequency > 0) {
      setInterval(update, updateFrequency)
    }
  }
  /**
   * 取消订阅数据
   * @param {*String 监听的唯一标识符} listenerGUID
   */
  unsubscribeDataListener(listenerGUID) {
    this._datafeed._logMessage('Unsubscribing ' + listenerGUID)
    delete this._subscribers[listenerGUID]
  }
  /**
   * 订阅数据
   * @param {*Object 商品信息对象} symbolInfo
   * @param {*String 分辨率} resolution
   * @param {*Object 回调数据} newDataCallback
   * @param {*String 监听的唯一标识符} listenerGUID
   */
  subscribeDataListener(symbolInfo, resolution, newDataCallback, listenerGUID) {
    this._datafeed._logMessage('Subscribing ' + listenerGUID)

    if (!this._subscribers.hasOwnProperty(listenerGUID)) {
      this._subscribers[listenerGUID] = {
        symbolInfo: symbolInfo,
        resolution: resolution,
        lastBarTime: NaN,
        listeners: []
      }
    }

    this._subscribers[listenerGUID].listeners.push(newDataCallback)
  }
  /**
   * 计算周期范围
   * @param {*String 分辨率} resolution
   * @param {*Number 周期范围} requiredPeriodsCount
   */
  periodLengthSeconds(resolution, requiredPeriodsCount) {
    let daysCount = 0

    if (resolution === 'D') {
      daysCount = requiredPeriodsCount
    } else if (resolution === 'M') {
      daysCount = 31 * requiredPeriodsCount
    } else if (resolution === 'W') {
      daysCount = 7 * requiredPeriodsCount
    } else {
      daysCount = requiredPeriodsCount * resolution / (24 * 60)
    }

    return daysCount * 24 * 60 * 60
  }
}
  var tradeView = {
    widget: null,
    dataFeed: null,
    symbol_name: '', // 传入的商品码
    interval: '',
    // 这个字段确认接收到k线历史返回
    has_history: false,
    // k线的histore请求最大次数
    request_his_number: 0,
    // 价格精度
    pricescale: 100,
    // 渲染按钮数组
    button_arr: [
      {
        value: 'line',
        text: 'Time',
      },
      {
        value: '1',
        text: '1min',
      },
      {
        value: '5',
        text: '5min',
      },
      {
        value: '15',
        text: '15min',
      },
      {
        value: '30',
        text: '30min',
      },
      {
        value: '60',
        text: '1h',
      },
      {
        value: '240',
        text: '4h',
      },
      {
        value: '1D',
        text: '1D',
      },
      // {
      //   value: '1W',
      //   text: '1W',
      // },
    ],
    init: function (options, callback) {
      // console.log('初始化')
      this.symbol_name = options.symbol;
      this.interval = options.interval;
      this.pricescale = options.pricescale;
      this.dataFeed = new Datafeeds(this)
      // global.Vue.$store.commit('setDataFeed', this.dataFeed);

      // TradingView.onready(() => {
      this.widget = new TradingView.widget({
        autosize: true,
        symbol: options.symbol,
        interval: options.interval,
        container_id: 'tv_chart_container',
        datafeed: this.dataFeed,
        // 设置初始化范围会出现问题，不可预料的请求，最好不要设置
        // timeframe: '3D',
        library_path: '/tv_charting_library/',
        //library_path: 'static/charting_library/',
        drawings_access: {
          type: 'black',
          tools: [{name: 'Regression Trend'}]
        },
        numeric_formatting: {
          decimal_sign: '.'
        },
        timezone: 'Asia/Shanghai',
        locale: options.lang,
        debug: false,
        enabled_features: [
          // "hide_left_toolbar_by_default"  //第一次默认隐藏侧边栏
        ],
        // 禁用列表
        // widget_logo 禁用logo
        disabled_features: [
          "header_symbol_search",
          "header_interval_dialog_button",
          "header_compare",
          "header_resolutions",
          // "left_toolbar",   //侧边栏
          "volume_force_overlay",
          "display_market_status",
          "header_screenshot",  //截图
          "header_undo_redo", //切换上一个k线
          "timeframes_toolbar", //
          "go_to_date",
          "header_settings", // 设置
          "header_chart_type"
        ],
        // 设置工具栏和整个k线的背景色
        toolbar_bg: "#171b2b",
        // 样式覆盖
        overrides: {
          // 图表颜色
          "paneProperties.background": "#171b2b",
          // 网格垂直线
          "paneProperties.vertGridProperties.color": "rgba(95, 107, 129, 0.1)",
          // 网格横线
          "paneProperties.horzGridProperties.color": "rgba(95, 107, 129, 0.1)",
          // 收起左上角标题
          'paneProperties.legendProperties.showLegend': true,
          // 区域+字坐标颜色
          'scalesProperties.lineColor': "rgb(95, 107, 129)",
          // 区域k线指标的文本颜色
          'scalesProperties.textColor': "rgb(90, 104, 129)",
          // 蜡烛样式
          "mainSeriesProperties.candleStyle.upColor": "rgba(81, 181, 137, 1)",
          "mainSeriesProperties.candleStyle.downColor": "rgba(234, 94, 89, 1)",
          "mainSeriesProperties.candleStyle.drawWick": true,
          // 烛芯边框
          "mainSeriesProperties.candleStyle.borderUpColor": "rgba(81, 181, 137, 1)",
          "mainSeriesProperties.candleStyle.borderDownColor": "rgba(234, 94, 89, 1)",
          // 烛芯颜色
          "mainSeriesProperties.candleStyle.wickUpColor": 'rgba(81, 181, 137, 1)',
          "mainSeriesProperties.candleStyle.wickDownColor": 'rgba(234, 94, 89, 1)',
          // 分时面积图
          'mainSeriesProperties.areaStyle.color1': "#0094FF",
          'mainSeriesProperties.areaStyle.color2': "rgba(255, 255, 255, 0)",
          'mainSeriesProperties.areaStyle.linecolor': "#0094FF",
          // 设置最高k线柱距离top-border的高度
          "paneProperties.topMargin": 12,
          // 副指标高度 支持的值: large, medium, small, tiny
          "volumePaneSize": "medium"
        },
        // 成交量line
        studies_overrides: {
          // 红
          "volume.volume.color.0": "rgba(234, 94, 89, 0.3)",
          // 绿
          "volume.volume.color.1": "rgba(81, 181, 137, 0.3)",
        },
        // css
        custom_css_url: '/tv_charting_library/static/style/night.css',

      })
      this.widget.onChartReady(() => {
        // 均线
        const ma_5 = this.widget.chart().createStudy("Moving Average", false, false, [10], null, {"plot.color": "rgb(150, 95, 196)"});
        const ma_10 = this.widget.chart().createStudy("Moving Average", false, false, [20], null, {"plot.color": "rgb(132, 170, 213)"});
        const ma_30 = this.widget.chart().createStudy("Moving Average", true, false, [30], null, {"plot.color": "rgb(85, 178, 99)"});
        // const ma_60 = this.widget.chart().createStudy("Moving Average", true, false, [60], null, {"plot.color": "rgb(183, 36, 138)"});

        this.widget.chart().setChartType(3);
        this.widget.chart().setEntityVisibility(ma_5, false);
        this.widget.chart().setEntityVisibility(ma_10, false);
        this.widget.chart().setEntityVisibility(ma_30, false);
        // this.widget.chart().setEntityVisibility(ma_60, false);

        // 渲染按钮
        let btn = {};
        const handleClick = (e, v) => {
          //
          if (v !== 'line') {
            this.request_his_number = 0;
            this.widget.chart().setResolution(v);
            // 蜡烛图
            this.widget.chart().setChartType(1);
            this.widget.chart().setEntityVisibility(ma_5, true);
            this.widget.chart().setEntityVisibility(ma_10, true);
            this.widget.chart().setEntityVisibility(ma_30, true);
            // this.widget.chart().setEntityVisibility(ma_60, true);
          } else {
            // 面积图
            this.widget.chart().setChartType(3);
            this.widget.chart().setEntityVisibility(ma_5, false);
            this.widget.chart().setEntityVisibility(ma_10, false);
            this.widget.chart().setEntityVisibility(ma_30, false);
            // this.widget.chart().setEntityVisibility(ma_60, false);
          }
          $(e.target).addClass('selected').closest('div.space-single').siblings('div.space-single').find('div.button').removeClass('selected');
        }
        this.button_arr.forEach((v) => {
          btn = this.widget.createButton().on('click', (e) => {
            handleClick(e, v.value);
          })
          // deal line lang
          if (v.value === 'line') {
            if (options.lang === 'zh') {
              btn[0].innerHTML = v.text;
              btn[0].text = v.text;
            } else {
              btn[0].innerHTML = 'line';
              btn[0].text = 'line';
            }
          } else {
            btn[0].innerHTML = v.text;
            btn[0].text = v.text;
          }
          // 处理默认为1min的样式
          if (v.text === 'Time') {
            btn.addClass('selected').parent().css('float', 'left');
          } else {
            btn.parent().css('float', 'left');
          }
        })
      })
      // })

      // 接收到历史数据的话返回信息
      // if (callback) {
      //   let callBackTime = setInterval(() => {
      //     if (this.has_history) {
      //       clearInterval(callBackTime);
      //       callback('ok');
      //     }
      //   }, 300)
      // }

    },

    // 移除tv
    removeTv() {
      this.widget.remove();
    },

    // 设置lang
    setLang(lang) {
      this.widget.setLanguage(lang);
    },

    // 切换商品
    // setSymbol: function(symbol, pricescale) {
    //   this.pricescale = pricescale;
    //   this.widget.chart().setSymbol(symbol, function() {});
    // },

    getBars: function (symbol, resolution, from, to, callback) {
      console.log('请求历史数据')
      $.get(''+$("#wss_post").val()+'/kline/loadKlineData' + '?symbol=' + symbol + '&subject=' + this.onSetSubject(resolution) + '&from=' + from * 1000 + '&to=' + to * 1000).then(responese => {
        let res = JSON.parse(responese);
        // $.get('http://192.168.2.184/kline/loadKlineData'
        let newBars = res.data || [];
        if (res.code !== 'NO_DATA') {
          if (newBars.length) {
            newBars.forEach(v => {
              v['time'] = Number(v['time']);
              v['volume'] = Number(v['volume']);
              v['close'] = Number(v['close']);
              v['high'] = Number(v['high']);
              v['low'] = Number(v['low']);
              v['open'] = Number(v['open']);
            })
            newBars.sort((l, r) => l.time > r.time ? 1 : -1)
            // console.log(newBars);
            // console.log('has_data');
            callback && callback({s: 'ok', bars: newBars});
            // this.has_history = true;
          } else {
            // console.log('此区间段没有数据，但是有历史数据，重新请求前一个区间段')
            // 最大请求10次防止后端返回的包出现数据错误
            if (this.request_his_number >= 30) {
              callback && callback({s: 'no_data'});
              return;
            }
            this.request_his_number++;
            const nextTime = this.changeFormSeconds(resolution, from);
            this.getBars(symbol, resolution, nextTime, to, callback)
          }
        } else {
          // console.log('no_data');
          callback && callback({s: 'no_data'});
          // this.has_history = true;
        }
      })
    },

    // 获取配置
    getConfig: function () {
      // https://b.aitrade.ga/books/tradingview/book/UDF.html
      return {
        supports_search: true,  //  请修改datafeed的searchSymbols函数
        supports_group_request: false, // 设置为true将无法进行单个商品解析
        supports_marks: true,  // 请修改datafeed的getMarks函数
        supports_timescale_marks: true, // 请修改datafeed的getTimescaleMarks函数
        supports_time: true
      }
    },

    getServerTime: function () {
      // 暂时由客户端生成时间
      return parseInt(Date.now() / 1000)
    },

    resolveTVSymbol: function (symbol) {
      this.is_fetchCacheData = true;
      this.symbol_name = symbol;
      // https://b.aitrade.ga/books/tradingview/book/Symbology.html
      return {
        'name': symbol,
        'exchange-traded': '',
        'exchange-listed': '',
        'timezone': 'Asia/Shanghai',
        'minmov': 1,
        'minmov2': 0,
        'pointvalue': 1,
        'fractional': false,
        'session': '24x7',
        'has_intraday': true,
        'has_no_volume': false,
        'description': symbol,
        'has_empty_bars': true,
        // 价格小数精度
        'pricescale': this.pricescale,
        // 成交小数精确度
        'volume_precision': 2,
        'ticker': symbol,
        // 显示周线和月线
        'has_weekly_and_monthly': true,
        'supported_resolutions': ['1', '5', '15', '30', '60', '240', '1D', '1W']
      }
    },
    // 处理请求的subject
    onSetSubject(v) {
      switch (v) {
        case '1':
          return '1m';
          break;
        case '5':
          return '5m';
          break;
        case '15':
          return '15m';
          break;
        case '30':
          return '30m';
          break;
        case '60':
          return '1h';
          break;
        case '240':
          return '4h';
          break;
        case '1D':
          return '1d';
          break;
        case '1W':
          return '1w';
          break;
        default:
          return '1d';
          break;
      }
    },
    changeFormSeconds(resolution, from) {
      let daysCount = 0
      if (resolution === 'D') {
        daysCount = from - (24 * 60 * 60 * 360);
      } else {
        daysCount = from - (24 * 60 * 60);
      }
      return daysCount
    }
  }

  exports.tradeView = tradeView;

})