package com.example.smarthomegestures;


import androidx.annotation.NonNull;

public enum GestureOption {
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

    GestureOption(String name){
        this.name = name;
    }

    @NonNull
    @Override public String toString(){
        return name;
    }
}
