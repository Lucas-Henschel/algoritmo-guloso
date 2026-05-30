package org.furb.helpers.questao02;

public class Place {
    private final String name;
    private final int value;
    private final int durationDays;

    public Place(String name, int value, int durationDays) {
        this.name = name;
        this.value = value;
        this.durationDays = durationDays;
    }

    public double getValuePerDay() {
        return (double) value / durationDays;
    }

    public int getValue() {
        return value;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public String getName() {
        return name;
    }
}