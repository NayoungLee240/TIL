package com.nyoungee.p440;

import java.util.Date;

public class Person {
    String name;
    String birth;
    String phone;

    public Person() {
    }

    public Person(String name, String birth, String phone) {
        this.name = name;
        this.birth = birth;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
