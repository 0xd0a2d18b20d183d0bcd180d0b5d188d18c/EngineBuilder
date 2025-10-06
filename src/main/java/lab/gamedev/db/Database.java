package lab.gamedev.db;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class Database {

  Sql2o sql2o = new Sql2o("jdbc:sqlite:save.db", null, null);
  Connection con = sql2o.open();

}
