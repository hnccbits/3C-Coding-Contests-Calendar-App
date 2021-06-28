package com.noobsever.codingcontests.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noobsever.codingcontests.Adapters.PlatformsListAdapter;
import com.noobsever.codingcontests.Models.ContestObject;
import com.noobsever.codingcontests.R;
import com.noobsever.codingcontests.Utils.Constants;
import com.noobsever.codingcontests.Utils.Methods;
import com.noobsever.codingcontests.ViewModel.ApiViewModel;
import com.noobsever.codingcontests.ViewModel.RoomViewModel;
//import com.schibsted.spain.parallaxlayerlayout.ParallaxLayerLayout;
//import com.schibsted.spain.parallaxlayerlayout.SensorTranslationUpdater;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContestFragment extends Fragment {

    ArrayList<String> mTabItemList;

    ApiViewModel apiViewModel;
    RoomViewModel mRoomViewModel;
    RecyclerView titlesRecycler;
    /** do not comment  this*/
    PlatformsListAdapter platformsListAdapter;
//    ParallaxLayerLayout mParallaxLayout;
//    SensorTranslationUpdater sensorTranslationUpdater;
    /**important for UI*/

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_layout_one, container, false);
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("OnFragment>>>", "onActivityCreated: "+" ContestFragment" );
//        mParallaxLayout = getActivity().findViewById(R.id.ActivityOneParallax);
//        sensorTranslationUpdater = new SensorTranslationUpdater(getContext());
//        mParallaxLayout.setTranslationUpdater(sensorTranslationUpdater);

        // RoomDB data saving start -------------------------------------------------------------------
        mRoomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);

//        // Testing Api Start -------------------------------------------------------------------------

        /**TODO: test API url: https://kontests.net/api/v1/all*/
//        apiViewModel = ViewModelProviders.of(this).get(ApiViewModel.class);
        apiViewModel=new ViewModelProvider(this).get(ApiViewModel.class);
        apiViewModel.init();
        apiViewModel.getAllContests().observe(getActivity(), new Observer<List<ContestObject>>() {
            @Override
            public void onChanged(List<ContestObject> contestObjects) {
                mRoomViewModel.deleteAndAddAllTuples(contestObjects);
                //mRoomViewModel.addAllContest(contestObjects);
            }
        });

        apiViewModel.fetchContestFromApi();

        try {
            mTabItemList = (ArrayList<String>) Methods.fetchTabItems( getContext());

        }catch (NullPointerException e) {
            e.printStackTrace();
            // Displays all tabs by Default.
            mTabItemList = new ArrayList<>();
            mTabItemList.add(Constants.CODEFORCES);
            mTabItemList.add(Constants.CODECHEF);
            mTabItemList.add(Constants.HACKERRANK);
            mTabItemList.add(Constants.HACKEREARTH);
            mTabItemList.add(Constants.SPOJ);
            mTabItemList.add(Constants.ATCODER);
            mTabItemList.add(Constants.LEETCODE);
            mTabItemList.add(Constants.GOOGLE);

            // Bug fixed below : When App launches for first time Setting checkboxes remaining unmarked.
            Methods.saveTabItems(getContext(),mTabItemList);
        }

        titlesRecycler = getActivity().findViewById(R.id.titles_recycler_view);
        titlesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        platformsListAdapter = new PlatformsListAdapter(getContext(),mTabItemList);
        titlesRecycler.setAdapter(platformsListAdapter);

        saveActivity();
    }
    public void saveActivity() {
        Methods.setPreferences(getContext(), Constants.LAYOUT_SWITCH_KEY, Constants.CURRENT_ACTIVITY, 1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_layout:
                startActivity(new Intent(getContext(),LayoutTwoActivity.class));
                getActivity().finishAffinity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
//        sensorTranslationUpdater.registerSensorManager();
    }

    @Override
    public void onPause() {
        super.onPause();
//        sensorTranslationUpdater.unregisterSensorManager();
    }
}
