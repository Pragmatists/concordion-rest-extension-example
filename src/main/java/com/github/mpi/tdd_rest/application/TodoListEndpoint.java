package com.github.mpi.tdd_rest.application;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.mpi.tdd_rest.domain.TaskID;
import com.github.mpi.tdd_rest.domain.TodoItem;
import com.github.mpi.tdd_rest.domain.TodoList;
import com.github.mpi.tdd_rest.domain.TodoList.TodoItemDoesNotExistException;
import com.itextpdf.text.DocumentException;

@RestController
public class TodoListEndpoint {

    @Autowired
    private TodoList list;
    
    @RequestMapping(
            method = POST,
            value  = "/tasks",
            consumes = "application/json")
    public ResponseEntity<Void> post(@RequestBody NewTodoItemJson json){

        TaskID taskID = list.newItem(json.description, json.priority);
        
        return created("/tasks/%s", taskID);
    }

    private ResponseEntity<Void> created(String location, Object... params) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Location", String.format(location, params));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(
            method = POST,
            value  = "/tasks/{task_id}/complete")
    public void complete(@PathVariable("task_id") int taskID){

        list.complete(new TaskID(taskID));
    }

    @RequestMapping(
            method = DELETE,
            value  = "/tasks/{task_id}")
    public ResponseEntity<Void> delete(@PathVariable("task_id") int taskID){
        
        list.remove(new TaskID(taskID));
        
        return noContent();
    }

    private ResponseEntity<Void> noContent() {
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(
            method = GET,
            value  = "/tasks/{task_id}")
    public TodoItemJson get(@PathVariable("task_id") int taskID){
        
        TodoItem task = list.find(new TaskID(taskID));
        return new TodoItemJson(task);
    }
    
    @RequestMapping(
            method = GET,
            value  = "/tasks")
    public TodoItemListJson list(){
        return new TodoItemListJson(list);
    }

    @RequestMapping(
            method = GET,
            value  = "/tasks",
            produces = "application/pdf")
    public void listAsPdf(HttpServletResponse response) throws DocumentException, IOException {
        response.setHeader("Content-Type", "application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=todo-list.pdf");
        new PdfTodoList(list).printPdfTo(response.getOutputStream());
    }

    @RequestMapping(
            method = GET,
            value  = "/tasks",
            produces = "application/xml")
    public void listAsXml(HttpServletResponse response) throws DocumentException, IOException {
        //response.setHeader("Content-Type", "application/xml");
        new XmlTodoList(list).printXmlTo(response.getOutputStream());
    }
    
    @ExceptionHandler
    public ResponseEntity<ErrorJson> handle(TodoItemDoesNotExistException error){
        return notFound(new ErrorJson(error));
    }

    private ResponseEntity<ErrorJson> notFound(ErrorJson json) {
        return new ResponseEntity<>(json, HttpStatus.NOT_FOUND);
    }
    
    static class ErrorJson {

        String code;
        String description;
        
        ErrorJson(TodoItemDoesNotExistException error) {
            code = error.getClass().getSimpleName();
            description = error.getMessage();
        }
    }
    
    @JsonPropertyOrder({"task_id", "completed", "description", "priority"})
    static class TodoItemJson {
        
        @JsonProperty("task_id")
        int taskID;
        String description;
        boolean completed;
        int priority;
        
        public TodoItemJson(TodoItem task) {
            this.taskID = Integer.valueOf(task.taskID().toString());
            this.description = task.description();
            this.completed = task.isCompleted();
            this.priority = task.priority();
        }
    }
    
    static class TodoItemListJson {
        
        List<TodoItemJson> tasks = new ArrayList<>();

        public TodoItemListJson(TodoList list) {
            list.forEach(task -> tasks.add(new TodoItemJson(task)));
        }
    }

    static class NewTodoItemJson {
        String description;
        int priority;
    }
}
