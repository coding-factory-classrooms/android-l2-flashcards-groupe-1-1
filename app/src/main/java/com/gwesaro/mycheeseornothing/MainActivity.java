package com.gwesaro.mycheeseornothing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.EventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

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
                Intent intent = new Intent(MainActivity.this, QuestionsListActivity.class);
                startActivity(intent);
            }
        });

        String path = "bleu_d_auvergne.jpg";
        String name = path.split("\\.")[0];
        int drawableResourceId = this.getResources().getIdentifier(name, "drawable", this.getPackageName());

        findViewById(R.id.learnCheeseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] difficulty = {"Easy", "Medium", "Hard"};

                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
                materialAlertDialogBuilder.setTitle("Choisir le niveau de difficulté")
                        .setSingleChoiceItems(difficulty, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                                intent.putExtra("difficulty", which);
                            }
                        })
                        .show();
            }
        });

        QuestionCollection collection = new QuestionCollection(QuestionMode.ALL);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawableFromImagePath(String imagePath) {
        int id = mainActivity.getResources()
                .getIdentifier(imagePath.split("\\.")[0], "drawable", mainActivity.getPackageName());
        return id == 0 ? null : mainActivity.getDrawable(id);

    }
}