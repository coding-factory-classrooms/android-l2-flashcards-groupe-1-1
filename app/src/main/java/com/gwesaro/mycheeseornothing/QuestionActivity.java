package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwesaro.mycheeseornothing.Question.Question;
import com.gwesaro.mycheeseornothing.Question.Quiz;

import kotlin.jvm.Throws;

public class QuestionActivity extends AppCompatActivity {

    private final String TAG = "QuestionActivity";
    private Quiz quiz;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process();
            }
        });

        Intent srcIntent = getIntent();

        if(srcIntent.getExtras().containsKey("question")){
            question = srcIntent.getParcelableExtra("question");
            updateInterface(question);
        }else if(srcIntent.getExtras().containsKey("quiz")){
            quiz = srcIntent.getParcelableExtra("quiz");
            process();
        }else{
            throw new IllegalArgumentException();

        }

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
        TextView answer1TextView = findViewById(R.id.answer1TextView);
        TextView answer2TextView = findViewById(R.id.answer2TextView);
        TextView answer3TextView = findViewById(R.id.answer3TextView);
        TextView answer4TextView = findViewById(R.id.answer4TextView);
        questionTextView.setText(question.question);
        answer1TextView.setText(question.answers[0]);
        answer2TextView.setText(question.answers[1]);
        answer3TextView.setText(question.answers[2]);
        if (question.answers.length > 3) {
            answer4TextView.setText(question.answers[3]);
        }
        ImageView imageView = findViewById(R.id.questionImageView);
        imageView.setImageResource(getResources().getIdentifier(question.imagePath.split("\\.")[0], "drawable", getPackageName()));
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