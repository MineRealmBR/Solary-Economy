package nuvemplugins.solaryeconomy.commands.subcommands;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;

import nuvemplugins.solaryeconomy.abstracts.SubCommand;
import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.plugin.Economia;

public class SubCmdCriar extends SubCommand
{

	public SubCmdCriar(String command)
	{
		super("criar", "§cUse: /" + command + " criar [nome] [valor]", "solaryeconomy.commands.criar", "create", "new");
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

			if (Economia.createAccount(nome, valor)) {
				sender.sendMessage(SolaryEconomy.mensagens.get("ACCOUNT_CREATE").replace("{nome}", nome));
			} else {
				sender.sendMessage(SolaryEconomy.mensagens.get("ACCOUNT_EXISTS").replace("{nome}", nome));
			}

		} else {
			sender.sendMessage(this.getUsage());
		}

	}

}
