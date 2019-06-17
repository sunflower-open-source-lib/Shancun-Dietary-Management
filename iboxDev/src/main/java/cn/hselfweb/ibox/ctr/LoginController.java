package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.User;
import cn.hselfweb.ibox.db.UserRepository;
import cn.hselfweb.ibox.db.Validation;
import cn.hselfweb.ibox.db.ValidationRepository;
import cn.hselfweb.ibox.utils.AliyunMessageUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class LoginController {

    private final UserRepository userRepository;

    private final ValidationRepository validationRepository;

    @Autowired
    public LoginController(UserRepository userRepository, ValidationRepository validationRepository) {
        this.validationRepository = validationRepository;
        this.userRepository = userRepository;
    }

    /**
     *登录接口
     * @param tel 电话号码
     * @param password 密码
     * @param request 带有用户表示的cookie信息
     * @return {code:0/1/2,msg:用户名或密码错误/登录成功/用户名不存在}
     */
    @RequestMapping(value = "/login", params={"tel","password"},method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(String tel,
                                     String password,
                                     //HttpSession session0,
                                     HttpServletRequest request) {
        System.out.println(request);
        System.out.println("heelo");
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userRepository.findByTel(tel);
        if (user == null) {
            map.put("code",2);
            map.put("msg", "用户名不存在");
            return map;
        }

        User user0 = userRepository.findByTelAndPassword(tel, password);
        if (user0 == null) {
            map.put("code",0);
            map.put("msg", "用户名或密码错误");
            return map;
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", user0.getUid());
            session.setMaxInactiveInterval(2*24*60*60);
            map.put("code",1);
            map.put("msg", "登录成功");
        }
        return map;
    }

    /**
     * 发送验证码请求
     * @param tel 电话号码
     * @return {code:0/1,msg:succeed/failed}
     */
    @RequestMapping(value = "validations/getiden",params="tel", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> getMessage(String tel) {
        
        System.out.println("已进入验证码发送请求");
        Map<String, Object> respon = new HashMap<>();
        Map<String, String> paramMap = new HashMap<>();
        String randomNum = createRandomNum(6);
        String jsonContent = "{\"code\":\"" + randomNum + "\"}";
        System.out.println(jsonContent);
        paramMap.put("phoneNumber", tel);
        paramMap.put("msgSign", "冰箱管家");
        paramMap.put("templateCode", "SMS_151085461");
        paramMap.put("TemplateParam", jsonContent);
        paramMap.put("jsonContent", jsonContent);
        try {
            SendSmsResponse sendSmsResponse = AliyunMessageUtil.sendSms(paramMap);
            System.out.println("状态码是： " + sendSmsResponse.getCode());
            if(sendSmsResponse.getCode().equals("OK")){
            System.out.println("状态码ok");
            Date now = new Date();
            Date dueDate = new Date(now.getTime() + 300000);
            Validation validation = new Validation();
            validation.setIdentity(randomNum);
            validation.setTel(tel);
            validation.setDuedate(dueDate);
            validationRepository.save(validation);
            respon.put("code",1);
            respon.put("msg", "succeed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            respon.put("code",0);
            respon.put("msg", "failed");
        }
        return respon;
    }

    public static String createRandomNum(int num) {
        String randomNumStr = "";
        for (int i = 0; i < num; i++) {
            int randomNum = (int) (Math.random() * 10);
            randomNumStr += randomNum;
        }
        return randomNumStr;
    }

    /**
     * 短信接口
     * @param tel
     * @param password
     * @param iden
     * @param message
     * @param username
     * @return
     */

    /**
     * 注册请求
     * @param tel 电话号码
     * @param password 密码
     * @param iden 验证码
     * @param message 用户信息
     * @param username 用户名
     * @return {code:0/1/2/3,msg:验证码不正确/注册成功/验证码失效/手机号已被注册}
     */
    @RequestMapping(value = "/validations/register",params={"tel","password","iden","message","username"}, method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> register(
             String tel,
             String password,
             String iden,
             String message,
             String username) {

        Map<String, Object> map = new HashMap<String, Object>();
        List<Validation> validationList = validationRepository.findAllByTelOrderByDuedateDesc(tel);
        if(validationList.size() == 0){
            map.put("code",0);
            map.put("msg","验证码不正确");
            return map;
        }
        Validation validation =  validationList.get(0);
        if(validation.getIdentity().equals(iden)){
            Date date = new Date();
            Date duedate = validation.getDuedate();
            User user0 = userRepository.findByTel(tel);
            if(user0 == null){
                if(date.compareTo(duedate) < 0){
                    User user = new User();
                    user.setPassword(password);
                    user.setUserName(username);
                    user.setInfo(message);
                    user.setTel(tel);
                    userRepository.save(user);
                    map.put("code",1);
                    map.put("msg","注册成功");
                }else{
                    map.put("code",2);
                    map.put("msg","验证码已失效");
                }
            }
            else{
                map.put("code",3);
                map.put("msg","手机号已被注册");
            }
        }else{
            map.put("code",0);
            map.put("msg","验证码不正确");
        }
        return map;
    }

    @RequestMapping("login/log/{username}/{password}")
    public @ResponseBody
    String log(
            @PathVariable("username") String username,
            @PathVariable("password") String password
    ) {
        HttpHeaders headers = new HttpHeaders();
        return "succsess";
    }

    /**
     * token接口
     * @param session 用户的session
     * @return 返回Token
     */
    @RequestMapping("/token")
    public Map<String, String> token(HttpSession session) {
        return Collections.singletonMap("token", session.getId());
    }
}
