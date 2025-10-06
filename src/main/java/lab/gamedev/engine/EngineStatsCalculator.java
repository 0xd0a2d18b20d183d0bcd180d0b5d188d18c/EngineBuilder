package lab.gamedev.engine;

public class EngineStatsCalculator {

  // return cubic mm
  public static int calcDisplacementPerCyl(int bore, int stroke) {
    return Math.toIntExact(Math.round(Math.PI * stroke * Math.pow((double) bore / 2, 2)));
  }

  // return cubic mm
  public static int calcDisplacement(int cylinderDisplacement, int cylinders) {
    return cylinderDisplacement * cylinders;
  }

  // return Nm * m
  public static int calcTorque(double displacement, int mep) {
    // 1000 / 2pi / 2 (4 stroke) = 79.5
    return (int) ((displacement / 1000000000) * mep * 79.5);
  }

  // return kwt
  public static int calcPower(int torque, int rpm) {
    return (torque * rpm) / 9550;
  }

  // return hp
  public static int calcHorsepower(int kwt) {
    return Math.toIntExact((long) (kwt * 1.341));
  }
}
