package market;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Customer {

    private ProductType desiredType;
    private int desiredAmount;
    private Map<String, Integer> basket;
    private int waiting;
    public Runnable onArrival;

    public Customer(Random random){
        ProductType[] types = ProductType.values();
        this.desiredType = types[random.nextInt(types.length)];
        this.desiredAmount = 1 + random.nextInt(6);
        this.waiting = 1 + random.nextInt(3);
        this.basket = new HashMap<>();
    }

    public ProductType getDesiredType(){
        return desiredType;
    }

    public int getDesiredAmount(){
        return desiredAmount;
    }

    public Map<String, Integer> getBasket(){
        return basket;
    }

    public int getWaiting(){
        return waiting;
    }
    // добавление товара в корзину
    public void addToBasket(String name, int amount){
        int amountNow = this.basket.getOrDefault(name, 0);
        this.basket.put(name, amount + amountNow);
    }
    // количество товаров в корзине
    public int getBasketProductCount(){
        int count = 0;
        for(int amount : basket.values()){
            count += amount;
        }
        return count;
    }

}
