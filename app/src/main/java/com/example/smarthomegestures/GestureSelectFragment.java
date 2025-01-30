package com.example.smarthomegestures;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.smarthomegestures.databinding.FragmentGestureSelectBinding;

import java.util.Optional;

public class GestureSelectFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentGestureSelectBinding binding;
    private GestureSelectionViewModel viewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentGestureSelectBinding.inflate(inflater, container, false);

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<GestureOption> adapter = new ArrayAdapter<GestureOption>(
                getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item,
                GestureOption.values()
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        binding.gestureSelectSpinner.setAdapter(adapter);
        binding.gestureSelectSpinner.setOnItemSelectedListener(this);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = NavHostFragment.findNavController(GestureSelectFragment.this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.GestureSelectFragment);

        viewModel = new ViewModelProvider(backStackEntry).get(GestureSelectionViewModel.class);

        binding.buttonSelect.setOnClickListener(v ->
                navController.navigate(R.id.action_GestureSelectFragment_to_ExpertExampleFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        GestureOption gesture = (GestureOption)parent.getItemAtPosition(position);
        Log.d("gestureSelect", gesture.toString());
        viewModel.selectGestureOption(gesture);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        viewModel.clearGestureOption();
    }

}