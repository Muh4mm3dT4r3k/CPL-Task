package com.mohamed.imperative.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {
    private int id;
    private String name;
    private String contact;
    private String paymentMethod;
    
    public Customer(int id, String name, String contact, String paymentMethod) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.paymentMethod = paymentMethod;
    }

}