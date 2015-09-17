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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.everydaytasks.data.Task;
import com.example.android.everydaytasks.data.TaskDataSource;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    CheckBoxAdapter checkboxFragmentAdapter;
    ArrayList<Task> taskList;
    TaskDataSource myTaskDatabase;
    View rootView;
    ListView listView;
    LinearLayout addTask_on_layout;
    LinearLayout addTask_on_foot;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myTaskDatabase = new TaskDataSource(getActivity());
        myTaskDatabase.open();
        taskList = myTaskDatabase.getAllTasks();

        // Add this line in order for this fragment to handle menu events.
        // setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        checkboxFragmentAdapter = new CheckBoxAdapter(
                getActivity(),
                taskList);
        //connect this fragment class with xml file

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //make reference to List View
        listView = (ListView) rootView.findViewById(R.id.main_list_view);
        //set adapter
        listView.setAdapter(checkboxFragmentAdapter);
        addTask_on_layout = (LinearLayout) rootView.findViewById(R.id.add_task_on_fragment);
        addTask_on_foot = (LinearLayout)inflater.inflate(R.layout.add_task, container, false);



        if(taskList.size() != 0){
            listView.setVisibility(View.VISIBLE);
            listView.addFooterView(addTask_on_foot);
            listenButtons(addTask_on_foot);
        }else {
            listView.setVisibility(View.GONE);
            addTask_on_layout.setVisibility(View.VISIBLE);
            listenButtons(addTask_on_layout);
        }


        return rootView;
    }

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
            checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    task.setChecked(isChecked);
                    myTaskDatabase.updateTask(task);
                }
            });
            return checkBox;
        }



    }

    public void listenButtons(LinearLayout addTask){

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
            //if that was first checkbox switch to another view.
            if(taskList.size() == 0){
                addTask_on_layout.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                listView.addFooterView(addTask_on_foot);
                listenButtons(addTask_on_foot);
            }
            //add new checkbox
            Task quickTask = myTaskDatabase.createTask(textToInsertInCheckbox, false, 0);
            taskList.add(quickTask);
            //erase text from EditBox
            newTaskEditText.clearComposingText();
            newTaskEditText.setText("");
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        myTaskDatabase.close();
    }


}



