package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.gwesaro.mycheeseornothing.Question.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionsListActivity extends AppCompatActivity {
    QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_list);
        setTitle("Liste des questions");

        List<Question> questions = new ArrayList<>();

        QuestionAdapter adapter = new QuestionAdapter(questions);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewQuestion);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}