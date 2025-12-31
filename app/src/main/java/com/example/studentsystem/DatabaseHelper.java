package com.example.studentsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentSystem.db";
    private static final int DATABASE_VERSION = 2;

    // STUDENT TABLE
    public static final String TABLE_STUDENT = "students";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_COURSE = "course";
    public static final String COL_AGE = "age";

    // USER TABLE
    public static final String TABLE_USER = "users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_EMAIL = "email";
    public static final String COL_PHONE = "phone";
    public static final String COL_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createStudentTable = "CREATE TABLE " + TABLE_STUDENT + " (" +
                COL_ID + " TEXT PRIMARY KEY, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_COURSE + " TEXT NOT NULL, " +
                COL_AGE + " INTEGER NOT NULL)";
        db.execSQL(createStudentTable);

        String createUserTable = "CREATE TABLE " + TABLE_USER + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PHONE + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    //  USER METHODS
    public boolean registerUser(String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USER + " WHERE email=? OR phone=?",
                new String[]{email, phone});

        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, email);
        values.put(COL_PHONE, phone);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USER, null, values);
        return result != -1;
    }

    public boolean loginUser(String input, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USER +
                        " WHERE (email=? OR phone=?) AND password=?",
                new String[]{input, input, password});

        boolean success = cursor.getCount() > 0;
        cursor.close();
        return success;
    }

    //STUDENT METHODS
    public boolean insertStudent(String id, String name, String course, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID, id);
        values.put(COL_NAME, name);
        values.put(COL_COURSE, course);
        values.put(COL_AGE, age);
        return db.insert(TABLE_STUDENT, null, values) != -1;
    }

    public Cursor getAllStudents() {
        return getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_STUDENT, null);
    }

    public Cursor searchStudent(String query) {
        return getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_STUDENT +
                        " WHERE id=? OR name LIKE ?",
                new String[]{query, "%" + query + "%"});
    }

    public boolean updateStudent(String id, String name, String course, int age) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_COURSE, course);
        values.put(COL_AGE, age);
        return getWritableDatabase().update(
                TABLE_STUDENT, values, "id=?",
                new String[]{id}) > 0;
    }

    public boolean deleteStudent(String id) {
        return getWritableDatabase().delete(
                TABLE_STUDENT, "id=?",
                new String[]{id}) > 0;
    }
}
