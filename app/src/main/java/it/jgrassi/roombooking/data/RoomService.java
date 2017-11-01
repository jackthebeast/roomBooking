package it.jgrassi.roombooking.data;

import java.util.List;

import io.reactivex.Observable;
import it.jgrassi.roombooking.model.BookRequest;
import it.jgrassi.roombooking.model.BookingResult;
import it.jgrassi.roombooking.model.Room;
import it.jgrassi.roombooking.model.RoomsRequest;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by jacop on 29/10/2017.
 */

public interface RoomService {
    @POST
    Observable<List<Room>> fetchRooms(@Url String url, @Body RoomsRequest request);

    @POST
    Observable<BookingResult> book(@Url String url, @Body BookRequest request);
}
