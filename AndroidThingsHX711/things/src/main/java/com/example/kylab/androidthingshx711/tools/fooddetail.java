package com.example.kylab.androidthingshx711.tools;

import java.util.Date;
import java.util.List;

public class fooddetail {
    private int code;
    private String msg;
    private List<UserBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<UserBean> getData() {
        return data;
    }

    public void setData(List<UserBean> muser) {
        this.data = muser;
    }


    public class UserBean {

        private String foodId;
        private String foodName;
        private String foodUrl;
        private String foodPhotoUrl;
        private String comment;
        private String weight;
        private String startTime;
        private String time;
        private String type;
        private String percent;
        private String tareWeight;


        public String getTareWeight() {
            return tareWeight;
        }

        public void setTareWeight(String tareweight) {
            this.tareWeight = tareWeight;
        }


        public String getFoodId() {
            return foodId;
        }

        public void setFoodId(String foodId) {
            this.foodId = foodId;
        }

        public String getFoodname() {
            return foodName;
        }

        public void setFoodname(String foodname) {
            this.foodName = foodname;
        }

        public String getFoodurl() {
            return foodUrl;
        }

        public void setFoodurl(String foodurl) {
            this.foodUrl = foodurl;
        }

        public String getFoodPhotoUrl() {
            return foodPhotoUrl;
        }

        public void setFoodPhotoUrl(String foodPhotoUrl) {
            this.foodPhotoUrl = foodPhotoUrl;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }


    }

}
