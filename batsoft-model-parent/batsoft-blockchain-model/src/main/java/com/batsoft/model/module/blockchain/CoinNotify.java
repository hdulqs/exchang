/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:20:29 
*/

package com.batsoft.model.module.blockchain;
import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import com.batsoft.utils.annotation.Money;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;

/**
* 
* <p>CoinNotify</p>
* @author: Bat Admin
* @Date :  2018-05-22 14:20:29 
*/
@Table(name="blockchain_coin_notify")
@Entity
@Data
@ToString
public class CoinNotify extends BaseModel {



    /**
    * id
    */

        @Id
    @Column(name="id")
    private String id;
    /**
    * 回调地址
    */

            @NotNull(message="回调地址不能为空")
            @Length(max = 16, message = "回调地址长度必须介于1和16之间")
            @Words(field = "回调地址", message = "回调地址包含敏感词")
    @Column(name="notify_url")
    private String notifyUrl;
    /**
    * 回调用户
    */

            @NotNull(message="回调用户不能为空")
            @Length(max = 30, message = "回调用户长度必须介于1和30之间")
            @Words(field = "回调用户", message = "回调用户包含敏感词")
    @Column(name="notify_user")
    private String notifyUser;
    /**
    * 回调方法
    */

            @NotNull(message="回调方法不能为空")
            @Length(max = 20, message = "回调方法长度必须介于1和20之间")
            @Words(field = "回调方法", message = "回调方法包含敏感词")
    @Column(name="notify_method")
    private String notifyMethod;
    /**
    * 回调参数
    */

            @Words(field = "回调参数", message = "回调参数包含敏感词")
    @Column(name="notify_parameter")
    private String notifyParameter;
    /**
    * 回调类型POST/GET
    */

            @Length(max = 20, message = "回调类型POST/GET长度必须介于1和20之间")
            @Words(field = "回调类型POST/GET", message = "回调类型POST/GET包含敏感词")
    @Column(name="notify_type")
    private String notifyType;
    /**
    * 回调次数
    */

    @Column(name="notify_count")
    private Integer notifyCount;
    /**
    * 回调状态
    */

            @Length(max = 20, message = "回调状态长度必须介于1和20之间")
            @Words(field = "回调状态", message = "回调状态包含敏感词")
    @Column(name="notify_state")
    private String notifyState;

}
