package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        radioGroup = findViewById(R.id.radioGroup);
        resultTextView = findViewById(R.id.resultTextView);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submitButton.setEnabled(true);
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
        updateInterface(quiz.getNextQuestion());

    }

    private void process() {
        if (quiz.hasNext()) {
            submitButton.setText("Question suivante");
            handleProcess();
        }
        else {
            submitButton.setText("Fin du quiz");
            if (!resultTextView.getText().toString().equals("")) {
                navigateToStats();
            }
            else {
                displayQuestionResponse();
            }
        }
    }

    private void handleProcess(){
        //means that we have already display if answer is correct to user
        if (!resultTextView.getText().toString().equals("")) {
            updateInterface(quiz.getNextQuestion());
        }
        else {
            displayQuestionResponse();
        }
    }

    private void displayQuestionResponse(){
        //check if answer is correct and display to user
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedRbText = selectedRadioButton.getText().toString();

            resultTextView.setText(quiz.CheckAnswer(selectedRbText) ? "Bonne réponse" : "Désolé, la bonne réponse est " + quiz.getCurrentQuestion().answer);
        } else {
            Log.i(TAG, "nothing selected");
        }
    }


    private void updateInterface(Question question) {
        //disabled button
        submitButton.setEnabled(false);
        submitButton.setText("Valider");
        resultTextView.setText("");

        setTitle(quiz.getMode().toString().toLowerCase() + " " + (quiz.getIndexQuestion() + 1) + " / " + quiz.getQuestionsCount());
        TextView questionTextView = findViewById(R.id.questionTextView);
        questionTextView.setText(question.question);

        handleRadioUpdate(quiz.mixAnswers(question.answers));

        ImageView imageView = findViewById(R.id.questionImageView);
        imageView.setImageResource(getResources().getIdentifier(question.imagePath.split("\\.")[0], "drawable", getPackageName()));
    }

    private void handleRadioUpdate(String[] answers){
        //create or update radiobutton for current question answers
        for (int i=0; i<answers.length; i++){
            RadioButton radioButton ;
            if (radioGroup.getChildAt(i) != null){
                radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setText(answers[i]);
                radioButton.setChecked(false);

            }
            else{
                radioButton = new RadioButton(this);
                radioButton.setId(i);
                radioButton.setText(answers[i]);
                radioGroup.addView(radioButton);
            }
        }

        //delete all radio button over answers length
        if ( radioGroup.getChildCount()>answers.length){
            for (int i=answers.length; i<radioGroup.getChildCount(); i++){
                radioGroup.removeViewAt(i);
            }
        }
    }

    private void navigateToStats() {
        Log.i(TAG, "C'est la fin bg");
        /*
        Intent intent = new Intent(this, StatsActivity.class);
        intent.putExtra("percent" , this.quest.getSuccessPercent());
        intent.putExtra("rate" , this.quest.getQuizRate());
        intent.putExtra("mode" , this.quest.getMode());
        startActivity(intent);
        
         */
    }
}