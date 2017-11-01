package it.jgrassi.roombooking.data;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jacop on 29/10/2017.
 */

public class RoomFactory {

    public static final String ROOMS_URL = "getrooms";
    public static final String BOOK_URL = "sendpass";

    public static RoomService create(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(FactoryUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(RoomService.class);
    }
}
