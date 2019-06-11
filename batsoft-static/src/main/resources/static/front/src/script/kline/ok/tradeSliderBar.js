define([], function() {
    var win = window;

    var content = '<div class="tradeSliderBar">'
                + '  <div class="sliderBody" tabindex="-1">'
                + '     <a class="quarter quarterOne" percent="0"></a>'
                + '     <a class="quarter quarterTwo" percent="25"></a>'
                + '     <a class="quarter quarterThree" percent="50"></a>'
                + '     <a class="quarter quarterFour" percent="75"></a>'
                + '     <a class="quarter quarterFive" percent="100"></a>'
                + '     <div class="sliderLine">'
                + '         <div class="dragBar"></div>'
                + '     </div>'
                + '  </div>'
                + '  <span class="percentNum">0%</span>'
                + '</div>';

    // 获取正确的百分比值
    function getLegalPercentValue(newPercent) {
        newPercent < 0 && (newPercent = 0);
        newPercent > 100 && (newPercent = 100);

        return newPercent;
    }

    /**
     * @param config.type   1: buy, 2: sell
     * @param config.width  拖动条宽度
     * @param config.containerId 容器元素ID
     */
    function TradeSliderBar(config) {

        // 总宽度
        var totalWidth = config.width || 212;

        var startX = -1;
        var endX = -1;

        var dragging = false;
        var dragStopping = false;

        var $container = jQuery('#' + config.containerId);

        // 如果元素不存在 则不再继续执行
        if ($container.length < 1) return;

        $container.html(content);

        $container.children().addClass('width' + totalWidth);

        var lineClass = 'lineBlue';
        if (config.type) {
            lineClass = config.type == 'buy' ? 'lineGreen' : 'lineRed'
        }
        $container.find('.sliderLine').addClass(lineClass);

        var sliderBody = $container.find('.sliderBody');
        var $sliderQuarter = $container.find('a');

        sliderBody.css('width', totalWidth + 'px');

        var dragBar = $container.find('.dragBar').get(0);

        this.$container = $container;

        this.currPercent = 0;
        this.onPercentChangeFn = function(percent) {};
        this.onPercentChangeEndFn = function(percent) {};

        var self = this;

        // 任意位置点击
        sliderBody.click(function(e) {
            chooseAnyPercent(e);
        });

        // 结点位置点击
        $sliderQuarter.click(function(e) {
            var percent = jQuery(this).attr('percent');
            
            changePercent(parseInt(percent), e);
        });

        // 开始拖动
        dragBar.addEventListener('mousedown', function(e) {
            dragging = true;
            startX = endX = e.pageX;
        });

        // 拖动结束
        document.addEventListener('mouseup', function(e) {
            if (dragging) {
                dragging = false;
                dragStopping = true;

                setTimeout(function() {
                    dragStopping = false;
                }, 100);

                self.onPercentChangeEndFn(self.currPercent);
            }
        });

        // 处理拖动
        document.addEventListener('mousemove', function(e) {
            if (!dragging) return;

            window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();

            endX = e.pageX;

            var percent = calculateNewPercent(startX, endX);

            changePercent(percent);

            if (config.roundNumber) {
                percent = Math.round(percent);
            }

            self.onPercentChangeFn(percent);

            startX = endX;
        });

        // 选择任意位置
        function chooseAnyPercent(event) {
            if (dragStopping) return;

            var percent = (event.offsetX / totalWidth * 100).toFixed(2) * 1;

            percent = getLegalPercentValue(percent);

            self.changePercent(percent);

            if (config.roundNumber) {
                percent = Math.round(percent);
            }

            self.onPercentChangeFn(percent);

            self.onPercentChangeEndFn(percent);
        }

        // 选择四分节点百分比
        function changePercent(percent, event) {
            self.currPercent = percent;

            renderView(percent);

            if (event) {
                self.onPercentChangeFn(percent);
                self.onPercentChangeEndFn(percent);
                event.stopPropagation();
            }
        }

        // 计算新的百分比
        function calculateNewPercent(startX, endX) {
            var movePercent = (endX - startX) / totalWidth * 100;
            var newPercent = (self.currPercent + movePercent).toFixed(2) * 1;

            return getLegalPercentValue(newPercent);
        }

        // 渲染视图
        function renderView(percent) {
            var width = percent + '%';
            var left = -4;

            var sliderLine = self.$container.find('.sliderLine');
            var dragBar = self.$container.find('.dragBar');
            var percentNum = self.$container.find('.percentNum');

            sliderLine.css('width', width);

            if (percent != 0) {
                // 小于-4时须置为-4
                left = Math.max(left, totalWidth * percent * 0.01 - 8);
            }

            dragBar.css('margin-left', left + 'px');

            var type = config.type;
            var bgClass = 'quarterBlue';
            if (type) {
                bgClass = type == 'buy' ? 'quarterGreen' : 'quarterRed'
            }

            self.$container.find('a').each(function() {

                var $this = jQuery(this);

                var percent = Number($this.attr('percent'));

                if (self.currPercent > percent) {
                    $this.addClass(bgClass);
                } else {
                    $this.removeClass(bgClass);
                }

            });

            if (config.roundNumber) {
                percent = Math.round(percent);
            }

            percentNum.html(percent + '%');
        }

        this.changePercent = changePercent;
    }

    // 百分比变化回调
    TradeSliderBar.prototype.onPercentChange = function(fn) {
        if (typeof fn == 'function') {
            this.onPercentChangeFn = fn;
        }
    };

    // 更改百分比结束回调
    TradeSliderBar.prototype.onPercentChangeEnd = function(fn) {
        if (typeof fn == 'function') {
            this.onPercentChangeEndFn = fn;
        }
    };

    // 设置指定的百分比
    TradeSliderBar.prototype.setPercent = function(percent) {

        percent = Number(percent);

        if (percent > 100) {
            percent = 100;
        }

        // 先检测changePercent是否可用
        this.changePercent && this.changePercent(percent);
    };

    // 获取百分比
    TradeSliderBar.prototype.getCurrPercent = function() {
        return this.currPercent;
    };

    return TradeSliderBar;

});


// 测试

//var config = {
//    type: 'buy',
//    containerId: 'sliderBar'
//};
//
//var sliderBar = new TradeSliderBar(config);
//
//sliderBar.onPercentChange(function(percent) {
//    console.log('percent:', percent);
//});
//
//setTimeout(function() {
//    sliderBar.setPercent(50);
//}, 1000);