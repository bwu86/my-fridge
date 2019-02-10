package main;

import abstractions.Loadable;
import abstractions.Saveable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class User implements Serializable, Saveable, Loadable {
    Fridge fridge;
    ShoppingHistory history;
    List<FoodItem> library;
    private static final String COMMAND_VIEW_FRIDGE = "fridge";
    private static final String COMMAND_BUY = "buy";
    private static final String COMMAND_EAT = "eat";
    private static final String COMMAND_BACK = "back";
    private static final String COMMAND_QUIT = "quit";
    private static final String COMMAND_SAVE_AND_QUIT = "save";
    private static final String COMMAND_FRESH = "fresh";
    private static final String COMMAND_SOON = "soon";
    private Scanner scanner = new Scanner(System.in);


    public User() {
        // Modeled after B04-LoggingCalculator starter file
        System.out.println("Welcome to my fridge!");
        load();
        this.library = loadFoodLibrary();
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

    private void printMenu() {
        System.out.println("[" + COMMAND_VIEW_FRIDGE + "] - See whatCinda fridge!");
        System.out.println("[" + COMMAND_BUY + "] - Buy groceries");
        System.out.println("[" + COMMAND_QUIT + "] - Quit");
        System.out.println("[" + COMMAND_SAVE_AND_QUIT + "] - Save & Quit");
    }

    private void buyGroceries() {
        String operation;

        while (true) {
            try {
                listGroceries();
                System.out.println("Enter the index of the grocery you want to buy");
                int index = (scanner.nextInt());
                System.out.println("How many would you like to buy?");
                int qty = scanner.nextInt();
                System.out.println("How much does it cost?");
                BigDecimal price = scanner.nextBigDecimal();
                if (index <= library.size()) {
                    FoodManager newItem = new FoodManager(library.get(index - 1), qty, price, fridge);
                    fridge.addItem(newItem);
                }
            } catch (InputMismatchException e) {
                break;
            }
        }
    }

    private void listGroceries() {
        for (int i = 0; i < library.size(); i++) {
            System.out.println("[" + (i + 1) + "] - " + library.get(i).getName());
        }
    }

    private void printFridgeMenu() {
        System.out.println("[" + COMMAND_EAT + "] - Eat some food");
        System.out.println("[" + COMMAND_BACK + "] - Return to previous menu");
    }

    private void enterFridgeMenu() {
        String operation;

        while (true) {
            printFridgeMenu();
            operation = scanner.nextLine();
            System.out.println("You selected " + operation + ".");

            if (operation.equals(COMMAND_BACK)) {
                break;
            } else if (operation.equals(COMMAND_EAT)) {
                System.out.println("Do you want to eat [fresh] food or [soon] to be expired food?");
                operation = scanner.nextLine();

                if (operation.equals(COMMAND_FRESH)) {
                    fridge.printFridge(Status.NEW);
                    eatFood(Status.NEW);
                } else if (operation.equals(COMMAND_SOON)) {
                    fridge.printFridge(Status.SOON);
                    eatFood(Status.SOON);
                }
                break;
            } else {
                System.out.println("Sorry, that command2 wasn't recognized.");
            }
        }

    }

    private void eatFood(Status status) {
        while (true) {
            System.out.println("Select an item you want to eat.");
            try {
                Scanner s = new Scanner(System.in);
                int index = s.nextInt();
                if (index > 0 && index <= fridge.getFoodList(status).size()) {
                    FoodManager foodItem = fridge.getFoodList(status).get(index - 1);
                    System.out.println("How many " + foodItem.getFood().getName() + " would you like to eat? You have [" + foodItem.getCurrentQuantity() + "] left.");
                    int qty = s.nextInt();
                    if (qty > 0 && qty <= foodItem.getCurrentQuantity()) {
                        foodItem.eatQuantity(qty);
                        break;
                    }
                } else {
                    System.out.println("You don't have enough food to eat.");
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Sorry try another input!");
                scanner.reset();
            }
        }
    }

    private List<FoodItem> loadFoodLibrary() {
        String prefix = "C:\\Users\\Lorenzo Bisceglia\\Google Drive\\1 - School\\1 - BCS\\Personal Projects\\my-fridge\\src\\";
        String path = prefix + "FoodLibrary.json";

        String json = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            json = buffer.toString();

            JsonParser jp = new JsonParser();
            JsonArray ja = (JsonArray) jp.parse(json);

            List<FoodItem> foodList = new ArrayList<>();

            for (JsonElement o : ja) {
                JsonObject food = o.getAsJsonObject();
                String SKU = food.get("SKU").getAsString();
                String name = food.get("name").getAsString();
                int daysFresh = food.get("daysFresh").getAsInt();
                FoodItem newFood = new FoodItem(SKU, name, daysFresh);
                foodList.add(newFood);
            }

            return foodList;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }


//
//        List<FoodItem> foodItems = new ArrayList<>();
//        try (Reader reader = new java.io.FileReader(path)) {
//            JsonReader jsonReader = new JsonReader(reader);
//
//            Type setType = new TypeToken<List<FoodItem>>() {}.getType();
//
//            Gson gson = new Gson();
//            foodItems = gson.fromJson(jsonReader, setType);
//            return foodItems;
//
//        } catch (FileNotFoundException e) {
//            System.out.println("Food Library not found.");
//        } catch (IOException e) {
//            System.out.println("IO Exception");
//        }
//        return foodItems;
//    }

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
