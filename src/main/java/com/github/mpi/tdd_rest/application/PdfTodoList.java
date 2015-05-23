package com.github.mpi.tdd_rest.application;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.github.mpi.tdd_rest.domain.TodoList;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PdfTodoList {

    private TodoList list;

    public PdfTodoList(TodoList list) {
        this.list = list;
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
        
        StringBuilder builder = new StringBuilder(format("<center><h1>To-Do List:</h1></center><hr/>"));
        builder.append("<ul>");

        list.forEach(item -> {
            int priority = 100 + (item.priority()*25);
            builder.append(format("<li style='text-decoration: %s; font-size: %d%%; line-height: 200%%;'>", item.isCompleted() ? "line-through" : "initial", priority));
            builder.append(format("%s", item.description()));
            builder.append("</li>");
        });
        builder.append("</ul>");
        
        return builder.toString();
    }
}
