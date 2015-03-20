package com.github.mpi.tdd_rest.application.invoice;


public class AuthorizationFixture extends InvoiceFixture {

    public String getStatus(){
        if(isSuccessful()){
            return "OK";
        }
        if(response().statusCode() == 403){
            return "Forbidden";
        }
        return status();
    }
    
    public void doAsAdmin(){
        // alter security contxet holder
    }

    public void doAsRegular(){        
        // alter security contxet holder
    }

    public void doAsCreator(){        
        // alter security contxet holder
    }
}
