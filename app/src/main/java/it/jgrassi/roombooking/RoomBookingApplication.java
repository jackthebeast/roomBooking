package it.jgrassi.roombooking;

import android.app.Application;
import android.content.Context;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import it.jgrassi.roombooking.data.RoomFactory;
import it.jgrassi.roombooking.data.RoomService;


/**
 * Created by jacop on 19/10/2017.
 */

public class RoomBookingApplication extends Application{

    private Scheduler scheduler;
    private RoomService roomService;

    public static RoomBookingApplication getApp(Context context){
        return (RoomBookingApplication) context.getApplicationContext();
    }


    public static RoomBookingApplication createApp(Context context) {
        return RoomBookingApplication.getApp(context);
    }
    public Scheduler subscribeScheduler(){
        if(scheduler == null)
            scheduler = Schedulers.io();
        return scheduler;
    }

    public RoomService getRoomService(){
        if(roomService == null){
            roomService = RoomFactory.create();
        }
        return roomService;
    }

}
