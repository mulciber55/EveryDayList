package com.example.android.everydaytasks.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.everydaytasks.data.TaskTable.TaskColumns;

import java.util.ArrayList;


public class TaskDataSource {

    //Reference to database, our TaskData Helper and all columns in table.
    private SQLiteDatabase db;
    private TaskDataHelper dbHelper;
    private String[] allColumns = {TaskColumns._ID, TaskColumns.TITLE, TaskColumns.CHECKED, TaskColumns.ORDER};

    //constructor create new TaskDataHelper
    public TaskDataSource(Context context) {
        dbHelper = new TaskDataHelper(context);
    }

    //method to open database connection
    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    //method to close database connection
    public void close() {
        dbHelper.close();
    }

    /**
     * Method create new task row in Database and new object.
     * @param title title of checkbox
     * @param checked checked/unchecked checkbnox
     * @param order witch one should be on list
     * @return created Task object
     */
    public Task createTask(String title, boolean checked, int order) {

        //put values to content value
        ContentValues values = new ContentValues();
        values.put(TaskColumns.TITLE, title);
        values.put(TaskColumns.CHECKED, checked);
        values.put(TaskColumns.ORDER, order);

        //put values into database and get row ID
        long newRowId;
        newRowId = db.insert(
                TaskTable.TABLE_NAME,
                null,
                values);
        //get cursor from added row
        Cursor cursor = db.query(TaskTable.TABLE_NAME,
                allColumns, TaskColumns._ID + " = " + newRowId, null,
                null, null, null);
        cursor.moveToFirst();

        //make object from cursor and return it
        Task newTask = cursorToTask(cursor);
        // make sure to close the cursor
        cursor.close();
        return newTask;
    }


    /**
     * Method delete row based on ID
     * @param task task witch should be removed
     */
    public void deleteTask(Task task) {
        long id = task.getId();
        db.delete(TaskTable.TABLE_NAME, TaskColumns._ID
                + " = " + id, null);
    }

    public void updateTask(Task task){
        ContentValues values = new ContentValues();
        values.put(TaskColumns.TITLE, task.getTitle());
        values.put(TaskColumns.CHECKED, task.isChecked());
        values.put(TaskColumns.ORDER, task.getOrder());
        db.update(TaskTable.TABLE_NAME, values,TaskColumns._ID + " = " + task.getId(),null);

    }

    /**
     * Method return all task from database
     * @return collection of all task in database
     */
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        Cursor cursor = db.query(TaskTable.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = cursorToTask(cursor);
            tasks.add(task);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tasks;
    }

    /**
     * make new Task object from cursor
     * @param cursor cursor contained taks from database
     * @return Task object from cursor
     */
    private Task cursorToTask(Cursor cursor) {
        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        boolean checked = (cursor.getInt(2) != 0);
        int order = cursor.getInt(3);

        return  new Task(
                id,
                title,
                checked,
                order
        );
    }
}
