package com.bradfieldcs.archanaburra;

import java.time.Clock;

public class ClockSetter {
    private static final Clock DEFAULT_CLOCK = Clock.systemDefaultZone();
    private static volatile Clock clock = DEFAULT_CLOCK;

    public static Clock getClock() {
        return clock;
    }

    public static void setClock(Clock clock) {
        ClockSetter.clock = clock;
    }

    public static void reset() {
        ClockSetter.clock = DEFAULT_CLOCK;
    }
}
