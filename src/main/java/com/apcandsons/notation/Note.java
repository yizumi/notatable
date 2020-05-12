package com.apcandsons.notation;

public class Note {

    private final double frequency;
    private final Pitch pitch;
    private final int score;
    private int id;

    public Note(Pitch pitch, double frequency) {
        this.pitch = pitch;
        this.frequency = frequency;
        this.score = getScore();
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDiff() {
        return pitch.getFreq() - frequency;
    }

    /**
     * Between 1-100
     * @return
     */
    public int getScore() {
        return Double.valueOf((1 - Math.abs(getDiff()) / (pitch.getFreq() - pitch.getPrevPitchFreq())) * 100).intValue();
    }

    public String toString() {
        return this.pitch.toString() + " (Score: " + getScore() + " expected " + pitch.getFreq() + " actual: " + frequency + ")";
    }
}
