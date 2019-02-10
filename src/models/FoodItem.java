package models;

import java.io.Serializable;

public class FoodItem implements Serializable {
    private String SKU;
    private String name;
    private int daysFresh;

    public FoodItem(String sku, String name){
        this.name = name;
        this.SKU=sku;
    }

    public int getDaysFresh() {
        return daysFresh;
    }

    public void setDaysFresh(int daysFresh) {
        this.daysFresh = daysFresh;
    }

    public String getSKU() {
        return SKU;

    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
