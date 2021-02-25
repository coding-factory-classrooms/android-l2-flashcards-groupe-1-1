package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gwesaro.mycheeseornothing.Question.Question;
import com.gwesaro.mycheeseornothing.Question.QuestionCollection;
import com.gwesaro.mycheeseornothing.Question.QuestionCollectionEventListener;
import com.gwesaro.mycheeseornothing.Question.QuestionMode;
import com.gwesaro.mycheeseornothing.Question.Quiz;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements QuestionCollectionEventListener {

    private final String TAG = "MainActivity";
    private QuestionCollection questionCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionCollection = ((App)getApplication()).questionCollection;

        findViewById(R.id.aboutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.questionsListButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionCollection.fetchQuestions(QuestionMode.ALL);
            }
        });

        findViewById(R.id.learnCheeseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionMode[] values = QuestionMode.values();
                String[] difficulty = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    difficulty[i] = values[i].getModeFrench();
                }

                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
                materialAlertDialogBuilder.setTitle("Choisir le niveau de difficulté")
                        .setSingleChoiceItems(difficulty, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                questionCollection.fetchQuestions(QuestionMode.values()[which]);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        questionCollection.addQuestionCollectionEventListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        questionCollection.removeQuestionCollectionEventListener(this);
    }

    @Override
    public void onFailed(Exception e) {
        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuestionsChanged(ArrayList<Question> questions, QuestionMode mode) {
        Quiz quiz = new Quiz(questions, mode);
        quiz.mixQuestions();
        Intent intent = new Intent(MainActivity.this, QuestionsListActivity.class);
        intent.putExtra("quiz", quiz);
        startActivity(intent);
    }
}