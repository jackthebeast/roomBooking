package it.jgrassi.roombooking.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.Observable;
import java.util.Observer;

import it.jgrassi.roombooking.R;
import it.jgrassi.roombooking.databinding.RoomListActivityBinding;
import it.jgrassi.roombooking.viewmodel.RoomListViewModel;

/**
 * Created by jacop on 29/10/2017.
 */

public class RoomListActivity extends AppCompatActivity implements Observer{

    private static String PARAM_DAY = "PARAM_DAY";
    private RoomListActivityBinding binding;
    private RoomListViewModel viewModel;
    private LocalDate date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        date = (LocalDate) getIntent().getSerializableExtra(PARAM_DAY);
        if(date==null)
            date = new LocalDate();

        initDataBinding();
        setupObserver(viewModel);
        setupRoomListView(binding.listRoom);
    }

    public void setupObserver(Observable observable) {
        observable.addObserver(this);
    }

    private void setupRoomListView(RecyclerView list) {
        RoomsAdapter adapter = new RoomsAdapter(date);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new RoomsDivider(this));
    }

    private void initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.room_list_activity);
        viewModel = new RoomListViewModel(this, date);
        binding.setMainViewModel(viewModel);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof RoomListViewModel) {
            RoomsAdapter adapter = (RoomsAdapter) binding.listRoom.getAdapter();
            RoomListViewModel viewmodel = (RoomListViewModel) observable;
            adapter.setRoomList(viewmodel.getRoomList());
        }
    }


    public static void launchDetail(Context context, LocalDate day) {
        Intent intent = new Intent(context, RoomListActivity.class);
        intent.putExtra(PARAM_DAY, day);
        ((Activity)context).finish();
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(0,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.reset();
    }

}
