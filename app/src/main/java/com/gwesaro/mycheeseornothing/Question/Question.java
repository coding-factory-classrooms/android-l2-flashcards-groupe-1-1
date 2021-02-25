package com.gwesaro.mycheeseornothing.Question;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    public final String question;
    public final String[] answers;
    public final String answer;
    public final String imagePath;

    public Question(String question, String[] answers, String answer, String imagePath) {
        this.question = question;
        this.answers = answers;
        this.answer = answer;
        this.imagePath = imagePath;
    }

    public boolean isValid(String answer) {
        return this.answer.equals(answer);
    }

    protected Question(Parcel in) {
        question = in.readString();
        answers = in.createStringArray();
        answer = in.readString();
        imagePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeStringArray(answers);
        dest.writeString(answer);
        dest.writeString(imagePath);
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
}
