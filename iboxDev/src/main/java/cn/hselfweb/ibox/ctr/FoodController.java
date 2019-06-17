package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.FoodInfo;
import cn.hselfweb.ibox.db.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 食物管理
 */
@RestController
public class FoodController {


    private final FoodRepository foodRepository;


    private final RecordRepository recordRepository;


    private final OfficialCardRepository officialCardRepository;


    private final UnOfficialCardRepository unOfficialCardRepository;

    private final IceBoxRepository iceBoxRepository;


    @Autowired
    public FoodController(FoodRepository foodRepository, RecordRepository recordRepository, OfficialCardRepository officialCardRepository, UnOfficialCardRepository unOfficialCardRepository, IceBoxRepository iceBoxRepository) {
        this.foodRepository = foodRepository;
        this.officialCardRepository = officialCardRepository;
        this.recordRepository = recordRepository;
        this.unOfficialCardRepository = unOfficialCardRepository;
        this.iceBoxRepository = iceBoxRepository;
    }

    private static List<Record> removeDuplicateOrder(List<Record> orderList) {
        Set<Record> set = new TreeSet<Record>(Comparator.comparing(Record::getUuid));
        set.addAll(orderList);
        return new ArrayList<>(set);
    }

    /**
     * 获取冰箱食物列表信息
     * @param macip 冰箱唯一标识
     * @return 食物信息列表
     */
    @RequestMapping(value = "/foods/getallfoodlist", params={"macip"},method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getAllFoodlist(String macip) {

        HashMap<String,Object> map = new HashMap<>();
        System.out.println("已进入获取食物列表");
        List<FoodInfo> foodInfoList = new ArrayList<>();
        List<Record> recordList = recordRepository.findAllByIceId(macip);
        for (Record data : recordList) {
            System.out.println(data.getUuid());
        }
        recordList = removeDuplicateOrder(recordList);

        for (int i = 0; i < recordList.size(); i++) {
            String uuid = recordList.get(i).getUuid();
            System.out.println("第 " + i + recordList.get(i).getUuid());
            List<OfficialCard> officialCard = officialCardRepository.getAllByUuid(uuid);
            if (officialCard.size() == 0) {
                System.out.println("官方卡里没有找到");
                UnOfficialCard unOfficialCard = unOfficialCardRepository.getByUuid(uuid);
                if(unOfficialCard == null){

                    continue;
                }
                System.out.println(unOfficialCard.getFoodWeight());
                FoodInfo foodInfo = new FoodInfo();
                if (unOfficialCard.getFoodWeight() > 0) {
                    foodInfo.setWeight(unOfficialCard.getFoodWeight());
                    foodInfo.setFoodName(unOfficialCard.getFoodName());
                    foodInfo.setFoodUrl(unOfficialCard.getFoodUrl());
                    foodInfo.setType(unOfficialCard.getType());
                    foodInfo.setTime(unOfficialCard.getFoodTime());
                    foodInfo.setPercent(unOfficialCard.getPercent());
                    foodInfo.setFoodId(unOfficialCard.getUuid());
                    List<Record> records = recordRepository.findByUuidOrderByOpDateDesc(uuid);
                    Date OpDate = records.get(0).getOpDate();
                    foodInfo.setStartTime(OpDate);
                    foodInfo.setFoodPhotoUrl(records.get(0).getFoodPhoto());
                    foodInfoList.add(foodInfo);
                }

            } else {
                //System.out.println(officialCard.toString());
                Long foodId = officialCard.get(0).getFoodId();
                System.out.println("第 foodid" + i + foodId);
                FoodInfo foodInfo = new FoodInfo();
                Food food = foodRepository.getAllByFoodId(foodId);
                if (food.getFoodWeight() == 0) {
                    officialCard.remove(food);
                } else {
                    foodInfo.setWeight(food.getFoodWeight());//食材重量
                    foodInfo.setFoodName(food.getFoodName());//食材二级分类
                    foodInfo.setFoodUrl(food.getFoodUrl());//二级分类图标
                    Long day = food.getFoodTime();//用于计算保质期
                    foodInfo.setComment(food.getComment());//食材描述
                    foodInfo.setType(food.getType());//存储方式
                    foodInfo.setTime(food.getFoodTime());
                    foodInfo.setFoodId(food.getFoodId().toString());
                    foodInfo.setPercent((food.getPercent()));
                    foodInfo.setTareWeight(food.getFoodWeight());
                    List<Record> records = recordRepository.findByUuidOrderByOpDateDesc(uuid);
                    Date OpDate = records.get(0).getOpDate();
                    foodInfo.setStartTime(OpDate);
                    foodInfo.setFoodPhotoUrl(records.get(0).getFoodPhoto());
                    foodInfoList.add(foodInfo);
                }
            }
        }
        if(foodInfoList == null){
            map.put("code",0);
            map.put("msg","冰箱没有食材");
        }else{
            map.put("code",1);
            map.put("msg","食材获取成功");
        }
        map.put("data",foodInfoList);
        return map;
    }

    /**
     * food record 双表存入及更新接口
     *
     * @param macip       就是冰箱的ice_id
     * @param foodName    食物名称
     * @param uuid        官方卡或非官方卡的索引ID
     * @param comment     介绍
     * @param foodTime    食物存储时间
     * @param type        当前的操作是冷冻还是冷藏
     * @param opFlag      是首次存入还是续存
     * @param opDate      存入食物的时间
     * @param foodParent  食物的上一级父亲
     * @param foodPhoto   食物的照片链接
     * @param foodWeight  食物的重量
     * @param foodPercent 食物距离过期的百分比
     * @param taretWeight 皮重
     * @return 返回操作成功或者失败
     */
    @RequestMapping(value = "/foods/putFoodDataIn", method = RequestMethod.POST)
    @ResponseBody
    public List<String> putFoodData(
            String macip,
            String foodName,
            String uuid,
            String comment,
            Long foodTime,
            Long type,
            Long opFlag,
            String opDate,
            Long foodParent,
            String foodPhoto,
            Long foodWeight,
            double foodPercent,
            Long taretWeight
    ) {
        System.out.println("食材是："+comment);
        List<String> message = new ArrayList<>();
        System.out.println(foodWeight);
        Date date = new Date();
        System.out.println("uuid: " + uuid);
        DateFormat format= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try{
            date = format.parse(opDate);
        } catch (ParseException e) {
                e.printStackTrace();
        }
        Food food = new Food();
        IceBox iceBox = iceBoxRepository.getIceBoxByIceId(macip);
        UnOfficialCard unOfficialCard = new UnOfficialCard();
        OfficialCard officialCard = officialCardRepository.getByUuid(uuid);
        if(officialCard != null){
            Long foodId1 = officialCard.getFoodId();
            if(officialCardRepository.existsByFoodId(foodId1)){
                System.out.println("官方食材");
                food = foodRepository.getAllByFoodId(foodId1);
                //food.setFoodId(foodId1);
                //food.setFoodName(foodName);
                //food.setComment(comment);
                if(food == null){
                    System.out.println("food为空");
                }
                food.setFoodTime(foodTime);
                //food.setFoodUrl(foodPhoto);
                food.setFoodWeight(foodWeight);
                food.setType(type);
                food.setPercent(foodPercent);
                food.setFoodParent(foodParent);
                food.setComment(comment);
                foodRepository.save(food);

                Record record = new Record();

                /*long record_id = new Date().getTime();
                System.out.println("record_id:  " + record_id);
                record.setRecordId(record_id);*/
                record.setUuid(uuid);
                record.setIceId(macip);
                record.setFid(iceBox.getFid());
                record.setOpFlag(0L);
                record.setOpDate(new Date());
                record.setTareWeight(taretWeight);
                record.setFoodWeight(foodWeight);
                record.setFoodPhoto(foodPhoto);
                recordRepository.save(record);
                message.add("success");
                System.out.println("官方卡food插入成功啦");
            }
        }else{
            System.out.println("非官方食材");
            unOfficialCard.setUuid(uuid);
            unOfficialCard.setFid(iceBox.getFid());
            unOfficialCard.setFoodUrl(foodPhoto);
            unOfficialCard.setType(type);
            unOfficialCard.setFoodTime(foodTime);
            unOfficialCard.setFoodName(foodName);
            unOfficialCard.setFoodWeight(foodWeight);
            unOfficialCard.setPercent(foodPercent);
            unOfficialCardRepository.save(unOfficialCard);
        }



        return message;
    }

    /**
     *获取食物
     * @param uuid 卡的uuid
     * @return {code:1/0,data:数据/,msg:食物获取成功/没有该食物}
     */
    @RequestMapping(value = "/foods/getfood", params={"uuid"},method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getFood(String uuid) {
        System.out.println("获取食物");
        System.out.println("uuid: "+uuid);

        Map<String,Object> map = new HashMap<String,Object>();
        OfficialCard officialCard = officialCardRepository.getByUuid(uuid);
        FoodInfo foodInfo = new FoodInfo();
        List<FoodInfo> foodInfoList = new ArrayList<>();
        if(officialCard != null){
            Long foodId =  officialCard.getFoodId();
            Food food = foodRepository.getAllByFoodId(foodId);
            if(food != null){
                System.out.println("在官方卡里");
                if(food.getFoodWeight() > 0) {

                    foodInfo.setWeight(food.getFoodWeight());//食材重量
                    foodInfo.setFoodName(food.getFoodName());//食材二级分类
                    foodInfo.setFoodUrl(food.getFoodUrl());//二级分类图标
                    Long day = food.getFoodTime();//用于计算保质期
                    foodInfo.setComment(food.getComment());//食材描述
                    foodInfo.setType(food.getType());//存储方式
                    foodInfo.setTime(food.getFoodTime());
                    foodInfo.setFoodId(food.getFoodId().toString());
                    foodInfo.setPercent((food.getPercent()));
                    List<Record> records = recordRepository.findByUuidOrderByOpDateDesc(uuid);
                    Record record = new Record();
                    for(int i = 0; i < records.size(); i++){
                        if(records.get(i).getOpFlag() == 0){
                            record = records.get(i);
                            break;
                        }
                    }
                    Date OpDate = record.getOpDate();
                    System.out.println(OpDate.toString());
                    foodInfo.setTareWeight(record.getTareWeight());
                    foodInfo.setStartTime(OpDate);
                    foodInfo.setFoodPhotoUrl(record.getFoodPhoto());
                    foodInfoList.add(foodInfo);
                    map.put("code", 1);
                    map.put("data", foodInfoList);
                    map.put("msg", "获取食物成功");
                }else{
                    foodInfo.setWeight(food.getFoodWeight());//食材重量
                    foodInfo.setComment(food.getComment());//食材描述
                    foodInfo.setFoodName(food.getFoodName());//食材二级分类
                    foodInfoList.add(foodInfo);
                    map.put("code", 1);
                    map.put("data", foodInfoList);
                    map.put("msg", "获取食物成功");
                }
            }else{
                UnOfficialCard unOfficialCard = unOfficialCardRepository.getByUuid(uuid);
                if(unOfficialCard != null){
                    System.out.println("在非官方卡里");
                    if(unOfficialCard.getFoodWeight()>0){
                        foodInfo.setWeight(unOfficialCard.getFoodWeight());
                        foodInfo.setFoodName(unOfficialCard.getFoodName());
                        foodInfo.setFoodUrl(unOfficialCard.getFoodUrl());
                        foodInfo.setType(unOfficialCard.getType());
                        foodInfo.setTime(unOfficialCard.getFoodTime());
                        foodInfo.setPercent(unOfficialCard.getPercent());
                        foodInfo.setFoodId(unOfficialCard.getUuid());
                        List<Record> records = recordRepository.findByUuidOrderByOpDateDesc(uuid);

                        Date OpDate = records.get(0).getOpDate();
                        foodInfo.setStartTime(OpDate);
                        foodInfo.setFoodPhotoUrl(records.get(0).getFoodPhoto());
                        foodInfoList.add(foodInfo);
                        map.put("code",1);
                        map.put("data",unOfficialCard);
                        map.put("msg","获取食物成功");
                    }else{
                        map.put("code",0);
                        map.put("msg","没有该食物");
                    }
                }
            }
        }else{
            map.put("code",0);
            map.put("msg","没有该食物");
        }

        return map;
    }

}
//00FF0FF000FFCF4D54B1104842778919C204B640
//00FF0FF000FFCF4D54B1104842778919C204B640