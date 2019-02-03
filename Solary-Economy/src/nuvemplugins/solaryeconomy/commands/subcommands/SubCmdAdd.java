package nuvemplugins.solaryeconomy.commands.subcommands;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;

import nuvemplugins.solaryeconomy.abstracts.SubCommand;
import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.plugin.Economia;

public class SubCmdAdd extends SubCommand
{

	public SubCmdAdd(String command)
	{
		super("add", "§cUse: /" + command + " add [jogador] [valor]", "solaryeconomy.commands.add", "give");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (args.length >= 3) {

			String nome = args[1];

			BigDecimal valor = this.numbers.getDecimal(args[2]);

			if (valor.doubleValue() <= 0) {
				sender.sendMessage(SolaryEconomy.mensagens.get("NUMBER_NULL"));
				return;
			}

			if (Economia.addBalance(nome, valor)) {
				sender.sendMessage(SolaryEconomy.mensagens.get("MONEY_ADD").replace("{player}", nome).replace("{valor}",
						SolaryEconomy.numberFormat(valor)));
			} else {
				sender.sendMessage(SolaryEconomy.mensagens.get("PLAYER_NOTFOUND").replace("{nome}", nome));
			}

		} else {
			sender.sendMessage(this.getUsage());
		}

	}

}
