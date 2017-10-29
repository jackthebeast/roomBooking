package it.jgrassi.roombooking.viewmodel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import it.jgrassi.roombooking.R;
import it.jgrassi.roombooking.RoomBookingApplication;
import it.jgrassi.roombooking.data.FactoryUtils;
import it.jgrassi.roombooking.data.RoomFactory;
import it.jgrassi.roombooking.data.RoomService;
import it.jgrassi.roombooking.model.Room;
import it.jgrassi.roombooking.model.RoomsRequest;

/**
 * Created by jacop on 29/10/2017.
 */

public class RoomListViewModel extends Observable {

    public ObservableInt roomsProgress;
    public ObservableInt roomsRecycler;

    private List<Room> roomList;
    private Context context;
    private CompositeDisposable compositeDisposable;

    public RoomListViewModel(Context context){
        this.context = context;
        this.roomList = new ArrayList<>();
        roomsRecycler = new ObservableInt(View.GONE);
        roomsProgress = new ObservableInt(View.GONE);
        compositeDisposable = new CompositeDisposable();

        initializeViews();
        fetchRoomList();

    }

    private  void initializeViews(){
        roomsRecycler.set(View.GONE);
        roomsProgress.set(View.VISIBLE);
    }

    private void setRoomsData(List<Room> rooms){
        roomList.addAll(rooms);
        setChanged();
        notifyObservers();
    }

    public List<Room> getRoomList(){
        return roomList;
    }

    private void fetchRoomList() {

        RoomBookingApplication application = RoomBookingApplication.createApp(context);
        RoomService service = application.getRoomService();

        RoomsRequest requestBody = new RoomsRequest(RoomsRequest.NOW);

        Disposable disposable = service.fetchRooms(FactoryUtils.BASE_URL + RoomFactory.ROOM_URL, requestBody)
                .subscribeOn(application.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Room>>() {
                    @Override
                    public void accept(List<Room> rooms) throws Exception {
                        setRoomsData(rooms);
                        roomsProgress.set(View.GONE);
                        roomsRecycler.set(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(context, String.format(context.getResources().getString(R.string.loading_error), "Rooms"), Toast.LENGTH_LONG).show();
                        roomsProgress.set(View.GONE);
                        roomsRecycler.set(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }
    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }
    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

}
