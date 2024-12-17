package com.mohamed.functional.customer;

public record Customer(
        int id,
        String name,
        String contact,
        String paymentMethod
) {

    public Customer updateCustomerInformation(String newName, String newContact, String newPaymentMethod) {
        return new Customer(id, newName, newContact, newPaymentMethod);
    }
}
