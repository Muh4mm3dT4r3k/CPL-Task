package com.mohamed.functional.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CustomerService {
    private final List<Customer> customers;

    public CustomerService(List<Customer> customers) {
        this.customers = List.copyOf(customers);
    }

    public CustomerService addCustomer(Customer customer) {
        return new CustomerService(
                Stream.concat(customers.stream(), Stream.of(customer)).toList()
        );
    }

    public Optional<Customer> findCustomer(int customerId) {
        Predicate<Customer> customerPredicate = customer -> customer.id() == customerId;
        return customers
                .stream()
                .filter(customerPredicate)
                .findFirst();
    }

    public CustomerService updateCustomerInformation(int customerId, String name, String contact, String paymentMethod) {
        List<Customer> updatedCustomerList = customers
                .stream()
                .map(customer -> customer.id() == customerId
                        ? customer.updateCustomerInformation(name, contact, paymentMethod)
                        : customer)
                .toList();
        return new CustomerService(updatedCustomerList);
    }
}
