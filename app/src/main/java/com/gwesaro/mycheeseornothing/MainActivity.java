package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
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
    private boolean isQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionCollection = ((App)getApplication()).questionCollection;
        isQuiz = true;

        /**
         * add listener on "about" button and create a new Intent to load the target activity
         */
        findViewById(R.id.aboutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        /**
         * add listener on "liste de questions" button and call fetchQuestions function to load the data from API
         */
        findViewById(R.id.questionsListButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isQuiz = false;
                questionCollection.fetchQuestions(QuestionMode.ALL);
            }
        });

        /**
         * add listener on "apprendre le fromage" button
         */
        findViewById(R.id.learnCheeseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionMode[] values = QuestionMode.values();
                String[] difficulty = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    difficulty[i] = values[i].getModeFrench();
                }

                /**
                 * create new dialog window to set a single choice item of difficulty and call fetchQuestions function to load the data from API
                 */
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
                materialAlertDialogBuilder.setTitle("Choisir le niveau de difficulté")
                        .setSingleChoiceItems(difficulty, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isQuiz = true;
                                questionCollection.fetchQuestions(QuestionMode.values()[which]);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    /**
     * add a listener on questionCollection
     */
    @Override
    protected void onResume() {
        super.onResume();
        questionCollection.addQuestionCollectionEventListener(this);
    }
    /**
     * remove listener on questionCollection
     */
    @Override
    protected void onPause() {
        super.onPause();
        questionCollection.removeQuestionCollectionEventListener(this);
    }

    /**
     * add listener when backButton is pressed and set two button for Cancel or Accept
     */
    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Quitter l'application")
                .setMessage("Êtes-vous sûr de vouloir quitter My Cheese Or Nothing ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        finishAffinity();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }

    /**
     * display a toast if cannot fetch data from API
     */
    @Override
    public void onFailed(Exception e) {
        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * launch a quiz and start the correct Activity (Question or QuestionList)
     * depending on isQuiz boolean.
     * isQuiz is true if click on button 'learn cheese', should start QuestionActivity.
     */
    @Override
    public void onQuestionsChanged(ArrayList<Question> questions, QuestionMode mode) {
        Quiz quiz = new Quiz(questions, mode);
        quiz.mixQuestions();
        Intent intent = new Intent(MainActivity.this, isQuiz ? QuestionActivity.class : QuestionsListActivity.class);
        intent.putExtra("quiz", quiz);
        startActivity(intent);
    }
}