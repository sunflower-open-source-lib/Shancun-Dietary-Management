package com.example.kylab.androidthingshx711.tools;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class MenuDetail {
    private int code;
    private String msg;
    private List<String> data;



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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> muser) {
        this.data = muser;
    }


    public class menuBean {
        @SerializedName("@context")
        private String context;
        @SerializedName("@type")
        private String type;
        private String name;
        private String image;
        private Author author;
        private String description;
        private AggregateRating aggregateRating;
        private List<String> recipeIngredient;
        private String recipeInstructions;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public AggregateRating getAggregateRating() {
            return aggregateRating;
        }

        public void setAggregateRating(AggregateRating aggregateRating) {
            this.aggregateRating = aggregateRating;
        }

        public List<String> getRecipeIngredient() {
            return recipeIngredient;
        }

        public void setRecipeIngredient(List<String> recipeIngredient) {
            this.recipeIngredient = recipeIngredient;
        }

        public String getRecipeInstructions() {
            return recipeInstructions;
        }

        public void setRecipeInstructions(String recipeInstructions) {
            this.recipeInstructions = recipeInstructions;
        }
        public class Author{
            @SerializedName("@type")
            private String type;
            private String name;
            public void setType(String type) {
                this.type = type;
            }
            public String getType() {
                return type;
            }

            public void setName(String name) {
                this.name = name;
            }
            public String getName() {
                return name;
            }
        }
        public class AggregateRating{
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
    }

}
