package com.github.mpi.tdd_rest.domain;

public class BillingDetails {

    private String contact;
    private String addressLine1;
    private String addressLine2;

    public BillingDetails(String contact, String addressLine1, String addressLine2) {
        this.contact = contact;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
    }

    public String getContact() {
        return contact;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }
    
}
