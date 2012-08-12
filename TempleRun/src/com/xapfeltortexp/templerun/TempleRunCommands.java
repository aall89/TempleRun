package com.xapfeltortexp.templerun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TempleRunCommands implements CommandExecutor {

	// ChatColors
	private ChatColor red = ChatColor.RED;
	private ChatColor blue = ChatColor.BLUE;
	private ChatColor gold = ChatColor.GOLD;
	private ChatColor yellow = ChatColor.YELLOW;
	private ChatColor green = ChatColor.GREEN;

	private String prefix = this.yellow + "[" + gold + "TempleRun" + yellow + "] ";

	/*   ____                                          _     
	 *  / ___|___  _ __ ___  _ __ ___   __ _ _ __   __| |___ 
	 * | |   / _ \| '_ ` _ \| '_ ` _ \ / _` | '_ \ / _` / __|
	 * | |__| (_) | | | | | | | | | | | (_| | | | | (_| \__ \
	 *  \____\___/|_| |_| |_|_| |_| |_|\__,_|_| |_|\__,_|___/
	 */

	/**
	 * Register our CommandExecutor
	 */

	public TempleRunMain plugin;

	public TempleRunCommands(TempleRunMain plugin) {
		this.plugin = plugin;
	}

	// Starting with Commands

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String labal, String[] args) {
		// Is the sender a Player?
		if (sender instanceof Player) {
			// Is our Command /templerun ?
			final Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("templerun")) {
				if (args.length == 0) {
					if (!player.hasPermission("templerun.join")) {
						player.sendMessage(prefix + this.red + "You dont have Permissions to execute TempleRun Commands.");
						return true;
					}
					player.sendMessage(yellow + "[" + gold + "TempleRun Commands" + yellow + "]");
					player.sendMessage(green + "/tr " + yellow + "can be used too.");
					player.sendMessage(red + "----------------------------------------------");
					player.sendMessage(gold + "/tr join " + yellow + "= Join the TempleRun Game");
					player.sendMessage(gold + "/tr leave " + yellow + "= Leave the TempleRun Game");
					player.sendMessage(gold + "/tr setspawn " + yellow + "= Set the SpawnPoint for TempleRun");
					player.sendMessage(gold + "/tr credits " + yellow + "= Show the Plugin Credits.");
					player.sendMessage(gold + "/tr score " + yellow + "= Show your currently HighScore.");
					player.sendMessage(gold + "/tr kick <PLAYER> " + yellow + "= Kick an Player out of TempleRun.");
					player.sendMessage(red + "----------------------------------------------");
					player.sendMessage(gold + "Create a TempleRun Sign:");
					player.sendMessage(green + "Line 1: " + yellow + "[TempleRun]");
					player.sendMessage(green + "Line 2: " + yellow + "Finish");
					return true;
				}
				/*      _       _       
				 *     | | ___ (_)_ __  
				 *  _  | |/ _ \| | '_ \ 
				 * | |_| | (_) | | | | |
				 *  \___/ \___/|_|_| |_|
				 */

				if (args[0].equalsIgnoreCase("join")) {
					if (args.length > 1) {
						player.sendMessage(yellow + "[" + gold + "TempleRun Commands" + yellow + "]");
						player.sendMessage(green + "/tr " + yellow + "can be used too.");
						player.sendMessage(red + "----------------------------------------------");
						player.sendMessage(gold + "/tr join " + yellow + "= Join the TempleRun Game");
						player.sendMessage(gold + "/tr leave " + yellow + "= Leave the TempleRun Game");
						player.sendMessage(gold + "/tr setspawn " + yellow + "= Set the SpawnPoint for TempleRun");
						player.sendMessage(gold + "/tr credits " + yellow + "= Show the Plugin Credits.");
						player.sendMessage(gold + "/tr score " + yellow + "= Show your currently HighScore.");
						player.sendMessage(gold + "/tr kick <PLAYER> " + yellow + "= Kick an Player out of TempleRun.");
						player.sendMessage(red + "----------------------------------------------");
						player.sendMessage(gold + "Create a TempleRun Sign:");
						player.sendMessage(green + "Line 1: " + yellow + "[TempleRun]");
						player.sendMessage(green + "Line 2: " + yellow + "Finish");
						return false;
					}
					if (!player.hasPermission("templerun.join")) {
						player.sendMessage(prefix + this.red + "You dont have Permissions to execute TempleRun Comamnds.");
						return true;
					}
					if (player.getGameMode() == GameMode.CREATIVE) {
						player.sendMessage(prefix + this.red + "You are in Creative-Mode. Leave it at first.");
						return true;
					}
					// Is the Player already in TempleRun?
					if (plugin.players.contains(player.getName())) {
						player.sendMessage(prefix + this.red + "You are already in TempleRun");
						return true;
					}
					// Is there already one Person who run?
					if (plugin.i == 1) {
						player.sendMessage(prefix + this.red + "You cant join TempleRun. You have to wait.");
						return true;
					}
					if (!plugin.getConfig().contains("TempleRun.Spawn.world")) {
						player.sendMessage(prefix + ChatColor.RED + "Please set the SpawnPoint at First. " + ChatColor.GREEN + "/templerun setspawn");
						return true;
					}

					// Teleport to TempleRun
					Location spawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("TempleRun.Spawn.world")), plugin.getConfig().getDouble("TempleRun.Spawn.X"), plugin.getConfig().getDouble("TempleRun.Spawn.Y"), plugin.getConfig().getDouble("TempleRun.Spawn.Z"));
					player.teleport(spawn);
					player.sendMessage(ChatColor.GRAY + "TempleRun will start in:");

					/*
					 *  ____       _              _       _           
					 * / ___|  ___| |__   ___  __| |_   _| | ___ _ __ 
					 * \___ \ / __| '_ \ / _ \/ _` | | | | |/ _ \ '__|
					 *  ___) | (__| | | |  __/ (_| | |_| | |  __/ |   
					 * |____/ \___|_| |_|\___|\__,_|\__,_|_|\___|_|   
					 */

					final Plugin pl = plugin;
					Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

						@Override
						public void run() {
							if (plugin.number != -1) {
								if (plugin.number != 0) {
									player.sendMessage("" + ChatColor.GOLD + plugin.number);
									plugin.number--;
								} else {
									plugin.fall.remove(player.getName());
									player.sendMessage(ChatColor.RED + "RUN ! RUN ! RuuuuaaannnnN !");
									player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2400000, plugin.Speed));
									plugin.i++;
									plugin.players.add(player.getName());
									player.setHealth(20);
									player.setFoodLevel(20);
									plugin.number--;

								}
							}
						}

					}, 0L, 20L);
					plugin.number = 3;
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

						@Override
						public void run() {
							Bukkit.getServer().getScheduler().cancelTasks(pl);

						}
					}, 20L * 4);
					return true;

				}

				/*  _                         
				 * | |    ___  __ ___   _____ 
				 * | |   / _ \/ _` \ \ / / _ \
				 * | |__|  __/ (_| |\ V /  __/
				 * |_____\___|\__,_| \_/ \___|
				 */

				if (args[0].equalsIgnoreCase("leave")) {
					// Wrong Usage.
					if (args.length > 1) {
						player.sendMessage(yellow + "[" + gold + "TempleRun Commands" + yellow + "]");
						player.sendMessage(green + "/tr " + yellow + "can be used too.");
						player.sendMessage(red + "----------------------------------------------");
						player.sendMessage(gold + "/tr join " + yellow + "= Join the TempleRun Game");
						player.sendMessage(gold + "/tr leave " + yellow + "= Leave the TempleRun Game");
						player.sendMessage(gold + "/tr setspawn " + yellow + "= Set the SpawnPoint for TempleRun");
						player.sendMessage(gold + "/tr credits " + yellow + "= Show the Plugin Credits.");
						player.sendMessage(gold + "/tr score " + yellow + "= Show your currently HighScore.");
						player.sendMessage(gold + "/tr kick <PLAYER> " + yellow + "= Kick an Player out of TempleRun.");
						player.sendMessage(red + "----------------------------------------------");
						player.sendMessage(gold + "Create a TempleRun Sign:");
						player.sendMessage(green + "Line 1: " + yellow + "[TempleRun]");
						player.sendMessage(green + "Line 2: " + yellow + "Finish");
						return false;
					}
					// You dont have Permissions
					if (!player.hasPermission("templerun.leave")) {
						player.sendMessage(prefix + this.red + "You dont have Permissions to execute TempleRun Commands.");
						return true;
					}
					// Are you really in TempleRun?
					if (!plugin.players.contains(player.getName())) {
						player.sendMessage(prefix + this.red + "You are not in TempleRun. You cant leave it");
						return true;
					}
					// Yep you are. You leaved.
					player.removePotionEffect(PotionEffectType.SPEED);
					player.sendMessage(prefix + this.red + "You left TempleRun :(. To hard?");
					plugin.points = 0;
					plugin.walk.clear();
					plugin.i--;
					plugin.players.remove(player.getName());
					player.teleport(player.getWorld().getSpawnLocation());
					player.setSprinting(false);
					return true;
				}
				/*
				 *   ____              _ _ _       
				 *  / ___|_ __ ___  __| (_) |_ ___ 
				 * | |   | '__/ _ \/ _` | | __/ __|
				 * | |___| | |  __/ (_| | | |_\__ \
				 *  \____|_|  \___|\__,_|_|\__|___/
				 */
				if (args[0].equalsIgnoreCase("credits")) {
					// Wrong Usage.
					if (args.length > 1) {
						player.sendMessage(yellow + "[" + gold + "TempleRun Commands" + yellow + "]");
						player.sendMessage(green + "/tr " + yellow + "can be used too.");
						player.sendMessage(red + "----------------------------------------------");
						player.sendMessage(gold + "/tr join " + yellow + "= Join the TempleRun Game");
						player.sendMessage(gold + "/tr leave " + yellow + "= Leave the TempleRun Game");
						player.sendMessage(gold + "/tr setspawn " + yellow + "= Set the SpawnPoint for TempleRun");
						player.sendMessage(gold + "/tr credits " + yellow + "= Show the Plugin Credits.");
						player.sendMessage(gold + "/tr score " + yellow + "= Show your currently HighScore.");
						player.sendMessage(gold + "/tr kick <PLAYER> " + yellow + "= Kick an Player out of TempleRun.");
						player.sendMessage(red + "----------------------------------------------");
						player.sendMessage(gold + "Create a TempleRun Sign:");
						player.sendMessage(green + "Line 1: " + yellow + "[TempleRun]");
						player.sendMessage(green + "Line 2: " + yellow + "Finish");
						return false;
					}
					// You dont have Permissioms
					if (!player.hasPermission("templerun.join")) {
						player.sendMessage(prefix + this.red + "You dont have Permissions to execute TempleRun Commands.");
						return true;
					}
					// I am the Developer ;)
					player.sendMessage(this.gold + "Plugin developed by:");
					player.sendMessage(this.red + "xapfeltortexp");
					player.sendMessage(this.blue + "www.LostForce.com");
					return true;
				}
				/*           _                                  
				 *  ___  ___| |_ ___ _ __   __ ___      ___ __  
				 * / __|/ _ \ __/ __| '_ \ / _` \ \ /\ / / '_ \ 
				 * \__ \  __/ |_\__ \ |_) | (_| |\ V  V /| | | |
				 * |___/\___|\__|___/ .__/ \__,_| \_/\_/ |_| |_|
				 *                 |_|                         
				 * 
				 */
				if (args[0].equalsIgnoreCase("setspawn")) {
					// Wrong usage.
					if (args.length > 1) {
						player.sendMessage(yellow + "[" + gold + "TempleRun Commands" + yellow + "]");
						player.sendMessage(green + "/tr " + yellow + "can be used too.");
						player.sendMessage(red + "----------------------------------------------");
						player.sendMessage(gold + "/tr join " + yellow + "= Join the TempleRun Game");
						player.sendMessage(gold + "/tr leave " + yellow + "= Leave the TempleRun Game");
						player.sendMessage(gold + "/tr setspawn " + yellow + "= Set the SpawnPoint for TempleRun");
						player.sendMessage(gold + "/tr credits " + yellow + "= Show the Plugin Credits.");
						player.sendMessage(gold + "/tr score " + yellow + "= Show your currently HighScore.");
						player.sendMessage(gold + "/tr kick <PLAYER> " + yellow + "= Kick an Player out of TempleRun.");
						player.sendMessage(red + "----------------------------------------------");
						player.sendMessage(gold + "Create a TempleRun Sign:");
						player.sendMessage(green + "Line 1: " + yellow + "[TempleRun]");
						player.sendMessage(green + "Line 2: " + yellow + "Finish");
						return false;
					}
					// You dont have Permissions
					if (!player.hasPermission("templerun.setspawn")) {
						player.sendMessage(prefix + this.red + "You dont have Permissions to execute TempleRun Commands.");
						return true;
					}
					// Are you in TempleRun? Leave it at first.
					if (plugin.players.contains(player.getName())) {
						player.sendMessage(prefix + this.red + "You cant set the Spawn, when you are in the TempleRun Game.");
						return true;
					}
					// Set the Spawn now
					player.sendMessage(prefix + this.red + "You set the Spawn Point.");

					// Config
					plugin.getConfig().set("TempleRun.Spawn.world", player.getWorld().getName());
					plugin.getConfig().set("TempleRun.Spawn.X", player.getLocation().getX());
					plugin.getConfig().set("TempleRun.Spawn.Y", player.getLocation().getY());
					plugin.getConfig().set("TempleRun.Spawn.Z", player.getLocation().getZ());
					plugin.getConfig().set("TempleRun.Spawn.Direction", player.getLocation().getDirection());
					plugin.saveConfig();
					return true;

				}
				/*
				 *  ____                     
				 * / ___|  ___ ___  _ __ ___ 
				 * \___ \ / __/ _ \| '__/ _ \
				 *  ___) | (_| (_) | | |  __/
				 * |____/ \___\___/|_|  \___|
				 */
				if (args[0].equalsIgnoreCase("score")) {
					// You dont have Permissions
					if (args.length == 1) {
						if (!player.hasPermission("templerun.score")) {
							player.sendMessage(prefix + this.red + "You dont have Permissions to execute TempleRun Commands.");
							return true;
						}
						// You dont have a Score at the Moment
						if (!plugin.database.hasIndex(player.getName())) {
							player.sendMessage(prefix + this.red + "You dont have a Score yet.");
							return true;
						}
						// Wrong Usage.
						String name = player.getName();
						int score = plugin.database.getInt(name, "score");
						player.sendMessage(gold + "----------------------------------------------");
						player.sendMessage(this.red + "Your Score: " + green + score);
						player.sendMessage(gold + "----------------------------------------------");
						return false;
					}
					if (args.length > 1) {
						player.sendMessage(yellow + "[" + gold + "TempleRun Commands" + yellow + "]");
						player.sendMessage(green + "/tr " + yellow + "can be used too.");
						player.sendMessage(red + "----------------------------------------------");
						player.sendMessage(gold + "/tr join " + yellow + "= Join the TempleRun Game");
						player.sendMessage(gold + "/tr leave " + yellow + "= Leave the TempleRun Game");
						player.sendMessage(gold + "/tr setspawn " + yellow + "= Set the SpawnPoint for TempleRun");
						player.sendMessage(gold + "/tr credits " + yellow + "= Show the Plugin Credits.");
						player.sendMessage(gold + "/tr score " + yellow + "= Show your currently HighScore.");
						player.sendMessage(gold + "/tr kick <PLAYER> " + yellow + "= Kick an Player out of TempleRun.");
						player.sendMessage(red + "----------------------------------------------");
						player.sendMessage(gold + "Create a TempleRun Sign:");
						player.sendMessage(green + "Line 1: " + yellow + "[TempleRun]");
						player.sendMessage(green + "Line 2: " + yellow + "Finish");
						return false;
					}

				}
				/*
				 *  _  ___      _    
				 * | |/ (_) ___| | __
				 * | ' /| |/ __| |/ /
				 * | . \| | (__|   < 
				 * |_|\_\_|\___|_|\_\
				 */
				if (args[0].equalsIgnoreCase("kick")) {
					if (args.length == 1) {
						player.sendMessage(prefix + this.red + "Wrong Usage. /tr kick <PLAYER>");
						return true;
					}
					if (!player.hasPermission("templerun.kick")) {
						player.sendMessage(prefix + this.red + "You dont have Permissions to execute TempleRun Commands.");
						return true;
					}
					Player target = Bukkit.getServer().getPlayer(args[1]);
					if (target == null) {
						player.sendMessage(prefix + ChatColor.RED + "Cant find this Player.");
						return false;
					}
					if (!plugin.players.contains(target.getName())) {
						player.sendMessage(prefix + ChatColor.RED + "Why you want to kick that Person? He doesnt play TempleRun.");
						return true;
					}
					player.sendMessage(red + "You kicked " + green + target.getName() + red + " out of TempleRun.");
					target.sendMessage(this.red + "Kicked from TempleRun by " + this.green + player.getName() + red + ".");
					target.teleport(target.getWorld().getSpawnLocation());
					plugin.players.remove(target.getName());
					plugin.i--;
					target.removePotionEffect(PotionEffectType.SPEED);
					return true;
				}
			}

			/**
			 * Else to sender instance of Player
			 */
		} else {
			sender.sendMessage("[TempleRun] You can just execute TempleRun Commands InGame.");
			return true;
		}
		// Return false to the Command sender
		return false;
	}

}
