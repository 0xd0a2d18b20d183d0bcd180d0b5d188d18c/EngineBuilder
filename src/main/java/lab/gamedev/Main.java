package lab.gamedev;

import lab.gamedev.engine.Engine;
import lab.gamedev.engine.EngineStatsGenerator;
import java.util.Arrays;

import static lab.gamedev.engine.EngineStatsCalculator.*;

public class Main {

  public static void main(String[] args) {

    System.out.println(Arrays.toString(args));

    // commands

    // contracts

     // check available contracts

     // apply for contract

    // engines

      // show list of my engines

      // create new engine

      // create new version of existing engine

      // remove engine

    // engineers

      // check available engineers

      // hire new engineer

      // show list current engineers

      // fire engineer by id

    // close turn

  }

  public static Engine createNewEngine() {
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
    e.setDisplacement(calcDisplacement(e.getCylinderDisplacement(), e.getCylinders()));

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
    e.setHp(calcHorsepower(e.getKwt()));
    return e;
  }
}
