package com.gwesaro.mycheeseornothing;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gwesaro.mycheeseornothing.Question.Question;
import com.gwesaro.mycheeseornothing.Question.Quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> implements View.OnClickListener {

    List<Question> questions;

    public QuestionAdapter(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * Override method click to navigate to the right flashCard with the right data retrieve from the Tag
     * @param v : current view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rootItem:
                Context context = v.getContext();
                Question question = (Question) v.getTag();
                ArrayList<Question> list = new ArrayList<Question>();
                list.add(question);
                Quiz quiz = new Quiz(list, question.mode);
                quiz.mixQuestions();
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("quiz", quiz);
                context.startActivity(intent);
                break;
        }
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_questions_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Override method onBindView holder to set the correct data to the recyclerView
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        Question question = questions.get(position);

        // build a string to avoid question title to overflow its parent view
        StringBuilder answer = new StringBuilder();
        for (int i = 0; answer.length()<37 && i < question.answers.length; i++) {
            if (i != 0) {
                answer.append(" ~ ");
            }
            answer.append(question.answers[i]);
        }

        // cut answers and question if String is too long
        String answerStr = new String(answer);
        String questionStr = question.question;

        holder.listQuestionTextView.setText(questionStr);
        holder.listAnswerTextView.setText(answerStr);
        holder.listModeTextView.setText(question.mode.getModeFrench());

        // set item image
        holder.itemView.setTag(question);
        switch (question.mode) {
            case EASY:
                holder.iconListImageView.setImageResource(R.drawable.logo_easy);
                break;
            case MEDIUM:
                holder.iconListImageView.setImageResource(R.drawable.logo_medium);
                break;
            case HARD:
                holder.iconListImageView.setImageResource(R.drawable.logo_hard);
                break;
            case ALL:
            default:
                holder.iconListImageView.setImageResource(R.drawable.logo2);
                break;
        }
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView listQuestionTextView;
        final TextView listModeTextView;
        final ImageView iconListImageView;
        final TextView listAnswerTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listQuestionTextView = itemView.findViewById(R.id.listQuestionTextView);
            listModeTextView = itemView.findViewById(R.id.listModeTextView);
            listAnswerTextView = itemView.findViewById(R.id.listAnswerTextView);
            iconListImageView = itemView.findViewById(R.id.iconListImageView);
        }
    }
}