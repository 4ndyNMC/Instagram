package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.parse.CountCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    ConstraintLayout clLogin;
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity();
        }

        clLogin = findViewById(R.id.clLogin);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                login(username, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                try {
                    signup(username, password);
                } catch (ParseException e) {
                    Snackbar.make(clLogin, "Sorry, there was trouble signing up", Snackbar.LENGTH_LONG).show();
                    Log.i(TAG, "signup error", e);
                }

            }
        });
    }

    private void signup(String username, String password) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (e != null) {
                    Snackbar.make(clLogin, "Sorry, there was trouble signing up", Snackbar.LENGTH_LONG).show();
                    return;
                }
                Log.i(TAG, "number of counts: " + count);
                if (count > 0) {
                    Snackbar.make(clLogin, "That username already exists", Snackbar.LENGTH_LONG).show();
                    return;
                }
                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                try {
                    user.signUp();
                } catch (ParseException ex) {
                    Snackbar.make(clLogin, "Sorry, there was trouble signing up", Snackbar.LENGTH_LONG).show();
                }
                goToMainActivity();
            }
        });
    }

    public void login(String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    // TODO: Better error handling
                    Log.e(TAG, "Issue with login ):", e);
                    Snackbar.make(clLogin,
                            "Sorry, that is an invalid username/password",
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                goToMainActivity();
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}