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
import androidx.navigation.fragment.NavHostFragment;

import com.example.smarthomegestures.databinding.FragmentGestureSelectBinding;

import java.util.Optional;

public class GestureSelectFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentGestureSelectBinding binding;

    private Optional<String> selection;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentGestureSelectBinding.inflate(inflater, container, false);

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(),
                R.array.gestures_array,
                android.R.layout.simple_spinner_item
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

        binding.buttonSelect.setOnClickListener(v ->
                NavHostFragment.findNavController(GestureSelectFragment.this)
                        .navigate(R.id.action_GestureSelectFragment_to_ExpertExampleFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object thing = parent.getItemAtPosition(position);
        Log.d("gestureSelect", thing.toString());
        selection = Optional.of(thing.toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selection = Optional.empty();
    }

}