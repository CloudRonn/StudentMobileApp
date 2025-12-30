package com.example.studentsystem;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentDetailsActivity extends AppCompatActivity {

    TextView tvId, tvName, tvCourse, tvAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        tvId = findViewById(R.id.tvId);
        tvName = findViewById(R.id.tvName);
        tvCourse = findViewById(R.id.tvCourse);
        tvAge = findViewById(R.id.tvAge);

        // Get student details from Intent
        if (getIntent() != null) {
            tvId.setText("ID: " + getIntent().getStringExtra("id"));
            tvName.setText("Name: " + getIntent().getStringExtra("name"));
            tvCourse.setText("Course: " + getIntent().getStringExtra("course"));
            tvAge.setText("Age: " + getIntent().getIntExtra("age", 0));
        }
    }
}

