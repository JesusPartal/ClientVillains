package model;

import java.io.Serializable;

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    public String getStats() {
        return "Just a person";
    }
}