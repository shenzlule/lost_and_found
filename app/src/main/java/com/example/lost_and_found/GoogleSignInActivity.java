package com.example.lost_and_found;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private View dimOverlay;

    private ProgressBar progressBar;

    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        mAuth = FirebaseAuth.getInstance();
        signInButton = findViewById(R.id.signInButton);

        dimOverlay = findViewById(R.id.dimOverlay);

        progressBar = findViewById(R.id.progressBar);

        ImageView appIcon = findViewById(R.id.app_icon);
        TextView appName = findViewById(R.id.app_name);
        Button signInButton = findViewById(R.id.signInButton);

        // Load app icon with Glide
//        Glide.with(this)
//                .load(R.drawable.icon) // Replace with your actual app icon
//                .placeholder(R.drawable.icon) // Default image
//                .circleCrop() // Ensures the image is circular
//                .into(appIcon);


        // Create the wiggle animation
        ObjectAnimator wiggleAnimator = ObjectAnimator.ofFloat(appIcon, "rotation", -10f, 10f);  // Rotate -10 to 10 degrees
        wiggleAnimator.setDuration(500);  // Duration of one wiggle cycle
        wiggleAnimator.setRepeatCount(ObjectAnimator.INFINITE);  // Repeat indefinitely
        wiggleAnimator.setRepeatMode(ObjectAnimator.REVERSE);  // Reverse after each cycle
        wiggleAnimator.setInterpolator(new DecelerateInterpolator());  // Smooth out the animation

        // Start the animation
        wiggleAnimator.start();


        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Ensure you have this ID from Firebase
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(v -> signInWithGoogle());
        // Check if user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMain();
        }
    }

    private void signInWithGoogle() {
        dimOverlay.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            progressBar.setVisibility(View.VISIBLE); // Hide progress bar
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data)
                        .getResult(ApiException.class);

                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                    dimOverlay.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                }
            } catch (ApiException e) {
                progressBar.setVisibility(View.GONE); // Hide progress bar
                dimOverlay.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                Log.w("GoogleSignIn", "Sign in failed", e);
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE); // Hide progress bar
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToMain() {
        Intent intent = new Intent(GoogleSignInActivity.this, MainTabs.class);
        startActivity(intent);
        finish();
    }





}
