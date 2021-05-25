package com.noobsever.codingcontests.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.noobsever.codingcontests.Models.ContestObject;
import com.noobsever.codingcontests.R;
import com.noobsever.codingcontests.ViewModel.RoomViewModel;

import java.util.List;

public class RoomActivity extends AppCompatActivity {

    private static final String TAG = "RoomActivity";
    RoomViewModel mRoomViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        testRoomDB();
    }

    private void testRoomDB() {
        mRoomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);

        addDummyContests();
        mRoomViewModel.getAllContests().observe(this, new Observer<List<ContestObject>>() {
            @Override
            public void onChanged(List<ContestObject> contestObjects) {
                if(contestObjects!=null) {
                    Log.i(TAG,"Fetching LiveData");
                    for(int i=0;i<contestObjects.size();i++)
                        Log.i(TAG,contestObjects.get(i).getTitle()+" [Contest updated on LiveData]");
                }
            }
        });


       // testSearchQuery();
    }

//    private void testSearchQuery() {
//        // Valid query
//        String searchContest1 = "AIM ICPC";
//        ContestObject contestObject1 = mRoomViewModel.findContestByPlatform(searchContest1);
//        if(contestObject1!=null)
//            Log.i(TAG, contestObject1.getTitle()+" found");
//        else
//            Log.i(TAG,searchContest1+" not found");
//
//        List<ContestObject> list = mRoomViewModel.getContestByTime("2021-05-25T00:00:00","2021-05-36T00:00:00");
//        for(int i=0;i<list.size();i++) {
//            Log.i(TAG, "testSearchQuery: Found Contests : ["+list.get(i).getTitle()+" ]");
//        }
//    }

    public void addDummyContests() {
        mRoomViewModel.addContest(new ContestObject("AIM ICPC","2021-05-25T00:00:00",
                "2021-05-26T00:00:00","86400","https://www.codechef.com/AIMICPC?itm_campaign=contest_listing","Running","codechef.com"));

        mRoomViewModel.addContest(new ContestObject("Summer Code Challenge","2021-05-25T19:00:00","2021-05-25T21:30:00","9000",
                "https://www.codechef.com/SMCC2021?itm_campaign=contest_listing","Yet To","codechef.com"));

        mRoomViewModel.addContest(new ContestObject("May Lunchtime 2021","2021-05-22T19:30:00","2021-05-29T22:30:00",
                "10800","https://www.codechef.com/LTIME96?itm_campaign=contest_listing","Yet To","codechef.com"));
    }
}