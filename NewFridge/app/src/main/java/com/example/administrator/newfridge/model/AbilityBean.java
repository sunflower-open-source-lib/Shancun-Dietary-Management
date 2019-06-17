package com.example.administrator.newfridge.model;

/**
 * Created by Fu.
 * QQ:908323236
 * 2017/10/25 15:37
 * 健康推荐
 */

public class AbilityBean {

    //有哪个些能力
    public static final String[] abilitys = {"肉类", "蔬菜类", "蛋类", "海鲜类", "水果类"};

    //每个能力的值，范围0~100，单位%
    private int meat;
    private int vegetable;
    private int egg;
    private int fish;
    private int fruit;

    public AbilityBean(int meat, int vegetable, int egg, int fish, int fruit) {
        this.meat = meat;
        this.vegetable = vegetable;
        this.egg = egg;
        this.fish = fish;
        this.fruit = fruit;
    }

    public static String[] getAbilitys() {
        return abilitys;
    }

    public int getMeat() {
        return meat;
    }

    public void setMeat(int meat) {
        this.meat = meat;
    }

    public int getVegetable() {
        return vegetable;
    }

    public void setVegetable(int vegetable) {
        this.vegetable = vegetable;
    }

    public int getEgg() {
        return egg;
    }

    public void setEgg(int egg) {
        this.egg = egg;
    }

    public int getFish() {
        return fish;
    }

    public void setFish(int fish) {
        this.fish = fish;
    }

    public int getFruit() {
        return fruit;
    }

    public void setFruit(int fruit) {
        this.fruit = fruit;
    }

    public int[] getAllAbility() {
        int[] allAbility = {meat,vegetable,egg,fish,fruit};
        return allAbility;
    }
}
