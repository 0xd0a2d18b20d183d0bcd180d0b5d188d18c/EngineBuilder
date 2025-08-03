package org.example;

import static org.example.EngineStatsCalculator.*;

public class EngineGenerator {

    public static Engine generateFrom(Engine e) {
        // Fill missing values with logic
        e.setCylinderDisplacement(calcDisplacementPerCyl(e.getBore(), e.getStroke()));
        e.setDisplacement(calcDisplacement(e.getCylinderDisplacement(), e.getCylinders()));

        e.setMep(EngineStatsGenerator.generateMEP(e));
        e.setBsfc(EngineStatsGenerator.generateBSFC(e));
        e.setDurabilityHours(EngineStatsGenerator.generateDurabilityHours(e));
        e.setCost(EngineStatsGenerator.generateCost(e));
        e.setLength(EngineStatsGenerator.generateLength(e));
        e.setWidth(EngineStatsGenerator.generateWidth(e));
        e.setWeight(EngineStatsGenerator.generateWeight(e));
        e.setCenterOfMass(EngineStatsGenerator.generateCenterOfMass(e));

        e.setTorque(calcTorque(e.getDisplacement(), e.getMep()));
        e.setKwt(calcPower(e.getTorque(), e.getRpm()));
        e.setHp(calcHorsepower(e.getKwt()));
        return e;
    }
}
