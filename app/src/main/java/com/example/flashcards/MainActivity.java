package com.example.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button addcardBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    FlashCardAdapter cardAdapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addcardBtn = findViewById(R.id.addButton);
        recyclerView = findViewById(R.id.recyclerview);
        menuBtn = findViewById(R.id.menu);

        addcardBtn.setOnClickListener((v -> startActivity(new Intent(MainActivity.this,FlashCardDetailsActivity.class))));
        menuBtn.setOnClickListener((v -> showMenu()));
        setupRecyclerView();

    }

    void showMenu(){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    void setupRecyclerView(){
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Flashcard> options = new FirestoreRecyclerOptions.Builder<Flashcard>()
                .setQuery(query,Flashcard.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardAdapter = new FlashCardAdapter(options,this);
        recyclerView.setAdapter(cardAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cardAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cardAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardAdapter.notifyDataSetChanged();
    }
}