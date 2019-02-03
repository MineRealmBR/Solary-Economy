package nuvemplugins.solaryeconomy.commands.subcommands;

import org.bukkit.command.CommandSender;

import nuvemplugins.solaryeconomy.abstracts.SubCommand;
import nuvemplugins.solaryeconomy.app.SolaryEconomy;
import nuvemplugins.solaryeconomy.plugin.Economia;

public class SubCmdToggle extends SubCommand
{

	public SubCmdToggle(String command)
	{
		super("toggle", "§cUse: /" + command + " toggle", "solaryeconomy.commands.toggle");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		String toggle = Economia.toggle(sender.getName()) ? "OFF" : "ON";
		sender.sendMessage(SolaryEconomy.mensagens.get("MONEY_TOGGLE").replace("{toggle}", toggle));

	}

}
