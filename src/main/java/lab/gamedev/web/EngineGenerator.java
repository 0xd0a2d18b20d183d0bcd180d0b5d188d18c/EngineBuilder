package lab.gamedev.web;

import static lab.gamedev.engine.EngineStatsCalculator.calcDisplacement;
import static lab.gamedev.engine.EngineStatsCalculator.calcDisplacementPerCyl;
import static lab.gamedev.engine.EngineStatsCalculator.calcHorsepower;
import static lab.gamedev.engine.EngineStatsCalculator.calcPower;
import static lab.gamedev.engine.EngineStatsCalculator.calcTorque;
import static lab.gamedev.engine.EngineStatsGenerator.generateBSFC;
import static lab.gamedev.engine.EngineStatsGenerator.generateCenterOfMass;
import static lab.gamedev.engine.EngineStatsGenerator.generateCost;
import static lab.gamedev.engine.EngineStatsGenerator.generateDurabilityHours;
import static lab.gamedev.engine.EngineStatsGenerator.generateLength;
import static lab.gamedev.engine.EngineStatsGenerator.generateMEP;
import static lab.gamedev.engine.EngineStatsGenerator.generateWeight;
import static lab.gamedev.engine.EngineStatsGenerator.generateWidth;
import lab.gamedev.engine.Engine;

public class EngineGenerator {

    public static Engine generateFrom(Engine e) {
        e.setCylinderDisplacement(calcDisplacementPerCyl(e.getBore(), e.getStroke()));
        e.setDisplacement(calcDisplacement(e.getCylinderDisplacement(), e.getCylinders()));

        e.setMep(generateMEP(e));
        e.setBsfc(generateBSFC(e));
        e.setDurabilityHours(generateDurabilityHours(e));
        e.setCost(generateCost(e));
        e.setLength(generateLength(e));
        e.setWidth(generateWidth(e));
        e.setWeight(generateWeight(e));
        e.setCenterOfMass(generateCenterOfMass(e));

        e.setTorque(calcTorque(e.getDisplacement(), e.getMep()));
        e.setKwt(calcPower(e.getTorque(), e.getRpm()));
        e.setHp(calcHorsepower(e.getKwt()));
        return e;
    }
}
