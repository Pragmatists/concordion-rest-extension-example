package com.github.mpi.tdd_rest.application.invoice;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import rest.fixtures.RestFixture;

import com.github.mpi.Application;
import com.github.mpi.support.RestRunner;
import com.github.mpi.tdd_rest.domain.BillingDetails;
import com.github.mpi.tdd_rest.domain.Invoice;
import com.github.mpi.tdd_rest.domain.InvoiceRepository;
import com.github.mpi.tdd_rest.domain.Money;
import com.github.mpi.tdd_rest.domain.Tax;

@RunWith(RestRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@IntegrationTest
@WebAppConfiguration
public class InvoiceFixture extends RestFixture {

    @Autowired
    private InvoiceRepository repository;
    
    @Override
    public void clear() {
        super.clear();
        repository.clear();
    }
    
    public Invoice createInvoice(String invoiceNumber){
        Invoice invoice = new Invoice(invoiceNumber);
        repository.store(invoice);
        return invoice;
    }
    
    public void billTo(Invoice invoice, String name, String addressLine1, String addressLine2){
        invoice.billFor(new BillingDetails(name, addressLine1, addressLine2));
    }
    
    public void addLineItem(Invoice invoice, String product, Integer quantity, String price, String tax){
        invoice.addLineItem(quantity, product, Money.eur(price)).applyTax(Tax.of(tax));
    }
}
