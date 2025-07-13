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
  public static int generateDurabilityHours(Engine e) {
    int base = 2000; // baseline for a durable, unstressed design

    // Material penalty or bonus
    double materialFactor = 1.0;
    if (e.getBlockMaterial() == Engine.Material.AL) {
      materialFactor -= 0.1; // aluminum wears faster
    }
    if (e.getHeadMaterial() == Engine.Material.AL) {
      materialFactor -= 0.1; // aluminum wears faster
    }

    // Fuel system durability
    double fuelFactor = switch (e.getFuelSystem()) {
      case CARB, INLINE_PUMP, ROTARY_PUMP, IDI -> 1.05;
      case MPFI, SPFI -> 1.0;
      case DI, HEUI -> 0.9;
      case CRDI, UNIT_INJECTOR -> 0.95;
    };

    // Timing type
    double timingFactor = switch (e.getTimingType()) {
      case OHV -> 1.05;
      case OHC -> 1.0;
      case DOHC -> 0.95;
    };

    // Compression ratio penalty (above 10 → reduce)
    double crPenalty = (e.getCompressionRatio() > 10.0)
            ? 1.0 - ((e.getCompressionRatio() - 10.0) * 0.03)
            : 1.0;

    // High RPM reduces life — above 6000 starts to hurt
    double rpmPenalty = (e.getRpm() > 6000)
            ? 1.0 - ((e.getRpm() - 6000) / 1000.0) * 0.05
            : 1.0;

    // Clamp penalties to not go negative
    crPenalty = Math.max(0.7, crPenalty);
    rpmPenalty = Math.max(0.7, rpmPenalty);

    // Combine everything
    double result = base * materialFactor * fuelFactor * timingFactor * crPenalty * rpmPenalty;

    result = randomMultiply(result);

    return (int) result;
  }

  // Generate Length
  public static int generateLength(Engine e) {
    int cylindersPerBank = switch (e.getLayout()) {
      case I -> e.getCylinders();
      case V, Boxer -> e.getCylinders() / 2;
    };

    // Length: mostly depends on stroke and cylinders per bank
    int baseLength = (int) (e.getStroke() * cylindersPerBank * 1.1);

    // Add extra space for manifolds, accessories
    int extraLength = 100 + (int)(Math.random() * 50); // 100–150 mm

    return baseLength + extraLength;
  }

  // Generate Width
  public static int generateWidth(Engine e) {
    // Width: depends on bore and layout
    int baseWidth = switch (e.getLayout()) {
      case I -> (int) (e.getBore() * 1.5);        // narrow
      case V -> (int) (e.getBore() * 2.5);        // wider due to 2 banks
      case Boxer -> (int) (e.getBore() * 3.0);    // very wide
    };

    // Add extra space for manifolds, accessories
    int extraWidth = 80 + (int)(Math.random() * 50);   // 80–130 mm

    return baseWidth + extraWidth;
  }

  // Generate weight
  public static int generateWeight(Engine e) {
    double baseWeight = e.getDisplacement() * 200; // kg per liter
    // Material modifier
    if (e.getBlockMaterial() == Engine.Material.CI) baseWeight *= 1.15;
    if (e.getHeadMaterial() == Engine.Material.CI) baseWeight *= 1.10;

    // Layout modifier
    baseWeight *= switch (e.getLayout()) {
      case I -> 1.0;
      case V -> 1.1;
      case Boxer -> 1.15;
    };

    // Timing type modifier
    baseWeight *= switch (e.getTimingType()) {
      case OHV -> 0.95;
      case OHC -> 1.0;
      case DOHC -> 1.05;
    };

    // Random ±10% variation
    double randomFactor = 0.90 + Math.random() * 0.20;
    baseWeight *= randomFactor;
    return (int) baseWeight;
  }

  // Generate Cost
  public static int generateCost(Engine engine) {
    double base = 1000;

    // Displacement factor (bigger = more material)
    base += engine.getDisplacement() * 1500; // e.g., 2.0L → +$3000

    // Valves per cylinder (more valves = more complex head)
    base += (engine.getValvesPerCyl() - 2) * 300 * engine.getCylinders();

    // Timing type
    base += switch (engine.getTimingType()) {
      case OHV -> 200;
      case OHC -> 500;
      case DOHC -> 800;
    };

    // Fuel system complexity
    base += switch (engine.getFuelSystem()) {
      case CARB -> 100;
      case SPFI -> 300;
      case MPFI -> 500;
      case DI -> 800;
      case IDI, INLINE_PUMP, ROTARY_PUMP -> 300;
      case UNIT_INJECTOR, HEUI -> 600;
      case CRDI -> 1000;
    };

    // Material differences
    if (engine.getBlockMaterial() == Engine.Material.AL) base += 500;
    if (engine.getHeadMaterial() == Engine.Material.AL) base += 300;

    // Timing drive (belt slightly cheaper)
    base += (engine.getTimingDriveType() == Engine.TimingDriveType.chain) ? 150 : 50;

    // Adjust for RPM — high-revving engines need tighter tolerances
    if (engine.getRpm() > 6000) base += (engine.getRpm() - 6000) * 0.5;

    // Add random quality/luck modifier (±10%)
    double qualityFactor = 0.90 + Math.random() * 0.20;
    base *= qualityFactor;

    // Clamp to allowed range
    base = Math.max(1000, Math.min(base, 10000));

    return (int) base;
  }

  // Generate Center of Mass
  public static int generateCenterOfMass(Engine e) {
    // Estimate base engine height from layout and cylinders
    int height = switch (e.getLayout()) {
      case I -> 500 + e.getCylinders() * 5;     // tall, narrow
      case V -> 450 + e.getCylinders() * 4;     // more compact
      case Boxer -> 350 + e.getCylinders() * 3; // low profile
    };

    // Add effect from timing type (DOHC = taller head)
    height += switch (e.getTimingType()) {
      case OHV -> 0;
      case OHC -> 20;
      case DOHC -> 40;
    };

    // Materials: CI is heavier low → lowers CoM
    double weightBias = 0.0;
    if (e.getBlockMaterial() == Engine.Material.CI) weightBias -= 20;
    if (e.getHeadMaterial() == Engine.Material.CI) weightBias -= 10;

    // Final center of mass is roughly at 40–50% of height
    double baseCoM = height * 0.45;

    // Apply material bias
    baseCoM += weightBias;

    // Add small random ±5%
    baseCoM *= (0.95 + Math.random() * 0.10);

    // Clamp reasonable range
    baseCoM = Math.max(250, Math.min(baseCoM, 700));

    return (int) baseCoM;
  }

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
