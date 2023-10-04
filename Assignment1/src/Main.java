/*
* Derek D'Arcy
* 9/15/23
* Java Assignment 1
* I have neither given nor received unauthorized aid in completing this work,
* nor have I presented someone else's work as my own.
* */

public class Main {
    public static void main(String[] args) {
        //Hardcoded customer information
        final String CUSTOMER_NAME = "Derek";

        //Initializing and setting product values in parallel arrays
        int[] productCode = new int[]{1001, 1002, 1003, 1004};
        //Validating supplied values
        int prodCodeSize = productCode.length;
        for(int i = 0; i< prodCodeSize; i++){
            if(productCode[i] <0){
                System.out.println("Item code invalid. Please try again with valid codes.");
                return;
            }
        }
        String[] productName = {"Eggs", "Pancakes", "Bacon", "Milk"};
        String[] productDescription = {"1 Dozen Grade A Large", "12 Oz Pancake Batter Mix", "1 Lb Smoked Bacon", "1 Gallon 2% Milk"};

        double[] productPrice = {2.99, 5.99, 4.99, 2.39};
        //Validating supplied values
        int priceSize = productPrice.length;
        for(int i = 0; i< priceSize; i++){
            if(productPrice[i] <0){
                System.out.println("Item Price invalid. Please try again with valid codes.");
                return;
            }
        }

        //Hardcoded amount of each item customer purchased
        int[] amountPurchased = new int[]{2,1,4,2};
        //Validating supplied values
        int amountSize = amountPurchased.length;
        for(int i = 0; i< amountSize; i++){
            if(amountPurchased[i] <0){
                System.out.println("Amount purchased invalid. Please try again with valid codes.");
                return;
            }
        }
        //Calculate subtotal and tax
        double[] purchasedPrice = new double[4];
        double subTotal = 0;
        for(int i = 0; i<4; i++) {
            purchasedPrice[i] = productPrice[i] * amountPurchased[i];
            subTotal += purchasedPrice[i];
        }
        double tax = subTotal * .06;
        double total = subTotal + tax;

        print(CUSTOMER_NAME, productCode, productName, productDescription, amountPurchased, productPrice, subTotal, tax, total);
    }

    public static void print(String CUSTOMER_NAME, int [] productCode, String [] productName, String[] productDescription, int []amountPurchased, double []productPrice, double subTotal, double tax, double total){
    //This function handles printing out and formatting the final receipt
        System.out.println("Welcome to Java Store");
        System.out.println("Customer: "+ CUSTOMER_NAME);
        System.out.println("Itemized Receipt: ");
        System.out.print("Product Code\t Product name\t Product Description \t\t Amount Purchased \t\t Price Per Item \t\tPrice");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        for(int i = 0; i<4; i++){
            System.out.printf("%-17d%-16s%-29s%-29s%.2f%17.2f\n",productCode[i], productName[i], productDescription[i], amountPurchased[i], productPrice[i], (productPrice[i] * amountPurchased[i]));
        }
        System.out.printf("\n%102s%10.2f", "Subtotal: ", subTotal);
        System.out.printf("\n%97s%15.2f", "Tax: ", tax);
        System.out.printf("\n%99s%13.2f","Total: ", total);
    }

}