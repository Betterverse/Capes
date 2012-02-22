package lib.PatPeter.SQLibrary;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class SQLite extends DatabaseHandler
{
  public String location;
  public String name;
  private File sqlFile;

  public SQLite(Logger log, String prefix, String name, String location)
  {
    super(log, prefix, "[SQLite] ");
    this.name = name;
    this.location = location;
    File folder = new File(this.location);
    if ((this.name.contains("/")) || 
      (this.name.contains("\\")) || 
      (this.name.endsWith(".db"))) {
      writeError("The database name can not contain: /, \\, or .db", true);
    }
    if (!folder.exists()) {
      folder.mkdir();
    }

    sqlFile = new File(folder.getAbsolutePath() + File.separator + name + ".db");
  }

  protected boolean initialize()
  {
    try
    {
      Class.forName("org.sqlite.JDBC");

      return true;
    } catch (ClassNotFoundException e) {
      writeError("You need the SQLite library " + e, true);
    }return false;
  }

  public Connection open()
  {
    if (initialize()) {
      try {
        return DriverManager.getConnection("jdbc:sqlite:" + 
          sqlFile.getAbsolutePath());
      } catch (SQLException e) {
        writeError("SQLite exception on initialize " + e, true);
      }
    }
    return null;
  }

  public void close()
  {
    Connection connection = open();
    if (connection != null)
      try {
        connection.close();
      } catch (SQLException ex) {
        writeError("Error on Connection close: " + ex, true);
      }
  }

  public Connection getConnection()
  {
    if (connection == null)
      return open();
    return connection;
  }

  public boolean checkConnection()
  {
    Connection connection = open();

    return connection != null;
  }

  public ResultSet query(String query)
  {

    return null;
  }

  PreparedStatement prepare(String query)
  {
    Connection connection = null;
    try
    {
      connection = open();
      PreparedStatement ps = connection.prepareStatement(query);
      return ps;
    } catch (SQLException e) {
      if (!e.toString().contains("not return ResultSet"))
        writeError("Error in SQL prepare() query: " + e.getMessage(), false);
    }
    return null;
  }

  public boolean createTable(String query)
  {
    Connection connection = open();
    Statement statement = null;
    try {
      if ((query.equals("")) || (query == null)) {
        writeError("SQL Create Table query empty.", true);
        return false;
      }

      statement = connection.createStatement();
      statement.execute(query);
      return true;
    } catch (SQLException ex) {
      writeError(ex.getMessage(), true);
    }return false;
  }

  public boolean checkTable(String table)
  {
    DatabaseMetaData dbm = null;
    try {
      dbm = open().getMetaData();
      ResultSet tables = dbm.getTables(null, null, table, null);

      return tables.next();
    }
    catch (SQLException e)
    {
      writeError("Failed to check if table \"" + table + "\" exists: " + e.getMessage(), true);
    }return false;
  }

  public boolean wipeTable(String table)
  {
    Connection connection = open();
    Statement statement = null;
    String query = null;
    try {
      if (!checkTable(table)) {
        writeError("Error at Wipe Table: table, " + table + ", does not exist", true);
        return false;
      }
      statement = connection.createStatement();
      query = "DELETE FROM " + table + ";";
      statement.executeQuery(query);
      return true;
    } catch (SQLException ex) {
      if ((!ex.getMessage().toLowerCase().contains("locking")) && 
        (!ex.getMessage().toLowerCase().contains("locked")) && 
        (!ex.toString().contains("not return ResultSet")))
        writeError("Error at SQL Wipe Table Query: " + ex, false); 
    }
    return false;
  }

  public void retry(String query)
  {
    boolean passed = false;
    Connection connection = open();
    Statement statement = null;

    while (!passed)
      try {
        statement = connection.createStatement();
        statement.executeQuery(query);
        passed = true;
      } catch (SQLException ex) {
        if ((ex.getMessage().toLowerCase().contains("locking")) || (ex.getMessage().toLowerCase().contains("locked")))
          passed = false;
        else
          writeError("Error at SQL Query: " + ex.getMessage(), false);
      }
  }

  public ResultSet retryResult(String query)
  {
    boolean passed = false;
    Connection connection = open();
    Statement statement = null;
    ResultSet result = null;

    while (!passed) {
      try {
        statement = connection.createStatement();
        result = statement.executeQuery(query);
        passed = true;
        return result;
      } catch (SQLException ex) {
        if ((ex.getMessage().toLowerCase().contains("locking")) || (ex.getMessage().toLowerCase().contains("locked")))
          passed = false;
        else {
          writeError("Error at SQL Query: " + ex.getMessage(), false);
        }
      }
    }

    return null;
  }
}