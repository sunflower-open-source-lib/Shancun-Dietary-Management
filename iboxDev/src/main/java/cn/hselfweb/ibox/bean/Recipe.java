package cn.hselfweb.ibox.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

@Data
@Getter
@Setter
@TargetUrl(value = "https://www.xinshipu.com/zuofa/\\w+",sourceRegion = "//a[@class='shipu']")
public class Recipe {
    @ExtractBy("//script[@type='application/ld+json']")
    private String information;
}
