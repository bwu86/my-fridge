package main;

import abstractions.Loadable;
import abstractions.Saveable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import models.FoodItem;
import models.Fridge;
import models.ShoppingHistory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class User implements Serializable, Saveable, Loadable {
    Fridge fridge;
    ShoppingHistory history;
    List<FoodItem> library;
    private static final String COMMAND_VIEW_FRIDGE = "fridge";
    private static final String COMMAND_BUY = "buy";
    private static final String COMMAND_EAT = "eat";
    private static final String COMMAND_THROW_OUT = "throw out";
    private static final String COMMAND_QUIT = "quit";
    private static final String COMMAND_SAVE_AND_QUIT = "save";
    private Scanner scanner = new Scanner(System.in);


//    public User() {
////        this.library = loadFoodLibrary();
////    }

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


//    private String getUserInput() {
//        Scanner scanner = new Scanner(System.in);
//        return scanner.next();
//    }
//
//    private void print(String string) {
//        if (string != null) {
//            System.out.println(string);
//        }
//    }

    public static void Main(String[] args) {
        new User();
    }

    public User() {

        // Modeled after B04-LoggingCalculator starter file
        System.out.println("Welcome to my fridge!");
        load();
        String operation;

        while (true) {
            printMenu();
            operation = scanner.nextLine();
            System.out.println("You selected " + operation + ".");

            if (operation.equals(COMMAND_VIEW_FRIDGE)) {
                enterFridgeMenu();
            } else if (operation.equals(COMMAND_BUY)) {
                buyGroceries();
            } else if (operation.equals(COMMAND_QUIT)) {
                System.out.println("Thanks for playing! See you next time.");
                break;
            } else if (operation.equals(COMMAND_SAVE_AND_QUIT)) {
                System.out.println("Saving data...");
                save();
                System.out.println("Your data has been saved! See you next time.");
                break;
            } else {
                System.out.println("Sorry, command not recognized!");
            }
        }
    }

    public static void main(String[] args) {
        new User();
    }

    private void printMenu() {
        System.out.println("[" + COMMAND_VIEW_FRIDGE + "] - See whatCinda fridge!");
        System.out.println("[" + COMMAND_BUY + "] - Buy groceries");
        System.out.println("[" + COMMAND_QUIT + "] - Quit");
        System.out.println("[" + COMMAND_SAVE_AND_QUIT + "] - Save & Quit");
    }

    private void printFridgeMenu() {
        System.out.println("[" + COMMAND_EAT + "] - Eat some food");
    }

    private void enterFridgeMenu() {
        printFridgeMenu();
    }

    @Override
    //Modeled after Object Stream tutorial, 2018-10-01 [https://www.mkyong.com/java/how-to-read-and-write-java-object-to-a-file/]
    public void save() {
        try {
            FileOutputStream fFridge = new FileOutputStream(new File("fridge.txt"));
            ObjectOutputStream oFridge = new ObjectOutputStream(fFridge);

            // Write objects to file
            oFridge.writeObject(this.fridge);

            oFridge.close();
            fFridge.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found. Unable to save.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing stream");
        }

        try {
            FileOutputStream fHistory = new FileOutputStream(new File("shopping-history.txt"));
            ObjectOutputStream oHistory = new ObjectOutputStream(fHistory);

            // Write objects to file
            oHistory.writeObject(this.history);

            oHistory.close();
            fHistory.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing stream");
        }

    }

    @Override
    //Modeled after Object Stream tutorial, 2018-10-01 [https://www.mkyong.com/java/how-to-read-and-write-java-object-to-a-file/]
    // TODO: add specification and tests for this method
    public void load() {
        try {
            FileInputStream fFridge = new FileInputStream(new File("fridge.txt"));
            ObjectInputStream oFridge = new ObjectInputStream(fFridge);

            Fridge restoredFridge = (Fridge) oFridge.readObject();

            FileInputStream fHistory = new FileInputStream(new File("shopping-history.txt"));
            ObjectInputStream oHistory = new ObjectInputStream(fHistory);

            ShoppingHistory restoredHistory = (ShoppingHistory) oHistory.readObject();

            oFridge.close();
            fFridge.close();

            oHistory.close();
            fHistory.close();

            this.fridge = restoredFridge;
            this.history = restoredHistory;

        } catch (FileNotFoundException e) {
            System.out.println("No saved state found. Creating a new fridge...");

            this.fridge = new Fridge();
            this.history = new ShoppingHistory();

        } catch (IOException e) {
            System.out.println("No saved state found." + "\n" + "Creating new league!");

            this.fridge = new Fridge();
            this.history = new ShoppingHistory();

        } catch (ClassNotFoundException e) {
            System.out.println("Class not found.");

            this.fridge = new Fridge();
            this.history = new ShoppingHistory();
        }
    }
}
