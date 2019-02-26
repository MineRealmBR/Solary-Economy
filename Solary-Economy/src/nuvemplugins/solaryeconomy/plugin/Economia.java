package nuvemplugins.solaryeconomy.plugin;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.minerealm.bukkit.engine.user.BukkitUser;
import com.minerealm.bukkit.engine.user.BukkitUserData;

import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.plugin.objetos.Account;
import nuvemplugins.solaryeconomy.plugin.objetos.MoneyTopAccount;
import nuvemplugins.solaryeconomy.plugin.vault.Vault;

public class Economia
{

	public static final Map<UUID, Account> ACCOUNTS = new HashMap<>();
	public static List<MoneyTopAccount> MONEY_TOP = new ArrayList<>();
	public static MoneyTopAccount MAGNATA = new MoneyTopAccount("console", new BigDecimal(0));

	public static void loadAll()
	{
		File userdataFolder = BukkitUserData.USERDATA_FOLDER;
		if (userdataFolder != null && userdataFolder.isDirectory()) {
			for (File file : userdataFolder.listFiles()) {
				try {
					String fileName = file.getName();
					if ((fileName != null && !fileName.isEmpty()) && fileName.endsWith(".json")) {
						UUID uuid = UUID.fromString(fileName.substring(0, fileName.length() - (".json").length()));
						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
						if (offlinePlayer != null) {
							ACCOUNTS.put(uuid, new Account(offlinePlayer));
						}
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
	}

	public static boolean hasAccount(OfflinePlayer offlinePlayer)
	{
		return (getAccount(offlinePlayer) != null);
	}

	public static boolean hasAccount(String name)
	{
		return hasAccount(Bukkit.getOfflinePlayer(name));
	}

	public synchronized static Account getAccount(OfflinePlayer offlinePlayer)
	{
		return ACCOUNTS.get(offlinePlayer.getUniqueId());
	}

	public synchronized static void delete(Account account)
	{
		if (account != null) {
			BukkitUser bukkitUser = account.getUser();
			if (bukkitUser != null) {
				BukkitUserData userdata = bukkitUser.getUserdata().load();
				userdata.data.remove("economy");
				userdata.save();
			}
		}
	}

	public synchronized static void refreshMoneyTop()
	{
		List<MoneyTopAccount> moneyTop = new ArrayList<MoneyTopAccount>();

		for (Account account : ACCOUNTS.values()) {
			moneyTop.add(MoneyTopAccount.valueOf(account));
		}

		Collections.sort(moneyTop);
		MONEY_TOP = moneyTop;

		if (!MONEY_TOP.isEmpty()) {
			MoneyTopAccount magnata = MONEY_TOP.get(0);
			if (magnata.getName() != MAGNATA.getName()) {
				MAGNATA = magnata;
				if (SolaryEconomy.config.getYaml().getBoolean("magnata_broadcast")) {
					String accountName = MAGNATA.getName();
					String accountBalance = SolaryEconomy.numberFormat(MAGNATA.getBalance());

					if (SolaryEconomy.config.getYaml().getBoolean("economy_top.prefix")) {
						Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
						if (vault != null) {
							accountName = Vault.getPrefix(MAGNATA.getName()).concat(MAGNATA.getName());
						}
					}
					for (World world : Bukkit.getWorlds()) {
						for (Player player : world.getPlayers()) {
							player.sendMessage(" ");
							player.sendMessage(SolaryEconomy.mensagens.get("MAGNATA_NEW")
									.replace("{player}", accountName).replace("{valor}", accountBalance));
							player.sendMessage(" ");
						}
					}
				}
			}
		}
	}

	public static boolean createAccount(OfflinePlayer offlinePlayer, BigDecimal balance)
	{
		if (!hasAccount(offlinePlayer)) {
			Account account = new Account(offlinePlayer.getName(), balance);
			account.saveAccount();
			ACCOUNTS.put(offlinePlayer.getUniqueId(), account);
			return true;
		}
		return false;
	}

	public static boolean createAccount(String name, BigDecimal balance)
	{
		return createAccount(Bukkit.getOfflinePlayer(name), balance);
	}

	public static boolean deleteAccount(String name)
	{
		return deleteAccount(Bukkit.getOfflinePlayer(name));
	}

	public static boolean deleteAccount(OfflinePlayer offlinePlayer)
	{
		Account account = getAccount(offlinePlayer);
		if (account != null) {
			ACCOUNTS.remove(offlinePlayer.getUniqueId());
			delete(account);
			return true;

		}
		return false;
	}

	public static BigDecimal getBalance(String name)
	{
		return getBalance(Bukkit.getOfflinePlayer(name));
	}

	public static BigDecimal getBalance(OfflinePlayer offlinePlayer)
	{
		Account account = getAccount(offlinePlayer);
		if (account != null) {
			return account.getBalance();
		} else {
			return new BigDecimal(0.0);
		}
	}

	public static boolean setBalance(OfflinePlayer offlinePlayer, BigDecimal balance)
	{
		Account account = getAccount(offlinePlayer);
		if (account != null) {
			account.setBalance(balance);
		}
		return (account != null);
	}

	public static boolean setBalance(String name, BigDecimal balance)
	{
		return setBalance(Bukkit.getOfflinePlayer(name), balance);
	}

	public static boolean toggle(OfflinePlayer offlinePlayer)
	{
		Account account = getAccount(offlinePlayer);
		if (account != null) {
			account.setToggle(!account.isToggle());
			return account.isToggle();
		}
		return false;
	}

	public static boolean toggle(String name)
	{
		return toggle(Bukkit.getOfflinePlayer(name));
	}

	public static boolean isToggle(String name)
	{
		return isToggle(Bukkit.getOfflinePlayer(name));
	}

	public static boolean isToggle(OfflinePlayer offlinePlayer)
	{
		Account account = getAccount(offlinePlayer);
		if (account != null) {
			return account.isToggle();
		}
		return false;
	}

	public static boolean addBalance(String name, BigDecimal valor)
	{
		return setBalance(name, getBalance(name).add(valor));
	}

	public static boolean substractBalance(String name, BigDecimal valor)
	{
		return setBalance(name, getBalance(name).subtract(valor));
	}

	public static boolean hasBalance(String name, BigDecimal balance)
	{
		try {
			return getBalance(name).doubleValue() >= balance.doubleValue();
		} catch (Exception exception) {
			return false;
		}
	}

}
