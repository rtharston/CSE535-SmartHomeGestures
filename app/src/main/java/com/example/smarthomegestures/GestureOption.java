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

    public int gestureExampleVideo()
    {
        switch (this) {
            case turn_on_lights:
                return R.raw.example_light_on;
            case turn_off_lights:
                return R.raw.example_light_off;
            case turn_on_fan:
                return R.raw.example_fan_on;
            case turn_off_fan:
                return R.raw.example_fan_off;
            case increase_fan_speed:
                return R.raw.example_increase_fan_speed;
            case decrease_fan_speed:
                return R.raw.example_decrease_fan_speed;
            case set_thermostat_to_specified_temperature:
                return R.raw.example_set_thermo;
            case _0:
                return R.raw.example_0;
            case _1:
                return R.raw.example_1;
            case _2:
                return R.raw.example_2;
            case _3:
                return R.raw.example_3;
            case _4:
                return R.raw.example_4;
            case _5:
                return R.raw.example_5;
            case _6:
                return R.raw.example_6;
            case _7:
                return R.raw.example_7;
            case _8:
                return R.raw.example_8;
            case _9:
                return R.raw.example_9;
            default:
//                throw new RuntimeException("Can't touch this.");
                return R.raw.example_0;
        }
    }

    public String gestureEndpoint()
    {
        switch (this) {
            case turn_on_lights:
                return "LightOn";
            case turn_off_lights:
                return "LightOff";
            case turn_on_fan:
                return "FanOn";
            case turn_off_fan:
                return "FanOff";
            case increase_fan_speed:
                return "IncreaseFanSpeed";
            case decrease_fan_speed:
                return "DecreaseFanSpeed";
            case set_thermostat_to_specified_temperature:
                return "SetThermo";
            // all the numbers are just their name
            default:
                return name;
        }
    }

    public String videoName()
    {
        return gestureEndpoint() + ".mp4";
    }
}
