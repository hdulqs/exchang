define(function (require) {
    !function() {
        //手机号
        jQuery.validator.addMethod("mobile", function(value, element) {
            var length = value.length;
            var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
            return this.optional(element) || (length == 11 && mobile.test(value));
        });

         //校验密码：只能建议输入6-20个使用字母、数字和符号两种及以上组合
        jQuery.validator.addMethod("password", function(value, element) {
            var patrn=/^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]{6,20}$/;
            return this.optional(element) || (patrn.test(value));
        });

    }();

});