package it.jgrassi.roombooking.model;

import java.io.Serializable;

/**
 * Created by jacop on 29/10/2017.
 */

public class Room implements Serializable{

    public String name;

    public String location;

    public String[] equipment;

    public String size;

    public int capacity;

    public String[] avail;

    public String[] images;
}
