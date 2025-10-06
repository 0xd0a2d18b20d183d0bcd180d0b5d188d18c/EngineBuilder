package lab.gamedev.engineer;

import java.util.ArrayList;

public class Engineer {

  String name;
  ArrayList<Trait> traits;
  int currentProject;
  boolean isHired;

  class Trait {
    String title;
    String effect;
  }
}
