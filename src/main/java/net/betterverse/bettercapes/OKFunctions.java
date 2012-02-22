package net.betterverse.bettercapes;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.WidgetAnchor;

public class OKFunctions
{
  private static OKmain plugin;

  public OKFunctions(OKmain instance)
  {
    plugin = instance;
  }

  public static void playerGetCape(Player plr) {
    ResultSet check = OKDB.dbm.query("SELECT cape FROM players WHERE player = '" + plr.getName() + "'");
    boolean delete = false;
    try {
      if (check.next()) {
        do {
          String cape = check.getString("cape");
          if (!OKmain.CheckPermission(plr, "bettercapes.cape." + cape))
            delete = true;
          else
            try {
              SpoutManager.getPlayer(plr).setCape((String)OKmain.nameurls.get(OKmain.nodenames.get(cape)));
            } catch (Exception e) {
              delete = true;
            }
        }
        while (
          check.next());
      }
      check.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    if (delete)
      OKDB.dbm.query("DELETE FROM players WHERE player = '" + plr.getName() + "'");
  }

  public static void playerSetCape(Player plr, String node)
  {
    OKDB.dbm.query("DELETE FROM players WHERE player = '" + plr.getName() + "'");
    try {
      SpoutManager.getPlayer(plr).setCape((String)OKmain.nameurls.get(OKmain.nodenames.get(node)));
    } catch (Exception localException) {
    }
    OKDB.dbm.query("INSERT INTO players (player,cape) VALUES ('" + plr.getName() + "','" + node + "')");
  }

  public static void playerRemoveCape(Player plr) {
    OKDB.dbm.query("DELETE FROM players WHERE player = '" + plr.getName() + "'");
    try {
      SpoutManager.getPlayer(plr).resetCape();
    } catch (Exception localException) {
    }
  }

  public static void playerOpenCapeGUI(Player plr, Integer page) {
    GenericPopup pop = new GenericPopup();
    GenericContainer mainbox = new GenericContainer();
    mainbox.setLayout(ContainerType.VERTICAL);
    mainbox.setHeight(300);
    mainbox.setWidth(300);
    mainbox.setAlign(WidgetAnchor.TOP_CENTER);
    mainbox.setY(10);
    GenericContainer abox = new GenericContainer();
    abox.setLayout(ContainerType.HORIZONTAL);
    abox.setHeight(22);
    abox.setWidth(300);
    abox.setAlign(WidgetAnchor.TOP_CENTER);
    GenericLabel label = new GenericLabel("SELECT YOUR CAPE");
    label.setTextColor(new Color(1.0F, 1.0F, 0.0F, 1.0F));
    label.setWidth(220).setHeight(20);
    label.setAuto(false);
    label.setFixed(true);
    label.setTooltip(ChatColor.YELLOW + OKmain.name + ChatColor.WHITE + " v" + ChatColor.GREEN + OKmain.version + ChatColor.WHITE + " by " + (String)OKmain.authors.get(0));
    abox.addChild(label);
    mainbox.addChild(abox);
    Integer i = Integer.valueOf(0);
    Integer j = Integer.valueOf(1);
    Integer max = Integer.valueOf(3);
    Integer start = Integer.valueOf(page.intValue() * 3 - 3);
    GenericButton button;
    if (page.intValue() == 1) {
      GenericContainer thebox = new GenericContainer();
      thebox.setLayout(ContainerType.HORIZONTAL);
      thebox.setHeight(65);
      thebox.setWidth(300);
      thebox.setAlign(WidgetAnchor.TOP_CENTER);
      GenericTexture texture = new GenericTexture();
      texture.setUrl(OKmain.nocape);
      texture.setWidth(66).setHeight(51);
      texture.setMargin(2, 10, 2, 0);
      texture.setFixed(true);
      thebox.addChild(texture);
      button = new OKGenericButton("No cape", "");
      button.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
      button.setHoverColor(new Color(1.0F, 1.0F, 0.0F, 1.0F));
      button.setWidth(150).setHeight(20);
      button.setMargin(17, 10, 1, 0);
      button.setFixed(true);
      button.setAuto(false);
      thebox.addChild(button);
      mainbox.addChild(thebox);
      max = Integer.valueOf(2);
      start = Integer.valueOf(0);
    }
    Integer amount = Integer.valueOf(0);
    for (String node : OKmain.nodenames.keySet()) {
      if (OKmain.CheckPermission(plr, "bettercapes.cape." + node)) {
        i = Integer.valueOf(i.intValue() + 1);
        amount = Integer.valueOf(amount.intValue() + 1);
        if ((i.intValue() >= start.intValue()) && (j.intValue() <= max.intValue())) {
          j = Integer.valueOf(j.intValue() + 1);
          GenericContainer box = new GenericContainer();
          box.setHeight(65);
          box.setWidth(300);
          box.setLayout(ContainerType.HORIZONTAL);
          box.setAlign(WidgetAnchor.TOP_CENTER);
          GenericTexture textures = new GenericTexture();
          textures.setUrl((String)OKmain.nameurls.get(OKmain.nodenames.get(node)));
          textures.setWidth(66).setHeight(51);
          textures.setMargin(2, 10, 2, 0);
          textures.setFixed(true);
          box.addChild(textures);
          GenericButton buttons = new OKGenericButton((String)OKmain.nodenames.get(node), node);
          buttons.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
          buttons.setHoverColor(new Color(1.0F, 1.0F, 0.0F, 1.0F));
          buttons.setWidth(150).setHeight(20);
          buttons.setMargin(17, 10, 1, 0);
          buttons.setFixed(true);
          buttons.setAuto(false);
          box.addChild(buttons);
          mainbox.addChild(box);
        }
      }
    }
    GenericContainer pagebox = new GenericContainer();
    pagebox.setHeight(30);
    pagebox.setWidth(300);
    pagebox.setLayout(ContainerType.HORIZONTAL);
    pagebox.setAlign(WidgetAnchor.TOP_CENTER);
    Integer prev = Integer.valueOf(page.intValue() - 1);
    GenericButton buttons = new OKGenericPagerButton("Previous", prev);
    buttons.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
    buttons.setHoverColor(new Color(1.0F, 1.0F, 0.0F, 1.0F));
    buttons.setWidth(100).setHeight(20);
    buttons.setMargin(4, 5, 8, 0);
    buttons.setFixed(true);
    buttons.setAuto(false);
    pagebox.addChild(buttons);
    Integer next = Integer.valueOf(page.intValue() + 1);
    if (next.intValue() > Math.ceil(Double.valueOf((Double.valueOf(amount.intValue()).doubleValue() - 2.0D) / 3.0D).doubleValue() + 1.0D)) {
      next = Integer.valueOf(0);
    }
    GenericButton buttonstwo = new OKGenericPagerButton("Next", next);
    buttonstwo.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
    buttonstwo.setHoverColor(new Color(1.0F, 1.0F, 0.0F, 1.0F));
    buttonstwo.setWidth(100).setHeight(20);
    buttonstwo.setMargin(4, 0, 8, 0);
    buttonstwo.setFixed(true);
    buttonstwo.setAuto(false);
    pagebox.addChild(buttonstwo);
    mainbox.addChild(pagebox);
    pop.attachWidget(plugin, mainbox);
    SpoutManager.getPlayer(plr).getMainScreen().attachPopupScreen(pop);
  }
}