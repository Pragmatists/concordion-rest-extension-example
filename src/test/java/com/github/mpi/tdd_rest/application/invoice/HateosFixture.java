package com.github.mpi.tdd_rest.application.invoice;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.mpi.tdd_rest.domain.Invoice;
import com.github.mpi.tdd_rest.domain.InvoiceRepository;

public class HateosFixture extends InvoiceFixture {

    @Autowired
    private InvoiceRepository repository;
    
    public String followLink(String link){
        
        try {
        
            return response().then().assertThat()
                .statusCode(200)
                .extract().path(link);

        } catch (Exception e) {
            e.printStackTrace();
            return "(link not found)";
        }
    }
    
    public void createInvoices(Integer start, Integer end){
        for(int i=start; i<end; i++){
            repository.store(new Invoice(String.format("INV.2015.%02d", i)));
        }
    }
        
}
