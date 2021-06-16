package com.noobsever.codingcontests.Screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.noobsever.codingcontests.Adapters.CardAdapter;
import com.noobsever.codingcontests.Models.ContestObject;
import com.noobsever.codingcontests.R;
import com.noobsever.codingcontests.Repository.RoomRepository;
import com.noobsever.codingcontests.Utils.Constants;
import com.noobsever.codingcontests.Utils.Methods;
import com.noobsever.codingcontests.ViewModel.ApiViewModel;
import com.noobsever.codingcontests.ViewModel.RoomViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowContestCardsActivity extends AppCompatActivity {

    Toolbar toolbar;
    private RecyclerView mRecyclerCodeforces;
    private CardAdapter mCardAdapter;
    private ImageView mContestImage;
    RoomViewModel mRoomViewModel;
    ApiViewModel apiViewModel;
    List<ContestObject> contestByPlatform;
    String website;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contest_cards);

        mContestImage = findViewById(R.id.contest_image);

        toolbar = findViewById(R.id.app_bar_of_cards);
        setSupportActionBar(toolbar);
        website = getIntent().getStringExtra(Constants.WEBSITE);
        Objects.requireNonNull(getSupportActionBar()).setTitle(website);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(website.equals(Constants.GOOGLE))
        {
            website="Kick Start";       // due to API result ,for Google
        }
        mRecyclerCodeforces = findViewById(R.id.ContestCardsRecycler);
        mRecyclerCodeforces.setLayoutManager(new LinearLayoutManager(this));
        contestByPlatform = new ArrayList<>();
        mCardAdapter = new CardAdapter(this,contestByPlatform);
        mRecyclerCodeforces.setAdapter(mCardAdapter);
        mCardAdapter.setData(contestByPlatform);


        mRoomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
                mRoomViewModel.getAllContests().observe(this, new Observer<List<ContestObject>>() {
            @Override
            public void onChanged(List<ContestObject> contestObjects) {
                EventBus.getDefault().post(contestObjects);
                Log.e("Objs on show card>>>>",String.valueOf(contestObjects.size()));
                mRecyclerCodeforces.setAdapter(mCardAdapter);
                for(ContestObject contest :contestObjects)
                {
                    if(contest.getPlatform().toLowerCase().equals(website.toLowerCase()))
                    {
                        contestByPlatform.add(contest);
                    }
                }
                mCardAdapter.setData(contestByPlatform);

            }
        });
        assert website != null;
        switch (website) {
            case Constants.CODEFORCES:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.codeforces2));
                break;
            case Constants.CODECHEF:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.codechef2));
                break;
            case Constants.HACKEREARTH:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.hackerearth2));
                break;
            case Constants.HACKERRANK:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.hackerrank2));
                break;
            case Constants.LEETCODE:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.leetcode2));
                break;
            case Constants.SPOJ:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.spoj2));
                break;
            case Constants.GOOGLE:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.google2));
                break;
            case Constants.ATCODER:
                mContestImage.setImageDrawable(getResources().getDrawable(R.drawable.atcoder2));
                break;
        }

        contestByPlatform = mRoomViewModel.findContestByPlatform(Methods.getSiteName(website));
        for(ContestObject co: contestByPlatform){
           co.setStart(Methods.utcToLocalTimeZone(ShowContestCardsActivity.this,co.getStart()));
           co.setEnd(Methods.utcToLocalTimeZone(ShowContestCardsActivity.this,co.getEnd()));
           co.setDuration(Methods.secondToFormatted(co.getDuration()));
        }
        mCardAdapter.setData(contestByPlatform);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contest_search_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                SearchView searchView = (SearchView) item.getActionView();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        mCardAdapter.getFilter().filter(s);
                        return false;
                    }
                });
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}