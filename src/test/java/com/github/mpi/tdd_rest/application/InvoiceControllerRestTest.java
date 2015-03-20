package com.github.mpi.tdd_rest.application;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.mpi.Application;
import com.github.mpi.tdd_rest.domain.BillingDetails;
import com.github.mpi.tdd_rest.domain.Invoice;
import com.github.mpi.tdd_rest.domain.InvoiceRepository;
import com.github.mpi.tdd_rest.domain.Money;
import com.github.mpi.tdd_rest.domain.Tax;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@IntegrationTest
@WebAppConfiguration
public class InvoiceControllerRestTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void should_return_details_of_invoice_by_invoice_number() {

        // given:
        Invoice invoice = new Invoice("INV.2015.01");
        invoice.billFor(new BillingDetails("Sherlock Holmes", "London", "Baker Street 221b"));
        invoice.addLineItem("Cap", Money.eur("12.49")).applyTax(Tax.of("23%"));
        invoice.addLineItem(2, "Pipe", Money.eur("5.99")).applyTax(Tax.of("23%"));
        invoiceRepository.store(invoice);

        when().
            get("/invoices/INV.2015.01").
        then().         
            statusCode(200).
            body(equalTo(json("{"
                    + "'invoiceNumber':'INV.2015.01',"
                    + "'billingDetails':{"
                        + "'contact':'Sherlock Holmes',"
                        + "'addressLine1':'London',"
                        + "'addressLine2':'Baker Street 221b'},"
                    + "'lineItems':["
                        + "{'product':'Cap','quantity':1,'price':'12.49 EUR','tax':'23%'},"
                        + "{'product':'Pipe','quantity':2,'price':'5.99 EUR','tax':'23%'}"
                    + "]}")));
    }
    
    @Test
    public void should_return_details_of_invoice_by_invoice_number__json_path_assert() {
        
        // given:
        Invoice invoice = new Invoice("INV.2015.01");
        invoice.billFor(new BillingDetails("Sherlock Holmes", "London", "Baker Street 221b"));
        invoice.addLineItem("Cap", Money.eur("12.49")).applyTax(Tax.of("23%"));
        invoice.addLineItem(2, "Pipe", Money.eur("5.99")).applyTax(Tax.of("23%"));
        invoiceRepository.store(invoice);
        
        when().
            get("/invoices/INV.2015.01").
        then().         
            statusCode(200).
            body("invoiceNumber", equalTo("INV.2015.01")).
            body("billingDetails.contact", equalTo("Sherlock Holmes")).
            body("billingDetails.addressLine1", equalTo("London")).
            body("billingDetails.addressLine2", equalTo("Baker Street 221b")).
            body("lineItems[0].product", equalTo("Cap")).
            body("lineItems[0].quantity", equalTo(1)).
            body("lineItems[0].price", equalTo("12.49 EUR")).
            body("lineItems[0].tax", equalTo("23%")).
            body("lineItems[1].product", equalTo("Pipe")).
            body("lineItems[1].quantity", equalTo(2)).
            body("lineItems[1].price", equalTo("5.99 EUR")).
            body("lineItems[1].tax", equalTo("23%"));
    }

    private String json(String json){
        return json.replaceAll("'", "\"");
    }    
}
