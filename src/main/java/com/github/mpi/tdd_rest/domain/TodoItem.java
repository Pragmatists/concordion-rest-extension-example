package com.github.mpi.tdd_rest.domain;

public class TodoItem {

    private final TaskID taskID;
    private final String description;
    private final int priority;
    private boolean completed;
    
    public TodoItem(TaskID taskID, String description, int priority) {
        this.taskID = taskID;
        this.description = description;
        this.priority = priority;
    }
    
    public void complete(){
        completed = true;
    }

    public String description() {
        return description;
    }

    public TaskID taskID() {
        return taskID;
    }

    public int priority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }
    
}
