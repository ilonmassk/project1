package org.example.biblioteka;

public class PenaltyCalculator {
    public static int calculatePenalty(int year, int overdueDays) {
        int rate;
        if (year < 1900) {
            rate = 500;
        } else if (year < 2000) {
            rate = 100;
        } else {
            rate = 50;
        }
        return rate * overdueDays;
    }
}

