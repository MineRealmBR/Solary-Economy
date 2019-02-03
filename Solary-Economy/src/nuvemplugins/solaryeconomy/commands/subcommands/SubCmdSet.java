package nuvemplugins.solaryeconomy.commands.subcommands;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;

import nuvemplugins.solaryeconomy.abstracts.SubCommand;
import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.plugin.Economia;

public class SubCmdSet extends SubCommand
{

	public SubCmdSet(String command)
	{
		super("set", "§cUse: /" + command + " set [jogador] [valor]", "solaryeconomy.commands.set", "definir", "setar");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (args.length >= 3) {
			String nome = args[1];
			BigDecimal valor = this.numbers.getDecimal(args[2]);

			if (valor.doubleValue() < 0) {
				sender.sendMessage(SolaryEconomy.mensagens.get("NUMBER_NULL"));
				return;
			}

			if (Economia.setBalance(nome, valor)) {
				sender.sendMessage(SolaryEconomy.mensagens.get("MONEY_SET").replace("{player}", nome).replace("{valor}",
						SolaryEconomy.numberFormat(valor)));
			} else {
				sender.sendMessage(SolaryEconomy.mensagens.get("PLAYER_NOTFOUND").replace("{nome}", nome));
			}

		} else {
			sender.sendMessage(this.getUsage());
		}

	}

}
