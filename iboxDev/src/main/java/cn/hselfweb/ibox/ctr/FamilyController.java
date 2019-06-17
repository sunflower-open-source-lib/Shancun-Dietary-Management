package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.Family0;
import cn.hselfweb.ibox.db.*;
import cn.hselfweb.ibox.utils.QrCodeUtil;
import cn.hselfweb.ibox.utils.UpLoadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.filechooser.FileSystemView;
import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 冰箱管理
 */
@RestController
@RequestMapping
public class FamilyController {

    private final FamilyRepository familyRepository;

    private final UserRepository userRepository;

    private final IceBoxRepository iceBoxRepository;



    @Autowired
    public FamilyController(FamilyRepository familyRepository, UserRepository userRepository,IceBoxRepository iceBoxRepository) {
        this.familyRepository = familyRepository;
        this.userRepository = userRepository;
        this.iceBoxRepository = iceBoxRepository;
    }

    /**
     *创建家庭
     * @param familyName  家庭名称
     * @param request 带有用户信息的cookie
     * @return {code:0/1/2 msg:失败/成功/已创建}
     */
    @RequestMapping(value = "/families/createfamily/{familyname}", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> creatFamily(
            @PathVariable("familyname") String familyName,
            HttpServletRequest request
    ) {
        Map<String, Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        List<Family> families = familyRepository.findAllByUid(uid);
        List<Family> aa = familyRepository.findAllByOrderByFidDesc();
        Long fid;
        if(aa.size() > 0){
            fid = aa.get(0).getFid() + 1;
        }else{
            fid = 0L;
        }
        Family family = new Family();
        if (families.size() == 0) {
            family.setName(familyName);
            family.setUid(uid);
            family.setRole(1L);
            family.setFid(fid);
            Family family1 = familyRepository.save(family);
            if (family1.getFid() != null) {
                //生成家庭二维码
                QrCodeUtil qrCodeUtil = new QrCodeUtil();
                String url = "fid="+fid;
                String path = "D://testQrcode//";
                String fileName = fid+".jpg";
                qrCodeUtil.createQrCode(url, path, fileName);
                //存储到七牛云
                UpLoadUtil upLoadUtil = new UpLoadUtil();
                upLoadUtil.setKey(""+fid);
                //upLoadUtil.setLocalFilePath(path+fileName);
                upLoadUtil.setLocalFilePath("D://testQrcode//"+fileName);
                upLoadUtil.upload();
                respon.put("fid",family.getFid());
                respon.put("code", 1);
                respon.put("msg", "家庭创建成功");
            } else {
                respon.put("code", 0);
                respon.put("msg", "家庭创建失败");
            }
        } else {
            respon.put("code", 2);
            respon.put("msg", "你已经创建过家庭了");
        }
        return respon;
    }

    /**
     * 获取当前用户所在家庭数据
     * @param request uidSession
     * @return List Family
     */
    @RequestMapping(value = "families/getFamilyInfo", method = RequestMethod.GET)
    public @ResponseBody
    List<Family> getFamilyInfo(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        return familyRepository.findAllByUid(uid);
    }

    /**
     *底端获取家庭信息
     * @param macip 冰箱id
     * @return {code:0/1,msg:获取家庭失败/获取家庭成功,data:-/家庭数据}
     */
    @RequestMapping(value = "families/getfamily",params = {"macip"}, method = RequestMethod.POST)
    public @ResponseBody
    Map<String,Object> getFamily(
            String macip
    ) {
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        IceBox iceBox = iceBoxRepository.getIceBoxByIceId(macip);
        Long fid = iceBox.getFid();
        data.put("fid",fid);
        List<Family> families = familyRepository.findAllByFid(fid);
        List<Family0> family0s = new ArrayList<>();
        if(families != null){
            for(int i = 0; i < families.size(); i++){
                Family0 family0 = new Family0();
                Family family = families.get(i);
                Long uid = family.getUid();
                family0.setRole(family.getRole());
                User user = userRepository.findByUid(uid);
                family0.setUserName(user.getUserName());
                family0s.add(family0);
            }
            data.put("user",family0s);
            map.put("code",1);
            map.put("msg","获取家庭成功");
            map.put("data",data);
        }else{
            map.put("code",0);
            map.put("msg","获取家庭失败");
        }
        return map;
    }


    /**
     * 获取当前用户在哪些家庭中
     * @param request uidSession
     * @return List Family
     */
    @RequestMapping(value = "families", method = RequestMethod.GET)
    public @ResponseBody
    List<Family> getFamilies(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        return familyRepository.findAllByUid(uid);
    }

    /**
     *退出家庭
     * @param fid     家庭id
     * @param request uidSession
     * @return {code:0/1/2,msg:该成员为管理员/成功/家庭不存在}
     */

    @RequestMapping(value = "families/outFamily", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Map<Object,Object> outFamily(
            Long fid,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        Map<Object,Object> map = new HashMap<>();
        List<Family> families = familyRepository.findAllByFid(fid);
        if(families.size()!=0){
            Family family = families.get(0);
            if(family.getRole() != 1){
                familyRepository.deleteByFidAndUid(fid, uid);
                map.put("code",1);
                map.put("msg","删除成功");
            }else{
                System.out.println("该成员为管理员");
                map.put("code",0);
                map.put("msg","该成员为管理员");
            }
        }else{
            System.out.println("家庭不存在");
            map.put("code",2);
            map.put("msg","家庭不存在");
        }
        return map;
    }

    /**
     * 邀请人加入家庭
     * @param fid     家庭id
     * @param request uidSession
     * @return {code:0/1,msg:你已经属于该家庭/加入家庭成功}
     */
    @RequestMapping(value = "families/invitation", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> invitation(
            Long fid,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Map<String,Object> map = new HashMap<>();
        Long uid = (Long) session.getAttribute("user");
        Family family = familyRepository.findByFidAndUid(fid, uid);
        if(family == null){
            List<Family> families = familyRepository.findAllByFid(fid);
            Family family0 = new Family();
            family0.setName(families.get(0).getName());
            family0.setRole(0L);
            family0.setFid(fid);
            family0.setUid(uid);
            familyRepository.save(family0);
            map.put("code",1);
            map.put("msg","加入家庭成功");
        }else{
            map.put("code",0);
            map.put("msg","你已经属于该家庭");
        }
        return map;
    }
}
