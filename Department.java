package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Department {

    private Map<String, Product> products;
    private ProductType type;
    private List<Cashier> cashiers;
    private int notWaiting = 0;

    public Department(ProductType type){
        this.type = type;
        this.products = new HashMap<>();
        this.cashiers = new ArrayList<>();
    }

    public ProductType getType(){
        return type;
    }

    public Map<String, Product> getProducts(){
        return products;
    }

    public List<Cashier> getCashiers(){
        return cashiers;
    }

    public int getNotWaiting(){
        return notWaiting;
    }

    public void addProduct(Product product){
        products.put(product.getName(), product);
    }

    public void addCashier(Cashier cashier){
        cashiers.add(cashier);
    }

    public Product getProduct(String name){
        return products.get(name);
    }
    // выполнение шагов в общем для касс и товаров
    public void updateStep(){
        for (Product product : products.values()){
            product.updateStep();
        }

        for (Cashier cashier : cashiers){
            cashier.doStep(this);
        }
    }
    // заполнение корзины нужными товарами
    public void fillBasket(Customer customer){
        int need = customer.getDesiredAmount() - customer.getBasketProductCount();

        for(Product product : products.values()){
            if (need <= 0) {
                break;
            }
            if (product.getAmount() > 0){
                int takeNeed = Math.min(product.getAmount(), need);
                product.sell(takeNeed);

                customer.addToBasket(product.getName(), takeNeed);
                need -= takeNeed;
            }
        }
    }
    // постановка покупателя в очередь
    public void acceptCustomer(Customer customer){
        Cashier cashier = getNotBusyCashier();
        int queueLen = cashier.getQueueLen();

        if (queueLen > customer.getWaiting()){
            notWaiting++;
            System.out.println("Покупатель товаров '" + customer.getDesiredType().getProductType() + "' ушёл.");
        }

        cashier.getQueue().add(customer);
        System.out.println("Покупатель товаров '" + customer.getDesiredType().getProductType() + "' встал в очередь.");
    }
    //самая маленькая очередь
    private Cashier getNotBusyCashier(){
        Cashier notBusy = cashiers.getFirst();
        for (Cashier cashier : cashiers){
            if(cashier.getQueueLen() < notBusy.getQueueLen()){
                notBusy = cashier;
            }
        }
        return notBusy;
    }
}
