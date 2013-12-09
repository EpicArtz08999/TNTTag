package me.noraaron1.TagV2;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

public class Tag extends JavaPlugin implements Listener{
	
	//Contributer: Hoot215
	
	//learn Java
	
	//ToDo: Add spectators
	
	
	//todo: fix the bug with the Disconnect when  a player is it and they disconnect, choose a random player
	
	//todo: add a thing that makes all the players disapper on lobby
	
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Tag plugin;
	public Player tagger = null;
	public Player taggee = null;
	public Player chaser = null;
	public ArrayList<Player> players = new ArrayList<Player>();
	public ArrayList<Player> tagged = new ArrayList<Player>();
	public ArrayList<Location> locations = new ArrayList<Location>();
	public boolean currentGame = false;
	public boolean gameStarted = false;
	public Scoreboard spec; // Don't assign it a value yet.

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " has been disabled!");
		//plugin = null;
	}

	@Override
	public void onEnable() {
		plugin = this;
		
		PluginManager pm = getServer().getPluginManager();
		
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion()
				+ " has been enabled!");
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
		{
			
		@Override 
		public void run() 
		{
			spec = Bukkit.getScoreboardManager().getNewScoreboard();
			spec.registerNewTeam("spectators");
			spec.getTeam("spectators").setAllowFriendlyFire(true);
			//spec.getTeam("spectators").setCanSeeFriendlyInvisibles(true);
			System.out.println(spec.getTeam("spectators").canSeeFriendlyInvisibles() + " : 9 is true!");
			}
		}
		);
		
		pm.registerEvents(this, this);
		pm.registerEvents(new SignListener(), this);
		//pm.registerEvents(new SignListenerInteraction(), this);
		pm.registerEvents(new ItemDropListener(), this);
		pm.registerEvents(new ItemPickupListener(), this);
		pm.registerEvents(new InventoryDragEventListener(), this);
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player cmdPlayer = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("tag")) {
			return false;
		} else if (cmd.getName().equalsIgnoreCase("tagnew")) {
			if (!currentGame) {
				Bukkit.getServer()
						.broadcastMessage(
								"�AA new game of tag is starting! Use �b/tagjoin�A to join in.");
				chaser = cmdPlayer;
				addPlayer(chaser);
				currentGame = true;
				
				Randomiser.Randomisers1(null);
			} else
				cmdPlayer
						.sendMessage("�AA game has already been created. Use �b/tagjoin�A to join in.");
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tagstart")) {
			if (gameStarted)
				cmdPlayer.sendMessage("�AThe game has already been started!");
			else if (cmdPlayer == chaser) {
				gameStarted = true;
				Bukkit.getServer().broadcastMessage(
						"�AA new game of tag has begun!");
				chaser.sendMessage("�ARight click people to tag them!");
				chaser.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
						10000, 5));
			} else
				cmdPlayer
						.sendMessage("�AOnly the game creator can start the game.");
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tagstop")) {
			if (players.contains(cmdPlayer)) {
				if (cmdPlayer == chaser) {
					resetGame();
					Bukkit.getServer().broadcastMessage(
							"�AThe game was stopped by "
									+ cmdPlayer.getDisplayName());
				} else
					cmdPlayer
							.sendMessage("�AOnly the person who is \"it\" or a moderator can stop the game. Use �b/tagquit�A to quit the game.");
			} else if (cmdPlayer.hasPermission("tag.stop")) {
				resetGame();
				Bukkit.getServer().broadcastMessage(
						"�AThe game was stopped by "
								+ cmdPlayer.getDisplayName());
			} else
				cmdPlayer
						.sendMessage("�AYou can only stop the game if you are in the game.");
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tagjoin")) {
			if (!currentGame)
				cmdPlayer
						.sendMessage("�AThere is no current game in progress. Use �b/tagnew�A to create one");
			else if (cmdPlayer == chaser)
				cmdPlayer
						.sendMessage("�AYou can't join a game you created silly...");
			else if (players.contains(cmdPlayer))
				cmdPlayer.sendMessage("�AYou have already joined.");
			else if (players.size() < 60) {
				addPlayer(cmdPlayer);
			} else
				sender.sendMessage("�AThis game is full.");
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tagquit")) {
			if (currentGame) {
				if (players.contains(cmdPlayer)) {
					if (cmdPlayer == chaser) {
						if (players.size() == 1) {
							resetGame();
						} else {
							if (gameStarted) {
								chaser = players.get(players.size() - 1);
								chaser.addPotionEffect(new PotionEffect(
										PotionEffectType.SPEED, 10000, 5));
							} else {
								chaser = players.get(players.size() - 1);
							}
							chaser.sendMessage("�AYou are now \"it\". Right click people to tag them!");
							removePlayer(cmdPlayer);
						}
					} else
						removePlayer(cmdPlayer);
				} else
					cmdPlayer
							.sendMessage("�AYou can only quit the game if you are in the game.");
			} else
				cmdPlayer
						.sendMessage("�AThere is no current game in progress. Use �b/tagnew�A to create one.");
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tagwho")) {
			if (chaser == null)
				return false;
			cmdPlayer.sendMessage(chaser.getDisplayName() + "�A is it!!");
			return true;
		} else if (cmd.getName().equalsIgnoreCase("taglist")) {
			if (players.size() == 0)
				return false;
			for (int i = 0; i < players.size(); i++) {
				cmdPlayer.sendMessage("�A" + (i + 1) + ". "
						+ players.get(i).getDisplayName());
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tagtagged")) {
			if (tagged.size() == 0)
				return false;
			for (int i = 0; i < players.size(); i++) {
				cmdPlayer.sendMessage("�A" + (i + 1) + ". "
						+ tagged.get(i).getDisplayName());
			}
			return true;
		}
		return false;
	}

	public void resetGame() {
		for (int i = 0; i < players.size(); i++) {
			players.get(i).teleport(locations.get(i));
			players.get(i).removePotionEffect(PotionEffectType.SPEED);
		}
		players = new ArrayList<Player>();
		tagged = new ArrayList<Player>();
		locations = new ArrayList<Location>();
		chaser = null;
		currentGame = false;
		gameStarted = false;
		spec.getTeam("spectators").getPlayers().removeAll(players);
		Bukkit.getServer().broadcastMessage("�AThe game of tag is now over.");
	}

	public void removePlayer(Player p) {
		p.removePotionEffect(PotionEffectType.SPEED);
		int index = players.indexOf(p);
		p.teleport(locations.get(index));
		tagged.remove(p);
		players.remove(p);
		locations.remove(locations.get(index));
		Bukkit.getServer().broadcastMessage(
				p.getDisplayName() + "�A has left the game!");
	}

	public void addPlayer(Player p) {
		players.add(p);
		locations.add(p.getLocation());
		p.setGameMode(GameMode.SURVIVAL);
		p.teleport(chaser);
		Bukkit.getServer().broadcastMessage(
				p.getDisplayName() + "�A has joined the game!");
	}

	@EventHandler
	public void onPlayerTag(PlayerInteractEntityEvent event) {
		Entity e = event.getRightClicked();
		if (e instanceof Player) {
			tagger = event.getPlayer();
			taggee = (Player) event.getRightClicked();
			if (players.contains(tagger)) {
				if (gameStarted) {
					if (players.contains(taggee)) {
						if (tagger == chaser) {
							Bukkit.broadcastMessage(taggee.getDisplayName()
									+ "�A was tagged by "
									+ tagger.getDisplayName() + "�A. Now "
									+ taggee.getDisplayName() + "�A is it!!");
							tagger.getInventory().clear();
							tagger.getInventory().setHelmet(new ItemStack(Material.AIR));
							tagger.sendMessage("�AYou tagged "
									+ taggee.getDisplayName());
							taggee.sendMessage("�AYou were tagged by "
									+ tagger.getDisplayName());
							taggee.sendMessage("�ARight click people to tag them!");
							newChaser();
							if (!tagged.contains(taggee))
								tagged.add(taggee);
							
	
					} else {
						tagger.sendMessage(taggee.getDisplayName()
								+ "�A isn't playing.");
						taggee.sendMessage(tagger.getDisplayName()
								+ "�A wants to play tag with you. Use �b/tagjoin�A to play.");
					}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (players.contains(p)) {
			if (p == chaser) {
				if (players.size() == 1) {
					resetGame();
				} else {
					if (gameStarted) {
						chaser = players.get(players.size() - 1);
						chaser.addPotionEffect(new PotionEffect(
								PotionEffectType.SPEED, 10000, 5));
					} else {
						chaser = players.get(players.size() - 1);
					}
					chaser.sendMessage("You are now \"it\". Right click people to tag them!");
					removePlayer(p);
				}
			} else
				removePlayer(p);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		final Player ps = event.getEntity().getPlayer();
		if (players.contains(p)) {
			if (p == chaser) {
				if (players.size() == 1) {
					resetGame();
				} else {
					if (gameStarted) {
						chaser = players.get(players.size() - 1);
						chaser.addPotionEffect(new PotionEffect(
								PotionEffectType.SPEED, 10000, 5));
					} else {
						chaser = players.get(players.size() - 1);
					}
					chaser.sendMessage("You are now \"it\". Right click people to tag them!");
					removePlayer(p);
				}
			} else
				removePlayer(p);
		}
		
		
		event.getDrops().clear();
		
		new BukkitRunnable(){ 
			public void run(){
				spec.getTeam("spectators").addPlayer(ps);
				//e.setRespawnLocation(new Location(e.getPlayer().getWorld(),232,77,538));
				ps.teleport(new Location(ps.getPlayer().getWorld(), 232,77,538));
				ps.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 0));
			} 
		};
			
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e){
		 Player p = e.getPlayer();
			spec.getTeam("spectators").addPlayer(p);
			e.setRespawnLocation(new Location(e.getPlayer().getWorld(),232,77,538));
			p.teleport(new Location(p.getPlayer().getWorld(), 168,88,522));
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 0));
		
	}

	public void newChaser() {
		chaser.removePotionEffect(PotionEffectType.SPEED);
		chaser = taggee;
		chaser.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000,
				5));
		chaser.getInventory().setHelmet(new ItemStack(Material.TNT));
		chaser.getInventory().addItem(new ItemStack(Material.TNT, 1));
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		
		Sign s = (Sign) e.getClickedBlock().getState();
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(s.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[Join]") && s.getLine(1).equalsIgnoreCase(ChatColor.BLACK + "1")){
					if(gameStarted){
					e.getPlayer().performCommand("say started");
					}else{
						e.getPlayer().performCommand("say not started");
					}
						
				}else if(!s.getLine(1).equalsIgnoreCase(ChatColor.BLACK + "1")){
					e.getPlayer().sendMessage("You can't join this Arena!");
				}
			}
		}
	}
