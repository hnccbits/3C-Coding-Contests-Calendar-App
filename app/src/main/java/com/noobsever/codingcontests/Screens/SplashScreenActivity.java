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
import com.noobsever.codingcontests.DrawerActivity;
import com.noobsever.codingcontests.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(
                R.layout.activity_splash_screen);

        /**Show splash Screen UI for 2000 milliseconds and then change move to SignIn Activity*/
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, DrawerActivity.class));
                finish(); // kill SplashScreenActivity.java activity to save memory
            }
        }, 2000);

        /** When app is opened for the first time it send firebase to subscrice to topic 'hncc'
         * This topic is used to push notification to app from Firebase
         * For the first time user should subscribe to to this topic
         * If Internet is down/off save its state in 'activatedNotificationFirstTime' and retry the next time user opens app
         *
         * 'notificationIsOn' saves if notification is on/off irrespective of using the app for first time or nth timt */
        SharedPreferences sharedpreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean notificationIsOn = sharedpreferences.getBoolean("notificationIsOn", false);
        boolean activatedNotificationFirstTime = sharedpreferences.getBoolean("activatedNotificationFirstTime", false);

        if (!activatedNotificationFirstTime) {
            if (!notificationIsOn) {

                /* subscribe to topic and check for success or failure and then save your state.*/
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

//                                    Toast.makeText(SplashScreenActivity.this, "Subscribe Success", Toast.LENGTH_SHORT).show();
                                }
                                editor.commit();
                            }
                        });

            }
        }


    }
}