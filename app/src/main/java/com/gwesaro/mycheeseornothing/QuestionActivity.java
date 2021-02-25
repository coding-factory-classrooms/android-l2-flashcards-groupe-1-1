package com.gwesaro.mycheeseornothing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gwesaro.mycheeseornothing.Question.Question;
import com.gwesaro.mycheeseornothing.Question.Quiz;

public class QuestionActivity extends AppCompatActivity {

    private final String TAG = "QuestionActivity";
    private Quiz quiz;
    private RadioGroup radioGroup;
    private Button submitButton;
    private TextView resultTextView;
    private TextView detailResultTextView;
    private ColorStateList colorStateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        radioGroup = findViewById(R.id.radioGroup);
        resultTextView = findViewById(R.id.resultTextView);
        detailResultTextView = findViewById(R.id.detailResultTextView);

        colorStateList = new ColorStateList(
                new int[][] {
                        new int[] { android.R.attr.state_enabled } //enabled
                },
                new int[] { getResources().getColor(R.color.blue1) }
        );

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submitButton.setEnabled(group.getCheckedRadioButtonId() != -1);
            }
        });

        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(radioGroup.getCheckedRadioButtonId() == -1)) {
                    process();
                }
            }
        });

        Intent srcIntent = getIntent();
        if (srcIntent.getExtras().containsKey("quiz")) {
            quiz = srcIntent.getParcelableExtra("quiz");
            updateInterface(quiz.getNextQuestion());
        }
        else {
            throw new IllegalArgumentException("Quiz extra is required!");
        }
    }

    private void process() {
        if (quiz.hasNext()) {
            submitButton.setText("Question suivante");
            handleProcess();
        }
        else {
            submitButton.setText("Fin du quiz");
            if (!resultTextView.getText().toString().isEmpty()) {
                navigateToStats();
            }
            else {
                displayQuestionResponse();
            }
        }
    }

    private void handleProcess() {
        //means that we have already display if answer is correct to user
        if (!resultTextView.getText().toString().isEmpty()) {
            updateInterface(quiz.getNextQuestion());
        }
        else {
            displayQuestionResponse();
        }
    }

    private void displayQuestionResponse() {
        //check if answer is correct and display to user
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedRbText = selectedRadioButton.getText().toString();

            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                radioGroup.getChildAt(i).setClickable(false);
            }

            boolean isCorrect = quiz.CheckAnswer(selectedRbText);
            resultTextView.setText(isCorrect ? "Bonne réponse !" : "Mauvaise réponse !");
            detailResultTextView.setText(isCorrect ? "" : "La bonne réponse est " + quiz.getCurrentQuestion().answer);
            resultTextView.setTextColor( getResources().getColor( (isCorrect ? R.color.green : R.color.red) ));
        }
        else {
            Toast.makeText(QuestionActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateInterface(Question question) {
        resultTextView.setText("");
        detailResultTextView.setText("");

        setTitle(question.mode.getModeFrench() + " ~ Question : " + (quiz.getIndexQuestion() + 1) + " / " + quiz.getQuestionsCount());

        TextView questionTextView = findViewById(R.id.questionTextView);
        questionTextView.setText(question.question);

        handleRadioUpdate(quiz.mixAnswers(question.answers));

        ImageView imageView = findViewById(R.id.questionImageView);
        imageView.setImageResource(getResources().getIdentifier(question.imagePath.split("\\.")[0], "drawable", getPackageName()));

        //disabled button
        submitButton.setEnabled(false);
        submitButton.setText("Valider");
    }

    private void handleRadioUpdate(String[] answers) {
        radioGroup.clearCheck();
        //create or update radiobutton for current question answers
        for (int i = 0; i < answers.length; i++) {
            RadioButton radioButton;
            if (radioGroup.getChildAt(i) != null) {
                radioButton = (RadioButton)radioGroup.getChildAt(i);
                radioButton.setText(answers[i]);
            }
            else {
                radioButton = new RadioButton(this);
                radioButton.setId(i);
                radioButton.setText(answers[i]);
                radioButton.setTextColor(getResources().getColor(R.color.black));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setRadioButtonColor(radioButton);
                }
                radioGroup.addView(radioButton);
            }
            radioButton.setClickable(true);
        }

        //delete all radio button over answers length
        if (radioGroup.getChildCount() > answers.length){
            for (int i = answers.length; i < radioGroup.getChildCount(); i++) {
                radioGroup.removeViewAt(i);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setRadioButtonColor(RadioButton radioButton){
        radioButton.setButtonTintList(colorStateList);
    }

    private void navigateToStats() {
        Intent intent = new Intent(this, StatsActivity.class);
        int nbCorrectAnswer = quiz.getValidAnswersCount();
        intent.putExtra("nbCorrectAnswers" , nbCorrectAnswer );
        intent.putExtra("nbQuestions" , quiz.getQuestionsCount());
        intent.putExtra("modeOrdinal" , quiz.getMode().ordinal());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}