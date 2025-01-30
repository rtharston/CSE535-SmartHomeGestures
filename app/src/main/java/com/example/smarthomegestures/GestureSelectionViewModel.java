package com.example.smarthomegestures;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Optional;

public class GestureSelectionViewModel extends ViewModel {

    private final MutableLiveData<Optional<GestureOption>> selectedGestureOption = new MutableLiveData<>();
    public void selectGestureOption(GestureOption GestureOption) {
        selectedGestureOption.setValue(Optional.of(GestureOption));
    }
    public void clearGestureOption() {
        selectedGestureOption.setValue(Optional.empty());
    }
    public LiveData<Optional<GestureOption>> getSelectedGestureOption() {
        return selectedGestureOption;
    }
}
