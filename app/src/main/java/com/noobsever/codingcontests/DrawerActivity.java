package com.noobsever.codingcontests;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.messaging.FirebaseMessaging;
import com.noobsever.codingcontests.Models.ContestObject;
import com.noobsever.codingcontests.Utils.Constants;
import com.noobsever.codingcontests.Utils.Methods;
import com.noobsever.codingcontests.ViewModel.ApiViewModel;
import com.noobsever.codingcontests.ViewModel.RoomViewModel;
import com.noobsever.codingcontests.databinding.ActivityDrawerBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class DrawerActivity extends AppCompatActivity  {
    // TODO: Navigation Drawer has Notification, FAQ, Share US, Feedback, OpenSource Tab. These tabs are Incomplete. Contact Anubhaw Sir for FAQ Fragment.
    // TODO: Remove all libraries that are not in use
    // TODO: Feedback should show user a Edit Text. Collect Feedback and save it in a Google spreadsheet. Search Google how to access spreadsheet from Android.
    // TODO: Everywhere in the App show all images using Glide
    // TODO: Remove all deprecated methods
    // TODO: Reduce MIN SDK from 26. At least Marshmallow or KitKat should be able to run it.
    // TODO: Make all variables private
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDrawerBinding binding;

    //TODO: Make all variables private
    DrawerLayout drawer;

    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    ApiViewModel apiViewModel;
    RoomViewModel mRoomViewModel;
    boolean doubleBackPressExitOnce = false;
    ImageView profilePicture;
    TextView emailId, name;
    NavController navController;

    private CheckBox cforces, cchef, hrank, hearth, spoj, atcoder, leetcode, google;
    private SwitchMaterial switchTwelve, switchTwentyFour, switchNotification, switchRated, switchRunning, switch24Hours, switchUpcomingSevenDays, switchTwoWeeks, switchOneMonth;
    ArrayList<String> checkedItem;
    ArrayList<String> prevCheckedListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** View Binding done automatically by Android Template*/
        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDrawer.toolbar);
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        checkedItem = new ArrayList<>();

        /* fetching previously saved checkboxes in data */
        try {
            prevCheckedListItem = (ArrayList<String>) Methods.fetchTabItems(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_open,R.id.nav_open_source)
                .setDrawerLayout(drawer)
                .build();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if(item.getItemId()==R.id.nav_open_source){
                    Toast.makeText(DrawerActivity.this, "Nipun", Toast.LENGTH_SHORT).show();
                }
                return false;

            }
        });
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        
        if(drawer!=null && drawer instanceof DrawerLayout ){
            DrawerLayout mDrawer=(DrawerLayout)drawer;
            mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(@NonNull @NotNull View drawerView) {
                }

                @Override
                public void onDrawerClosed(@NonNull @NotNull View drawerView) {
                    saveCheckBox();
                    /* When drawer closed it will restart the activity when fragment status is Home fragment and
                     * checkboxes are changed from previous checkboxes.
                     */
                    if(navController.getCurrentDestination().getId()==R.id.nav_home && !checkedItem.equals(prevCheckedListItem)) {
                        finish();
                        startActivity(new Intent(DrawerActivity.this, DrawerActivity.class));
                        overridePendingTransition(0, 0);
                    }
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });
        }
        /** Access complete Nav Drawer */
        View header = navigationView.getHeaderView(0);

        /* Initialising all the layout views
         * used in nav header layout.
         */
        initHeaderView(header);
        handleSettingsInNavDrawer();

        mRoomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        apiViewModel = new ViewModelProvider(this).get(ApiViewModel.class);
        apiViewModel.init();

        /**Since Internet is necessary for App to function
         * Check if Internet is Available
         * */
        new Methods.InternetCheck(this).isInternetConnectionAvailable(new Methods.InternetCheck.InternetCheckListener() {
            @Override
            public void onComplete(boolean connected) {
                if (connected) {
                    Log.e("INTERNET", "CONNECTED");
                    Methods.setPreferences(DrawerActivity.this, Constants.ISINTERNET, Constants.ISINTERNET, 1);
                    apiViewModel.fetchContestFromApi();
                } else {
                    Methods.setPreferences(DrawerActivity.this, Constants.ISINTERNET, Constants.ISINTERNET, 0);
                }
            }
        });

        apiViewModel.getAllContests().observe(DrawerActivity.this, new Observer<List<ContestObject>>() {
            @Override
            public void onChanged(List<ContestObject> contestObjects) {
                mRoomViewModel.deleteAndAddAllTuples(contestObjects);
            }
        });
    }
    private void initHeaderView(View header){
        /* This method initialisng all attributes
         * used in navigation drawer header 
         */
        cforces = header.findViewById(R.id.cb_codeforces);
        cchef = header.findViewById(R.id.cb_codechef);
        hrank = header.findViewById(R.id.cb_hackerrank);
        hearth = header.findViewById(R.id.cb_hackerearth);
        spoj = header.findViewById(R.id.cb_spoj);
        atcoder = header.findViewById(R.id.cb_atcoder);
        leetcode = header.findViewById(R.id.cb_leetcode);
        google = header.findViewById(R.id.cb_google);
        switchTwelve = header.findViewById(R.id.switch_12_time_format);
        switchTwentyFour = header.findViewById(R.id.switch_24_time_format);
        switchNotification = header.findViewById(R.id.switch_notification);
        switchRated = header.findViewById(R.id.switch_ratedcontest);
        switchRunning = header.findViewById(R.id.switch_runningcontest);
        switch24Hours = header.findViewById(R.id.switch_24Hours);
        switchUpcomingSevenDays = header.findViewById(R.id.switch_upcoming1week);
        switchTwoWeeks = header.findViewById(R.id.switch_twoWeeks);
        switchOneMonth = header.findViewById(R.id.switch_oneMonth);
        profilePicture=header.findViewById(R.id.profile_picture);
        emailId=header.findViewById(R.id.profile_email);
        name=header.findViewById(R.id.profile_name);
    }
    private void handleSettingsInNavDrawer() {
        /** Set Profile Picture, User Name and Email Address in Navigation Drawer */
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String personEmail = account.getEmail();
            emailId.setText(personEmail);

            name.setText(account.getGivenName() + " " + account.getFamilyName());
            Glide.with(this).load(account.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(profilePicture);// show Image using glide
        }

        restoreCheckBoxState();

        restoreToggledItemsState();

        switchRated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //If Rated Contest switch checked, 1 is stored in sharedPreferences indicating ON
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_RATED, Constants.SWITCH_RATED, 1);
                } else {
                    //If Rated Contest switch Unchecked, 0 is stored in sharedPreferences indicating ON
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_RATED, Constants.SWITCH_RATED, 0);
                }
            }
        });
        switchRunning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //If Running Contest switch checked, 1 is stored in sharedPreferences indicating ON
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_RUNNING, Constants.SWITCH_RUNNING, 1);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_24HOURS, Constants.SWITCH_24HOURS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_UPCOMING_SEVEN_DAYS, Constants.SWITCH_UPCOMING_SEVEN_DAYS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_2WEEKS, Constants.SWITCH_2WEEKS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_1MONTH, Constants.SWITCH_1MONTH, 0);
                    switch24Hours.setChecked(false);
                    switchUpcomingSevenDays.setChecked(false);
                    switchTwoWeeks.setChecked(false);
                    switchOneMonth.setChecked(false);
                } else {
                    //If Running Contest switch Unchecked, 0 is stored in sharedPreferences indicating ON
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_RUNNING, Constants.SWITCH_RUNNING, 0);
                }
            }
        });
        switch24Hours.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_24HOURS, Constants.SWITCH_24HOURS, 1);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_UPCOMING_SEVEN_DAYS, Constants.SWITCH_UPCOMING_SEVEN_DAYS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_2WEEKS, Constants.SWITCH_2WEEKS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_1MONTH, Constants.SWITCH_1MONTH, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_RUNNING, Constants.SWITCH_RUNNING, 0);
                    switchRunning.setChecked(false);
                    switchUpcomingSevenDays.setChecked(false);
                    switchTwoWeeks.setChecked(false);
                    switchOneMonth.setChecked(false);
                } else {

                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_24HOURS, Constants.SWITCH_24HOURS, 0);
//                    switchUpcomingSevenDays.setChecked(true);
//                    switchTwoWeeks.setChecked(true);
//                    switchOneMonth.setChecked(true);
                }
            }
        });
        switchUpcomingSevenDays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_24HOURS, Constants.SWITCH_24HOURS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_UPCOMING_SEVEN_DAYS, Constants.SWITCH_UPCOMING_SEVEN_DAYS, 1);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_2WEEKS, Constants.SWITCH_2WEEKS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_1MONTH, Constants.SWITCH_1MONTH, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_RUNNING, Constants.SWITCH_RUNNING, 0);
                    switchRunning.setChecked(false);
                    switch24Hours.setChecked(false);
                    switchTwoWeeks.setChecked(false);
                    switchOneMonth.setChecked(false);
                } else {

                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_UPCOMING_SEVEN_DAYS, Constants.SWITCH_UPCOMING_SEVEN_DAYS, 0);
//                    switch24Hours.setChecked(true);
//                    switchTwoWeeks.setChecked(true);
//                    switchOneMonth.setChecked(true);
                }
            }
        });
        switchTwoWeeks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_24HOURS, Constants.SWITCH_24HOURS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_UPCOMING_SEVEN_DAYS, Constants.SWITCH_UPCOMING_SEVEN_DAYS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_2WEEKS, Constants.SWITCH_2WEEKS, 1);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_1MONTH, Constants.SWITCH_1MONTH, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_RUNNING, Constants.SWITCH_RUNNING, 0);
                    switchRunning.setChecked(false);
                    switchUpcomingSevenDays.setChecked(false);
                    switch24Hours.setChecked(false);
                    switchOneMonth.setChecked(false);
                } else {

                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_2WEEKS, Constants.SWITCH_2WEEKS, 0);
//                    switchUpcomingSevenDays.setChecked(true);
//                    switch24Hours.setChecked(true);
//                    switchOneMonth.setChecked(true);
                }
            }
        });
        switchOneMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_24HOURS, Constants.SWITCH_24HOURS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_UPCOMING_SEVEN_DAYS, Constants.SWITCH_UPCOMING_SEVEN_DAYS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_2WEEKS, Constants.SWITCH_2WEEKS, 0);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_1MONTH, Constants.SWITCH_1MONTH, 1);
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_RUNNING, Constants.SWITCH_RUNNING, 0);
                    switchRunning.setChecked(false);
                    switchUpcomingSevenDays.setChecked(false);
                    switch24Hours.setChecked(false);
                    switchTwoWeeks.setChecked(false);
                } else {

                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_1MONTH, Constants.SWITCH_1MONTH, 0);
//                    switchUpcomingSevenDays.setChecked(true);
//                    switch24Hours.setChecked(true);
//                    switchTwoWeeks.setChecked(true);
                }
            }
        });
        switchTwelve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //If 12 hour switch checked, 1 is stored in sharedPreferences indicating ON
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_TWELVE, Constants.SWITCH_TWELVE, 1);
                    switchTwentyFour.setChecked(false);
                } else {
                    //If 12 hour switch unchecked, 0 is stored in sharedPreferences indicating OFF and 24 hr switch checked
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_TWELVE, Constants.SWITCH_TWELVE, 0);
                    switchTwentyFour.setChecked(true);
                }
            }
        });

        switchTwentyFour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //If 24 hour switch checked, 0 is stored in sharedPreferences indicating 12 hour switch OFF
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_TWELVE, Constants.SWITCH_TWELVE, 0);
                    switchTwelve.setChecked(false);
                } else {
                    //If 24 hour switch checked, 1 is stored in sharedPreferences indicating 12 hour switch ON
                    Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_TWELVE, Constants.SWITCH_TWELVE, 1);
                    switchTwelve.setChecked(true);

                }
            }
        });

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //If notification switch ON, 1 is stored in sharedPreferences
                SharedPreferences sharedpreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);


                boolean notificationIsOn = sharedpreferences.getBoolean("notificationIsOn", false);
                if (!isChecked)
                /**notification was enabled now disable it*/

                    FirebaseMessaging.getInstance().unsubscribeFromTopic("hncc")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    if (task.isSuccessful()) {
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putBoolean("notificationIsOn", false);
                                        editor.commit();

                                        Toast.makeText(DrawerActivity.this, "Notification Off", Toast.LENGTH_SHORT).show();
                                        Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_NOTIFICATION, Constants.SWITCH_NOTIFICATION, 1);
                                    }


                                }
                            });
                    //If notification switch ON, 0 is stored in sharedPreferences
                else {
                    /** Notification was disabled now enable it
                     * unsubscribe from topic 'hncc'
                     * On Action Successful save your state using SharedPreferences */
                    FirebaseMessaging.getInstance().subscribeToTopic("hncc")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putBoolean("notificationIsOn", true);
                                        editor.commit();
                                        Toast.makeText(DrawerActivity.this, "Notification On", Toast.LENGTH_SHORT).show();
                                        Methods.setPreferences(DrawerActivity.this, Constants.SWITCH_NOTIFICATION, Constants.SWITCH_NOTIFICATION, 0);
                                    }

                                }
                            });
                }
            }
        });
    }

    // TODO: If Navigation Drawer is Open and the user presses back button then the list should update
    @Override
    public void onBackPressed() {
        /* If drawer is opened when back pressed drawer will be closed */
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(navController.getCurrentDestination().getId()==R.id.nav_home){
            /* If currently in home fragment on back pressed application will closed */
            finishAffinity();
        }
        else {
            /* If currently in other fragment instead of home fragment on back pressed
             * user will navigate to home fragment.
             */
            navController.navigate(R.id.nav_home);
        }
//        finishAffinity();
    }
    private void saveCheckBox(){
        /* When drawer will closed this method save all checkboxes state and store
         * this in database when checkboxes checked methods store 1 and when checkboxes
         * not checked methods store 0.
         * It means 1 stands for true and 0 stands for false.
         */
        checkedItem.clear();
        if (cforces.isChecked()) {
            checkedItem.add(Constants.CODEFORCES);
            Methods.setPreferences(DrawerActivity.this,Constants.CODEFORCES,Constants.CODEFORCES,1);
        }
        else  Methods.setPreferences(DrawerActivity.this,Constants.CODEFORCES,Constants.CODEFORCES,0);

        if (cchef.isChecked()){
            checkedItem.add(Constants.CODECHEF);
            Methods.setPreferences(DrawerActivity.this,Constants.CODECHEF,Constants.CODECHEF,1);
        }
        else Methods.setPreferences(DrawerActivity.this,Constants.CODECHEF,Constants.CODECHEF,0);

        if (hrank.isChecked()) {
            checkedItem.add(Constants.HACKERRANK);
            Methods.setPreferences(DrawerActivity.this,Constants.HACKERRANK,Constants.HACKERRANK,1);
        }
        else  Methods.setPreferences(DrawerActivity.this,Constants.HACKERRANK,Constants.HACKERRANK,0);

        if (hearth.isChecked())
        {
            checkedItem.add(Constants.HACKEREARTH);
            Methods.setPreferences(DrawerActivity.this,Constants.HACKEREARTH,Constants.HACKEREARTH,1);
        }
        else Methods.setPreferences(DrawerActivity.this,Constants.HACKEREARTH,Constants.HACKEREARTH,0);

        if (spoj.isChecked()) {
            checkedItem.add(Constants.SPOJ);
            Methods.setPreferences(DrawerActivity.this,Constants.SPOJ,Constants.SPOJ,1);
        }
        else Methods.setPreferences(DrawerActivity.this,Constants.SPOJ,Constants.SPOJ,0);

        if (atcoder.isChecked()) {
            checkedItem.add(Constants.ATCODER);
            Methods.setPreferences(DrawerActivity.this,Constants.ATCODER,Constants.ATCODER,1);
        }
        else Methods.setPreferences(DrawerActivity.this,Constants.ATCODER,Constants.ATCODER,0);

        if (leetcode.isChecked())  {
            checkedItem.add(Constants.LEETCODE);
            Methods.setPreferences(DrawerActivity.this,Constants.LEETCODE,Constants.LEETCODE,1);
        }
        else  Methods.setPreferences(DrawerActivity.this,Constants.LEETCODE,Constants.LEETCODE,0);

        if (google.isChecked()){
            checkedItem.add(Constants.GOOGLE);
            Methods.setPreferences(DrawerActivity.this,Constants.GOOGLE,Constants.GOOGLE,1);
        }
        else Methods.setPreferences(DrawerActivity.this,Constants.GOOGLE,Constants.GOOGLE,0);

        if (checkedItem.isEmpty()) {
            cforces.setChecked(true);
            checkedItem.add(Constants.CODEFORCES);
            Methods.showToast(this, "Atleast 1 platform has to be selected");
            Methods.setPreferences(DrawerActivity.this,Constants.CODEFORCES,Constants.CODEFORCES,1);
        }
    }

    public void restoreCheckBoxState() {
        HashSet<String> set = new HashSet<>(prevCheckedListItem);
        cforces.setChecked(set.contains(Constants.CODEFORCES));
        cchef.setChecked(set.contains(Constants.CODECHEF));
        hrank.setChecked(set.contains(Constants.HACKERRANK));
        hearth.setChecked(set.contains(Constants.HACKEREARTH));
        spoj.setChecked(set.contains(Constants.SPOJ));
        atcoder.setChecked(set.contains(Constants.ATCODER));
        leetcode.setChecked(set.contains(Constants.LEETCODE));
        google.setChecked(set.contains(Constants.GOOGLE));
    }

    /***/
    public void restoreToggledItemsState() {
        switchTwelve.setChecked(Methods.getIntPreferences(DrawerActivity.this, Constants.SWITCH_TWELVE, Constants.SWITCH_TWELVE) != 0);
        switchTwentyFour.setChecked(Methods.getIntPreferences(DrawerActivity.this, Constants.SWITCH_TWELVE, Constants.SWITCH_TWELVE) == 0);
        switchNotification.setChecked(Methods.getIntPreferences(DrawerActivity.this, Constants.SWITCH_NOTIFICATION, Constants.SWITCH_NOTIFICATION) != 0);
        switchRated.setChecked(Methods.getIntPreferences(DrawerActivity.this, Constants.SWITCH_RATED, Constants.SWITCH_RATED) != 0);
        switchRunning.setChecked(Methods.getIntPreferences(DrawerActivity.this, Constants.SWITCH_RUNNING, Constants.SWITCH_RUNNING) != 0);
        switch24Hours.setChecked(Methods.getIntPreferences(DrawerActivity.this, Constants.SWITCH_24HOURS, Constants.SWITCH_24HOURS) != 0);
        switchUpcomingSevenDays.setChecked(Methods.getIntPreferences(DrawerActivity.this, Constants.SWITCH_UPCOMING_SEVEN_DAYS, Constants.SWITCH_UPCOMING_SEVEN_DAYS) != 0);
        switchTwoWeeks.setChecked(Methods.getIntPreferences(DrawerActivity.this, Constants.SWITCH_2WEEKS, Constants.SWITCH_2WEEKS) != 0);
        switchOneMonth.setChecked(Methods.getIntPreferences(DrawerActivity.this, Constants.SWITCH_1MONTH, Constants.SWITCH_1MONTH) != 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}