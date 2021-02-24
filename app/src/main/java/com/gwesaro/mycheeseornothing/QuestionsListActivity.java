package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class QuestionsListActivity extends AppCompatActivity {
    QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_list);

        questionAdapter = new QuestionAdapter();
        setTitle("Liste des questions");
    }
}