package org.example;

import java.util.Random;

public class EngineStatsGenerator {

  // Generate MEP
  public static int generateMEP(Engine engine) {
    Random r = new Random();
    double variation = 1.0;
    double luckRoll = Math.random(); // 0.0 to 1.0
    if (luckRoll < 0.02) {
      // Super durable engine
      variation *= 1.2 + Math.random() * 0.2; // 1.2x to 1.4x
    } else if (luckRoll > 0.98) {
      // Terrible lemon engine
      variation *= 0.7 + Math.random() * 0.2; // 0.7x to 0.9x
    }
    double base = 150 * engine.getCompressionRatio() * variation; // kPa

    // Compression ratio modifier: +3% per full point above 8.5, down if below
    double crModifier = 1.0 + (engine.getCompressionRatio() - 8.5) * 0.03;

    // Valvetrain type
    double timingModifier = switch (engine.getTimingType()) {
      case OHV -> 0.95;
      case OHC -> 1.00;
      case DOHC -> 1.05;
    };

    // Valves per cylinder
    double valveModifier = 1.0 + (engine.getValvesPerCyl() - 2) * 0.02;

    // Fuel system
    double fuelModifier = switch (engine.getFuelSystem()) {
      case CARB -> 0.92;
      case SPFI -> 0.95;
      case MPFI -> 1.00;
      case DI -> 1.05;
      case IDI, INLINE_PUMP, ROTARY_PUMP -> 0.90;
      case UNIT_INJECTOR, HEUI -> 0.95;
      case CRDI -> 1.00;
    };

    // Small impact from timing drive
    double driveModifier = (engine.getTimingDriveType() == Engine.TimingDriveType.belt) ? 1.01 : 0.99;

    // Cylinder size penalty: very large cylinders have less efficient burn
    double sizePenalty = (engine.getCylinderDisplacement() > 0.7) ? 0.95 : 1.0;

    // Combine modifiers
    double mep = base * crModifier * timingModifier * valveModifier * fuelModifier * driveModifier * sizePenalty;

    mep = randomMultiply(mep);

    return (int) mep;
  }

  // Generate BSFC
  public static int generateBSFC(Engine engine) {
    Random r = new Random();
    int baseBSFC = r.nextInt(engine.getMinBSFC(), engine.getMaxBSFC());
    double compressionModifier = 1.0 - 0.02 * (engine.getCenterOfMass() - 8);  // better CR reduces BSFC
    double timingModifier = switch (engine.getTimingType()) {
      case OHV -> 1.05;
      case OHC -> 1.02;
      case DOHC -> 1.00;
    };
//    double boostModifier = 1.0 + 0.05 * (boostPressure / 100.0);  // slight penalty for high boost

    return (int) (baseBSFC * compressionModifier * timingModifier);
  }

  // Generate Durability Hours
  public static int generateDurabilityHours(Engine engine) {
    int base = 2000; // baseline for a durable, unstressed design

    // Material penalty or bonus
    double materialFactor = 1.0;
    if (engine.getBlockMaterial() == Engine.Material.AL) {
      materialFactor -= 0.1; // aluminum wears faster
    }
    if (engine.getHeadMaterial() == Engine.Material.AL) {
      materialFactor -= 0.1; // aluminum wears faster
    }

    // Fuel system durability
    double fuelFactor = switch (engine.getFuelSystem()) {
      case CARB, INLINE_PUMP, ROTARY_PUMP, IDI -> 1.05;
      case MPFI, SPFI -> 1.0;
      case DI, HEUI -> 0.9;
      case CRDI, UNIT_INJECTOR -> 0.95;
    };

    // Timing type
    double timingFactor = switch (engine.getTimingType()) {
      case OHV -> 1.05;
      case OHC -> 1.0;
      case DOHC -> 0.95;
    };

    // Compression ratio penalty (above 10 → reduce)
    double crPenalty = (engine.getCompressionRatio() > 10.0)
            ? 1.0 - ((engine.getCompressionRatio() - 10.0) * 0.03)
            : 1.0;

    // High RPM reduces life — above 6000 starts to hurt
    double rpmPenalty = (engine.getRpm() > 6000)
            ? 1.0 - ((engine.getRpm() - 6000) / 1000.0) * 0.05
            : 1.0;

    // Clamp penalties to not go negative
    crPenalty = Math.max(0.7, crPenalty);
    rpmPenalty = Math.max(0.7, rpmPenalty);

    // Combine everything
    double result = base * materialFactor * fuelFactor * timingFactor * crPenalty * rpmPenalty;

    result = randomMultiply(result);

    return (int) result;
  }

  // Generate Dimensions

  // Generate Cost

  private static double randomMultiply(double result) {
    // Add rare extremes
    double luckRoll = Math.random(); // 0.0 to 1.0
    if (luckRoll < 0.2) {                   // Super durable engine
      result *= 1.2 + Math.random() * 0.2; // 1.2x to 1.4x
    }
    if (luckRoll < 0.5 && luckRoll > 0.2) { // Good durable engine
      result *= 1.0 + Math.random() * 0.2;
    }
    if (luckRoll > 0.5 && luckRoll < 0.8) { // Bad engine
      result *= 0.8 + Math.random() * 0.2;
    }
    if (luckRoll > 0.8) {                   // Terrible lemon engine
      result *= 0.6 + Math.random() * 0.2;
    }
    return result;
  }

}
