package com.example.android.everydaytasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    ArrayAdapter<String> listFragmentAdapter;
    ArrayList<String> taskList = new ArrayList<String>(Arrays.asList("Task 1", "Task 2"));

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listFragmentAdapter = new CheckBoxAdapter(
                getActivity(),
                taskList);
        //connect this fragment class with xml file
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //make reference to List View
        ListView listView = (ListView) rootView.findViewById(R.id.main_list_view);
        listView.setAdapter(listFragmentAdapter);
        //add footer
        LinearLayout addTask = (LinearLayout) inflater.inflate(R.layout.add_task, container, false);
        listView.addFooterView(addTask);
        listenButtons(addTask);

        return rootView;
    }

    /**
     * Custom Adapter connecting arrayList od Strings with CheckBox
     */
    public class CheckBoxAdapter extends ArrayAdapter<String> {

        public CheckBoxAdapter(Context context, ArrayList<String> boxes) {
            super(context, 0, boxes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.check_box, parent, false);
            }
            CheckBox checkBox = (CheckBox) convertView;
            String checkBoxText = getItem(position);
            checkBox.setText(checkBoxText);
            return checkBox;
        }


    }

    /**
     * Method add new checkbox wen plus button is press.
     *
     * @param layout get layout with buttons
     */
    public void listenButtons(LinearLayout layout) {

        //references to UI
        final Button plusButtonEditStart = (Button) layout.findViewById(R.id.plus_button_edit_start);
        final Button plusButtonEditStop = (Button) layout.findViewById(R.id.plus_button_edit_stop);
        final EditText newTaskEditText = (EditText) layout.findViewById(R.id.new_task_edit_text);
        final TextView addNewTaskTextBox = (TextView) layout.findViewById(R.id.add_new_task_text_view);


        plusButtonEditStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //reference to Animation
                final Animation slideAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.plus_button_move_to_right);
                //Listener to change Visible of UI elements after animation
                slideAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        plusButtonEditStart.setVisibility(View.GONE);
                        plusButtonEditStop.setVisibility(View.VISIBLE);
                        addNewTaskTextBox.setVisibility(View.GONE);
                        newTaskEditText.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                //Start animation
                v.startAnimation(slideAnimation);
            }
        });

        plusButtonEditStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //reference to Animation
                final Animation slideAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.plus_button_move_to_left);
                //Listener to change Visible of UI elements after animation
                slideAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        plusButtonEditStart.setVisibility(View.VISIBLE);
                        plusButtonEditStop.setVisibility(View.GONE);
                        addNewTaskTextBox.setVisibility(View.VISIBLE);
                        newTaskEditText.setVisibility(View.GONE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                //check if user type title of Task
                String textToInsertInCheckbox = newTaskEditText.getText().toString();
                if (textToInsertInCheckbox.isEmpty()) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.empty_task_toast), Toast.LENGTH_LONG).show();
                } else {
                    //Start animation
                    v.startAnimation(slideAnimation);
                    //add new checkbox
                    taskList.add(textToInsertInCheckbox);
                    newTaskEditText.setText("");
                }

            }
        });

    }


}



