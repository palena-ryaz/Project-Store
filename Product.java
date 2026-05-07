package market;

public class Product {

    protected String name;
    protected ProductType type;
    protected double price;
    protected double priceBase;
    protected double priceSeason;
    protected int amount;
    protected int amountBase;
    protected int timeRestock;
    protected int countStepsRestock;
    protected boolean isSeason;
    protected int soldCount;
    protected double soldIncome;

    public Product(String name, ProductType type, double priceBase, double priceSeason, int amount, int timeRestock,
                   boolean isSeason){
        this.name = name;
        this.type = type;
        this.price = priceBase;
        this.priceBase = priceBase;
        this.priceSeason = priceSeason;
        this.amount = amount;
        this.timeRestock = timeRestock;
        this.countStepsRestock = timeRestock;
        this.isSeason = isSeason;
        this.soldCount = 0;
        this.soldIncome = 0;
        this.amountBase = amount;
    }

    public int getAmount(){
        return amount;
    }

    public double getPrice(){
        return price;
    }

    public String getName(){
        return name;
    }

    public double getSoldIncome(){
        return soldIncome;
    }

    public int getSoldCount(){
        return soldCount;
    }
    // Один шаг
    public void updateStep(){
        countStepsRestock --;
        if (countStepsRestock <= 0){
            restock();
        }

        if (isSeason){
            updatePrice();
        }
    }
    // Сезонные товары обновляются
    protected void updatePrice(){
        double randomValue = Math.random();
        if (randomValue <= 0.2){
            this.price = this.priceBase + (priceBase * priceSeason);
            System.out.println("Цена товара '"+ this.name + "' обновлено до " + this.getPrice() + ".");
        }
    }
    // Обновление товара в отделе
    protected void restock(){
        this.amount = this.amountBase;
        this.countStepsRestock = this.timeRestock;
        System.out.println("Количество товара '" + this.name + "' обновлено.");
    }
    // списание товара с отдела для продажи в кассах
    public void sell(int amount){
        if (amount != 0 && amount <= this.amount){
            this.amount -= amount;
            this.soldCount += amount;
            this.soldIncome += this.price * amount;
        }
    }

    public boolean getIsSeason(){
        return isSeason;
    }

    public double getPriceBase(){
        return priceBase;
    }

    public ProductType getType() {
        return type;
    }
}
