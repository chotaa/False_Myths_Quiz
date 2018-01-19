package com.example.android.falsemythsquiz;

import android.content.Intent;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Results extends AppCompatActivity {

    // Declaring the class and view variable for sound FX playback.
    SoundPool mySound;
    int choiceClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Retrieve the score that was sent from the MainActivity.
        Intent intent = getIntent();
        String score = intent.getStringExtra("theScore");

        // Set the text on score_field TextView, displaying the final score.
        TextView scoreField = findViewById(R.id.score_field);
        scoreField.setText(score + "/85");

        // Initializing the SoundPool class and loading the sound file matching it to choiceClick variable.
        mySound = new SoundPool.Builder().setMaxStreams(2).build();
        choiceClick = mySound.load(this, R.raw.click, 1);
    }

    public void onRestart(View view) {

        // Play click sound FX.
        mySound.play(choiceClick, .5f, .5f, 1, 0, 1);

        // The intent that opens the MainActivity, where the questions are.
        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);
    }
}
