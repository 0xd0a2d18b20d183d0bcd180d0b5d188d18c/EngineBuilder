package org.example;

import static org.example.EngineStatsCalculator.*;

public class Main {

  public static void main(String[] args) {

    // Set
    Engine e = new Engine();
    e.setCylinders(4);
    e.setBore(86);
    e.setStroke(86);
    e.setRpm(6000);
    e.setValvesPerCyl(2);
    e.setCompressionRatio(6);
    e.setLayout(Engine.Layout.I);
    e.setTimingType(Engine.TimingType.OHV);
    e.setBlockMaterial(Engine.Material.CI);
    e.setHeadMaterial(Engine.Material.CI);
    e.setTimingDriveType(Engine.TimingDriveType.chain);
    e.setFuelType(Engine.FuelType.gasoline);
    e.setFuelSystem(Engine.FuelSystem.CARB);

    // Calc
    e.setCylinderDisplacement(calcDisplacementPerCyl(e.getBore(), e.getStroke()));
    e.setDisplacement(calcDisplacement(e.getCylinderDisplacement(), e.cylinders));

    // Generate
    e.setMep(EngineStatsGenerator.generateMEP(e));
    e.setBsfc(EngineStatsGenerator.generateBSFC(e));
    e.setDurabilityHours(EngineStatsGenerator.generateDurabilityHours(e));
    e.setCost(EngineStatsGenerator.generateCost(e));
    e.setLength(EngineStatsGenerator.generateLength(e));
    e.setWidth(EngineStatsGenerator.generateWidth(e));
    e.setWeight(EngineStatsGenerator.generateWeight(e));
    e.setCenterOfMass(EngineStatsGenerator.generateCenterOfMass(e));

    // Calc
    e.setTorque(calcTorque(e.getDisplacement(), e.getMep()));
    e.setKwt(calcPower(e.getTorque(), e.getRpm()));
    e.setHp(calcHorsepower(e.kwt));

    System.out.println(e);
  }
}
