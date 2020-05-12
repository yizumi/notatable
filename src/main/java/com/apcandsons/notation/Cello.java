package com.apcandsons.notation;

public class Cello implements Instrument {

    @Override
    public Pitch getLowestPitch() {
        return Pitch.PITCHES[12];
    }

    @Override
    public Pitch getHighestPitch() {
        return Pitch.PITCHES[60];
    }

    @Override
    public boolean isPitchInRange(Pitch p) {
        return getLowestPitch().gte(p) && getHighestPitch().lte(p);
    }
}
