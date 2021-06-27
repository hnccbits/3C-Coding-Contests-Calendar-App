package com.noobsever.codingcontests.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.noobsever.codingcontests.R;
import com.noobsever.codingcontests.SignIn;
import com.noobsever.codingcontests.Utils.Constants;
import com.noobsever.codingcontests.Utils.Methods;

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
        boolean notificationIsOn = sharedpreferences.getBoolean("notificationIsOn", false);
        boolean activatedNotificationFirstTime = sharedpreferences.getBoolean("activatedNotificationFirstTime", false);

        if (!activatedNotificationFirstTime) {
            if (!notificationIsOn) {


                FirebaseMessaging.getInstance().subscribeToTopic("hncc")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                if (!task.isSuccessful()) {
                                    editor.putBoolean("activatedNotificationFirstTime", true);
                                    editor.putBoolean("notificationIsOn", true);

                                    Toast.makeText(SplashScreenActivity.this, "Subscribe Failed", Toast.LENGTH_SHORT).show();
                                } else {
                                    editor.putBoolean("activatedNotificationFirstTime", true);
                                    editor.putBoolean("notificationIsOn", true);

                                    Toast.makeText(SplashScreenActivity.this, "Subscribe Success", Toast.LENGTH_SHORT).show();
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