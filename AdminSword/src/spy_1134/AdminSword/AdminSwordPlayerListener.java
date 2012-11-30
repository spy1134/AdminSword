package spy_1134.AdminSword;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AdminSwordPlayerListener extends AdminSwordMain implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		// If the player right clicked...
		if (event.getAction().toString().equals("RIGHT_CLICK_AIR") || event.getAction().toString().equals("RIGHT_CLICK_BLOCK"))
		{
			// Get the item they had in hand.
			ItemStack item = event.getItem();
			// If they were holding an item...
			if(item != null)
			{
				// Hand off to rightClickEvent.
				rightClickEvent(event.getPlayer(), event.getItem());
			}
		}
	}
	
	public void rightClickEvent(Player source, ItemStack item)
	{
		if (checkAdminSword(source) == true)
		{
			if (item.getTypeId() == 283 || item.getTypeId() == 268 || item.getTypeId() == 272 || item.getTypeId() == 267 || item.getTypeId() == 276)
			{
				if (hasTypePermission(source, getAdminSwordType(source)))
				{
					performAction(source, getAdminSwordType(source));
				}
			}
		}
	}
	
	public void performAction(Player player, String eventType)
	{
		new AdminSwordEvent(player, eventType);
	}
}