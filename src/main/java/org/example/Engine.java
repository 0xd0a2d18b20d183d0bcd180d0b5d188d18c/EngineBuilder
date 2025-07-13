package org.example;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@ToString
public class Engine {

  // user set
  int cylinders;
  int bore; // mm
  int stroke; // mm
  int rpm; // peak
  int valvesPerCyl; // 2-5
  double compressionRatio; // from 5.0 to 13.5
  Layout layout;
  TimingType timingType;
  Material blockMaterial;
  Material headMaterial;
  TimingDriveType timingDriveType;
  FuelType fuelType;
  FuelSystem fuelSystem;

  // calculated precisely
  double cylinderDisplacement; // Litres
  double displacement; // Litres
  int torque; //N*m
  int kwt;
  int hp;

  // calculated with random
  int mep; // kPa
  int width; // mm
  int length; // mm
  int weight; // kg
  int centerOfMass; // mm (from floor)
  int durabilityHours; // from 1000 to 2500 in general
  int cost; // $
  int bsfc; // Brake Specific Fuel Consumption | Units: grams of fuel per kilowatt-hour (g/kWh)

  enum Layout {
    V, I, Boxer
  }

  enum Material {
    AL, CI
  }

  enum TimingType {
    OHV, OHC, DOHC;
  }

  enum TimingDriveType {
    belt, chain;
  }

  enum FuelType {
    gasoline, diesel;
  }

  enum FuelSystem {
    // gasoline
    CARB, SPFI, MPFI, DI,
    // diesel
    IDI, INLINE_PUMP, ROTARY_PUMP, UNIT_INJECTOR, HEUI, CRDI
  }

  int getMinBSFC() {
    return switch (this.fuelSystem) {
      case CARB          -> 300;
      case SPFI          -> 280;
      case MPFI          -> 260;
      case DI            -> 230;
      case IDI           -> 240;
      case INLINE_PUMP   -> 220;
      case ROTARY_PUMP   -> 230;
      case UNIT_INJECTOR -> 190;
      case HEUI          -> 200;
      case CRDI          -> 180;
    };
  }

  int getMaxBSFC() {
    return switch (this.fuelSystem) {
      case CARB          -> 360;
      case SPFI          -> 320;
      case MPFI          -> 300;
      case DI            -> 270;
      case IDI           -> 280;
      case INLINE_PUMP   -> 260;
      case ROTARY_PUMP   -> 270;
      case UNIT_INJECTOR -> 230;
      case HEUI          -> 240;
      case CRDI          -> 220;
    };
  }
}
