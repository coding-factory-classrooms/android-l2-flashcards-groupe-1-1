package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gwesaro.mycheeseornothing.Question.Question;
import com.gwesaro.mycheeseornothing.Question.QuestionCollection;
import com.gwesaro.mycheeseornothing.Question.QuestionCollectionEventListener;
import com.gwesaro.mycheeseornothing.Question.QuestionMode;
import com.gwesaro.mycheeseornothing.Question.Quiz;

import java.util.ArrayList;
import java.util.List;

public class QuestionsListActivity extends AppCompatActivity {
    private static final String TAG = "QuestionListActivity";
    QuestionCollection questionCollection;
    List<Question> listQuestions = new ArrayList<>();
    private Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_list);
        setTitle("Liste des questions");
        Intent srcIntent = getIntent();

        quiz = srcIntent.getParcelableExtra("quiz");


        for (int i = 0; i < quiz.getQuestionsCount(); i++) {
            listQuestions.add(quiz.get(i));
        }


        QuestionAdapter adapter = new QuestionAdapter(listQuestions);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewQuestion);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

}