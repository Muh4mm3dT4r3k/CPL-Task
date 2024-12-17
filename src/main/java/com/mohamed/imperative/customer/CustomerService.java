package com.mohamed.imperative.customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private List<Customer> customers;
    
    public CustomerService() {
        this.customers = new ArrayList<>();
    }
    
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
    
    public Customer findCustomer(int customerId) {
        for (Customer customer : customers) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }
    
    public void updateCustomerContact(int customerId, String newContact) {
        Customer customer = findCustomer(customerId);
        if (customer != null) {
            customer.setContact(newContact);
        }
    }
} 