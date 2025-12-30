package com.example.studentsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    RecyclerView recyclerView;
    StudentAdapter adapter;
    ArrayList<Student> studentList;

    Button btnEnroll, btnUpdate, btnDelete;
    TextView tvNoStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        btnEnroll = findViewById(R.id.btnEnroll);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        tvNoStudents = findViewById(R.id.tvNoStudents);

        studentList = new ArrayList<>();
        adapter = new StudentAdapter(this, studentList);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        recyclerView.setAdapter(adapter);

        loadStudents();

        // Button Clicks
        btnEnroll.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EnrollActivity.class)));
        btnUpdate.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UpdateActivity.class)));
        btnDelete.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DeleteActivity.class)));

        // Example: Light/Dark mode toggle (simple)
        findViewById(R.id.btnToggleTheme).setOnClickListener(v -> {
            int currentNightMode = getResources().getConfiguration().uiMode;
            if ((currentNightMode & 0x30) == 0x20) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // light
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // dark
            }
        });
    }

    private void loadStudents() {
        studentList.clear();
        Cursor cursor = db.getAllStudents();
        if (cursor.getCount() == 0) {
            tvNoStudents.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        } else {
            tvNoStudents.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String course = cursor.getString(2);
            int age = cursor.getInt(3);

            studentList.add(new Student(id, name, course, age));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents(); // refresh when coming back from other activities
    }
}
