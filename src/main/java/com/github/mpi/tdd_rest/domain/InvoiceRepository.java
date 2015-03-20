package com.github.mpi.tdd_rest.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class InvoiceRepository {

    private List<Invoice> storage = new ArrayList<>();
    
    public void store(Invoice invoice) {
        storage.add(invoice);
    }

    public Optional<Invoice> load(String invoiceNumber) {
        return storage.stream().filter( i -> invoiceNumber.equals(i.getNumber())).findFirst();
    }

    public List<Invoice> loadAll(){
        return storage;
    }

    public void clear() {
        storage.clear();
    }
    
}
