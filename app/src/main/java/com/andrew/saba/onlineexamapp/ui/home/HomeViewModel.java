package com.andrew.saba.onlineexamapp.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andrew.saba.onlineexamapp.data.model.Question;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    // Database object
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Database fields names
    private static final String ANSWER = "answer";
    private static final String MCQ = "mcq";
    private static final String QUESTION = "question";

    // Questions list
     public final static ArrayList<Question> questions=new ArrayList<>();

     // Flag to know if error happened in connection with Firebase
    public MutableLiveData<Boolean> connected= new MutableLiveData<>(true);

    public HomeViewModel() {


        // Retrieve questions data from Firebase
        db.collection("questions").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String questionText=  document.getString(QUESTION);
                            String answer=  document.getString(ANSWER);
                            ArrayList<String> mcq = (ArrayList<String>) document.get(MCQ);
                            Question q=new Question(questionText,answer,mcq);
                            questions.add(q);
                        }
                        connected.setValue(true);
                    } else {
                        Log.w("DB", "Error getting documents.", task.getException());
                        connected.setValue(false);
                    }
                }
        );
    }
}