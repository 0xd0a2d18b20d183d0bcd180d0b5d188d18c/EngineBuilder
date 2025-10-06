package lab.gamedev.web;

import lab.gamedev.engine.Engine;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/engine")
public class EngineController {

    @PostMapping("/generate")
    public Engine generate(@RequestBody Engine input) {
        return EngineGenerator.generateFrom(input);
    }
}
