package com.rflpazini.contactlist.model;

import java.io.Serializable;

/**
 * Created by rflpazini on 6/1/16.
 */
public class User implements Serializable{

    private String name;
    private String phone;
    private String email;

    public User() {

    }

    public User(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
