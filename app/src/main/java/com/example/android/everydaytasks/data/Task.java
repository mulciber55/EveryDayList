package com.example.android.everydaytasks.data;

/**
 * Simple Model Base prototype
 * Created by borys_000 on 2015-09-08.
 */
public class Task {



    private int mId;
    private String mTitle;
    private boolean mChecked;
    private int mOrder;

    public void setId(int id) {
        mId = id;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public void setOrder(int order) {
        mOrder = order;
    }


    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public int getOrder() {
        return mOrder;
    }

    Task(int id, String title, boolean checked, int order){
        this.mId = id;
        this.mTitle = title;
        this.mChecked = checked;
        this.mOrder = order;
    }

}

