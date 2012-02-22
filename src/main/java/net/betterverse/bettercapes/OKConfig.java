package net.betterverse.bettercapes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public class OKConfig
{
  private static OKmain plugin;
  public static String directory = "plugins" + File.separator + OKmain.name;
  static File file = new File(directory + File.separator + "config.yml");

  public OKConfig(OKmain instance)
  {
    plugin = instance;
  }

  public void configCheck()
  {
    new File(directory).mkdir();
    if (!file.exists()) {
      try {
        OKLogger.info("Attempting to create configuration file...");
        file.createNewFile();
        addDefaults();
        OKLogger.info("Configuration file successfully created.");
      } catch (Exception ex) {
        ex.printStackTrace();
        OKLogger.error("Erorr creating configuration file.");
      }
    } else {
      OKLogger.info("Attempting to load configuration file...");
      loadkeys();
      OKLogger.info("Configuration file successfully loaded.");
    }
  }

  private static void write(String root, Object x) {
    YamlConfiguration config = load();
    config.set(root, x);
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String readString(String root) {
    YamlConfiguration config = load();
    return config.getString(root);
  }

  private static YamlConfiguration load() {
    try {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
      return config;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static List<String> readStringList(String root) {
    YamlConfiguration config = load();
    List list = new ArrayList();
    for (String key : config.getConfigurationSection(root).getKeys(false)) {
      list.add(key);
    }
    return list;
  }

  private static void addDefaults() {
    write("no-cape-pic", "http://bettercraft.net/spout/images/capes/nocape.png");
    write("capes.Cape One.url", "http://bettercraft.net/spout/images/capes/cape1.png");
    write("capes.Cape One.node", "capeone");
    write("capes.Cape Two.url", "http://bettercraft.net/spout/images/capes/beastnode.png");
    write("capes.Cape Two.node", "capetwo");
    loadkeys();
  }

  public static void loadkeys() {
    List<String> capelist = readStringList("capes");
    OKmain.nodenames.clear();
    OKmain.nameurls.clear();
    int i = 0;
    for (String cape : capelist) {
      OKmain.nodenames.put(readString("capes." + cape + ".node"), cape);
      OKmain.nameurls.put(cape, readString("capes." + cape + ".url"));
      i++;
    }
    OKmain.nocape = readString("no-cape-pic");
    OKLogger.info("Loaded " + i + " capes!");
  }
}