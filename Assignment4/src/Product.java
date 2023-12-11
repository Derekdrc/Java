//Product Class
class Product {
    //Member Variables
    int productCode;
    String productName;
    String productDescription;
    double productPrice;
    int amountPurchased;
    double discount;
    String productIconSource;

    //Constructor
    public Product(int code, String name, String desc, double price, String prodIcon) {
        this.productCode = code;
        this.productName = name;
        this.productDescription = desc;
        this.productPrice = price;
        this.productIconSource = prodIcon;
    }

    //Getters
    public int getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getAmountPurchased() {
        return amountPurchased;
    }

    public double getDiscount() {
        return discount;
    }

    public String getProductIconSource() {
        return productIconSource;
    }

    //Setters
    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public void setAmountPurchased(int amountPurchased) {
        this.amountPurchased = amountPurchased;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setProductIconSource(String productIconSource) {
        this.productIconSource = productIconSource;
    }
}