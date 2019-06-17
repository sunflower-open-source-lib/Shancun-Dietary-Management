package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
/**
 * @author Cyberhan
 *
 * @version v1
 */
@RestController
public class RecordController {
    private final RecordRepository recordRepository;

    private final OfficialCardRepository officialCardRepository;

    private final UnOfficialCardRepository unOfficialCardRepository;

    private final FoodRepository foodRepository;

    @Autowired
    public RecordController(RecordRepository recordRepository,OfficialCardRepository officialCardRepository,UnOfficialCardRepository unOfficialCardRepository,FoodRepository foodRepository) {
        this.recordRepository = recordRepository;
        this.officialCardRepository = officialCardRepository;
        this.unOfficialCardRepository = unOfficialCardRepository;
        this.foodRepository = foodRepository;
    }

    /**
     *
     * @param macip 冰箱id
     * @param uuid  NFC卡id
     * @return {code:1/0,msg:食物取出成功/没有该食物}
     */
    @RequestMapping(value = "/outfood/",params={"macip","uuid"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> outfood(
            String macip,
            String uuid
    ) {
        Map<String,Object> map = new HashMap<>();
        //Record record = recordRepository.findByIceIdAndUuid(macip,uuid);
        List<Record> records = recordRepository.findByUuidOrderByOpDateDesc(uuid);

        if(records == null){
            map.put("code",0);
            System.out.println("1111");
            map.put("msg","没有该食物");
            return map;
        }else{
            Record record1 = records.get(0);
            System.out.println(record1.getOpDate().toString());
            System.out.println("opFlag== "+record1.getOpFlag());
            if(record1.getOpFlag() == 0){
                if(record1.getFoodWeight() > 0){
                    Record record = new Record();
                    record.setFid(record1.getFid());
                    record.setOpFlag(1L);
                    record.setIceId(macip);
                    record.setOpDate(new Date());
                    record.setUuid(uuid);
                    record.setFoodWeight(0L);
                    record.setTareWeight(0L);
                    recordRepository.save(record);
                    OfficialCard officialCard = officialCardRepository.getByUuid(uuid);
                    if(officialCard != null){
                        Long foodId = officialCard.getFoodId();
                        Food food = foodRepository.getAllByFoodId(foodId);
                        food.setFoodWeight(0L);
                        foodRepository.save(food);
                        map.put("code",1);
                        map.put("msg","食物取出成功");
                    }else{
                        UnOfficialCard  unOfficialCard= unOfficialCardRepository.getByUuid(uuid);
                        if(unOfficialCard != null){
                            unOfficialCard.setFoodWeight(0L);
                            unOfficialCardRepository.save(unOfficialCard);
                            map.put("code",1);
                            map.put("msg","食物取出成功");
                        }else{
                            System.out.println("2222");
                            map.put("code",0);
                            map.put("msg","没有该食物");
                        }
                    }
                }else{
                    System.out.println("33333");
                    map.put("code",0);
                    map.put("msg","没有该食物");
                }
            }else{
                System.out.println("44444444");
                map.put("code",0);
                map.put("msg","没有该食物");
            }
        }
        return map;
    }

}
