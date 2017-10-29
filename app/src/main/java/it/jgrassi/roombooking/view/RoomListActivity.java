package it.jgrassi.roombooking.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Observable;
import java.util.Observer;

import it.jgrassi.roombooking.R;
import it.jgrassi.roombooking.databinding.RoomListActivityBinding;
import it.jgrassi.roombooking.viewmodel.RoomListViewModel;

/**
 * Created by jacop on 29/10/2017.
 */

public class RoomListActivity extends AppCompatActivity implements Observer {

    private RoomListActivityBinding binding;
    private RoomListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initDataBinding();
        setupObserver(viewModel);
        setupRoomListView(binding.listRoom);
    }

    public void setupObserver(Observable observable){
        observable.addObserver(this);
    }

    private void setupRoomListView(RecyclerView list){
        RoomsAdapter adapter = new RoomsAdapter();
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new RoomsDivider(this));
    }

    private void initDataBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.room_list_activity);
        viewModel = new RoomListViewModel(this);
        binding.setMainViewModel(viewModel);
    }

    @Override
    public void update(Observable observable, Object data){
        if(observable instanceof RoomListViewModel){
            RoomsAdapter adapter = (RoomsAdapter) binding.listRoom.getAdapter();
            RoomListViewModel viewmodel = (RoomListViewModel) observable;
            adapter.setRoomList(viewmodel.getRoomList());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.reset();
    }

}
