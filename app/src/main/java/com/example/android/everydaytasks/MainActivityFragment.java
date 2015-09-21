package com.example.android.everydaytasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.everydaytasks.data.ButtonAnimations;
import com.example.android.everydaytasks.data.Task;
import com.example.android.everydaytasks.data.TaskDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private CheckBoxAdapter mCheckboxFragmentAdapter;
    private ArrayList<Task> taskList;
    private TaskDataSource mTaskDatabase;
    private View rootView;
    private RecyclerView mTasksRecyclerView;
    private CheckBoxAdapter mAdpter;
    private LinearLayout addTask_on_layout;
    private ButtonAnimations mButtonAnimations;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskDatabase = new TaskDataSource(getActivity());
        mTaskDatabase.open();
        mButtonAnimations = new ButtonAnimations(getActivity());


        // Add this line in order for this fragment to handle menu events.
        // setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //connect this fragment class with xml file

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //make reference to RecyclerView
        mTasksRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler_view);
        //Give Layout Manager to RecyclerView
        mTasksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //set adapter
        updateUI();
        addTask_on_layout = (LinearLayout) rootView.findViewById(R.id.add_task_on_fragment);
        listenButtons(addTask_on_layout);

        return rootView;
    }

    private void updateUI() {
        taskList = mTaskDatabase.getAllTasks();
        mAdpter = new CheckBoxAdapter(taskList);
        mTasksRecyclerView.setAdapter(mAdpter);
    }

    public void listenButtons(LinearLayout addTask) {

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
                mButtonAnimations.animateToRight(plusButtonEditStart, plusButtonEditStop, newTaskEditText, addNewTaskTextBox);
            }
        });

        plusButtonEditStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String textToInsertInCheckbox = mButtonAnimations.animateToLeft(plusButtonEditStart, plusButtonEditStop, newTaskEditText, addNewTaskTextBox);
                if (textToInsertInCheckbox.isEmpty()){
                        Toast.makeText(getActivity(), getActivity().getString(R.string.empty_task_toast), Toast.LENGTH_LONG).show();
                    }else {
                        Task quickTask = mTaskDatabase.createTask(textToInsertInCheckbox, false, 0);
                        taskList.add(quickTask);
                }
            }
        });

        //custom listener to Enter Key, it works the same way as plusButtonEditStop
        newTaskEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String textToInsertInCheckbox = mButtonAnimations.animateToLeft(plusButtonEditStart, plusButtonEditStop, newTaskEditText, addNewTaskTextBox);
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (textToInsertInCheckbox.isEmpty()){
                        Toast.makeText(getActivity(), getActivity().getString(R.string.empty_task_toast), Toast.LENGTH_LONG).show();
                    }else {
                        Task quickTask = mTaskDatabase.createTask(textToInsertInCheckbox, false, 0);
                        taskList.add(quickTask);
                    }
                    return true;
                }
                return false;
            }
        });

    }


    private class TaskHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private CheckBox mCheckBox;
        private Task mTask;

        public TaskHolder(View itemView) {
            super(itemView);
            //set Listener on whole item View
            itemView.setOnClickListener(this);
            //find checkbox on View
            mCheckBox = (CheckBox) itemView.findViewById(R.id.simple_checkbox);
        }

        //Bind data with views
        public void bindTask(Task task) {
            mTask = task;
            mCheckBox.setText(mTask.getTitle());
            mCheckBox.setChecked(mTask.isChecked());
        }

        //listener holding checkBox checking
        @Override
        public void onClick(View v) {
            CheckBox checkBox = (CheckBox) v;
            mTask.setChecked(checkBox.isChecked());
            mTaskDatabase.updateTask(mTask);
            updateUI();
        }
    }


    /**
     * Custom Adapter connecting Tasks list to CheckBox
     */
    private class CheckBoxAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<com.example.android.everydaytasks.data.Task> mTasks;

        public CheckBoxAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        //Bind XML Layout for single element and make ViewHolder with it.
        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from((getActivity()));
            View view = layoutInflater.inflate(R.layout.check_box, parent, false);
            return new TaskHolder(view);
        }

        //create Item view from current position Task
        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bindTask(task);
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTaskDatabase.close();
    }


}



