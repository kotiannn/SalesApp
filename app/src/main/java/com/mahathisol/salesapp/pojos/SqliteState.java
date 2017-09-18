package com.mahathisol.salesapp.pojos;

/**
 * Created by HP on 08/03/2017.
 */

public class SqliteState {

    String id; // change to string
    String name;

    public SqliteState(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public SqliteState(String name) {
        this.name = name;
    }
    public SqliteState() {
        // TODO Auto-generated constructor stub
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
