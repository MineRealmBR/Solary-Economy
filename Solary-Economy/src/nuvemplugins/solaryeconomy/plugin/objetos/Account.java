package nuvemplugins.solaryeconomy.plugin.objetos;

import java.math.BigDecimal;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.google.gson.JsonObject;
import com.minerealm.bukkit.engine.user.BukkitUser;

public class Account implements Comparable<Account>
{

	private BukkitUser user;
	private JsonObject data;

	public Account(String name) {
		this(name, new BigDecimal(0.0D));
	}

	public Account(OfflinePlayer offlinePlayer) {
		this(offlinePlayer, new BigDecimal(0.0D));
	}

	public Account(String name, BigDecimal balance) {
		this(Bukkit.getOfflinePlayer(name), balance);
	}

	public Account(OfflinePlayer offlinePlayer, BigDecimal balance) {
		this.user = BukkitUser.valueOf(offlinePlayer);
		if (this.user != null) {
			this.data = this.user.getUserdata().load().get("economy");
		}

		if (this.data == null) {
			this.data = new JsonObject();
			this.data.addProperty("name", offlinePlayer.getName());
			this.data.addProperty("balance", balance.toPlainString());
			this.data.addProperty("toggle", false);
		} else {
			if (this.data.get("name") == null) {
				this.data.addProperty("name", offlinePlayer.getName());
			}
			if (this.data.get("balance") == null) {
				this.data.addProperty("balance", balance.toPlainString());
			}
			if (this.data.get("toggle") == null) {
				this.data.addProperty("toggle", false);
			}
		}
	}

	public String getName()
	{
		return this.data.get("name").getAsString();
	}

	public void setName(String name)
	{
		this.data.addProperty("name", name);
		this.saveAccount();
	}

	public BigDecimal getBalance()
	{
		return new BigDecimal(this.data.get("balance").getAsString());
	}

	public void setBalance(BigDecimal valor)
	{
		this.data.addProperty("balance", valor.toPlainString());
		this.saveAccount();
	}

	public boolean isToggle()
	{
		return this.data.get("toggle").getAsBoolean();
	}

	public void setToggle(boolean toggle)
	{
		this.data.addProperty("toggle", toggle);
		this.saveAccount();
	}

	public BukkitUser getUser()
	{
		return this.user;
	}

	public void saveAccount()
	{
		if (this.user != null) {
			this.user.getUserdata().load().set("economy", this.data).save();

		}
	}

	@Override
	public int compareTo(Account account)
	{
		if (account.getBalance().doubleValue() > this.getBalance().doubleValue()) {
			return 1;
		}

		if (account.getBalance().doubleValue() < this.getBalance().doubleValue()) {
			return -1;
		}

		return 0;
	}

}
