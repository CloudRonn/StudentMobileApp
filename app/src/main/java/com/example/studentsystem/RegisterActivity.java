package com.example.studentsystem;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText email, phone, pass, confirm;
    DatabaseHelper db;
    boolean show1 = false, show2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        email = findViewById(R.id.etEmail);
        phone = findViewById(R.id.etPhone);
        pass = findViewById(R.id.etPassword);
        confirm = findViewById(R.id.etConfirm);

        ImageView eye1 = findViewById(R.id.eye1);
        ImageView eye2 = findViewById(R.id.eye2);

        eye1.setOnClickListener(v -> toggle(pass, eye1, show1 = !show1));
        eye2.setOnClickListener(v -> toggle(confirm, eye2, show2 = !show2));

        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            if (!pass.getText().toString().equals(confirm.getText().toString())) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.registerUser(
                    email.getText().toString(),
                    phone.getText().toString(),
                    pass.getText().toString())) {

                Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggle(EditText et, ImageView img, boolean show) {
        et.setTransformationMethod(show ?
                HideReturnsTransformationMethod.getInstance() :
                PasswordTransformationMethod.getInstance());
    }
}

