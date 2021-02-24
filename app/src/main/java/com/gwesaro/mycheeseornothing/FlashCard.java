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

    public void shuffleResponses(){
        Collections.shuffle(this.responses);
    }
    public int getResponsesNb() {
        return this.responses.size();
    }
    public boolean isResponseCorrect(String response){
        return response.equals(this.answer);
    }

    //region getRandomList
    public ArrayList<String> getRandomList() {
        return getRandomList(this.getResponsesNb());
    }

    public ArrayList<String> getRandomList(int size) {
        shuffleResponses();
        return (ArrayList<String>) new ArrayList<String>(this.responses.subList(0, size));
    }

    public ArrayList<String> getRandomListWithAnswer() {
        ArrayList<String> list;
        do {
            list = getRandomList();
        }while (!list.contains(this.answer));
        return list;
    }

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
