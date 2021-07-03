package com.noobsever.codingcontests.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.noobsever.codingcontests.DrawerActivity;
import com.noobsever.codingcontests.R;
import com.noobsever.codingcontests.Screens.LayoutTwoActivity;
import com.noobsever.codingcontests.Screens.ShowContestCardsActivity;
import com.noobsever.codingcontests.databinding.FragmentHomeBinding;

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

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    ArrayList<String> mTabItemList;

    ApiViewModel apiViewModel;
    RoomViewModel mRoomViewModel;
    RecyclerView titlesRecycler;
    /**
     * do not comment  this
     */
    PlatformsListAdapter platformsListAdapter;
//    ParallaxLayerLayout mParallaxLayout;
//    SensorTranslationUpdater sensorTranslationUpdater;
    /**
     * important for UI
     */

    Context context;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        return root;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("OnFragment>>>", "onActivityCreated: " + " ContestFragment");
//        mParallaxLayout = getActivity().findViewById(R.id.ActivityOneParallax);
//        sensorTranslationUpdater = new SensorTranslationUpdater(context);
//        mParallaxLayout.setTranslationUpdater(sensorTranslationUpdater);

        // RoomDB data saving start -------------------------------------------------------------------
        mRoomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
//
////        // Testing Api Start -------------------------------------------------------------------------
//
//        /**TODO: test API url: https://kontests.net/api/v1/all*/
//        apiViewModel = ViewModelProviders.of(this).get(ApiViewModel.class);
        apiViewModel = new ViewModelProvider(this).get(ApiViewModel.class);
        apiViewModel.init();
        apiViewModel.getAllContests().observe(getActivity(), new Observer<List<ContestObject>>() {
            @Override
            public void onChanged(List<ContestObject> contestObjects) {
                mRoomViewModel.deleteAndAddAllTuples(contestObjects);
                //mRoomViewModel.addAllContest(contestObjects);
            }
        });

        apiViewModel.fetchContestFromApi();
//
//        try {
//            mTabItemList = (ArrayList<String>) Methods.fetchTabItems( context);
//        }catch (NullPointerException e) {
//            e.printStackTrace();
//            // Displays all tabs by Default.
//            mTabItemList = new ArrayList<>();
//            mTabItemList.add(Constants.CODEFORCES);
//            mTabItemList.add(Constants.CODECHEF);
//            mTabItemList.add(Constants.HACKERRANK);
//            mTabItemList.add(Constants.HACKEREARTH);
//            mTabItemList.add(Constants.SPOJ);
//            mTabItemList.add(Constants.ATCODER);
//            mTabItemList.add(Constants.LEETCODE);
//            mTabItemList.add(Constants.GOOGLE);
//
//            // Bug fixed below : When App launches for first time Setting checkboxes remaining unmarked.
//            Methods.saveTabItems(context,mTabItemList);
//        }
        boolean cforces, cchef, hrank, hearth, atcoder, leetcode, google, spoj;
        cforces = Methods.getIntPreferences(context, Constants.CODEFORCES, Constants.CODEFORCES) != 0;
        cchef = Methods.getIntPreferences(context, Constants.CODECHEF, Constants.CODECHEF) != 0;
        hrank = Methods.getIntPreferences(context, Constants.HACKERRANK, Constants.HACKERRANK) != 0;
        hearth = Methods.getIntPreferences(context, Constants.HACKEREARTH, Constants.HACKEREARTH) != 0;
        atcoder = Methods.getIntPreferences(context, Constants.ATCODER, Constants.ATCODER) != 0;
        leetcode = Methods.getIntPreferences(context, Constants.LEETCODE, Constants.LEETCODE) != 0;
        google = Methods.getIntPreferences(context, Constants.GOOGLE, Constants.GOOGLE) != 0;
        spoj = Methods.getIntPreferences(context, Constants.SPOJ, Constants.SPOJ) != 0;
        mTabItemList = new ArrayList<>();
        /* Adding in the list only the user check */
        if (cforces) mTabItemList.add(Constants.CODEFORCES);
        if (cchef) mTabItemList.add(Constants.CODECHEF);
        if (hrank) mTabItemList.add(Constants.HACKERRANK);
        if (hearth) mTabItemList.add(Constants.HACKEREARTH);
        if (spoj) mTabItemList.add(Constants.SPOJ);
        if (atcoder) mTabItemList.add(Constants.ATCODER);
        if (leetcode) mTabItemList.add(Constants.LEETCODE);
        if (google) mTabItemList.add(Constants.GOOGLE);
        if (mTabItemList.isEmpty()) {
            mTabItemList.add(Constants.CODEFORCES);
            mTabItemList.add(Constants.CODECHEF);
            mTabItemList.add(Constants.HACKERRANK);
            mTabItemList.add(Constants.HACKEREARTH);
            mTabItemList.add(Constants.SPOJ);
            mTabItemList.add(Constants.ATCODER);
            mTabItemList.add(Constants.LEETCODE);
            mTabItemList.add(Constants.GOOGLE);
        }
        Methods.saveTabItems(context, mTabItemList);

//
        titlesRecycler = view.findViewById(R.id.titles_recycler_view);
        titlesRecycler.setLayoutManager(new LinearLayoutManager(context));
        platformsListAdapter = new PlatformsListAdapter(context, mTabItemList);
        titlesRecycler.setAdapter(platformsListAdapter);
        platformsListAdapter.notifyDataSetChanged();
//
        saveActivity();
    }

    public void saveActivity() {
        Methods.setPreferences(context, Constants.LAYOUT_SWITCH_KEY, Constants.CURRENT_ACTIVITY, 1);
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
//        super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case R.id.menu_layout:
//                startActivity(new Intent(context, LayoutTwoActivity.class));
//                getActivity().finishAffinity();
//                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
////        sensorTranslationUpdater.registerSensorManager();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
////        sensorTranslationUpdater.unregisterSensorManager();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}