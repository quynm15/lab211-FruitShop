
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author quynm J1.L.P0023
 */
public class Main {

    public static Scanner scanner = new Scanner(System.in);

    //allow user to input an integer in range [min..max]
    public static int inputInt(String mess, int min, int max) {
        System.out.print(mess);
        int number = 0;
        do {
            try {
                number = Integer.parseInt(scanner.nextLine());
                if (number >= min && number <= max) {
                    return number;
                } else {
                    System.err.print("Out of range! Please enter in range " + min + " to " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.err.print("Invalid! Please enter an integer number: ");
            }
        } while (true);
    }

    //allow user to input a double in range [min..max]
    public static double inputDouble(String mess, double min, double max) {
        System.out.print(mess);
        double number;
        do {
            try {
                number = Double.parseDouble(scanner.nextLine());
                if (number >= min && number <= max) {
                    return number;
                } else {
                    System.err.print("Out of range! Please enter in range " + min + " to " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.err.print("Invalid! Please enter a double number: ");
            }
        } while (true);
    }

    //allow user to input a not null String
    public static String inputString(String mess) {
        String string;
        do {
            System.out.print(mess);
            string = scanner.nextLine();
            if (!string.trim().equals("")) {
                return string;
            } else {
                System.err.println("This is required field. Please enter a string which is not null!");
            }
        } while (true);
    }

    //allow user to input Y or N
    public static boolean inputYN(String mess) {
        String option;
        do {
            System.out.print(mess);
            option = scanner.nextLine().trim();
            //end selecting process and order.
            if (option.equalsIgnoreCase("y")) {
                return true;
            } //continue to select
            else if (option.equalsIgnoreCase("n")) {
                return false;
            } else {
                System.err.println("Please enter Y for yes or N for no!");
            }
        } while (true);
    }

    public static Database db = new Database();

    //allow owner to create fruit
    public static void createFruit() {
        do {
            String id = inputString("Enter ID: ");
            if (db.checkDuplicateFruitID(id)) {
                System.err.println("Duplicate ID, please re-enter!");
                continue;
            }
            String name = inputString("Fruit name: ");
            int quantity = inputInt("Enter the total quantity: ", 0, Integer.MAX_VALUE);
            double price = inputDouble("Enter price: ", 0, Double.MAX_VALUE);
            String origin = inputString("Origin: ");

            db.getWarehouse().add(new Fruit(id, name, price, quantity, origin));
            System.out.println("Added successfully!");

            //check continue
            if (!inputYN("Do you want to continue? (y/n): ")) {
                return;
            }
        } while (true);
    }

    //to display all product in order
    public static void displayOrderList(ArrayList<Fruit> order) {
        double total = 0;
        System.out.printf("%15s%15s%15s%15s\n", "Product", "Quantity", "Price", "Amount");
        for (Fruit fruit : order) {
            System.out.printf("%15s%15d%15.0f%15.0f\n", fruit.getName(), fruit.getQuantity(),
                    fruit.getPrice(), fruit.getPrice() * fruit.getQuantity());
            total += fruit.getPrice() * fruit.getQuantity();
        }
        System.out.println("Total: " + total);
    }

    //allow owner to view all orders
    public static void viewOrder() {
        if (db.getOrders().isEmpty()) {
            System.out.println("There is no orders!");
            return;
        }
        for (Customer customer : db.getOrders().keySet()) {
            System.out.println("Customer: " + customer.getName());
            displayOrderList(db.getOrders().get(customer));
        }
    }

    //to display all list of fruit which buyer can choose
    public static void displayAvailableFruit(ArrayList<Fruit> availables) {
        int item = 1;
        System.out.printf("%-10s%-20s%-20s%-15s\n", "Item", "Fruit name", "Origin", "Price");
        for (Fruit fruit : availables) {
            System.out.printf("%-10d%-20s%-20s%-15.0f\n", item++, fruit.getName(), fruit.getOrigin(), fruit.getPrice());
        }
    }

    //to check exist item
    public static boolean checkExistItem(ArrayList<Fruit> order, String id) {
        for (Fruit selectedFruit : order) {
            if (selectedFruit.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    //allow buyer to shopping
    public static void shopping() {
        //if there is no product in ware house
        if (db.getWarehouse().isEmpty()) {
            System.out.println("There is nothing to buy! Thanks for choosing us. See you later!");
            return;
        }
        //else
        ArrayList<Fruit> order = new ArrayList<>();
        ArrayList<Fruit> availables = new ArrayList<>();
        do {
            //get list of available fruit to buy
            availables.clear();
            for (Fruit fruit : db.getWarehouse()) {
                if (fruit.getQuantity() > 0) {
                    availables.add(fruit);
                }
            }
            //if all the product have quantity = 0
            if (availables.isEmpty()) {
                System.out.println("All products were sold out!");
                //if there are some products in cart
                if (!order.isEmpty()) {
                    displayOrderList(order);
                    Customer customer = new Customer(inputString("Enter your name: "));
                    db.getOrders().put(customer, order);
                }
                return;
            }
            //else
            displayAvailableFruit(availables);
            int choosingItem = inputInt("Choose an item: ", 1, availables.size());
            Fruit choosingFruit = availables.get(choosingItem - 1);
            System.out.println("You selected: " + choosingFruit.getName());
            int choosingQuantity = inputInt("Please enter quantity: ", 1, choosingFruit.getQuantity());
            //if the item is exist in order -> ask user to update quantity or not
            if (checkExistItem(order, choosingFruit.getId())) {
                //if user enter Y -> update the quantity
                if (inputYN("This item has been selected. Enter Y to update quantity, N if you dont want to update: ")) {
                    //traverse all order
                    for (Fruit selectedFruit : order) {
                        //find the exist item
                        if (selectedFruit.getId().equalsIgnoreCase(choosingFruit.getId())) {
                            //update diff quantity in warehouse: 
                            //newQuan = oldQuan(in ware house) - (choosingQuan (input) - orderQuan (last order))
                            int diffQuan = choosingQuantity - selectedFruit.getQuantity(); //chenh lech so luong
                            db.getFruitByID(choosingFruit.getId()).setQuantity(choosingFruit.getQuantity() - diffQuan);//set newQuan
                            //update quantity in order
                            selectedFruit.setQuantity(choosingQuantity);
                        }
                    }
                }//if user enter N -> do nothing
                else {
                    //do nothing
                }
            } else {
                //add to the list
                order.add(new Fruit(choosingFruit.getId(), choosingFruit.getName(), choosingFruit.getPrice(), choosingQuantity));
                //update quantity in warehouse: newQuan = oldQuan (in warehouse) - choosingQuan
                db.getFruitByID(choosingFruit.getId()).setQuantity(choosingFruit.getQuantity() - choosingQuantity);
            }

            //check continue
            if (inputYN("Do you want to order now? (y/n): ")) {
                displayOrderList(order);
                Customer customer = new Customer(inputString("Enter your name: "));
                db.getOrders().put(customer, order);
                return;
            }
        } while (true);
    }

    public static void Menu() {
        System.out.println("FRUIT SHOP SYSTEM");
        System.out.println("1. Create Fruit");
        System.out.println("2. View orders");
        System.out.println("3. Shopping (for buyer)");
        System.out.println("4. Exit");
        System.out.println("(Please choose 1 to Create a fruit, 2 to view order, 3 to shopping and 4 to Exit program)");
    }

    public static void main(String[] args) {
        do {
            Menu();
            int selection = inputInt("Your selection: ", 1, 4);
            switch (selection) {
                case 1:
                    createFruit();
                    break;
                case 2:
                    viewOrder();
                    break;
                case 3:
                    shopping();
                    break;
                case 4:
                    return;
            }
        } while (true);
    }

}
