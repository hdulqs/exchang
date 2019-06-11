package com.batsoft.core.common;


public final class Constants {
    /**
     * UTF-8编码
     */
    public static final String UTF8 = "UTF-8";
    public final static String MSG = "msg";
    public final static JsonResult.ResultCode SUCCESS = JsonResult.ResultCode.SUCCESS;
    public final static JsonResult.ResultCode FAILED = JsonResult.ResultCode.FAILE;
    public final static JsonResult.ResultCode NO_LOGIN =JsonResult.ResultCode.NO_LOGIN;
    public final static JsonResult.ResultCode OTHER_CODE= JsonResult.ResultCode.OTHERS;
    public final static String STATUS_200 = "200";
    public final static String SAVE="save";
    public final static String UPDATE="update";
    public final static String MONEY_FORMAT= "0.000000";
    /**
     * 平台LOGO
     */
    public final static String SITE_LOGO_URL = "/upload/logo";
    /**
     * 文件上传路径
     */
    public final static String UPLOAD_ATTACH = "upload/attach";

    /******************************cache*********************************/
    public static final String CACHE_CONFIG_KEY="appConfig:";

    public static final String CACHE_WEB_KEY="web:";
    public static final String CACHE_SECURITY_KEY="security:";

    public static final String CACHE_EX_PREFIX="exchange:";

    /*******************************币账户************************************/
    public static final String CNY_NAME="人民币";
    public static final String CNY_CODE="CNY";
    public static final String USD_NAME="美元";
    public static final String USD_CODE="USD";

}
