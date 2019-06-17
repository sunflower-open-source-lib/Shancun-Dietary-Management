package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
public class TestController {
    private final RecordRepository recordRepository;

    private final OfficialCardRepository officialCardRepository;

    private final UnOfficialCardRepository unOfficialCardRepository;

    private final FoodRepository foodRepository;

    @Autowired
    public TestController(RecordRepository recordRepository,OfficialCardRepository officialCardRepository,UnOfficialCardRepository unOfficialCardRepository,FoodRepository foodRepository) {
        this.recordRepository = recordRepository;
        this.officialCardRepository = officialCardRepository;
        this.unOfficialCardRepository = unOfficialCardRepository;
        this.foodRepository = foodRepository;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {

        Food food = new Food();
        food.setFoodId(13L);
        food.setFoodTime(32L);
        //food.setFoodUrl(foodPhoto);
        food.setFoodWeight(100L);
        food.setType(1L);
        food.setPercent(0.3);
        food.setFoodParent(234L);
        food.setComment("hello");
        foodRepository.save(food);

        Record record = new Record();

        record.setUuid("sdfsd");
        record.setIceId("sdfdf");
        record.setFid(234L);
        record.setOpFlag(0L);
        record.setOpDate(new Date());
        record.setTareWeight(12L);
        record.setFoodWeight(123L);
        record.setFoodPhoto("helloworld");
        recordRepository.save(record);
        return "success";
    }

}
