import java.io.Serializable;

/**
 * Created by lucl on 2017/9/13.
 */
public interface BaseService<T, PK extends Serializable> {
    /**
     * 充值接口
     * @param entity
     * @return
     */
    public String recharge(T entity);
}
