package org.example;

import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

@ToString
public class Contract {

  int id;
  String title;
  String description;
  ArrayList<Requirements> requirements;

  @ToString
  static class Requirements {
    Type type;
    int value;
    Comparison comparison;

    enum Type {
      cost, length, width, weight, torque, power, displacement, layout
    }

    enum Comparison {
      more, less, equals
    }
  }

  public static Contract generateRandomContract() {
    Random rand = new Random();
    Contract contract = new Contract();
    contract.id = rand.nextInt(10000);
    contract.title = "Contract #" + contract.id;
    contract.description = "Build an engine that meets the following requirements:";
    contract.requirements = new ArrayList<>();

    HashSet<Requirements.Type> usedTypes = new HashSet<>();

    // Decide how many unique requirements (1–3)
    int numRequirements = 1 + rand.nextInt(3);

    while (contract.requirements.size() < numRequirements) {
      Requirements req = new Requirements();

      // Pick a random type, avoiding duplicates
      Requirements.Type[] types = Requirements.Type.values();
      Requirements.Type type;
      do {
        type = types[rand.nextInt(types.length)];
      } while (usedTypes.contains(type));
      req.type = type;
      usedTypes.add(type);

      // Random comparison
      Requirements.Comparison[] comparisons = Requirements.Comparison.values();
      req.comparison = comparisons[rand.nextInt(comparisons.length)];

      // Random value based on type
      switch (req.type) {
        case cost -> req.value = 1000 + rand.nextInt(9000);           // $1k–10k
        case length, width -> req.value = 400 + rand.nextInt(400);    // mm
        case weight -> req.value = 100 + rand.nextInt(300);           // kg
        case torque -> req.value = 100 + rand.nextInt(500);           // Nm
        case power -> req.value = 50 + rand.nextInt(300);             // hp
        case displacement -> req.value = 1000 + rand.nextInt(4000);   // cc
        case layout -> req.value = rand.nextInt(3);                   // 0=V, 1=I, 2=Boxer
      }

      contract.requirements.add(req);
    }

    return contract;
  }
}
