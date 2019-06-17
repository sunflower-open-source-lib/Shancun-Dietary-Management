package cn.hselfweb.ibox;

import cn.hselfweb.ibox.db.Food;
import cn.hselfweb.ibox.db.FoodRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoodRepositoryTests {
    @Resource
    private FoodRepository foodRepository;
    @Test
    public void saveTest(){
        Food food=new Food();
        food.setFoodId((long) 122332);
        food.setFoodName("111");
        food.setComment("hhhh");
        food.setFoodTime((long) 123);
        food.setFoodUrl("9999");
        food.setFoodWeight((long) 1123);
        food.setType((long) 11);
        food.setPercent(5.5);
        food.setFoodParent((long) 111);
        Food fooded=foodRepository.save(food);
    }
}
