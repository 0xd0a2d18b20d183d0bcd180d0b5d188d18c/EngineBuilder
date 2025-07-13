package org.example;

public class EngineStatsCalculator {

  static double calcDisplacementPerCyl(double bore, double stroke) {
    double pi = 0.7853;
    double boreC = bore * bore;
    double sq = pi * boreC * stroke;
    double next = sq / 1000000;
    int first3Digits = (int)(next * 1000) % 1000;
    return (double) first3Digits / 1000;
  }

  static double calcDisplacement(double cylinderDisplacement, int cylinders) {
    return cylinderDisplacement * cylinders;
  }

  static int calcTorque(double displacement, int mep) {
    // 1000 / 2pi / 2 (4 stroke) = 79.5
    return (int) ((displacement / 1000) * mep * 79.5);
  }

  static int calcPower(int torque, int rpm) {
    return (torque * rpm) / 9550;
  }

  static int calcHorsepower(int kwt) {
    return Math.toIntExact((long) (kwt * 1.341));
  }
}
