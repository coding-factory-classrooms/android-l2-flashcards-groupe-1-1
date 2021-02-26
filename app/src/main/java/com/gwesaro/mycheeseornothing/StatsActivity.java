package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gwesaro.mycheeseornothing.Question.QuestionMode;

public class StatsActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Intent srcIntent = getIntent();

        /**
         * retrieve the extra data from intent
         */
        int nbCorrectAnswers = srcIntent.getIntExtra("nbCorrectAnswers", 0);
        int nbQuestions = srcIntent.getIntExtra("nbQuestions", 1);
        QuestionMode mode = QuestionMode.values()[srcIntent.getIntExtra("modeOrdinal", 0)];
        TextView modeTextView = findViewById(R.id.modeTextView);

        modeTextView.setText(mode.getModeFrench());
        ImageView statsImageView = findViewById(R.id.statsImageView);

        /**
         * switch / case to set the right icon for the given mode
         */
        switch (mode) {
            case EASY: statsImageView.setImageResource(R.drawable.logo_easy); break;
            case MEDIUM: statsImageView.setImageResource(R.drawable.logo_medium); break;
            case HARD: statsImageView.setImageResource(R.drawable.logo_hard); break;
            case RANDOM:
            case ALL:
            default: statsImageView.setImageResource(R.drawable.logo2); break;
        }

        // display user rate ("2/3")
        TextView goodAnswersCountTextView = findViewById(R.id.goodAnswersCountTextView);
        goodAnswersCountTextView.setText(nbCorrectAnswers +  "/" + nbQuestions);

        // adapt label 'Bonne reponse' if one or several question
        TextView labelAnswer = findViewById(R.id.labelAnswer);
        labelAnswer.setText(nbCorrectAnswers > 1 ? "Bonnes réponses" : "Bonne réponse");

        // calculate user's correct answers percentage
        float percent = (float) nbCorrectAnswers / nbQuestions * 100;
        float result = (float) Math.round(percent * 100) / 100;

        TextView percentTextView = findViewById(R.id.percentTextView);
        percentTextView.setText(result + " %");

        int color;
        if (result <= 33.3f) {
            setMediaPlayer(R.raw.song_bad);
            color = R.color.red;
        }
        else if (result <= 66.6f) {
            setMediaPlayer(R.raw.song_middle);
            color = R.color.cheese;
        }
        else {
            setMediaPlayer(R.raw.song_good);
            color = R.color.green2;
        }

        percentTextView.setTextColor(getResources().getColor(color));

        // button to backspace
        Button returnToMainButton = findViewById(R.id.returnToMainButton);
        returnToMainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // display a progressBar with animation (fill depending on user's percentage)
        ProgressBar progress = findViewById(R.id.progressBar);
        progress.getProgressDrawable().setColorFilter(getResources().getColor(color), android.graphics.PorterDuff.Mode.SRC_IN);
        ProgressBarAnimation anim = new ProgressBarAnimation(progress, 0.0f, percent);
        anim.setDuration(2000);
        progress.startAnimation(anim);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private void setMediaPlayer(int rawId) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (((App)getApplication()).hasSoundEffect) {
            mediaPlayer = MediaPlayer.create(StatsActivity.this, rawId);
            mediaPlayer.start();
        }
    }
}