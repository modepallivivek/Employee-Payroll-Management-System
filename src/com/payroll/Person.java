package com.payroll;

/**
 * Base class for a person.
 *
 * Stores only the data common to every person in the system (name and
 * phone number). Employee adds the employment-specific fields and the
 * salary calculation. All fields are private and are accessed only
 * through getters and setters, which demonstrates encapsulation.
 */
public class Person {
    private String name;
    private String phone;

    public Person(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
