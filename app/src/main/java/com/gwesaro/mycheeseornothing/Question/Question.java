package com.gwesaro.mycheeseornothing.Question;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Question implements Parcelable {
    public final String question;
    public final String[] answers;
    public final String answer;
    public final String imagePath;
    public final QuestionMode mode;
    public final int modeOrdinal;

    /**
     * Question constructor
     * @param question
     * @param answers
     * @param answer
     * @param imagePath
     * @param mode
     */
    public Question(String question, String[] answers, String answer, String imagePath, QuestionMode mode) {
        this.question = question;
        this.answers = answers;
        this.answer = answer;
        this.imagePath = imagePath;
        this.mode = mode;
        this.modeOrdinal = this.mode.ordinal();
    }


    protected Question(Parcel in) {
        question = in.readString();
        answers = in.createStringArray();
        answer = in.readString();
        imagePath = in.readString();
        modeOrdinal = in.readInt();
        mode = QuestionMode.values()[modeOrdinal];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeStringArray(answers);
        dest.writeString(answer);
        dest.writeString(imagePath);
        dest.writeInt(modeOrdinal);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public boolean isValid(String answer) {
        return this.answer.equals(answer);
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answers=" + Arrays.toString(answers) +
                ", answer='" + answer + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
