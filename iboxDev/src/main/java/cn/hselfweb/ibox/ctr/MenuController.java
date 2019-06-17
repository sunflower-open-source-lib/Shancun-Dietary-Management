package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.FoodInfo;
import cn.hselfweb.ibox.bean.Recipe;
import cn.hselfweb.ibox.db.*;
import cn.hselfweb.ibox.pipeline.RecipePipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.example.GithubRepo;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class MenuController {

    private final FoodRepository foodRepository;

    private final RecordRepository recordRepository;

    private final OfficialCardRepository officialCardRepository;

    private final UnOfficialCardRepository unOfficialCardRepository;

    private final IceBoxRepository iceBoxRepository;



    @Autowired
    public MenuController(FoodRepository foodRepository, RecordRepository recordRepository, OfficialCardRepository officialCardRepository, UnOfficialCardRepository unOfficialCardRepository, IceBoxRepository iceBoxRepository) {
        this.foodRepository = foodRepository;
        this.officialCardRepository = officialCardRepository;
        this.recordRepository = recordRepository;
        this.unOfficialCardRepository = unOfficialCardRepository;
        this.iceBoxRepository = iceBoxRepository;
    }

    /**
     *
     * @param macip 冰箱id
     * @param request session信息
     * @return {code:0/1,msg:冰箱里没有食材/获取食材成功,data:-/食谱数据}
     */
    @RequestMapping(value = "/menus/", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> select(
            String macip,
            HttpServletRequest request
    ) {
        //HttpSession session = request.getSession();
        //Long uid = (Long)session.getAttribute("user");
        Map<String,Object> map = new HashMap<>();
        FoodController foodCon = new FoodController(foodRepository,recordRepository,officialCardRepository,unOfficialCardRepository,iceBoxRepository);
        Map<String,Object> foodMap = foodCon.getAllFoodlist(macip);
        List<FoodInfo> foodInfoList = (List<FoodInfo>)foodMap.get("data");
        System.out.println(foodMap.get("data").toString());
        ArrayList<String> foods = new ArrayList<>();
        if((int)foodMap.get("code") == 0){
            map.put("code",0);
            map.put("msg","冰箱里没有食材");
        }else{
            for(int i = 0; i < foodInfoList.size(); i++){
                FoodInfo foodInfo = foodInfoList.get(i);
                String food = foodInfo.getComment();
                System.out.println("食物： "+ food);
                foods.add(food);
            }

            String url0 = "https://www.xinshipu.com/doSearch.html?q=";
            String allFoods = "";
            for(int i = 0; i < foods.size(); i++){
                allFoods = allFoods + foods.get(i) + "%20";
                System.out.println(foods.get(i));
            }
            String url = url0 + allFoods;
            //String url = url0 + "白菜";
            System.out.println("url: "+url);
            RecipePipeline results = new RecipePipeline();
            /*OOSpider.create(Site.me()
                    , results, Recipe.class)
                    .addUrl(url).thread(5).run();*/
            OOSpider.create(Site.me().setSleepTime(1000)
                    , results, Recipe.class).
                    addUrl(url).thread(5).run();
            List<Recipe> infos = results.informations;
            List<String> infoData = new ArrayList<>();
            for(int i = 0; i < infos.size(); i++){
                String info = infos.get(i).getInformation();
                String[] temp = info.split(">\n");
                String[] temp1 = temp[1].split("<");
                infoData.add(temp1[0]);
                System.out.println(temp1[0]);
            }
            map.put("code",1);
            map.put("msg","获取食谱成功");
            map.put("data",infoData);
        }
        return map;
    }
}
