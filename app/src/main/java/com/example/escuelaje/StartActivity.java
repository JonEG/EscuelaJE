package com.example.escuelaje;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 69;
    private LottieAnimationView lottie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_start);
        prepareLottie();

        checkIfUserIsAlreadySingedIn();
    }

    private void prepareLottie(){
        lottie = findViewById(R.id.startLottie);

        lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                //new AuthUI.IdpConfig.FacebookBuilder().build(),
                //new AuthUI.IdpConfig.TwitterBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                // Get an instance of AuthUI based on the default app
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                        .setIsSmartLockEnabled(true).build(), RC_SIGN_IN);
    }

    private void checkIfUserIsAlreadySingedIn(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // Start signed in activity
            startActivity(ListonActivity.createIntent(this, null));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            // not signed in
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            handleSignInResponse(resultCode, data);
            return;
        }
    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        Toast toast;
        // Successfully signed in
        if (resultCode == Activity.RESULT_OK) {
            startActivity(ListonActivity.createIntent(this, response));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                toast = Toast.makeText(this, "Sign in was cancelled!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                toast = Toast.makeText(this, "You have no internet connection", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                toast = Toast.makeText(this, "Unknown Error!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }
        toast = Toast.makeText(this, "Unknown Error!", Toast.LENGTH_LONG);
        toast.show();
    }


    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, StartActivity.class);
        return in;
    }
}