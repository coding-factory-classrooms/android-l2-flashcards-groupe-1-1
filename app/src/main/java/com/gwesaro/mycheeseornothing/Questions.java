package com.gwesaro.mycheeseornothing;

import android.text.BoringLayout;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Questions {
    private String mode ; // easy , medium, hard
    private int nbQuestions; // total quiz nb questionn
    private JSONObject questions;// (array containing 4 fields necessary for FlashCard (#3))
    private ArrayList<Integer> indexList;
    private int questionCounter=0;
    private Boolean[] booleanArray;


    public Questions(String mode,  JSONObject json, int nbQuestions) {
        initialize(mode, nbQuestions, json);
    }

    public Questions(String mode, JSONObject json) {
        initialize(mode, this.getModeNbQuestion(mode), json);
    }

    /**
     *
     * @param mode : easy, medium or hard (name used in JSON)
     * @param nbQuestions
     * @param json : typically having a 'questions' field
     */
    private void initialize(String mode, int nbQuestions, JSONObject json){
        this.mode = mode;
        this.nbQuestions = nbQuestions;
        setQuestionsByCompleteJson(json);
        resetIndexList();
    }

    public int getModeNbQuestion() {
        return getModeNbQuestion(this.mode);
    }

    public int getModeNbQuestion(String mode) {
        switch (mode){
            case "easy" :
               return  3 ;
            case "medium" :
                return 4;
            case "hard" :
                return 5;
            default:
                return 0;
        }
    }

    //region getter / setter

    //region classical
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getNbQuestions() {
        return nbQuestions;
    }

    public void setNbQuestions(int nbQuestions) {
        this.nbQuestions = nbQuestions;
    }

    public JSONObject getQuestions() {
        return questions;
    }

    public void setQuestions(JSONObject questions) {
        this.questions = questions;
    }

    public ArrayList<Integer> getIndexList() {
        return indexList;
    }

    public void setIndexList(ArrayList<Integer> indexList) {
        this.indexList = indexList;
    }

    public int getQuestionCounter() {
        return questionCounter;
    }

    public void setQuestionCounter(int questionCounter) {
        this.questionCounter = questionCounter;
    }

    public Boolean[] getBooleanArray() {
        return booleanArray;
    }

    public void setBooleanArray(Boolean[] booleanArray) {
        this.booleanArray = booleanArray;
    }

    //endregion



    public void setQuestionsByCompleteJson(JSONObject json) {
        try{
            this.questions = json.getJSONObject("questions");
        }catch (Exception e){
        }
    }

    public JSONObject getCurrentQuestion(){
        try {
            return this.getQuestions().getJSONArray(this.mode).getJSONObject(this.questionCounter);
        }catch (Exception e){
        }
        return null;
    }


    public void setBooleanArray(int index, boolean bool) {
       this.booleanArray[index] = bool;
    }
    public void setBooleanArray(boolean bool) {
        this.booleanArray[this.getQuestionCounter()] = bool;
    }



    //endregion

    //region stats
    public int getAnswerCount(Boolean[] arrayOfSheeps) {
        int count = 0;
        for (Boolean b : arrayOfSheeps) if (b) count++;
        return count;
    }
    //endregion


    public void  resetGame(){
        this.setQuestionCounter(0);
        this.resetIndexList();
    }

    /**
     * Used to initialise question's list index we will display to user.
     */
    private void resetIndexList(){
        int initSize = 0; // supposed to be better for perf
        try {
            initSize = this.questions.getJSONArray("easy").length();
        }catch (Exception e){
        }
        this.indexList = new ArrayList<>(initSize);
        for(int i = 0; i < initSize; i++){
            this.indexList.add(i);
        }
        shuffleIndex();
    }

    public void shuffleIndex(){
        Collections.shuffle(this.indexList);
    }


    public JSONObject getFurtherQuestion(){
        this.questionCounter ++;
        return this.getCurrentQuestion();
    }


    @Override
    public String toString() {
        return "Questions{" +
                "mode='" + mode + '\'' +
                ", nbQuestions=" + nbQuestions +
                ", questions=" + questions +
                ", indexList=" + indexList +
                ", questionCounter=" + questionCounter +
                ", booleanArray=" + Arrays.toString(booleanArray) +
                '}';
    }
}
