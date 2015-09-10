package com.example.android.everydaytasks.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.android.everydaytasks.data.TaskTable.TaskColumns;

public class TaskDataHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "everyday.db";

    public TaskDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE " + TaskTable.TABLE_NAME + " (");
            sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sb.append(TaskColumns.TITLE + " TEXT NOT NULL, ");
            sb.append(TaskColumns.CHECKED + " INTEGER, ");
            sb.append(TaskColumns.ORDER + " INTEGER ");
            sb.append(");");
            db.execSQL(sb.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TaskDataHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TaskTable.TABLE_NAME);
        onCreate(db);
    }
}
