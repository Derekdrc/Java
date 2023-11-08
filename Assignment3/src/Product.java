/*
 * Derek D'Arcy
 * 11/2/23
 * Java Assignment
 * I have neither given nor received unauthorized aid in completing this work,
 * nor have I presented someone else's work as my own.
 * */

import java.util.ArrayList;
import java.util.Scanner;

public class Product {
    //Member Variables
    int productCode;
    String productName;
    String productDescription;
    double productPrice;
    int amountPurchased;
    double discount;

    //Constructor
    public Product(int code, String name, String desc, double price){
        this.productCode = code;
        this.productName = name;
        this.productDescription = desc;
        this.productPrice = price;
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

    public void setDiscount(double discount) { this.discount = discount; }

    public static void main(String[] args) {
        //create arrayList to hold all product objects
        ArrayList<Product> productCatalog = new ArrayList<Product>();

        Product egg =  new Product(1001, "Eggs", "1 Dozen Grade A Large", 2.99);
        productCatalog.add(egg);
        Product pancake =  new Product(1002, "Pancakes", "12 Oz Pancake Batter Mix", 5.99);
        productCatalog.add(pancake);
        Product bacon =  new Product(1003, "Bacon", "1 Lb Smoked Bacon", 4.99);
        productCatalog.add(bacon);
        Product milk =  new Product(1004, "Milk", "1 Gallon 2% Milk", 2.39);
        productCatalog.add(milk);

        //Initialize Scanner
        Scanner rd = new Scanner(System.in);

        //checks for passcode even if it is not the only argument string provided
        for(int i = 0; i < args.length; i++){
            //check args array for run time given 'password'
            if(args[i].equals("MCS3603")){
               while(true) {
                   //Loop through to let admin create as many new products as they would like
                   productCatalog.add(adminConsole(rd, productCatalog));
                   System.out.println("Press '1' if you would like to add another product, press '2' if you would like to exit the admin console: ");
                   int choice = rd.nextInt();
                   while(choice != 1 && choice != 2){
                       System.out.println("Invalid input. Press '1' if you would like to add another product, press '2' if you would like to exit the admin console: ");
                       choice = rd.nextInt();
                   }
                   if (choice == 2){
                       //Clear buffer
                       rd.nextLine();
                       break;
                   }
                   //Clear buffer
                   rd.nextLine();
               }
            }
        }


        //Validate supplied product codes
        for(int i = 0; i < productCatalog.size(); i++){
            if(productCatalog.get(i).getProductCode() < 0){
                System.out.println("Item code invalid. Please try again with valid codes.");
                return;
            }
        }


        //Get users name
        System.out.println("Please enter customer name: ");
        String customerName = rd.nextLine();


        //Validating supplied prices
        for(int i = 0; i< productCatalog.size(); i++){
            if(productCatalog.get(i).getProductPrice() <0){
                System.out.println("Item Price invalid. Please try again with valid codes.");
                return;
            }
        }

        //Call function to print out the available items and descriptions
        printCatalog(customerName, productCatalog);

        //Get amount purchased of each item from user by calling function
        for(int i = 0; i< productCatalog.size(); i++){
            productCatalog.get(i).setAmountPurchased(getNumberPurchased(rd, productCatalog, i));
        }

        //Set the discount according to each items quantity purchased
        for (int i = 0; i<productCatalog.size(); i++){
            if(productCatalog.get(i).getAmountPurchased()>=10){
                productCatalog.get(i).setDiscount((productCatalog.get(i).getProductPrice() * productCatalog.get(i).getAmountPurchased() * .1));
            }
        }

        //Validating supplied values
        for(int i = 0; i< productCatalog.size(); i++){
            if(productCatalog.get(i).getAmountPurchased()<0){
                System.out.println("Amount purchased invalid. Please try again with valid amounts.");
                return;
            }
        }

        //Calculate subtotal, discounts and tax
        double subTotal = 0;
        for(int i = 0; i< productCatalog.size(); i++) {
            if(productCatalog.get(i).getAmountPurchased() >=10){
                subTotal += (productCatalog.get(i).getProductPrice() * productCatalog.get(i).getAmountPurchased()) * .9;
            }else{
                subTotal += productCatalog.get(i).getProductPrice() * productCatalog.get(i).getAmountPurchased();
            }
        }

        //Calculate the pay in full discount
        System.out.println("Would you like to pay in full, or pay in installments? Press '1' to pay in full now and receive a 5% discount, press '2' for installments.");
        int installChoice = rd.nextInt();
        while(installChoice!=1 && installChoice!=2){
            System.out.println("Invalid choice. Please Press '1' to pay in full now, press '2' for installments.");
            installChoice = rd.nextInt();
        }
        double payFullDiscount = 0;
        if(installChoice == 1){
            payFullDiscount = subTotal*.05;
        }

        //Calculate tax and total
        double tax = (subTotal-payFullDiscount) * .06;
        double total = subTotal-payFullDiscount + tax;

        //Call the function to print out the receipt
        printFinal(customerName, productCatalog, subTotal, payFullDiscount, tax, total);

        //Close scanner object
        rd.close();

    }

    public static Product adminConsole(Scanner rd, ArrayList<Product> catalog) {
        //This function acts as the admin ability to create a new product and add it to catalog when password is supplied
        System.out.println("Welcome to the admin console.\n");
        boolean more = true;
            String temp = " ";
            int tempCode = 0;
            String tempName = " ";
            String tempDescr = " ";
            double tempPrice = 0.0;
            //Do/while loops until inputs are valid
            //Item code
            do {
                try {
                    System.out.println("Please enter a valid item code for the new product: ");
                    temp = rd.nextLine();
                    tempCode = Integer.parseInt(temp);
                    //Validate that product code is unique
                    for(int i = 0; i< catalog.size(); i++){
                        while(tempCode == catalog.get(i).getProductCode()){
                            System.out.println("Product code already in use. Try again. ");
                            temp = rd.nextLine();
                            tempCode = Integer.parseInt(temp);
                        }
                    }
                    //If everything checks out, break out of do while loop to continue
                    break;
                } catch (Exception e) {
                    //Catch invalid type and make user restart
                    System.out.println("Invalid input.");
                }
            }while(true);
            //Item Name
            do {
                try {
                    System.out.println("Please enter an item name for the new product: ");
                    boolean nonNumeric = true;
                    temp = rd.nextLine();
                    char[] chars = temp.toCharArray();
                    for(int i = 0; i<chars.length; i++){
                        if(Character.isDigit(chars[i])){
                            nonNumeric = false;
                        }
                    }
                    if(nonNumeric){
                        tempName = (temp);
                        break;
                    }else{
                        System.out.println("Invalid input type.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input.");
                }
            }while(true);
            //Item Description
            do {
                try {
                    System.out.println("Please enter an item description for the new product: ");
                    boolean nonNumeric = true;
                    temp = rd.nextLine();
                    char[] chars = temp.toCharArray();
                    for(int i = 0; i< chars.length; i++){
                        if(Character.isDigit(chars[i])){
                            nonNumeric = false;
                        }
                    }
                    if(nonNumeric){
                        tempDescr = (temp);
                        break;
                    }else{
                        System.out.println("Invalid input. ");
                    }

                } catch (Exception e) {
                    System.out.println("Invalid input.");
                }
            }while (true);
            //Item price
            do{
                try {
                    System.out.println("Please enter a valid item price for the new product: ");
                    temp = rd.nextLine();
                    tempPrice = Double.parseDouble(temp);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid input.");
                }

            } while (true);
        // Once everything is validated, create new instance of Product object and return it to be added into Catalog arraylist
        return new Product(tempCode, tempName, tempDescr, tempPrice);
    }

    public static void printCatalog(String customerName, ArrayList<Product> catalog){
        //This function handles printing out and formatting the initial catalog
        System.out.println("Hello " + customerName);
        System.out.println("These are the available products for you to purchase: ");
        System.out.print("Product Code\t Product name\t Product Description \t\tPrice");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------------------");
        for(int i = 0; i< catalog.size(); i++){
            System.out.printf("%-17d%-16s%-29s%-29s\n",catalog.get(i).getProductCode(), catalog.get(i).getProductName(), catalog.get(i).getProductDescription(), catalog.get(i).getProductPrice());
        }
    }

    public static int getNumberPurchased(Scanner rd, ArrayList<Product> catalog, int currentItem){
        //This function gets and verifies input from the user to get how many of a product they are purchasing
        System.out.println("How many " + catalog.get(currentItem).getProductName() + " would you like to purchase?");
        int purchase = rd.nextInt();
        while (purchase <0){
            System.out.println("Invalid amount. Please enter how a valid number to purchase");
            purchase = rd.nextInt();
        }
        return purchase;
    }

    public static void printFinal(String customerName, ArrayList<Product> catalog, double subTotal, double payFullDiscount, double tax, double total){
        //This function handles printing out and formatting the final receipt
        System.out.println("\n\n\nCustomer: "+ customerName);
        System.out.println("Itemized Receipt: ");
        String [] labels = {"Product Code", "Product Name", "Product Description", "Amount Purchased", "Price Per Item", "Item Discounts", "Price"};
        System.out.printf("%-17s%-16s%-29s%-29s%-25s%-18s%-17s", labels[0], labels[1], labels[2], labels[3], labels[4], labels[5], labels[6]);
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
        for(int i = 0; i< catalog.size(); i++){
            System.out.printf("%-17d%-16s%-29s%-29s%.2f%26.2f%17.2f\n",catalog.get(i).getProductCode(), catalog.get(i).getProductName(), catalog.get(i).getProductDescription(), catalog.get(i).getAmountPurchased(), catalog.get(i).getProductPrice(), (-1*catalog.get(i).getDiscount()), (catalog.get(i).getProductPrice() * catalog.get(i).getAmountPurchased()) - catalog.get(i).getDiscount());
        }
        System.out.printf("\n%129s%10.2f", "Subtotal: ", subTotal);
        System.out.printf("\n%129s%10.2f", "Pay in Full Discount: ",(-1*payFullDiscount));
        System.out.printf("\n%129s%10.2f", "Tax: ", tax);
        System.out.printf("\n\n%129s%10.2f","Total: ", total);
    }

}