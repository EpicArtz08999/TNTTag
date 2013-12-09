package me.noraaron1.TagV2;

import java.util.Random;

import org.bukkit.Bukkit;

public class Randomiser{
	
	public static void Randomisers1(Random players){
		
		int randomplayers;
		
		for(int counter =26; counter<=26;counter++){
			
			randomplayers = 1+players.nextInt(26);
			
			if(randomplayers == 26){
				Bukkit.getServer().broadcastMessage("26");
			}
			
			/*/if(randomplayers == 1){
				Bukkit.getServer().broadcastMessage("1");
			}else if(randomplayers == 2){
				Bukkit.getServer().broadcastMessage("1");
			}else if(randomplayers == 3){
				Bukkit.getServer().broadcastMessage("1");
				/*/
		}
	}
}
