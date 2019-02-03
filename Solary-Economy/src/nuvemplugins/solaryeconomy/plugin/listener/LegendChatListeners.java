package nuvemplugins.solaryeconomy.plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.plugin.Economia;
import nuvemplugins.solaryeconomy.plugin.objetos.Account;

public class LegendChatListeners implements Listener
{

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEnable(ChatMessageEvent event)
	{
		if (event.isCancelled()) {
			return;
		}

		boolean use_magnata = SolaryEconomy.config.getYaml().getBoolean("magnata_tag");
		if (use_magnata) {
			String magnata_tag = SolaryEconomy.mensagens.get("MAGNATA_TAG");
			if (magnata_tag == null) {
				magnata_tag = "§a[$]";
			}

			Account magnata = Economia.MAGNATA;
			Player player = event.getSender();

			if ((magnata != null) && (player != null) && player.getName().equals(magnata.getName())) {
				event.setTagValue("solary_economy_magnata", magnata_tag);
			}

		}
	}

}
