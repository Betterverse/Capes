package net.betterverse.bettercapes;

import java.io.File;
import lib.PatPeter.SQLibrary.SQLite;

public class OKDB
{
  public static SQLite dbm;

  public static void initialize(OKmain instance)
  {
    File dbfile = new File("plugins" + File.separator + OKmain.name);
    if (!dbfile.exists()) {
      dbfile.mkdir();
    }
    dbm = new SQLite(OKLogger.getLog(), OKLogger.getPrefix(), "database", dbfile.getPath());
    OKLogger.dbinfo("Loading database...");
    dbm.open();
    if (!dbm.checkTable("players")) {
      OKLogger.dbinfo("Creating table 'players'...");
      String query = "CREATE TABLE players (player VARCHAR(255), cape VARCHAR(255));";
      dbm.createTable(query);
    }
  }

  public static void disable() {
    try {
      dbm.close();
    }
    catch (Exception localException)
    {
    }
  }
}