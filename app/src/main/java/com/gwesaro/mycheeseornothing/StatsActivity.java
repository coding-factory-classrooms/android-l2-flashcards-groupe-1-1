package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gwesaro.mycheeseornothing.Question.QuestionMode;

public class StatsActivity extends AppCompatActivity {

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

        TextView modeTextView = findViewById(R.id.modeTextView);
        QuestionMode mode = QuestionMode.values()[srcIntent.getIntExtra("modeOrdinal", 0)];

        modeTextView.setText(mode.getModeFrench());

        ImageView statsImageView = findViewById(R.id.statsImageView);

        /**
         * switch / case to set the rigth icon for the given mode
         */
        switch (mode) {
            case EASY:
                statsImageView.setImageResource(R.drawable.logo_easy);
                break;
            case MEDIUM:
                statsImageView.setImageResource(R.drawable.logo_medium);
                break;
            case HARD:
                statsImageView.setImageResource(R.drawable.logo_hard);
                break;
            case ALL:
            default:
                statsImageView.setImageResource(R.drawable.logo2);
                break;
        }

        TextView goodAnswersCountTextView = findViewById(R.id.goodAnswersCountTextView);
        goodAnswersCountTextView.setText(nbCorrectAnswers +  "/" + nbQuestions);
        TextView labelAnswer = findViewById(R.id.labelAnswer);
        labelAnswer.setText(nbCorrectAnswers > 1 ? "Bonnes réponses" : "Bonne réponse");

        float percent = (float) nbCorrectAnswers / nbQuestions * 100;
        float result = (float) Math.round(percent * 100) / 100;

        TextView percentTextView = findViewById(R.id.percentTextView);
        percentTextView.setText(result + " %");
        int color = result < 33.3f ? R.color.red : (result > 66.6f ? R.color.green : R.color.cheese);
        percentTextView.setTextColor(getResources().getColor(color));

        Button returnToMainButton = findViewById(R.id.returnToMainButton);
        returnToMainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        ProgressBar progress = findViewById(R.id.progressBar);
        progress.getProgressDrawable().setColorFilter(getResources().getColor(color), android.graphics.PorterDuff.Mode.SRC_IN);
        ProgressBarAnimation anim = new ProgressBarAnimation(progress, 0.0f, percent);
        anim.setDuration(1000);
        progress.startAnimation(anim);
    }
}