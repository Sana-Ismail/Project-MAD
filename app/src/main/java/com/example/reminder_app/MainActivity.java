package com.example.reminder_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.reminder_app.DatabaseContract.Reminder.COL_LOCATION;
import static com.example.reminder_app.DatabaseContract.Reminder.TABLE_NAME;


//  add some fonts
// * do some gradients
// * context menu for listviwew
// * make custom button
// * seperate listview
// * round icon
// * change date style and row width
// check null and incorrect values
// * check for multiple notifications
// get id of a data
// actionbar fonts

public class MainActivity extends AppCompatActivity  {

    ListView listView;
    FloatingActionButton fab;

    DatabaseHelpers databaseHelper;


    MyAdapter myAdapter;

    ArrayList<String>titlE = new ArrayList<>();
    ArrayList<String>descriptioN = new ArrayList<>();
    ArrayList<String>timE = new ArrayList<>();
    ArrayList<String>datE = new ArrayList<>();
    ArrayList<String>locatioN = new ArrayList<>();

    //ArrayList<String>idPending = new ArrayList<>();
    List<Integer>idpending = new ArrayList<Integer>();

// ****************** >>>>>>>  OptionsMenu   <<<<<<<*****************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;

//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.options_menu, menu);
//        return true;
    }


    // ******** >>>  Click events on optionsMenu   <<<<<<<************
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.today:
                Toast.makeText(this, "Today's Reminders", Toast.LENGTH_SHORT).show();
                Intent intentToday = new Intent(this, TodayReminder.class);
                startActivity(intentToday);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


//*****************************************************************************************************************
//*****************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelpers(this);

        updateList();

        listView = findViewById(R.id.listview);
        myAdapter = new MyAdapter(this, titlE, descriptioN, timE, datE, locatioN);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, titlE.get(position), Toast.LENGTH_SHORT).show();        ///titlE[position]
                editData(titlE.get(position));
            }
        });

        // step-1: make a menu resource file
        // step-2: register view for contextmenu
        registerForContextMenu(listView);
        // step-3: onCreateContextMenu
        // step-4: onContextItemSelected

         fab = findViewById(R.id.fab_btn);
         final Intent intent_addreminder = new Intent(this, MainActivity2_addReminder.class);
         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(intent_addreminder);
             }
         });

         //deleteRecords();

    }
//***********************>>>>>  onCreate ends here   <<<<<********************************************************************************




    // ******** >>>  update the list view by retrieving data from data base and displaying in listview   <<<<<<<************
    public void updateList(){
        Intent intentGetBack = new Intent();
        int numberOf_reminders = intentGetBack.getIntExtra("count", 0);
        SQLiteDatabase sqLiteDatabase;
        sqLiteDatabase = databaseHelper.getReadableDatabase();

        // search for that id which is numberOf_reminders
        //String query = "select * from " + TABLE_NAME + " where " + DatabaseContract.Reminder._ID + " = '" + numberOf_reminders + "'";
        String query = "select * from " + TABLE_NAME ;

        Cursor c = sqLiteDatabase.rawQuery(query, null);

        if (c.getCount()==0){
            Toast.makeText(this, "No data of serial " + numberOf_reminders, Toast.LENGTH_SHORT).show();
        }

        while (c.moveToNext()){
            // add to listview
            titlE.add(c.getString(1));
            descriptioN.add(c.getString(2));
            timE.add(c.getString(3));
            datE.add(c.getString(4));
            locatioN.add(c.getString(5));
            idpending.add(c.getInt(6));
        }
    }


    //*****************************************************************************************************************
    //*****************************************************************************************************************
    // ********************** >>>>>>  Create custom adapter for custom listView   <<<<<<<************
     class MyAdapter extends ArrayAdapter<String>{
        Context context;
        ArrayList<String>title = new ArrayList<>();
        ArrayList<String>description = new ArrayList<>();
        ArrayList<String>time = new ArrayList<>();
        ArrayList<String>date = new ArrayList<>();
        ArrayList<String>location = new ArrayList<>();
        /*String title[];
        String description[];
        String time[];
        String date[];
        String location[];  */


        MyAdapter(Context c , ArrayList<String>titles, ArrayList<String>descriptions, ArrayList<String>times , ArrayList<String>dates, ArrayList<String>locations){    //String titles[], String descriptions[], String times[] , String dates[], String locations[]
            super(c,R.layout.row, R.id.title, titles);

            this.context = c;
            this.title = titles;  //this.title.add(titles);
            this.description = descriptions;
            this.time = times;
            this.date = dates;
            this.location = locations;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row  = layoutInflater.inflate(R.layout.row, parent, false);
            TextView rtitle = row.findViewById(R.id.title);
            TextView rdescrription = row.findViewById(R.id.description);
            TextView rtime = row.findViewById(R.id.time);
            TextView rdate = row.findViewById(R.id.date);
            TextView rlocation = row.findViewById(R.id.location);

            // setting resources on view
            rtitle.setText(title.get(position));    //rtitle.setText(title[position]);
            rdescrription.setText(description.get(position));
            rtime.setText(time.get(position));
            rdate.setText(date.get(position));
            rlocation.setText(location.get(position));

            return row;
        }
    }


    //*****************************************************************************************************************
    //*****************************************************************************************************************
    // ******************* >>>>>>>>>  Inflate menu on contextMenu   <<<<<<<<<*********************
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    // ******** >>>  Click events on contextMenu   <<<<<<<************
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.edit_option:
                //Toast.makeText(this, "You chose to edit: " + ((TextView)info.targetView).getText().toString(), Toast.LENGTH_SHORT).show();
                int id_positions = info.position;
                String getNamee = titlE.get(id_positions);
                Toast.makeText(this, "position: " + id_positions, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "title " + getNamee, Toast.LENGTH_SHORT).show();
                editData(getNamee);
                return true;
//                                           *********
            case R.id.delete_option:

                //Toast.makeText(this, "You chose to delete: " + ((TextView)info.targetView).getText().toString(), Toast.LENGTH_SHORT).show();
                // alert box
                SQLiteDatabase sqLiteDatabase;
                sqLiteDatabase = databaseHelper.getWritableDatabase();

                long id_item = info.id;
                Toast.makeText(this, "Item id:" + id_item, Toast.LENGTH_SHORT).show();

                int position_item = info.position;
                Toast.makeText(this, "position: "+ position_item, Toast.LENGTH_SHORT).show();

                String name = titlE.get(position_item);
                Toast.makeText(this, "name: "+ name, Toast.LENGTH_SHORT).show();

                cancelReminder(name, position_item);
                //int id_position = info.position;     // position of the item in listView
                //String str_id = ""+id_position;
                //Toast.makeText(this, "position: " + id_position, Toast.LENGTH_SHORT).show();
                String whereClause = DatabaseContract.Reminder.COL_TITLE + " = ?";
                String[] whereArgs = {name};

                int affectedRows = sqLiteDatabase.delete(DatabaseContract.Reminder.TABLE_NAME, whereClause, whereArgs);
                Toast.makeText(this, "Rows affected " + affectedRows, Toast.LENGTH_SHORT).show();

                if (affectedRows>0){
                    Toast.makeText(this, "Record has been deleted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Couldn't delete "+ position_item + ", " + name, Toast.LENGTH_SHORT).show();
                }

                //updateList();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    //*****************************************************************************************************************
    //*****************************************************************************************************************
    // ******** >>>  Method used in edit option   <<<<<<<************
    public void editData(String name){

        Toast.makeText(this, "inside editdata", Toast.LENGTH_SHORT).show();
        String etitle = "";
        String edescription ="";
        String etime = "";
        String edate = "";
        String elocation = "";
        int pendingId = 0;

        SQLiteDatabase sqLiteDatabase;
        sqLiteDatabase = databaseHelper.getReadableDatabase();

        String query = "select * from " + TABLE_NAME + " where " + DatabaseContract.Reminder.COL_TITLE + " = '" + name + "'";
        Cursor c = sqLiteDatabase.rawQuery(query, null);

        if (c.getCount()==0){
            Toast.makeText(this, "No data of title " + name, Toast.LENGTH_SHORT).show();
        }

        while (c.moveToNext()){
            etitle = c.getString(1);
            edescription = c.getString(2);
            etime = c.getString(3);
            edate = c.getString(4);
            elocation = c.getString(5);
            pendingId = c.getInt(6);
        }

        //Toast.makeText(this, etitle + ", " + edescription +", " + etime +", " + edate +", " + elocation, Toast.LENGTH_SHORT).show();

        Intent intentEdit = new Intent(this, EditReminder.class);
        intentEdit.putExtra("title", etitle);
        intentEdit.putExtra("desc", edescription);
        intentEdit.putExtra("time", etime);
        intentEdit.putExtra("date", edate);
        intentEdit.putExtra("loc", elocation);
        //intentEdit.putExtra("id", pendingId);
        startActivity(intentEdit);
    }


    //*****************************************************************************************************************
//*****************************************************************************************************************
    // ***************** >>>>>>>>>  Method used in delete option   <<<<<<<<<<<*******************
    public void cancelReminder(String names, int position_item){
        Toast.makeText(this, "inside cancelReminder", Toast.LENGTH_SHORT).show();
        String etitle = "";
        String edescription ="";
        String etime = "";
        String edate = "";
        String elocation = "";
        int pendingId = 0;

        SQLiteDatabase sqLiteDatabase;
        sqLiteDatabase = databaseHelper.getReadableDatabase();

        String query = "select * from " + TABLE_NAME + " where " + DatabaseContract.Reminder.COL_TITLE + " = '" + names + "'";
        Cursor c = sqLiteDatabase.rawQuery(query, null);

        if (c.getCount()==0){
            Toast.makeText(this, "No data of title " + names, Toast.LENGTH_SHORT).show();
        }

        while (c.moveToNext()){
            etitle = c.getString(1);
            edescription = c.getString(2);
            etime = c.getString(3);
            edate = c.getString(4);
            elocation = c.getString(5);
            pendingId = c.getInt(6);
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, Alertreciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pendingId, intent, 0);
        alarmManager.cancel(pendingIntent);

        titlE.remove(position_item);
        descriptioN.remove(position_item);
        timE.remove(position_item);
        datE.remove(position_item);
        locatioN.remove(position_item);
        //idpending.remove(position_item);
        myAdapter.notifyDataSetChanged();
    }

   /* public void deleteRecords(){
        String whereClause = DatabaseContract.Reminder._ID + " = ?";
        String[] whereArgs = {"4"};

        SQLiteDatabase sqLiteDatabase;
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
        Toast.makeText(this, "Rows affected: " + result, Toast.LENGTH_SHORT).show();

        if (result>0){
           Toast.makeText(this, "Records have been deleted", Toast.LENGTH_SHORT).show();
       }
    }  */

}