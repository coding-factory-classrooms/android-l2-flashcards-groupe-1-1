package com.gwesaro.mycheeseornothing;

import android.app.Application;
import android.media.MediaPlayer;
import android.util.Log;

import com.gwesaro.mycheeseornothing.Question.QuestionCollection;

/**
 * only used to get collection easely
 */
public class App extends Application {

    public QuestionCollection questionCollection;

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        questionCollection = new QuestionCollection();
        mediaPlayer = MediaPlayer.create(App.this, R.raw.song_wellcome);
        mediaPlayer.start();
    }
}
