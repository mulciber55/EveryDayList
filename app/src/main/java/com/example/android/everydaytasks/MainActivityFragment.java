package com.example.android.everydaytasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
        //references to elements from checkbox, preparation to animate
        final Button plusButtonEditStart = (Button) addTask.findViewById(R.id.plus_button_edit_start);
        final Button plusButtonEditStop = (Button) addTask.findViewById(R.id.plus_button_edit_stop);
        final EditText newTaskEditText = (EditText) addTask.findViewById(R.id.new_task_edit_text);
        final TextView addNewTaskTextBox = (TextView) addTask.findViewById(R.id.add_new_task_text_view);
        //should change Enter Key on keyboard to "+" (not working on my Samsung?)
        newTaskEditText.setImeActionLabel("+", KeyEvent.KEYCODE_ENTER);


        plusButtonEditStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                animateToRight(plusButtonEditStart,plusButtonEditStop,newTaskEditText,addNewTaskTextBox);
            }
        });

        plusButtonEditStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                animateToLeft(plusButtonEditStart, plusButtonEditStop, newTaskEditText, addNewTaskTextBox);
            }
        });

        //custom listener to Enter Key, it works the same way as plusButtonEditStop
        newTaskEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    animateToLeft(plusButtonEditStart, plusButtonEditStop, newTaskEditText, addNewTaskTextBox);
                    return true;
                }
                return false;
            }
        });

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
     * Method animate adding checkbox
     *
     *@param plusButtonEditStart button on left, slide to right and hide
     *@param plusButtonEditStop button shows after start button slide
     *@param newTaskEditText EditText show after slide
     *@param addNewTaskTextBox Text VIew hide after slide
     *
     */
    public void animateToRight(final Button plusButtonEditStart, final Button plusButtonEditStop, final EditText newTaskEditText, final TextView addNewTaskTextBox) {

        //reference to Animation
        final Animation slideAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.plus_button_move_to_right);

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
                //show keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInputFromWindow(newTaskEditText.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                newTaskEditText.requestFocus();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        //Start animation
        plusButtonEditStart.startAnimation(slideAnimation);
    }


    /**
     * Method animate adding checkbox
     *
     *@param plusButtonEditStart button on left shows after stop button slide
     *@param plusButtonEditStop button slide to left and hide
     *@param newTaskEditText EditText hide after slide
     *@param addNewTaskTextBox Text VIew show after slide
     *
     */

    public void animateToLeft(final Button plusButtonEditStart, final Button plusButtonEditStop, final EditText newTaskEditText, final TextView addNewTaskTextBox) {
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

        String textToInsertInCheckbox = newTaskEditText.getText().toString();
        //don't allow to add empty task
        if (textToInsertInCheckbox.isEmpty()) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.empty_task_toast), Toast.LENGTH_LONG).show();
        } else {
            //hide keyboard
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(newTaskEditText.getWindowToken(), 0);
            //hide EditText (looks beater that way)
            newTaskEditText.setVisibility(View.INVISIBLE);
            //Start animation
            plusButtonEditStop.startAnimation(slideAnimation);
            //add new checkbox
            taskList.add(textToInsertInCheckbox);
            //erase text from EditBox
            newTaskEditText.clearComposingText();
            newTaskEditText.setText("");
        }
    }


}



