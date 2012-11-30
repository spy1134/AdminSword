// AdminSword by spy_1134
// http://dev.bukkit.org/server-mods/adminsword
// Fully tested with MC 1.2.5
// Partially tested with MC 1.3.1

// Feel free to re-use my code so long as the following conditions are met:
// 1.) You share the resulting work so other people may benefit from your work as well.
// 2.) You let users know that I was the original author.
// 3.) You don't charge any money for your work.

package spy_1134.AdminSword;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminSwordMain extends JavaPlugin {
	// Initialize some global variables.
	static AdminSwordMain plugin;
	static String defaultAction;
	static String[] swordTypes;
	static PluginDescriptionFile pluginDescription;
	
	// Initialize event listeners and display a startup message.
	public void onEnable()
	{	
		// Register the PlayerListener.
		getServer().getPluginManager().registerEvents(new AdminSwordPlayerListener(), this);
		
		// Register the command executor.
		AdminSwordCommands commandExecutor = new AdminSwordCommands();
		getCommand("adminsword").setExecutor(commandExecutor);
		getCommand("as").setExecutor(commandExecutor);
		
		// Populate sword type list.
		AdminSwordMain.swordTypes = new String[] {"fireball",
												  "lightning",
												  "teleport",
												  "tnt"};
		
		// Used to clear up some errors in my setDefaultType method.
		// No idea why it worked, but who cares.
		plugin = this;
		
		// Load configuration from disk.
		plugin.getConfig();
		
		// Save configuration. This is done to create config files in the plugin folder.
		plugin.saveConfig();
		
		// Parse configuration. This should be moved to
		// a separate method if it becomes too big.
		defaultAction = this.getConfig().getString("defaultAction");
		if (defaultAction == null || defaultAction == "null")
		{
			defaultAction = "lightning";
		}
		
		// Display startup message.
		pluginDescription = getDescription();
		getLogger().info("AdminSword v" + pluginDescription.getVersion() + " enabled!");
	}
	
	// Display a shutdown message.
	public void onDisable()
	{
		getLogger().info("AdminSword v" + pluginDescription.getVersion() + " disabled!");
	}
	
	
	
	
	// Toggles the AdminSword variable.
	// Returns true on enable, false on disable.
	public boolean toggleAdminSword(Player player)
	{
		// Call get AdminSwordEvent to initialize metadata and prevent errors.
		getAdminSwordType(player);
		// If adminsword is set to false, enable it.
		if (checkAdminSword(player) == false)
		{
			player.setMetadata("adminsword", new FixedMetadataValue((Plugin) this, true));
			return true;
		}
		// If adminsword is set to true, disable it.
		else if (checkAdminSword(player) == true)
		{
			player.setMetadata("adminsword", new FixedMetadataValue((Plugin) this, false));
			return false;
		}
		else
		{
			// Something must have gone wrong.
			// Send debug text.
			player.sendMessage(ChatColor.RED + "toggleAdminSword did nothing!");
			return true;
		}
	}
	
	// Checks if AdminSword is active.
	// Returns a boolean.
	public boolean checkAdminSword(Player player)
	{
		if (player.hasMetadata("adminsword"))
		{
			if (player.getMetadata("adminsword").get(0).asBoolean() == true)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	// Gets the player's specified event.
	public String getAdminSwordType(Player player)
	{
		// If the event type is set...
		if (player.hasMetadata("adminswordevent"))
		{
			// Return the event type specified.
			return player.getMetadata("adminswordevent").get(0).asString();
		}
		// If no event type is set...
		else
		{
			// Set the player's event type to default.
			setAdminSwordType(player, defaultAction, player);
			return defaultAction;
		}
	}
	
	// Sets a player's AdminSword event.
	// Return true on success.
	// Returns false if target doesn't have permission.
	public boolean setAdminSwordType(Player player, String action, CommandSender source)
	{
		if (hasTypePermission(player, action))
		{
			player.setMetadata("adminswordevent", new FixedMetadataValue((Plugin) this, action));
			if (source instanceof Player && source == player)
			{
				player.sendMessage(ChatColor.GREEN + "AdminSword type changed to: " + action);
			}
			else
			{
				player.sendMessage(ChatColor.GREEN + "AdminSword type changed to " + action + " by " + source.getName());
				source.sendMessage(ChatColor.GREEN + player.getName() + "\'s AdminSword type changed to " + action);
			}
			return true;
		}
		else
		{
			if (source instanceof Player && source == player)
			{
				source.sendMessage("You do not have permission to use " + action);
			}
			else
			{
				source.sendMessage(player.getName() + " does not have permission to use " + action);
			}
			return false;
		}
	}
	
	// Checks to see if a player has permission to use a certain sword type.
	public boolean hasTypePermission(Player player, String type)
	{
		if (player.hasPermission("adminsword.type." + type))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// Toggles whether the player can fly.
	// Returns true on enable, false on disable.
	public boolean toggleFlight(Player player)
	{
		// Check if the player is already allowed to fly.
		if (player.getAllowFlight())
		{
			// If they are allowed to fly, disallow it.
			player.setAllowFlight(false);
			return false;
		}
		// If player does not have flying enabled...
		else
		{
			// Allow flight.
			player.setAllowFlight(true);
			return true;
		}
	}
	
	// Returns the first player whose name starts with the specified string.
	public Player findPlayer(String playerName)
	{
	    Player[] players = Bukkit.getOnlinePlayers();
	    // Will run once for each player connected.
	    for(int i = 0; i < players.length; i++)
	    {
		// If the string is found in the beginning of the current player name...
		if(players[i].getPlayerListName().indexOf(playerName) == 0)
		{
		    // Return the player.
		    return players[i];
		}
	    }
	    // If no players are online or no player was returned...
	    return null;
	}
	
	public static int numberOfMatches(String playerName)
	{
		int matches = 0;
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
		for(int i = 0; i < onlinePlayers.length; i++)
		{
			if(onlinePlayers[i].getName().indexOf("playerName") == 0)
			{
				matches++;
			}
		}
		return matches;
	}
	
	// Checks if a specified type is a valid sword type.
	public boolean isValidType(String type)
	{
		// Loop through the list of sword types...
		// Start i at 0, loop until i is no longer less than the number of swords, increment i.
		for(int i = 0; i < swordTypes.length; i++)
		{
			// Check if the name of the current sword matches the one specified.
			if (swordTypes[i].equals(type))
			{
				// If there is a match, return true.
				return true;
			}
		}
		// If the loop has finished without returning true,
		// then the specified type doesn't exist. Return false.
		return false;
	}
	
	// Sets the default sword type in the configuration file.
	public boolean setDefaultType(String type)
	{
		if (isValidType(type))
		{
			plugin.getConfig().set("defaultAction", type); 
			plugin.saveConfig();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// A helpful function I used for debugging commands that loops
	// through an array and spits out it's contents to a sender.
	public void dumpArray(String[] array, CommandSender sender)
	{
		sender.sendMessage("Array Dump:");
		for(int i=0; i < array.length; i++)
		{
			sender.sendMessage(i + ": " + array[i]);
		}
	}
}