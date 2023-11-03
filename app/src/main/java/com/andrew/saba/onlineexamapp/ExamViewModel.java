package com.andrew.saba.onlineexamapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andrew.saba.onlineexamapp.data.model.Question;
import com.andrew.saba.onlineexamapp.ui.home.HomeViewModel;

import java.util.ArrayList;


public class ExamViewModel extends ViewModel {
    ArrayList<Question> questions=HomeViewModel.questions;



    MutableLiveData<Boolean> isFinished=new MutableLiveData<>(false);
    int currentQuestionIndex=0;

    MutableLiveData<String> selectedValue=new MutableLiveData<>();

    int grade=0;


    public void setSelectedValue(String value) {
        selectedValue.setValue(value);
    }

    MutableLiveData<Question> currentQuestion= new MutableLiveData<>(questions.get(0));
    int score=0;

    ArrayList<String> selectedAnswers= new ArrayList<>();
    public ExamViewModel() {

    }


    public void getGrade(){
        currentQuestionIndex=0;
        for (int i = 0; i < questions.size(); i++) {
            Question q=questions.get(i);
            String a=selectedAnswers.get(i);
            if (q.answer.equals(a)){
                score++;
            }
        }
        grade= score;
    }

    public void submitSelectedAnswer(){
        selectedAnswers.add(selectedValue.getValue());
    }

  public void nextQuestion(){
        currentQuestionIndex++;
        if (currentQuestionIndex>questions.size()-1) {
            getGrade();
            isFinished.setValue(true);
            return;
        }
        currentQuestion.setValue(questions.get(currentQuestionIndex));
  }
}