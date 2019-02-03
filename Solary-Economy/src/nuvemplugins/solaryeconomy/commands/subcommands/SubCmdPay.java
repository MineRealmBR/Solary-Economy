package nuvemplugins.solaryeconomy.commands.subcommands;

import java.math.BigDecimal;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nuvemplugins.solaryeconomy.abstracts.SubCommand;
import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.plugin.Economia;

public class SubCmdPay extends SubCommand
{

	public SubCmdPay(String command)
	{
		super("pay", "§cUse: /" + command + " pay [jogador] [valor]", "solaryeconomy.commands.pay", "pagar", "enviar");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (args.length >= 3) {
			String nome = args[1];

			BigDecimal valor = this.numbers.getDecimal(args[2]);

			if (valor.doubleValue() < 1.0) {
				sender.sendMessage(SolaryEconomy.mensagens.get("NUMBER_NULL"));
				return;
			}

			if (!(sender instanceof Player)) {
				return;
			}

			if (sender.getName().equalsIgnoreCase(nome)) {
				sender.sendMessage(SolaryEconomy.mensagens.get("MONEY_PAY_ERRO"));
				return;
			}

			if (Economia.hasBalance(sender.getName(), valor)) {

				if (!Economia.isToggle(nome)) {
					if (Economia.addBalance(nome, valor)) {
						Economia.substractBalance(sender.getName(), valor);
						sender.sendMessage(SolaryEconomy.mensagens.get("MONEY_PAY_SENDER").replace("{player}", nome)
								.replace("{valor}", SolaryEconomy.numberFormat(valor)));

						Player target = Bukkit.getPlayer(nome);
						if (target != null) {
							if (sender != target) {
								target.sendMessage(SolaryEconomy.mensagens.get("MONEY_PAY_RECEIVER")
										.replace("{player}", sender.getName())
										.replace("{valor}", SolaryEconomy.numberFormat(valor)));
							}
						}

					} else {
						sender.sendMessage(SolaryEconomy.mensagens.get("PLAYER_NOTFOUND").replace("{nome}", nome));
					}
				} else {
					sender.sendMessage(SolaryEconomy.mensagens.get("MONEY_TOGGLED"));
				}
			} else {
				sender.sendMessage(SolaryEconomy.mensagens.get("NO_MONEY"));
			}

		} else {
			sender.sendMessage(this.getUsage());
		}

	}

}
