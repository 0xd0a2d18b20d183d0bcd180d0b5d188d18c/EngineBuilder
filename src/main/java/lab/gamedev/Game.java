package lab.gamedev;

import lab.gamedev.contract.Contract;
import java.util.ArrayList;

public class Game {

  String companyTitle;
  String name;

  int actualTurn;
  int maxTurn;
  Money money;

  ArrayList<Contract> availableContracts;

  ArrayList<Contract> pendingContracts;

  ArrayList<Contract> currentContracts;

  ArrayList<Contract> completedContracts;

  class Money {
    int amount;
    int flow;
  }
}
