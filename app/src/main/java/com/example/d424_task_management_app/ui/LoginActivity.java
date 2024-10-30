package com.example.d424_task_management_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d424_task_management_app.R;
import com.example.d424_task_management_app.database.Repository;
import com.example.d424_task_management_app.entities.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private EditText input_username;
    private EditText input_password;
    private String username;
    private String password;
    Repository repository;
    UserSessionManagement userSessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        repository = new Repository(getApplication());
        userSessionManagement = new UserSessionManagement(this);

        input_username = findViewById(R.id.username);
        input_password = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(view -> {
            username = input_username.getText().toString().trim();
            password = input_password.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "To login, please enter a username and password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(username, password);
            }
        });

        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(view -> {
            username = input_username.getText().toString().trim();
            password = input_password.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "To register, please enter a username and password", Toast.LENGTH_SHORT).show();
            } else if (repository.getUser(input_username.getText().toString()) != null) {
                Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        new Thread(() -> {
            User user = repository.getUser(username);
            runOnUiThread(() -> {
                if (user != null) {
                    String hashedPassword = hashPassword(password, user.getSalt());
                    if (hashedPassword.equals(user.getHashedPassword())) {
                         Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                        int userID = user.getUserID();
                        userSessionManagement.createSession(userID);
                        startActivity(new Intent(getApplicationContext(), TaskList.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void registerUser(String username, String password) {
        new Thread(() -> {
            String salt = generateSalt();
            String hashedPassword = hashPassword(password, salt);
            User user = new User(username, hashedPassword, salt);
            runOnUiThread(() -> {
                int userID = repository.insert(user);
                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                userSessionManagement.createSession(userID);

                startActivity(new Intent(getApplicationContext(), TaskList.class));
            });
        }).start();
    }

    private String hashPassword(String password, String salt) {
        String combined = password + salt;
        byte[] bytes = combined.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateSalt() {
        byte[] salt = new byte[16];
        new java.security.SecureRandom().nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }
}
