package com.example.studentsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentSystem.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_STUDENT = "students";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_COURSE = "course";
    public static final String COL_AGE = "age";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // ID is now TEXT so user can enter any value
        String createTable = "CREATE TABLE " + TABLE_STUDENT + " (" +
                COL_ID + " TEXT PRIMARY KEY, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_COURSE + " TEXT NOT NULL, " +
                COL_AGE + " INTEGER NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }

    // INSERT
    public boolean insertStudent(String id, String name, String course, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID, id);
        values.put(COL_NAME, name);
        values.put(COL_COURSE, course);
        values.put(COL_AGE, age);

        long result = db.insert(TABLE_STUDENT, null, values);
        return result != -1;
    }

    // VIEW
    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_STUDENT, null);
    }

    // SEARCH by ID or Name
    public Cursor searchStudent(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_STUDENT + " WHERE id = ? OR name LIKE ?",
                new String[]{query, "%" + query + "%"});
    }

    // UPDATE
    public boolean updateStudent(String id, String name, String course, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_COURSE, course);
        values.put(COL_AGE, age);

        int result = db.update(TABLE_STUDENT, values, "id = ?", new String[]{id});
        return result > 0;
    }

    // DELETE
    public boolean deleteStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_STUDENT, "id = ?", new String[]{id});
        return result > 0;
    }
}
