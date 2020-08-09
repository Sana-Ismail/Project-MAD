package com.example.reminder_app;

import android.provider.BaseColumns;

public class DatabaseContract {
    DatabaseContract(){
    }

    // inner class that defines table contents
    public static abstract class Reminder implements BaseColumns{
        public static final String TABLE_NAME = "reminderInfo";
        public static final String COL_TITLE = "reminderTitle";
        public static final String COL_Description = "reminderDescription";
        public static final String COL_TIME = "reminderTime";
        public static final String COL_DATE = "reminderDate";
        public static final String COL_LOCATION = "reminderLocation";
        public static final String COL_PENDING_ID = "idPendingIntent";

        public static final String TABLE_NAME2 = "reminderInfo2";
        public static final String COL_TITLE2 = "reminderTitle2";
        public static final String COL_TIME2 = "reminderTime2";
        public static final String COL_DATE2 = "reminderDate2";
        public static final String COL_PENDING_ID2 = "idPendingIntent2";
    }

}
