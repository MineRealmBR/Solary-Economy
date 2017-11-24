package solaryeconomy.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import solaryeconomy.SolaryEconomy;
import solaryeconomy.abstracts.SubCommand;
import solaryeconomy.plugin.objetos.Account;
import solaryeconomy.plugin.vault.Vault;

public class SubCmdMagnata extends SubCommand {

	public SubCmdMagnata(String command) {
		super("magnata", "�cUse: /" + command + " magnata", "solaryeconomy.commands.magnata");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

		Account account = SolaryEconomy.economia.getMagnata();
		sender.sendMessage(" ");
		if (account != null) {
			String accountname = account.getName();
			String valor = SolaryEconomy.numberFormat(account.getValor());
			if (SolaryEconomy.config.getYaml().getBoolean("economy_top.prefix")) {
				Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
				if (vault != null) {
					accountname = Vault.getPrefix(account.getName()).concat(account.getName());
				}
			}
			sender.sendMessage(SolaryEconomy.mensagens.get("MAGNATA_VIEW").replace("{player}", accountname)
					.replace("{valor}", valor));

		} else {
			sender.sendMessage(SolaryEconomy.mensagens.get("MAGNATA_NOT_FOUND"));
		}

		sender.sendMessage(" ");

	}

}