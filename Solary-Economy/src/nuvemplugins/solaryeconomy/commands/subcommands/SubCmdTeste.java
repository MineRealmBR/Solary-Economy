package nuvemplugins.solaryeconomy.commands.subcommands;

import org.bukkit.command.CommandSender;

import nuvemplugins.solaryeconomy.abstracts.SubCommand;

public class SubCmdTeste extends SubCommand
{

	public SubCmdTeste(String command)
	{
		super("teste", "§cUse: /" + command + "teste", "solaryeconomy.commands.teste", "testar");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		sender.sendMessage("§cModo teste temporariamente desativado :/");
	}

}
