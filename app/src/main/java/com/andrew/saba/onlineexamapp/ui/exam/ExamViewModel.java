package com.andrew.saba.onlineexamapp.ui.exam;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andrew.saba.onlineexamapp.data.model.Question;
import com.andrew.saba.onlineexamapp.ui.home.HomeViewModel;

import java.util.ArrayList;


public class ExamViewModel extends ViewModel {

    // Questions list
    ArrayList<Question> questions=HomeViewModel.questions;

    // Total user grade
    int grade=0;

    // Store index of current question
    int currentQuestionIndex=0;

    // boolean detect if questions are all solved
    MutableLiveData<Boolean> isFinished=new MutableLiveData<>(false);


    // Store current question to show in UI
    MutableLiveData<Question> currentQuestion= new MutableLiveData<>(questions.get(0));


    // Store the value user selected
    MutableLiveData<String> selectedValue=new MutableLiveData<>();

    // Store answers entered by the user
    ArrayList<String> selectedAnswers= new ArrayList<>();


    //Update selected value
    public void setSelectedValue(String value) {
        selectedValue.setValue(value);
    }

    // Calculate user grade
    public void getGrade(){
        currentQuestionIndex=0;
        for (int i = 0; i < questions.size(); i++) {
            Question q=questions.get(i);
            String a=selectedAnswers.get(i);
            if (q.answer.equals(a)){
                grade++;
            }
        }
    }

    // Add answer select by the user to selected answers list
    public void submitSelectedAnswer(){
        selectedAnswers.add(selectedValue.getValue());
    }

    // Move to next question
     public void nextQuestion(){
        currentQuestionIndex++;
        // If there is no other questions the calculate user grade and finish the exam
        if (currentQuestionIndex>questions.size()-1) {
            getGrade();
            isFinished.setValue(true);
            return;
        }
        currentQuestion.setValue(questions.get(currentQuestionIndex));
    }
}