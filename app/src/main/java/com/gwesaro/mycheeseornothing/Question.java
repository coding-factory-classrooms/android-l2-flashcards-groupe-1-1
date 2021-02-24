package com.gwesaro.mycheeseornothing;

import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Question {
    private final String question;
    private final String[] answers;
    private final String answer;
    private final Drawable image;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Question(String question, String[] answers, String answer, String imagePath) {
        this.question = question;
        this.answers = answers;
        this.answer = answer;
        this.image = MainActivity.getDrawableFromImagePath(imagePath);
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public String getAnswer() {
        return answer;
    }

    public Drawable getImage() {
        return image;
    }

    public boolean isValid(String answer) {
        return this.answer.equals(answer);
    }
}
