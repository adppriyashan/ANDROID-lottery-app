package com.codetek.lottaryapp.Models.DB;

public class User {
    private int id;
    private String name,email;
    private int usertype;

    public User(int id, String name, String email, int usertype) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.usertype = usertype;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getUsertype() {
        return usertype;
    }
}
