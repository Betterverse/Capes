package net.betterverse.bettercapes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OKPlayerListener implements Listener
{
  private OKmain plugin;

  public OKPlayerListener(OKmain instance)
  {
    this.plugin = instance;
  }

	@EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player plr = event.getPlayer();
    OKFunctions.playerGetCape(plr);
  }
}