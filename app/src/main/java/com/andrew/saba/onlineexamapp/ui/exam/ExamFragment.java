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

    private ExamViewModel viewModel;

    public static ExamFragment newInstance() {
        return new ExamFragment();
    }

    private FragmentExamBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=FragmentExamBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ExamViewModel.class);
        viewModel.currentQuestion.observe(this.getViewLifecycleOwner(), question -> {
            binding.questionText.setText(question.question);
            binding.radioButton0.setText(question.mcq.get(0));
            binding.radioButton1.setText(question.mcq.get(1));
            binding.radioButton2.setText(question.mcq.get(2));
            binding.radioButton3.setText(question.mcq.get(3));
        });

        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            binding.nextBtn.setVisibility(View.VISIBLE);
            RadioButton selectedRadioButton = radioGroup.findViewById(i);
            if (selectedRadioButton != null) {
                String selectedValue = selectedRadioButton.getText().toString();
                viewModel.setSelectedValue(selectedValue);
            }
        });

        viewModel.isFinished.observe(this.getViewLifecycleOwner(), isDone -> {
            if (isDone){
                binding.radioGroup.setVisibility(View.GONE);
                binding.nextBtn.setVisibility(View.GONE);
                binding.imageView4.setImageResource(R.drawable.baseline_grading_24);
                binding.questionText.setText(String.format("Your final grade is %d / %d", viewModel.grade, viewModel.questions.size()));
            }
        });

        binding.nextBtn.setOnClickListener(view ->{
            binding.radioGroup.clearCheck();
            binding.nextBtn.setVisibility(View.GONE);
            viewModel.submitSelectedAnswer();
            viewModel.nextQuestion();
        });
    }

}