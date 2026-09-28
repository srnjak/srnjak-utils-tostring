package com.srnjak.utils.tostring.model;

/**
 * Plain object with a nested {@link Address} and an enum property.
 */
public class Person {

    private final String name;
    private final int age;
    private final Address address;
    private final Color favorite;

    public Person(String name, int age, Address address, Color favorite) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.favorite = favorite;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Address getAddress() {
        return address;
    }

    public Color getFavorite() {
        return favorite;
    }
}
