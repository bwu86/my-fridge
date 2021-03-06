package models;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;
import java.util.Observable;

public class FoodManager extends Observable {
    private FoodItem food;
    private final int originalQuantity;
    private int currentQuantity;
    private Calendar purchaseDate, expiryDate, soonDate;
    private int daysUntilSoon;
    private BigDecimal price;
    private Status status;

    public FoodManager(FoodItem food, int originalQuantity, BigDecimal price, Fridge fridge){
        this.food = food;
        this.originalQuantity = originalQuantity;
        this.currentQuantity = originalQuantity;
        this.price = price;

        //TODO: CHANGE THIS TO SOMETHING BETTER
        daysUntilSoon = food.getDaysFresh()/3;

        Calendar today = Calendar.getInstance();
        purchaseDate = today;
        expiryDate = Calendar.getInstance();
        expiryDate.add(Calendar.DATE, food.getDaysFresh());
        soonDate = Calendar.getInstance();
        soonDate.add(Calendar.DATE, food.getDaysFresh()-daysUntilSoon);
        status = updateStatus();
        addObserver(fridge);
    }

    public boolean isFresh(){
        Calendar curDate = Calendar.getInstance();
        return (expiryDate.before(curDate) || (expiryDate.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)
                && expiryDate.get(Calendar.YEAR) == curDate.get(Calendar.YEAR)));
    }

    public Status updateStatus(){
        Calendar curDate = Calendar.getInstance();
        Status stat;
        if (isFresh()){
            if (curDate.before(soonDate)){
                stat = Status.NEW;
            }
            else{
                stat = Status.SOON;
            }
        }
        else {
            stat = Status.EXPIRED;
        }
        setChanged();
        notifyObservers(this);
        return stat;
    }

    public Status getStatus(){
        return status;
    }

    public FoodItem getFood() {
        return food;
    }

    public int getOriginalQuantity() {
        return originalQuantity;
    }

    public int getCurrentQuantity(){
        return currentQuantity;
    }

    public int getTotalEaten(){
        return originalQuantity - currentQuantity;
    }

    public void eatQuantity(int quantity) {
        if (quantity <= this.currentQuantity){
            this.currentQuantity -= quantity;
            if (this.currentQuantity == 0){
                this.status = Status.EATEN;
                updateStatus();
            }
        }
    }

    public Calendar getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Calendar expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getDaysUntilSoon() {
        return daysUntilSoon;
    }

    public void setDaysUntilSoon(int daysUntilSoon) {
        this.daysUntilSoon = daysUntilSoon;
    }

    public Calendar getSoonDate() {
        return soonDate;
    }

    public void setSoonDate(Calendar soonDate) {
        this.soonDate = soonDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodManager that = (FoodManager) o;
        return Objects.equals(food, that.food) &&
                Objects.equals(originalQuantity, that.originalQuantity) &&
                Objects.equals(purchaseDate, that.purchaseDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(food, originalQuantity, purchaseDate);
    }

}
