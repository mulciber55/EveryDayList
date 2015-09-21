package com.example.android.everydaytasks.data;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.everydaytasks.R;

/**
 * Created by borys on 21.09.2015.
 * Class with animations
 */
public class ButtonAnimations {
    private Context mContext;
    public ButtonAnimations(Context context){
        mContext = context;
    }

    /**
     * Method animate adding checkbox
     *
     * @param plusButtonEditStart button on left, slide to right and hide
     * @param plusButtonEditStop  button shows after start button slide
     * @param newTaskEditText     EditText show after slide
     * @param addNewTaskTextBox   Text VIew hide after slide
     */
    public void animateToRight(final Button plusButtonEditStart, final Button plusButtonEditStop, final EditText newTaskEditText, final TextView addNewTaskTextBox) {

        //reference to Animation
        final Animation slideAnimation = AnimationUtils.loadAnimation(mContext, R.anim.plus_button_move_to_right);

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
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
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
     * @param plusButtonEditStart button on left shows after stop button slide
     * @param plusButtonEditStop  button slide to left and hide
     * @param newTaskEditText     EditText hide after slide
     * @param addNewTaskTextBox   Text VIew show after slide
     */
    public String animateToLeft(final Button plusButtonEditStart, final Button plusButtonEditStop, final EditText newTaskEditText, final TextView addNewTaskTextBox) {
        //reference to Animation
        final Animation slideAnimation = AnimationUtils.loadAnimation(mContext, R.anim.plus_button_move_to_left);
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
            return textToInsertInCheckbox;
        } else {
            //hide keyboard
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(newTaskEditText.getWindowToken(), 0);
            //hide EditText (looks beater that way)
            newTaskEditText.setVisibility(View.INVISIBLE);
            //Start animation
            plusButtonEditStop.startAnimation(slideAnimation);
            //add new checkbox
            //erase text from EditBox
            newTaskEditText.clearComposingText();
            newTaskEditText.setText("");
            return textToInsertInCheckbox;
        }
    }

}
