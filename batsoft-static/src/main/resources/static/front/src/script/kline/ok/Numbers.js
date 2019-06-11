;(function (global, undefined) {

    'use strict';

    var currencyRE = /(\d)(?=(\d{3})+\.)/g;

    /**
     * 过滤输入值 转为数字形式
     * @param value {string|number}     需要转换的值
     * @param digits {number}           允许输入的最大小数位数
     * @returns {string}
     */
    function filter(value, digits) {

        value = String(value);

        // 过滤非数字组成字符
        value = String(value).replace(/[^0-9\.]/g, '');

        // 多个小数点非法
        var illegal = isNaN(Number(value));

        // 非法时移除新输入的字符
        if (illegal) {
            value = value.substring(0, value.length - 1);

            // 再处理
            return filter(value, digits);
        }

        // 如果传入小数点位数限制 并且小数点存在 则处理小数点位限制
        if (typeof digits === 'number' && value.indexOf('.') > -1) {
            var parts = value.split('.');

            // 整数限制
            if (digits < 1) {
                return parts[0];
            }

            // 超过小数点限制时 对其截断
            if (parts[1].length > digits) {
                parts[1] = parts[1].substring(0, digits);
            }

            value = parts.join('.');
        }

        return value;
    }

    /**
     * 进位保留数字指定位数
     * @param number {number|string}    要处理的数字
     * @param digits {number}           保留的小数位数
     * @returns {string}
     */
    function roundUp(number, digits) {

        return Number(number).toFixed(digits);
    }

    /**
     * 进位指定位数
     * @param number
     * @param digits
     */
    function roundUpDigits(number, digits) {

        var fullAndExtraDigits = formatDigits(number, digits + 1);

        var lastDigit = fullAndExtraDigits.substring(fullAndExtraDigits.length - 1);

        if (lastDigit !== '0') {
            fullAndExtraDigits = fullAndExtraDigits.substring(0, fullAndExtraDigits.length - 1) + '9';
        }

        return Number(fullAndExtraDigits).toFixed(digits);
    }

    /**
     * 截位保留指定位数
     * @param number {number|string}    要处理的数字
     * @param digits {number}           保留的小数位数
     * @returns {string}
     */
    function roundDown(number, digits) {

        return formatDigits(number, digits);
    }

    /**
     * 格式化为货币形式 1,234.000
     * @param value {number|string}     要处理的数字
     * @param digits {number}           保留的小数位数
     * @returns {string}
     */
    function formatCurrency(value, digits) {

        value = String(value);

        // 含有小数部分情况处理
        if (value.indexOf('.') > -1) {

            // 未指定小数位数时 获取实际的小数位数
            if (digits === undefined) {
                digits = getDigits(value);
            }

            return formatDigits(value, digits).replace(currencyRE, '$1,');
        }

        var digitsSpecified = digits !== undefined;

        // 未指定小数位时 默认1位 用于格式化
        if (!digitsSpecified) {
            digits = 1;
        }

        var currencyString = formatDigits(value, digits).replace(currencyRE, '$1,');

        // 指定了小数位时 直接返回格式化字符串
        if (digitsSpecified) {
            return currencyString;
        }

        // 未指定小数位数 则截取整数部分
        return currencyString.substring(0, currencyString.lastIndexOf('.'));
    }

    /**
     * 格式化小数位
     * @param value {number|string}     要处理的数字
     * @param digits {number}           保留的小数位数
     * @returns {string}
     */
    function formatDigits(value, digits) {

        var realDigits = getDigits(value);

        if (realDigits <= digits) {
            return Number(value).toFixed(digits);
        }

        var parts = String(value).split('.');

        parts[1] = parts[1].substring(0, digits);

        return parts.join('.');
    }

    /**
     * 获取小数位数
     * @param value {number|string}     要处理的数字
     * @returns {number}
     */
    function getDigits(value) {

        value = String(value);

        if (value.indexOf('.') > -1) {

            var parts = value.split('.');

            return parts[1].length;
        }

        return 0;
    }

    /**
     * 去除输入内容中非数字部分，且只保留digits位小数
     * @param str    要处理的内容
     */
    function formatLegalCurrency(str) {
        if (str) {
            // var reg = new RegExp("/(\.\d{" + digits + "}).+/g");
            return str.replace(/[^\d\.]/g, "").replace(/(\.\d+)\..+/g, "$1");
        }
    }

    /**
     * formatBankNumber
     * @param number
     */
    function formatBankNumber(number) {
        if (number) {
            return number = number.replace(/(\d{4})(?=\d)/g, "$1 ");
        }
    }

    var numbers = {
        filter: filter,
        roundUp: roundUp,
        roundDown: roundDown,
        roundUpDigits: roundUpDigits,
        formatDigits: formatDigits,
        formatCurrency: formatCurrency,
        formatLegalCurrency: formatLegalCurrency,
        formatBankNumber: formatBankNumber,
    };

    if (typeof define === 'function') {             // RequireJS || SeaJS
        define(function (require, exports, module) {
            module.exports = numbers;
        });
    } else if (typeof exports !== 'undefined') {    // NodeJS
        module.exports = numbers;
    } else if (typeof window !== 'undefined') {     // Browser
        window.Numbers = numbers;
    }

    // test cases
    /*
     numbers.filter('123d456.123f456', 4);   // '123456.1234'

     numbers.roundUp(123.4567, 3);           // '123.457'
     numbers.roundDown(123.4567, 3);         // '123.456'

     numbers.formatCurrency(1234.567);       // '1,234.567'
     numbers.formatCurrency(1234.567, 4);    // '1,234.5670'
     */

})(this);