package com.example.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class FlashCardDetailsActivity extends AppCompatActivity {

    EditText questionEdit,answerEdit;
    ImageButton saveCardBtn;
    TextView pageTitle;
    String question,answer,docId;
    boolean isEditMode = false;
    TextView deleteCardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card_details);

        questionEdit = findViewById(R.id.enter_question);
        answerEdit = findViewById(R.id.enter_answer);
        saveCardBtn = findViewById(R.id.savecard_btn);
        pageTitle = findViewById(R.id.page_title);
        deleteCardBtn = findViewById(R.id.deletecard_btn);

        //receive Data
        question = getIntent().getStringExtra("question");
        answer = getIntent().getStringExtra("answer");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        questionEdit.setText(question);
        answerEdit.setText(answer);

        if(isEditMode){
            pageTitle.setText("Edit Card");
            deleteCardBtn.setVisibility(View.VISIBLE);
        }


        saveCardBtn.setOnClickListener((v -> saveCard()));
        deleteCardBtn.setOnClickListener((v) -> deleteCardFromFirebase());
    }

    void saveCard(){
        String cardquestion = questionEdit.getText().toString();
        String cardanswer = answerEdit.getText().toString();
        if(cardquestion==null || cardquestion.isEmpty()){
            questionEdit.setError("Question is Required");
            return;
        }

        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion(cardquestion);
        flashcard.setAnswer(cardanswer);
        flashcard.setTimestamp(Timestamp.now());

        saveCardToFirebase(flashcard);

    }

    void saveCardToFirebase(Flashcard flashcard){
        DocumentReference documentReference;
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }
        else{
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(flashcard).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note added
                    Utility.showToast(FlashCardDetailsActivity.this,"FlashCard Added Successfully");
                    finish();
                }
                else{
                    //note not added
                    Utility.showToast(FlashCardDetailsActivity.this,"Failed While Adding FlashCard");
                }
            }
        });
    }

    void deleteCardFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note deleted
                    Utility.showToast(FlashCardDetailsActivity.this,"FlashCard Deleted Successfully");
                    finish();
                }
                else{
                    //note not deleted
                    Utility.showToast(FlashCardDetailsActivity.this,"Failed While Deleting FlashCard");
                }
            }
        });
    }
}