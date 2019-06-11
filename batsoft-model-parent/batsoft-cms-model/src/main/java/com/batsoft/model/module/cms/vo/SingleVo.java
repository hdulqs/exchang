package com.batsoft.model.module.cms.vo;

import com.batsoft.model.module.cms.Single;
import com.batsoft.utils.Encodes;
import lombok.ToString;

/**
 * @aouthor LouSir
 * @date 2018/5/28 13:54
 */
@ToString
public class SingleVo extends Single {

    public String singleForm;
    public String title;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getSingleForm() {
        return singleForm;
    }

    @Override
    public void setSingleForm(String singleForm) {
        this.singleForm = Encodes.unescapeHtml(singleForm);
    }
}
