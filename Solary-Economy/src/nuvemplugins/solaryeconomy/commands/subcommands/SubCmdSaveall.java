package nuvemplugins.solaryeconomy.commands.subcommands;

import org.bukkit.command.CommandSender;

import nuvemplugins.solaryeconomy.abstracts.SubCommand;
import nuvemplugins.solaryeconomy.plugin.Economia;

public class SubCmdSaveall extends SubCommand
{

	public SubCmdSaveall(String command)
	{
		super("saveall", "§cUse: /" + command + "saveall", "solaryeconomy.commands.saveall", "salvartudo");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		Economia.saveAll();
		sender.sendMessage("§eTodas as contas foram salvas com sucesso.");

	}

}
