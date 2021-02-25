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

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> implements View.OnClickListener {

    List<Question> questions;

    public QuestionAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public void onClick(View v) {
        //todo

    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_questions_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.questionTextView.setText(question.question + "");
        holder.answer1TextView.setText(question.answers[0] + "");
        holder.answer2TextView.setText(question.answers[1] + "");
        holder.answer3TextView.setText(question.answers[2] + "");
        holder.answer4TextView.setText(question.answers[3] + "");
       // holder.imageView.setImageResource(this.getResources().getIdentifier(question.imagePath.split("\\.")[0], "drawable", getPackageName()));

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView questionTextView;
        final TextView answer1TextView;
        final TextView answer2TextView;
        final TextView answer3TextView;
        final TextView answer4TextView;
        final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTextView = itemView.findViewById(R.id.questionTextView);
            answer1TextView = itemView.findViewById(R.id.answer1TextView);
            answer2TextView = itemView.findViewById(R.id.answer2TextView);
            answer3TextView = itemView.findViewById(R.id.answer3TextView);
            answer4TextView = itemView.findViewById(R.id.answer4TextView);
            imageView = itemView.findViewById(R.id.questionImageView);
        }
    }
}