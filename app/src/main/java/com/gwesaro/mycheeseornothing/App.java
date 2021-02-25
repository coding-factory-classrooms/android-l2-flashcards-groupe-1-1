package com.gwesaro.mycheeseornothing;

import android.app.Application;

import com.gwesaro.mycheeseornothing.Question.QuestionCollection;

public class App extends Application {

    public QuestionCollection questionCollection;

    @Override
    public void onCreate() {
        super.onCreate();
        questionCollection = new QuestionCollection();
    }
}
