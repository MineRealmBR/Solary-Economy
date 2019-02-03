package nuvemplugins.solaryeconomy.app;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import nuvemplugins.solaryeconomy.database.Database;
import nuvemplugins.solaryeconomy.database.MySQL;
import nuvemplugins.solaryeconomy.database.SQLite;
import nuvemplugins.solaryeconomy.manager.Mensagens;
import nuvemplugins.solaryeconomy.plugin.Economia;
import nuvemplugins.solaryeconomy.plugin.objetos.Account;
import nuvemplugins.solaryeconomy.plugin.objetos.MoneyRunnables;
import nuvemplugins.solaryeconomy.plugin.vault.VaultEconomy;
import nuvemplugins.solaryeconomy.util.Config;

public class SolaryEconomy implements Listener
{

	public SolaryEconomy(JavaPlugin plugin)
	{
		instance = plugin;
	}

	public static final String PLUGIN_NAME = "Solary-Economy";
	public static final String AUTHOR = "Sr_Edition";
	public static final String VERSION = "1.4";

	public static String table;

	public static JavaPlugin instance;
	public static Database database;
	public static Mensagens mensagens;
	public static MoneyRunnables moneyRunnables;
	public static Config config;

	public void onEnable()
	{
		config = new Config(instance, "config.yml");
		this.database();
		mensagens = new Mensagens(instance);

		instance.getServer().getPluginManager().registerEvents(this, instance);
		if (config.getYaml().getBoolean("use_vault")) {
			try {
				Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
				if (vault != null) {
					new VaultEconomy();
				}
				Plugin legendchat = Bukkit.getPluginManager().getPlugin("Legendchat");
				if (legendchat != null) {
					Class<?> listener_clazz = Class
							.forName("nuvemplugins.solaryeconomy.plugin.listener.LegendChatListeners");
					Object listener = listener_clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
					Bukkit.getServer().getPluginManager().registerEvents((Listener) listener, instance);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		moneyRunnables = new MoneyRunnables();
		moneyRunnables.start();
	}

	public void onDisable()
	{
		moneyRunnables.stop();

		Economia.saveAll();

		if (database != null) {
			if (database.connection()) {
				database.close();
			}
		}

	}

	public void database()
	{
		try {
			FileConfiguration config = instance.getConfig();
			boolean usemysql = config.getBoolean("mysql.enable");
			if (usemysql) {
				String hostname = config.getString("mysql.hostname");
				String database_name = config.getString("mysql.database");
				String username = config.getString("mysql.username");
				String password = config.getString("mysql.password");
				String table_name = config.getString("mysql.table");
				int port = config.getInt("mysql.port");
				MySQL mysql = new MySQL(instance);
				mysql.setHostname(hostname);
				mysql.setDatabase(database_name);
				mysql.setUsername(username);
				mysql.setPassword(password);
				mysql.setPort(port);
				table = table_name;
				database = mysql;

			} else {
				table = PLUGIN_NAME.toLowerCase().replace("-", "");
				database = new SQLite(instance);
			}

			if (database.open()) {
				database.close();
			} else {
				table = PLUGIN_NAME.toLowerCase().replace("-", "");
				database = new SQLite(instance);
			}
			database.open();

			database.execute("create table if not exists " + table + " (name varchar(40), valor text, toggle int);");

			database.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
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

	@EventHandler(priority = EventPriority.HIGH)
	public void onQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		if (player != null) {
			Account account = Economia.getAccount(player);
			if (account != null) {
				Economia.save(account);
				Economia.ACCOUNTS.remove(player.getUniqueId());

			}

		}

	}

}
