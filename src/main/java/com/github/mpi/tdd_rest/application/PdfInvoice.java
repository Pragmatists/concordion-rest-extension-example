package com.github.mpi.tdd_rest.application;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.github.mpi.tdd_rest.domain.Invoice;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PdfInvoice {

    private Invoice invoice;

    public PdfInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
    
    public void printPdfTo(OutputStream output) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, output);
        
        document.open();
        
        InputStream html = IOUtils.toInputStream(toHtml());
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, html);
        
        document.close();
    }

    private String toHtml() {
        
        StringBuilder builder = new StringBuilder(format("<h1>Invoice <b>%s</b></h1><hr/>", invoice.getNumber()));
        builder.append("<div><b>Bill To:</b></div>");
        builder.append(format("<div>%s</div>", invoice.getBillingDetails().getContact()));
        builder.append(format("<div>%s</div>", invoice.getBillingDetails().getAddressLine1()));
        builder.append(format("<div>%s</div>", invoice.getBillingDetails().getAddressLine2()));
        builder.append("<div><hr/></div>");
        builder.append("<div><b>Items:</b><br/></div>");
        
        builder.append("<table cellpadding='5' width='80%' border='2'><tr><th>Product</th><th>Qty</th><th>Price</th><th>Tax</th></tr>");

        invoice.getLineItem().stream().forEach(item -> {
            builder.append("<tr>");
            builder.append(format("<td>%s</td>", item.getProduct()));
            builder.append(format("<td>%s</td>", item.getQuantity()));
            builder.append(format("<td>%s</td>", item.getPrice()));
            builder.append(format("<td>%s</td>", item.getTax()));
            builder.append("</tr>");
        });
        
        builder.append(format("</table>"));
        return builder.toString();
    }
    
}
