import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class Product {
    private String productName;
    private String productCode;
    private double productPrice;
    private String productDescription;
    private ImageIcon productIcon;

    public Product(String productName, String productCode, double productPrice, String productDescription, ImageIcon productIcon) {
        this.productName = productName;
        this.productCode = productCode;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productIcon = productIcon;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public ImageIcon getProductIcon() {
        return productIcon;
    }
}

public class InvoiceGUI extends JFrame {
    private JLabel customerNameLabel, customerIDLabel, productListLabel;
    private JTextField customerNameField, customerIDField;
    private JTextArea productListArea;
    private JButton calculateButton, saveButton;

    private Product[] products;
    private ArrayList<String> invoiceDetails;

    public InvoiceGUI() {
        setTitle("Invoice Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        customerNameLabel = new JLabel("Customer Name:");
        customerIDLabel = new JLabel("Customer ID:");
        productListLabel = new JLabel("Products List:");

        customerNameField = new JTextField(20);
        customerIDField = new JTextField(10);

        productListArea = new JTextArea(15, 40);
        productListArea.setEditable(false);

        calculateButton = new JButton("Calculate Invoice");
        saveButton = new JButton("Save Invoice");

        panel.add(customerNameLabel);
        panel.add(customerNameField);
        panel.add(customerIDLabel);
        panel.add(customerIDField);
        panel.add(productListLabel);
        panel.add(new JScrollPane(productListArea));
        panel.add(calculateButton);
        panel.add(saveButton);

        add(panel);

        loadProductsFromFile();
        invoiceDetails = new ArrayList<>();

        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateInvoice();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveInvoice();
            }
        });

        setVisible(true);
    }

    private void loadProductsFromFile() {
        // Read products from an external file and populate the 'products' array
        // Placeholder data for demonstration
        products = new Product[]{
                new Product("Product1", "P001", 10.0, "Description 1", new ImageIcon("icon1.png")),
                new Product("Product2", "P002", 15.0, "Description 2", new ImageIcon("icon2.png")),
                // Add other products similarly
        };
    }

    private void calculateInvoice() {
        String customerName = customerNameField.getText();
        String customerID = customerIDField.getText();

        double total = 0;
        double totalWithTax = 0;

        invoiceDetails.clear();
        productListArea.setText("");

        invoiceDetails.add("Customer Name: " + customerName);
        invoiceDetails.add("Customer ID: " + customerID);
        invoiceDetails.add("Invoice Date: " + getCurrentDateTime());

        for (Product product : products) {
            // Simulated quantities (for demonstration)
            int quantity = 2;

            double price = product.getProductPrice() * quantity;
            if (quantity >= 10) {
                price *= 0.9;
            }

            total += price;

            String productDetails = String.format("%s (%s) - Quantity: %d - Price: $%.2f",
                    product.getProductName(), product.getProductCode(), quantity, price);
            invoiceDetails.add(productDetails);
        }

        totalWithTax = total * 1.06;

        String paymentMethod = JOptionPane.showInputDialog(this, "Enter Payment Method (Full / Installments):");
        if (paymentMethod != null && paymentMethod.equalsIgnoreCase("full")) {
            double discount = totalWithTax * 0.05;
            totalWithTax -= discount;
            invoiceDetails.add("Payment Method: Full Payment");
            invoiceDetails.add(String.format("Discount Applied (-5%%): $%.2f", discount));
        } else {
            invoiceDetails.add("Payment Method: Installments");
        }

        invoiceDetails.add(String.format("Total (with 6%% Tax): $%.2f", totalWithTax));
        displayInvoiceDetails();
    }

    private void saveInvoice() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("invoice.txt"))) {
            for (String detail : invoiceDetails) {
                writer.println(detail);
            }
            JOptionPane.showMessageDialog(this, "Invoice saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving invoice: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayInvoiceDetails() {
        for (String detail : invoiceDetails) {
            productListArea.append(detail + "\n");
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new InvoiceGUI();
            }
        });
    }
}