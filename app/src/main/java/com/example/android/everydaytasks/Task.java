package com.example.android.everydaytasks;

/**
 * Simple Model Base prototype
 * Created by borys_000 on 2015-09-08.
 */
public class Task {

    public String title;
    public String category;
    public boolean checked;
    public String when;
    public byte order;

Task(String title, String category, boolean checked, String when){
    this.title = title;
    this.category = category;
    this.checked = checked;
    this.when = when;
}

}
