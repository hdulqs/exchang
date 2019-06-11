package com.batsoft.model.module.member.vo;

import com.batsoft.model.module.member.User;
import com.batsoft.utils.StringUtils;

/**
 * Created by Administrator on 2017/9/20.
 */
public class UserVo  extends User {
    /**
     * 是否设置交易密码
     */
    private boolean hasTradePassword;
    /**
     * 是否手机认证
     */
    private boolean hasMobileValid;
    /**
     * 是否邮箱认证
     */
    private boolean hasEmailValid;
    /**
     * 是否Google认证
     */
    private boolean hasGoogleValid;
    /**
     * 是否实名认证
     */
    private boolean hasRealNameValid;
    /**
     * 实名安全
     */
    private String realNameSecurity;
    private String mobileSecurity;
    private String emailSecurity;

    public String getMobileSecurity() {
        if(StringUtils.isEmpty(this.getUserMobile())){
            return "";
        }else{
            return StringUtils.mobileReplice(this.getUserMobile());
        }
    }

    public String getEmailSecurity() {
        if(StringUtils.isEmpty(this.getUserEmail())){
            return "";
        }else{
            return StringUtils.emailReplice(this.getUserEmail());
        }
    }

    public String getRealNameSecurity() {
        if(StringUtils.isEmpty(this.getRealName())){
            return "";
        }else{
            return StringUtils.nameReplice(this.getRealName());
        }
    }

    public boolean isHasTradePassword() {
        if(StringUtils.isEmpty(this.getTradePassword())){
            return false;
        }else{
            return true;
        }
    }


    public boolean isHasMobileValid() {
        if(StringUtils.isEmpty(this.getUserMobile())){
            return false;
        }else{
            return true;
        }
    }



    public boolean isHasEmailValid() {
        if(StringUtils.isEmpty(this.getUserEmail())){
            return false;
        }else{
            return true;
        }
    }



    public boolean isHasGoogleValid() {
        if(OPENGOOGLEAUTH0.equals(this.getOpenGoogleAuth())){
            return false;
        }else{
            return true;
        }
    }

    public boolean isHasRealNameValid() {
        if(REALSTATE_NORMAL.equals(this.getRealState()) || REALSTATE_FAILE.equals(this.getRealState())){
            return false;
        }else{
            return true;
        }
    }


}
