package com.gwesaro.mycheeseornothing.Question;

import java.util.EventListener;

public interface QuestionCollectionFailedListener extends EventListener {

    void onFailed(Exception e);
}
