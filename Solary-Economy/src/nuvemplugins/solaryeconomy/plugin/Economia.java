package nuvemplugins.solaryeconomy.plugin;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.plugin.objetos.Account;
import nuvemplugins.solaryeconomy.plugin.vault.Vault;

public class Economia
{

	public static final Map<UUID, Account> ACCOUNTS = new HashMap<>();
	public static final List<Account> MONEY_TOP = new ArrayList<>();
	public static Account MAGNATA = new Account("console", new BigDecimal(0));

	public static boolean hasAccount(OfflinePlayer offlinePlayer)
	{
		return (getAccount(offlinePlayer) != null);
	}

	@SuppressWarnings("deprecation")
	public static boolean hasAccount(String name)
	{
		return hasAccount(Bukkit.getOfflinePlayer(name));
	}

	public synchronized static Account getAccount(OfflinePlayer offlinePlayer)
	{
		Account account = (offlinePlayer != null ? ACCOUNTS.get(offlinePlayer.getUniqueId()) : null);

		if (account == null) {
			synchronized (SolaryEconomy.database) {
				SolaryEconomy.database.open();
				try {
					ResultSet result = SolaryEconomy.database.query(
							"select * from " + SolaryEconomy.table + " where name='" + offlinePlayer.getName() + "'");
					if (result.next()) {
						account = Account.valueOf(result);
					}

				} catch (Exception exception) {
					exception.printStackTrace();
				}
				SolaryEconomy.database.close();
			}

			if (account != null) {
				ACCOUNTS.put(offlinePlayer.getUniqueId(), account);
			}
		}

		return account;
	}

	public synchronized static void saveAll()
	{
		synchronized (SolaryEconomy.database) {
			SolaryEconomy.database.open();
			try {
				synchronized (ACCOUNTS) {

					for (Account account : ACCOUNTS.values()) {
						try {
							ResultSet result = SolaryEconomy.database.query("select toggle from " + SolaryEconomy.table
									+ " where name='" + account.getName() + "'");

							if (result.next()) {
								SolaryEconomy.database.execute("update " + SolaryEconomy.table + " set valor='"
										+ account.getBalance().toPlainString() + "', toggle='"
										+ (account.isToggle() ? 1 : 0) + "' where name='" + account.getName() + "'");
							} else {
								SolaryEconomy.database.execute("insert into " + SolaryEconomy.table + " values ('"
										+ account.getName() + "', '" + account.getBalance().toPlainString() + "', '"
										+ (account.isToggle() ? 1 : 0) + "')");
							}
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			SolaryEconomy.database.close();
		}
	}

	public synchronized static void save(Account account)
	{
		if (account != null) {
			synchronized (SolaryEconomy.database) {
				SolaryEconomy.database.open();
				try {
					ResultSet result = SolaryEconomy.database.query(
							"select toggle from " + SolaryEconomy.table + " where name='" + account.getName() + "'");

					if (result.next()) {
						SolaryEconomy.database.execute("update " + SolaryEconomy.table + " set valor='"
								+ account.getBalance().toPlainString() + "', toggle='" + (account.isToggle() ? 1 : 0)
								+ "' where name='" + account.getName() + "'");
					} else {
						SolaryEconomy.database.execute("insert into " + SolaryEconomy.table + " values ('"
								+ account.getName() + "', '" + account.getBalance().toPlainString() + "', '"
								+ (account.isToggle() ? 1 : 0) + "')");
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				SolaryEconomy.database.close();
			}
		}
	}

	public synchronized static void delete(Account account)
	{
		if (account != null) {
			synchronized (SolaryEconomy.database) {
				SolaryEconomy.database.open();
				try {

					ResultSet result = SolaryEconomy.database.query(
							"select toggle from " + SolaryEconomy.table + " where name='" + account.getName() + "'");
					if (result.next()) {
						SolaryEconomy.database.execute(
								"delete from " + SolaryEconomy.table + " where name='" + account.getName() + "'");
					}

				} catch (Exception exception) {
					exception.printStackTrace();
				}
				SolaryEconomy.database.close();
			}
		}
	}

	public synchronized static void refreshMoneyTop()
	{

		saveAll();

		synchronized (SolaryEconomy.database) {
			Account magnata = new Account(MAGNATA.getName(), MAGNATA.getBalance());

			SolaryEconomy.database.open();
			try {

				ResultSet result = SolaryEconomy.database.query("select * from " + SolaryEconomy.table.concat(
						" where length(name) <= " + SolaryEconomy.config.getYaml().getInt("economy_top.name_size")
								+ " order by cast(valor as double) desc limit "
								+ SolaryEconomy.config.getYaml().getInt("economy_top.size") + ";"));

				MONEY_TOP.clear();

				while (result.next()) {
					try {

						Account account = Account.valueOf(result);
						if (account != null) {

							MONEY_TOP.add(account);

							if (account.getBalance().doubleValue() > magnata.getBalance().doubleValue()) {
								magnata = account;
							}

						}

					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			SolaryEconomy.database.close();

			if ((!magnata.getName().equals(MAGNATA.getName()))
					&& SolaryEconomy.config.getYaml().getBoolean("magnata_broadcast")) {
				MAGNATA = magnata;

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
						player.sendMessage(SolaryEconomy.mensagens.get("MAGNATA_NEW").replace("{player}", accountName)
								.replace("{valor}", accountBalance));
						player.sendMessage(" ");
					}
				}
			}
		}

	}

	public static boolean createAccount(OfflinePlayer offlinePlayer, BigDecimal balance)
	{
		if (!hasAccount(offlinePlayer)) {
			Account account = new Account(offlinePlayer.getName(), balance);
			ACCOUNTS.put(offlinePlayer.getUniqueId(), account);
			save(account);
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static boolean createAccount(String name, BigDecimal balance)
	{
		return createAccount(Bukkit.getOfflinePlayer(name), balance);
	}

	@SuppressWarnings("deprecation")
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

	@SuppressWarnings("deprecation")
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

	@SuppressWarnings("deprecation")
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

	@SuppressWarnings("deprecation")
	public static boolean toggle(String name)
	{
		return toggle(Bukkit.getOfflinePlayer(name));
	}

	@SuppressWarnings("deprecation")
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
