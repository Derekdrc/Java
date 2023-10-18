/*
 * Derek D'Arcy
 * 10/15/23
 * Java Assignment
 * I have neither given nor received unauthorized aid in completing this work,
 * nor have I presented someone else's work as my own.
 * */

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner rd = new Scanner(System.in);
        System.out.println("Please enter customer name: ");

        String customerName = rd.nextLine();

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

        //Call function to print out the available items and descriptions
        printCatalog(customerName, productCode, productName, productDescription, productPrice);

        //Get amount purchased of each item from user by calling function
        int[] amountPurchased = new int[4];
        for(int i = 0; i<4; i++){
            amountPurchased[i] = getNumberPurchased(rd, productName, i);
        }

        double[] itemDiscount = new double[4];
        for (int i = 0; i<amountPurchased.length; i++){
            if(amountPurchased[i]>=10){
                itemDiscount[i] = (productPrice[i] * amountPurchased[i]) * .1;
            }
        }


        //Validating supplied values
        int amountSize = amountPurchased.length;
        for(int i = 0; i< amountSize; i++){
            if(amountPurchased[i] <0){
                System.out.println("Amount purchased invalid. Please try again with valid codes.");
                return;
            }
        }

        //Calculate subtotal, discounts and tax
        double[] purchasedPrice = new double[4];
        double subTotal = 0;
        for(int i = 0; i<4; i++) {
            if(amountPurchased[i] >=10){
                purchasedPrice[i] = (productPrice[i] * amountPurchased[i]) * .9;
            }else{
                purchasedPrice[i] = productPrice[i] * amountPurchased[i];
            }
            subTotal += purchasedPrice[i];
        }
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

        double tax = (subTotal-payFullDiscount) * .06;
        double total = subTotal-payFullDiscount + tax;

        //Call the function to print out the receipt
        printFinal(customerName, productCode, productName, productDescription, amountPurchased, productPrice, itemDiscount, subTotal, payFullDiscount, tax, total);

        //Close scanner object
        rd.close();
    }

    public static void printCatalog(String customerName, int [] productCode, String [] productName, String[] productDescription, double [] productPrice){
        //This function handles printing out and formatting the initial catalog
        System.out.println("Hello " + customerName);
        System.out.println("These are the available products for you to purchase: ");
        System.out.print("Product Code\t Product name\t Product Description \t\tPrice");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------------------");
        for(int i = 0; i<4; i++){
            System.out.printf("%-17d%-16s%-29s%-29s\n",productCode[i], productName[i], productDescription[i], productPrice[i]);
        }
    }

    public static int getNumberPurchased(Scanner rd, String [] productName, int currentItem){
        //This function gets and verifies input from the user to get how many of a product they are purchasing
        System.out.println("How many " + productName[currentItem] + " would you like to purchase?");
        int purchase = rd.nextInt();
        while (purchase <0){
            System.out.println("Invalid amount. Please enter how a valid number to purchase");
            purchase = rd.nextInt();
        }
        return purchase;
    }

    public static void printFinal(String customerName, int [] productCode, String [] productName, String[] productDescription, int []amountPurchased, double []productPrice, double []itemDiscount, double subTotal, double payFullDiscount, double tax, double total){
        //This function handles printing out and formatting the final receipt
        System.out.println("\n\n\nCustomer: "+ customerName);
        System.out.println("Itemized Receipt: ");
        System.out.print("Product Code\t Product name\t Product Description \t\t Amount Purchased \t\t Price Per Item \t\t Item Discounts \t\tPrice");
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
        for(int i = 0; i<4; i++){
            System.out.printf("%-17d%-16s%-29s%-29s%.2f%26.2f%17.2f\n",productCode[i], productName[i], productDescription[i], amountPurchased[i], productPrice[i], (-1*itemDiscount[i]), (productPrice[i] * amountPurchased[i]) - itemDiscount[i]);
        }
        System.out.printf("\n%129s%10.2f", "Subtotal: ", subTotal);
        System.out.printf("\n%129s%10.2f", "Pay in Full Discount: ",(-1*payFullDiscount));
        System.out.printf("\n%129s%10.2f", "Tax: ", tax);
        System.out.printf("\n\n%129s%10.2f","Total: ", total);
    }

}