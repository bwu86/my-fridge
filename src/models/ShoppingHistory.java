package models;

import java.util.*;

public class ShoppingHistory implements Observer{

    private HashMap<Status, List<FoodManager>> historyMap = new HashMap<>();

    public ShoppingHistory() {
        this.historyMap.put(Status.NEW, new ArrayList<>());
        this.historyMap.put(Status.SOON, new ArrayList<>());
        this.historyMap.put(Status.EXPIRED, new ArrayList<>());
        this.historyMap.put(Status.EATEN, new ArrayList<>());
        this.historyMap.put(Status.THROWNOUT, new ArrayList<>());
    }

    //TODO: INTEGRATE OBSERVER PATTERN AND FRONT-END
    private void addItem(FoodManager newItem){
        List<FoodManager> foodList = historyMap.get(newItem.getStatus());
        foodList.add(newItem);
    }

    //TODO: integrate with observer pattern & front-end
    private void removeItem(FoodManager foodItem) {
        List<FoodManager> foodList = historyMap.get(foodItem.getStatus());
        if (foodList.contains(foodItem)) {
            foodList.remove(foodItem);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        FoodManager foodItem = (FoodManager) o;
        Status status = (Status) arg;
        changeFoodStatus(foodItem, status);
    }

    private void changeFoodStatus(FoodManager foodItem, Status status){
        Status currStatus = foodItem.status;
        historyMap.remove(currStatus, foodItem);
        List<FoodManager> tempList =  historyMap.get(status);
        tempList.add(foodItem);
    }
}
