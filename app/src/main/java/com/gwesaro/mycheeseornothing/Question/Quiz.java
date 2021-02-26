package com.gwesaro.mycheeseornothing.Question;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Quiz implements Parcelable {

    private final QuestionMode mode;
    private final int modelOrdinal;
    private int[] mixedIndex;
    private int indexQuestion;
    private Question currentQuestion;
    private int validAnswersCount;
    private final ArrayList<Question> questions;

    /**
     * Quiz constructor
     * @param questions
     * @param mode
     */
    public Quiz(ArrayList<Question> questions, QuestionMode mode) {
        this.questions = questions;
        this.mode = mode;
        this.modelOrdinal = this.mode.ordinal();
        this.mixedIndex = new int[0];
        this.validAnswersCount = 0;
        this.indexQuestion = 0;
        this.currentQuestion = null;
    }

    /**
     * return the theoretical number of questions.
     * @return
     */
    private int getNumberOfQuestions() {
        switch (this.mode) {
            case ALL: return this.questions.size();
            case EASY: return 5;
            case MEDIUM: return 8;
            case HARD: return 11;
            default: return 0;
        }
    }

    /**
     * Mix the questions's indexes
     */
    public void mixQuestions() {
        this.currentQuestion = null;
        this.mixedIndex = new int[Math.min(getNumberOfQuestions(), questions.size())];

        // create a copy of index list to mix easely our questions
        List<Integer> allIndex = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            allIndex.add(i);
        }

        // mix our questions' index list
        Random random = new Random();
        for (int i = 0; i < mixedIndex.length; i++) {
            mixedIndex[i] = allIndex.remove(random.nextInt(allIndex.size()));
        }
    }

    /**
     * Mix the questions' answers string
     * @param answers : String array of a question's answers
     * @return
     */
    public String[] mixAnswers(String[] answers) {
        List<String> strList = Arrays.asList(answers);
        Collections.shuffle(strList);
        answers = strList.toArray(new String[strList.size()]);
        return answers;
    }

    /**
     * return the question to the given index
     * @param index
     * @return
     */
    public Question get(int index) {
        return questions.get(index);
    }

    /**
     * return the valid answers count
     * @return
     */
    public int getValidAnswersCount() {
        return validAnswersCount;
    }

    /**
     * return the question's index
     * @return
     */
    public int getIndexQuestion() {
        return indexQuestion;
    }

    /**
     * return the question count
     * @return
     */
    public int getQuestionsCount() {
        return this.mixedIndex.length;
    }

    /**
     * return the question's mode
     * @return
     */
    public QuestionMode getMode() {
        return this.mode;
    }

    /**
     * return true if there is a next question index
     * @return
     */
    public boolean hasNext() {
        return this.indexQuestion + 1 < mixedIndex.length;
    }

    /**
     * return the next question
     * @return
     */
    public Question getNextQuestion() {
        this.indexQuestion = currentQuestion == null ? 0 : this.indexQuestion + 1;
        currentQuestion = get(mixedIndex[this.indexQuestion]);
        return currentQuestion;
    }

    /**
     * return the current question
     * @return
     */
    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    /**
     * check if the given answer is the right one, and increment validAnswersCount
     * @param answer
     * @return
     */
    public boolean CheckAnswer(String answer) {
        if(currentQuestion.isValid(answer)) {
            this.validAnswersCount++;
            return true;
        }
        return false;
    }

    protected Quiz(Parcel in) {
        modelOrdinal = in.readInt();
        mode = QuestionMode.values()[modelOrdinal];
        mixedIndex = in.createIntArray();
        indexQuestion = in.readInt();
        currentQuestion = in.readParcelable(Question.class.getClassLoader());
        validAnswersCount = in.readInt();
        questions = in.createTypedArrayList(Question.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(modelOrdinal);
        dest.writeIntArray(mixedIndex);
        dest.writeInt(indexQuestion);
        dest.writeParcelable(currentQuestion, flags);
        dest.writeInt(validAnswersCount);
        dest.writeTypedList(questions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };
}
