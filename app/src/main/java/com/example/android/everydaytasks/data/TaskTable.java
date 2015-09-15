package com.example.android.everydaytasks.data;

import android.provider.BaseColumns;

public class TaskTable {

    public static final String TABLE_NAME = "task_table";

    public class TaskColumns implements BaseColumns{

        public static final String TITLE = "title";
        public static final String CHECKED = "is_checked";
        public static final String ORDER = "task_order";
    }

}
