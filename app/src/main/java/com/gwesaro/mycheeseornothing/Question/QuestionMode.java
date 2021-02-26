package com.gwesaro.mycheeseornothing.Question;

public enum QuestionMode {
    ALL,
    EASY,
    MEDIUM,
    HARD;

    /**
     * return the mode in French
     * @return : mode's french equivalent
     */
    public String getModeFrench() {
        return getModeFrench(this);
    }

    /**
     * return the mode in French from a given mode
     * @param mode : a QuestionMode
     * @return : given mode's french equivalent
     */
    public String getModeFrench(QuestionMode mode) {
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
