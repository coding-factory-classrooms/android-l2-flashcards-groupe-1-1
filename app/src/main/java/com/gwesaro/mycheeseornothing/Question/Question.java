package com.gwesaro.mycheeseornothing.Question;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.gwesaro.mycheeseornothing.MainActivity;

public class Question implements Parcelable {
    private final String question;
    private final String[] answers;
    private final String answer;
    private final String imagePath;
    private Drawable image;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Question(String question, String[] answers, String answer, String imagePath) {
        this.question = question;
        this.answers = answers;
        this.answer = answer;
        this.imagePath = imagePath;
        this.image = MainActivity.getDrawableFromImagePath(this.imagePath);
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
