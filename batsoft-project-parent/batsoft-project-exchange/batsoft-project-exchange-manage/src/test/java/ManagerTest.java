import com.batsoft.AdminApplication;
import com.batsoft.app.exchange.CustomerAccountController;
import com.batsoft.model.module.system.manage.AppUser;
import com.batsoft.service.module.system.service.manage.AppUserService;
import com.batsoft.shiro.PasswordHelper;
import com.batsoft.utils.Md5Util;
import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AdminApplication.class)
public class ManagerTest {


//    @Autowired
//    AppUserService appUserService;

    @Test
    public void test(){
        String password = "123456";
        String salt = "94e575d995bffe79f89b079403047ede";
        String md5Encode = (Md5Util.MD5Encode(password,"utf-8"));
//        AppUser appUser =  appUserService.findAppUser("admin");
        PasswordHelper passwordHelper = new PasswordHelper();
//        String passwd  = passwordHelper.encryString("123456",appUser.getSalt());
        String passwd  = passwordHelper.encryString(md5Encode,salt);
        System.out.println("passwd:"+passwd);


    }
}
