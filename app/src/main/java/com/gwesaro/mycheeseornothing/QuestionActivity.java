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
        radioGroup = findViewById(R.id.radioGroup);
        resultTextView = findViewById(R.id.resultTextView);
        detailResultTextView = findViewById(R.id.detailResultTextView);
        findViewById(R.id.questionImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = new ImageView(QuestionActivity.this);
                image.setImageResource(getResources().getIdentifier(quiz.getCurrentQuestion().imagePath.split("\\.")[0], "drawable", getPackageName()));
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this).setView(image);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        colorStateList = new ColorStateList(
                new int[][] { new int[] { android.R.attr.state_enabled } }, //enabled
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
                if (radioGroup.getCheckedRadioButtonId() != -1) {
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
        setMediaPlayer(R.raw.song_quiz_start);
    }

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

    private void handleProcess() {
        if (!resultTextView.getText().toString().isEmpty()) {
            updateInterface(quiz.getNextQuestion());
        }
        else {
            displayQuestionResponse();
        }
    }

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

    private void updateInterface(Question question) {
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

    private void handleRadioUpdate(String[] answers) {
        radioGroup.clearCheck();
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
                radioButton.setTextColor(getResources().getColor(R.color.black));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setRadioButtonColor(radioButton);
                }
                radioGroup.addView(radioButton);
            }
            radioButton.setClickable(true);
        }
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