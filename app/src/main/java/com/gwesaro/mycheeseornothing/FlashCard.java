package com.gwesaro.mycheeseornothing;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class FlashCard {

    private String question;
    private int imageRes; // example : R.drawable.ic_launcher_background
    private ArrayList<String> responses;
    private String answer;

    public FlashCard(String question, int imageRes, ArrayList<String> responses, String answer) {
        initialise( question, imageRes, responses, answer);
    }

    public FlashCard(JSONObject json) {
        try {
            JsonHelper jsonHelper = new JsonHelper();
            initialise(json.getString("question"), json.getInt("imageRes"), jsonHelper.jsonToArrayList(json.getJSONArray("responses")), json.getString("answer"));
        }catch (Exception e){
        }
    }

    private void initialise(String question, int imageRes, ArrayList<String> responses, String answer){
        this.question = question;
        this.imageRes = imageRes;
        this.responses = responses;
        shuffleResponses();
        this.answer = answer;
    }



    //region getters / setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public ArrayList<String> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<String> responses) {
        this.responses = responses;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    //endregion

    /**
     * used to shuffle our responses liste
     */
    public void shuffleResponses(){
        Collections.shuffle(this.responses);
    }

    /**
     * used to get current responses list size
     * @return
     */
    public int getResponsesNb() {
        return this.responses.size();
    }

    /**
     * used to get a boolean if a response correspond to our current answer
     * @param response : a string response
     * @return true if response is correct
     */
    public boolean isResponseCorrect(String response){
        return response.equals(this.answer);
    }

    //region getRandomList

    /**
     * used to get a complete random list from our FlashCard thanks to a shuffle
     * @return a list of response
     */
    public ArrayList<String> getRandomList() {
        shuffleResponses();
        return this.getResponses();
    }

    /**
     * used to get a random list from our FlashCard
     * @param size : Array's size to return
     * @return a list of response
     */
    public ArrayList<String> getRandomList(int size) {
        shuffleResponses();
        return (ArrayList<String>) new ArrayList<String>(this.responses.subList(0, size));
    }

    /**
     * used to get a complete random list that should contain the current answer
     * @return a list of response containing answer
     */
    public ArrayList<String> getRandomListWithAnswer() {
        ArrayList<String> list;
        do {
            list = getRandomList();
        }while (!list.contains(this.answer));
        return list;
    }

    /**
     * used to get a random list that should contain the current answer
     * @param size : Array's size to return
     * @return a list of response containing answer
     */
    public ArrayList<String> getRandomListWithAnswer(int size) {
        ArrayList<String> list;
        do {
            list = getRandomList(size);
        }while (!list.contains(this.answer));
        return list;
    }
    //endregion

    @Override
    public String toString() {
        return "FlashCard{" +
                "question='" + question + '\'' +
                ", imageRes=" + imageRes +
                ", responses=" + responses +
                ", answer='" + answer + '\'' +
                '}';
    }

    //pour pouvoir obtenir les reponses dans un ordre randomisé et limité en size(), contenant Nécessairement l' answer de la FlashCard
}
