package com.example;

import java.util.*;

/**
 * some fo the constructors/methods/fields have their modifier set to
 * private/public even though they go against the rules of good encapsulation or
 * do not make sense. The reason behind this decision was to be able to create a
 * more thorough testing process.
 */
public class Person{

    private String name, time, domain, city, town;
    private int age;
    private String email;
    public String car = "", house = "tt";

    /*
     * for testing
     */
    private final int x = 2, id = 10, t = 5, p = 12;
    // private static int id = 10;

    /**
     * for testing
     */
    public Person      () {

    }

    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    private String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Person [age=" + age + ", email=" + email + ", name=" + name + "]";
    }

    public Map<String, String> newMethod() {

        Map<String, String> map = new HashMap<>();

        return map;
    }
}

