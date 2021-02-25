package com.gwesaro.mycheeseornothing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gwesaro.mycheeseornothing.Question.Question;
import com.gwesaro.mycheeseornothing.Question.Quiz;

public class QuestionActivity extends AppCompatActivity {

    private final String TAG = "QuestionActivity";
    private Quiz quiz;
    private RadioGroup radioGroup;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //@todo enabled validation button (disabled before)
                submitButton.setClickable(true);
            }
        });

        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process();
            }
        });

        Intent srcIntent = getIntent();
        quiz = srcIntent.getParcelableExtra("quiz");
        process();
    }

    private void process() {
        if (quiz.hasNext()) {
            updateInterface(quiz.getNextQuestion());
        }
        else {
            navigateToStats();
        }
    }

    private void updateInterface(Question question) {
        setTitle(quiz.getMode().toString().toLowerCase() + " " + (quiz.getIndexQuestion() + 1) + " / " + quiz.getQuestionsCount());
        TextView questionTextView = findViewById(R.id.questionTextView);
        questionTextView.setText(question.question);

        handleRadioUpdate(question.answers);

        //disabled button
        submitButton.setClickable(false);

        ImageView imageView = findViewById(R.id.questionImageView);
        imageView.setImageResource(getResources().getIdentifier(question.imagePath.split("\\.")[0], "drawable", getPackageName()));
    }

    private void handleRadioUpdate(String[] answers){
        for (int i=0; i<answers.length; i++){
            RadioButton radioButton ;
            if (radioGroup.getChildAt(i) != null){
                radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setText(answers[i]);
            }
            else{
                radioButton = new RadioButton(this);
                radioButton.setId(i);
                radioButton.setText(answers[i]);
                radioGroup.addView(radioButton);
            }
        }
        Log.i(TAG, radioGroup.getChildAt(1) + "");
    }

    private void navigateToStats() {
        Log.i(TAG, "C'est la fin bg");
        //        Intent intent = new Intent(this, StatsActivity.class);
        //        intent.putExtra("percent" , this.quest.getSuccessPercent());
        //        intent.putExtra("rate" , this.quest.getQuizRate());
        //        intent.putExtra("mode" , this.quest.getMode());
        //        startActivity(intent);
    }
}