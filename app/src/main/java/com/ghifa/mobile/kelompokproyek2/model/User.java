package com.ghifa.mobile.kelompokproyek2.model;

/**
 * Created by mobile-pc on 5/10/17.
 */

public class User {

    public String name;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

}