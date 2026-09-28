package com.srnjak.utils.tostring.model;

/**
 * Nested object used to verify recursion behavior.
 */
public class Address {

    private final String street;
    private final String city;

    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }
}
