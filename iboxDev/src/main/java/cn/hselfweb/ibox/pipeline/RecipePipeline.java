package cn.hselfweb.ibox.pipeline;

import cn.hselfweb.ibox.bean.Recipe;
import lombok.Getter;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class RecipePipeline implements PageModelPipeline{

    //public static List<Recipe> informations = new ArrayList<Recipe>();
    public  List<Recipe> informations = new ArrayList<Recipe>();

    public void process(Object o, Task task) {
        Recipe recipe = (Recipe)o;
        informations.add(recipe);
        System.out.println("hello");
    }
}
