package com.github.mpi;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.mpi.tdd_rest.domain.BillingDetails;
import com.github.mpi.tdd_rest.domain.Invoice;
import com.github.mpi.tdd_rest.domain.InvoiceRepository;
import com.github.mpi.tdd_rest.domain.Money;
import com.github.mpi.tdd_rest.domain.Tax;

@Component
@Profile("demo")
public class Demo {

    @Autowired
    private InvoiceRepository repository;
    
    @PostConstruct
    public void populateWithSampleData(){
        
        Invoice invoice = new Invoice("INV.2015.01");
        invoice.billFor(new BillingDetails("Sherlock Holmes", "London", "Baker Street 221b"));
        invoice.addLineItem("Cap", Money.eur("12.49")).applyTax(Tax.of("23%"));
        invoice.addLineItem(2, "Pipe", Money.eur("5.99")).applyTax(Tax.of("23%"));

        repository.store(invoice);
        
        Invoice invoice2 = new Invoice("INV.2015.02");
        invoice2.billFor(new BillingDetails("Gandalf the Grey", "Middle-earth", "n/a"));
        invoice2.addLineItem("Robe", Money.eur("54.49")).applyTax(Tax.of("23%"));
        invoice2.addLineItem(2, "Magic Stick", Money.eur("1000")).applyTax(Tax.of("8%"));
        
        repository.store(invoice2);
    }
    
}
