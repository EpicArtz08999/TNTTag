package me.noraaron1.TagV2;


import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener{
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if(e.getLines().length == 0){
			
		}else if(e.getLine(0).equalsIgnoreCase("[TNTTag]") && e.getLine(1).equalsIgnoreCase("1") && e.getPlayer().hasPermission("TNTTag.Sign.Create")){
			e.getPlayer().sendMessage("You created a TNTTag sign for arena: 1");
			e.setLine(0, ChatColor.GREEN + "[Join]");
			e.setLine(1, ChatColor.BLACK + "1");
			e.setLine(2, ChatColor.BLACK + "0:10");
			e.setLine(3, ChatColor.BLACK + "TntTag");
		}
		else if(e.getLine(0).equalsIgnoreCase("[TNTTag]") && e.getPlayer().hasPermission("TNTTag.Sign.create")){
			e.getPlayer().sendMessage("A TNTTag sign must a valid number for the arena on line 2! ");
			e.getBlock().breakNaturally();
		}else if(e.getLine(0).equalsIgnoreCase("[TNTTag]") && e.getLine(1).equalsIgnoreCase("spectate 1") && e.getPlayer().hasPermission("TNTTag.Sign.Create")){
			//same code lines as [Join] Sign, but changed to [Spectate]
		}else if(e.getLine(0).equalsIgnoreCase("[TNTTag]") && e.getPlayer().hasPermission("TNTTag.Sign.Create")){
			//same code lines as Error
		}
			
	}
	
}