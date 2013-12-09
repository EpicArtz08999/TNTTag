package me.noraaron1.TagV2;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener
  implements Listener
{
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onItemPickup(PlayerDropItemEvent e)
  {
      e.setCancelled(true);
    }
}