package main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import models.FoodItem;
import models.Fridge;
import models.ShoppingHistory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {
    Fridge fridge;
    ShoppingHistory history;
    List<FoodItem> library;

    public User() {
        this.library = loadFoodLibrary();
    }

    private List<FoodItem> loadFoodLibrary() {
        String path = "../../models/FoodLibrary.json";
        List<FoodItem> foodItems = new ArrayList<>();
        try (Reader reader = new java.io.FileReader(path)) {
            JsonReader jsonReader = new JsonReader(reader);

            Type setType = new TypeToken<List<FoodItem>>() {
            }.getType();

            Gson gson = new Gson();
            foodItems = gson.fromJson(jsonReader, setType);

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found Exception");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
        return foodItems;
    }

    public static void Main(String[] args) {
        new User();
    }
}
