package nuvemplugins.solaryeconomy.plugin.objetos;

import org.bukkit.scheduler.BukkitRunnable;

import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.plugin.Economia;

public class MoneyRunnables
{

	private BukkitRunnable moneyTopRunnable;

	public MoneyRunnables()
	{
		this.start();
	}

	public void start()
	{
		if (this.moneyTopRunnable == null) {
			this.moneyTopRunnable = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Economia.refreshMoneyTop();
				}
			};
			this.moneyTopRunnable.runTaskTimerAsynchronously(SolaryEconomy.instance, 20,
					SolaryEconomy.config.getYaml().getInt("economy_top.refresh_time"));
		}

	}

	public void stop()
	{
		if (this.moneyTopRunnable != null) {
			this.moneyTopRunnable.cancel();
			this.moneyTopRunnable = null;
		}
	}
}
