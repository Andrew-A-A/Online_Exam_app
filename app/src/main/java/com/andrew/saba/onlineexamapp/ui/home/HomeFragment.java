package com.andrew.saba.onlineexamapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.andrew.saba.onlineexamapp.R;
import com.andrew.saba.onlineexamapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    // Binding object
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // Show in UI if data retrieved from Firebase
        homeViewModel.connected.observe(getViewLifecycleOwner(), isConnected -> {
                binding.radioButton.setChecked(isConnected);
        });

        // Initialize binding object
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set click listener for the start button
        binding.button.setOnClickListener(view -> {
            if (HomeViewModel.questions.isEmpty()) {
            Toast.makeText(this.requireContext(), "Connection error, please try again", Toast.LENGTH_SHORT).show();
            return;
            }
            NavDirections action = new ActionOnlyNavDirections(R.id.action_nav_home_to_examFragment);
            Navigation.findNavController(view).navigate(action);
        });

        return root;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}