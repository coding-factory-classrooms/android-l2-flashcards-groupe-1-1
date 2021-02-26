package com.gwesaro.mycheeseornothing.Question;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionCollection{

    private final Collection<QuestionCollectionEventListener> eventListeners;

    /**
     * QuestionsCollection constructor
     */
    public QuestionCollection() {
        this.eventListeners = new ArrayList<QuestionCollectionEventListener>();
    }

    /**
     * @todo Romano c'est pour toi
     * @param listener
     */
    public void addQuestionCollectionEventListener(QuestionCollectionEventListener listener) {
        this.eventListeners.add(listener);
    }

    /**
     * @todo Romano c'est pour toi
     * @param listener
     */
    public void removeQuestionCollectionEventListener(QuestionCollectionEventListener listener) {
        this.eventListeners.remove(listener);
    }

    /**
     * @todo Romano c'est pour toi
     * @param e
     */
    private void onFailed(Exception e) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (QuestionCollectionEventListener listener : eventListeners) {
                    listener.onFailed(e);
                }
            }
        });
    }

    /**
     * @todo Romano c'est pour toi
     * @param questions
     * @param mode
     */
    private void onQuestionsChanged(ArrayList<Question> questions, QuestionMode mode) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                for(QuestionCollectionEventListener listener : eventListeners) {
                    listener.onQuestionsChanged(questions, mode);
                }
            }
        });
    }

    /**
     * used to fetch questions from (Best PO ever) Robin's server.
     * mode is used to fetch the wanted questions.
     * @param mode : quiz mode
     */
    public void fetchQuestions(QuestionMode mode) {
        // create the url to send to server
        String url = "http://gryt.tech:8080/mycheeseornothing/";
        if (mode != QuestionMode.ALL) {
            url += "?difficulty=" + mode.toString().toLowerCase();
        }

        // start request
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onFailed(new Exception("Impossible de récupérer les données.\n" + e.getMessage()));
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = Objects.requireNonNull(response.body()).string();
                ArrayList<Question> questions = new ArrayList<>();
                try {
                    JSONArray json = new JSONArray(body);

                    // loop for each question in the jsonArray
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject question = new JSONObject(json.get(i).toString());

                        // get current question potential answers
                        JSONArray jsonAnswers = question.getJSONArray("responses");
                        String[] answers = new String[jsonAnswers.length()];
                        for (int j = 0; j < jsonAnswers.length(); j++) {
                            answers[j] = jsonAnswers.getString(j);
                        }

                        // add a new question to list
                        questions.add(new Question(
                                question.getString("question"),
                                answers,
                                question.getString("answer"),
                                question.getString("imageName"),
                                QuestionMode.valueOf(question.getString("mode").toUpperCase())));
                    }
                    onQuestionsChanged(questions, mode);
                }
                catch (JSONException e) {
                    onFailed(new Exception("La réponse envoyée par le serveur contient une erreur de JSON !\n" + e.getMessage()));
                }
            }
        });
    }
}
