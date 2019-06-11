package com.batsoft.utils.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/11.
 */
public class MoneyValidator implements ConstraintValidator<Money, BigDecimal> {


    int point=2;

    @Override
    public void initialize(Money money) {
        point= money.point();
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext arg1) {
        DecimalFormat decimalFormat=new DecimalFormat("0.00000000");
        String moneyStr= decimalFormat.format(value);

        // TODO Auto-generated method stub
        String  moneyReg = "^(([1-9][0-9]*)|(([0]\\.\\d{1,"+point+"}|[1-9][0-9]*\\.\\d{1,"+point+"})))$";//表示金额的正则表达式
       // String  moneyReg = "^(([1-9]\\d{0,9})|0)(\\.\\d{1,"+2+"})?$";//表示金额的正则表达式
         Pattern moneyPattern = Pattern.compile(moneyReg);
        if (value == null) {
            return true;
        }
        return moneyPattern.matcher(moneyStr).matches();
    }

}