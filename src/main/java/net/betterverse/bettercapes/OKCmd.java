package net.betterverse.bettercapes;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class OKCmd
  implements CommandExecutor
{
  private static OKmain plugin;

  public OKCmd(OKmain instance)
  {
    plugin = instance;
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    boolean handled = false;
    if (is(label, "capes")) {
      if ((args.length == 1) && (is(args[0], "reload"))) {
        handled = true;
        if (!isPlayer(sender)) {
          OKConfig.loadkeys();
          OKLogger.info("Notice: Configuration reloaded!");
        }
        else if (OKmain.CheckPermission(getPlayer(sender), "bettercapes.reload")) {
          OKConfig.loadkeys();
          sendMessage(sender, ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "Configuration reloaded!");
        } else {
          sendMessage(sender, ChatColor.LIGHT_PURPLE + "You do not have permission to do this.");
        }
      }
      else if ((args.length == 2) && (is(args[0], "select"))) {
        if (isPlayer(sender)) {
          handled = true;
          if (OKmain.CheckPermission(getPlayer(sender), "bettercapes.use")) {
            if (args[1].equalsIgnoreCase("none")) {
              OKFunctions.playerRemoveCape(getPlayer(sender));
              sendMessage(sender, ChatColor.GOLD + "|| " + ChatColor.WHITE + "Your cape has successfully been removed.");
            }
            else if (OKmain.CheckPermission(getPlayer(sender), "bettercapes.cape." + args[1])) {
              OKFunctions.playerSetCape(getPlayer(sender), args[1]);
              sendMessage(sender, ChatColor.GOLD + "|| " + ChatColor.WHITE + "Your cape has successfully been set.");
            } else {
              sendMessage(sender, ChatColor.LIGHT_PURPLE + "You do not have permission to do this.");
            }
          }
          else
            sendMessage(sender, ChatColor.LIGHT_PURPLE + "You do not have permission to do this.");
        }
      }
      else if ((args.length == 1) && (is(args[0], "list"))) {
        if (isPlayer(sender)) {
          handled = true;
          if (OKmain.CheckPermission(getPlayer(sender), "bettercapes.use")) {
            sendMessage(sender, ChatColor.GOLD + "||" + ChatColor.WHITE + " Listing all capes. Type " + ChatColor.YELLOW + "/cape select " + ChatColor.GREEN + "<ID>" + ChatColor.WHITE + " to select one.");
            sendMessage(sender, ChatColor.DARK_GREEN + " || " + ChatColor.WHITE + "ID: " + ChatColor.GREEN + "none" + ChatColor.WHITE + " | NAME: " + ChatColor.GREEN + "No cape");
            for (String node : OKmain.nodenames.keySet())
              if (OKmain.CheckPermission(getPlayer(sender), "bettercapes.cape." + node))
                sendMessage(sender, ChatColor.DARK_GREEN + " || " + ChatColor.WHITE + "ID: " + ChatColor.GREEN + node + ChatColor.WHITE + " | NAME: " + ChatColor.GREEN + (String)OKmain.nodenames.get(node));
          }
          else
          {
            sendMessage(sender, ChatColor.LIGHT_PURPLE + "You do not have permission to do this.");
          }
        }
      }
      else if (isPlayer(sender)) {
        handled = true;
        if (OKmain.CheckPermission(getPlayer(sender), "bettercapes.use")) {
          if (SpoutManager.getPlayer(getPlayer(sender)).isSpoutCraftEnabled())
            OKFunctions.playerOpenCapeGUI(getPlayer(sender), Integer.valueOf(1));
          else
            sendMessage(sender, ChatColor.RED + "|| " + ChatColor.WHITE + "You need to be using SpoutCraft to use this command.");
        }
        else {
          sendMessage(sender, ChatColor.LIGHT_PURPLE + "You do not have permission to do this.");
        }
      }
    }

    return handled;
  }

  private boolean is(String entered, String label) {
    return entered.equalsIgnoreCase(label);
  }

  private boolean sendMessage(CommandSender sender, String message) {
    boolean sent = false;
    if (isPlayer(sender)) {
      Player player = (Player)sender;
      player.sendMessage(message);
      sent = true;
    }
    return sent;
  }

  private boolean isPlayer(CommandSender sender) {
    return sender instanceof Player;
  }

  private Player getPlayer(CommandSender sender) {
    Player player = null;
    if (isPlayer(sender)) {
      player = (Player)sender;
    }
    return player;
  }
}