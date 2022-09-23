package api.model;

import api.model.Ingredient;

import java.util.List;

public class IngredientsList {

    public IngredientsList(){
    }

    private String success;
    private List<Ingredient> data;


    public String getStatus() {
        return success;
    }

    public void setStatus(String success) {
        this.success = success;
    }

    public List<Ingredient> getIngredients() {
        return data;
    }

    public void setIngredients(List<Ingredient> data) {
        this.data = data;
    }
}
