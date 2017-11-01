package it.jgrassi.roombooking.model;

/**
 * Created by jacop on 01/11/2017.
 */

public class BookRequest {

    public Booking booking;

    public Person[] passes;

    public BookRequest(Booking booking, Person[] passes) {
        this.booking = booking;
        this.passes = passes;
    }
}

