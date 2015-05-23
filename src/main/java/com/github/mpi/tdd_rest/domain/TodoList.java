package com.github.mpi.tdd_rest.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

@Component
public class TodoList {

    public class TodoItemDoesNotExistException extends IllegalArgumentException {

        public TodoItemDoesNotExistException(TaskID taskID) {
            super(String.format("To-Do item with id=%s not found!", taskID));
        }

    }

    private Map<TaskID, TodoItem> tasks = new HashMap<>();
    private int nextID = 1;
    
    public TaskID newItem(String description, int priority){
        
        TaskID taskID = new TaskID(nextID++);
        tasks.put(taskID, new TodoItem(taskID, description, priority));
        return taskID;
    }

    public void complete(TaskID taskID){
        TodoItem task = find(taskID);
        task.complete();
    }
    
    public void remove(TaskID taskID){       
        if(tasks.remove(taskID) == null){
            throw new TodoItemDoesNotExistException(taskID);
        }
    }
    
    public void clear(){
        tasks.clear();
        nextID = 1;
    }

    public TodoItem find(TaskID taskID) {
        
        TodoItem item = tasks.get(taskID);
        
        if(item != null)
            return item;
        
        throw new TodoItemDoesNotExistException(taskID);
    }

    public void forEach(Consumer<TodoItem> c) {
        tasks.forEach((id, task) -> c.accept(task));
    }
}
