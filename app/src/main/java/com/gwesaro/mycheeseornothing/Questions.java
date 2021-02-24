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


    //region constructor / init
    public Questions(String mode,  JSONObject json, int nbQuestions) {
        initialize(mode, nbQuestions, json);
    }

    public Questions(String mode, JSONObject json) {
        initialize(mode, this.getModeNbQuestion(mode), json);
    }

    /**
     * initialise a questions time.
     * @param mode : easy, medium or hard (name used in JSON)
     * @param nbQuestions : nb questions for this quiz
     * @param json : typically having a 'questions' field
     */
    private void initialize(String mode, int nbQuestions, JSONObject json){
        this.mode = mode;
        this.nbQuestions = nbQuestions;
        setQuestionsByCompleteJson(json);
        resetIndexList();
        this.setBooleanArray(new Boolean[this.getNbQuestions()]);
    }

    /**
     * used to get current mode question number
     * @return
     */
    public int getModeNbQuestion() {
        return getModeNbQuestion(this.mode);
    }

    /**
     * used to get an amount of question depending on given mode
     * @param mode
     * @return
     */
    public int getModeNbQuestion(String mode) {
        switch (mode){
            case "easy" :
               return  2 ;
            case "medium" :
                return 3;
            case "hard" :
                return 5;
            default:
                return 0;
        }
    }
    //endregion

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

    /**
     * get a JSONObject from another, containing field 'questions', and set 'questions' attributes
     * @param json : a JSONObject, get from file or HTTP request
     */
    public void setQuestionsByCompleteJson(JSONObject json) {
        try{
            this.questions = json.getJSONObject("questions");
        }catch (Exception e){
        }
    }

    /**
     * Used to get the current question, depending on current mode,
     * current indexList and current question counter
     * @return
     */
    public JSONObject getCurrentQuestion(){
        try {
            return this.getQuestions().getJSONArray(this.mode).getJSONObject( this.getCurrentIndex(this.questionCounter));
        }catch (Exception e){
        }
        return null;
    }


    /**
     * Used to set a boolean in booleanArray at a specified location
     * @param index : target index
     * @param bool  : the new boolean
     */
    public void setBooleanArray(int index, boolean bool) {
       this.booleanArray[index] = bool;
    }

    /**
     * Used to set a boolean in booleanArray at current question location
     * @param bool
     */
    public void setBooleanArray(boolean bool) {
        this.setBooleanArray(this.getQuestionCounter(), bool);
    }


    /**
     * used to get the current question index from our JSON
     * @param index :
     * @return current question index
     */
    public int getCurrentIndex(int index){
        return this.getIndexList().get(index);
    }

    //endregion

    //region stats

    /**
     * used to get the current correct answer count
     * @return correct count number
     */
    public int getAnswerCount() {
        int count = 0;
        for (Boolean b : this.getBooleanArray()) if (b) count++;
        return count;
    }

    /**
     * used to get a success percent depending on current correct answer count and
     * total question number
     * @return
     */
    public float getSuccessPercent(){
        return (float) this.getAnswerCount() / this.getNbQuestions() * 100;
    }
    //endregion


    public void  resetGame(){
        this.setQuestionCounter(0);
        this.resetIndexList();
    }


    /**
     * used to get a random number, between min included and max excluded
     * @param min : min int
     * @param max : max int
     * @return a random int between the 2 values
     */
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Used to initialise question's list index we will display to user.
     * first, we define how much questions will be insert.
     * Then create a random size limited list
     */
    private void resetIndexList(){
        int initSize = this.getNbQuestions();// supposed to be better for perf
        int jsonSize = 0;
        try {
            jsonSize = this.questions.getJSONArray(this.getMode()).length();
            if (jsonSize<initSize){
                initSize = jsonSize;
            }
        }catch (Exception e){
        }

        //region create size limited list
        this.indexList = new ArrayList<>(initSize);

        for(int i = 0; i < initSize; i++){
            int random = getRandomNumber(0, jsonSize);
            if (this.indexList.contains(random)){
                i--;
            }
            else{
                this.indexList.add(random);
            }
        }
        //endregion

    }


    /**
     * used to get the further questions.
     * First, increment the questionCounter.
     * Then, return the current question
     * @return
     */
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
