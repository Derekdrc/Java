/*
Derek D'Arcy
11/30/23
Assignment 4 - GUI Market
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MarketGUI implements ActionListener, MouseListener {
    //Frame to hold all panels
    JFrame window = new JFrame("Market");

    //Card Layout to switch between start screen, purchase screen, and invoice screen
    CardLayout cl = new CardLayout();
    JPanel container = new JPanel(cl);

    //Panel to hold start screen, user will enter ID here
    JPanel startScreen = new JPanel(new GridLayout(2,2));
    JLabel customerIdLabel = new JLabel("Customer ID: ");
    JButton confirmIdButton = new JButton("Confirm Customer ID");
    JLabel messageLabel = new JLabel("Please enter valid Customer ID!");
    JTextField enterIdField = new JTextField("Enter Customer ID Here");


    //Screen to hold all panels with ability to purchase
    JPanel purchaseScreen = new JPanel(new BorderLayout());
    //Info Panel
    JPanel infoPanel = new JPanel(new GridLayout(1,4));
    JLabel customerFName = new JLabel();
    JLabel customerLName = new JLabel();
    JLabel customerLastTotal = new JLabel();
    JLabel customerLastDate = new JLabel();
    //Product Panel
    JPanel productPanel = new JPanel(new GridLayout(6, 8));
    JLabel prodCodeLabel = new JLabel("Product Code");
    JLabel prodNameLabel = new JLabel("Product Name");
    JLabel prodDescriptionLabel = new JLabel("Product Description");
    JLabel prodPriceLabel = new JLabel("Product Price");
    JLabel prodImageLabel = new JLabel("Product Image");
    JLabel minus1Label = new JLabel("-1");
    JLabel numLabel = new JLabel("Current Amount");
    JLabel plus1Label = new JLabel(("+1"));
    ArrayList<JLabel> prodCodesList = new ArrayList<>();
    ArrayList<JLabel> prodNamesList = new ArrayList<>();
    ArrayList<JLabel> prodDescrList = new ArrayList<>();
    ArrayList<JLabel> prodPriceList = new ArrayList<>();
    ArrayList<JLabel> prodImageList = new ArrayList<>();
    ArrayList<JButton> minusButtonList = new ArrayList<>();
    ArrayList<JLabel> amountLabelList = new ArrayList<>();
    ArrayList<JButton> plusButtonList = new ArrayList<>();
    //Purchase Panel
    JPanel purchaseButtonPanel = new JPanel(new GridLayout(1,2));
    JButton confirmPurchase = new JButton("Confirm Purchase");
    String[] paymentMethodArray = {"Pay in full", "Pay in Installments"};
    JComboBox paymentMethodBox = new JComboBox(paymentMethodArray);


    //Panel to hold entire invoice screen
    JPanel invoiceScreen = new JPanel(new BorderLayout());
    //Panel to hold customer name and date
    JPanel nameDatePanel = new JPanel(new GridLayout(2,1));
    JLabel nameLabel = new JLabel("Customer: ");
    JLabel dateLabel = new JLabel("Date: ");
    //Panel to hold the main itemized list
    JPanel mainItemizedPanel = new JPanel(new GridLayout(6, 7));
    JLabel invoiceCodeLabel = new JLabel("Product Code");
    JLabel invoiceProdNameLabel = new JLabel("Product Name");
    JLabel invoiceDescriptionLabel = new JLabel("Product Description");
    JLabel invoiceAmountPurchasedLabel = new JLabel("Amount Purchased");
    JLabel invoicePricePerLabel = new JLabel("Product Price");
    JLabel invoiceDiscountsLabel = new JLabel("Discounts");
    JLabel invoicePriceLabel = new JLabel("Price");
    ArrayList<JLabel> invoiceCodesLabelList = new ArrayList<>();
    ArrayList<JLabel> invoiceNameLabelList = new ArrayList<>();
    ArrayList<JLabel> invoiceDescrLabelList = new ArrayList<>();
    ArrayList<JLabel> invoiceAmountLabelList = new ArrayList<>();
    ArrayList<JLabel> invoicePricePerLabelList = new ArrayList<>();
    ArrayList<JLabel> invoiceDiscountLabelList = new ArrayList<>();
    ArrayList<JLabel> invoicePriceLabelList = new ArrayList<>();
    //Panel to hold price, tax and discounts
    JPanel summaryPanel = new JPanel(new GridLayout(1,7));
    JButton printReceipt = new JButton("Print Receipt?");
    JPanel finalPricePanel = new JPanel(new GridLayout(4, 1));
    JLabel subtotalLabel = new JLabel("Subtotal: $");
    JLabel payFullDiscLabel = new JLabel("Pay in full discount: -$");
    JLabel taxLabel = new JLabel("Tax: $");
    JLabel totalLabel = new JLabel("Total: $");


    //ArrayList to hold catalog of products
    ArrayList<Product> productCatalog = new ArrayList<>();

    //ArrayList to hold list of customers
    ArrayList<Customer> customerList = new ArrayList<>();

    public MarketGUI() throws IOException, ParseException {
        //First, fill products and customers from file
        loadCustomers();
        loadProducts();

        window.add(container);
        container.add(startScreen, "start");
        container.add(purchaseScreen, "purchase");
        container.add(invoiceScreen, "invoice");

        //Populate start screen
        startScreen.add(customerIdLabel);
        customerIdLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        customerIdLabel.setHorizontalAlignment(JLabel.CENTER);
        startScreen.add(enterIdField);
        enterIdField.setHorizontalAlignment(JTextField.CENTER);
        startScreen.add(messageLabel);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        startScreen.add(confirmIdButton);
        confirmIdButton.addActionListener(this);

        //Populate Purchase Screen
        purchaseScreen.add(infoPanel, BorderLayout.NORTH);
        infoPanel.add(customerFName);
        customerFName.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(customerLName);
        customerLName.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(customerLastTotal);
        customerLastTotal.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(customerLastDate);
        customerLastDate.setHorizontalAlignment(JLabel.CENTER);
        purchaseScreen.add(productPanel, BorderLayout.CENTER);
        productPanel.add(prodCodeLabel);
        prodCodeLabel.setHorizontalAlignment(JLabel.CENTER);
        productPanel.add(prodNameLabel);
        prodNameLabel.setHorizontalAlignment(JLabel.CENTER);
        productPanel.add(prodDescriptionLabel);
        prodDescriptionLabel.setHorizontalAlignment(JLabel.CENTER);
        productPanel.add(prodPriceLabel);
        prodPriceLabel.setHorizontalAlignment(JLabel.CENTER);
        productPanel.add(prodImageLabel);
        prodImageLabel.setHorizontalAlignment(JLabel.CENTER);
        productPanel.add(minus1Label);
        minus1Label.setHorizontalAlignment(JLabel.CENTER);
        productPanel.add(numLabel);
        numLabel.setHorizontalAlignment(JLabel.CENTER);
        productPanel.add(plus1Label);
        plus1Label.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 0; i<productCatalog.size(); i++){
            String tempCode = Integer.toString(productCatalog.get(i).getProductCode());
            String tempPrice = "$" + Double.toString(productCatalog.get(i).getProductPrice());

            prodCodesList.add(new JLabel(tempCode));
            prodNamesList.add(new JLabel(productCatalog.get(i).getProductName()));
            prodDescrList.add(new JLabel(productCatalog.get(i).getProductDescription()));
            prodPriceList.add(new JLabel(tempPrice));
            ImageIcon tempImg = new ImageIcon(productCatalog.get(i).getProductIconSource());
            prodImageList.add(new JLabel(tempImg));
            minusButtonList.add(new JButton("-"));
            minusButtonList.get(i).addActionListener(this);
            amountLabelList.add(new JLabel("0"));
            amountLabelList.get(i).setForeground(Color.BLUE);
            plusButtonList.add(new JButton("+"));
            plusButtonList.get(i).addActionListener(this);
        }

        for(int i = 0; i < prodCodesList.size(); i++){
            productPanel.add(prodCodesList.get(i));
            prodCodesList.get(i).setHorizontalAlignment(JLabel.CENTER);
            productPanel.add(prodNamesList.get(i));
            prodNamesList.get(i).setHorizontalAlignment(JLabel.CENTER);
            productPanel.add(prodDescrList.get(i));
            prodDescrList.get(i).setHorizontalAlignment(JLabel.CENTER);
            productPanel.add(prodPriceList.get(i));
            prodPriceList.get(i).setHorizontalAlignment(JLabel.CENTER);
            productPanel.add(prodImageList.get(i));
            prodImageList.get(i).setHorizontalAlignment(JLabel.CENTER);
            productPanel.add(minusButtonList.get(i));
            minusButtonList.get(i).setHorizontalAlignment(JLabel.CENTER);
            productPanel.add(amountLabelList.get(i));
            amountLabelList.get(i).setHorizontalAlignment(JLabel.CENTER);
            productPanel.add(plusButtonList.get(i));
            plusButtonList.get(i).setHorizontalAlignment(JLabel.CENTER);
        }

        purchaseScreen.add(purchaseButtonPanel, BorderLayout.SOUTH);
        purchaseButtonPanel.add(paymentMethodBox);
        paymentMethodBox.setSelectedIndex(0);
        paymentMethodBox.addActionListener(this);
        purchaseButtonPanel.add(confirmPurchase);
        confirmPurchase.addActionListener(this);


        //Populate Invoice Screen
        invoiceScreen.add(nameDatePanel, BorderLayout.NORTH);
        invoiceScreen.add(mainItemizedPanel, BorderLayout.CENTER);
        invoiceScreen.add(summaryPanel, BorderLayout.SOUTH);
        nameDatePanel.add(nameLabel);
        nameDatePanel.add(dateLabel);
        mainItemizedPanel.add(invoiceCodeLabel);
        mainItemizedPanel.add(invoiceProdNameLabel);
        mainItemizedPanel.add(invoiceDescriptionLabel);
        mainItemizedPanel.add(invoiceAmountPurchasedLabel);
        mainItemizedPanel.add(invoicePricePerLabel);
        mainItemizedPanel.add(invoiceDiscountsLabel);
        mainItemizedPanel.add(invoicePriceLabel);
        for(int i = 0; i<productCatalog.size(); i++){
            invoiceCodesLabelList.add(new JLabel(Integer.toString(productCatalog.get(i).getProductCode())));
            invoiceNameLabelList.add(new JLabel(productCatalog.get(i).getProductName()));
            invoiceDescrLabelList.add(new JLabel(productCatalog.get(i).getProductDescription()));
            invoiceAmountLabelList.add(new JLabel());
            invoicePricePerLabelList.add(new JLabel("$" + Double.toString(productCatalog.get(i).getProductPrice())));

            invoiceDiscountLabelList.add(new JLabel("- $"));

            invoicePriceLabelList.add(new JLabel("$"));
        }

        for(int i = 0; i<productCatalog.size(); i++){
            mainItemizedPanel.add(invoiceCodesLabelList.get(i));
            mainItemizedPanel.add(invoiceNameLabelList.get(i));
            mainItemizedPanel.add(invoiceDescrLabelList.get(i));
            mainItemizedPanel.add(invoiceAmountLabelList.get(i));
            mainItemizedPanel.add(invoicePricePerLabelList.get(i));
            mainItemizedPanel.add(invoiceDiscountLabelList.get(i));
            mainItemizedPanel.add(invoicePriceLabelList.get(i));
        }

        summaryPanel.add(printReceipt);
        printReceipt.addActionListener(this);
        for(int i = 0; i<5; i++){
            summaryPanel.add(new JLabel(" "));
        }
        summaryPanel.add(finalPricePanel);
        finalPricePanel.add(subtotalLabel);
        finalPricePanel.add(payFullDiscLabel);
        finalPricePanel.add(taxLabel);
        finalPricePanel.add(totalLabel);



        window.setSize(700,300);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cl.show(container, "start");

    }

    //round number to 2 decimals
    double roundDouble(double num){
        num = Math.round(num*100);
        return (num/100);
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

    void saveInvoiceToFile() throws IOException{
        FileWriter receipt_file = new FileWriter("src//Files//Receipt.txt");
        BufferedWriter bw = new BufferedWriter(receipt_file);

        bw.write(customerFName.getText() + " " + customerLName.getText());
        bw.newLine();
        bw.write(dateLabel.getText());
        bw.newLine();
        bw.write(String.format( "%-17s %-17s %-25s %-17s %-30s %-17s %-17s","Code", "Name", "Description", "Amount Purchased", "Price Per Item", "Discounts", "Price"));
        bw.newLine();
        for(int i = 0; i<productCatalog.size(); i++){
            bw.write(String.format("%-17s %-17s", productCatalog.get(i).getProductCode(), productCatalog.get(i).getProductName()));
            bw.write(String.format( "%-30s %-17s %-30s",productCatalog.get(i).getProductDescription(), amountLabelList.get(i).getText(), productCatalog.get(i).getProductPrice()));
            bw.write(String.format("%-17s", invoiceDiscountLabelList.get(i).getText()));
            bw.write(String.format("%-17s", invoicePriceLabelList.get(i).getText()));
            bw.newLine();
        }
        bw.newLine();
        bw.newLine();
        bw.write(subtotalLabel.getText());
        bw.newLine();
        bw.write(payFullDiscLabel.getText());
        bw.newLine();
        bw.write(taxLabel.getText());
        bw.newLine();
        bw.write(totalLabel.getText());
        bw.newLine();
        bw.flush();
        bw.close();
    }

        public static void main(String[] args) throws IOException, ParseException {
        boolean run = false;
        for(int i = 0; i< args.length; i++){
                if(args[i].equals("GUI")){
                    //GUI based
                    run = true;
                    new MarketGUI();
                }
                if (args[i].equals("CMD")){
                    //Text based
                    run = true;
                    new MarketCMDLine();
                }
            }
        if(!run){
            System.out.println("Invalid Run type. Please Specify 'GUI' or 'CMD' and try again");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Button to confirm customer ID
        if(e.getSource() == confirmIdButton){
            String enteredId = enterIdField.getText();
            int testId = -1;
            try{
                testId = Integer.parseInt(enteredId);
            }catch (NumberFormatException ex){
                messageLabel.setText("Invalid Input. Please try again");
                enterIdField.setText("");
            }

            for(int i =0; i< customerList.size(); i++){
                if(customerList.get(i).getId() == testId){
                    customerFName.setText(customerList.get(i).getFirstName());
                    customerLName.setText(customerList.get(i).getLastName());

                    customerLastTotal.setText("Last Invoice: $" + Double.toString(customerList.get(i).getPrevTotal()));
                    String tempDate = customerList.get(i).getPrevDate();
                    customerLastDate.setText("Last Date: " + tempDate);
                    window.setSize(1200,700);
                    cl.show(container, "purchase");
                }
            }
            messageLabel.setText("Invalid Input. Please try again");
            enterIdField.setText("");
        }
        //-1 buttons
        for(int i = 0; i < productCatalog.size(); i++){
            if(e.getSource() == minusButtonList.get(i)){
                if(amountLabelList.get(i).getText().equals("0")){
                    break;
                }else{
                    int tempAmount = Integer.parseInt(amountLabelList.get(i).getText());
                    tempAmount--;
                    amountLabelList.get(i).setText(Integer.toString(tempAmount));
                }
            }
        }

        //+1 buttons
        for(int i = 0; i < productCatalog.size(); i++){
            if(e.getSource() == plusButtonList.get(i)){
                int tempAmount = Integer.parseInt(amountLabelList.get(i).getText());
                tempAmount++;
                amountLabelList.get(i).setText(Integer.toString(tempAmount));
            }
        }

        //Purchase Button
        if(e.getSource() == confirmPurchase){
            int tempAmount = 0;
            for(int i = 0; i<productCatalog.size(); i++){
                tempAmount+= Integer.parseInt(amountLabelList.get(i).getText());
            }
            if(tempAmount == 0){
                System.out.println("Nothing Purchased");
            }else{
                //Confirm button was pressed, switch to invoice
                for(int i = 0; i<productCatalog.size(); i++){
                    invoiceAmountLabelList.get(i).setText(amountLabelList.get(i).getText());
                    double tempDiscount = 0;
                    if(Integer.parseInt(amountLabelList.get(i).getText()) >= 10){
                        tempDiscount = (0.1 * productCatalog.get(i).getProductPrice() * Integer.parseInt(amountLabelList.get(i).getText()));
                    }
                    invoiceDiscountLabelList.get(i).setText("- $" + Double.toString(roundDouble(tempDiscount)));
                    double tempPrice = productCatalog.get(i).getProductPrice() * Integer.parseInt(amountLabelList.get(i).getText()) - tempDiscount;
                    invoicePriceLabelList.get(i).setText("$" + Double.toString(roundDouble(tempPrice)));
                }
                nameLabel.setText(customerFName.getText() + " " + customerLName.getText());
                String tempDate = new SimpleDateFormat("MM/dd/yyyy").format((Calendar.getInstance().getTime()));
                dateLabel.setText(tempDate);

                //Calculate final invoice
                double tempSubtotal = 0;
                double tempPayFullDisc = 0;
                double tempTax;
                double tempTotal;

                for(int i = 0; i<productCatalog.size(); i++){
                    tempSubtotal+= productCatalog.get(i).getProductPrice() * Double.parseDouble(invoiceAmountLabelList.get(i).getText());
                }
                if(paymentMethodBox.getSelectedIndex() == 0){
                    tempPayFullDisc = tempSubtotal * .05;
                }
                tempTax = (tempSubtotal * 0.06);
                tempTotal = (tempSubtotal - tempPayFullDisc + tempTax);
                subtotalLabel.setText("Subtotal: $" + Double.toString(roundDouble(tempSubtotal)));
                payFullDiscLabel.setText("Pay in Full Discount: - $" + Double.toString(roundDouble(tempPayFullDisc)));
                taxLabel.setText("$Tax: " + Double.toString(roundDouble(tempTax)));
                totalLabel.setText("Total: $" + Double.toString(roundDouble(tempTotal)));

                for(int i = 0; i<customerList.size(); i++){
                    if(customerList.get(i).getFirstName().equals(customerFName.getText())){
                        customerList.get(i).setPrevDate(tempDate);
                        customerList.get(i).setPrevTotal(tempTotal);
                    }
                }

                //Save updated customer information list
                try {
                    saveCustomerList();
                } catch (IOException ex) {
                    System.out.println("Unable to save updated customer information");
                }
                cl.show(container, "invoice");
            }
        }

        //Print receipt button to write invoice to file
        if(e.getSource() == printReceipt){
            try {
                saveInvoiceToFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}