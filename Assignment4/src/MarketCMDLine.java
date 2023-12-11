import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MarketCMDLine {
    //ArrayList to hold catalog of products
    ArrayList<Product> productCatalog = new ArrayList<>();

    //ArrayList to hold list of customers
    ArrayList<Customer> customerList = new ArrayList<>();

    public MarketCMDLine() throws IOException, ParseException {
        //First, fill products and customers from file
        loadCustomers();
        loadProducts();


        //Initialize Scanner
        Scanner rd = new Scanner(System.in);

        int custCode = -1;
        System.out.println("Please enter Customer ID: ");
        boolean success = false;
        while(!success) {
            try {
                custCode = rd.nextInt();
                for(int i = 0; i<customerList.size(); i++){
                    if(custCode == customerList.get(i).getId()){
                        success = true;
                    }
                }
                if(!success) {
                    System.out.println("Not a valid Customer ID, please try again");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Invalid input. Please try again. ");
                rd.nextLine();
            }

        }

        String customerName = " ";
        for(int i = 0; i<customerList.size(); i++){
            if(custCode == customerList.get(i).getId()){
                customerName = customerList.get(i).getFirstName() + " " + customerList.get(i).getLastName();
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

        //call Function to check if user wishes to print receipt
        System.out.println();
        int choice = -1;
        boolean print = false;
        while(!print) {
            try {
                System.out.println("Would you like to print the receipt? (Save to file) Press '1' to print, or '2' to not print");
                choice = rd.nextInt();
                if(choice !=1 && choice !=2){
                    System.out.println("Not a valid choice. Please choose '1' to print, or '2' to not print");
                }else if(choice == 1){
                    saveReceiptToFile(customerName, productCatalog, roundDouble(subTotal), roundDouble(payFullDiscount), roundDouble(tax), roundDouble(total));
                    print =true;
                }else{
                    System.out.println("Thank you for shopping with us!");
                    print = true;
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again. ");
                rd.nextLine();
            }

        }

        //Close scanner object
        rd.close();
        for(int i = 0; i<customerList.size(); i++){
            if(custCode == customerList.get(i).getId()){
                customerList.get(i).setPrevTotal(total);
                String tempDate = new SimpleDateFormat("MM/dd/yyyy").format((Calendar.getInstance().getTime()));
                customerList.get(i).setPrevDate(tempDate);
            }
        }
        saveCustomerList();
    }

    //Load customers from file
    void loadCustomers() throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader("src//Files//Customers.txt"));

        int tempId;
        String tempFName;
        String tempLName;
        double tempTotal;
        String tempDate;

        String line = br.readLine();
        while(line != null){
            tempId = Integer.parseInt(line);
            tempFName = br.readLine();
            tempLName = br.readLine();
            tempTotal = Double.parseDouble(br.readLine());
            tempDate = br.readLine();

            customerList.add(new Customer(tempId, tempFName, tempLName, tempTotal, tempDate));

            line = br.readLine();
        }
        br.close();
    }

    //Load products from file
    void loadProducts() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src//Files//Products.txt"));

        int tempProductCode;
        String tempProductName;
        String tempProductDescription;
        double tempProductPrice;
        String tempProductIconSource;

        String line = br.readLine();
        while(line != null){
            tempProductCode = Integer.parseInt(line);
            tempProductName = br.readLine();
            tempProductDescription = br.readLine();
            tempProductPrice = Double.parseDouble(br.readLine());
            tempProductIconSource = br.readLine();

            productCatalog.add(new Product(tempProductCode, tempProductName, tempProductDescription, tempProductPrice, tempProductIconSource));

            line = br.readLine();
        }
        br.close();
    }

    //Save customer list
    void saveCustomerList() throws IOException{
        FileWriter customer_file = new FileWriter("src//Files//Customers.txt", false);
        BufferedWriter bw = new BufferedWriter(customer_file);

        for(int i = 0; i < customerList.size(); i++){
            bw.write(Integer.toString(customerList.get(i).getId()));
            bw.newLine();
            bw.write(customerList.get(i).getFirstName());
            bw.newLine();
            bw.write(customerList.get(i).getLastName());
            bw.newLine();
            bw.write(Double.toString(roundDouble(customerList.get(i).getPrevTotal())));
            bw.newLine();
            bw.write(customerList.get(i).getPrevDate());
            bw.newLine();
        }
        bw.flush();
        bw.close();
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

    public static void saveReceiptToFile(String customerName, ArrayList<Product> productCatalog, double subTotal, double payFullDiscount, double tax, double total) throws IOException {
        FileWriter receipt_file = new FileWriter("src//Files//Receipt.txt");
        BufferedWriter bw = new BufferedWriter(receipt_file);

        bw.write(customerName);
        bw.newLine();
        String tempDate = new SimpleDateFormat("MM/dd/yyyy").format((Calendar.getInstance().getTime()));
        bw.write(tempDate);
        bw.newLine();
        bw.write(String.format( "%-17s %-17s %-25s %-17s %-30s %-17s %-17s","Code", "Name", "Description", "Amount Purchased", "Price Per Item", "Discounts", "Price"));
        bw.newLine();
        for(int i = 0; i<productCatalog.size(); i++){
            bw.write(String.format("%-17s %-17s", productCatalog.get(i).getProductCode(), productCatalog.get(i).getProductName()));
            bw.write(String.format( "%-30s %-17s %-30s",productCatalog.get(i).getProductDescription(), productCatalog.get(i).getAmountPurchased(), productCatalog.get(i).getProductPrice()));
            double tempDiscount = (-1*productCatalog.get(i).getDiscount());
            tempDiscount = Math.round(tempDiscount*100);
            tempDiscount = tempDiscount/100;
            bw.write(String.format("%-17s",tempDiscount));
            double tempPrice = (productCatalog.get(i).getProductPrice() * productCatalog.get(i).getAmountPurchased()) - productCatalog.get(i).getDiscount();
            tempPrice = Math.round(tempPrice*100);
            tempPrice = tempPrice/100;
            bw.write(String.format("%-17s",tempPrice));
            bw.newLine();
        }
        bw.newLine();
        bw.newLine();
        bw.write("Subtotal: $" + Double.toString(subTotal));
        bw.newLine();
        bw.write("Pay in Full Discount: -$" + Double.toString(payFullDiscount));
        bw.newLine();
        bw.write("Tax: $" + Double.toString(tax));
        bw.newLine();
        bw.write("Total: $" + Double.toString(total));
        bw.newLine();
        bw.flush();
        bw.close();
    }

    //round number to 2 decimals
    double roundDouble(double num){
        num = Math.round(num*100);
        return (num/100);
    }

}
