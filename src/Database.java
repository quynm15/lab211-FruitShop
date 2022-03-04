
import java.util.ArrayList;
import java.util.Hashtable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author quynm
 */
public class Database {
    
    private ArrayList<Fruit> warehouse = new ArrayList<>();
    private Hashtable<Customer, ArrayList<Fruit>> orders = new Hashtable<>();

    public Database() {
    }

    public ArrayList<Fruit> getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(ArrayList<Fruit> warehouse) {
        this.warehouse = warehouse;
    }

    public Hashtable<Customer, ArrayList<Fruit>> getOrders() {
        return orders;
    }

    public void setOrders(Hashtable<Customer, ArrayList<Fruit>> orders) {
        this.orders = orders;
    }
    
    public void addFruit(Fruit f){
        warehouse.add(f);
    }
    
    public void addOrder(Customer c, ArrayList<Fruit> o){
        orders.put(c, o);
    }
    
    public boolean checkDuplicateFruitID(String id){
        for (Fruit fruit : warehouse) {
            if(fruit.getId().equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }
    
    public Fruit getFruitByID(String ID){
        for (Fruit fruit : warehouse) {
            if(fruit.getId().equalsIgnoreCase(ID)){
                return fruit;
            }
        }
        return null;
    }
    
}
