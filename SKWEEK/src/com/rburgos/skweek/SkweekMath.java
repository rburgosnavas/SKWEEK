package com.rburgos.skweek;

public class SkweekMath {

    private SkweekMath() {
    }

    static double add(double a, double b) {
        return (a + b);
    }

    static double sub(double a, double b) {
        return (b - a);
    }

    static double mul(double a, double b) {
        return (a * b);
    }

    static double div(double a, double b) {
        return (b / a);
    }

    static double mod(double a, double b) {
        return (b % a);
    }

    static double pow(double a, double b) {
        return Math.pow(b, a);
    }

    static double lShift(double a, double b) {
        return ((int) b << (int) a);
    }

    static double rShift(double a, double b) {
        return ((int) b >> (int) a);
    }

    static double or(double a, double b) {
        return ((int) b | (int) a);
    }

    static double and(double a, double b) {
        return ((int) b & (int) a);
    }

}
