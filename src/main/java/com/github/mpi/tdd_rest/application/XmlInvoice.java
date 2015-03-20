package com.github.mpi.tdd_rest.application;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.github.mpi.tdd_rest.domain.Invoice;
import com.itextpdf.text.DocumentException;

public class XmlInvoice {

    private Invoice invoice;

    public XmlInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
    
    public void printXmlTo(OutputStream output) throws DocumentException, IOException {
        
        InputStream xml = IOUtils.toInputStream(toXml());
        IOUtils.copy(xml, output);
    
    }

    private String toXml() {
        
        StringBuilder builder = new StringBuilder();
        builder.append("<invoice>");
        builder.append(format("<number>%s</number>", invoice.getNumber()));
        builder.append("<billingDetails>");
        builder.append(format("<contact>%s</contact>", invoice.getBillingDetails().getContact()));
        builder.append(format("<addressLine1>%s</addressLine1>", invoice.getBillingDetails().getAddressLine1()));
        builder.append(format("<addressLine2>%s</addressLine2>", invoice.getBillingDetails().getAddressLine2()));
        builder.append("</billingDetails>");
        builder.append("<items>");
        
        invoice.getLineItem().stream().forEach(item -> {
            builder.append("<item>");
            builder.append(format("<product>%s</product>", item.getProduct()));
            builder.append(format("<quantity>%s</quantity>", item.getQuantity()));
            builder.append(format("<price>%s</price>", item.getPrice()));
            builder.append(format("<tax>%s</tax>", item.getTax()));
            builder.append("</item>");
        });
        
        builder.append("</items></invoice>");
        return builder.toString();
    }
    
}
