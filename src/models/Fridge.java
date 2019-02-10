package models;

import java.util.*;

public class Fridge implements Observer {
    private HashMap<Status, List<FoodManager>> foodMap = new HashMap<>();

    public Fridge() {
        this.foodMap.put(Status.NEW, new ArrayList<>());
        this.foodMap.put(Status.SOON, new ArrayList<>());
        this.foodMap.put(Status.EXPIRED, new ArrayList<>());
    }

    //TODO: INTEGRATE OBSERVER PATTERN AND FRONT-END
    private void addItem(FoodManager newItem){
        List<FoodManager> foodList = foodMap.get(newItem.getStatus());
        foodList.add(newItem);
    }

    //TODO: INTEGRATE OBSERVER PATTERN AND FRONT-END
    private void removeItem(FoodManager itemToRemove){
        List<FoodManager> foodList = foodMap.get(itemToRemove.getStatus());
        if (foodList.contains(itemToRemove)){
            foodList.remove(itemToRemove);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        FoodManager foodItem = (FoodManager) o;
        removeItem(foodItem);
        addItem(foodItem);
    }
}
