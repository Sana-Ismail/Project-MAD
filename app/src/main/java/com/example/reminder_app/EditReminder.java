package com.example.reminder_app;

import androidx.appcompat.app.AppCompatActivity;
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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import static com.example.reminder_app.DatabaseContract.Reminder.TABLE_NAME;

public class EditReminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

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

    String getTitle;
    int pendingID;      // of pendingIntent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        et_title = findViewById(R.id.et);
        et_description = findViewById(R.id.et2);
        et_location = findViewById(R.id.loc);
        date_tv = findViewById(R.id.date_tv);
        time_tv = findViewById(R.id.time_tv);
        btn_setReminder = findViewById(R.id.btn_setReminder);
        find_location = findViewById(R.id.find_location);

        // ******** >>>  get data from previous activity when clicked on edit   <<<<<<<************

        //Intent intentGetData = new Intent();
        getTitle = getIntent().getStringExtra("title");
        et_title.setText(getTitle);
        //Toast.makeText(this, getTitle, Toast.LENGTH_SHORT).show();

        String getDescription = getIntent().getStringExtra("desc");
        et_description.setText(getDescription);
        //Toast.makeText(this, getDescription, Toast.LENGTH_SHORT).show();

        String getTime = getIntent().getStringExtra("time");
        time_tv.setText(getTime);
        // Toast.makeText(this, getTime, Toast.LENGTH_SHORT).show();

        String getDate = getIntent().getStringExtra("date");
        date_tv.setText(getDate);
        //Toast.makeText(this, getDate, Toast.LENGTH_SHORT).show();

        String getLocation = getIntent().getStringExtra("loc");    //String getLocation = intentGetData.getStringExtra("loc");  never do such a thing in your life ever
        et_location.setText(getLocation);
        //Toast.makeText(this, getLocation, Toast.LENGTH_SHORT).show();

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
                    calendar.set(years, months, days, hours, minutes, 0);
                    Toast.makeText(EditReminder.this, "Inside setAlarm", Toast.LENGTH_SHORT).show();

                    startAlarm(calendar);
                    updateReminder();
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
//***********************>>>>>  onCreate ends here   <<<<<********************************************************************************

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
        title = et_title.getText().toString();
        description = et_description.getText().toString();
        location = et_location.getText().toString();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alertreciever.class);
        intent.putExtra("title", title);
        intent.putExtra("desc", description);

        pendingID = (int) System.currentTimeMillis();
        Toast.makeText(this, "Time in millis " + pendingID, Toast.LENGTH_SHORT).show();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pendingID, intent, 0);  // id for each pending intent must be unique

        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE,1);
            dateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
            date_tv.setText(dateString);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Inside startAlarm " + calendar.getTimeInMillis(), Toast.LENGTH_SHORT).show();
    }


    public void updateReminder() {
        Toast.makeText(this, "inside addReminder", Toast.LENGTH_SHORT).show();
        number_of_reminders++;

        databaseHelper = new DatabaseHelpers(this);

        runOnUiThread(new Runnable(){
            public void run() {

                try {
                    //SQLiteDatabase db = databaseHelper.getWritableDatabase();

                    Thread.sleep(1000);// sleeps 1 second
                    //Do Your process here.
                    boolean resultUpdate = databaseHelper.updateData(getTitle, title, description, location, str_time, dateString, pendingID);
                   if (resultUpdate){
                       Toast.makeText(EditReminder.this, "Reminder updated", Toast.LENGTH_SHORT).show();
                       Intent intentSendBack = new Intent(EditReminder.this, MainActivity.class);
                       startActivity(intentSendBack);
                   }
                   else {
                       Toast.makeText(EditReminder.this, "Updation Failed", Toast.LENGTH_SHORT).show();
                   }

                } catch (final Exception ex) {
                    Log.i("---","Exception in thread");
                }
            }
        });

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




}