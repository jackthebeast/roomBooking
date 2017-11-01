package it.jgrassi.roombooking.model;

/**
 * Created by jacop on 01/11/2017.
 */

public class Booking {
    public String date;

    public String date_start;

    public String date_end;

    public String title;

    public String description;

    public String room;

    public Booking(long date, long date_start, long date_end, String title, String description, String room) {
        this.date = Long.toString(date);
        this.date_start = Long.toString(date_start);
        this.date_end = Long.toString(date_end);
        this.title = title;
        this.description = description;
        this.room = room;
    }
}
