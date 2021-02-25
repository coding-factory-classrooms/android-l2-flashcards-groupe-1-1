package com.gwesaro.mycheeseornothing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gwesaro.mycheeseornothing.Question.Question;
import com.gwesaro.mycheeseornothing.Question.QuestionMode;
import com.gwesaro.mycheeseornothing.Question.Quiz;

public class QuestionActivity extends AppCompatActivity {

    private final String TAG = "QuestionActivity";
    private Quiz quiz;
    private RadioGroup radioGroup;
    private Button submitButton;
    private TextView resultTextView;
    private TextView detailResultTextView;
    private ColorStateList colorStateList;
    private ImageView questionImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        radioGroup = findViewById(R.id.radioGroup);
        resultTextView = findViewById(R.id.resultTextView);
        detailResultTextView = findViewById(R.id.detailResultTextView);
        questionImageView = findViewById(R.id.questionImageView);

        /**
         * add listener on FlashCard image to expend them
         */
        questionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * create new imageView with the current question's image
                 */
                ImageView image = new ImageView(QuestionActivity.this);
                image.setImageResource(getResources().getIdentifier(quiz.getCurrentQuestion().imagePath.split("\\.")[0], "drawable", getPackageName()));

                /**
                 * create a dialog and set the imageView
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this)
                                .setView(image);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                /**
                 * add listener to close the dialog
                 */
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

        /**
         * @todo Gwen
         */
        colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{getResources().getColor(R.color.blue1)}
        );


        /**
         * @todo Gwen
         */
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
                if (!(radioGroup.getCheckedRadioButtonId() == -1)) {
                    process();
                }
            }
        });

        Intent srcIntent = getIntent();
        if (srcIntent.getExtras().containsKey("quiz")) {
            quiz = srcIntent.getParcelableExtra("quiz");
            updateInterface(quiz.getNextQuestion());
        } else {
            throw new IllegalArgumentException("Quiz extra is required!");
        }
    }

    /**
     * add listener when backButton is pressed and set two button for Cancel or Accept
     */
    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(QuestionActivity.this)
                .setTitle("Quitter le quiz")
                .setMessage("Tu ne veux pas un dernier petit bout de fromage ?")
                .setPositiveButton("Oui, quitter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        navigateToStats();
                    }
                })
                .setNegativeButton("J'en veux encore !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        /**
         * switch / case to set the rigth icon mode from the question's mode to dialog
         */
        switch (quiz.getMode()) {
            case EASY: dialog.setIcon(R.drawable.logo_easy); break;
            case MEDIUM: dialog.setIcon(R.drawable.logo_medium); break;
            case HARD: dialog.setIcon(R.drawable.logo_hard); break;
            case ALL:
            default:
                dialog.setIcon(R.drawable.logo2);
                break;
        }
        dialog.show();
    }

    /**
     * if the current question has a next question, the process continue. If no we display the stats
     */
    private void process() {
        if (quiz.hasNext()) {
            submitButton.setText("Question suivante");
            handleProcess();
        } else {
            submitButton.setText("Fin du quiz");
            if (!resultTextView.getText().toString().isEmpty()) {
                navigateToStats();
            } else {
                displayQuestionResponse();
            }
        }
    }

    private void handleProcess() {
        //means that we have already display if answer is correct to user
        if (!resultTextView.getText().toString().isEmpty()) {
            updateInterface(quiz.getNextQuestion());
        } else {
            displayQuestionResponse();
        }
    }

    /**
     * display the correct text if the answer is correct or not with the rigth colors
     */
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
            resultTextView.setTextColor(getResources().getColor((isCorrect ? R.color.green : R.color.red)));
        } else {
            Toast.makeText(QuestionActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * update interface dynamically with the rigth data from the API
     * @param question
     */
    private void updateInterface(Question question) {
        ScrollView scrollView = findViewById(R.id.questionScrollView);
        scrollView.scrollTo(0, 0);
        resultTextView.setText("");
        detailResultTextView.setText("");
        setTitle(question.mode.getModeFrench() + " ~ Question : " + (quiz.getIndexQuestion() + 1) + " / " + quiz.getQuestionsCount());
        TextView questionTextView = findViewById(R.id.questionTextView);
        questionTextView.setText(question.question);
        handleRadioUpdate(quiz.mixAnswers(question.answers));
        ImageView imageView = findViewById(R.id.questionImageView);
        imageView.setImageResource(getResources().getIdentifier(question.imagePath.split("\\.")[0], "drawable", getPackageName()));
        submitButton.setEnabled(false);
        submitButton.setText("Valider");
    }

    /**
     * @todo Gwen
     * @param answers
     */
    private void handleRadioUpdate(String[] answers) {
        radioGroup.clearCheck();
        //create or update radiobutton for current question answers
        for (int i = 0; i < answers.length; i++) {
            RadioButton radioButton;
            if (radioGroup.getChildAt(i) != null) {
                radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setText(answers[i]);
            } else {
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
        if (radioGroup.getChildCount() > answers.length) {
            for (int i = answers.length; i < radioGroup.getChildCount(); i++) {
                radioGroup.removeViewAt(i);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setRadioButtonColor(RadioButton radioButton) {
        radioButton.setButtonTintList(colorStateList);
    }

    /**
     * create a new intent to navigate to stats with the right data
     */
    private void navigateToStats() {
        Intent intent = new Intent(this, StatsActivity.class);
        int nbCorrectAnswer = quiz.getValidAnswersCount();
        intent.putExtra("nbCorrectAnswers", nbCorrectAnswer);
        intent.putExtra("nbQuestions", quiz.getQuestionsCount());
        intent.putExtra("modeOrdinal", quiz.getMode().ordinal());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}