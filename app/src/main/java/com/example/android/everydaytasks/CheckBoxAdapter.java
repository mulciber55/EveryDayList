package com.example.android.everydaytasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.android.everydaytasks.data.Task;
import com.example.android.everydaytasks.data.TaskDataSource;

import java.util.ArrayList;

/**
 * Custom Adapter connecting arrayList od Strings with CheckBox
 */
public class CheckBoxAdapter extends ArrayAdapter<Task> {

    public CheckBoxAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.check_box, parent, false);
        }
        CheckBox checkBox = (CheckBox) convertView;
        final Task task = getItem(position);
        String title = task.getTitle();
        boolean isChecked = task.isChecked();
        checkBox.setText(title);
        checkBox.setChecked(isChecked);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TaskDataSource taskDatabase =  new TaskDataSource(getContext());
                taskDatabase.open();
                task.setChecked(isChecked);
                taskDatabase.updateTask(task);
                taskDatabase.close();
            }
        });
        return checkBox;
    }



}