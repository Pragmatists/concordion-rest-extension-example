package com.github.mpi.tdd_rest.application;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.mpi.tdd_rest.domain.BillingDetails;
import com.github.mpi.tdd_rest.domain.Invoice;
import com.github.mpi.tdd_rest.domain.Invoice.InvoiceLineItem;
import com.github.mpi.tdd_rest.domain.InvoiceRepository;
import com.itextpdf.text.DocumentException;

@RestController
public class InvoiceController {

    private InvoiceRepository repository;
    
    @Autowired
    public InvoiceController(InvoiceRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(
            value   = "/invoices/{invoiceNumber:.+}",
            method  = GET,
            produces = "application/json"
            )
    public InvoiceJson findInvoice(@PathVariable("invoiceNumber") String invoiceNumber){
    
        Invoice invoice = repository.load(invoiceNumber).orElseThrow(IllegalArgumentException::new);
        return new InvoiceJson(invoice);
    }
    
    static class LinksJson {
        String href;
    }
    
    static class InvoiceJson {
        
        LinksJson _links = new LinksJson();
        String invoiceNumber;
        BillingDetailsJson billingDetails;        
        List<InvoiceItemJson> lineItems = new ArrayList<>();

        InvoiceJson(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
            this._links.href = "/invoices/" + invoiceNumber;
        }
        
        InvoiceJson(Invoice invoice) {
            this(invoice.getNumber());
            billingDetails = new BillingDetailsJson(invoice.getBillingDetails());
            invoice.getLineItem().stream()
                .forEach(lineItem -> lineItems.add(new InvoiceItemJson(lineItem)));
        }

    }
    
    static class BillingDetailsJson {
        
        String contact;
        String addressLine1;
        String addressLine2;
        
        BillingDetailsJson(BillingDetails billingDetails) {
            this(billingDetails.getContact(), billingDetails.getAddressLine1(), billingDetails.getAddressLine2());
        }
        
        BillingDetailsJson(String contact, String addressLine1, String addressLine2) {
            this.contact = contact;
            this.addressLine1 = addressLine1;
            this.addressLine2 = addressLine2;
        }
    }
    
    static class InvoiceItemJson {
        
        String product;
        Integer quantity;
        String price;
        String tax;

        InvoiceItemJson(InvoiceLineItem lineItem) {
            this(lineItem.getProduct(), lineItem.getQuantity(), 
                    lineItem.getPrice().toString(), lineItem.getTax().toString());
        }

        InvoiceItemJson(String product, int quantity, String price, String tax) {
            this.product = product;
            this.quantity = quantity;
            this.price = price;
            this.tax = tax;
        }
    }
    
    @RequestMapping(
            value   = "/invoices",
            method  = GET)
    public List<InvoiceJson> list(){
        return repository.loadAll().stream().map(InvoiceJson::new).collect(Collectors.toList());
    }
    
    @RequestMapping(
            value   = "/invoices/{invoiceNumber:.+}",
            method  = GET,
            produces = "application/pdf"
            )
    public void printInvoiceToPdf(@PathVariable("invoiceNumber") String invoiceNumber, HttpServletResponse response) throws IOException, DocumentException{
    
        response.setContentType("application/pdf");
        
        Invoice invoice = repository.load(invoiceNumber).orElseThrow(IllegalArgumentException::new);
        new PdfInvoice(invoice).printPdfTo(response.getOutputStream());        
    }

    @RequestMapping(
            value   = "/invoices/{invoiceNumber:.+}",
            method  = GET,
            produces = "application/xml"
            )
    public void exportInvoiceToXml(@PathVariable("invoiceNumber") String invoiceNumber, HttpServletResponse response) throws IOException, DocumentException{
    
        response.setContentType("application/xml");

        Invoice invoice = repository.load(invoiceNumber).orElseThrow(IllegalArgumentException::new);
        new XmlInvoice(invoice).printXmlTo(response.getOutputStream());
    }

    @RequestMapping(
            value   = "/invoices/{invoiceNumber:.+}",
            method  = RequestMethod.PUT
            )
    public void put(@PathVariable("invoiceNumber") String invoiceNumber){
    }

    @RequestMapping(
            value   = "/invoices/{invoiceNumber:.+}",
            method  = RequestMethod.DELETE
            )
    public ResponseEntity<Void> delete(@PathVariable("invoiceNumber") String invoiceNumber){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}

