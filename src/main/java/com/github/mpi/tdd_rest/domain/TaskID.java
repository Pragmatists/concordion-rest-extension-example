package com.github.mpi.tdd_rest.domain;

import java.util.Objects;

public class TaskID {

    private final int id;

    public TaskID(int id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if(!(obj instanceof TaskID)){
            return false;
        }
        
        TaskID other = (TaskID) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.format("%d", id);
    }

}
