package com.gwesaro.mycheeseornothing;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Questions {
    private String mode ; // easy , medium, hard
    private int nbQuestions; // total quiz nb questionn
    private JSONObject questions;// (array containing 4 fields necessary for FlashCard (#3))
    private ArrayList<Integer> indexList;
    private int questionCounter=0;
    private String[] userResponses; //could be more usefull, because at end, could display list of question and if answer is good


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
        this.setUserResponses(new String[this.getNbQuestions()]);
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

    public String[] getUserResponses() {
        return userResponses;
    }

    public void setUserResponses(String[] userResponses) {
        this.userResponses = userResponses;
    }

    //endregion

    /**
     * @todo probably could be delete
     * get a JSONObject from another, containing field 'questions', and set 'questions' attributes
     * @param json : a JSONObject, get from file or HTTP request
     */
    public void setQuestionsByCompleteJson(JSONObject json) {
        try{
            this.questions = json.getJSONObject("questions");
        }catch (Exception e){
        }
    }

    //region get a question
    /**
     * Used to get the current question, depending on current mode,
     * current indexList and current question counter
     * @return
     */
    public JSONObject getCurrentQuestion(){
        try {
            return this.getQuestionAtIndex(this.questionCounter);
        }catch (Exception e){
        }
        return null;
    }

    public JSONObject getQuestionAtIndex(int index){
        try {
            return this.getQuestions().getJSONArray(this.mode).getJSONObject( this.getCurrentIndexFromList(index));
        }catch (Exception e){
        }
        return null;
    }
    //endregion


    /**
     * Used to set a boolean in booleanArray at a specified location
     * @param index : target index
     * @param userResponse  : user's response
     */
    public void setUserResponse(int index, String userResponse) {
       this.userResponses[index] = userResponse;
    }

    /**
     * Used to set a boolean in booleanArray at current question location
     * @param userResponse : user's response
     */
    public void setUserResponse(String userResponse) {
        this.setUserResponse(this.getQuestionCounter(), userResponse);
    }



    /**
     * used to get the current question index from our JSON
     * @param index :
     * @return current question index
     */
    public int getCurrentIndexFromList(int index){
        return this.getIndexList().get(index);
    }

    //endregion


    public boolean isAnswerAtIndexCorrect(int index){
        return this.getUserResponses()[index].equals(this.getAnswerAtIndex(index));
    }

    public String getAnswerAtIndex(int index){
        try {
            return this.getQuestionAtIndex(index).getString("answer");
        }catch (Exception e){
        }
        return null;
    }

    //region stats

    /**
     * used to get the current correct answer count
     * @return correct count number
     */
    public int getAnswerCount() {
        int count = 0;
        for (int i =0 ; i<this.getNbQuestions(); i++){
            if (isAnswerAtIndexCorrect(i)){
                count ++;
            }
        }
        return count;
    }

    /**
     * used to get a success percent depending on current correct answer count and
     * total question number
     * @return success percentage
     */
    public float getSuccessPercent(){
        return (float) this.getAnswerCount() / this.getNbQuestions() * 100;
    }

    /**
     * used to get a string with quiz rate
     * @return success percentage
     */
    public String getQuizRate(){
        return  this.getAnswerCount() + "/" + this.getNbQuestions() ;
    }


    /**
     * used to return a resume of the quiz (all question, with answer and boolean if have correctly answered)
     */
    public ArrayList<Map<String, Object>> getQuizSumary(){
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (int i=0; i<this.getNbQuestions(); i++){
            try {
                JSONObject json = getQuestionAtIndex(i);
                HashMap<String, Object> map = new HashMap<>();
                map.put("question", json.getString("question") );
                map.put("answer", json.getString("answer"));
                map.put("isCorrect", isAnswerAtIndexCorrect(i) );
                list.add(map);
            }catch (Exception e){
                Log.e("MainAcc", e.toString());
            }
        }
        return list;
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
     * @todo risque de retomber souvent sur les meme index dans le for
     * faire une copie du tableau de questions, delete celui trouver,
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

        /*for(int i = 0; i < initSize; i++){
            int random = getRandomNumber(0, jsonSize);
            if (this.indexList.contains(random)){
                i--;
            }
            else{
                this.indexList.add(random);
            }
        }*/

        ArrayList<Integer> array = new ArrayList<>(initSize);
        //ArrayList<Integer> input = new ArrayList<>(initSize);
        for(int i=0;i<jsonSize;i++){
            array.add(i);
        }

        Collections.shuffle(array);

        for(int j=0;j<initSize;j++){
            this.indexList .add(array.get(j));
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
                ", userResponses=" + Arrays.toString(userResponses) +
                '}';
    }
}
