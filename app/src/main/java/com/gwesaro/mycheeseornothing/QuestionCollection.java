package com.gwesaro.mycheeseornothing;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionCollection {

    private final String TAG = "QuestionsV2";
    private final QuestionMode mode;
    private final List<Question> questions;
    private int validAnswersCount;

    public QuestionCollection(QuestionMode mode) {
        this.mode = mode;
        this.questions = new ArrayList<>();
        this.validAnswersCount = 0;
        fetchQuestions();
    }

    private void fetchQuestions() {
        String url = "http://gryt.tech:8080/mycheeseornothing/";
        if (this.mode != QuestionMode.ALL) {
            url += "?difficulty" + this.mode;
        }
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = Objects.requireNonNull(response.body()).string();
                try {
                    JSONArray json = new JSONArray(body);
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject question = new JSONObject(json.get(i).toString());
                        JSONArray jsonAnswers = question.getJSONArray("responses");
                        String[] answers = new String[jsonAnswers.length()];
                        for (int j = 0; j < jsonAnswers.length(); j++) {
                            answers[j] = jsonAnswers.getString(j);
                        }
                        questions.add(new Question(
                                question.getString("question"),
                                answers,
                                question.getString("answer"),
                                question.getString("imageName")
                        ));
                    }
                }
                catch (JSONException e) {
                    Log.e(TAG, "onResponse fail: " + e.getMessage());
                }
            }
        });
    }
}
