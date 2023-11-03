package com.andrew.saba.onlineexamapp.data.model;

import java.util.ArrayList;

public class Question {
    String question;
    String answer;
    ArrayList<String> mcq;


   public Question(String question,String answer,ArrayList<String> mcq){
        this.question=question;
        this.answer=answer;
        this.mcq=mcq;
    }

}
