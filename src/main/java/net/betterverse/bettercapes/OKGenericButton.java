package net.betterverse.bettercapes;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.InGameHUD;
import org.getspout.spoutapi.player.SpoutPlayer;

public class OKGenericButton extends GenericButton
{
  String val;

  public OKGenericButton(String label, String value)
  {
    setText(label);
    this.val = value;
  }

  public void onButtonClick(ButtonClickEvent event) {
    if (this.val.equals(""))
      OKFunctions.playerRemoveCape(event.getPlayer());
    else {
      OKFunctions.playerSetCape(event.getPlayer(), this.val);
    }
    event.getPlayer().getMainScreen().closePopup();
  }
}