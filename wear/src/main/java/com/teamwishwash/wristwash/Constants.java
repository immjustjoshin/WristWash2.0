package com.teamwishwash.wristwash;

/**
 * Created by Joshua on 10/6/2018.
 */

public class Constants {
    public interface ACTION {
        String RECORD_LABEL = "com.teamwishwash.wristwash.action.record";
        String START_SERVICE = "com.teamwishwash.wristwash.action.start-service";
        String STOP_SERVICE = "com.teamwishwash.wristwash.action.stop-service";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}
