package com.example.android.falsemythsquiz;

import android.app.ActionBar;
import android.content.Intent;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Welcome extends AppCompatActivity {

    // Declaring the class and view variable for sound FX playback.
    SoundPool mySound;
    int choiceClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initializing the SoundPool class and loading the sound file matching it to choiceClick variable.
        mySound = new SoundPool.Builder().setMaxStreams(2).build();
        choiceClick = mySound.load(this, R.raw.click, 1);
    }

    public void beginButton(View view) {

        // Play click sound FX.
        mySound.play(choiceClick, .5f, .5f, 1, 0, 1);

        // The intent that opens the MainActivity, where the questions are.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
