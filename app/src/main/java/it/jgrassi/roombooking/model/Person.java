package it.jgrassi.roombooking.model;

/**
 * Created by jacop on 01/11/2017.
 */

public class Person {

    String name;
    String number;
    String email;

    public Person(String name, String number, String email) {
        this.name = name;
        this.number = number;
        this.email = email;
    }

    @Override
    public String toString(){
        String result = "";
        result += name + " (";
        result += number + ", ";
        result += email + ")";
        return result;
    }
}
