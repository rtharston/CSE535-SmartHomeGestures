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

    enum GestureOption {
        turn_on_lights("Turn on lights"),
        turn_off_lights("Turn off lights"),
        turn_on_fan("Turn on fan"),
        turn_off_fan("Turn off fan"),
        increase_fan_speed("Increase fan speed"),
        decrease_fan_speed("Decrease fan speed"),
        set_thermostat_to_specified_temperature("Set Thermostat to specified temperature"),
        _0("0"),
        _1("1"),
        _2("2"),
        _3("3"),
        _4("4"),
        _5("5"),
        _6("6"),
        _7("7"),
        _8("8"),
        _9("9");
        
        private final String name;

        private GestureOption(String name){
            this.name = name;
        }

        @NonNull
        @Override public String toString(){
            return name;
        }
    }

    private FragmentGestureSelectBinding binding;

    private Optional<GestureOption> selection;

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
        GestureOption gesture = (GestureOption)parent.getItemAtPosition(position);
        Log.d("gestureSelect", gesture.toString());
        selection = Optional.of(gesture);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selection = Optional.empty();
    }

}