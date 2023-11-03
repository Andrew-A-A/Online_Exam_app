package com.andrew.saba.onlineexamapp.ui.login;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrew.saba.onlineexamapp.MainActivity;
import com.andrew.saba.onlineexamapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 4;
    private EditText emailEditText, passwordEditText;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button emailLoginButton = findViewById(R.id.emailLoginButton);
        Button googleSignInButton = findViewById(R.id.googleSignInButton);
        Button registrationButton = findViewById(R.id.registrationButton);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Email login button click listener
        emailLoginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                signInWithEmailPassword(email, password);
            }
        });

        // Registration button click listener
        registrationButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                registerWithEmailPassword(email, password);
            }
        });

        // Google Sign-In button click listener
        googleSignInButton.setOnClickListener(view -> signInWithGoogle());
    }

    // Sign in with email and password
    private void signInWithEmailPassword(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "User not found, register please", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                Log.i("login", "signInWithEmailPassword: Login done");
                loginDone();
            }  // Failed to log in with email and password
            // Handle the failure

        });
    }

    // Sign in with Google
    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                loginDone();
            } catch (ApiException e) {
                // Failed to sign in with Google
                Toast.makeText(this, "Error, " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Sign in with Firebase using Google credentials
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Log.i("login", "signInWithGoogle: Login done");
                        loginDone();
                    } else {
                        // Failed to log in with Google
                        Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Register a new user with email and password
    private void registerWithEmailPassword(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnFailureListener(e -> Toast.makeText(this, "Error, " + e, Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        loginDone();
                    }
                });
    }

    // Handle the completion of login or registration
    private void loginDone() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            if (!isMainActivityRunning()) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
                finish();
            }
        }
    }

    // Helper method to check if MainActivity is running
    private boolean isMainActivityRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo taskInfo : runningTasks) {
            if (taskInfo.baseActivity.getPackageName().equals(getPackageName()) &&
                    taskInfo.baseActivity.getClassName().equals(MainActivity.class.getName())) {
                return true;
            }
        }

        return false;
    }
}
