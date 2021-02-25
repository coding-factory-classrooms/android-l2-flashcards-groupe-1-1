package com.gwesaro.mycheeseornothing.Question;

import java.util.ArrayList;
import java.util.EventListener;

public interface QuestionCollectionEventListener extends EventListener {

    void onFailed(Exception e);

    void onQuestionsChanged(ArrayList<Question> questions, QuestionMode mode);
}
