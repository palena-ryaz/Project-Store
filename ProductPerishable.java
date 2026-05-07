package market;

public class ProductPerishable extends Product{

    private int timeExpire;
    private int countStepsExpire;

    public ProductPerishable(String name, ProductType type, double priceBase, double priceSeason, int amount,
                             int timeRestock, boolean isSeason, int timeExpire){
        super(name, type, priceBase, priceSeason, amount, timeRestock, isSeason);

        this.timeExpire = timeExpire;
        this.countStepsExpire = timeExpire;
    }

    public int getCountStepsExpire(){
        return countStepsExpire;
    }

    // один шаг для наследника с порчей продукта
    public void updateStep() {
        super.updateStep();

        this.countStepsExpire --;
        if (this.countStepsExpire <= 0){
            this.expire();
        }
    }
    // снятие товара с отдела с истечением срока годности
    private void expire(){
        this.amount = 0;
        System.out.println("Срок годности товара '" + this.name + "' истёк.");
    }
}
