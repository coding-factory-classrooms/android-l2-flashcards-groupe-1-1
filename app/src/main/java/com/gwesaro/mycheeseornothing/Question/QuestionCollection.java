package com.gwesaro.mycheeseornothing.Question;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionCollection implements Parcelable {

    private QuestionMode mode;
    private final int modeOrdinal;
    private List<Question> questions;
    private int[] mixedIndex;
    private int indexQuestion;
    private Question currentQuestion;
    private int validAnswersCount;

    private static String TAG = "QuestionCollection";

    private Collection<QuestionsChangedListener> questionsListeners;
    private Collection<QuestionCollectionFailedListener> failedListeners;

    public QuestionCollection(QuestionMode mode) {
        this.mode = mode;
        this.modeOrdinal = this.mode.ordinal();
        this.questions = new ArrayList<>();
        this.mixedIndex = new int[0];
        this.validAnswersCount = 0;
        this.indexQuestion = 0;
        this.currentQuestion = null;
        this.questionsListeners = new ArrayList<QuestionsChangedListener>();
        this.failedListeners = new ArrayList<QuestionCollectionFailedListener>();
        fetchQuestions();
    }

    public void addQuestionsChangedListener(QuestionsChangedListener listener) {
        this.questionsListeners.add(listener);
    }

    public void removeQuestionsChangedListener(QuestionsChangedListener listener) {
        this.questionsListeners.remove(listener);
    }

    private void onQuestionsChanged() {
        for(QuestionsChangedListener listener : this.questionsListeners) {
            listener.onQuestionsChanged();
        }
    }

    public void addQuestionCollectionFailedListener(QuestionCollectionFailedListener listener) {
        this.failedListeners.add(listener);
    }

    public void removeQuestionCollectionFailedListener(QuestionCollectionFailedListener listener) {
        this.failedListeners.remove(listener);
    }

    private void onFailed(Exception e) {
        for(QuestionCollectionFailedListener listener : this.failedListeners) {
            listener.onFailed(e);
        }
    }

    private void fetchQuestions() {
        String url = "http://gryt.tech:8080/mycheeseornothing/";
        if (this.mode != QuestionMode.ALL) {
            url += "?difficulty=" + this.mode.toString().toLowerCase();
        }
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
                questions.clear();
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
                    onFailed(new Exception("La réponse envoyée par le serveur contient une erreur de JSON !\n" + e.getMessage()));
                    return;
                }
                mixQuestions();
            }
        });
    }

    private int getNumberOfQuestions() {
        switch (this.mode) {
            case ALL: return this.questions.size();
            case EASY: return 5;
            case MEDIUM: return 8;
            case HARD: return 11;
            default: return 0;
        }
    }

    private void mixQuestions() {
        this.currentQuestion = null;
        this.mixedIndex = new int[Math.min(getNumberOfQuestions(), questions.size())];
        List<Integer> allIndex = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            allIndex.add(i);
        }
        Random random = new Random();
        for (int i = 0; i < mixedIndex.length; i++) {
            mixedIndex[i] = allIndex.remove(random.nextInt(allIndex.size()));
        }
        onQuestionsChanged();
    }

    public Question get(int index) {
        return questions.get(index);
    }

    public int getValidAnswersCount() {
        return validAnswersCount;
    }

    public int getQuestionsCount() {
        return this.mixedIndex.length;
    }

    public QuestionMode getMode() {
        return this.mode;
    }

    public boolean hasNext() {
        return this.indexQuestion + 1 < mixedIndex.length;
    }

    public Question getNextQuestion() {
        this.indexQuestion = currentQuestion == null ? 0 : this.indexQuestion + 1;
        currentQuestion = get(mixedIndex[this.indexQuestion]);
        return currentQuestion;
    }

    public boolean CheckAnswer(String answer) {
        if(currentQuestion.isValid(answer)) {
            this.validAnswersCount++;
            return true;
        }
        return false;
    }

    protected QuestionCollection(Parcel in) {
        modeOrdinal = in.readInt();
        mode = QuestionMode.values()[modeOrdinal];
        questions = in.createTypedArrayList(Question.CREATOR);
        mixedIndex = in.createIntArray();
        indexQuestion = in.readInt();
        currentQuestion = in.readParcelable(Question.class.getClassLoader());
        validAnswersCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(modeOrdinal);
        dest.writeTypedList(questions);
        dest.writeIntArray(mixedIndex);
        dest.writeInt(indexQuestion);
        dest.writeParcelable(currentQuestion, flags);
        dest.writeInt(validAnswersCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionCollection> CREATOR = new Creator<QuestionCollection>() {
        @Override
        public QuestionCollection createFromParcel(Parcel in) {
            return new QuestionCollection(in);
        }

        @Override
        public QuestionCollection[] newArray(int size) {
            return new QuestionCollection[size];
        }
    };
}
