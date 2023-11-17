package com.example.flashcards;

import com.google.firebase.Timestamp;

public class Flashcard {
    String question;
    String answer;
    Timestamp timestamp;

    public Flashcard() {
    }

    public Flashcard(String question, String answer, Timestamp timestamp) {
        this.question = question;
        this.answer = answer;
        this.timestamp = timestamp;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
