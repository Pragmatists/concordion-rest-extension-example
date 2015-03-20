package com.github.mpi.tdd_rest.application;

import static com.github.mpi.tdd_rest.domain.Money.eur;
import static com.github.mpi.tdd_rest.domain.Tax.of;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.mpi.Application;
import com.github.mpi.tdd_rest.domain.BillingDetails;
import com.github.mpi.tdd_rest.domain.Invoice;
import com.github.mpi.tdd_rest.domain.InvoiceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@IntegrationTest
@WebAppConfiguration
public class InvoiceControllerSeleniumTest {

    @Autowired
    private InvoiceRepository invoiceRepository;
    
    private WebDriver driver;

    @Before
    public void setUp() {
        
//        driver = new HtmlUnitDriver(true);
        driver = new FirefoxDriver(new FirefoxProfile());
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }
    
    @After
    public void tearDown(){
        driver.close();
    }
    
    public String path(String page){
        return "http://localhost:8080" + page;
    }
    
    @Test
    public void should_return_details_of_invoice_by_invoice_number() throws Exception {

        // given:
        Invoice invoice = new Invoice("INV.2015.01");
        invoice.billFor(new BillingDetails("Sherlock Holmes", "London", "Baker Street 221b"));
        invoice.addLineItem("Cap", eur("12.49")).applyTax(of("23%"));
        invoice.addLineItem(2, "Pipe", eur("5.99")).applyTax(of("23%"));
        invoiceRepository.store(invoice);
        
        // when:
        driver.get(path("/client/invoices.html"));
        driver.findElement(By.linkText("INV.2015.01")).click();
        
        // then:
        assertThat(isVisible(".invoice")).isTrue();
        assertThat(textOf(".invoice .invoice-number")).isEqualTo("INV.2015.01");
        assertThat(textOf(".invoice .billing-details .contact")).isEqualTo("Sherlock Holmes");
        assertThat(textOf(".invoice .billing-details .addressLine1")).isEqualTo("London");
        assertThat(textOf(".invoice .billing-details .addressLine2")).isEqualTo("Baker Street 221b");
        assertThat(textOf(".invoice .item:nth-child(1) .product")).isEqualTo("Cap");
        assertThat(textOf(".invoice .item:nth-child(1) .qty")).isEqualTo("1");
        assertThat(textOf(".invoice .item:nth-child(1) .price")).isEqualTo("12.49 EUR");
        assertThat(textOf(".invoice .item:nth-child(1) .tax")).isEqualTo("23%");
        assertThat(textOf(".invoice .item:nth-child(2) .product")).isEqualTo("Pipe");
        assertThat(textOf(".invoice .item:nth-child(2) .qty")).isEqualTo("2");
        assertThat(textOf(".invoice .item:nth-child(2) .price")).isEqualTo("5.99 EUR");
        assertThat(textOf(".invoice .item:nth-child(2) .tax")).isEqualTo("23%");
    }

    private boolean isVisible(String css) {
        return driver.findElement(By.cssSelector(css)).isDisplayed();
    }

    private String textOf(String css) {
        return driver.findElement(By.cssSelector(css)).getText();
    }

}
