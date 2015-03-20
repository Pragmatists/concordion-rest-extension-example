package com.github.mpi.tdd_rest.application;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloEndpoint {

    public static class GreeetinsJson {
        public GreeetinsJson(String message) {
            this.greeting = message;
        }

        String greeting;
    }

    public static class NameJson {
        String name;
    }

    @RequestMapping(
            method = POST,
            value  = "/hello"
            )
    public GreeetinsJson hello(@RequestBody NameJson json){
        return new GreeetinsJson("Hello, " + json.name + "!");
    }
    
}
