package com.gwesaro.mycheeseornothing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        questionImageView = findViewById(R.id.questionImageView);
        radioGroup = findViewById(R.id.radioGroup);
        resultTextView = findViewById(R.id.resultTextView);
        detailResultTextView = findViewById(R.id.detailResultTextView);
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
           /**
             * used to validate question (and display result),
             * navigate to StatsActivity at quiz end, or go to next question
             * if there is one (and update interface).
             * Check if one Radiobutton is select before executing
             */
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() != -1) {
                    process();
                }
            }
        });
        findViewById(R.id.questionImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = new ImageView(QuestionActivity.this);
                image.setImageResource(getResources().getIdentifier(quiz.getCurrentQuestion().imagePath.split("\\.")[0], "drawable", getPackageName()));
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });  
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this).setView(image);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        /**
         * use to set color of RadioButtons, could be used to have different color
         * depending on RadioButton state
         */
        colorStateList = new ColorStateList(
                new int[][] { new int[] { android.R.attr.state_enabled } }, //enabled
                new int[] { getResources().getColor(R.color.blue1) }
        );

        /**
         * used to enable submitButton when radioGroup have one
         * RadioButton checked
         */

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submitButton.setEnabled(group.getCheckedRadioButtonId() != -1);
            }
        });

        /**
         * try to get quiz from Intent and set interface
         */

        Intent srcIntent = getIntent();
        if (srcIntent.getExtras().containsKey("quiz")) {
            quiz = srcIntent.getParcelableExtra("quiz");
            updateInterface(quiz.getNextQuestion());
        }
        else {
            throw new IllegalArgumentException("Quiz extra is required!");
        }
        setMediaPlayer(R.raw.song_quiz_start);
    }

    /**
     * add listener when backButton is pressed and set two button for Cancel or Accept.
     * if Accept, we navigate to StatsActivity
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
         * switch / case to set the right icon mode from the question's mode to dialog
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

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private void setMediaPlayer(int rawId) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(QuestionActivity.this, rawId);
        mediaPlayer.start();
    }

    /**
     * if the current question has a next question, the process continue.
     * If not, we navigate to StatsActivity if already have displayQuestionResponse
     */
    private void process() {
        if (quiz.hasNext()) {
            submitButton.setText("Question suivante");
            handleProcess();
        }
        else {
            submitButton.setText("Fin du quiz");
            if (!resultTextView.getText().toString().isEmpty()) {
                navigateToStats();
            } else {
                displayQuestionResponse();
            }
        }
    }

    /**
     * if we have already displayQuestionResponse to user,
     * we updateInterface with next question.
     * Else, we displayQuestionResponse
     */
    private void handleProcess() {
        if (!resultTextView.getText().toString().isEmpty()) {
            updateInterface(quiz.getNextQuestion());
        }
        else {
            displayQuestionResponse();
        }
    }

    /**
     * display the correct text if the answer is correct or not with the right colors;
     * all RadioButton are set unClickable, to avoid to select other answers
     * after having display Answer to user
     */
    private void displayQuestionResponse() {
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedRbText = selectedRadioButton.getText().toString();

            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                radioGroup.getChildAt(i).setClickable(false);
            }

            if (quiz.CheckAnswer(selectedRbText)) {
                resultTextView.setText("Bonne réponse !");
                detailResultTextView.setText("");
                resultTextView.setTextColor(getResources().getColor((R.color.green2)));
                setMediaPlayer(R.raw.song_right_answer);
            }
            else {
                resultTextView.setText("Mauvaise réponse !");
                detailResultTextView.setText("La bonne réponse est " + quiz.getCurrentQuestion().answer + ".");
                resultTextView.setTextColor(getResources().getColor(R.color.red));
                setMediaPlayer(R.raw.song_wring_answer);
            }
        }
        else {
            Toast.makeText(QuestionActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * update interface dynamically with the right data from the API;
     * need a question object to set all elements in main view
     * @param question : current question POJO with all
     *                 needed information
     */
    private void updateInterface(Question question) {
        //reset Question response views content
        resultTextView.setText("");
        detailResultTextView.setText("");

        setTitle(question.mode.getModeFrench() + " ~ Question : " + (quiz.getIndexQuestion() + 1) + " / " + quiz.getQuestionsCount());

        //set question title
        TextView questionTextView = findViewById(R.id.questionTextView);
        questionTextView.setText(question.question);

        //update radioButtons
        handleRadioUpdate(quiz.mixAnswers(question.answers));

        //set imageView with question image
        ImageView imageView = findViewById(R.id.questionImageView);
        imageView.setImageResource(getResources().getIdentifier(question.imagePath.split("\\.")[0], "drawable", getPackageName()));

        //reset submitButton
        submitButton.setEnabled(false);
        submitButton.setText("Valider");
    }

    /**
     * this methods clear radioGroup checked RadioButton.
     * also create as many RadioButton as answers.
     *
     * We don't delete all Radiobutton, we set exiting ones with a possible answer
     * and delete unnecessary ones.
     * @param answers : current question answers, used to create same RadioButton number
     */
    private void handleRadioUpdate(String[] answers) {
        radioGroup.clearCheck();

        // create or update radiobutton for current question answers
        for (int i = 0; i < answers.length; i++) {
            RadioButton radioButton;
            if (radioGroup.getChildAt(i) != null) {
                radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setText(answers[i]);
            }
            else {
                radioButton = new RadioButton(this);
                radioButton.setId(i);
                radioButton.setText(answers[i]);

                // set radiobutton color for text and Circle
                radioButton.setTextColor(getResources().getColor(R.color.black));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setRadioButtonColor(radioButton);
                }
                radioGroup.addView(radioButton);
            }
            radioButton.setClickable(true);
        }

        // delete all unnecessary radioButton, over answers length
        if (radioGroup.getChildCount() > answers.length) {
            for (int i = answers.length; i < radioGroup.getChildCount(); i++) {
                radioGroup.removeViewAt(i);
            }
        }
    }

    /**
     * set a color to a RqdioButton circle
     * @param radioButton : the radioButton to update
     */
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

        // avoid user to return to this activity after navigate to the new one
        finish();
    }
}