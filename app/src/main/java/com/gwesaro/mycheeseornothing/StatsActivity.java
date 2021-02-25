package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent srcIntent = getIntent();
        int nbCorrectAnswers = srcIntent.getIntExtra("nbCorrectAnswers", 0);
        int nbQuestions = srcIntent.getIntExtra("nbQuestions", 1);

        TextView modeTextView = findViewById(R.id.modeTextView);
        modeTextView.setText( srcIntent.getStringExtra("mode"));

        TextView rateTextView = findViewById(R.id.rateTextView);
        rateTextView.setText(nbCorrectAnswers +  "/" + nbQuestions);

        float percent = (float) nbCorrectAnswers / nbQuestions * 100;
        float result = Math.round(percent*100) / 100;
        Log.i("test" , result + "");
        TextView percentTextView = findViewById(R.id.percentTextView);
        percentTextView.setText(  result + "%");


        Button returnToMainButton = findViewById(R.id.returnToMainButton);
        returnToMainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

}