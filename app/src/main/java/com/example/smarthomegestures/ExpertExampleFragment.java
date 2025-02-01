package com.example.smarthomegestures;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

        viewModel = new ViewModelProvider(requireActivity()).get(GestureSelectionViewModel.class);
        viewModel.getSelectedGestureOption().observe(getViewLifecycleOwner(), selectedGesture -> selectedGesture.ifPresent(gestureOption -> {
            binding.gestureName.setText(gestureOption.toString());

            playVideoFor(selectedGesture.get());
        }));

        binding.buttonPractice.setOnClickListener(v ->
                navController.navigate(R.id.action_ExpertExampleFragment_to_RecordFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void playVideoFor(GestureOption gesture) {
        Uri uri = Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+gesture.gestureExampleVideo());

        binding.gestureVideoView.setVideoURI(uri);
        binding.gestureVideoView.start();
        Log.d("videoStart", "Started video from " + uri.getPath());
    }
}