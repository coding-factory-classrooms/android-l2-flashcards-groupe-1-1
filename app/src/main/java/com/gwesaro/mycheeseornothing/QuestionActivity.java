package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

public class QuestionActivity extends AppCompatActivity {

    private final String TAG = "QuestionActivity";
    private Questions quest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);



        try {
            JsonHelper jHelper = new JsonHelper();
            String str = jHelper.loadJSONFromRes(getBaseContext());
            JSONObject obj = new JSONObject(str);

            this.quest = new Questions("easy", obj);
            Log.i(TAG, this.quest.toString());

            /*
            Log.i(TAG, this.quest.toString());
            Log.i(TAG, this.quest.getCurrentQuestion().toString());
            this.quest.setUserResponse("Parmesan");

            Log.i(TAG, this.quest.getFurtherQuestion().toString());

            this.quest.setUserResponse("Parmesan");

            Log.i(TAG, this.quest.toString());
            Log.i(TAG, this.quest.getAnswerCount() + "/" + this.quest.getNbQuestions() + " -- " + this.quest.getSuccessPercent() + "%");
            Log.i(TAG, this.quest.getQuizSumary().toString());

            */



            updateInterface();
            //go to Stats (end finish quiz)
            //navigateToStats();

        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }


    private void updateInterface(){
        Log.i(TAG, this.quest.getCurrentQuestion().toString());

        FlashCard card = new FlashCard(  this.quest.getCurrentQuestion());
        Log.i(TAG, card.toString());

        //ImageView cheeseImageView = findViewById(R.id.cheeseImageView);
        //cheeseImageView.setImageResource();
        TextView questionTextView = findViewById(R.id.questionTextView);
        questionTextView.setText(card.getQuestion());
    }

    private void navigateToStats() {
        Intent intent = new Intent(this, StatsActivity.class);
        intent.putExtra("percent" , this.quest.getSuccessPercent());
        intent.putExtra("rate" , this.quest.getQuizRate());
        intent.putExtra("mode" , this.quest.getMode());
        startActivity(intent);
    }


}