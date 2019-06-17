package com.example.administrator.newfridge.model.menus;

import com.google.gson.annotations.SerializedName;

public class AggregateRating {
    @SerializedName("@type")
    private String type;
    private String ratingValue;
    private String reviewCount;
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }
    public String getRatingValue() {
        return ratingValue;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }
    public String getReviewCount() {
        return reviewCount;
    }

}

