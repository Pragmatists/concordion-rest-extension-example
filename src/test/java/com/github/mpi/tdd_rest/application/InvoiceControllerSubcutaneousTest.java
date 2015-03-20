package com.github.mpi.tdd_rest.application;

import static com.github.mpi.tdd_rest.domain.Money.eur;
import static com.github.mpi.tdd_rest.domain.Tax.of;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.mpi.Application;
import com.github.mpi.tdd_rest.application.InvoiceController.BillingDetailsJson;
import com.github.mpi.tdd_rest.application.InvoiceController.InvoiceItemJson;
import com.github.mpi.tdd_rest.application.InvoiceController.InvoiceJson;
import com.github.mpi.tdd_rest.domain.BillingDetails;
import com.github.mpi.tdd_rest.domain.Invoice;
import com.github.mpi.tdd_rest.domain.InvoiceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@IntegrationTest
@WebAppConfiguration
public class InvoiceControllerSubcutaneousTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceController controller;
    
    @Test
    public void should_return_details_of_invoice_by_invoice_number() throws Exception {

        // given:
        Invoice invoice = new Invoice("INV.2015.01");
        invoice.billFor(new BillingDetails("Sherlock Holmes", "London", "Baker Street 221b"));
        invoice.addLineItem("Cap", eur("12.49")).applyTax(of("23%"));
        invoice.addLineItem(2, "Pipe", eur("5.99")).applyTax(of("23%"));

        invoiceRepository.store(invoice);

        // when:
        InvoiceJson actualInvoice = controller.findInvoice("INV.2015.01");
        
        // then:
        InvoiceJson expectedInvoice = new InvoiceJson("INV.2015.01");
        expectedInvoice.billingDetails = new BillingDetailsJson("Sherlock Holmes", "London", "Baker Street 221b");
        expectedInvoice.lineItems.add(new InvoiceItemJson("Cap", 1, "12.49 EUR", "23%"));
        expectedInvoice.lineItems.add(new InvoiceItemJson("Pipe", 2, "5.99 EUR", "23%"));
        
        assertReflectionEquals(expectedInvoice, actualInvoice);
    }
}
