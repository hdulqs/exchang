/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:20:59 
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
* <p>CoinApiUser</p>
* @author: Bat Admin
* @Date :  2018-05-22 14:20:59 
*/
@Table(name="blockchain_coin_api_user")
@Entity
@Data
@ToString
public class CoinApiUser extends BaseModel {

            /**
            * 状态
            *0:禁用
            */
                public static final Integer  STATUS0=0;
            /**
            * 状态
            *1:正常
            */
                public static final Integer  STATUS1=1;


    /**
    * id
    */

        @Id
    @Column(name="id")
    private String id;
    /**
    * 用户名
    */

            @NotNull(message="用户名不能为空")
            @Length(max = 32, message = "用户名长度必须介于1和32之间")
            @Words(field = "用户名", message = "用户名包含敏感词")
    @Column(name="user_name")
    private String userName;
    /**
    * 用户密码
    */

            @Length(max = 128, message = "用户密码长度必须介于1和128之间")
            @Words(field = "用户密码", message = "用户密码包含敏感词")
    @Column(name="user_password")
    private String userPassword;
    /**
    * 用户秘钥
    */

            @NotNull(message="用户秘钥不能为空")
            @Length(max = 64, message = "用户秘钥长度必须介于1和64之间")
            @Words(field = "用户秘钥", message = "用户秘钥包含敏感词")
    @Column(name="user_key")
    private String userKey;
    /**
    * 用户ip
    */

            @Length(max = 20, message = "用户ip长度必须介于1和20之间")
            @Words(field = "用户ip", message = "用户ip包含敏感词")
    @Column(name="user_ip")
    private String userIp;
    /**
    * 联系方式
    */

            @Length(max = 15, message = "联系方式长度必须介于1和15之间")
            @Words(field = "联系方式", message = "联系方式包含敏感词")
    @Column(name="user_phone")
    private String userPhone;
    /**
    * 通知地址
    */

            @NotNull(message="通知地址不能为空")
            @Length(max = 255, message = "通知地址长度必须介于1和255之间")
            @Words(field = "通知地址", message = "通知地址包含敏感词")
    @Column(name="notify_url")
    private String notifyUrl;
    /**
    * 状态
    */

            @NotNull(message="状态不能为空")
    @Column(name="status")
    private Integer status;

}
