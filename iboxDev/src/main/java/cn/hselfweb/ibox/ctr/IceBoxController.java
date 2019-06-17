package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.*;
import cn.hselfweb.ibox.utils.QrCodeUtil;
import cn.hselfweb.ibox.utils.UpLoadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

import static cn.hselfweb.ibox.ctr.IceBoxController.getRandom;

@RestController
public class IceBoxController {

    private final IceBoxRepository iceBoxRepository;

    private final UserRepository userRepository;

    private final FamilyRepository familyRepository;

    private final ConnectRepository connectRepository;


    @Autowired
    public IceBoxController(IceBoxRepository iceBoxRepository, UserRepository userRepository, FamilyRepository familyRepository,ConnectRepository connectRepository) {
        this.iceBoxRepository = iceBoxRepository;
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
        this.connectRepository = connectRepository;
    }

    /**
     * 获取冰箱信息
     * @param macip 冰箱唯一标识
     * @return IceBox
     */
    /*@RequestMapping(value = "/geticeboxinfo", method = RequestMethod.POST)
    @ResponseBody
    public IceBox query(String macip) {

        return iceBoxRepository.getIceBoxByIceId(macip);
    }*/

    /**
     * 创建冰箱
     * @param nickName 冰箱昵称
     * @param fid 家庭 如没有家庭创建默认家庭时此参数不用提供
     * @param request userSession
     * @return {code:0/1/2,msg:../../..}
     */
    @RequestMapping(value = "/iceboxes/createicebox", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> createBox(String nickName,
                                        Long fid,
                                        HttpServletRequest request) {
        Map<String,Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long)session.getAttribute("user");
        User user = userRepository.findByUid(uid);
        List<Family> aa = familyRepository.findAllByOrderByFidDesc();
        Long fid0;
        if(aa.size() > 0){
            fid0 = aa.get(0).getFid() + 1;
        }else{
            fid0 = 0L;
        }
        if(fid == null){
            Family family = new Family();
            family.setName(user.getUserName()+"的家庭");
            family.setUid(uid);
            family.setRole(1L);
            family.setFid(fid0);
            Family family1 = familyRepository.save(family);
            fid = family1.getFid();
            if(fid != null){
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
                respon.put("fid",fid);
                System.out.println("默认家庭创建成功");
            }else{
                respon.put("code",2);
                respon.put("msg","默认家庭创建失败");
            }
        }
        String iceId = getRandom();
        IceBox iceBox = new IceBox(iceId,fid,nickName);
        IceBox iceBox0 = iceBoxRepository.save(iceBox);
        if(iceBox0.getIceId() != null){
            respon.put("code",1);
            respon.put("msg","冰箱创建成功");
            respon.put("iceId",iceId);
        }else{
            respon.put("code",0);
            respon.put("msg","冰箱创建失败");
        }
        return respon;
    }

    public static String getRandom(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-","");
    }


    /**
     *删除冰箱
     * @param iceId 冰箱id
     * @param request uid session
     * @return {code:0/1,msg:冰箱不存在/删除冰箱成功}
     */
    @Transactional
    @RequestMapping(value = "iceboxes/delect", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> delect(String iceId,
                         HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long)session.getAttribute("user");
        User user = userRepository.findByUid(uid);
        IceBox iceBox = iceBoxRepository.getIceBoxByIceId(iceId);
        if(iceBox != null){
            iceBoxRepository.deleteByIceId(iceId);
            map.put("code",1);
            map.put("msg","删除冰箱成功");
        }else{
            map.put("code",0);
            map.put("msg","冰箱不存在");
        }
        return map;
    }


    /**
     * 获取我的所有冰箱信息
     * @param request 请求session
     * @return {code:1/0,msg:获取冰箱信息成功/没有冰箱,data:冰箱数据/-}
     */
    @RequestMapping(value = "iceboxes/getmyicebox", method = RequestMethod.GET)
    public @ResponseBody
    Map<String,Object> getBoxId(HttpServletRequest request){
        ArrayList<IceBox> list = new ArrayList<IceBox>();
        Map<String,Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        System.out.println(uid);
        List<Family> families = familyRepository.findAllByUid(uid);
        int length = families.size();
        System.out.println("length长度"+length);
        if(length > 0){
            for(int i = 0; i < length; i++){
                Long fid = families.get(i).getFid();
                System.out.println(("fid为" + fid));
                List<IceBox> iceBoxes = iceBoxRepository.getAllByFid(fid);
                for(int j = 0; j < iceBoxes.size(); j++){
                    IceBox iceBox = iceBoxes.get(j);
                    list.add(iceBox);
                }
            }
            respon.put("code",1);
            respon.put("data",list);
            respon.put("msg","获取冰箱信息成功");
        }
        else{
            respon.put("code",0);
            respon.put("msg","还没有冰箱");
        }
        return respon;
    }

    /**
     * 手机端关联底端冰箱
     * @param iceId0  手机端创建虚拟冰箱的冰箱id
     * @param iceId1 底端真实冰箱的冰箱id
     * @param request 请求session
     * @return {code:0/1/2,msg:手机端虚拟冰箱不存在/冰箱关联成功/底端冰箱不存在}
     */
    @Transactional
    @RequestMapping(value = "iceboxes/connectbox", method = RequestMethod.POST)
    public @ResponseBody
    Map<String,Object> connectBox(
            String iceId0,//手机端创建虚拟冰箱的冰箱id
            String iceId1,//底端真实冰箱的冰箱id
            HttpServletRequest request){
        Map<String,Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        IceBox iceBox0 = iceBoxRepository.getIceBoxByIceId(iceId0);
        IceBox iceBox1 = iceBoxRepository.getIceBoxByIceId(iceId1);
        if(iceBox0 == null){
            respon.put("code",0);
            respon.put("msg","手机端虚拟冰箱不存在");
            return respon;
        }
        if(iceBox1 == null){
            respon.put("code",2);
            respon.put("msg","底端冰箱不存在");
            return respon;
        }
        iceBox1.setIceName(iceBox0.getIceName());
        iceBox1.setFid(iceBox0.getFid());
        iceBoxRepository.save(iceBox1);
        iceBoxRepository.deleteByIceId(iceId0);
        respon.put("code",1);
        respon.put("msg","关联冰箱成功");
        return respon;
    }
}
