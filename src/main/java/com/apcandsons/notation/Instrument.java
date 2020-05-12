package com.apcandsons.notation;

public interface Instrument {
    Pitch getLowestPitch();
    Pitch getHighestPitch();
    boolean isPitchInRange(Pitch p);
}
