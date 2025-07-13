package org.example;

import static org.example.EngineStatsCalculator.*;

public class Main {

  public static void main(String[] args) {

    // Set
    Engine engine = new Engine();
    engine.setCylinders(4);
    engine.setBore(86);
    engine.setStroke(86);
    engine.setRpm(6000);
    engine.setValvesPerCyl(2);
    engine.setCompressionRatio(6);
    engine.setLayout(Engine.Layout.I);
    engine.setTimingType(Engine.TimingType.OHV);
    engine.setBlockMaterial(Engine.Material.CI);
    engine.setHeadMaterial(Engine.Material.CI);
    engine.setTimingDriveType(Engine.TimingDriveType.chain);
    engine.setFuelType(Engine.FuelType.gasoline);
    engine.setFuelSystem(Engine.FuelSystem.CARB);

    // Generate
    engine.setMep(EngineStatsGenerator.generateMEP(engine));
    engine.setBsfc(EngineStatsGenerator.generateBSFC(engine));

    // Calc
    engine.setCylinderDisplacement(calcDisplacementPerCyl(engine.getBore(), engine.getStroke()));
    engine.setDisplacement(calcDisplacement(engine.getCylinderDisplacement(), engine.cylinders));
    engine.setTorque(calcTorque(engine.getDisplacement(), engine.getMep()));
    engine.setKwt(calcPower(engine.getTorque(), engine.getRpm()));
    engine.setHp(calcHorsepower(engine.kwt));

    System.out.println(engine);
  }
}
