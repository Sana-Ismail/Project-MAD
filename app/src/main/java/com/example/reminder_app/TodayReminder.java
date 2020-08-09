package com.example.reminder_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.reminder_app.DatabaseContract.Reminder.TABLE_NAME;

public class TodayReminder extends AppCompatActivity {

    ListView listView;
    TextView no_reminder;

    DatabaseHelpers databaseHelper;

    MyAdapters myAdapter;

    ArrayList<String> titlE = new ArrayList<>();
    ArrayList<String>descriptioN = new ArrayList<>();
    ArrayList<String>timE = new ArrayList<>();
    ArrayList<String>datE = new ArrayList<>();
    ArrayList<String>locatioN = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_reminder);

        databaseHelper = new DatabaseHelpers(this);


        listView = findViewById(R.id.listvieww);
        myAdapter = new MyAdapters(this, titlE, descriptioN, timE, datE, locatioN);
        listView.setAdapter(myAdapter);
        no_reminder = findViewById(R.id.no_reminders);
        updateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TodayReminder.this, titlE.get(position), Toast.LENGTH_SHORT).show();        ///titlE[position]
                editData(titlE.get(position));
            }
        });


        // step-1: make a menu resource file
        // step-2: register view for contextmenu
        registerForContextMenu(listView);
        // step-3: onCreateContextMenu
        // step-4: onContextItemSelected
    }




    // ******** >>>  update the list view by retrieving data from data base and displaying in listview   <<<<<<<************
    public void updateList(){



        Intent intentGetBack = new Intent();
        int numberOf_reminders = intentGetBack.getIntExtra("count", 0);

        SQLiteDatabase sqLiteDatabase;
        sqLiteDatabase = databaseHelper.getReadableDatabase();

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        // search for that id which is numberOf_reminders
        //String query = "select * from " + TABLE_NAME + " where " + DatabaseContract.Reminder._ID + " = '" + numberOf_reminders + "'";
        String query = "select * from " + TABLE_NAME + " where " + DatabaseContract.Reminder.COL_DATE + " = '" + currentDate + "'";
        Cursor c = sqLiteDatabase.rawQuery(query, null);

        if (c.getCount()==0){
           no_reminder = findViewById(R.id.no_reminder);   // we initialize both of these here to get rid of null pointer exception.
           // listView = findViewById(R.id.listvieww);
            Toast.makeText(this, "No data of date " + currentDate, Toast.LENGTH_SHORT).show();
            no_reminder.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
        else{
            while (c.moveToNext()){
                // add to listview
                titlE.add(c.getString(1));
                descriptioN.add(c.getString(2));
                timE.add(c.getString(3));
                datE.add(c.getString(4));
                locatioN.add(c.getString(5));
            }
        }

    }





    // ********************** >>>>>>  Create custom adapter for custom listView   <<<<<<<************
    class MyAdapters extends ArrayAdapter<String> {
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


        MyAdapters(Context c , ArrayList<String>titles, ArrayList<String>descriptions, ArrayList<String>times , ArrayList<String>dates, ArrayList<String>locations){    //String titles[], String descriptions[], String times[] , String dates[], String locations[]
            super(c,R.layout.row, R.id.title, titles);

            this.context = c;    //this.context = c;
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

                titlE.remove(position_item);
                descriptioN.remove(position_item);
                timE.remove(position_item);
                datE.remove(position_item);
                locatioN.remove(position_item);
                myAdapter.notifyDataSetChanged();

                //updateList();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }



    // ******** >>>  Method used in edit option   <<<<<<<************
    public void editData(String name){

        Toast.makeText(this, "inside editdata", Toast.LENGTH_SHORT).show();
        String etitle = "";
        String edescription ="";
        String etime = "";
        String edate = "";
        String elocation = "";

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
        }

        //Toast.makeText(this, etitle + ", " + edescription +", " + etime +", " + edate +", " + elocation, Toast.LENGTH_SHORT).show();

        Intent intentEdit = new Intent(this, EditReminder.class);
        intentEdit.putExtra("title", etitle);
        intentEdit.putExtra("desc", edescription);
        intentEdit.putExtra("time", etime);
        intentEdit.putExtra("date", edate);
        intentEdit.putExtra("loc", elocation);
        startActivity(intentEdit);
    }



}