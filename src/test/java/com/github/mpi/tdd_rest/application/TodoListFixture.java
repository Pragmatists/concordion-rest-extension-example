package com.github.mpi.tdd_rest.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.mpi.Application;
import com.github.mpi.tdd_rest.domain.TaskID;
import com.github.mpi.tdd_rest.domain.TodoList;

@SpringApplicationConfiguration(classes = {Application.class})
@IntegrationTest
@WebAppConfiguration
public class TodoListFixture extends RestFixture {
    
    @Autowired
    private TodoList list;
    
    public void clearItems(){
        list.clear();
    }
    
    public TaskID newItem(){
        return newItem("To-Do item description", 1, "N");
    }
    
    public TaskID newItem(String description, int priority, String completed){
        
        TaskID taskID = list.newItem(description, priority);
        if("Y".equals(completed)){
            list.complete(taskID);
        }
        
        return taskID;
    }
    
}
