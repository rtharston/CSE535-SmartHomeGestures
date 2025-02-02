package com.example.smarthomegestures;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smarthomegestures.databinding.FragmentRecordGestureBinding;

public class RecordGestureFragment extends Fragment {

    private FragmentRecordGestureBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRecordGestureBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         binding.videoCaptureButton.setOnClickListener(v ->
             Log.d("buttonRecordGesture", "Record gesture")
            // TODO: save recording
         );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}