package it.jgrassi.roombooking.model;

import java.util.Date;

/**
 * Created by jacop on 29/10/2017.
 */

public class RoomsRequest {

    public static final String NOW = "now";
    public static final String TODAY = "today";

    public String date;

    public RoomsRequest(String date){
        this.date = date;
    }

}
