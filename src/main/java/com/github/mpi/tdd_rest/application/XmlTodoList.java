package com.github.mpi.tdd_rest.application;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.github.mpi.tdd_rest.domain.TodoList;
import com.itextpdf.text.DocumentException;

public class XmlTodoList {

    private TodoList list;

    public XmlTodoList(TodoList list) {
        this.list = list;
    }
    
    public void printXmlTo(OutputStream output) throws DocumentException, IOException {
        
        InputStream xml = IOUtils.toInputStream(toXml());
        IOUtils.copy(xml, output);
    }

    private String toXml() {
        
        StringBuilder builder = new StringBuilder();
        builder.append("<todo-list>");

        list.forEach(item -> {
            builder.append(format("<todo-item task-id=\"%s\"", item.taskID()));
            builder.append(format(" completed=\"%s\">", item.isCompleted() ? "true": "false"));
            builder.append(format("<priority>%d</priority>", item.priority()));
            builder.append(format("<description>%s</description>", item.description()));
            builder.append("</todo-item>");
        });
        builder.append("</todo-list>");
        
        return builder.toString();
    }
    
}
