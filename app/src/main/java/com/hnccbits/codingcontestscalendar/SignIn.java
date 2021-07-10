package com.hnccbits.codingcontestscalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SignIn extends AppCompatActivity {

    private static final int RC_SIGN_IN = 355;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signIn;
    Button skip;
    Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signIn = findViewById(R.id.sign_in_button);
        skip = findViewById(R.id.button_skip);
        signOut = findViewById(R.id.button_sign_out);

//         serverClientId is seperate for every integrated signh In. It is just like a Token.
//        String serverClientId = getString(R.string.web_client_id);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() { // sign out button is invisibe for now
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, DrawerActivity.class));
            }
        });

    }

    private void signIn() {
        /**Open SignIn Intent
         * Opens Dilogue Box to choose among your Google account(s) */
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        // TODO: Deprecated menthod. To be fixed later.
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**After user selects an account form Intent
         * or presses back button
         * Handle SignIn */

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            /**Sign In success
             * Update UI if necessary
             * and then switch Activity*/
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignIn.this, DrawerActivity.class));


        } catch (ApiException e) {
            /** SignIn Failure */
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "FAILURE", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        /** User pressed SignOut button*/
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SignIn.this, "Sign Out Success", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            startActivity(new Intent(SignIn.this, DrawerActivity.class));
        }

    }
}