package com.noobsever.codingcontests.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.noobsever.codingcontests.R;
import com.noobsever.codingcontests.SignIn;
import com.noobsever.codingcontests.Utils.Constants;
import com.noobsever.codingcontests.Utils.Methods;

import static android.content.ContentValues.TAG;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            public void run() {
//                if(loadActivity()==1 || loadActivity()==0)
                    startActivity(new Intent(SplashScreenActivity.this, SignIn.class));
//                else
//                    startActivity(new Intent(SplashScreenActivity.this, LayoutTwoActivity.class));
                finish();
            }
        }, 1000);

        SharedPreferences sharedpreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//        boolean firstTime= sharedpreferences.getBoolean("FirstTime",false);
        boolean disableNotification= sharedpreferences.getBoolean("disableNotification",false);
        boolean activatedNotification= sharedpreferences.getBoolean("activatedNotification",false);

        if (!disableNotification){
            if (!activatedNotification){

                FirebaseMessaging.getInstance().subscribeToTopic("hncc")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                if (!task.isSuccessful()) {
                                    editor.putBoolean("activatedNotification",false);                                }
                                else{
                                    editor.putBoolean("activatedNotification",true);
                                }
                                editor.commit();
                            }
                        });

            }
        }



    }

    //  Function to get the last opened activity.
    int loadActivity() {
        return Methods.getIntPreferences(getApplicationContext(), Constants.LAYOUT_SWITCH_KEY, Constants.CURRENT_ACTIVITY);
    }
}