package com.squorpikkor.sqrchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private FirebaseAuth mAuth;

    private EditText emailEdit, passEdit, passConEdit, nameEdit;
    private TextView loginText;
    private Button signUpButton;

    boolean loginModeActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEdit = findViewById(R.id.email_edit);
        passEdit = findViewById(R.id.password_edit);
        passConEdit = findViewById(R.id.password_confirm_edit);
        nameEdit = findViewById(R.id.name_edit);
        loginText = findViewById(R.id.toggleLoginSignUpTextView);
        signUpButton = findViewById(R.id.loginSignUpButton);
        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(v -> signInUp(
                emailEdit.getText().toString().trim(),
                passEdit.getText().toString().trim(),
                passConEdit.getText().toString().trim(),
                loginModeActive
        ));

        loginText.setOnClickListener(v -> toggleLoginMode());

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
        }
    }

    private void signInUp(String email, String password, String confirm, boolean isLogged) {
        if (password.length()<7) Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        else if (email.isEmpty()) Toast.makeText(this, "Please input your email", Toast.LENGTH_SHORT).show();

        else if (isLogged) signInUser(email, password);
        else if (!password.equals(confirm))Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
        else signUpUser(email, password);
    }

    private void toggleLoginMode() {
        if (loginModeActive) {
            loginModeActive = false;
            signUpButton.setText("Sign Up");
            loginText.setText("Or log in");
            passConEdit.setVisibility(View.VISIBLE);
        } else {
            loginModeActive = true;
            signUpButton.setText("Sign In");
            loginText.setText("Or sign up");
            passConEdit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }

    }

    private void reload() {

    }


    //будет создан юзер в firebase
    private void signUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        createUser(user);
//                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                        updateUI(null);
                    }
                });
    }

    private void createUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(nameEdit.getText().toString().trim());
    }


    private void signInUser(String email, String password) {
        Log.e(TAG, "signInUser: ");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
//                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                        updateUI(null);
                    }
                });


    }

//    private void updateUI(Object o) {
//    }
}