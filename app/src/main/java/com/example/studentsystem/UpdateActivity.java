package com.example.studentsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {

    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private ArrayList<Student> studentList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        db = new DatabaseHelper(this);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.recyclerView);

        studentList = new ArrayList<>();
        adapter = new StudentAdapter(this, studentList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> searchStudents());
    }

    private void searchStudents() {
        String query = etSearch.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Enter Student ID or Name", Toast.LENGTH_SHORT).show();
            return;
        }

        studentList.clear();
        Cursor cursor = db.searchStudent(query);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No student found", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            return;
        }

        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String course = cursor.getString(2);
            int age = cursor.getInt(3);
            studentList.add(new Student(id, name, course, age));
        }

        adapter.notifyDataSetChanged();

        // Handle tapping a student card for editing
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(android.view.View view, int position) {
                        Student s = studentList.get(position);
                        showUpdateDialog(s);
                    }

                    @Override
                    public void onLongItemClick(android.view.View view, int position) {
                        // Optional: long click
                    }
                })
        );
    }

    private void showUpdateDialog(Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Student");

        android.view.View view = getLayoutInflater().inflate(R.layout.dialog_update_student, null);

        EditText etName = view.findViewById(R.id.etName);
        EditText etCourse = view.findViewById(R.id.etCourse);
        EditText etAge = view.findViewById(R.id.etAge);

        etName.setText(student.getName());
        etCourse.setText(student.getCourse());
        etAge.setText(String.valueOf(student.getAge()));

        Button btnSave = view.findViewById(R.id.btnSave);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String course = etCourse.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();

            if (name.isEmpty() || course.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Age must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = db.updateStudent(student.getId(), name, course, age);
            if (updated) {
                Toast.makeText(this, "Student Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                searchStudents(); // refresh list
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
