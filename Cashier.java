package market;

import java.util.ArrayList;
import java.util.Map;

public class Cashier {
    private ArrayList<Customer> queue;
    private double income;
    private int servicedCount;

    public Cashier(){
        this.income = 0.0;
        this.queue = new ArrayList<>();
        this.servicedCount = 0;
    }

    public ArrayList<Customer> getQueue(){
        return queue;
    }

    public double getIncome(){
        return income;
    }

    public int getServicedCount(){
        return servicedCount;
    }

    public int getQueueLen(){
        return queue.size();
    }
    // выполнение шага обслуживание покупателя в очереди
    public void doStep(Department department){
        if(queue.isEmpty()){
            return;
        }
        Customer customer = queue.removeFirst();
        double money = 0.0;
        for(Map.Entry<String, Integer> entry : customer.getBasket().entrySet()){
            String name = entry.getKey();
            int amount = entry.getValue();

            Product product = department.getProduct(name);
            if(product != null){
                money += product.getPrice() * amount;
            }

        }
        this.income += money;
        this.servicedCount++;

        System.out.println("Покупатель обслужен на сумму " + income + ".");
    }
}
