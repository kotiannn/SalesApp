package com.mahathisol.salesapp.pojos;

/**
 * Created by Belal on 1/27/2017.
 */

public class Name {
    private String name;
    private int status;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Name(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }
}
