package nuvemplugins.solaryeconomy.plugin.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.plugin.Economia;
import nuvemplugins.solaryeconomy.plugin.objetos.Cheque;

public class PlayerInteract implements Listener
{

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		if (player != null) {
			Action action = event.getAction();
			if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				ItemStack item = player.getItemInHand();
				if (item != null && item.getType() != Material.AIR) {
					if (Cheque.isCheque(item)) {
						Cheque cheque = Cheque.valueOf(player.getItemInHand());
						if (cheque != null) {
							Economia.addBalance(player.getName(), cheque.getValor());

							if (item.getAmount() > 1) {
								item.setAmount(item.getAmount() - 1);
							} else {
								player.setItemInHand(new ItemStack(Material.AIR));
							}
							player.updateInventory();
							player.sendMessage("§aVocê creditou em sua conta o valor de "
									+ SolaryEconomy.numberFormat(cheque.getValor()));
						} else {
							player.sendMessage("§cEsse é um cheque sem fundo :/");
						}
					}
				}
			}
		}
	}

}
