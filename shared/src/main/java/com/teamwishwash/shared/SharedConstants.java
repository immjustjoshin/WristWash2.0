package com.teamwishwash.shared;

/**
 * Created by Joshua on 10/6/2018.
 */

/**
 * This file contains constants that are shared between the handheld and wearable applications,
 * such as the voice commands and data layer communications tags
 */
public class SharedConstants {
    //voice commands 'before' and 'after' were chosen because they are easily distinguishable from most words, unlike 'start'/'stop'
    //TODO: Maybe put in shared settings file? A little different than constants
    public interface VOICE_COMMANDS {
        String START_COMMAND = "before";
        String STOP_COMMAND = "after";
    }

    public interface DATA_LAYER_CONSTANTS {
        String SENSOR_PATH = "/sensors/";
        String LABEL_PATH = "/label";
    }

    public interface VALUES {
        String TIMESTAMPS = "com.teamwishwash.shared.values.timestamps";
        String SENSOR_VALUES = "com.teamwishwash.shared.values.sensor-values";
        String LABEL_TIMESTAMP = "com.teamwishwash.shared.values.label-timestamp";
        String ACTIVITY = "com.teamwishwash.shared.values.activity";
        String COMMAND = "com.teamwishwash.shared.values.command";
        String SCORE_KEY = "score call";
    }

    public interface COMMANDS {
        String START_SENSOR_SERVICE = "com.teamwishwash.shared.commands.start-sensor-service";
        String STOP_SENSOR_SERVICE = "com.teamwishwash.shared.commands.stop-sensor-service";
        String SHOW_SCORE = "com.teamwishwash.shared.commands.show-score";
    }
}