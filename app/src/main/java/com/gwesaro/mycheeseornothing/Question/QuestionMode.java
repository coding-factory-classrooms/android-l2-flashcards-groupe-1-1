package com.gwesaro.mycheeseornothing.Question;

public enum QuestionMode {
    ALL,
    EASY,
    MEDIUM,
    HARD;

    public String getModeFrench() {
        return getModeFrenchOf(this);
    }

    public String getModeFrenchOf(QuestionMode mode) {
        switch (mode) {
            case EASY: return "Facile";
            case MEDIUM: return "Moyen";
            case HARD: return "Difficile";
            case ALL:
            default:
                return "Aléatoire";
        }
    }
}
