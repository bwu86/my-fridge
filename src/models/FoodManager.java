package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;
import java.util.Observable;

public class FoodManager extends Observable implements Serializable {
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
        setCalTime(today);
        purchaseDate = today;
        expiryDate = Calendar.getInstance();
        setCalTime(expiryDate);
        expiryDate.add(Calendar.DATE, food.getDaysFresh());
        soonDate = Calendar.getInstance();
        setCalTime(soonDate);
        soonDate.add(Calendar.DATE, food.getDaysFresh()-daysUntilSoon);
        this.status = updateStatus();
        addObserver(fridge);
    }

    private void setCalTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public boolean isFresh(){
        Calendar curDate = Calendar.getInstance();
        setCalTime(curDate);
//        return (curDate.compareTo(expiryDate) <= 0);
        return (curDate.before(expiryDate) || (expiryDate.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)
                && expiryDate.get(Calendar.YEAR) == curDate.get(Calendar.YEAR)));
    }

    public Status updateStatus(){
        Calendar curDate = Calendar.getInstance();
        setCalTime(curDate);
        Status stat;

        if (this.getCurrentQuantity() == 0){
            stat = Status.EATEN;
        }

        else if (isFresh()){
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
        this.status = stat;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodManager that = (FoodManager) o;
        return originalQuantity == that.originalQuantity &&
                Objects.equals(food, that.food) &&
                Objects.equals(price, that.price) &&
                status == that.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(food, originalQuantity, purchaseDate, price, status);
    }

    public int getTotalEaten(){
        return originalQuantity - currentQuantity;

    }

    public void eatQuantity(int quantity) {
        if (quantity <= this.currentQuantity){
            this.currentQuantity -= quantity;
            updateStatus();
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

}