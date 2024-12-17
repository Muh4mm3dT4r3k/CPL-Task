package com.mohamed.functional.customer;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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

    private <T> Function<Customer, T> withCustomerId(int customerId, Function<Customer, T> operation) {
        return customer -> customer.id() == customerId ? operation.apply(customer) : null;
    }

    public CustomerService updateCustomerInformation(int customerId, String name, String contact, String paymentMethod) {
        Function<Customer, Customer> updateOperation = 
            customer -> customer.updateCustomerInformation(name, contact, paymentMethod);
            
        List<Customer> updatedCustomerList = customers
                .stream()
                .map(customer -> Optional.ofNullable(withCustomerId(customerId, updateOperation).apply(customer))
                        .orElse(customer))
                .toList();
        return new CustomerService(updatedCustomerList);
    }

    public List<Customer> getAllCustomers() {
        return List.copyOf(customers);
    }
}
