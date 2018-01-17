package com.example.android.falsemythsquiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // Keeps track of the score.
    int totalPoints = 0;

    // Keeps the number of checkboxes that are checked.
    int numberCheckedCheckbox = 0;

    // Declaring the class and view variable for sound FX playback.
    SoundPool mySound;
    int choiceClick;
    int bugSound;
    int quizComplete;

    // Declarations for the listen & spell question.
    Button soundButton;
    EditText userTextInput;

    // Declaring the RadioGroups. For some reason I don't yet understand, I read that they have to be declared here.
    private RadioGroup q1Group;
    private RadioGroup q2Group;
    private RadioGroup q3Group;
    private RadioGroup q4Group;
    private RadioGroup q5Group;
    private RadioGroup q6Group;
    private RadioGroup q9Group;

    // Declaring Checkboxes for later use.
    CheckBox checkBanana;
    CheckBox checkSpinach;
    CheckBox checkAlmond;
    CheckBox checkCarrot;

    // This variable helps with keeping track of the unanswered questions.
    int answerMissing = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Matching the RadioGroups with XML counterparts.
        q1Group = findViewById(R.id.question_group1);
        q2Group = findViewById(R.id.question_group2);
        q3Group = findViewById(R.id.question_group3);
        q4Group = findViewById(R.id.question_group4);
        q5Group = findViewById(R.id.question_group5);
        q6Group = findViewById(R.id.question_group6);
        q9Group = findViewById(R.id.question_group9);

        // Matching the Checkboxes with XML counterparts.
        checkBanana = findViewById(R.id.checkbox_banana);
        checkSpinach = findViewById(R.id.checkbox_spinach);
        checkAlmond = findViewById(R.id.checkbox_almond);
        checkCarrot = findViewById(R.id.checkbox_carrot);

        // Matching views for listen & spell question.
        soundButton = findViewById(R.id.sound_button);
        userTextInput = findViewById(R.id.text_input);

        // This creates a "listener" thingy that waits for an onClick event to happen.
        soundButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        mySound.play(bugSound, .25f, .25f, 1, 0, 1);
                    }
                });


        // Initializing the SoundPool class and loading the sound file matching it to choiceClick variable.
        mySound = new SoundPool.Builder().setMaxStreams(2).build();
        choiceClick = mySound.load(this, R.raw.click, 1);
        bugSound = mySound.load(this, R.raw.cicada, 1);
        quizComplete = mySound.load(this, R.raw.complete, 1);
    }

    // When on of the checkboxes are clicked. This method runs.
    public void onCheckboxClick(View view) {

        mySound.play(choiceClick, .5f, .5f, 1, 0, 2);

        // To check if max number of checkboxes are checked or not.
        Boolean maxChecked = false;

        // Establishing "checked" status.
        Boolean checked = ((CheckBox) view).isChecked();

        // To be able to use Checkbox methods (ie. setChecked), I had to convert ViewId(int) to Checkbox object.
        CheckBox theCheckbox = whichCheckbox(view.getId());

        // Kind of an unnecessarily complex way to make sure no more than 2 checkboxes are checked.
        if ((numberCheckedCheckbox >= 2) && checked) {
            theCheckbox.setChecked(false);
            maxChecked = true;
            // theCheckbox.toggle();
            Toast.makeText(this, "You can check two boxes at most", Toast.LENGTH_SHORT).show();
        } else if (!checked) {
            theCheckbox.setChecked(true);
            theCheckbox.toggle();
        }

        checked = theCheckbox.isChecked();

        if (checked) {
            numberCheckedCheckbox++;
        } else if (!checked && maxChecked) {

        } else {
            numberCheckedCheckbox--;
        }
    }

    // Converting View IDs to Checkbox object.
    private CheckBox whichCheckbox(int checkedId) {

        if (checkedId == R.id.checkbox_banana) {
            return checkBanana;

        } else if (checkedId == R.id.checkbox_spinach) {
            return checkSpinach;

        } else if (checkedId == R.id.checkbox_almond) {
            return checkAlmond;

        } else if (checkedId == R.id.checkbox_carrot) {
            return checkCarrot;
        } else
            return null;

    }

    // Method for playing the sound file when any RadioButton is touched.
    public void onTouch(View view) {
        mySound.play(choiceClick, .5f, .5f, 1, 0, 2);
    }

    public void onScoreClick(View view) {

        // Play the sound matched to choiceClick.
        mySound.play(choiceClick, .5f, .5f, 1, 0, 1);

        // Getting the checked RadioButton for each RadioGroup and then checking if the answer is correct, for each question.
        // Also, different points are given depending on the type of question (explained on the results page).
        int q1Id = q1Group.getCheckedRadioButtonId();
        totalPoints += calculateScore(q1Id, R.id.q1_white, false);

        int q2Id = q2Group.getCheckedRadioButtonId();
        totalPoints += calculateScore(q2Id, R.id.q2_false, true);

        int q3Id = q3Group.getCheckedRadioButtonId();
        totalPoints += calculateScore(q3Id, R.id.q3_sour_rice, false);

        int q4Id = q4Group.getCheckedRadioButtonId();
        totalPoints += calculateScore(q4Id, R.id.q4_true, true);

        int q5Id = q5Group.getCheckedRadioButtonId();
        totalPoints += calculateScore(q5Id, R.id.q5_japan, false);

        int q6Id = q6Group.getCheckedRadioButtonId();
        totalPoints += calculateScore(q6Id, R.id.q6_false, true);

        int q9Id = q9Group.getCheckedRadioButtonId();
        totalPoints += calculateScore(q9Id, R.id.q9_competency, false);

        // Checkbox question points checked. 5 Points for each correct checkbox.
        if (checkAlmond.isChecked())
            totalPoints += 5;

        if (checkSpinach.isChecked())
            totalPoints += 5;

        // Name the insect question points delivered.
        if (userTextInput.getText().toString().toLowerCase().equals("cicada"))
            totalPoints += 20;

        // Text input unanswered question flags.
        if (userTextInput.getText().toString().trim().length() > 1)
            answerMissing += 1;

        // Checkbox unanswered question flags.
        if (numberCheckedCheckbox > 0)
            answerMissing += 1;

        // This is to check if there are unanswered questions. And if yes, then show a Toast message warning.
        if (answerMissing < 9 && numberCheckedCheckbox == 1) {
            Toast.makeText(this, (9 - answerMissing) + " more to go!\n\n1 answer missing in question 7\n\nPlease answer all the questions :)", Toast.LENGTH_SHORT).show();
            totalPoints = 0;
            answerMissing = 0;
            return;
        } else if (answerMissing < 9) {
            Toast.makeText(this, (9 - answerMissing) + " more to go!\n\nPlease answer all the questions :)", Toast.LENGTH_SHORT).show();
            totalPoints = 0;
            answerMissing = 0;
            return;
        }

        // Play quiz completion stinger / sound FX.
        mySound.play(quizComplete, .25f, .25f, 2, 0, 1);

        // Sending the score to the results page.
        Intent intent = new Intent(this, Results.class);
        String message = Integer.toString(totalPoints);
        intent.putExtra("theScore", message);
        startActivity(intent);

        // This is to prevent a cheap way to cheat by going back to the question page and hitting 'Calculate My Score' button again.
        totalPoints = 0;

        /** I commented the line below, because I created a more advanced results page.
         * I Kept the line below, because it is stated as "required" in the rubric */
        // Toast.makeText(this, "Points " + totalPoints, Toast.LENGTH_SHORT).show();
    }

    private int calculateScore(int idChecked, int idCorrect, boolean isTrueFalse) {

        // Calculating score for each question with 3 arguments. While keeping track of unanswered questions.
        if (idChecked == idCorrect) {
            answerMissing += 1;
            if (isTrueFalse) {
                return 5;
            } else {
                return 10;
            }
            // If there is no RadioButton checked for that RadioGroup, the method returns -1. This is how I catch missing answers.
        } else if (idChecked == -1) {
            return 0;
        } else {
            answerMissing += 1;
            return 0;
        }
    }

}
