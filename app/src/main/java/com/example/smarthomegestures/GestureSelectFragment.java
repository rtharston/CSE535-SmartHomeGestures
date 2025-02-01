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

public class GestureSelectFragment extends Fragment implements AdapterView.OnItemClickListener {

    private FragmentGestureSelectBinding binding;
    private GestureSelectionViewModel viewModel;
    private ArrayAdapter<GestureOption> gestureSelectAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentGestureSelectBinding.inflate(inflater, container, false);

        gestureSelectAdapter = new ArrayAdapter<>(
                getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1,
                GestureOption.values()
        );
        // Apply the adapter to the drop down menu.
        binding.gestureSelectText.setAdapter(gestureSelectAdapter);
        binding.gestureSelectText.setOnItemClickListener(this);

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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        GestureOption gesture = gestureSelectAdapter.getItem(position);
        Log.d("gestureSelect", gesture.toString());
        viewModel.selectGestureOption(gesture);
    }

}