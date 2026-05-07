package market;

public enum ProductType {
    FRUITS("Фрукты"),
    VEGETABLES("Овощи"),
    MEAT("Мясо"),
    BAKERY("Выпечка");

    private String productType;

    ProductType(String productType){
        this.productType = productType;
    }

    public String getProductType(){
        return productType;
    }
}
