package it.jgrassi.roombooking.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

import it.jgrassi.roombooking.R;
import it.jgrassi.roombooking.databinding.RoomDetailActivityBinding;
import it.jgrassi.roombooking.model.Room;
import it.jgrassi.roombooking.viewmodel.RoomDetailViewModel;

public class RoomDetailActivity extends AppCompatActivity implements Observer {

    private static String PARAM_ROOM = "PARAM_ROOM";
    private Room room;
    private RoomDetailActivityBinding roomDetailActivityBinding;
    private RoomDetailViewModel roomDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentExtra();
        initDataBinding();
        setupObserver(roomDetailViewModel);
    }

    private void getIntentExtra() {
        Room room = (Room) getIntent().getSerializableExtra(PARAM_ROOM);

        this.room = room;
    }

    public void setupObserver(Observable observable) {
        observable.addObserver(this);
    }

    private void initDataBinding(){
        roomDetailActivityBinding = DataBindingUtil.setContentView(this, R.layout.room_detail_activity);
        roomDetailViewModel = new RoomDetailViewModel(this, room);
        roomDetailActivityBinding.setDetailViewModel(roomDetailViewModel);
    }

    public static void launchDetail(Context context, Room room, Pair<View, String>... sharedElements) {
        Intent intent = new Intent(context, RoomDetailActivity.class);
        intent.putExtra(PARAM_ROOM, room);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, sharedElements );

        context.startActivity(intent, options.toBundle());
    }

    @Override public void update(Observable observable, Object data) {
        if (observable instanceof RoomDetailViewModel) {
//            RoomsAdapter adapter = (RoomsAdapter) roomDetailActivityBinding.listRoom.getAdapter();
//            RoomDetailViewModel viewmodel = (RoomDetailViewModel) observable;
//            adapter.setRoomList(viewmodel.getBookList());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        roomDetailViewModel.reset();
    }
}
