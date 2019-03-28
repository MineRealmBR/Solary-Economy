package nuvemplugins.solaryeconomy;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.commands.SolaryCommand;

public class Main extends JavaPlugin
{

	public static void main(String[] args)
	{
		System.out.println(UUID.randomUUID().toString().replace("-", ""));
	}
	public static SolaryEconomy plugin;

	@Override
	public void onEnable()
	{
		plugin = new SolaryEconomy(this);
		plugin.onEnable();
		if (Bukkit.getPluginManager().isPluginEnabled(this)) {
			this.getCommand("money").setExecutor(new SolaryCommand("money"));
		}
	}

	@Override
	public void onDisable()
	{
		plugin.onDisable();
	}

}
