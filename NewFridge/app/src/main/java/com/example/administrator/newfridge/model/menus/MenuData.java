package com.example.administrator.newfridge.model.menus;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuData {

    @SerializedName("@context")
    private String context;
    @SerializedName("@type")
    private String type;
    private String name;
    private String image;
    private MenuAuthor author;
    private String description;
    private AggregateRating aggregateRating;
    private List<String> recipeIngredient;
    private String recipeInstructions;

    public void setContext(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public void setType(String Type) {
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

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setAuthor(MenuAuthor author) {
        this.author = author;
    }

    public MenuAuthor getAuthor() {
        return author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAggregateRating(AggregateRating aggregateRating) {
        this.aggregateRating = aggregateRating;
    }

    public AggregateRating getAggregateRating() {
        return aggregateRating;
    }

    public void setRecipeIngredient(List<String> recipeIngredient) {
        this.recipeIngredient = recipeIngredient;
    }

    public List<String> getRecipeIngredient() {
        return recipeIngredient;
    }

    public void setRecipeInstructions(String recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
    }

    public String getRecipeInstructions() {
        return recipeInstructions;
    }
}
