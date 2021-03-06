package TempleRun.Util;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import TempleRun.TempleRun;

public class Util {

	public static TempleRun main;
	public static String prefix = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_RED + "TempleRun" + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "] " + ChatColor.GRAY;

	public Util(TempleRun main) {
		Util.main = main;
	}

	public static HashMap<String, Long> players = new HashMap<String, Long>();
	public static HashMap<String, Location> oldLoc = new HashMap<String, Location>();
	public static HashMap<String, HashMap<Integer, Integer>> diamond = new HashMap<String, HashMap<Integer, Integer>>();
	public static HashMap<String, Integer> coins = new HashMap<String, Integer>();

	public static HashMap<String, Location> checkpoint = new HashMap<String, Location>();

	public static HashMap<String, HashMap<Integer, Integer>> Zblocks = new HashMap<String, HashMap<Integer, Integer>>();
	public static HashMap<String, HashMap<Integer, Integer>> Xblocks = new HashMap<String, HashMap<Integer, Integer>>();

	/* Boolean damit das dem Spiel joinen kann */
	public static boolean game = true;

	/* Schauen ob der Spieler gerade TempleRun nutzt */
	public static boolean isPlaying(String player) {
		if (players.containsKey(player))
			return true;
		return false;
	}

	/* Den Spieler zu TempleRun hinzufügen */
	public static void removePlayer(String player) {
		players.remove(player);
		Xblocks.remove(player);
		Zblocks.remove(player);
		diamond.remove(player);
		coins.remove(player);
	}

	/* Den Spieler aus TempleRun herraus schmeisen */
	public static void addPlayer(String player, long time, Player p) {
		players.put(player, time);
		Xblocks.put(player, new HashMap<Integer, Integer>());
		Zblocks.put(player, new HashMap<Integer, Integer>());
		Xblocks.get(player).put(0, 5);
		Zblocks.get(player).put(0, 6);
		diamond.put(player, new HashMap<Integer, Integer>());
		coins.put(player, 0);
	}

	public static void saveOldLoc(Player player) {
		oldLoc.put(player.getName(), player.getLocation());
	}

	/* Spieler zu TempleRun teleportieren */
	public static void teleport(Player player, Location loc) {
		player.teleport(loc);
	}

	/**
	 * 
	 * @param plugin
	 * @return
	 */
	public static Location getSpawnLocation(Plugin plugin) {

		Location loc = null;

		if (isAllSet(main)) {

			String world = plugin.getConfig().getString("TempleRun.Spawn.World");
			double x = plugin.getConfig().getDouble("TempleRun.Spawn.X");
			double y = plugin.getConfig().getDouble("TempleRun.Spawn.Y");
			double z = plugin.getConfig().getDouble("TempleRun.Spawn.Z");
			double yaw = plugin.getConfig().getDouble("TempleRun.Spawn.Yaw");
			double pitch = plugin.getConfig().getDouble("TempleRun.Spawn.Pitch");

			loc = new Location(plugin.getServer().getWorld(world), x, y, z, (float) yaw, (float) pitch);
			return loc;
		}

		return loc;
	}

	/* Die Zeit von dem Spieler bekommen */
	public static long getTime(String player) {
		return players.get(player);
	}

	/* Das Spiel so stoppen das keiner joinen kann */
	public static void stopGame(boolean kickall) {
		Util.game = false;

		if (kickall)
			kickAll(main, prefix + "TempleRun got stopped and all Players kicked too!");
	}

	/* Das Spiel wieder starten */
	public static void startGame() {
		Util.game = true;
	}

	/* Schauen ob das Spiel an ist */
	public static boolean isRunning() {
		if (Util.game)
			return true;
		return false;
	}

	/* Bekommt wieviele Leute in TempleRun sind */
	public static int getSize() {
		return players.size();
	}

	/* TempleRun stoppen */
	public static void kickAll(Plugin plugin, String message) {

		for (Player p : plugin.getServer().getOnlinePlayers()) {

			String s = p.getName();
			if (isPlaying(s)) {

				p.removePotionEffect(PotionEffectType.SPEED);

				Location loc = getOldLocation(s);
				teleport(p, loc);
				oldLoc.remove(s);
				removePlayer(s);

				p.sendMessage(message);
			}
		}
		players.clear();
		oldLoc.clear();
		checkpoint.clear();

	}

	/* Set TempleRun Spawn */
	public static void setSpawnLocation(Plugin plugin, Player player) {

		String world = player.getLocation().getWorld().getName();
		double x = player.getLocation().getX();
		double y = player.getLocation().getY() + 1;
		double z = player.getLocation().getZ();
		float yaw = player.getLocation().getYaw();
		float pitch = player.getLocation().getPitch();

		plugin.getConfig().set("TempleRun.Spawn.World", world);
		plugin.getConfig().set("TempleRun.Spawn.X", x);
		plugin.getConfig().set("TempleRun.Spawn.Y", y);
		plugin.getConfig().set("TempleRun.Spawn.Z", z);
		plugin.getConfig().set("TempleRun.Spawn.Yaw", yaw);
		plugin.getConfig().set("TempleRun.Spawn.Pitch", pitch);
		plugin.saveConfig();
	}

	public static Location getOldLocation(String player) {
		return oldLoc.get(player);
	}

	/* Sind alle Location gesetzt ? */
	public static boolean isAllSet(Plugin plugin) {
		if (plugin.getConfig().getString("TempleRun.Spawn.World") != null)
			return true;
		return false;
	}

	/* Kickt einen Spieler */
	public static void kickPlayer(Player player) {

		Location loc = getOldLocation(player.getName());
		teleport(player, loc);

		Util.oldLoc.remove(player.getName());

		removePlayer(player.getName());
	}

	/**
	 * 
	 * @param player
	 * @param x
	 * @param z
	 */
	public static void addLocation(String player, int x, int z) {
		Zblocks.get(player).put(coins.get(player), z);
		Xblocks.get(player).put(coins.get(player), x);
	}

	/**
	 * 
	 * @param player
	 */
	public static void addCoin(Player player) {
		int oldCoins = coins.get(player.getName());
		coins.put(player.getName(), oldCoins + 1);

		if (main.safepoints.isEmpty()) {
			player.sendMessage("is Empty");
			return;
		}

		for (String s : main.safepoints) {

			try {

				int i = Integer.parseInt(s);

				if (coins.get(player.getName()) == i) {
					safeCheckPoint(player.getName(), player.getLocation());
					player.sendMessage(prefix + "CheckPoint safed!");
				}

			} catch (NumberFormatException e) {
				player.sendMessage(prefix + "Cant safe CheckPoint! Contact Admin! He should look into the config.yml!");
			}

		}

	}

	/**
	 * 
	 * @param player
	 * @param x
	 * @param z
	 */
	public static void addDiamondBlock(String player, int x, int z) {
		diamond.get(player).put(x, z);
	}

	/**
	 * 
	 * @param player
	 * @param x
	 * @param z
	 * @return
	 */
	public static boolean isWalkedDiamond(String player, int x, int z) {
		if (diamond.get(player).containsKey(x) && diamond.get(player).containsValue(z))
			return true;
		return false;
	}

	/**
	 * 
	 * @param player
	 * @param x
	 * @param z
	 * @return
	 */
	public static boolean isWalkedOver(String player, int x, int z) {
		if (Xblocks.get(player).containsValue(x) && Zblocks.get(player).containsValue(z))
			return true;
		return false;
	}

	/**
	 * 
	 * @param player
	 */
	public static void helpMenu(Player player) {
		player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_RED + "TempleRun by §c§lxapfeltortexp" + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "]");
		player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "● §7/tr join             " + ChatColor.DARK_GRAY + "= " + ChatColor.GRAY + "Join TempleRun");
		player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "● §7/tr leave           " + ChatColor.DARK_GRAY + "= " + ChatColor.GRAY + "Leave TempleRun");
		player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "● §7/tr set spawn     " + ChatColor.DARK_GRAY + "= " + ChatColor.GRAY + "Set the TempleRun Spawn");
		player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "● §7/tr stop            " + ChatColor.DARK_GRAY + "= " + ChatColor.GRAY + "Stop TempleRun");
		player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "● §7/tr start           " + ChatColor.DARK_GRAY + "= " + ChatColor.GRAY + "Start TempleRun");
		player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "● §7/tr kick [PLAYER] " + ChatColor.DARK_GRAY + "= " + ChatColor.GRAY + "Kick Player out of TempleRun");
		player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "● §7/tr give [AMOUNT] " + ChatColor.DARK_GRAY + "= " + ChatColor.GRAY + "Give TempleRun Coins");
	}

	public static void safeCheckPoint(String player, Location loc) {
		checkpoint.put(player, loc);
	}

	public static Location getCheckPoint(String player) {
		return checkpoint.get(player);
	}

	public static boolean hasCheckPoint(String player) {
		if (checkpoint.containsKey(player))
			return true;
		return false;
	}

	/**
	 * 
	 * @param player
	 */
	public static void sendWinItems(Player player) {

		if (main.item.isEmpty()) {
			return;
		}

		for (String s : main.item) {

			if (!s.contains(",")) {
				continue;
			} else {
				String[] all = s.split(",");

				int item = 0, amount = 0;

				try {
					item = Integer.parseInt(all[0]);
					amount = Integer.parseInt(all[1]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

				player.getInventory().addItem(new ItemStack(item, amount));
			}

		}

	}

	public static boolean isCoin(ItemStack is) {

		if (!is.hasItemMeta()) {
			return false;
		}

		if (is.getItemMeta().getDisplayName() == null) {
			return false;
		} else {
			if (is.getItemMeta().getDisplayName().equalsIgnoreCase("§4§lTR-§c§lCoin") && is.getItemMeta().getLore().contains("§6Coin in TempleRun")) {
				return true;
			}
		}
		return false;
	}

}
