package org.example;

import java.util.Random;

public class EngineStatsGenerator {

  // Generate MEP
  public static int generateMEP(Engine engine) {
    double gamma = 1.35;

    // Step 1: Base MEP from compression ratio (thermodynamic approximation)
    double baseMEP = 200 * (Math.pow(engine.getCompressionRatio(), gamma) - 1); // kPa

    // Step 2: Timing type modifier
    double timingModifier = switch (engine.getTimingType()) {
      case DOHC -> 1.10;
      case OHC  -> 1.05;
      case OHV  -> 1.00;
    };

    // Step 3: Valve count modifier (bonus per valve beyond 2 per cylinder)
    double valveModifier = 1.0 + Math.max(0, (engine.getValvesPerCyl() - 2)) * 0.02;

    // Step 4: Fuel system modifier
    double fuelModifier = switch (engine.getFuelSystem()) {
      case CARB -> 0.95;
      case SPFI -> 0.98;
      case MPFI -> 1.00;
      case DI   -> 1.05;
      case IDI, INLINE_PUMP, ROTARY_PUMP -> 1.05;
      case UNIT_INJECTOR, HEUI -> 1.08;
      case CRDI -> 1.10;
    };

    // Step 5: Timing drive (small difference)
    double driveModifier = switch (engine.getTimingDriveType()) {
      case belt  -> 1.01;
      case chain -> 0.99;
    };

    // Final MEP
    double mep = baseMEP * timingModifier * valveModifier * fuelModifier * driveModifier;

    // Clamp MEP to realistic range (just in case)
    mep = Math.max(600, Math.min(mep, 2000));

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

  // Generate Dimensions

  // Generate Cost
}
