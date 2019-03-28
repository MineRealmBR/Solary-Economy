package nuvemplugins.solaryeconomy.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nuvemplugins.solaryeconomy.abstracts.SubCommand;
import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.commands.subcommands.SubCmdAdd;
import nuvemplugins.solaryeconomy.commands.subcommands.SubCmdCheque;
import nuvemplugins.solaryeconomy.commands.subcommands.SubCmdHelp;
import nuvemplugins.solaryeconomy.commands.subcommands.SubCmdMagnata;
import nuvemplugins.solaryeconomy.commands.subcommands.SubCmdPay;
import nuvemplugins.solaryeconomy.commands.subcommands.SubCmdReload;
import nuvemplugins.solaryeconomy.commands.subcommands.SubCmdRemove;
import nuvemplugins.solaryeconomy.commands.subcommands.SubCmdSet;
import nuvemplugins.solaryeconomy.commands.subcommands.SubCmdTeste;
import nuvemplugins.solaryeconomy.commands.subcommands.SubCmdToggle;
import nuvemplugins.solaryeconomy.commands.subcommands.SubCmdTop;
import nuvemplugins.solaryeconomy.plugin.Economia;
import nuvemplugins.solaryeconomy.plugin.vault.Vault;

public class SolaryCommand implements CommandExecutor
{

	private List<SubCommand> subcommands;

	public SolaryCommand(String command) {
		this.subcommands = new ArrayList<>();
		this.subcommands.add(new SubCmdHelp(command));
		this.subcommands.add(new SubCmdTop(command));
		this.subcommands.add(new SubCmdAdd(command));
		this.subcommands.add(new SubCmdRemove(command));
		this.subcommands.add(new SubCmdSet(command));
		this.subcommands.add(new SubCmdPay(command));
		this.subcommands.add(new SubCmdToggle(command));
		this.subcommands.add(new SubCmdReload(command));
		this.subcommands.add(new SubCmdMagnata(command));
		this.subcommands.add(new SubCmdCheque(command));
		this.subcommands.add(new SubCmdTeste(command));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender instanceof Player) {
			if (!Economia.hasAccount(sender.getName())) {
				Economia.createAccount(sender.getName(),
						new BigDecimal(SolaryEconomy.config.getYaml().getDouble("start_value")));
			}
		}
		if (args.length >= 1) {
			String arg = args[0].toLowerCase();
			if (!this.subcommands.isEmpty()) {
				for (SubCommand subCommand : this.subcommands) {
					if (arg.equalsIgnoreCase(subCommand.getName().toLowerCase())
							|| subCommand.getAlias().contains(arg)) {
						if (sender.hasPermission(subCommand.getPermission()) || subCommand.getPermission().isEmpty()) {
							subCommand.execute(sender, args);
						} else {
							sender.sendMessage(SolaryEconomy.mensagens.get("NO_PERMISSION"));
						}
						return false;
					}
				}
			}

			if (sender.hasPermission("solaryeconomy.commands.money.other")) {
				if (Economia.hasAccount(args[0])) {
					if (sender.getName().equals(args[0])) {
						if (sender instanceof Player) {
							sender.sendMessage(SolaryEconomy.mensagens.get("MONEY").replace("{valor}",
									SolaryEconomy.numberFormat(Economia.getBalance(sender.getName()))));
						} else {
							sender.sendMessage("§a/" + command.getName() + " ajuda §8- §7ver os comandos do plugin.");
						}
					} else {

						String accountNameDisplay = Vault.getPrefix(args[0]).concat(args[0]);

						sender.sendMessage(SolaryEconomy.mensagens.get("MONEY_OTHER")
								.replace("{valor}", SolaryEconomy.numberFormat(Economia.getBalance(args[0])))
								.replace("{player}", accountNameDisplay));
					}
				} else {
					sender.sendMessage(SolaryEconomy.mensagens.get("PLAYER_NOTFOUND").replace("{player}", args[0]));
				}
			}
		} else if (sender instanceof Player) {
			sender.sendMessage(SolaryEconomy.mensagens.get("MONEY").replace("{valor}",
					SolaryEconomy.numberFormat(Economia.getBalance(sender.getName()))));
		} else {
			sender.sendMessage("§a/" + command.getName() + " ajuda §8- §7ver os comandos do plugin.");
		}

		return false;
	}

}
