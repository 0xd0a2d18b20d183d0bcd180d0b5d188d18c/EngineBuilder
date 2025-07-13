package org.example;

public class Main {

  public static void main(String[] args) {
    System.out.println("Hello and welcome!");

//    var x = calcDisplacementPerCyl(86, 86);
//
//    int first3Digits = (int)(x * 1000) % 1000;

    System.out.println(calcDisplacementPerCyl(86, 86));

    int cyls = 4;
    int bore = 86;
    int stroke = 86;
    int mep = 1000;
    int rpm = 6000;

    double displ = calcDisplacementPerCyl(bore, stroke);
    double fullDispl = calcDisplacement(displ, cyls);
    int torque = calcTorque(fullDispl, mep);
    int kwt = calcPower(torque, rpm);
    int hp = calcHorsepower(kwt);
    System.out.println();
  }

  static double calcDisplacementPerCyl(double bore, double stroke) {
    double pi = 0.7853;
    double boreC = bore * bore;
    double sq = pi * boreC * stroke;
    double next = sq / 1000000;
    int first3Digits = (int)(next * 1000) % 1000;
    return (double) first3Digits / 1000;
  }

  static double calcDisplacement(double disp, int cyl) {
    return disp * cyl;
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
