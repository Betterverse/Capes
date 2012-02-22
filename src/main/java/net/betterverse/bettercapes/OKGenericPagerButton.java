package net.betterverse.bettercapes;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.InGameHUD;
import org.getspout.spoutapi.player.SpoutPlayer;

public class OKGenericPagerButton extends GenericButton
{
  Integer val = null;

  public OKGenericPagerButton(String label, Integer value)
  {
    setText(label);
    this.val = value;
  }

  public void onButtonClick(ButtonClickEvent event) {
    if (this.val.intValue() != 0) {
      event.getPlayer().getMainScreen().closePopup();
      OKFunctions.playerOpenCapeGUI(event.getPlayer(), this.val);
    }
  }
}