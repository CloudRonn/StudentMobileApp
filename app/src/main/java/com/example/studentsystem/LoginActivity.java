package com.example.studentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etInput, etPassword;
    ImageView eye;
    DatabaseHelper db;
    boolean isVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        etInput = findViewById(R.id.etInput);
        etPassword = findViewById(R.id.etPassword);
        eye = findViewById(R.id.eye);
        TextView register = findViewById(R.id.tvRegister);

        eye.setOnClickListener(v -> {
            if (isVisible) {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                eye.setImageResource(R.drawable.ic_eye_off);
            } else {
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                eye.setImageResource(R.drawable.ic_eye);
            }
            isVisible = !isVisible;
        });

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            if (db.loginUser(
                    etInput.getText().toString(),
                    etPassword.getText().toString())) {

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }
}
