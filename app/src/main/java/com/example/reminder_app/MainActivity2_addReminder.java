package com.example.reminder_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.reminder_app.DatabaseContract.Reminder.COL_DATE;
import static com.example.reminder_app.DatabaseContract.Reminder.COL_Description;
import static com.example.reminder_app.DatabaseContract.Reminder.COL_LOCATION;
import static com.example.reminder_app.DatabaseContract.Reminder.COL_TIME;
import static com.example.reminder_app.DatabaseContract.Reminder.COL_TITLE;
import static com.example.reminder_app.DatabaseContract.Reminder.TABLE_NAME;
import static com.example.reminder_app.DatabaseContract.Reminder.TABLE_NAME2;

public class MainActivity2_addReminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    EditText et_title, et_description, et_location;
    TextView date_tv, time_tv;
    Button btn_setReminder, find_location;
    //String title, description;

    //TextView textView , textViewDate;
    //Button open_timePicker , cancel_btn, open_DatePicker , setAlarm;

    String title;
    String description;
    String location;

    int minutes, hours, days, months, years;
    String str_time;
    String dateString;
    int number_of_reminders = 0;

    DatabaseHelpers databaseHelper;
    SQLiteDatabase db;

    int id;      // of pendingIntent

    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_add_reminder);


        et_title = findViewById(R.id.et);
        et_description = findViewById(R.id.et2);
        et_location = findViewById(R.id.loc);
        date_tv = findViewById(R.id.date_tv);
        time_tv = findViewById(R.id.time_tv);
        btn_setReminder = findViewById(R.id.btn_setReminder);
        find_location = findViewById(R.id.find_location);

        time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new Timepickerss();
                dialogFragment.show(getSupportFragmentManager(), "Time picker");
            }
        });

        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerss();
                datepicker.show(getSupportFragmentManager(), "date picker");
            }
        });
//      ************************>>>>    <<<<***************************

        btn_setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // alertbox for empty things
                String titlee = et_title.getText().toString();
                String desc = et_description.getText().toString();
                String loca = et_location.getText().toString();

                //checkDateTime();

                if ((titlee.isEmpty()) || (titlee.equals(" "))){
                    alertTitle();
                }
                else if ((desc.isEmpty()) || (desc.equals(" "))){
                    alertdesc();
                }
                else if ((loca.isEmpty()) || (loca.equals(" "))){
                    alertloc();
                }
                else{
                    Calendar calendar = Calendar.getInstance();
                    //calendar.set(Calendar.YEAR, years);
                    //calendar.set(Calendar.MONTH, months);
                    //calendar.set(Calendar.DAY_OF_MONTH, days);
               /* calendar.set(Calendar.HOUR, hours);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(years, months, days); */
                    calendar.set(years, months, days, hours, minutes, 0);
                    Toast.makeText(MainActivity2_addReminder.this, "Inside setAlarm", Toast.LENGTH_SHORT).show();

                    startAlarm(calendar);
                    addReminder();
                }
            }
        });

        find_location.setOnClickListener(new View.OnClickListener() {
            Intent chooser = null;
            @Override
            public void onClick(View v) {
                Intent intentfind = new Intent(Intent.ACTION_VIEW);
                intentfind.setData(Uri.parse("geo:33.6844, 73.0479"));
                chooser = Intent.createChooser(intentfind, "Launch maps");
                startActivity(chooser);
            }
        });

    }
//*******************************************>>>>>  onCreate ends here   <<<<<********************************************************************************

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        years = year;
        months = month;
        days = dayOfMonth;

        dateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        date_tv.setText(dateString);

        datePicker = view;

        Toast.makeText(this, "Inside onDateSet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        time_tv.setText(time);

        hours = hourOfDay;
        minutes = minute;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);

        updateTimeText(cal);

        Toast.makeText(this, "Inside onTimeSet", Toast.LENGTH_SHORT).show();
    }

    void updateTimeText(Calendar calendar) {
        str_time = " ";
        str_time += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        time_tv.setText(str_time);
    }

    void startAlarm(Calendar calendar) {
        //pending_request_code++;

        title = et_title.getText().toString();
        description = et_description.getText().toString();
        location = et_location.getText().toString();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

        Intent intent = new Intent(this, Alertreciever.class);
        intent.putExtra("title", title);
        intent.putExtra("desc", description);

        id = (int) System.currentTimeMillis();
        Toast.makeText(this, "Time in millis " + id, Toast.LENGTH_SHORT).show();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);  // id for each pending intent must be unique

        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE,1);
            dateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
            date_tv.setText(dateString);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        //intentArray.add(pendingIntent);

        Toast.makeText(this, "Inside startAlarm " + calendar.getTimeInMillis(), Toast.LENGTH_SHORT).show();
    }

    public void addReminder() {
        Toast.makeText(this, "inside addReminder", Toast.LENGTH_SHORT).show();
        number_of_reminders++;

        databaseHelper = new DatabaseHelpers(this);

        runOnUiThread(new Runnable(){
            public void run() {

                try {
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    Thread.sleep(1000);// sleeps 1 second
                    //Do Your process here.
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseContract.Reminder.COL_TITLE, title);
                    contentValues.put(DatabaseContract.Reminder.COL_Description, description);
                    contentValues.put(DatabaseContract.Reminder.COL_TIME, str_time);
                    contentValues.put(DatabaseContract.Reminder.COL_DATE, dateString);
                    contentValues.put(DatabaseContract.Reminder.COL_LOCATION, location);
                    contentValues.put(DatabaseContract.Reminder.COL_PENDING_ID, id);


                    long newRowId = db.insert(TABLE_NAME, null, contentValues);
                    if (newRowId > 0) {
                        Toast.makeText(MainActivity2_addReminder.this, "Reminder added in dataBase", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity2_addReminder.this, "new row id = " + newRowId, Toast.LENGTH_SHORT).show();
                        Intent intentSendBack = new Intent(MainActivity2_addReminder.this, MainActivity.class);
                        intentSendBack.putExtra("count", number_of_reminders);
                        startActivity(intentSendBack);
                    }
                    else {
                        Toast.makeText(MainActivity2_addReminder.this, "Reminder insertion failed in table1", Toast.LENGTH_SHORT).show();
                        //boolean isInserted = databaseHelper.insertData(title, description, location, str_time, dateString);
                    }


                } catch (final Exception ex) {
                    Log.i("---","Exception in thread");
                }
            }
        });


      /*  Intent intentService = new Intent(this, ServiceDataBase.class);
        intentService.putExtra("title", title);
        intentService.putExtra("desc", description);
        intentService.putExtra("time", str_time);
        intentService.putExtra("date", dateString);
        intentService.putExtra("loc", location);
        startActivity(intentService);  */

/*
        ClassAsyncTask classAsyncTask = new ClassAsyncTask(this);
        classAsyncTask.execute();
        */

    }

    public void alertTitle(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The title of reminder can not be empty")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alertdesc(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The description of reminder can not be empty")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alertloc(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The location of reminder can not be empty")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /*

    private static class ClassAsyncTask extends AsyncTask<Void, Void, String>{
        DatabaseHelper databaseHelper;

        private WeakReference<MainActivity2_addReminder> activityWeakReference;

        ClassAsyncTask(MainActivity2_addReminder activity){       // constructor
            activityWeakReference = new WeakReference<MainActivity2_addReminder>(activity);
        }
        @Override
        protected String doInBackground(Void... voids) {

            MainActivity2_addReminder activity = activityWeakReference.get();
            databaseHelper = new DatabaseHelper(activity);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContract.Reminder.COL_TITLE, activity.title);
            contentValues.put(DatabaseContract.Reminder.COL_Description, activity.description);
            contentValues.put(DatabaseContract.Reminder.COL_TIME, activity.str_time);
            contentValues.put(DatabaseContract.Reminder.COL_DATE, activity.dateString);
            contentValues.put(DatabaseContract.Reminder.COL_LOCATION, activity.location);

            long newRowId = db.insert(TABLE_NAME, null, contentValues);

            if (newRowId > 0) {
                Toast.makeText(activity, "Reminder added in dataBase", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "new row id = " + newRowId, Toast.LENGTH_SHORT).show();
            /* Intent intentSendBack = new Intent(this, MainActivity.class);
            intentSendBack.putExtra("count", number_of_reminders);
            startActivity(intentSendBack); */
          /*  } else {
                Toast.makeText(activity, "Reminder insertion failed", Toast.LENGTH_SHORT).show();

                //boolean isInserted = databaseHelper.insertData(title, description, location, str_time, dateString);
            }

            return "Reminder added in database";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MainActivity2_addReminder activity = activityWeakReference.get();
            if (activity==null || activity.isFinishing()){
                return;
            }
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
        }
    }       */

         /* public void checkDateTime(){
              runOnUiThread(new Runnable(){
                  public void run() {

                      try {
                          Thread.sleep(1000);// sleeps 1 second
                          //Do Your process here.

                          if (datePicker.SelectedDate == null)
                          {
                              // Do what you have to do
                          }


                      } catch (final Exception ex) {
                          Log.i("---","Exception in thread");
                      }
                  }
              });
          }   */

}























