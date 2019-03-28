package nuvemplugins.solaryeconomy.app;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import nuvemplugins.solaryeconomy.manager.Mensagens;
import nuvemplugins.solaryeconomy.plugin.Economia;
import nuvemplugins.solaryeconomy.plugin.listener.PlayerInteract;
import nuvemplugins.solaryeconomy.plugin.objetos.MoneyRunnables;
import nuvemplugins.solaryeconomy.plugin.vault.VaultEconomy;
import nuvemplugins.solaryeconomy.util.Config;

public class SolaryEconomy implements Listener
{

	public SolaryEconomy(JavaPlugin plugin) {
		instance = plugin;
	}

	public static final String PLUGIN_NAME = "Solary-Economy";
	public static final String AUTHOR = "Sr_Edition";
	public static final String VERSION = "1.5";

	public static String table;

	public static JavaPlugin instance;
	public static Mensagens mensagens;
	public static MoneyRunnables moneyRunnables;
	public static Config config;

	public void onEnable()
	{
		config = new Config(instance, "config.yml");
		mensagens = new Mensagens(instance);

		Bukkit.getServer().getPluginManager().registerEvents(this, instance);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), instance);

		if (config.getYaml().getBoolean("use_vault")) {
			try {
				Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
				if (vault != null) {
					new VaultEconomy();
				}
				boolean usingLegendChatEvent = false;

				try {
					Class<?> chatMessageEventClass = Class
							.forName("br.com.devpaulo.legendchat.api.events.ChatMessageEvent");
					usingLegendChatEvent = (chatMessageEventClass != null);
				} catch (Exception exception) {
					usingLegendChatEvent = false;
				}

				if (usingLegendChatEvent) {
					Class<?> listener_clazz = Class
							.forName("nuvemplugins.solaryeconomy.plugin.listener.LegendChatListeners");
					Object listener = listener_clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
					Bukkit.getServer().getPluginManager().registerEvents((Listener) listener, instance);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		new BukkitRunnable() {
			@Override
			public void run()
			{
				Economia.loadAll();

				moneyRunnables = new MoneyRunnables();
				moneyRunnables.start();

			}
		}.runTaskLater(instance, (30));
	}

	public void onDisable()
	{
		moneyRunnables.stop();

	}

	public static String numberFormat(BigDecimal bigDecimal)
	{
		String formated = "";
		double doubleValue = bigDecimal.doubleValue();
		DecimalFormat decimalFormat = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.GERMAN));
		formated += decimalFormat.format(bigDecimal);

		if ((doubleValue >= -1) && (doubleValue <= 1)) {
			formated += " " + config.getString("currency_name.singular");
		} else {
			formated += " " + config.getString("currency_name.plural");
		}
		return formated;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if (player != null) {
			if (!Economia.hasAccount(player)) {
				Economia.createAccount(player, new BigDecimal(SolaryEconomy.config.getYaml().getDouble("start_value")));
			}
		}
	}

}
