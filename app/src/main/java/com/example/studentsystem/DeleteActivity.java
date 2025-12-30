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

public class DeleteActivity extends AppCompatActivity {

    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private ArrayList<Student> studentList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

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

        // Handle tapping a student card for deletion
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(android.view.View view, int position) {
                        Student s = studentList.get(position);
                        showDeleteConfirmation(s);
                    }

                    @Override
                    public void onLongItemClick(android.view.View view, int position) {
                        // optional
                    }
                })
        );
    }

    private void showDeleteConfirmation(Student student) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete " + student.getName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean deleted = db.deleteStudent(student.getId());
                    if (deleted) {
                        Toast.makeText(this, "Student Deleted", Toast.LENGTH_SHORT).show();
                        searchStudents(); // refresh list
                    } else {
                        Toast.makeText(this, "Delete Failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}

