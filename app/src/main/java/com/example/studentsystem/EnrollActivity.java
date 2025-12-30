
package com.example.studentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnrollActivity extends AppCompatActivity {

    private EditText etId, etName, etCourse, etAge;
    private Button btnEnroll;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        db = new DatabaseHelper(this);

        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etCourse = findViewById(R.id.etCourse);
        etAge = findViewById(R.id.etAge);
        btnEnroll = findViewById(R.id.btnEnroll);

        btnEnroll.setOnClickListener(v -> {
            String id = etId.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String course = etCourse.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();

            // Validation
            if (id.isEmpty() || name.isEmpty() || course.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Age must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = db.insertStudent(id, name, course, age);
            if (inserted) {
                Toast.makeText(this, "Student Enrolled Successfully", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Enrollment Failed: ID might already exist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        etId.setText("");
        etName.setText("");
        etCourse.setText("");
        etAge.setText("");
    }
}
