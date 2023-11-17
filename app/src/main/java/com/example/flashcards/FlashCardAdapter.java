package com.example.flashcards;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.Flashcard;
import com.example.flashcards.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FlashCardAdapter extends FirestoreRecyclerAdapter<Flashcard, FlashCardAdapter.FlashCardViewHolder> {

    Context context;

    public FlashCardAdapter(@NonNull FirestoreRecyclerOptions<Flashcard> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FlashCardViewHolder holder, int position, @NonNull Flashcard flashcard) {
        holder.questionText.setText(flashcard.question);
        holder.answerText.setText(flashcard.answer);
        holder.timestampText.setText(Utility.timestampToString(flashcard.timestamp));

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context,FlashCardDetailsActivity.class);
            intent.putExtra("question",flashcard.question);
            intent.putExtra("answer",flashcard.answer);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public FlashCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_flashcard_item,parent,false);
        return new FlashCardViewHolder(view);
    }



    class FlashCardViewHolder extends RecyclerView.ViewHolder{

        TextView questionText,answerText,timestampText;

        public FlashCardViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.card_question);
            answerText = itemView.findViewById(R.id.card_answer);
            timestampText = itemView.findViewById(R.id.card_timestamp);
        }
    }
}
