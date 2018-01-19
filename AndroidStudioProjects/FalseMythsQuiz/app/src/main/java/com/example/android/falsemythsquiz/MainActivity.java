package com.example.android.falsemythsquiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Declaring constants to be used for saving instance states.
    static final String STATE_Q1 = "question1";
    static final String STATE_Q2 = "question2";
    static final String STATE_Q3 = "question3";
    static final String STATE_Q4 = "question4";
    static final String STATE_Q5 = "question5";
    static final String STATE_Q6 = "question6";
    static final String STATE_Q7_1 = "question7-1";
    static final String STATE_Q7_2 = "question7-2";
    static final String STATE_Q8 = "question8";
    static final String STATE_Q9 = "question9";
    static final String STATE_CHECKBOX = "numberOfCheckedCheckboxes";

    // Method that saves the data inside constants.
    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (checkBanana.isChecked()) outState.putInt(STATE_Q7_1, checkBanana.getId());
        else if (checkSpinach.isChecked()) outState.putInt(STATE_Q7_1, checkSpinach.getId());

        if (checkAlmond.isChecked()) outState.putInt(STATE_Q7_2, checkAlmond.getId());
        else if (checkCarrot.isChecked()) outState.putInt(STATE_Q7_2, checkCarrot.getId());

        // Save the UI's current state.
        outState.putInt(STATE_Q1, q1Group.getCheckedRadioButtonId());
        outState.putInt(STATE_Q2, q2Group.getCheckedRadioButtonId());
        outState.putInt(STATE_Q3, q3Group.getCheckedRadioButtonId());
        outState.putInt(STATE_Q4, q4Group.getCheckedRadioButtonId());
        outState.putInt(STATE_Q5, q5Group.getCheckedRadioButtonId());
        outState.putInt(STATE_Q6, q6Group.getCheckedRadioButtonId());
        outState.putInt(STATE_Q9, q9Group.getCheckedRadioButtonId());
        outState.putString(STATE_Q8, String.valueOf(userTextInput.getText().toString()));
        outState.putInt(STATE_CHECKBOX, numberCheckedCheckbox);

        super.onSaveInstanceState(outState);
    }

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

    // Method that restores the data from the constants.
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.getInt(STATE_Q7_1) == checkBanana.getId())
            checkBanana.setChecked(true);
        else if (savedInstanceState.getInt(STATE_Q7_1) == checkSpinach.getId())
            checkSpinach.setChecked(true);

        if (savedInstanceState.getInt(STATE_Q7_2) == checkAlmond.getId())
            checkAlmond.setChecked(true);
        else if (savedInstanceState.getInt(STATE_Q7_2) == checkCarrot.getId())
            checkCarrot.setChecked(true);

        q1Group.check(savedInstanceState.getInt(STATE_Q1));
        q2Group.check(savedInstanceState.getInt(STATE_Q2));
        q3Group.check(savedInstanceState.getInt(STATE_Q3));
        q4Group.check(savedInstanceState.getInt(STATE_Q4));
        q5Group.check(savedInstanceState.getInt(STATE_Q5));
        q6Group.check(savedInstanceState.getInt(STATE_Q6));
        userTextInput.setText(savedInstanceState.getString(STATE_Q8));
        q9Group.check(savedInstanceState.getInt(STATE_Q9));
        numberCheckedCheckbox = savedInstanceState.getInt(STATE_CHECKBOX);
    }

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

        // Since the bug_sound.mp3 is longer than a traditional sound FX, I couldn't use SoundPool class. Sound cuts in the middle of the playback.
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.bug_sound);

        // This creates a "listener" thingy that waits for an onClick event to happen.
        soundButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });

        // Initializing the SoundPool class and loading the sound file matching it to choiceClick and quizComplete variables.
        mySound = new SoundPool.Builder().setMaxStreams(2).build();
        choiceClick = mySound.load(this, R.raw.click, 1);
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

    /**
     * @param checkedId is the ID of the Checkbox that user selected.
     * @return This method returns Checkbox object.
     */
    private CheckBox whichCheckbox(int checkedId) {

        if (checkedId == R.id.checkbox_banana) {
            return checkBanana;

        } else if (checkedId == R.id.checkbox_spinach) {
            return checkSpinach;

        } else if (checkedId == R.id.checkbox_almond) {
            return checkAlmond;

        } else if (checkedId == R.id.checkbox_carrot) {
            return checkCarrot;
        } else return null;

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
        if (checkAlmond.isChecked()) totalPoints += 5;

        if (checkSpinach.isChecked()) totalPoints += 5;

        // Name the insect question points delivered.
        if (userTextInput.getText().toString().toLowerCase().equals("cicada")) totalPoints += 20;

        // Text input unanswered question flags.
        if (userTextInput.getText().toString().trim().length() > 1) answerMissing += 1;

        // Checkbox unanswered question flags.
        if (numberCheckedCheckbox > 0) answerMissing += 1;

        // This is to check if there are unanswered questions. And if yes, then show a Toast message warning.
        if (answerMissing < 9 && numberCheckedCheckbox == 1) {
            Toast.makeText(this, "Please answer all the questions\n\n" + (9 - answerMissing) + " more to go!\n\nA Checkbox missing in Question 7", Toast.LENGTH_SHORT).show();
            totalPoints = 0;
            answerMissing = 0;
            return;
        } else if (answerMissing < 9) {
            Toast.makeText(this, "Please answer all the questions\n\n" + (9 - answerMissing) + " more to go!", Toast.LENGTH_SHORT).show();
            totalPoints = 0;
            answerMissing = 0;
            return;
        } else if (answerMissing == 9 && numberCheckedCheckbox == 1) {
            Toast.makeText(this, "Allmost done!\n\nA Checkbox missing in Question 7", Toast.LENGTH_SHORT).show();
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

        /** I commented the line below and kept it on purpose, because I created a more advanced results page.
         * And, because it is stated as "required" in the rubric */
        // Toast.makeText(this, "Points " + totalPoints, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param idChecked   is the RadioButton ID that user selected.
     * @param idCorrect   is the RadioButton ID of the correct answer.
     * @param isTrueFalse is a flag for grading purposes. Since True / False questions are rather easy, they provide less points.
     * @return This method returns points for the totalPoints variable.
     */
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
