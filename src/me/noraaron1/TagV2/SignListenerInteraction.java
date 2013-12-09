package me.noraaron1.TagV2;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListenerInteraction implements Listener{
	
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		
		Sign s = (Sign) e.getClickedBlock().getState();
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(s.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[Join]")){
				if(s.getLine(1).equalsIgnoreCase(ChatColor.BLACK + "1")){	
					e.getPlayer().performCommand("say hi");
				}else if(!s.getLine(1).equalsIgnoreCase(ChatColor.BLACK + "1")){
					e.getPlayer().sendMessage("You can't join this Arena!");
				}
			}
		}
	}

}
