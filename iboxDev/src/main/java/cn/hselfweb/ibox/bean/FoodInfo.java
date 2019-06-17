package cn.hselfweb.ibox.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class FoodInfo {
    private String foodId;
    private String foodName;
    private String foodUrl;
    private String foodPhotoUrl;
    private String comment;
    private Long weight;
    private Date startTime;//存入日期
    private Long time;//存储时间
    private Long type;//存储方式
    private double percent;//保质期占比
    private Long tareWeight;
    //private Long
}
