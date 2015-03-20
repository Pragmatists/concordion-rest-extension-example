package com.github.mpi.tdd_rest.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class Invoice {

    private String invoiceNumber;
    private BillingDetails billingDetails = new BillingDetails("", "", "");
    private List<InvoiceLineItem> lineItems = new ArrayList<>();

    public String getNumber() {
        return invoiceNumber;
    }
    
    public Invoice(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void billFor(BillingDetails billingDetails) {
        this.billingDetails = billingDetails;
    }

    public InvoiceLineItem addLineItem(String product, Money price) {
        return addLineItem(1, product, price);
    }

    public InvoiceLineItem addLineItem(int quantity, String product, Money price) {
        InvoiceLineItem lineItem = new InvoiceLineItem(quantity, product, price);
        lineItems.add(lineItem);
        return lineItem;
    }
    
    public List<InvoiceLineItem> getLineItem() {
        return lineItems;
    }
    
    public BillingDetails getBillingDetails() {
        return billingDetails;
    }
    
    public class InvoiceLineItem {

        private String product;
        private Money price;
        private int quantity;
        private Tax tax;

        public InvoiceLineItem(int quantity, String product, Money price) {
            this.quantity = quantity;
            this.product = product;
            this.price = price;
        }

        public void applyTax(Tax tax) {
            this.tax = tax;
        }

        public String getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public Money getPrice() {
            return price;
        }

        public Tax getTax() {
            return tax;
        }
    }


}
