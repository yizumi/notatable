package com.apcandsons.notation;

class Pitch {
    public static double CHROMATIC_SHIFT = Math.pow(2.0, 1.0/12.0);
    private final int id;

    public boolean gte(Pitch p) {
        return p.getNumber() >= getNumber();
    }

    public boolean lte(Pitch p) {
        return p.getNumber() <= getNumber();
    }

    private int getNumber() {
        return scale * 12 + tone.getValue();
    }

    public double getNextPitchFreq() {
        return this.frequency * CHROMATIC_SHIFT;
    }

    public double getPrevPitchFreq() {
        return this.frequency / CHROMATIC_SHIFT;
    }

    public double getFreq() {
        return frequency;
    }

    enum Tone {
        C("ド", 0),
        C_SHARP("ド＃", 1),
        D("レ", 2),
        D_SHARP("レ＃", 3),
        E("ミ", 4),
        F("ファ", 5),
        F_SHARP("ファ＃", 6),
        G("ソ", 7),
        G_SHARP("ソ＃", 8),
        A("ラ", 9),
        A_SHARP("ラ＃", 10),
        B("シ", 11)
        ;

        private final String name;
        private final int value;

        private Tone(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public String toString() {
            return name;
        }
    }

    static float C0 = 16.43f; // 442.0f;
    private static int SIZE = 96;
    private final double frequency;
    private final int scale;
    private final Tone tone;

    public static Pitch[] PITCHES = initNotes();

    public static class Logarithm
    {
        public static double logb(double a, double b)
        {
            return Math.log(a) / Math.log(b);
        }

        public static double log2(double a)
        {
            return logb(a,2);
        }
    }

    private static Pitch[] initNotes() {
        Pitch[] pitches = new Pitch[SIZE];
        for (int i = 0; i < SIZE; i++) {
            pitches[i] = new Pitch(C0 * Math.pow(2, (double)i/12), Tone.values()[i % 12], i / 12);
        }
        return pitches;
    }

    /**
     * 353.518158 / C0
     * @param f
     * @return
     */

    public static Pitch fromFrequency(double f) {
        double d = Logarithm.log2(f / C0) * 12;
        int n = (int)Math.round(d);
        if (n < 0 || n >= PITCHES.length) {
            return null;
        }
        return PITCHES[n];
    }

    public Pitch(double frequency, Tone t, int scale) {
        this.id = scale * 12 + t.getValue();
        this.frequency = frequency;
        this.tone = t;
        this.scale = scale;
    }

    public String toString() {
        return tone.toString() + scale + " (freq: " + frequency + " Hz)";
    }
}
