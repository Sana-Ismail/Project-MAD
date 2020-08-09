package com.example.reminder_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
//                                        *********************************************************************************************************************************
import androidx.annotation.Nullable;           //************************>>>>>>>>>>>>>>>>   This class is not for use    <<<<<<<<<<<<***********************************
//                                         *********************************************************************************************************************************

import static com.example.reminder_app.DatabaseContract.Reminder.COL_DATE;
import static com.example.reminder_app.DatabaseContract.Reminder.COL_Description;
import static com.example.reminder_app.DatabaseContract.Reminder.COL_LOCATION;
import static com.example.reminder_app.DatabaseContract.Reminder.COL_PENDING_ID;
import static com.example.reminder_app.DatabaseContract.Reminder.COL_TIME;
import static com.example.reminder_app.DatabaseContract.Reminder.COL_TITLE;
import static com.example.reminder_app.DatabaseContract.Reminder.TABLE_NAME;
import static com.example.reminder_app.DatabaseContract.Reminder.TABLE_NAME2;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "ReminderApp.db";

    // create table query
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
            DatabaseContract.Reminder._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.Reminder.COL_TITLE + " TEXT NOT NULL," +
            DatabaseContract.Reminder.COL_Description + " TEXT, " +
            DatabaseContract.Reminder.COL_TIME + " TEXT, " +
            DatabaseContract.Reminder.COL_DATE + " TEXT, " +
            DatabaseContract.Reminder.COL_LOCATION + " TEXT)"
            //DatabaseContract.Reminder.COL_PENDING_ID + " INTEGER)"
            ;

    public static final String CREATE_TABLE_QUERY2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 + "( " +
            DatabaseContract.Reminder._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.Reminder.COL_TIME2 + " TEXT, " +
            DatabaseContract.Reminder.COL_DATE2 + " TEXT, " +
            DatabaseContract.Reminder.COL_PENDING_ID2 + " INTEGER)"
            ;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
        db.execSQL(CREATE_TABLE_QUERY2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            //db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_PENDING_ID + " INTEGER");
            db.execSQL(CREATE_TABLE_QUERY2);
            onCreate(db);
            }
    }

    /*public boolean insertData(String title, String description, String location, String time, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_Description, description);
        contentValues.put(COL_TIME, time);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_LOCATION, location);

        long newRowId = db.insert(TABLE_NAME, null, contentValues);   // try >0
        
        if (newRowId>0){
            return true;
        }
        else{
            return false;
        }
    } */

    public boolean updateData(String oldTitle, String title, String description, String location, String time, String date, int pendingID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_Description, description);
        contentValues.put(COL_TIME, time);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_LOCATION, location);
        contentValues.put(COL_PENDING_ID, pendingID);

        String whereClause = DatabaseContract.Reminder.COL_TITLE + " = ?";
        String[] whereArgs = {oldTitle};

        int result = sqLiteDatabase.update(TABLE_NAME, contentValues, whereClause, whereArgs);
        return true;
    }

}
