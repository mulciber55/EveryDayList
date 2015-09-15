package com.example.android.everydaytasks.data;
/**
 * Simple Model Base prototype
 * Created by borys_000 on 2015-09-08.
 */
public class Task {

    public int id;
    public String title;
    public String category;
    public boolean checked;
    public int order;

    Task(int id, String title, boolean checked, int order){
        this.id = id;
        this.title = title;
        this.checked = checked;
        this.order = order;
    }

}

