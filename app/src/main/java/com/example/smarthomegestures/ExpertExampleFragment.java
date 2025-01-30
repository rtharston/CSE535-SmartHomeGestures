package com.example.smarthomegestures;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.smarthomegestures.databinding.FragmentExpertExampleBinding;

public class ExpertExampleFragment extends Fragment {

    private FragmentExpertExampleBinding binding;
    private GestureSelectionViewModel viewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentExpertExampleBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = NavHostFragment.findNavController(ExpertExampleFragment.this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.GestureSelectFragment);

        final TextView helloTextView = getView().findViewById(R.id.gestureName);

        viewModel = new ViewModelProvider(backStackEntry).get(GestureSelectionViewModel.class);
        viewModel.getSelectedGestureOption().observe(getViewLifecycleOwner(), selectedGesture -> {
            // TODO: get the correct video for this gesture
            selectedGesture.ifPresent(gestureOption -> helloTextView.setText(gestureOption.toString()));
        });

        binding.buttonPractice.setOnClickListener(v ->
                navController.navigate(R.id.action_ExpertExampleFragment_to_RecordFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}