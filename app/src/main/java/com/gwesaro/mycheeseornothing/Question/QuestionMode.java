package com.gwesaro.mycheeseornothing.Question;

public enum QuestionMode {
    ALL,
    EASY,
    MEDIUM,
    HARD;

    /**
     * return the mode in French
     * @return
     */
    public String getModeFrench() {
        return getModeFrenchOf(this);
    }

    /**
     * return the mode in French from a mode
     * @param mode
     * @return
     */
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
