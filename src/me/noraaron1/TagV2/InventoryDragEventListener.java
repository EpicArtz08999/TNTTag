package me.noraaron1.TagV2;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryDragEventListener implements Listener{
	
	
	  @EventHandler(priority=EventPriority.LOWEST)
	  public void onInventoryDragEvent(InventoryDragEvent e)
	  {
		  e.getWhoClicked().closeInventory();
		  System.out.println("Test");
	      e.setCancelled(true);
	      e.getWhoClicked().closeInventory();
	  }
	  @EventHandler(priority=EventPriority.LOWEST)
	  public void onInventoryClickEvent(InventoryClickEvent e){
		  e.getWhoClicked().closeInventory();
		  e.setCancelled(true);
		  e.getWhoClicked().closeInventory();
	  }
}
