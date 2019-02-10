package models;

import java.io.Serializable;
import java.util.*;

public class Fridge implements Observer, Iterable<FoodManager>, Serializable {
    private HashMap<Status, List<FoodManager>> foodMap;

    public Fridge() {
        foodMap = new HashMap<>();
        this.foodMap.put(Status.NEW, new ArrayList<>());
        this.foodMap.put(Status.SOON, new ArrayList<>());
        this.foodMap.put(Status.EXPIRED, new ArrayList<>());
    }

    //TODO: INTEGRATE OBSERVER PATTERN AND FRONT-END
    public void addItem(FoodManager newItem){
        List<FoodManager> foodList = foodMap.get(newItem.getStatus());
        if (!foodList.contains(newItem)){
            foodList.add(newItem);
        }
    }

    public List<FoodManager> getFoodList (Status status){
        return foodMap.get(status);
    }

    //TODO: INTEGRATE OBSERVER PATTERN AND FRONT-END
    public void removeItem(FoodManager itemToRemove){
        if (foodMap.get(itemToRemove.getStatus()).contains(itemToRemove)){
            foodMap.get(itemToRemove.getStatus()).remove(itemToRemove);
        }
    }

    public void updateItems(){
        for (FoodManager f : this){
            Status oldstat = f.getStatus();
            f.updateStatus();
            if (f.getCurrentQuantity() == 0 || !f.isFresh() || f.getStatus() == Status.EATEN){
                removeItem(f);
            }
        }
        foodMap.get(Status.EXPIRED).clear();
    }

    public void printFridge(Status status){
        List<FoodManager> foodList = foodMap.get(status);
        for (int i=0; i<foodList.size(); i++){
            System.out.println((i+1) + " " + foodList.get(i).getFood().getName());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        FoodManager foodItem = (FoodManager) o;
        Status oldstat = ((FoodManager) o).getStatus();
        removeItem(foodItem);
        if (foodItem.getStatus() != Status.EATEN){
            addItem(foodItem);
        }
    }

    @Override
    public Iterator iterator() {
        return new FoodIterator();
    }

    private class FoodIterator implements Iterator<FoodManager> {
        Iterator newIterator = foodMap.get(Status.NEW).iterator();
        Iterator soonIterator = foodMap.get(Status.SOON).iterator();

        @Override
        public boolean hasNext() {
            return newIterator.hasNext() || soonIterator.hasNext();
        }

        @Override
        public FoodManager next() {
            FoodManager food = null;
            if (newIterator.hasNext()){
                food = (FoodManager) newIterator.next();
            }
            else if(soonIterator.hasNext()){
                food = (FoodManager) soonIterator.next();
            }
            return food;
        }
    }
}