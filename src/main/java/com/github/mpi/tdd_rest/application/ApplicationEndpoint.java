package com.github.mpi.tdd_rest.application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationEndpoint {

    class LinksJson {
        String invoices = "/invoices";
    }
    
    class RootJson {
        LinksJson _links = new LinksJson();
    }

    @RequestMapping(value="/", method=RequestMethod.GET)
    public RootJson root(){
        return new RootJson();
    }
}
