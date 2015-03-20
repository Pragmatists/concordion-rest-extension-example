package com.github.mpi.tdd_rest.application;

import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mpi.tdd_rest.application.InvoiceController.BillingDetailsJson;
import com.github.mpi.tdd_rest.application.InvoiceController.InvoiceItemJson;
import com.github.mpi.tdd_rest.application.InvoiceController.InvoiceJson;
import com.github.mpi.tdd_rest.domain.BillingDetails;
import com.github.mpi.tdd_rest.domain.Invoice;
import com.github.mpi.tdd_rest.domain.InvoiceRepository;
import com.github.mpi.tdd_rest.domain.Money;
import com.github.mpi.tdd_rest.domain.Tax;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceControllerUnitTest {

    @Mock
    private InvoiceRepository repository;
    
    private InvoiceController controller;

    @Before
    public void setUp() {

        controller = new InvoiceController(repository);
    }
    
    @Test
    public void should_return_details_of_invoice_by_invoice_number() throws Exception {

        // given:
        Invoice invoice = new Invoice("INV.2015.01");
        invoice.billFor(new BillingDetails("Sherlock Holmes", "London", "Baker Street 221b"));
        invoice.addLineItem("Cap", Money.eur("12.49")).applyTax(Tax.of("23%"));
        invoice.addLineItem(2, "Pipe", Money.eur("5.99")).applyTax(Tax.of("23%"));

        when(repository.load("INV.2015.01")).thenReturn(Optional.of(invoice));
        
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
