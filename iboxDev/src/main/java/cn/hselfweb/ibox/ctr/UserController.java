package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.IceOrder;
import cn.hselfweb.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class UserController {
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final IceBoxRepository iceBoxRepository;

    @Autowired
    public UserController(FamilyRepository familyRepository, UserRepository userRepository, IceBoxRepository iceBoxRepository) {
        this.iceBoxRepository = iceBoxRepository;
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }

    /**
     * 获取冰箱订阅者信息
     * @param macip 冰箱唯一标识
     * @return 订阅者信息列表
     */
    @RequestMapping(value = "users/geticeboxuserinfo", method = RequestMethod.POST)
    @ResponseBody
    public List<IceOrder>  getIceBoxUserInfo(
            String macip
    ) {
        System.out.println("helloworld");
        List<IceOrder> iceOrders = new ArrayList<IceOrder>();
        IceBox iceBox = iceBoxRepository.getIceBoxByIceId(macip);
        Long fid = iceBox.getFid();
        List<Family> families = familyRepository.findAllByFid(fid);
        for(int i = 0; i < families.size(); i++){
            Family family = families.get(i);
            Long uid = family.getUid();
            Long admin = family.getRole();
            User user = userRepository.findByUid(uid);
            IceOrder iceOrder = new IceOrder();
            iceOrder.setUser(user);
            iceOrder.setAdmin(admin);
            iceOrders.add(iceOrder);
        }
        return iceOrders;
    }


    /**
     * 上传用户头像
     * @param headUrl 用户头像url
     * @param request 带有用户信息的request对象
     * @return {code:0/1,msg:failed/succeed}
     */
    @RequestMapping(value = "users/putheadurl",params={"headUrl"}, method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> saveHeadUrl(
            String headUrl,
            HttpServletRequest request
    ) {
        System.out.println(headUrl);
        Map<String,Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        User user = userRepository.findByUid(uid);
        user.setHeadUrl(headUrl);
        User user0 = userRepository.save(user);
        if (user0.getHeadUrl() == headUrl) {
            respon.put("code",1);
            respon.put("msg", "succeed");
        } else {
            respon.put("code",0);
            respon.put("msg", "failed");
        }
        return respon;
    }

    /**
     *  获取当前用户信息
     * @param request 带有用户信息的request对象
     * @return 用户信息
     */
    @RequestMapping(value = "users/getUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public User getUserInfo(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        return userRepository.findByUid(uid);
    }
}
