package com.andrew.saba.onlineexamapp.ui.exam;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.andrew.saba.onlineexamapp.R;
import com.andrew.saba.onlineexamapp.databinding.FragmentExamBinding;

public class ExamFragment extends Fragment {

    // Declare view model object
    private ExamViewModel viewModel;

    public static ExamFragment newInstance() {
        return new ExamFragment();
    }

    // Binding object
    private FragmentExamBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Initialize binding object
        binding=FragmentExamBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initialize  view model object
        viewModel = new ViewModelProvider(this).get(ExamViewModel.class);

        // Set check listener for the radio buttons
        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            // Show 'next' button in UI
            binding.nextBtn.setVisibility(View.VISIBLE);
            // Update selected answer captured from UI in view model
            RadioButton selectedRadioButton = radioGroup.findViewById(i);
            if (selectedRadioButton != null) {
                String selectedValue = selectedRadioButton.getText().toString();
                viewModel.setSelectedValue(selectedValue);
            }
        });

        // Set click listener for the 'Next' button
        binding.nextBtn.setOnClickListener(view ->{
            binding.radioGroup.clearCheck();
            binding.nextBtn.setVisibility(View.GONE);
            viewModel.submitSelectedAnswer();
            viewModel.nextQuestion();
        });


        // Set observer to update UI with current question
        viewModel.currentQuestion.observe(this.getViewLifecycleOwner(), question -> {
            binding.questionText.setText(question.question);
            binding.radioButton0.setText(question.mcq.get(0));
            binding.radioButton1.setText(question.mcq.get(1));
            binding.radioButton2.setText(question.mcq.get(2));
            binding.radioButton3.setText(question.mcq.get(3));
        });


        // Set observer to detect if user finished the exam
        viewModel.isFinished.observe(this.getViewLifecycleOwner(), isDone -> {
            if (isDone){
                binding.radioGroup.setVisibility(View.GONE);
                binding.nextBtn.setVisibility(View.GONE);
                binding.imageView4.setImageResource(R.drawable.baseline_grading_24);
                binding.questionText.setText(String.format("Your final grade is %d / %d", viewModel.grade, viewModel.questions.size()));
            }
        });
    }

}