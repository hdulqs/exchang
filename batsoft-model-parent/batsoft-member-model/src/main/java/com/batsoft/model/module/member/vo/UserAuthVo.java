package com.batsoft.model.module.member.vo;

import lombok.Data;

import java.io.Serializable;


/**
 * Created by Administrator on 2017/9/20.
 */
@Data
public class UserAuthVo implements Serializable {
    //证件类型
    private Integer identity_type;
    //证件号码
    private String identity;
    //姓氏
    private String first_name;
    //名字
    private String last_name;
    //身份证正面
    private String front_pic1;
    //身份证背面
    private  String back_pic1;
    //手持身份证
    private String hand_pic1;
    private Integer sex;

    //地区
    private String country;

    private String identity_en;
    //姓氏
    private String first_name_en;
    //名字
    private String last_name_en;
    //护照正面
    private String front_pic_en1;
    //护照背面
    private  String back_pic_en1;
    //手持护照
    private String hand_pic_en1;

}
